package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.tags.*;
import org.apache.commons.digester.*;
import org.apache.poi.hssf.usermodel.*;
import org.xml.sax.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateProcessor
{
  private static final Logger log = Logger.getLogger(SsTemplateProcessor.class.getName());

  private static final Class[] DEFAULT_TAGS = new Class[]
    { SheetTag.class, RowTag.class, CellTag.class, ForEachTag.class, IfTag.class,
      IncludeTag.class, StyleTag.class, FunctionTag.class,
      WhileTag.class, RowBreakTag.class, DefaultStyleTag.class, SetTag.class,
      ChooseTag.class, WhenTag.class, OtherwiseTag.class
    };

  private Digester   digester = null;
  private Exception  parseException = null;
  private Map        renderTreeCache = new HashMap();
  private Collection customTags = null;

  private static Map processorCache = Collections.synchronizedMap(new HashMap());

  public static SsTemplateProcessor getInstance()
    throws SsTemplateException
  {
    return getInstance(null);
  }

  public static SsTemplateProcessor getInstance(Collection customTags)
    throws SsTemplateException
  {
    SsTemplateProcessor processor = (SsTemplateProcessor) processorCache.get(customTags);

    if (processor == null)
    {
      processor = new SsTemplateProcessor(customTags);
      processorCache.put(customTags, processor);
    }

    return processor;
  }

  protected SsTemplateProcessor(Collection customTags)
      throws SsTemplateException
  {
    this.customTags = customTags;
    configureDigester(getTags());
  }

  public HSSFWorkbook process(File templateFile, Map context)
    throws SsTemplateException
  {
    return process(templateFile.getParentFile(), templateFile, context);
  }

  public HSSFWorkbook process(File templateDir, File templateFile, Map context)
    throws SsTemplateException
  {
    WorkbookTag renderTree = retrieveRenderTree(templateFile.getAbsolutePath());
    SsTemplateContext templateContext = new SsTemplateContextImpl(this, templateDir, context);

    renderTree.render(templateContext);
    return templateContext.getWorkbook();
  }

  protected SsTemplateContext createTemplateContext(File templateDir)
  {
    return new SsTemplateContextImpl(this, templateDir);
  }

  protected Collection parseIncludeFile(File templateFile)
      throws SsTemplateException
  {
    if ( ! templateFile.exists() )
    {
      throw new SsTemplateException( "Could not find template file: " + templateFile);
    }

    WorkbookTag tag = retrieveRenderTree(templateFile.getAbsolutePath());
    return tag.getChildTags();
  }

  private SsTemplateTag instantiateTag(Class tagClass) throws SsTemplateException
  {
    SsTemplateTag tag = null;
    try
    {
      tag = (SsTemplateTag) tagClass.newInstance();
    }
    catch ( Exception e )
    {
      log.log(Level.SEVERE,"Error instantiating class: " + tagClass.getName(), e);
      throw new SsTemplateException( "Error instantiating class: " + tagClass.getName(), e );
    }
    return tag;
  }

  private void configureDigester(List tags) throws SsTemplateException
  {
    digester = new Digester();
    digester.setValidating(false);
    digester.addSetProperties("workbook");

    for (Iterator it = tags.iterator(); it.hasNext();)
    {
      SsTemplateTag tag = instantiateTag((Class) it.next());

      digester.addObjectCreate("*/" + tag.getTagName(), tag.getClass() );
      digester.addSetProperties("*/" + tag.getTagName());
      digester.addSetNext("*/" + tag.getTagName(), "addChildTag" );
    }

    // add special rull to add content to cell
    digester.addCallMethod("*/cell","setContents",0);
  }

  public List getTags()
  {
    List tags = new ArrayList();

    for (int i = 0; i < DEFAULT_TAGS.length; i++)
    {
      tags.add( DEFAULT_TAGS[i] );
    }

    if (customTags != null) tags.addAll(customTags);
    return tags;
  }

  public synchronized WorkbookTag retrieveRenderTree( String path ) throws SsTemplateException
  {
    long fileTimestamp = (new File(path)).lastModified();
    RenderTreeEntry renderTree = (RenderTreeEntry) renderTreeCache.get( path );
    if (( renderTree == null ) || ( renderTree.timestamp < fileTimestamp ))
    {
      renderTree = new RenderTreeEntry( fileTimestamp, parseRenderTree(path) );
      renderTreeCache.put( path, renderTree );
    }
    return renderTree.tree;
  }

  private WorkbookTag parseRenderTree(String path)
    throws SsTemplateException
  {
    WorkbookTag workbook = new WorkbookTag();
    try
    {
      digester.clear();
      digester.push( workbook );
      FileReader fr = new FileReader( path );
      digester.parse(fr);
      if ( parseException != null  )
        throw new SsTemplateException( "parse error", parseException );
      parseException = null;
      fr.close();
    }
    catch ( IOException ioe )
    {
      throw new SsTemplateException( "Error parsing template file: " + path, ioe );
    }
    catch ( SAXException se )
    {
      throw new SsTemplateException( "Error parsing template file: " + path, se );
    }

    return (WorkbookTag) digester.getRoot();
  }

  private class RenderTreeEntry
  {
    public long timestamp = 0L;
    public WorkbookTag tree = null;
    public RenderTreeEntry( long timestamp, WorkbookTag tree )
    {
      this.timestamp = timestamp;
      this.tree = tree;
    }
  }
}
