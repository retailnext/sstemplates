package com.carbonfive.sstemplates.tags;

import java.util.logging.*;
import com.carbonfive.sstemplates.servlet.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class TagTestBase extends SsTemplateTestBase
{
  protected static final Logger log = Logger.getLogger(SsTemplateServlet.class.getName());

  public TagTestBase(String s)
  {
    super(s);
  }

  public abstract void childRenderTest( SsTemplateContext context )
    throws SsTemplateException;

  protected class TestContextTag extends BaseTag
  {
    private boolean hasBeenRendered = false;
    private TagTestBase testObject = null;

    public TestContextTag( TagTestBase testObject )
    {
      this.testObject = testObject;
    }

    public boolean hasTagBeenRendered()
    {
      return hasBeenRendered;
    }

    public String getTagName()
    {
      return "sheetTagTest";
    }

    public void render( SsTemplateContext context ) throws SsTemplateException
    {
      testObject.childRenderTest(context);
      hasBeenRendered = true;
    }
  }
}
