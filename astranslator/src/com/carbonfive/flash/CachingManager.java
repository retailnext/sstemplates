package com.carbonfive.flash;

import java.util.*;

public class CachingManager
{
  private static HashMap encoderCaches = new HashMap();
  private static HashMap decoderCaches = new HashMap();

  public static ReferenceBasedCache getEncoderCache()
  {
    return getCache(encoderCaches);
  }

  public static ReferenceBasedCache getDecoderCache()
  {
    return getCache(decoderCaches);
  }

  private static ReferenceBasedCache getCache(HashMap caches)
  {
    if (! caches.containsKey(Thread.currentThread()))
    {
      caches.put(Thread.currentThread(), new ReferenceBasedCache());
    }
    return (ReferenceBasedCache) caches.get(Thread.currentThread());
  }

  static void removeEncoderCache()
  {
    encoderCaches.remove(Thread.currentThread());
  }

  static void removeDecoderCache()
  {
    encoderCaches.remove(Thread.currentThread());
  }
}