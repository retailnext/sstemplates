package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.apache.commons.collections.*;

public class LoopFinder
  implements Serializable
{
  private static final Log log = LogFactory.getLog(LoopFinder.class);

  private static final int MAX_DEPTH = 10;
  private static final int LOOP_MAX  = 500;

  private LinkedList buffer       = new LinkedList();
  private Bag        bag          = new HashBag();
  private Class      loopingClass = null;;

  public void add(Class klass)
  {
    // if we have already found a loop, don't bother with this anymore
    if (isLoop()) return;

    if (! ignore(klass))
    {
      int distance = buffer.indexOf(klass);
      if (distance != -1)
      {
        String key = klass.getName() + "-" + distance;
        bag.add(key);
        if (bag.getCount(key) >= LOOP_MAX) loopingClass = klass;
      }
    }

    buffer.addFirst(klass);
    if (buffer.size() == MAX_DEPTH + 1) buffer.removeLast();
  }

  private boolean ignore(Class klass)
  {
    if (klass.getPackage() == null) return false;
    if ("java.lang".equals(klass.getPackage().getName())) return true;
    return false;
  }

  public boolean isLoop()
  {
    return loopingClass != null;
  }

  public Class getLoopingClass()
  {
    return loopingClass;
  }
}
