package com.carbonfive.flash.test;

import java.io.*;
import java.util.*;

public class TestBean
  implements Serializable
{
  private int intField;
  private short shortField;
  private long longField;
  private double doubleField;
  private String strField;
  private Collection colField;

  public int getIntField() { return this.intField; }
  public void setIntField(int i) { this.intField = i; }
  public short getShortField() { return this.shortField; }
  public void setShortField(short s) { this.shortField = s; }
  public long getLongField() { return this.longField; }
  public void setLongField(long l) { this.longField = l; }
  public double getDoubleField() { return this.doubleField; }
  public void setDoubleField(double d) { this.doubleField = d; }
  public String getStrField() { return this.strField; }
  public void setStrField(String s) { this.strField = s; }
  public Collection getColField() { return this.colField; }
  public void setColField(Collection c) { this.colField = c; }

  public static TestBean getTestBean()
  {
    TestBean bean = new TestBean();
    bean.setIntField(1);
    bean.setStrField("A string");
    bean.setLongField(12345);
    bean.setDoubleField(1.1234);
    bean.setColField(new HashSet(Arrays.asList(new String[] { "A", "B" })));
    return bean;
  }

}
