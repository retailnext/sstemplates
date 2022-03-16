package com.carbonfive.sstemplates.tags;

import java.util.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class IncludeTag extends BaseTag
{
  protected String template = null;

  public String getTagName()
  {
    return "include";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( template == null )
      throw new SsTemplateException( "Include tag must have a template attribute" );
    String parsedTemplate = (String) parseExpression(template, String.class, context);

    Collection<SsTemplateTag> templateChildren = context.parseIncludeFile(parsedTemplate);
    renderChildren(context, templateChildren);
  }

  public void setTemplate(String template)
  {
    this.template = template;
  }

}
