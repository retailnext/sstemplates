package com.carbonfive.sstemplates.servlet;

import java.util.*;
import javax.servlet.http.*;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;

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

  public void setUp() throws Exception
  {
    super.setUp();

    request = new MockHttpServletRequest(getServletContext());
    request.setMethod("POST");
    response = new MockHttpServletResponse();
  }

  public void testFindDefaultHssfTemplateTags() throws Exception
  {
    List tags = getServlet().getProcessor().getHssfTemplateTags();
    assertTrue( "Finds at least one template tag", ! tags.isEmpty() );

    for (Iterator it = tags.iterator(); it.hasNext();)
    {
      Class o = (Class) it.next();
      assertTrue( "all tags extend SsTemplateTag", SsTemplateTag.class.isAssignableFrom(o) );
    }
  }

  public void test200() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    getServlet().service(request, response);

    assertEquals( HttpServletResponse.SC_OK, response.getStatus() );
  }

  public void test404() throws Exception
  {

    request.setServletPath("/templates/notfound.sst");

    getServlet().service(request, response);

    assertEquals( HttpServletResponse.SC_NOT_FOUND, response.getStatus() );
  }

  public void testInclude() throws Exception
  {
    request.setServletPath("/templates/servlet_include.sst");

    getServlet().service(request, response);
    assertEquals( HttpServletResponse.SC_OK, response.getStatus() );
  }

  public void testParseSimpleTemplate() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag = getRenderTree(request);
    assertNotNull( "Should have parsed render tree from /templates/servlet_basic.sst", workbookTag );
    assertTrue( "workbook should have at least one child tag", workbookTag.getChildTags().size() > 0 );
    assertTrue( "child of workbook " + workbookTag.getChildTags().get(0).getClass().getName() + " should be sheet",
                workbookTag.getChildTags().get(0) instanceof SheetTag );

    assertEquals("sheet name should be 'testSheet'", "testSheet",
                 ((SheetTag) workbookTag.getChildTags().get(0)).getName() );
  }

  public void testEvaluateEL() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag = getRenderTree(request);
    SsTemplateContextImpl context = getHssfTemplateContext(request);

    request.setAttribute("var1",new Integer(14));
    request.setAttribute("var2",new Integer(21));

    String expression = "${ var1 + var2 }";
    assertEquals( "Expression " + expression + " should be 35", 35,
                  ((Integer) workbookTag.parseExpression(expression,Integer.class,context)).intValue());
  }

  public void testRenderTreeCache() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    WorkbookTag workbookTag1 = getRenderTree(request);
    WorkbookTag workbookTag2 = getRenderTree(request);
    assertTrue( "Retrieving render tree from same path twice should return same instance",
                workbookTag1 == workbookTag2 );
  }
}
