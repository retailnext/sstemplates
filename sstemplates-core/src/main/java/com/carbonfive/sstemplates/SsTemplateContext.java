package com.carbonfive.sstemplates;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import de.odysseus.el.util.SimpleResolver;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.carbonfive.sstemplates.hssf.*;
import com.carbonfive.sstemplates.tags.SsTemplateTag;

import javax.el.*;

public abstract class SsTemplateContext extends ELContext
{
  private final ELResolver resolver;

  public SsTemplateContext() {
    this.resolver = new SimpleResolver();
  }

  @Override
  public ELResolver getELResolver() {
    return this.resolver;
  }

  public abstract void setPageVariable(String key, Object value);

  public abstract void unsetPageVariable( String key, Object oldValue );

  public abstract Object getPageVariable( String key );

  public abstract HSSFFont createFont( String name, short fontHeight, short color, boolean bold, boolean italic,
                       boolean strikeout, byte underline, short typeOffset );

  public abstract void incrementCellIndex();

  public abstract void incrementRowIndex();

  public abstract CellRangeAddress getRegionForCurrentLocation();

  public abstract String addStyleData( String name, HssfStyleData data );

  public abstract HSSFCellStyle getNamedStyle( String name )
    throws SsTemplateException;

  public abstract HssfStyleData getNamedStyleData( String name )
    throws SsTemplateException;

  public abstract boolean hasCachedStyleData(String name);

  public abstract HSSFWorkbook getWorkbook();

  public abstract void setWorkbook(HSSFWorkbook workbook);

  public abstract HSSFSheet getSheet();

  public abstract void setSheet(HSSFSheet sheet);

  public abstract HSSFRow getRow();

  public abstract void setRow(HSSFRow row);

  public abstract int getRowIndex();

  public abstract void setRowIndex(int rowIndex);

  public abstract int getColumnIndex();

  public abstract void setColumnIndex(int columnIndex);

  public abstract String getCurrentStyle();

  public abstract void setCurrentStyle(String currentStyle);

  public abstract HssfCellAccumulator getNamedAccumulator(String name);

  public abstract void registerMethod(String name, Method m);

  public abstract Collection<SsTemplateTag> parseIncludeFile(String parsedTemplate) throws SsTemplateException;

  public abstract File findFileInTemplateDirectory(String path);

  public abstract Object getCustomValue(Object key);

  public abstract void setCustomValue(Object key, Object value);

  public abstract short getColorIndex(short[] triplet) throws SsTemplateException;

  public abstract void setBackgroundColor(short[] triplet);

  public abstract short[] getBackgroundColor();

  public abstract int getMaxRowIndex();
  public abstract int getMaxColumnIndex();

  public abstract void setPageBreaks(int firstPageBreak, int nextPageBreak);
  public abstract int nextPageBreak(int row);

  public abstract ExpressionFactory getExpressionFactory();
}