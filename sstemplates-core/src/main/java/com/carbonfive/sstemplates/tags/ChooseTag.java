package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public class ChooseTag
  extends BaseTag
{
  protected boolean whenTagRendered;

  public String getTagName()
  {
    return "choose";
  }

  public void render(SsTemplateContext context) throws SsTemplateException
  {
    whenTagRendered = false;
    renderChildren(context);
  }

  boolean isWhenTagRendered()
  {
    return whenTagRendered;
  }

  void setWhenTagRendered(boolean whenTagRendered)
  {
    this.whenTagRendered = whenTagRendered;
  }
}
