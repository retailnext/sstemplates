package com.carbonfive.flash;

public class CachingEncoder
  implements ActionScriptEncoder
{
  private ActionScriptEncoder nextEncoder = null;

  public CachingEncoder(ActionScriptEncoder next)
  {
    this.nextEncoder = next;
  }

  public Object encodeObject( Object decodedObject )
  {
    ReferenceBasedCache encoderCache = CachingManager.getEncoderCache();

    if ( encoderCache.containsKey(decodedObject) )
    {
      return encoderCache.get( decodedObject );
    }

    Object encodedObject = nextEncoder.encodeObject( decodedObject );

    if (encodedObject != null)
    {
      encoderCache.put( decodedObject, encodedObject );
      return encodedObject;
    }

    return null;
  }

  public ActionScriptEncoder getNextEncoder()
  {
    return nextEncoder;
  }
}
