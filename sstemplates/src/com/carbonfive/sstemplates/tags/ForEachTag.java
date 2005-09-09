package com.carbonfive.sstemplates.tags;

import java.util.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class ForEachTag extends BaseTag
{
  protected String var;
  protected String varStatus;
  protected String items;
  protected String begin = "0";
  protected String end;
  protected String step = "1";

  /**
   * @deprecated
   */
  protected String indexVariable;
  /**
   * @deprecated
   */
  protected String map;

  public String getTagName()
  {
    return "forEach";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    //if ( var == null )
    //  throw new SsTemplateException("ForEach tag must have a 'var' attribute");

    //if (items == null)
    //  throw new SsTemplateException("ForEach tag must have an 'items' attribute");

    String parsedVar = (String) parseExpression(var, String.class, context);
    String parsedVarStatus = (String) parseExpression(varStatus, String.class, context);
    Integer parsedBegin = (Integer) parseExpression(begin, Integer.class, context);
    Integer parsedEnd = (Integer) parseExpression(end, Integer.class, context);
    Integer parsedStep = (Integer) parseExpression(step, Integer.class, context);

    String parsedIndexVariable = (String) parseExpression(indexVariable, String.class, context);

    Object oldVar = context.getPageVariable(parsedVar);
    Object oldVarStatus = context.getPageVariable(parsedVarStatus);
    Object oldIndexVariable = context.getPageVariable(parsedIndexVariable);

    Collection c = ( items == null ? new InfiniteList() : findCollection(context) );
    if (end == null) parsedEnd = c.size();
    VarStatus varStatusObj = new VarStatus(c.size(), parsedStep, parsedBegin, parsedEnd);
    int i = 1;
    Iterator it = c.iterator();

    while (i <= parsedBegin && it.hasNext()) { it.next(); i++; }
    while (i <= parsedEnd && it.hasNext())
    {
      Object current = it.next();
      if (var != null) context.setPageVariable(parsedVar, current);
      if (varStatus != null) context.setPageVariable(parsedVarStatus, varStatusObj);
      if (indexVariable != null) context.setPageVariable(parsedIndexVariable, varStatusObj.getIndex());
      renderChildren(context);

      for (int s = 1; s < parsedStep && it.hasNext(); s++) { it.next(); i++; }
      varStatusObj.inc();
    }
    context.unsetPageVariable(parsedVar, oldVar);
    context.unsetPageVariable(parsedVarStatus, oldVarStatus);
    context.unsetPageVariable(parsedIndexVariable, oldIndexVariable);
  }

  private static class InfiniteList extends ArrayList
  {
    public Iterator iterator() { return new InfiniteIterator(); }
  }

  private static class InfiniteIterator implements Iterator
  {
    public boolean hasNext() { return true; }
    public void remove() { return; }
    public Object next() { return null; }
  }

  private Collection findCollection(SsTemplateContext context) throws SsTemplateException
  {
    Collection c = null;

    Object obj = parseExpression(items, Object.class, context);
    if (obj == null)
      throw new SsTemplateException("Cannot find '" + items + "' in the page context");

    if (obj instanceof Collection) return (Collection) obj;
    else if (obj instanceof Map)
    {
      if (map != null) return ((Map) obj).keySet(); // i don't like this -mike
      return ((Map) obj).entrySet();
    }
    else if (obj.getClass().isArray()) return Arrays.asList((Object[]) obj);
    else
      throw new SsTemplateException( "'" + items + "' does not resolve to a collection, map or array");

  }

  public void setVar(String var)
  {
    this.var = var;
  }

  /**
   * @deprecated
   */
  public void setVariable(String var)
  {
    setVar(var);
  }

  /**
   * @deprecated
   */
  public void setArray(String array)
  {
    setItems(array);
  }

  public void setItems(String items)
  {
    this.items = items;
  }

  /**
   * @deprecated
   */
  public void setCollection(String col)
  {
    setItems(col);
  }

  /**
   * @deprecated
   */
  public void setMap(String map)
  {
    setItems(map);
    this.map = map;
  }

  /**
   * @deprecated
   */
  public void setIndexVariable(String indexVariable)
  {
    this.indexVariable = indexVariable;
  }

  public void setVarStatus(String varStatus)
  {
    this.varStatus = varStatus;
  }

  public void setBegin(String begin)
  {
    this.begin = begin;
  }

  public void setEnd(String end)
  {
    this.end = end;
  }

  public void setStep(String step)
  {
    this.step = step;
  }

  public static class VarStatus
  {
    private int size;
    private int step;
    private int begin;
    private int end;
    private int index;
    private int count;

    public VarStatus(int size, int step, int begin, int end)
    {
      this.size = size;
      this.step = step;
      this.begin = begin;
      this.end = end;
      this.index = begin;
      this.count = 1;
    }

    protected void inc()
    {
      this.count ++;
      this.index += step;
    }

    public int getIndex()
    {
      return index;
    }

    public int getCount()
    {
      return count;
    }

    public boolean isFirst()
    {
      return index == begin;
    }

    public boolean isLast()
    {
      return index + step >= end || index + step >= size;
    }
  }
}
