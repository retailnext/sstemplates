package com.carbonfive.flash;

public class CachingDecoder
  implements ActionScriptDecoder
{
  private ActionScriptDecoder nextDecoder = null;

  public CachingDecoder(ActionScriptDecoder next)
  {
    this.nextDecoder = next;
  }

  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    ReferenceBasedCache decoderCache = CachingManager.getDecoderCache();

    if ( decoderCache.containsKey(encodedObject) )
    {
      return decoderCache.get( encodedObject );
    }

    Object decodedObject = nextDecoder.decodeObject( encodedObject, desiredClass );

    if (decodedObject != null)
    {
      decoderCache.put( encodedObject, decodedObject );
      return decodedObject;
    }

    return null;
  }

  public ActionScriptDecoder getNextDecoder()
  {
    return nextDecoder;
  }
}
