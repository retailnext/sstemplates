package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;

/**
 * Cache used to maintain references between ASObject graphs as well as Java object graphs.  This cache knows
 * whether it should use equivalence (.equals()) or identity (==) to match objects.  The default is identity.
 */
public class ReferenceCache
  implements Serializable
{
  private static final Log log = LogFactory.getLog(ReferenceCache.class);
  
  private IdentityMap identityCache    = null;
  private HashMap     equivalenceCache = null;
  private boolean     useEquivalence   = false;

  public ReferenceCache(boolean useEquivalence)
  {
    this.identityCache    = new IdentityMap();
    this.equivalenceCache = new HashMap();
    this.useEquivalence   = useEquivalence;
  }

  public void put(Object key, Object value)
  {
    if (useEquivalence) equivalenceCache.put(key, value);
    else                identityCache.put(key, value);
  }

  public Object get(Object key)
  {
    if (useEquivalence) return equivalenceCache.get(key);
    else                return identityCache.get(key);
  }

  public boolean containsKey(Object key)
  {
    if (useEquivalence) return equivalenceCache.containsKey(key);
    else                return identityCache.containsKey(key);
  }

  public int size()
  {
    if (useEquivalence) return equivalenceCache.size();
    else                return identityCache.size();
  }
}
