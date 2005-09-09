package com.carbonfive.sstemplates.tags;

import org.apache.commons.logging.*;

/**
 * TODO: remove in favor of ForEachTag
 *
 * @deprecated
 */
public class IteratorTag
  extends ForEachTag
{
  private static final Log log = LogFactory.getLog(IteratorTag.class);

  public String getTagName()
  {
    return "iterator";
  }
}
