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

  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return null;
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    IdentityMap encoderCache = CachingManager.getEncoderCache();

    if ( encoderCache.containsKey(decodedObject) )
    {
      // log.debug("Cached object: " + decodedObject.getClass().getName());
      return encoderCache.get( decodedObject );
    }
    // log.debug("Translating object: " + decodedObject.getClass().getName());

    Object encodedShell = nextEncoder.encodeShell(ctx, decodedObject);
    if (encodedShell != null) encoderCache.put(decodedObject, encodedShell);

    return nextEncoder.encodeObject(ctx, encodedShell, decodedObject);
  }

  public ActionScriptEncoder getNextEncoder()
  {
    return nextEncoder;
  }
}
