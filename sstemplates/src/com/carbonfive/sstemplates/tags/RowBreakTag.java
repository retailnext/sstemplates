package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public class RowBreakTag extends BaseTag
{
  private String condition = null;

  public String getTagName()
  {
    return "rowbreak";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    context.getSheet().setRowBreak(context.getRowIndex() - 1);
  }

  public String getCondition()
  {
    return condition;
  }

  public void setCondition(String condition)
  {
    this.condition = condition;
  }

}
