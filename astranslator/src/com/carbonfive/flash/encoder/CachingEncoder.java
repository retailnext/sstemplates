package com.carbonfive.flash.encoder;

import java.util.*;
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
    ReferenceCache identityCache = CachingManager.getEncoderCache();

    if ( identityCache.containsKey(decodedObject) )
    {
      return identityCache.get( decodedObject );
    }

    Object encodedShell = nextEncoder.encodeShell(ctx, decodedObject);
    if (encodedShell != null) identityCache.put(decodedObject, encodedShell);

    Object encodedObject = nextEncoder.encodeObject(ctx, encodedShell, decodedObject);

    return encodedObject;
  }

  public ActionScriptEncoder getNextEncoder()
  {
    return nextEncoder;
  }
}
