package com.carbonfive.flash.test;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;

public class LargeObject
  implements Serializable
{
  private static final Log log = LogFactory.getLog(LargeObject.class);

  private String  string  = null;
  private Integer integer = null;
  private Double  dooble  = null;

  private List listOne = new ArrayList();
  private List listTwo = new ArrayList();
  private Map  map     = new HashMap();

  public List getListOne() { return listOne; }
  public List getListTwo() { return listTwo; }
  public Map getMap() { return map; }
  public String getString() { return string; }
  public void setString(String string) { this.string = string; }
  public Integer getInteger() { return integer; }
  public void setInteger(Integer integer) { this.integer = integer; }
  public Double getDooble() { return dooble; }
  public void setDooble(Double dooble) { this.dooble = dooble; }

  public static class SubObjectOne implements Serializable
  {
    private String string = null;
    private Double dooble = null;

    private List list = new ArrayList();
    private Map  map  = new HashMap();

    public String getString() { return string; }
    public void setString(String string) { this.string = string; }
    public Double getDooble() { return dooble; }
    public void setDooble(Double dooble) { this.dooble = dooble; }
    public List getList() { return list; }
    public Map getMap() { return map; }
  }

  public static LargeObject create()
  {
    LargeObject lo = new LargeObject();
    for (int i = 0; i < 100; i++) lo.getListOne().add(createSub(true));
    for (int i = 0; i < 50; i++) lo.getListTwo().add(createSub(false));
    for (int i = 0; i < 50; i++) lo.getMap().put("" + i, createSub(true));
    return lo;
  }

  private static SubObjectOne createSub(boolean deep)
  {
    SubObjectOne sub = new SubObjectOne();
    sub.setString("blahblahblah" + Math.random());
    sub.setDooble(new Double(Math.random()));

    if (! deep) return sub;

    for (int i = 0; i < 10; i++) sub.getList().add(createSub(false));
    for (int i = 0; i < 10; i++) sub.getMap().put("" + i, createSub(false));
    return sub;
  }
}
