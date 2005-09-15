package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

/**
 * TODO: remove WhileTag in favor of ForEachTag
 *
 * @author sivoh
 * @version $REVISION
 */
public class WhileTag extends BaseTag
{
  protected String test = null;

  public String getTagName()
  {
    return "while";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( test == null )
      throw new SsTemplateException( "While tag must have a 'test' attribute" );

    while ( ((Boolean) parseExpression(test, Boolean.class, context)).booleanValue() )
      renderChildren(context);
  }

  public void setTest(String test)
  {
    this.test = test;
  }
}
