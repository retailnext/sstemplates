package com.carbonfive.flash;

import java.util.*;

public class InfiniteLoopException extends ASTranslationException
{
  private LoopFinder finder = null;

  public InfiniteLoopException(LoopFinder finder)
  {
    super();
    this.finder = finder;
  }

  public LoopFinder getLoopFinder()
  {
    return finder;
  }

  public String getMessage()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("Infinite Loop detected at a recursion depth of: ").append(finder.getDepth())
       .append("\n\n  Last ").append(finder.getBuffer().size()).append(" classes inspected (with depth): ");

    Class key = null;
    for (Iterator i = finder.getBuffer().sequence().iterator(); i.hasNext(); )
    {
      key = (Class) i.next();
      buf.append("\n    ").append(finder.getBuffer().get(key)).append(" - ").append(key.getName());
    }

    buf.append("\n\n  Possible class - distance offenders: ");
    Map.Entry entry = null;
    for (Iterator i = finder.getPossibles().entrySet().iterator(); i.hasNext(); )
    {
      entry = (Map.Entry) i.next();
      buf.append("\n    ").append(entry.getKey()).append(" : ")
         .append(entry.getValue()).append(" instances");
    }

    buf.append("\n");

    return buf.toString();
  }
}
