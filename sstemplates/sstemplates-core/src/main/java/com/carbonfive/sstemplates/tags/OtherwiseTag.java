package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public class OtherwiseTag extends BaseTag
{
  public String getTagName()
  {
    return "otherwise";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    SsTemplateTag parent = getParentTag();
    if (! (parent instanceof ChooseTag))
      throw new SsTemplateException("'otherwise' tag must be child of 'choose' tag");

    if (! ((ChooseTag) parent).isWhenTagRendered())
      renderChildren(context);
  }
}
