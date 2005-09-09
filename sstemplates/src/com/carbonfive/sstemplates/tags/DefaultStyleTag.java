package com.carbonfive.sstemplates.tags;

import com.carbonfive.sstemplates.hssf.*;
import com.carbonfive.sstemplates.*;

import org.apache.poi.hssf.usermodel.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class DefaultStyleTag extends StyleTag
{
  public String getTagName()
  {
    return "defaultstyle";
  }

  protected void renderChildren(SsTemplateContext context) throws SsTemplateException
  {
    HssfStyleData styleData = context.getNamedStyleData(context.getCurrentStyle());

    if (context.getSheet() != null)
      throw new SsTemplateException("Must define defaultstyle before creating sheets");

    HSSFSheet sheet = context.getWorkbook().createSheet();
    styleData.setStyleAttributes(sheet.createRow(0).createCell((short)0).getCellStyle(), context);
    context.getWorkbook().removeSheetAt(0);

    super.renderChildren(context);
  }
}
