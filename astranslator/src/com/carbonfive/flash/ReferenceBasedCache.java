package com.carbonfive.flash;

import java.util.*;

/**
 * A lightweight HashMap (not all methods implemented) that wraps keys with
 * an object that uses equivalence (==) for equals() and hashCode().  It is
 * useful for caching objects by reference (regardless of their equals()
 * implementation).  Specifically, we needed it for handling ASObject
 * references.
 *
 * @author Alon Salont <alon@carbonfive.com>
 */
public class ReferenceBasedCache
{

  private HashMap map;

  public ReferenceBasedCache()
  {
    map = new HashMap();
  }

  public void put(Object key, Object value)
  {
    this.map.put(wrap(key), value);  // break
  }

  public Object get(Object key)
  {
    return this.map.get(wrap(key));
  }

  public boolean containsKey(Object key)
  {
    return this.map.containsKey(wrap(key));
  }

  public int size()
  {
    return this.map.size();
  }

  private Object wrap(Object obj)
  {
    return new WrappedObject(obj);
  }

  private class WrappedObject
  {
    private Object obj;

    WrappedObject(Object obj)
    {
      this.obj = obj;
    }

    public boolean equals(Object wrappedObj)
    {
      return this.obj == ((WrappedObject) wrappedObj).obj;
    }

    public int hashCode()
    {
      return this.obj.hashCode();
    }
  }
}