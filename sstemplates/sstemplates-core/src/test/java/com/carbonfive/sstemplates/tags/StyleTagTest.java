package com.carbonfive.sstemplates.tags;

import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class StyleTagTest extends TagTestBase
{
  public StyleTagTest( String name )
  {
    super(name);
  }

  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("style_tag.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    RowTag rowTag = (RowTag) sheetTag.getChildTags().iterator().next();
    StyleTag styleTag = (StyleTag) rowTag.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    styleTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue( "Style tag rendered children", testTag.hasTagBeenRendered());
  }

  public void testAlignStyle() throws Exception
  {
    SsTemplateContext templateContext =
      renderWorkbook("style_align.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( HSSFCellStyle.ALIGN_CENTER, row.getCell((short) 0).getCellStyle().getAlignment() );
    assertEquals( HSSFCellStyle.ALIGN_CENTER_SELECTION, row.getCell((short) 1).getCellStyle().getAlignment() );
    assertEquals( HSSFCellStyle.ALIGN_FILL, row.getCell((short) 2).getCellStyle().getAlignment() );
    assertEquals( HSSFCellStyle.ALIGN_GENERAL, row.getCell((short) 3).getCellStyle().getAlignment() );
    assertEquals( HSSFCellStyle.ALIGN_LEFT, row.getCell((short) 4).getCellStyle().getAlignment() );
    assertEquals( HSSFCellStyle.ALIGN_RIGHT, row.getCell((short) 5).getCellStyle().getAlignment() );
  }

  public void testBorderStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_border.sst?bs=dash-dot&bs=dash-dot-dot&bs=dashed" +
                                                  "&bs=dotted&bs=double&bs=hair&bs=medium&bs=medium-dash-dot" +
                                                  "&bs=medium-dash-dot-dot&bs=medium-dashed&bs=none" +
                                                  "&bs=slanted-dash-dot&bs=thick&bs=thin");


    short[] borderStyles = new short[] { HSSFCellStyle.BORDER_DASH_DOT, HSSFCellStyle.BORDER_DASH_DOT_DOT,
                                         HSSFCellStyle.BORDER_DASHED, HSSFCellStyle.BORDER_DOTTED,
                                         HSSFCellStyle.BORDER_DOUBLE, HSSFCellStyle.BORDER_HAIR,
                                         HSSFCellStyle.BORDER_MEDIUM, HSSFCellStyle.BORDER_MEDIUM_DASH_DOT,
                                         HSSFCellStyle.BORDER_MEDIUM_DASH_DOT_DOT, HSSFCellStyle.BORDER_MEDIUM_DASHED,
                                         HSSFCellStyle.BORDER_NONE, HSSFCellStyle.BORDER_SLANTED_DASH_DOT,
                                         HSSFCellStyle.BORDER_THICK, HSSFCellStyle.BORDER_THIN };

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( short i=0; i < borderStyles.length; i++ )
      assertEquals( "cell 0," + i + " should have correct top border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderTop() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( short i=0; i < borderStyles.length; i++ )
      assertEquals( "cell 1," + i + " should have correct bottom border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderBottom() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( short i=0; i < borderStyles.length; i++ )
      assertEquals( "cell 2," + i + " should have correct right border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderRight() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( short i=0; i < borderStyles.length; i++ )
      assertEquals( "cell 3," + i + " should have correct left border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderLeft() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(4);
    for ( short i=0; i < borderStyles.length; i++ )
    {
      assertEquals( "cell 4," + i + " should have correct top border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderTop() );
      assertEquals( "cell 4," + i + " should have correct bottom border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderBottom() );
      assertEquals( "cell 4," + i + " should have correct right border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderRight() );
      assertEquals( "cell 4," + i + " should have correct left border", borderStyles[i],
                    row.getCell(i).getCellStyle().getBorderLeft() );
    }
  }

  public void testBorderColor() throws Exception
  {
    ArrayList colorClasses = getColorClasses();

    String params = createParamListFromColorClasses(colorClasses);

    SsTemplateContext templateContext = renderWorkbook("style_border_color.sst?" + params);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 0," + i + " should have correct top border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getTopBorderColor());

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 1," + i + " should have correct bottom border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getBottomBorderColor() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 2," + i + " should have correct right border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getRightBorderColor() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 3," + i + " should have correct left border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getLeftBorderColor() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(4);
    for ( short i = 0; i < colorClasses.size(); i++ )
    {
      assertColorEquals( "cell 4," + i + " should have correct top border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getTopBorderColor() );
      assertColorEquals( "cell 4," + i + " should have correct bottom border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getBottomBorderColor() );
      assertColorEquals( "cell 4," + i + " should have correct right border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getRightBorderColor() );
      assertColorEquals( "cell 4," + i + " should have correct left border color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getLeftBorderColor() );
    }
  }

  private void assertColorEquals(String message, HSSFWorkbook workbook, Class colorClass, short colorIndex)
    throws Exception
  {
    HSSFColor intendedColor = (HSSFColor) colorClass.getConstructor(null).newInstance(null);
    HSSFPalette palette = workbook.getCustomPalette();
    HSSFColor actualColor = palette.getColor(colorIndex);
    assertTrue(message + " Intended: " + intendedColor.getTriplet() + " Actual: " + actualColor.getTriplet(),
               Arrays.equals(intendedColor.getTriplet(), actualColor.getTriplet()));
  }

  private void assertColorEquals(String message, HSSFWorkbook workbook, short intendedIndex, short actualIndex)
    throws Exception
  {
    HSSFPalette palette = workbook.getCustomPalette();
    HSSFColor intendedColor = palette.getColor(intendedIndex);
    HSSFColor actualColor = palette.getColor(actualIndex);
    assertTrue(message + " Intended: " + intendedColor.getTriplet() + " Actual: " + actualColor.getTriplet(),
               Arrays.equals(intendedColor.getTriplet(), actualColor.getTriplet()));
  }

  public void testDataFormat() throws Exception
  {
    List formats = createTestFormats();
    Map attributes = new HashMap();
    attributes.put( "formats", formats );
    SsTemplateContext templateContext = renderWorkbook("style_data_format.sst", attributes);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    HSSFDataFormat dataFormat = templateContext.getWorkbook().createDataFormat();

    for ( short i=0; i < formats.size(); i++ )
    {
      assertEquals( "cell " + i + " data format should be correct.", dataFormat.getFormat((String) formats.get(i)),
                    row.getCell(i).getCellStyle().getDataFormat() );
    }
  }

/*
  public void testColors()
    throws Exception
  {
    ArrayList colorClasses = getColorClasses();
    String params = createParamListFromColorClasses(colorClasses);

    InvocationContext context = getTestInvocation("colors.sst?" + params);
    SsTemplateContext templateContext = renderWorkbook(context);

    HSSFWorkbook workbook = templateContext.getWorkbook();
    workbook.write(new FileOutputStream("colors.xls"));

  }
*/

  public void testBackgroundAndForegroundColors() throws Exception
  {
    ArrayList colorClasses = getColorClasses();
    String params = createParamListFromColorClasses(colorClasses);

    SsTemplateContext templateContext = renderWorkbook("style_colors.sst?" + params);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 0," + i + " should have correct foreground color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getFillForegroundColor() );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( short i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( "cell 1," + i + " should have correct background color",
                         templateContext.getWorkbook(), (Class) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getFillBackgroundColor() );
  }

  public void testFillPattern() throws Exception
  {
    short[] fillPatterns = new short[] {  HSSFCellStyle.ALT_BARS, HSSFCellStyle.BIG_SPOTS, HSSFCellStyle.BRICKS,
                                          HSSFCellStyle.DIAMONDS, HSSFCellStyle.FINE_DOTS, HSSFCellStyle.NO_FILL,
                                          HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.SPARSE_DOTS,
                                          HSSFCellStyle.SQUARES, HSSFCellStyle.THICK_BACKWARD_DIAG,
                                          HSSFCellStyle.THICK_FORWARD_DIAG, HSSFCellStyle.THICK_HORZ_BANDS,
                                          HSSFCellStyle.THICK_VERT_BANDS, HSSFCellStyle.THIN_BACKWARD_DIAG,
                                          HSSFCellStyle.THIN_FORWARD_DIAG, HSSFCellStyle.THIN_HORZ_BANDS,
                                          HSSFCellStyle.THIN_VERT_BANDS };

    String[] params = new String[] {  "alt-bars", "big-spots", "bricks", "diamonds", "fine-dots", "no-fill",
                                      "solid-foreground", "sparse-dots", "squares", "thick-backward-diag",
                                      "thick-forward-diag", "thick-horz-bands","thick-vert-bands", "thin-backward-diag",
                                      "thin-forward-diag", "thin-horz-bands", "thin-vert-bands" };
    String paramString = "";
    for (int i=0; i < params.length; i++ )
    {
      paramString += "pattern=" + params[i];
      if ( i < params.length+1 ) paramString += "&";
    }

    SsTemplateContext templateContext = renderWorkbook("style_fill.sst?" + paramString);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( short i=0; i < fillPatterns.length; i++ )
    {
      assertEquals( "column " + i + " should have " + params[i] + " pattern.", fillPatterns[i],
                    row.getCell(i).getCellStyle().getFillPattern() );
    }
  }

  public void testFlags() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_flags.sst?flag=false");
    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertFalse( "hidden should be false.", row.getCell((short) 0).getCellStyle().getHidden() );
    assertFalse( "locked should be false.", row.getCell((short) 0).getCellStyle().getLocked() );
    assertFalse( "hidden should be false.", row.getCell((short) 0).getCellStyle().getWrapText() );

    templateContext = renderWorkbook("style_flags.sst?flag=true");
    row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertTrue( "hidden should be true.", row.getCell((short) 0).getCellStyle().getHidden() );
    assertTrue( "locked should be true.", row.getCell((short) 0).getCellStyle().getLocked() );
    assertTrue( "hidden should be true.", row.getCell((short) 0).getCellStyle().getWrapText() );

  }

  public void testIndentationAndRotation() throws Exception
  {
    int indention = 15;
    SsTemplateContext templateContext = renderWorkbook("style_indent_rotate.sst?indention="+indention+"&rotation=16");
    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertEquals( "indention should be "+indention+".", indention, row.getCell((short) 0).getCellStyle().getIndention() );
    assertEquals( "rotation should be 16.", 16, row.getCell((short) 0).getCellStyle().getRotation() );
  }

  public void testVerticalAlignStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_valign.sst?valign1=bottom&valign2=center" +
                                                  "&valign3=justify&valign4=top");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( HSSFCellStyle.VERTICAL_BOTTOM, row.getCell((short) 0).getCellStyle().getVerticalAlignment() );
    assertEquals( HSSFCellStyle.VERTICAL_CENTER, row.getCell((short) 1).getCellStyle().getVerticalAlignment() );
    assertEquals( HSSFCellStyle.VERTICAL_JUSTIFY, row.getCell((short) 2).getCellStyle().getVerticalAlignment() );
    assertEquals( HSSFCellStyle.VERTICAL_TOP, row.getCell((short) 3).getCellStyle().getVerticalAlignment() );
  }

  public void testFontName() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_font_name.sst?fn=Arial&fn=Helvetica" +
                                                  "&fn=NONExistantFont");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "font name", "Arial", findFont(row, (short) 0, templateContext).getFontName() );
    assertEquals( "font name", "Helvetica", findFont(row, (short) 1, templateContext).getFontName() );
    assertEquals( "font name", "NONExistantFont", findFont(row, (short) 2, templateContext).getFontName() );
  }

  public void testFontHeightItalicAndStrikeout() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_font_style.sst?fontHeight=240&italic=true&strikeout=true");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "fontHeight", 240, findFont(row, 0, templateContext).getFontHeight() );
    assertTrue( "italic is true", findFont(row, 0, templateContext).getItalic() );
    assertTrue( "strikeout is true", findFont(row, 0, templateContext).getStrikeout() );

    templateContext = renderWorkbook("style_font_style.sst?fontHeight=180&italic=false&strikeout=false");

    row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "fontHeight", 180, findFont(row, 0, templateContext).getFontHeight() );
    assertFalse( "italic is false", findFont(row, 0, templateContext).getItalic() );
    assertFalse( "strikeout is false", findFont(row, 0, templateContext).getStrikeout() );
  }

  public void testFontEnumerationProperties() throws Exception
  {
    short[] typeOffsets = new short[] { HSSFFont.SS_NONE, HSSFFont.SS_SUB, HSSFFont.SS_SUPER };
    String[] toNames = new String[] { "none", "sub", "super" };
    short[] fontWeights = new short[] { HSSFFont.BOLDWEIGHT_BOLD, HSSFFont.BOLDWEIGHT_NORMAL };
    String[] fwNames = new String[] { "bold", "normal" };
    short[] fontColors  = new short[] { HSSFFont.COLOR_RED };
    String[] fcNames = new String[] { "red" };
    short[] underlines   = new short[] { HSSFFont.U_DOUBLE, HSSFFont.U_DOUBLE_ACCOUNTING, HSSFFont.U_NONE,
                                        HSSFFont.U_SINGLE, HSSFFont.U_SINGLE_ACCOUNTING };
    String[] uNames = new String[] { "double", "double-accounting", "none", "single", "single-accounting" };

    SsTemplateContext templateContext = renderWorkbook("style_font_properties.sst?"
                                                  + paramListFromArray("typeOffsets",toNames) + "&"
                                                  + paramListFromArray("fontWeights",fwNames) + "&"
                                                  + paramListFromArray("fontColors",fcNames) + "&"
                                                  + paramListFromArray("underlines",uNames) );

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( short i=0; i < typeOffsets.length; i++ )
    {
      assertEquals( "cell 0," + i + " should have correct typeOffset.", typeOffsets[i],
                    findFont(row, i, templateContext).getTypeOffset() );
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( short i=0; i < fontWeights.length; i++ )
    {
      assertEquals( "cell 1," + i + " should have correct fontWeight.", fontWeights[i],
                    findFont(row, i, templateContext).getBoldweight() );
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( short i=0; i < fontColors.length; i++ )
    {
      assertColorEquals( "cell 2," + i + " should have correct fontColor.", templateContext.getWorkbook(),
                         fontColors[i], findFont(row, i, templateContext).getColor() );
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( short i=0; i < underlines.length; i++ )
    {
      assertEquals( "cell 3," + i + " should have correct underline.", underlines[i],
                    findFont(row, i, templateContext).getUnderline() );
    }
  }

  public void testInheritance() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_inheritance.sst?alignment1=right&border1=thin" +
                                                  "&border2=thick&background2=aqua&fontHeight1=145" +
                                                  "&fontWeight1=normal&fontWeight2=bold");

    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell((short) 0).getCellStyle();
    assertEquals( "alignment should be inherited from parent style", HSSFCellStyle.ALIGN_RIGHT,
                  cellStyle.getAlignment());
    assertEquals( "border should be overide parent style", HSSFCellStyle.BORDER_THICK,
                  cellStyle.getBorderLeft());
    assertColorEquals( "background should not be inherited", templateContext.getWorkbook(), HSSFColor.AQUA.class,
                       cellStyle.getFillBackgroundColor() );

    HSSFFont font = templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex());
    assertEquals( "font height should be inherited from parent", (short) 145, font.getFontHeight() );
    assertEquals( "font weight should overide that of parent", HSSFFont.BOLDWEIGHT_BOLD, font.getBoldweight() );
  }

  public void testStyleCaching() throws Exception
  {
    HashMap params = new HashMap();
    params.put( "style1", "bob");
    params.put( "style2", "jeff");
    params.put( "style3", "harry");
    params.put( "border1", "thin");
    params.put( "fillPattern1", "diamonds");
    params.put( "fontHeight1", "240" );
    params.put( "fillPattern2", "bricks");
    params.put( "fontHeight2", "200" );
    params.put( "fontWeight2", "normal" );
    params.put( "fontHeight3", "222" );
    params.put( "fontWeight3", "bold" );
    params.put( "alignment3", "right" );

    SsTemplateContext templateContext = renderWorkbook("style_cache.sst?" + mapToParams(params));

    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell((short)0).getCellStyle();
    assertNotNull("Style named bob should not be null", cellStyle );
    assertEquals( "style's border should be thin", HSSFCellStyle.BORDER_THIN, cellStyle.getBorderTop());
    assertEquals( "style's fillPattern should be diamonds", HSSFCellStyle.DIAMONDS, cellStyle.getFillPattern() );
    assertEquals( "style's fontHeight should be 240", 240,
                  templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getFontHeight() );


    cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell((short)1).getCellStyle();
    assertNotNull("Style named 'bob jeff harry' should not be null", cellStyle );
    assertEquals( "style's border should be thin", HSSFCellStyle.BORDER_THIN, cellStyle.getBorderTop());
    assertEquals( "style's fillPattern should be diamonds", HSSFCellStyle.BRICKS, cellStyle.getFillPattern() );
    assertEquals( "style's fontHeight should be 240", 222,
                  templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getFontHeight() );
    assertEquals( "style's fontHeight should be 240", HSSFFont.BOLDWEIGHT_BOLD,
                  templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getBoldweight() );
    assertEquals( "style's alignment should be right", HSSFCellStyle.ALIGN_RIGHT, cellStyle.getAlignment());
  }

  public void testRowHeightAndColumnWidth() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_height_width.sst?rowHeight=300&columnWidth=20");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertEquals( "column width should be 20", 20, sheet.getColumnWidth((short) 0) );
    assertEquals( "row height should be 300", 300, sheet.getRow(0).getHeight() );
  }

  public void testAutoColumnWidth() throws Exception
  {
    String str1 = "short+str";
    String str2 = "A+very+long+string";
    String str3 = "Medium+String";
    SsTemplateContext templateContext = renderWorkbook("style_width_auto.sst?str1="+str1+"&str2="+str2+"&str3="+str3);

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertEquals( "column width should be 256*the length the longest string +2",
                  256*(Math.max(Math.max(str1.length(),str2.length()),str3.length())+2), sheet.getColumnWidth((short) 0) );
  }

  /* hssft file doesn't exist
  public void testSheetBackground() throws Exception
  {
    InvocationContext context = getTestInvocation("style_test18.sst");
    SsTemplateContext templateContext = renderWorkbook(context);
    templateContext.getWorkbook().write(new FileOutputStream("style_test18.xls"));
  }
  */

  private String paramListFromArray(String param, String[] values)
  {
    StringBuffer paramList = new StringBuffer();
    for (int i=0; i < values.length; i++)
    {
      paramList.append(param + "=" + values[i]);
      if ( i < values.length - 1 ) paramList.append("&");
    }
    return paramList.toString();
  }

  private HSSFFont findFont(HSSFRow row, int index, SsTemplateContext templateContext)
  {
    return templateContext.getWorkbook().getFontAt(row.getCell((short) index).getCellStyle().getFontIndex());
  }

  private String mapToParams( Map map )
  {
    String params = "";
    for (Iterator it = map.keySet().iterator(); it.hasNext();)
    {
      Object key = it.next();
      params += key.toString() + "=" + map.get(key).toString();
      if ( it.hasNext() ) params += "&";
    }
    return params;
  }

  private List createTestFormats()
  {
    List formats = HSSFDataFormat.getBuiltinFormats();
    // remove reserved formats
    for (Iterator it = formats.iterator(); it.hasNext();)
    {
      String format = (String) it.next();
      if ( format.startsWith("0x") )
        it.remove();
    }
    formats.add("0.000%");
    formats.add("yy.m.d.h.mm.ss");
    return formats;
  }

  private String createParamListFromColorClasses(ArrayList colorClasses)
  {
    StringBuffer params = new StringBuffer();
    for (Iterator it = colorClasses.iterator(); it.hasNext();)
    {
      Class colorClass = (Class) it.next();
      String className = colorClass.getName();
      int dollarIndex = className.lastIndexOf("$");
      if ( dollarIndex >= 0 ) className = className.substring(dollarIndex+1);
      className = className.toLowerCase();
      className = className.replace('_','-');
      params.append("c="+className);
      if ( it.hasNext() ) params.append('&');
    }
    return params.toString();
  }

  private ArrayList getColorClasses()
  {
    ArrayList colorClasses = new ArrayList();
    Class[] colorClassesArray = HSSFColor.class.getClasses();
    for ( int i=0; i < colorClassesArray.length; i++ )
    {
      if ( HSSFColor.class.isAssignableFrom(colorClassesArray[i]))
        colorClasses.add(colorClassesArray[i]);
    }
    return colorClasses;
  }

  private short getStaticShort(Class clazz, String field)
          throws IllegalAccessException, NoSuchFieldException
  {
    short x = clazz.getDeclaredField(field).getShort(null);
    return x;
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getCurrentStyle().length() == 0 )
      throw new SsTemplateException( "Context has no current style" );
  }
}
