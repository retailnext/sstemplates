package com.carbonfive.sstemplates;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.servlet.jsp.el.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.carbonfive.sstemplates.hssf.*;
import com.carbonfive.sstemplates.tags.SsTemplateTag;

public interface SsTemplateContext extends VariableResolver, FunctionMapper
{
  Object setPageVariable( String key, Object value );

  void unsetPageVariable( String key, Object oldValue );

  Object getPageVariable( String key );

  Object resolveVariable( String name );

  HSSFFont createFont( String name, short fontHeight, short color, boolean bold, boolean italic,
                       boolean strikeout, byte underline, short typeOffset );

  void incrementCellIndex();

  void incrementRowIndex();

  CellRangeAddress getRegionForCurrentLocation();

  String addStyleData( String name, HssfStyleData data );

  HSSFCellStyle getNamedStyle( String name )
    throws SsTemplateException;

  HssfStyleData getNamedStyleData( String name )
    throws SsTemplateException;

  boolean hasCachedStyleData(String name);

  HSSFWorkbook getWorkbook();

  void setWorkbook(HSSFWorkbook workbook);

  HSSFSheet getSheet();

  void setSheet(HSSFSheet sheet);

  HSSFRow getRow();

  void setRow(HSSFRow row);

  int getRowIndex();

  void setRowIndex(int rowIndex);

  int getColumnIndex();

  void setColumnIndex(int columnIndex);

  String getCurrentStyle();

  void setCurrentStyle(String currentStyle);

  HssfCellAccumulator getNamedAccumulator(String name);

  void registerMethod(String name, Method m);

  // no prefix support
  Method resolveFunction(String prefix, String name);

  Collection<SsTemplateTag> parseIncludeFile(String parsedTemplate) throws SsTemplateException;

  public File findFileInTemplateDirectory(String path);

  Object getCustomValue(Object key);

  void setCustomValue(Object key, Object value);

  short getColorIndex(short[] triplet) throws SsTemplateException;

  void setBackgroundColor(short[] triplet);

  short[] getBackgroundColor();

  public int getMaxRowIndex();
  public int getMaxColumnIndex();

  public void setPageBreaks(int firstPageBreak, int nextPageBreak);
  public int nextPageBreak(int row);
}
