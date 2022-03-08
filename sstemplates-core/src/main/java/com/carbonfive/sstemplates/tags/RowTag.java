package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class RowTag extends BaseTag
{
  private String index = null;
  private String relativeIndex = null;
  private String style = null;

  public String getTagName()
  {
    return "row";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getSheet() == null )
      throw new SsTemplateException( "Row tag must be within a sheet tag" );

    int rowIndex = context.getRowIndex();
    if ( index != null )
    {
      rowIndex = ((Integer) parseExpression(index, Integer.class, context)).intValue();
      context.setRowIndex( rowIndex );
    }
    else if ( relativeIndex != null )
    {
      rowIndex = rowIndex + ((Integer) parseExpression(relativeIndex, Integer.class, context)).intValue();
      context.setRowIndex( rowIndex );
    }

    HSSFRow row = context.getSheet().getRow(rowIndex);
    if ( row == null )
      row = context.getSheet().createRow(rowIndex);


    String oldStyleName = context.getCurrentStyle();
    context.setCurrentStyle(parseCurrentStyle(oldStyleName, context));

    context.setRow(row);
    renderChildren(context);

    // add empty cell to fix strange google docs behavior that drops trailing column if columns are even
    if ( context.getColumnIndex() % 2 == 0 )
      new CellTag().render(context);

    context.setRow(null);

    context.setCurrentStyle(oldStyleName);

    context.incrementRowIndex();
  }

  private String parseCurrentStyle(String oldStyleName, SsTemplateContext context)
          throws SsTemplateException
  {
    String styleName = oldStyleName;
    if ( getStyle() != null )
    {
      String parsedStyle = (String) parseExpression(getStyle(),String.class,context);
      styleName = ( styleName.length() > 0 ) ? styleName + " " + parsedStyle : parsedStyle;
    }
    return styleName;
  }

  public String getIndex()
  {
    return index;
  }

  public void setIndex(String index)
  {
    this.index = index;
  }

  public String getStyle()
  {
    return style;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public String getRelativeIndex()
  {
    return relativeIndex;
  }

  public void setRelativeIndex(String relativeIndex)
  {
    this.relativeIndex = relativeIndex;
  }
}
