package com.carbonfive.flash.decoder;

import com.carbonfive.flash.*;

/**
 * Provides referenced-based caching for ActionScript to Java decoding.
 */
public class CachingDecoder
  implements ActionScriptDecoder
{
  private ActionScriptDecoder nextDecoder = null;

  public CachingDecoder(ActionScriptDecoder next)
  {
    this.nextDecoder = next;
  }

  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    return null;
  }

  public Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    ReferenceBasedCache decoderCache = CachingManager.getDecoderCache();

    if ( decoderCache.containsKey(encodedObject) )
    {
      return decoderCache.get( encodedObject );
    }

    Object decodedShell = nextDecoder.decodeShell(encodedObject, desiredClass);
    if (decodedShell != null) decoderCache.put(encodedObject, decodedShell);

    return nextDecoder.decodeObject(decodedShell, encodedObject, desiredClass);
  }

  public ActionScriptDecoder getNextDecoder()
  {
    return nextDecoder;
  }

  public String toString()
  {
    return "CachingDecoder[" + nextDecoder + "]";
  }
}
