package com.carbonfive.flash;

import java.util.*;

public class CachingManager
{
  private static HashMap encoderCaches = new HashMap();
  private static HashMap decoderCaches = new HashMap();

  public static ReferenceCache createEncoderCache(boolean useEquivalence)
  {
    return getCache(encoderCaches, useEquivalence);
  }

  public static ReferenceCache getEncoderCache()
  {
    return getCache(encoderCaches);
  }

  public static ReferenceCache getDecoderCache()
  {
    return getCache(decoderCaches, false);
  }

  private static ReferenceCache getCache(HashMap caches, boolean useEquivalence)
  {
    if (! caches.containsKey(Thread.currentThread()))
    {
      caches.put(Thread.currentThread(), new ReferenceCache(useEquivalence));
    }
    return (ReferenceCache) caches.get(Thread.currentThread());
  }

  private static ReferenceCache getCache(HashMap caches)
  {
    return getCache(caches, false);
  }

  static void removeEncoderCache()
  {
    encoderCaches.remove(Thread.currentThread());
  }

  static void removeDecoderCache()
  {
    decoderCaches.remove(Thread.currentThread());
  }
}