package com.carbonfive.sstemplates.hssf;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.util.HashMap;
import java.util.Map;
import com.carbonfive.sstemplates.*;

/**
 * @author sivoh
 * @version $REVISION
 */
public class HssfStyleData
{
  public static final String COLUMN_WIDTH_ATTRIBUTE = "columnWidth";
  public static final String ROW_HEIGHT_ATTRIBUTE = "rowHeight";
  public static final String AUTO_COLUMN_WIDTH_ATTRIBUTE = "autoColumnWidth";

  private HashMap styleData = new HashMap();

  public HssfStyleData()
  {
    // do nothing
  }

  public HssfStyleData( HssfStyleData[] datas )
  {
    for ( int i=0; i < datas.length; i++ )
      datas[i].overideAttributes(styleData);
  }

  public void put( String attribute, Object value )
  {
    styleData.put( attribute, value );
  }

  public Object get( String attribute )
  {
    return styleData.get( attribute );
  }

  public boolean containsKey( String attribute )
  {
    return styleData.containsKey(attribute);
  }

  public void overideAttributes( Map originalStyle )
  {
    originalStyle.putAll( styleData );
  }

  public void setStyleAttributes( HSSFCellStyle style, SsTemplateContext context  )
    throws SsTemplateException
  {
    if ( styleData.containsKey("border") )
    {
      short borderStyle = ((Integer) styleData.get("border")).shortValue();
      style.setBorderTop(borderStyle);
      style.setBorderBottom(borderStyle);
      style.setBorderRight(borderStyle);
      style.setBorderLeft(borderStyle);
    }
    style.setBorderTop( shortFromStyleData("borderTop",style.getBorderTop()));
    style.setBorderBottom( shortFromStyleData("borderBottom",style.getBorderBottom()));
    style.setBorderRight( shortFromStyleData("borderRight",style.getBorderRight()));
    style.setBorderLeft( shortFromStyleData("borderLeft",style.getBorderLeft()));

    if ( styleData.containsKey("borderColor") )
    {
      short bc = ((Integer) styleData.get("borderColor")).shortValue();
      style.setTopBorderColor(bc);
      style.setBottomBorderColor(bc);
      style.setRightBorderColor(bc);
      style.setLeftBorderColor(bc);
    }

    style.setTopBorderColor( shortFromStyleData("topBorderColor",style.getTopBorderColor()));
    style.setBottomBorderColor( shortFromStyleData("bottomBorderColor",style.getBottomBorderColor()));
    style.setRightBorderColor( shortFromStyleData("rightBorderColor",style.getRightBorderColor()));
    style.setLeftBorderColor( shortFromStyleData("leftBorderColor",style.getLeftBorderColor()));

    // work around strange handling of default colors by setting foreground to black if background is set but foreground isn't
    short foreground = shortFromStyleData("foreground", style.getFillForegroundColor());
    short background = shortFromStyleData("background", style.getFillBackgroundColor());
    if (( foreground == HSSFColor.AUTOMATIC.index )
        && ( background != HSSFColor.AUTOMATIC.index )
        && (background != HSSFColor.AUTOMATIC.index+1))
      foreground = HSSFColor.BLACK.index;

    style.setFillForegroundColor(foreground);
    style.setFillBackgroundColor(background);

    if (!styleData.containsKey("foreground") && (context.getBackgroundColor() != null))
    {
      style.setFillForegroundColor(context.getColorIndex(context.getBackgroundColor()) );
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    style.setDataFormat( shortFromStyleData("dataFormat",style.getDataFormat()));

    style.setFillPattern( shortFromStyleData("fillPattern",style.getFillPattern()));

    style.setHidden( booleanFromStyleData("hidden",style.getHidden()));
    style.setLocked( booleanFromStyleData("locked",style.getLocked()));
    style.setWrapText( booleanFromStyleData("wrapText",style.getWrapText()));

    style.setIndention(shortFromStyleData("indention", style.getIndention()));
    style.setRotation(shortFromStyleData("rotation", style.getRotation()));

    style.setAlignment(shortFromStyleData("align", style.getAlignment()));
    style.setVerticalAlignment(shortFromStyleData("valign", style.getVerticalAlignment()));

    HSSFFont oldFont = context.getWorkbook().getFontAt(style.getFontIndex());
    String parsedFontName = stringFromStyleData("fontName", oldFont.getFontName());
    boolean parsedItalic = booleanFromStyleData("italic",oldFont.getItalic());
    boolean parsedStrikeout = booleanFromStyleData("strikeout",oldFont.getStrikeout());
    short parsedFontHeight = shortFromStyleData("fontHeight", oldFont.getFontHeight());
    short parsedFontColor = shortFromStyleData("fontColor", oldFont.getColor());
    short parsedFontWeight = shortFromStyleData("fontWeight", oldFont.getBoldweight());
    short parsedTypeOffset = shortFromStyleData("typeOffset", oldFont.getTypeOffset());
    byte parsedUnderline = (byte) shortFromStyleData("underline", oldFont.getUnderline());

    HSSFFont font = context.createFont(parsedFontName,parsedFontHeight,parsedFontColor,parsedFontWeight,
                                       parsedItalic,parsedStrikeout,parsedUnderline,parsedTypeOffset);
    style.setFont(font);
  }

  public Integer getColumnWidth()
  {
    return (Integer) styleData.get( COLUMN_WIDTH_ATTRIBUTE );
  }

  public Integer getRowHeight()
  {
    return (Integer) styleData.get( ROW_HEIGHT_ATTRIBUTE );
  }

  public boolean getAutoColumnWidth()
  {
    Boolean acw = (Boolean) styleData.get( AUTO_COLUMN_WIDTH_ATTRIBUTE );
    return ( acw != null ) && ( acw.booleanValue() );
  }

  private short shortFromStyleData(String attribute, short defaultValue)
  {
    short value = defaultValue;
    if ( styleData.containsKey(attribute) )
      value = ((Integer) styleData.get(attribute)).shortValue();
    return value;
  }

  private boolean booleanFromStyleData(String attribute, boolean defaultValue)
  {
    boolean value = defaultValue;
    if ( styleData.containsKey(attribute) )
      value = ((Boolean) styleData.get(attribute)).booleanValue();
    return value;
  }

  private String stringFromStyleData(String attribute, String defaultValue)
  {
    String value = defaultValue;
    if ( styleData.containsKey(attribute) )
      value = (String) styleData.get(attribute);
    return value;
  }

  public int hashCode()
  {
    return styleData.hashCode();
  }

  public boolean equals(Object obj)
  {
    return ((HssfStyleData)obj).styleData.equals(styleData);
  }
}
