package com.carbonfive.sstemplates.tags;

import org.apache.commons.logging.*;
import com.carbonfive.sstemplates.*;

public class ChooseTag
  extends BaseTag
{
  private static final Log log = LogFactory.getLog(ChooseTag.class);

  public String getTagName()
  {
    return "choose";
  }

  public void render(SsTemplateContext context) throws SsTemplateException
  {
    renderChildren(context);
  }
}
