package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

/**
 * TODO: remove in favor of SetTag
 *
 * @deprecated Use SetTag instead.
 * @author sivoh
 * @version $REVISION
 */
public class DefineTag extends BaseTag
{
  private String name = null;
  private String value = null;

  public String getTagName()
  {
    return "define";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( name == null )
      throw new SsTemplateException( "Define tag must have a name attribute" );
    String parsedName = (String) parseExpression(name, String.class, context);

    if ( value == null )
      throw new SsTemplateException( "Define tag must have a value attribute" );
    Object parsedValue = (Object) parseExpression(value, Object.class, context);

    context.setPageVariable(parsedName, parsedValue);
    renderChildren(context);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }
}
