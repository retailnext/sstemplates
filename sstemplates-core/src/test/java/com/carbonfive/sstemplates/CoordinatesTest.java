package com.carbonfive.sstemplates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CoordinatesTest
{
  @Test
  public void testToString() throws Exception
  {
    Coordinates c = new Coordinates(0, (short) 0);
    assertEquals("A1", c.toString());

    c = new Coordinates(1, (short) 1);
    assertEquals("B2", c.toString());

    c = new Coordinates(2, (short) 26);
    assertEquals("AA3", c.toString());

    c = new Coordinates(3, (short) 53);
    assertEquals("BB4", c.toString());
  }
}
