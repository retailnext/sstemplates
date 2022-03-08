package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.*;

public interface SsTemplateTag
{
  String getTagName();

  void render( SsTemplateContext context ) throws SsTemplateException;

  void addChildTag( SsTemplateTag tag );

  SsTemplateTag getParentTag();

  void setParentTag( SsTemplateTag tag );
}
