package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.apache.commons.collections.*;

public class LoopFinder
  implements Serializable
{
  private static final Log log = LogFactory.getLog(LoopFinder.class);

  private static final int BUFFER_SIZE = 20;
  private static final int DEPTH_MAX   = 250;
  private static final int LOOP_MAX    = 50;

  private SequencedHashMap buffer       = new LRUMap();
  private Bag              bag          = new HashBag();
  private int              depth        = 0;
  private Map              possibles    = new HashMap();

  public void add(Class klass)
  {
    if (ignore(klass)) return;

    int distance = buffer.indexOf(klass);
    if (distance != -1)
    {
      String key = klass.getName() + " - " + distance;
      bag.add(key);
      if (bag.getCount(key) >= LOOP_MAX) possibles.put(key, new Integer(bag.getCount(key)));
    }

    buffer.put(klass, new Integer(depth));
    if (buffer.size() == BUFFER_SIZE + 1) buffer.remove(buffer.getLastKey());
  }

  private boolean ignore(Class klass)
  {
    if (klass.getPackage() == null) return false;
    if ("java.lang".equals(klass.getPackage().getName())) return true;
    return false;
  }

  public boolean isLoop()
  {
    return depth >= DEPTH_MAX;
  }

  public Map getPossibles()
  {
    return possibles;
  }

  public SequencedHashMap getBuffer()
  {
    return buffer;
  }

  public int getDepth()
  {
    return depth;
  }

  public void stepIn()
  {
    depth ++;
  }

  public void stepOut()
  {
    depth --;
  }
}
