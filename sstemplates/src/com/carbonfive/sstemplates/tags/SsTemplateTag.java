package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public interface SsTemplateTag
{
  String getTagName();

  void render( SsTemplateContext context ) throws SsTemplateException;

  void addHssfTag( SsTemplateTag tag );
}
