package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.tags.WorkbookTag;
import junit.framework.TestCase;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateCachelessTest extends TestCase
{
  private SsTemplateProcessor processor;
  private File templateDir;

  public SsTemplateCachelessTest(String s)
  {
    super(s);
  }

  protected void setUp() throws Exception
  {
    processor = SsTemplateProcessor.getInstance(false);
    URL url = getClass().getResource("/test/templates");
    templateDir = new File(URLDecoder.decode(url.getFile(), "UTF8"));
  }

  protected void tearDown() throws Exception
  {
  }

  public void testRenderWorkbooksWithCahchingOff() throws Exception
  {
    String[] workbooks = new String[]{"cell_blank.sst","cell_column.sst","cell_content.sst","cell_region.sst","cell_type.sst"};
    for ( int i=0; i < workbooks.length; i++ )
    {
      WorkbookTag tag = getRenderTree(workbooks[i]);
      assertNotNull( tag );
    }
    assertEquals( 0, processor.getTemplateCacheSize() );
  }

  protected WorkbookTag getRenderTree(String path) throws Exception
  {
    String realPath = new File(templateDir, path).getPath();
    return processor.retrieveRenderTree(realPath);
  }


}