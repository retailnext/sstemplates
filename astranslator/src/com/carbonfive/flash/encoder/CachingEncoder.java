package com.carbonfive.flash.encoder;

import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

/**
 * Provides reference-based caching for Java to ActionScript encoding.
 */
public class CachingEncoder
  implements ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(CachingEncoder.class);

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

    Object encodedObject = null;
    try
    {
      encodedObject = nextEncoder.encodeObject( decodedObject );
    }
    catch (StackOverflowError e)
    {
      log.error("Stack overflow encoding: " + decodedObject);
      throw e;
    }

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
