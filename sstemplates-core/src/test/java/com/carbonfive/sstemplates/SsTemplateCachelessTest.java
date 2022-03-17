package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.tags.WorkbookTag;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateCachelessTest
{
  private SsTemplateProcessor processor;
  private File templateDir;

  @BeforeEach
  protected void setUp() throws Exception
  {
    processor = SsTemplateProcessor.getInstance(false);
    URL url = getClass().getResource("/test/templates");
    templateDir = new File(URLDecoder.decode(url.getFile(), "UTF8"));
  }

  @Test
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