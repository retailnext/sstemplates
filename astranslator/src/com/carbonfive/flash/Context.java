package com.carbonfive.flash;

import java.io.*;
import org.apache.commons.logging.*;

/**
 * Context passed to encoders and decoders, as well as factories, during translation to and from Action Script.
 * This object holds a translation filter, which allows certain classes and properties to be ignored while
 * translating, as well as a loop finder, which detects infinite loops.
 */
public class Context
  implements Serializable
{
  private static final Log log = LogFactory.getLog(Context.class);

  private TranslationFilter filter     = null;
  private LoopFinder        loopFinder = new LoopFinder();

  /**
   * Return the base context, which contains the default translation filter.
   * @return Base <code>Context</code> object
   */
  public static Context getBaseContext()
  {
    Context ctx = new Context();
    ctx.setFilter(TranslationFilter.getBaseFilter());

    return ctx;
  }

  private Context() { }

  public TranslationFilter getFilter()
  {
    return filter;
  }

  public void setFilter(TranslationFilter filter)
  {
    this.filter = filter;
  }

  public LoopFinder getLoopFinder()
  {
    return loopFinder;
  }
}
