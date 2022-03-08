package com.carbonfive.sstemplates.tags;

import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import com.carbonfive.sstemplates.hssf.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class CellTag extends BaseTag
{
  private String column = null;
  private String relativeColumn = null;
  private String contents = null;
  private String type = null;
  private String parsedType = null;
  private String colspan = null;
  private String rowspan = null;
  private String style = null;
  private String accumulator = null;
  private String paginate = null;

  public String getTagName()
  {
    return "cell";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getRow() == null )
      throw new SsTemplateException( "Cell tag must be within a row tag" );

    int columnIndex = context.getColumnIndex();
    if ( column != null )
    {
      columnIndex = ((Integer) parseExpression(column, Integer.class, context)).intValue();
      context.setColumnIndex( columnIndex );
    }
    else if ( relativeColumn != null )
    {
      columnIndex = columnIndex + ((Integer) parseExpression(relativeColumn, Integer.class, context)).intValue();
      context.setColumnIndex( columnIndex );
    }

    // need to reset row because paginated cells might have moved it around
    HSSFRow row = context.getRow();
    int rowIndex = context.getRowIndex();
    createCell(context);
    context.setRow(row);
    context.setRowIndex(rowIndex);

    // accumulator get set here because we don't want pagination to cause duplication
    if (accumulator != null)
    {
      HSSFCell cell = context.getRow().getCell((short) columnIndex);
      setCellAccumulator(cell, context, accumulator);
    }

    context.incrementCellIndex();
  }

  private void createCell(SsTemplateContext context)
      throws SsTemplateException
  {
    Region region = createRegion(context);

    if ((paginate == null) || !((Boolean) parseExpression(paginate, Boolean.class, context)).booleanValue())
    {
      createCell(context, region, true, true);
      return;
    }

    List regions = splitRegionForPagination(context, region);
    for (Iterator i = regions.iterator(); i.hasNext();)
    {
      Region sub = (Region)i.next();
      createCell(context, sub, sub.getRowFrom() == region.getRowFrom(), sub.getRowTo() == region.getRowTo());
    }
  }

  private List splitRegionForPagination(SsTemplateContext context, Region region)
  {
    List regions = new ArrayList();
    int rowFrom = region.getRowFrom();
    while (region.getRowTo() >= context.nextPageBreak(rowFrom))
    {
      regions.add(new Region(rowFrom, region.getColumnFrom(), context.nextPageBreak(rowFrom)-1, region.getColumnTo()));
      rowFrom = context.nextPageBreak(rowFrom);
    }
    regions.add(new Region(rowFrom, region.getColumnFrom(), region.getRowTo(), region.getColumnTo()));
    return regions;
  }

  private void createCell(SsTemplateContext context, Region region, boolean showTopBorder, boolean showBottomBorder)
      throws SsTemplateException
  {
    int rowIndex = region.getRowFrom();
    int columnIndex = region.getColumnFrom();

    context.setRowIndex( rowIndex );
    HSSFRow row = context.getSheet().getRow(rowIndex);
    if ( row == null ) row = context.getSheet().createRow(rowIndex);
    context.setRow(row);

    HSSFCell cell = context.getRow().getCell((short) columnIndex);
    if ( cell == null ) cell = context.getRow().createCell((short) columnIndex);

    cell.setCellType(findCellType(context));

    setCellContents(cell, context);

    setCellStyle(cell, context, showTopBorder, showBottomBorder);

    if (( colspan != null ) || ( rowspan != null ))
    {
      context.getSheet().addMergedRegion(region);
      createRegionBorders(region, cell, context);
    }
  }

  private void setCellAccumulator(HSSFCell cell, SsTemplateContext context, String accumulator)
    throws SsTemplateException
  {
    for (StringTokenizer tok = new StringTokenizer(accumulator, " "); tok.hasMoreTokens();)
    {
      HssfCellAccumulator acc = context.getNamedAccumulator((String)parseExpression(tok.nextToken(), String.class, context));
      acc.addCell(cell, context.getRowIndex(), context.getColumnIndex());
    }
  }

  private void setCellStyle(HSSFCell cell, SsTemplateContext context, boolean showTopBorder, boolean showBottomBorder)
          throws SsTemplateException
  {
    String styleName = context.getCurrentStyle();
    if ( style != null )
      styleName = appendStyles(styleName, (String) parseExpression(style,String.class,context));
    if (!showTopBorder)
      styleName = appendStyles(styleName, "_noTopBorder");
    if (!showBottomBorder)
      styleName = appendStyles(styleName, "_noBottomBorder");

    if ( styleName.length() > 0 )
    {
      cell.setCellStyle( context.getNamedStyle(styleName) );

      // set special parameters not contained in style
      HssfStyleData data = context.getNamedStyleData(styleName);
      if (( data.getAutoColumnWidth() ) && ( cell.getCellType() == HSSFCell.CELL_TYPE_STRING )
          && ( cell.getStringCellValue() != null ))
      {
        int width = context.getSheet().getColumnWidth((short)context.getColumnIndex());
        context.getSheet().setColumnWidth((short)context.getColumnIndex(),
                                          (short) Math.min(256*100,Math.max(width,256*(cell.getStringCellValue().length()+2))));
      }
      else if ( data.getColumnWidth() != null )
        context.getSheet().setColumnWidth((short) context.getColumnIndex(), data.getColumnWidth().shortValue() );

      if ( data.getRowHeight() != null )
        context.getRow().setHeight( data.getRowHeight().shortValue() );
    }
  }

  private String appendStyles(String styleName, String parsedStyle)
  {
    if ( styleName.length() > 0 )
      styleName += " " + parsedStyle;
    else
      styleName = parsedStyle;
    return styleName;
  }

  private Region createRegion(SsTemplateContext context)
          throws SsTemplateException
  {
    short parsedColspan = 1;
    int parsedRowspan = 1;
    Region region = new Region(context.getRowIndex(), (short) context.getColumnIndex(),
                               context.getRowIndex(), (short) context.getColumnIndex());

    if ( colspan != null )
    {
      parsedColspan = ((Integer) parseExpression(colspan,Integer.class,context)).shortValue();
      region.setColumnTo((short) (context.getColumnIndex() + parsedColspan - 1));
    }

    if ( rowspan != null )
    {
      parsedRowspan = parseInt(rowspan, context);
      region.setRowTo(context.getRowIndex() + parsedRowspan - 1);
    }

    return region;
  }

  private void createRegionBorders(Region region, HSSFCell cell, SsTemplateContext context)
          throws SsTemplateException
  {
    int parsedColspan = region.getColumnTo() - region.getColumnFrom() + 1;
    int parsedRowspan = region.getRowTo() - region.getRowFrom() + 1;

    HSSFCellStyle style = cell.getCellStyle();
    if (( style.getBorderTop() != HSSFCellStyle.BORDER_NONE )
            || ( style.getBorderBottom() != HSSFCellStyle.BORDER_NONE )
            || ( style.getBorderRight() != HSSFCellStyle.BORDER_NONE )
            || ( style.getBorderLeft() != HSSFCellStyle.BORDER_NONE ))
    {
      for (short i=0; i < parsedColspan; i++ )
      {
        for ( int j=0; j < parsedRowspan; j++ )
        {
          if (( i > 0 ) || ( j > 0 ))
          {
            boolean leftBorder = ( i == 0 ) && ( style.getBorderLeft() != HSSFCellStyle.BORDER_NONE );
            boolean rightBorder = ( i == parsedColspan-1 ) && ( style.getBorderRight() != HSSFCellStyle.BORDER_NONE );
            boolean topBorder = ( j == 0 ) && ( style.getBorderTop() != HSSFCellStyle.BORDER_NONE );
            boolean bottomBorder = ( j == parsedRowspan-1 ) && ( style.getBorderBottom() != HSSFCellStyle.BORDER_NONE );

            if ( leftBorder || rightBorder || topBorder || bottomBorder )
            {
              String styleName = "!!!regionBorder-" + (leftBorder ? "L" + style.getBorderLeft() : "")
                  + ( rightBorder ? "R" + style.getBorderRight() : "") + ( topBorder ? "T" + style.getBorderTop() : "")
                  + ( bottomBorder ? "B" + style.getBorderBottom() : "");
              HssfStyleData styleData = null;
              if ( ! context.hasCachedStyleData(styleName) )
              {
                styleData = new HssfStyleData();
                if ( topBorder )
                {
                  styleData.put("borderTop",new Integer(style.getBorderTop()));
                  styleData.put("topBorderColor",new Integer(style.getTopBorderColor()));
                }
                if ( bottomBorder )
                {
                  styleData.put("borderBottom",new Integer(style.getBorderBottom()));
                  styleData.put("bottomBorderColor",new Integer(style.getBottomBorderColor()));
                }
                if ( rightBorder )
                {
                  styleData.put("borderRight",new Integer(style.getBorderRight()));
                  styleData.put("rightBorderColor",new Integer(style.getRightBorderColor()));
                }
                if ( leftBorder )
                {
                  styleData.put("borderLeft",new Integer(style.getBorderLeft()));
                  styleData.put("leftBorderColor",new Integer(style.getLeftBorderColor()));
                }

                context.addStyleData(styleName,styleData);
              }

              HSSFRow row = context.getSheet().getRow( context.getRowIndex() + j );
              if ( row == null )
                row = context.getSheet().createRow( context.getRowIndex() + j );
              HSSFCell newCell = row.createCell( (short) (context.getColumnIndex() + i) );
              newCell.setCellStyle( context.getNamedStyle(styleName) );
            }
          }
        }
      }
    }
  }

  private void setCellContents(HSSFCell cell, SsTemplateContext context) throws SsTemplateException
  {
    if (( contents != null  ) && ( contents.length() != 0 ))
    {
      if ( cell.getCellType() == HSSFCell.CELL_TYPE_STRING )
        cell.setCellValue( new HSSFRichTextString( (String) parseExpression(contents, String.class, context) ) );
      else if ( cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC )
      {
        if ((parsedType != null) && parsedType.equals("date"))
          cell.setCellValue( (Date) parseExpression(contents, Date.class, context));
        else
          cell.setCellValue( ((Double) parseExpression(contents, Double.class, context)).doubleValue() );
      }
      else if ( cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN )
        cell.setCellValue( ((Boolean) parseExpression(contents, Boolean.class, context)).booleanValue() );
      else if ( cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA )
        cell.setCellFormula( (String) parseExpression(contents, String.class, context) );
      else if ( cell.getCellType() == HSSFCell.CELL_TYPE_BLANK )
        cell.setCellFormula(null);
    }
  }

  private int findCellType(SsTemplateContext context) throws SsTemplateException
  {
    int cellType = HSSFCell.CELL_TYPE_STRING;
    if (( type != null ) && ( type.length() > 0 ))
    {
      parsedType = ((String) parseExpression( type, String.class, context )).toLowerCase();
      if ( "blank".equals( parsedType ) )
        cellType = HSSFCell.CELL_TYPE_BLANK;
      else if ( "boolean".equals( parsedType ))
        cellType = HSSFCell.CELL_TYPE_BOOLEAN;
      else if ( "error".equals( parsedType ))
        cellType = HSSFCell.CELL_TYPE_ERROR;
      else if ( "formula".equals( parsedType ))
        cellType = HSSFCell.CELL_TYPE_FORMULA;
      else if ( "numeric".equals( parsedType ) || "date".equals( parsedType) )
        cellType = HSSFCell.CELL_TYPE_NUMERIC;
      else if ( "string".equals( parsedType ))
        cellType = HSSFCell.CELL_TYPE_STRING;
      else
        throw new SsTemplateException( "Invalid cell type: " + parsedType );
    }
    else if ((contents == null) || (contents.length() == 0))
    {
      cellType = HSSFCell.CELL_TYPE_BLANK;
    }
    return cellType;
  }

  public String getColumn()
  {
    return column;
  }

  public void setColumn(String column)
  {
    this.column = column;
  }

  public String getRelativeColumn()
  {
    return relativeColumn;
  }

  public void setRelativeColumn(String relativeColumn)
  {
    this.relativeColumn = relativeColumn;
  }

  public String getContents()
  {
    return contents;
  }

  public void setContents(String contents)
  {
    this.contents = contents;
    if ( this.contents != null )
      this.contents = this.contents.trim();
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getColspan()
  {
    return colspan;
  }

  public void setColspan(String colspan)
  {
    this.colspan = colspan;
  }

  public String getRowspan()
  {
    return rowspan;
  }

  public void setRowspan(String rowspan)
  {
    this.rowspan = rowspan;
  }

  public String getStyle()
  {
    return style;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public String getAccumulator()
  {
    return accumulator;
  }

  public void setAccumulator(String accumulator)
  {
    this.accumulator = accumulator;
  }

  public String getPaginate()
  {
    return paginate;
  }

  public void setPaginate(String paginate)
  {
    this.paginate = paginate;
  }
}
