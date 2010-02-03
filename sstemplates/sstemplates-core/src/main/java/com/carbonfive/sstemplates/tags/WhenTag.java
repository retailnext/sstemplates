package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public class WhenTag extends BaseTag
{
  protected String test;

  public String getTagName()
  {
    return "when";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( test == null )
      throw new SsTemplateException( "When tag must have a 'test' attribute" );

    Boolean parsedTest = (Boolean) parseExpression(test, Boolean.class, context);

    if ( parsedTest.booleanValue() )
    {
      renderChildren(context);

      SsTemplateTag parent = getParentTag();
      if (! (parent instanceof ChooseTag))
        throw new SsTemplateException("'when' tag must be child of 'choose' tag");

      ((ChooseTag) parent).setWhenTagRendered(true);
    }
  }

  public void setTest(String test)
  {
    this.test = test;
  }
}
