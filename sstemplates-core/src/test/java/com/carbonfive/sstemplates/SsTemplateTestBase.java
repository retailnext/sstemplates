package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.tags.*;
import junit.framework.*;

import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class SsTemplateTestBase extends TestCase
{
  private SsTemplateProcessor processor;
  private File templateDir;

  public SsTemplateTestBase(String s)
  {
    super(s);
  }

  protected void setUp() throws Exception
  {
    processor = SsTemplateProcessor.getInstance();
      URL url = getClass().getResource("/test/templates");
      templateDir = new File(URLDecoder.decode(url.getFile(), "UTF8"));
  }

  protected void tearDown() throws Exception
  {
  }

  protected SsTemplateContext getHssfTemplateContext() throws ServletException
  {
    return processor.createTemplateContext(templateDir);
  }

  protected WorkbookTag getRenderTree(String path) throws Exception
  {
    String realPath = new File(templateDir, path).getPath();
    return processor.retrieveRenderTree(realPath);
  }

  protected SsTemplateContext renderWorkbook(String path) throws Exception
  {
    return renderWorkbook(path, null);
  }

  private static void setParamMaps(String query, SsTemplateContext templateContext) throws UnsupportedEncodingException
  {
    if (query == null) return;

    Map<String, String> param = new HashMap<String, String>();
    Map<String, String[]> paramValues = new HashMap<String, String[]>();

    StringTokenizer st = new StringTokenizer(query, "&");
    while (st.hasMoreTokens())
    {
      String tok = st.nextToken();
      int eq = tok.indexOf("=");
      String key = tok.substring(0, eq);
      String value = URLDecoder.decode(tok.substring(eq + 1), "UTF-8");

      if (paramValues.containsKey(key))
      {
        String[] arr = (String[]) paramValues.get(key);
        List<String> list = new ArrayList<String>(Arrays.asList(arr));
        list.add(value);
        paramValues.put(key, (String[]) list.toArray(arr));
      }
      else if (param.containsKey(key))
      {
        paramValues.put(key, new String[] { (String) param.get(key), value });
      }
      else
      {
        param.put(key, value);
        paramValues.put(key, new String[] { value });
      }
    }

    templateContext.setPageVariable("param", param);
    templateContext.setPageVariable("paramValues", paramValues);
  }

  protected SsTemplateContext renderWorkbook(String pathPlusQuery, Map<String, ?> attributes)
    throws Exception
  {
    if (attributes == null) attributes = new HashMap<String, Object>();

    URI uri = new URI(pathPlusQuery);

    WorkbookTag renderTree = getRenderTree(uri.getPath());
    SsTemplateContext templateContext = getHssfTemplateContext();

    setParamMaps(uri.getQuery(), templateContext);

    for (Iterator<String> it = attributes.keySet().iterator(); it.hasNext();)
    {
      String key = it.next();
      templateContext.setPageVariable(key, attributes.get(key));
    }

    renderTree.render(templateContext);
    return templateContext;
  }
}
