package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.tags.*;
import org.apache.commons.digester3.Digester;
import org.apache.poi.hssf.usermodel.*;
import org.xml.sax.*;

import java.io.*;
import java.util.*;

/**
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateProcessor
{
  @SuppressWarnings("unchecked")
  private static final Class<SsTemplateTag>[] DEFAULT_TAGS = new Class[]
    { SheetTag.class, RowTag.class, CellTag.class, ForEachTag.class, IfTag.class,
      IncludeTag.class, StyleTag.class, FunctionTag.class,
      WhileTag.class, RowBreakTag.class, DefaultStyleTag.class, SetTag.class,
      ChooseTag.class, WhenTag.class, OtherwiseTag.class
    };

  private Digester   digester = null;
  private Exception  parseException = null;
  private Map<String, RenderTreeEntry> renderTreeCache = null;
  private Collection<Class<SsTemplateTag>> customTags = null;

  private static Map<Collection<Class<SsTemplateTag>>, SsTemplateProcessor> processorCache = Collections.synchronizedMap(new HashMap<Collection<Class<SsTemplateTag>>, SsTemplateProcessor>());

  public static SsTemplateProcessor getInstance()
    throws SsTemplateException
  {
    return getInstance(null, true);
  }

  public static SsTemplateProcessor getInstance( boolean cacheTemplates )
    throws SsTemplateException
  {
    return getInstance(null, cacheTemplates);
  }

  public static SsTemplateProcessor getInstance(Collection<Class<SsTemplateTag>> customTags)
    throws SsTemplateException
  {
    return getInstance( customTags, true );
  }

  public static SsTemplateProcessor getInstance(Collection<Class<SsTemplateTag>> customTags, boolean cacheTemplates)
    throws SsTemplateException
  {
    if ( ! cacheTemplates )
      return new SsTemplateProcessor(customTags, cacheTemplates);

    SsTemplateProcessor processor = (SsTemplateProcessor) processorCache.get(customTags);

    if (processor == null)
    {
      processor = new SsTemplateProcessor(customTags, cacheTemplates);
      processorCache.put(customTags, processor);
    }

    return processor;
  }

  protected SsTemplateProcessor(Collection<Class<SsTemplateTag>> customTags, boolean cacheTemplates)
      throws SsTemplateException
  {
    this.customTags = customTags;
    if ( cacheTemplates )
      this.renderTreeCache = new HashMap<String, RenderTreeEntry>();
    configureDigester(getTags());
  }

  public HSSFWorkbook process(File templateFile, Map<String, Object> context)
    throws SsTemplateException
  {
    return process(templateFile.getParentFile(), templateFile, context);
  }

  public HSSFWorkbook process(File templateDir, File templateFile, Map<String, Object> context)
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

  protected Collection<SsTemplateTag> parseIncludeFile(File templateFile)
      throws SsTemplateException
  {
    if ( ! templateFile.exists() )
    {
      throw new SsTemplateException( "Could not find template file: " + templateFile);
    }

    WorkbookTag tag = retrieveRenderTree(templateFile.getAbsolutePath());
    return tag.getChildTags();
  }

  private SsTemplateTag instantiateTag(Class<SsTemplateTag> tagClass) throws SsTemplateException
  {
    SsTemplateTag tag = null;
    try
    {
      tag = tagClass.newInstance();
    }
    catch ( Exception e )
    {
      throw new SsTemplateException( "Error instantiating class: " + tagClass.getName(), e );
    }
    return tag;
  }

  private void configureDigester(List<Class<SsTemplateTag>> tags) throws SsTemplateException
  {
    digester = new Digester();
    digester.setValidating(false);
    digester.addSetProperties("workbook");

    for (Iterator<Class<SsTemplateTag>> it = tags.iterator(); it.hasNext();)
    {
      SsTemplateTag tag = instantiateTag((Class<SsTemplateTag>) it.next());

      digester.addObjectCreate("*/" + tag.getTagName(), tag.getClass() );
      digester.addSetProperties("*/" + tag.getTagName());
      digester.addSetNext("*/" + tag.getTagName(), "addChildTag" );
    }

    // add special rull to add content to cell
    digester.addCallMethod("*/cell","setContents",0);
  }

  public List<Class<SsTemplateTag>> getTags()
  {
    List<Class<SsTemplateTag>> tags = new ArrayList<Class<SsTemplateTag>>();

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
    RenderTreeEntry renderTree = renderTreeCache==null ? null : (RenderTreeEntry) renderTreeCache.get( path );
    if (( renderTree == null ) || ( renderTree.timestamp < fileTimestamp ))
    {
      renderTree = new RenderTreeEntry( fileTimestamp, parseRenderTree(path) );
      if ( renderTreeCache != null )
        renderTreeCache.put( path, renderTree );
    }
    return renderTree.tree;
  }

  public int getTemplateCacheSize()
  {
    return renderTreeCache == null ? 0 : renderTreeCache.size();
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
