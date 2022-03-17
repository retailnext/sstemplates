package com.carbonfive.sstemplates.tags;

import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import com.carbonfive.sstemplates.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class StyleTagTest extends TagTestBase
{
  @Test
  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("style_tag.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    RowTag rowTag = (RowTag) sheetTag.getChildTags().iterator().next();
    StyleTag styleTag = (StyleTag) rowTag.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    styleTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue( testTag.hasTagBeenRendered(), "Style tag rendered children" );
  }

  @Test
  public void testAlignStyle() throws Exception
  {
    SsTemplateContext templateContext =
      renderWorkbook("style_align.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( HorizontalAlignment.CENTER, row.getCell(0).getCellStyle().getAlignment() );
    assertEquals( HorizontalAlignment.CENTER_SELECTION, row.getCell(1).getCellStyle().getAlignment() );
    assertEquals( HorizontalAlignment.FILL, row.getCell(2).getCellStyle().getAlignment() );
    assertEquals( HorizontalAlignment.GENERAL, row.getCell(3).getCellStyle().getAlignment() );
    assertEquals( HorizontalAlignment.LEFT, row.getCell(4).getCellStyle().getAlignment() );
    assertEquals( HorizontalAlignment.RIGHT, row.getCell(5).getCellStyle().getAlignment() );
  }

  @Test
  public void testBorderStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_border.sst?bs=dash-dot&bs=dash-dot-dot&bs=dashed" +
                                                  "&bs=dotted&bs=double&bs=hair&bs=medium&bs=medium-dash-dot" +
                                                  "&bs=medium-dash-dot-dot&bs=medium-dashed&bs=none" +
                                                  "&bs=slanted-dash-dot&bs=thick&bs=thin");


    BorderStyle[] borderStyles = new BorderStyle[] { BorderStyle.DASH_DOT, BorderStyle.DASH_DOT_DOT,
                                         BorderStyle.DASHED, BorderStyle.DOTTED,
                                         BorderStyle.DOUBLE, BorderStyle.HAIR,
                                         BorderStyle.MEDIUM, BorderStyle.MEDIUM_DASH_DOT,
                                         BorderStyle.MEDIUM_DASH_DOT_DOT, BorderStyle.MEDIUM_DASHED,
                                         BorderStyle.NONE, BorderStyle.SLANTED_DASH_DOT,
                                         BorderStyle.THICK, BorderStyle.THIN };

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( int i=0; i < borderStyles.length; i++ )
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderTop(),
              "cell 0," + i + " should have correct top border" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( int i=0; i < borderStyles.length; i++ )
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderBottom(),
              "cell 1," + i + " should have correct bottom border" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( int i=0; i < borderStyles.length; i++ )
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderRight(),
              "cell 2," + i + " should have correct right border" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( int i=0; i < borderStyles.length; i++ )
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderLeft(),
              "cell 3," + i + " should have correct left border" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(4);
    for ( int i=0; i < borderStyles.length; i++ )
    {
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderTop(),
              "cell 4," + i + " should have correct top border" );
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderBottom(),
              "cell 4," + i + " should have correct bottom border" );
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderRight(),
              "cell 4," + i + " should have correct right border" );
      assertEquals( borderStyles[i], row.getCell(i).getCellStyle().getBorderLeft(),
              "cell 4," + i + " should have correct left border" );
    }
  }

  @Test
  public void testBorderColor() throws Exception
  {
    ArrayList<HSSFColorPredefined> colorClasses = getColorClasses();

    String params = createParamListFromColorClasses(colorClasses);

    SsTemplateContext templateContext = renderWorkbook("style_border_color.sst?" + params);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getTopBorderColor(),
                         "cell 0," + i + " should have correct top border color" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getBottomBorderColor(),
                         "cell 1," + i + " should have correct bottom border color" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getRightBorderColor(),
                         "cell 2," + i + " should have correct right border color" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getLeftBorderColor(),
                         "cell 3," + i + " should have correct left border color" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(4);
    for ( int i = 0; i < colorClasses.size(); i++ )
    {
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getTopBorderColor(),
                         "cell 4," + i + " should have correct top border color" );
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getBottomBorderColor(),
                         "cell 4," + i + " should have correct bottom border color" );
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getRightBorderColor(),
                         "cell 4," + i + " should have correct right border color" );
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getLeftBorderColor(),
                         "cell 4," + i + " should have correct left border color" );
    }
  }

  private void assertColorEquals(HSSFWorkbook workbook, HSSFColorPredefined intendedColor, short colorIndex, String message)
    throws Exception
  {
    HSSFPalette palette = workbook.getCustomPalette();
    HSSFColor actualColor = palette.getColor(colorIndex);
    assertTrue(Arrays.equals(intendedColor.getTriplet(), actualColor.getTriplet()),
            message + " Intended: " + intendedColor.getTriplet() + " Actual: " + actualColor.getTriplet());
  }

  private void assertColorEquals(HSSFWorkbook workbook, short intendedIndex, short actualIndex, String message)
    throws Exception
  {
    HSSFPalette palette = workbook.getCustomPalette();
    HSSFColor intendedColor = palette.getColor(intendedIndex);
    HSSFColor actualColor = palette.getColor(actualIndex);
    assertTrue(Arrays.equals(intendedColor.getTriplet(), actualColor.getTriplet()),
            message + " Intended: " + intendedColor.getTriplet() + " Actual: " + actualColor.getTriplet());
  }

  @Test
  public void testDataFormat() throws Exception
  {
    List<String> formats = createTestFormats();
    Map<String, List<String>> attributes = new HashMap<String, List<String>>();
    attributes.put( "formats", formats );
    SsTemplateContext templateContext = renderWorkbook("style_data_format.sst", attributes);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    HSSFDataFormat dataFormat = templateContext.getWorkbook().createDataFormat();

    for ( int i=0; i < formats.size(); i++ )
    {
      assertEquals( dataFormat.getFormat((String) formats.get(i)), row.getCell(i).getCellStyle().getDataFormat(),
              "cell " + i + " data format should be correct." );
    }
  }

  @Test
  public void testBackgroundAndForegroundColors() throws Exception
  {
    ArrayList<HSSFColorPredefined> colorClasses = getColorClasses();
    String params = createParamListFromColorClasses(colorClasses);

    SsTemplateContext templateContext = renderWorkbook("style_colors.sst?" + params);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getFillForegroundColor(),
                         "cell 0," + i + " should have correct foreground color" );

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( int i = 0; i < colorClasses.size(); i++ )
      assertColorEquals( templateContext.getWorkbook(), (HSSFColorPredefined) colorClasses.get(i),
                         row.getCell(i).getCellStyle().getFillBackgroundColor(),
                         "cell 1," + i + " should have correct background color" );
  }

  @Test
  public void testFillPattern() throws Exception
  {
    FillPatternType[] fillPatterns = new FillPatternType[] {  FillPatternType.ALT_BARS, FillPatternType.BIG_SPOTS, FillPatternType.BRICKS,
                                          FillPatternType.DIAMONDS, FillPatternType.FINE_DOTS, FillPatternType.NO_FILL,
                                          FillPatternType.SOLID_FOREGROUND, FillPatternType.SPARSE_DOTS,
                                          FillPatternType.SQUARES, FillPatternType.THICK_BACKWARD_DIAG,
                                          FillPatternType.THICK_FORWARD_DIAG, FillPatternType.THICK_HORZ_BANDS,
                                          FillPatternType.THICK_VERT_BANDS, FillPatternType.THIN_BACKWARD_DIAG,
                                          FillPatternType.THIN_FORWARD_DIAG, FillPatternType.THIN_HORZ_BANDS,
                                          FillPatternType.THIN_VERT_BANDS };

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
    for ( int i=0; i < fillPatterns.length; i++ )
    {
      assertEquals( fillPatterns[i], row.getCell(i).getCellStyle().getFillPattern(),
              "column " + i + " should have " + params[i] + " pattern." );
    }
  }

  @Test
  public void testFlags() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_flags.sst?flag=false");
    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertFalse( row.getCell(0).getCellStyle().getHidden(), "hidden should be false." );
    assertFalse( row.getCell(0).getCellStyle().getLocked(), "locked should be false." );
    assertFalse( row.getCell(0).getCellStyle().getWrapText(), "hidden should be false." );

    templateContext = renderWorkbook("style_flags.sst?flag=true");
    row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertTrue( row.getCell(0).getCellStyle().getHidden(), "hidden should be true." );
    assertTrue( row.getCell(0).getCellStyle().getLocked(), "locked should be true." );
    assertTrue( row.getCell(0).getCellStyle().getWrapText(), "hidden should be true." );

  }

  @Test
  public void testIndentationAndRotation() throws Exception
  {
    int indention = 15;
    SsTemplateContext templateContext = renderWorkbook("style_indent_rotate.sst?indention="+indention+"&rotation=16");
    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertEquals( indention, row.getCell(0).getCellStyle().getIndention(), "indention should be "+indention+"." );
    assertEquals( 16, row.getCell(0).getCellStyle().getRotation(), "rotation should be 16." );
  }

  @Test
  public void testVerticalAlignStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_valign.sst?valign1=bottom&valign2=center" +
                                                  "&valign3=justify&valign4=top");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( VerticalAlignment.BOTTOM, row.getCell(0).getCellStyle().getVerticalAlignment() );
    assertEquals( VerticalAlignment.CENTER, row.getCell(1).getCellStyle().getVerticalAlignment() );
    assertEquals( VerticalAlignment.JUSTIFY, row.getCell(2).getCellStyle().getVerticalAlignment() );
    assertEquals( VerticalAlignment.TOP, row.getCell(3).getCellStyle().getVerticalAlignment() );
  }

  @Test
  public void testFontName() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_font_name.sst?fn=Arial&fn=Helvetica" +
                                                  "&fn=NONExistantFont");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "Arial", findFont(row, 0, templateContext).getFontName(), "font name" );
    assertEquals( "Helvetica", findFont(row, 1, templateContext).getFontName(), "font name" );
    assertEquals( "NONExistantFont", findFont(row, 2, templateContext).getFontName(), "font name" );
  }

  @Test
  public void testFontHeightBoldItalicAndStrikeout() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_font_style.sst?fontHeight=240&italic=true&strikeout=true&bold=true");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( 240, findFont(row, 0, templateContext).getFontHeight(), "fontHeight" );
    assertTrue( findFont(row, 0, templateContext).getBold(), "bold is true" );
    assertTrue( findFont(row, 0, templateContext).getItalic(), "italic is true" );
    assertTrue( findFont(row, 0, templateContext).getStrikeout(), "strikeout is true" );

    templateContext = renderWorkbook("style_font_style.sst?fontHeight=180&italic=false&strikeout=false&bold=false");

    row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( 180, findFont(row, 0, templateContext).getFontHeight(), "fontHeight" );
    assertFalse( findFont(row, 0, templateContext).getBold(), "bold is false" );
    assertFalse( findFont(row, 0, templateContext).getItalic(), "italic is false" );
    assertFalse( findFont(row, 0, templateContext).getStrikeout(), "strikeout is false" );
  }

  @Test
  public void testFontEnumerationProperties() throws Exception
  {
    short[] typeOffsets = new short[] { HSSFFont.SS_NONE, HSSFFont.SS_SUB, HSSFFont.SS_SUPER };
    String[] toNames = new String[] { "none", "sub", "super" };
    boolean[] isBold = new boolean[] { true, false };
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
      assertEquals( typeOffsets[i], findFont(row, i, templateContext).getTypeOffset(),
              "cell 0," + i + " should have correct typeOffset." );
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(1);
    for ( short i=0; i < isBold.length; i++ )
    {
      assertEquals( isBold[i], findFont(row, i, templateContext).getBold(),
              "cell 1," + i + " should have correct fontWeight." );
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(2);
    for ( short i=0; i < fontColors.length; i++ )
    {
      assertColorEquals( templateContext.getWorkbook(), fontColors[i], findFont(row, i, templateContext).getColor(),
                         "cell 2," + i + " should have correct fontColor.");
    }

    row = templateContext.getWorkbook().getSheetAt(0).getRow(3);
    for ( short i=0; i < underlines.length; i++ )
    {
      assertEquals( underlines[i], findFont(row, i, templateContext).getUnderline(),
              "cell 3," + i + " should have correct underline." );
    }
  }

  @Test
  public void testInheritance() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_inheritance.sst?alignment1=right&border1=thin" +
                                                  "&border2=thick&background2=aqua&fontHeight1=145" +
                                                  "&fontWeight1=normal&fontWeight2=bold");

    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();
    assertEquals( HorizontalAlignment.RIGHT, cellStyle.getAlignment(),
            "alignment should be inherited from parent style" );
    assertEquals( BorderStyle.THICK, cellStyle.getBorderLeft(),
            "border should be overide parent style" );
    assertColorEquals( templateContext.getWorkbook(), HSSFColorPredefined.AQUA.getIndex(),
                       cellStyle.getFillBackgroundColor(), "background should not be inherited" );

    HSSFFont font = templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex());
    assertEquals( 145, font.getFontHeight(), "font height should be inherited from parent" );
    assertEquals( true, font.getBold(), "font weight should overide that of parent" );
  }

  @Test
  public void testStyleCaching() throws Exception
  {
    Map<String, String> params = new HashMap<String, String>();
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

    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();
    assertNotNull( cellStyle, "Style named bob should not be null" );
    assertEquals( BorderStyle.THIN, cellStyle.getBorderTop(), "style's border should be thin" );
    assertEquals( FillPatternType.DIAMONDS, cellStyle.getFillPattern(), "style's fillPattern should be diamonds" );
    assertEquals( 240, templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getFontHeight(),
            "style's fontHeight should be 240" );


    cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(1).getCellStyle();
    assertNotNull( cellStyle, "Style named 'bob jeff harry' should not be null");
    assertEquals( BorderStyle.THIN, cellStyle.getBorderTop(), "style's border should be thin" );
    assertEquals( FillPatternType.BRICKS, cellStyle.getFillPattern(), "style's fillPattern should be diamonds" );
    assertEquals( 222, templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getFontHeight(), "style's fontHeight should be 222" );
    assertEquals( true, templateContext.getWorkbook().getFontAt(cellStyle.getFontIndex()).getBold(), "style's font should be bold" );
    assertEquals( HorizontalAlignment.RIGHT, cellStyle.getAlignment(), "style's alignment should be right" );
  }

  @Test
  public void testRowHeightAndColumnWidth() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("style_height_width.sst?rowHeight=300&columnWidth=20");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertEquals( 20, sheet.getColumnWidth(0), "column width should be 20" );
    assertEquals( 300, sheet.getRow(0).getHeight(), "row height should be 300" );
  }

  @Test
  public void testAutoColumnWidth() throws Exception
  {
    String str1 = "short+str";
    String str2 = "A+very+long+string";
    String str3 = "Medium+String";
    SsTemplateContext templateContext = renderWorkbook("style_width_auto.sst?str1="+str1+"&str2="+str2+"&str3="+str3);

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertEquals( 256*(Math.max(Math.max(str1.length(),str2.length()),str3.length())+2), sheet.getColumnWidth(0),
            "column width should be 256*the length the longest string +2" );
  }

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
    return templateContext.getWorkbook().getFontAt(row.getCell(index).getCellStyle().getFontIndex());
  }

  private String mapToParams( Map<String, String> map )
  {
    String params = "";
    for (Iterator<String> it = map.keySet().iterator(); it.hasNext();)
    {
      Object key = it.next();
      params += key.toString() + "=" + map.get(key).toString();
      if ( it.hasNext() ) params += "&";
    }
    return params;
  }

  private List<String> createTestFormats()
  {
    List<String> formats = new ArrayList<String>(HSSFDataFormat.getBuiltinFormats());
    // remove reserved formats
    for (Iterator<String> it = formats.iterator(); it.hasNext();)
    {
      String format = it.next();
      if ( format.startsWith("0x") )
        it.remove();
    }
    formats.add("0.000%");
    formats.add("yy.m.d.h.mm.ss");
    return formats;
  }

  private String createParamListFromColorClasses(ArrayList<HSSFColorPredefined> colorClasses)
  {
    StringBuffer params = new StringBuffer();
    for (Iterator<HSSFColorPredefined> it = colorClasses.iterator(); it.hasNext();)
    {
      HSSFColorPredefined color = it.next();
      params.append("c="+color.name().toLowerCase().replace('_','-'));
      if ( it.hasNext() ) params.append('&');
    }
    return params.toString();
  }

  private ArrayList<HSSFColorPredefined> getColorClasses()
  {
    return new ArrayList<HSSFColorPredefined>(Arrays.asList(HSSFColorPredefined.values()));
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getCurrentStyle().length() == 0 )
      throw new SsTemplateException( "Context has no current style" );
  }
}