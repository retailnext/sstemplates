package com.carbonfive.flash;

import java.io.*;
import org.apache.commons.logging.*;

public class Context
  implements Serializable
{
  private static final Log log = LogFactory.getLog(Context.class);

  private TranslationFilter filter = null;

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

}
