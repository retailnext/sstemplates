package com.carbonfive.sstemplates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTest
{
  private SsTemplateProcessor processor;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() throws Exception
  {
    processor = SsTemplateProcessor.getInstance(false);
  }

  @Test
  void testPathTraversalInFindFileInTemplateDirectory() throws Exception
  {
    File templateDir = tempDir.toFile();
    SsTemplateContext context = processor.createTemplateContext(templateDir);

    assertThrows(IllegalArgumentException.class, () -> {
      context.findFileInTemplateDirectory("../../etc/passwd");
    });
  }

  @Test
  void testPathTraversalWithRelativePathBlocked() throws Exception
  {
    File templateDir = tempDir.toFile();
    SsTemplateContext context = processor.createTemplateContext(templateDir);

    assertThrows(IllegalArgumentException.class, () -> {
      context.findFileInTemplateDirectory("../secret.txt");
    });
  }

  @Test
  void testValidPathInTemplateDirectory() throws Exception
  {
    File templateDir = tempDir.toFile();
    SsTemplateContext context = processor.createTemplateContext(templateDir);

    File result = context.findFileInTemplateDirectory("valid.sst");
    assertEquals(new File(templateDir, "valid.sst").getCanonicalPath(), result.getCanonicalPath());
  }

  @Test
  void testNullPathInTemplateDirectory() throws Exception
  {
    File templateDir = tempDir.toFile();
    SsTemplateContext context = processor.createTemplateContext(templateDir);

    File result = context.findFileInTemplateDirectory(null);
    assertEquals(templateDir.getCanonicalPath(), result.getCanonicalPath());
  }

  @Test
  void testSubdirectoryPathAllowed() throws Exception
  {
    File templateDir = tempDir.toFile();
    new File(templateDir, "sub").mkdir();
    SsTemplateContext context = processor.createTemplateContext(templateDir);

    File result = context.findFileInTemplateDirectory("sub/template.sst");
    assertTrue(result.getCanonicalPath().startsWith(templateDir.getCanonicalPath()));
  }

  @Test
  void testXxePreventionRejectsDoctypeDecl() throws Exception
  {
    File templateFile = tempDir.resolve("xxe.sst").toFile();
    try (FileWriter fw = new FileWriter(templateFile))
    {
      fw.write("<?xml version=\"1.0\"?>\n");
      fw.write("<!DOCTYPE foo [\n");
      fw.write("  <!ENTITY xxe SYSTEM \"file:///etc/passwd\">\n");
      fw.write("]>\n");
      fw.write("<workbook><sheet name=\"test\"><row><cell>&xxe;</cell></row></sheet></workbook>\n");
    }

    assertThrows(SsTemplateException.class, () -> {
      processor.process(templateFile, new java.util.HashMap<>());
    });
  }
}
