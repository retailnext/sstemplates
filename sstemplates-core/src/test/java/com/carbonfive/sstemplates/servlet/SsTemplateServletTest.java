package com.carbonfive.sstemplates.servlet;

import javax.servlet.http.*;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Alex Cruikshank
 * @version $REVISION
 */
public class SsTemplateServletTest
    extends SsTemplateServletTestBase
{
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  public void setUp() throws Exception
  {
    super.setUp();

    request = new MockHttpServletRequest(getServletContext());
    request.setMethod("POST");
    response = new MockHttpServletResponse();
  }

  @Test
  public void testFindDefaultHssfTemplateTags() throws Exception
  {
    List<Class<SsTemplateTag>> tags = getServlet().getProcessor().getTags();
    assertTrue( ! tags.isEmpty(), "Finds at least one template tag" );
    for (Iterator<Class<SsTemplateTag>> it = tags.iterator(); it.hasNext();)
    {
      Class<?> o = it.next();
      assertTrue( SsTemplateTag.class.isAssignableFrom(o), "all tags extend SsTemplateTag" );
    }
  }

  @Test
  public void test200() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    getServlet().service(request, response);

    assertEquals( HttpServletResponse.SC_OK, response.getStatus() );
  }

  @Test
  public void test404() throws Exception
  {

    request.setServletPath("/templates/notfound.sst");

    getServlet().service(request, response);

    assertEquals( HttpServletResponse.SC_NOT_FOUND, response.getStatus() );
  }

  @Test
  public void testInclude() throws Exception
  {
    request.setServletPath("/templates/servlet_include.sst");

    getServlet().service(request, response);
    assertEquals( HttpServletResponse.SC_OK, response.getStatus() );
  }

  @Test
  public void testParseSimpleTemplate() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag = getRenderTree(request);
    assertNotNull( workbookTag, "Should have parsed render tree from /test.templates/servlet_basic.sst" );
    assertTrue( workbookTag.getChildTags().size() > 0, "workbook should have at least one child tag" );
    assertTrue( workbookTag.getChildTags().get(0) instanceof SheetTag,
           "child of workbook " + workbookTag.getChildTags().get(0).getClass().getName() + " should be sheet" );

    assertEquals( "testSheet", ((SheetTag) workbookTag.getChildTags().get(0)).getName(), "sheet name should be 'testSheet'" );
  }

  @Test
  public void testEvaluateEL() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag = getRenderTree(request);
    SsTemplateContextImpl context = getHssfTemplateContext(request);

    request.setAttribute("var1",Integer.valueOf(14));
    request.setAttribute("var2",Integer.valueOf(21));

    String expression = "${ var1 + var2 }";
    assertEquals( 35, ((Integer) workbookTag.parseExpression(expression,Integer.class,context)).intValue(),
          "Expression " + expression + " should be 35");
  }

  @Test
  public void testRenderTreeCache() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag1 = getRenderTree(request);
    WorkbookTag workbookTag2 = getRenderTree(request);
    assertTrue( workbookTag1 == workbookTag2, "Retrieving render tree from same path twice should return same instance" );
  }
}
