package com.carbonfive.sstemplates;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.apache.commons.lang3.builder.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import com.carbonfive.sstemplates.hssf.*;
import com.carbonfive.sstemplates.tags.SsTemplateTag;

/**
 * This class acts as an EL VariableResolver, but does not support the pageContext implicit object.
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateContextImpl
    implements SsTemplateContext
{
  private static final String UNNAMED_STYLE_PREFIX = "!!!UNNAMED";

  private Map<String, Object>  pageScope       = new HashMap<String, Object>();
  private Map<FontKey, HSSFFont> fontCache       = new HashMap<FontKey, HSSFFont>();
  private Map<String, HssfStyleData> styleDataCache  = new HashMap<String, HssfStyleData>();
  private Map<HssfStyleData, String> styleDataInverseCache  = new HashMap<HssfStyleData, String>();
  private Map<String, CachedStyle> styleCache   = new HashMap<String, CachedStyle>();
  private Map<String, HssfCellAccumulator> accumulatorCache = new HashMap<String, HssfCellAccumulator>();
  private Map<String, Method>   functions       = new HashMap<String, Method>();
  private Map<Object, Object>   customValues    = new HashMap<Object, Object>();
  private HSSFWorkbook          workbook        = null;
  private HSSFSheet             sheet           = null;
  private HSSFRow               row             = null;
  private String                currentStyle    = "";
  private int                   rowIndex        = 0;
  private int                   columnIndex     = 0;
  private int                   maxRowIndex     = Integer.MIN_VALUE;
  private int                   maxColumnIndex  = Integer.MIN_VALUE;
  private int                   styleCount      = 0;
  private int                   firstPageBreak  = 0;
  private int                   nextPageBreak   = 0;

  private SsTemplateProcessor processor       = null;
  protected File                templateDir     = null;
  private short[]               backgroundColor = null;

  public SsTemplateContextImpl(SsTemplateProcessor processor, File templateDir)
  {
    this.processor = processor;
    this.templateDir = templateDir;
    initStyles();
  }

  public SsTemplateContextImpl(SsTemplateProcessor processor, File templateDir, Map<String, Object> context)
  {
    this(processor, templateDir);
    pageScope = new HashMap<String, Object>(context);
  }

  private void initStyles()
  {
    HssfStyleData noTopBorder = new HssfStyleData();
    noTopBorder.put("borderTop", Integer.valueOf(BorderStyle.NONE.getCode()));
    addStyleData( "_noTopBorder", noTopBorder );

    HssfStyleData noBottomBorder = new HssfStyleData();
    noBottomBorder.put("borderBottom", Integer.valueOf(BorderStyle.NONE.getCode()));
    addStyleData( "_noBottomBorder", noBottomBorder );
  }

  public Collection<SsTemplateTag> parseIncludeFile(String path)
    throws SsTemplateException
  {
    return processor.parseIncludeFile(findFileInTemplateDirectory(path));
  }

  public File findFileInTemplateDirectory(String path)
  {
    String file = ( path == null ? "" : path );
    if ( file.startsWith("/") ) file = file.substring(1);

    return new File(templateDir, file);
  }

  public Object setPageVariable( String key, Object value )
  {
    return pageScope.put(key,value);
  }

  public void unsetPageVariable( String key, Object oldValue )
  {
    if ( oldValue != null )
      pageScope.put(key,oldValue);
    else
      pageScope.remove(key);
  }

  public Object getPageVariable( String key )
  {
    return pageScope.get(key);
  }

  public Object resolveVariable( String name )
  {
    if ( "pageScope".equals( name ) )
      return pageScope;

    if ( "accumulator".equals( name ) )
      return accumulatorCache;

    // otherwise, try to find the name in page, request, session, then application scope
    if ( pageScope.containsKey( name ) )
      return pageScope.get(name);

    return null;
  }

  public HSSFFont createFont( String name, short fontHeight, short color, boolean bold, boolean italic,
                              boolean strikeout, byte underline, short typeOffset )
  {
    FontKey fontKey = new FontKey(name,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    HSSFFont font = fontCache.get( fontKey );
    if ( font == null )
    {
      font = workbook.createFont();
      fontKey.setFontProperties( font );
      fontCache.put( fontKey, font );
    }
    return font;
  }

  public void incrementCellIndex()
  {
    setColumnIndex(columnIndex + 1);
    for ( CellRangeAddress region = getRegionForCurrentLocation(); region != null; region = getRegionForCurrentLocation() )
    {
      if (( columnIndex != region.getFirstColumn() ) || ( rowIndex != region.getFirstRow() ))
        setColumnIndex(region.getLastColumn() + 1);
    }
  }

  public void incrementRowIndex()
  {
    setRowIndex(rowIndex + 1);
    setColumnIndex(-1);
    incrementCellIndex();
  }

  public CellRangeAddress getRegionForCurrentLocation()
  {
    for (int i=0; i < sheet.getNumMergedRegions(); i++ )
    {
      if ( rangeContains( sheet.getMergedRegion(i), rowIndex, columnIndex ) )
        return sheet.getMergedRegion(i);
    }
    return null;
  }

  private boolean rangeContains( CellRangeAddress range, int row, int column )
  {
    return ( range.isFullColumnRange() || ( range.getFirstColumn() <= column && range.getLastColumn() >= column ))
        && ( range.isFullRowRange()    || ( range.getFirstRow()    <= row    && range.getLastRow()    >= row    ));
  }

  private String getUniqueStyleName()
  {
    return UNNAMED_STYLE_PREFIX + (styleCount++);
  }

  public String addStyleData( String name, HssfStyleData data )
  {
    if (name == null)
    {
      String previous = (String) styleDataInverseCache.get(data);
      if (previous != null) return previous;
      name = getUniqueStyleName();
    }

    styleDataCache.put( name, data );
    styleDataInverseCache.put ( data, name );
    return name;
  }

  public HSSFCellStyle getNamedStyle( String name )
    throws SsTemplateException
  {
    return getCachedStyleFromName(name).style;
  }

  public HssfStyleData getNamedStyleData( String name )
    throws SsTemplateException
  {
    return getCachedStyleFromName(name).styleData;
  }

  private CachedStyle getCachedStyleFromName(String name)
          throws SsTemplateException
  {
    CachedStyle cachedStyle = styleCache.get( name );
    if ( cachedStyle == null )
    {
      StringTokenizer st = new StringTokenizer(name, " ", false);
      HssfStyleData[] datas = new HssfStyleData[ st.countTokens() ];
      for (int i=0; i < datas.length; i++ )
      {
        String token = st.nextToken();
        datas[i] = styleDataCache.get( token );
        if ( datas[i] == null )
          throw new SsTemplateException( "Error retrieving undefined style " + token );
      }

      cachedStyle = new CachedStyle( new HssfStyleData( datas ), workbook.createCellStyle() );
      cachedStyle.styleData.setStyleAttributes( cachedStyle.style, this );
      styleCache.put( name, cachedStyle );
    }
    return cachedStyle;
  }

  public boolean hasCachedStyleData(String name)
  {
    return styleDataCache.containsKey( name );
  }

  public void setPageBreaks(int firstPageBreak, int nextPageBreak)
  {
    this.firstPageBreak = firstPageBreak;
    this.nextPageBreak = nextPageBreak;
  }

  public HSSFWorkbook getWorkbook()
  {
    return workbook;
  }

  public void setWorkbook(HSSFWorkbook workbook)
  {
    this.workbook = workbook;
  }

  public HSSFSheet getSheet()
  {
    return sheet;
  }

  public void setSheet(HSSFSheet sheet)
  {
    this.sheet = sheet;
  }

  public HSSFRow getRow()
  {
    return row;
  }

  public void setRow(HSSFRow row)
  {
    this.row = row;
  }

  public int getRowIndex()
  {
    return rowIndex;
  }

  public void setRowIndex(int rowIndex)
  {
    if (rowIndex > maxRowIndex) maxRowIndex = rowIndex;
    this.rowIndex = rowIndex;
  }

  public int getColumnIndex()
  {
    return columnIndex;
  }

  public void setColumnIndex(int columnIndex)
  {
    if (columnIndex > maxColumnIndex) maxColumnIndex = columnIndex;
    this.columnIndex = columnIndex;
  }

  public String getCurrentStyle()
  {
    return currentStyle;
  }

  public void setCurrentStyle(String currentStyle)
  {
    this.currentStyle = currentStyle;
  }

  public HssfCellAccumulator getNamedAccumulator(String name)
  {
    HssfCellAccumulator acc = accumulatorCache.get(name);

    if (acc == null)
    {
      acc = new HssfCellAccumulator();
      accumulatorCache.put(name, acc);
    }

    return acc;
  }

  public void registerMethod(String name, Method m)
  {
    functions.put(name, m);
  }

  // no prefix support
  public Method resolveFunction(String prefix, String name)
  {
    return functions.get(name);
  }

  public Object getCustomValue(Object key)
  {
    return customValues.get(key);
  }

  public void setCustomValue(Object key, Object value)
  {
    customValues.put(key, value);
  }

  public static final short firstColorIndex = 0xa;
  public static final short lastColorIndex = 0x40;
  private short currentColorIndex = firstColorIndex;
  private Map<Color, Short> colorMap = new HashMap<Color, Short>();
  private HSSFPalette palette;

  public short getColorIndex(short[] triplet)
    throws SsTemplateException
  {
    Color color = new Color(triplet);
    Short index = colorMap.get(color);

    if (index == null)
    {
      if (currentColorIndex > lastColorIndex)
        throw new SsTemplateException("Too many colors - not enough room in palette!");

      if (palette == null) palette = workbook.getCustomPalette();

      palette.setColorAtIndex(currentColorIndex, (byte)triplet[0], (byte)triplet[1], (byte)triplet[2]);
      index = Short.valueOf(currentColorIndex);
      colorMap.put(color, index);
      currentColorIndex++;
    }

    return index.shortValue();
  }

  public int getMaxRowIndex()
  {
    return maxRowIndex;
  }

  public int getMaxColumnIndex()
  {
    return maxColumnIndex;
  }

  public void setBackgroundColor(short[] triplet)
  {
    this.backgroundColor = triplet;
  }

  public short[] getBackgroundColor()
  {
    return backgroundColor;
  }

  public int nextPageBreak(int row)
  {
    if ((firstPageBreak <= 0) || (nextPageBreak <= 0)) return Short.MAX_VALUE;
    if (row < firstPageBreak) return firstPageBreak;
    return firstPageBreak + ((row - firstPageBreak)/nextPageBreak + 1)*nextPageBreak;
  }

  private class Color
  {
    private short[] triplet;

    public Color(short[] triplet)
    {
      this.triplet = triplet;
    }

    public int hashCode()
    {
      return triplet[0]*256*256 + triplet[1]*256 + triplet[2];
    }

    public boolean equals(Object obj)
    {
      return Arrays.equals(triplet, ((Color)obj).triplet);
    }
  }

  private class FontKey
  {
    String name = null;
    short fontHeight, color, typeOffset;
    boolean bold, italic, strikeout;
    byte underline;
    public FontKey( String name, short fontHeight, short color, boolean bold, boolean italic,
                    boolean strikeout, byte underline, short typeOffset )
    {
      this.name = name;
      this.fontHeight = fontHeight;
      this.color = color;
      this.bold = bold;
      this.italic = italic;
      this.strikeout = strikeout;
      this.underline = underline;
      this.typeOffset = typeOffset;
    }

    public boolean equals( Object other )
    {
      if (( other == null ) || ( ! (other instanceof FontKey) )) return false;
      FontKey font = (FontKey) other;
      return (new EqualsBuilder()).append(name,font.name).append(fontHeight,font.fontHeight).append(color,font.color)
              .append(bold,font.bold).append(italic,font.italic).append(strikeout,font.strikeout)
              .append(underline,font.underline).append(typeOffset,font.typeOffset).isEquals();
    }

    public int hashCode()
    {
      return (new HashCodeBuilder()).append(name).append(fontHeight).append(color).append(bold)
              .append(italic).append(strikeout).append(underline).append(typeOffset).toHashCode();
    }

    public void setFontProperties( HSSFFont font )
    {
      font.setFontName(name);
      font.setFontHeight(fontHeight);
      font.setColor(color);
      font.setBold(bold);
      font.setItalic(italic);
      font.setStrikeout(strikeout);
      font.setUnderline(underline);
      font.setTypeOffset(typeOffset);
    }
  }

  private class CachedStyle
  {
    public HssfStyleData styleData;
    public HSSFCellStyle style;
    public CachedStyle( HssfStyleData styleData, HSSFCellStyle style )
    {
      this.styleData = styleData;
      this.style = style;
    }
  }
}
