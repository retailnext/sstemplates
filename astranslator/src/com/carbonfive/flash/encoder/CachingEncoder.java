package com.carbonfive.flash.encoder;

import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

/**
 * Provides reference-based caching for Java to ActionScript encoding.
 */
public class CachingEncoder
  extends ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(CachingEncoder.class);

  private ActionScriptEncoder nextEncoder = null;

  public CachingEncoder(ActionScriptEncoder next)
  {
    this.nextEncoder = next;
  }

  public Object encodeShell(Object decodedObject)
  {
    return null;
  }

  public Object encodeObject(Object shell, Object decodedObject)
  {
    IdentityMap encoderCache = CachingManager.getEncoderCache();

    if ( encoderCache.containsKey(decodedObject) )
    {
//      log.debug("Found cached object: " + decodedObject.getClass().getName());
//      if (decodedObject instanceof String)
//        log.debug("  (String): " + decodedObject);
      return encoderCache.get( decodedObject );
    }

    Object encodedShell = nextEncoder.encodeShell(decodedObject);
    if (encodedShell != null) encoderCache.put(decodedObject, encodedShell);

    return nextEncoder.encodeObject(encodedShell, decodedObject);
  }

  public ActionScriptEncoder getNextEncoder()
  {
    return nextEncoder;
  }
}
