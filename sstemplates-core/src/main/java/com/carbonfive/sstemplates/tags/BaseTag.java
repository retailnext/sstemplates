package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;
import org.apache.commons.el.*;

import javax.servlet.jsp.el.*;
import java.util.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class BaseTag implements SsTemplateTag
{
  List<SsTemplateTag> childTags = new ArrayList<SsTemplateTag>();
  SsTemplateTag parentTag = null;
  ExpressionEvaluatorImpl evaluator = new ExpressionEvaluatorImpl();

  protected void renderChildren( SsTemplateContext context )
    throws SsTemplateException
  {
    renderChildren(context, childTags);
  }

  protected void renderChildren(SsTemplateContext context, Collection<SsTemplateTag> children)
    throws SsTemplateException
  {
    for (Iterator<SsTemplateTag> it = children.iterator(); it.hasNext();)
    {
      SsTemplateTag tag = it.next();
      tag.render(context);
    }
  }

  public void addChildTag( SsTemplateTag tag )
  {
    childTags.add( tag );
    tag.setParentTag( this );
  }

  public SsTemplateTag getParentTag()
  {
    return parentTag;
  }

  public void setParentTag(SsTemplateTag tag)
  {
    parentTag = tag;
  }

  public List<SsTemplateTag> getChildTags()
  {
    return childTags;
  }

  public Object parseExpression( String expression, Class<?> expectedType, SsTemplateContext context )
    throws SsTemplateException
  {
    if (expression == null) return null;

    try
    {
      return evaluator.evaluate( expression, expectedType, context, context );
    }
    catch ( ELException ele )
    {
      throw new SsTemplateException( "Error parsing expression " + expression, ele );
    }
  }

  public Integer parseInteger( String expression, SsTemplateContext context )
    throws SsTemplateException
  {
    return (Integer) parseExpression(expression, Integer.class, context);
  }

  public int parseInt( String expression, SsTemplateContext context )
    throws SsTemplateException
  {
    return parseInteger(expression, context).intValue();
  }

}
