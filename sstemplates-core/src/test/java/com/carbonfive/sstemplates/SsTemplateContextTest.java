package com.carbonfive.sstemplates;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.servlet.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Alex Cruikshank
 * @version $REVISION
 */
public class SsTemplateContextTest
    extends SsTemplateServletTestBase
{

  private MockHttpServletRequest request;

  @BeforeEach
  public void setUp() throws Exception
  {
    super.setUp();

    request = new MockHttpServletRequest(getServletContext());
    request.setMethod("POST");
  }

  @Test
  public void testFontCaching() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    SsTemplateContext context = getServlet().createTemplateContext(request);
    context.setWorkbook( new HSSFWorkbook() );

    String fontName = "Arial";
    short fontHeight = 240;
    short color = HSSFFont.COLOR_RED;
    boolean bold = true;
    boolean italic = true;
    boolean strikeout = true;
    byte underline = HSSFFont.U_DOUBLE;
    short typeOffset = 3;

    int initialFontCount = context.getWorkbook().getNumberOfFonts();
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    HSSFFont font = context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    assertNotNull( font, "context should have created a font" );
    assertEquals( initialFontCount+1,
                  context.getWorkbook().getNumberOfFonts(), "Only one font should have been added to the workbook" );
  }
}
