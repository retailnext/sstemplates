package com.carbonfive.sstemplates;

import java.io.File;
import java.io.IOException;

/**
 * Utility for validating that resolved file paths stay within an expected root directory.
 */
public final class PathValidation
{
  private PathValidation() {}

  /**
   * Asserts that {@code file} is contained within {@code root}.
   * @throws IllegalArgumentException if the file escapes the root directory
   * @throws IOException if the canonical path cannot be resolved
   */
  public static void assertWithinDirectory(File file, String canonicalRoot) throws IOException
  {
    String canonicalPath = file.getCanonicalPath();
    if ( !canonicalPath.startsWith(canonicalRoot + File.separator) && !canonicalPath.equals(canonicalRoot) )
    {
      throw new IllegalArgumentException("Path traversal not allowed: " + file);
    }
  }
}
