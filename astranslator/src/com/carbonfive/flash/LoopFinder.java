package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.apache.commons.collections.*;

/**
 * LoopFinder detects infinite loops during translation to Action Script.  Specific encoders notify the loop finder
 * when they are recursing down an object hierarchy, and when they are coming out of that recursion.  At a specific
 * maximum depth (250), an <code>InfiniteLoopException</code> is thrown.
 * <p>
 * This class also keeps a buffer of the last 20 classes encoded, as well as a list of "possibles".  The list of
 * possibles is a collection of classes that appear in a specific pattern with themselves during the translation.
 * For example, if during translation a <code>Player</code> object appears two encodings away from another
 * <code>Player</code> object 50 times or more, it is listed as a "possible".
 * <p>
 * When an <code>InfiniteLoopException</code> is generated, both the buffer of recently encoded classes and the list
 * of possibles is given to it.  Both of these are output when <code>InfiniteLoopException.getMessage()</code>
 * is called.
 */
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

  /**
   * Notify LoopFinder that a specific class is being encoded.
   * @param klass The class being encoded
   */
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

  /**
   * Indicates whether an infinite loop has been found or not.
   * @return true or false
   */
  public boolean isLoop()
  {
    return depth >= DEPTH_MAX;
  }

  /**
   * Get the list of "possibles", as described in the class JavaDoc above.
   * @return the list of "possibles"
   */
  public Map getPossibles()
  {
    return possibles;
  }

  /**
   * Get the buffer of the last 20 classes encoded, as described in the class JavaDoc above.
   * @return the last 20 classes encoded
   */
  public SequencedHashMap getBuffer()
  {
    return buffer;
  }

  /**
   * Get the current depth.
   * @return current depth
   */
  public int getDepth()
  {
    return depth;
  }

  /**
   * Descend one level of recursion.
   */
  public void stepIn()
  {
    depth ++;
  }

  /**
   * Ascend one level out of recursion.
   */
  public void stepOut()
  {
    depth --;
  }
}
