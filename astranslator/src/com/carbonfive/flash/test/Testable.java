package com.carbonfive.flash.test;

import java.util.*;

public interface Testable
{
  int getIntField();

  void setIntField(int i);

  short getShortField();

  void setShortField(short s);

  long getLongField();

  void setLongField(long l);

  double getDoubleField();

  void setDoubleField(double d);

  String getStrField();

  void setStrField(String s);

  Collection getColField();

  void setColField(Collection c);

  Object getTransObj();

  void setTransObj(String transObj);

  String getVolObj();

  void setVolObj(String volObj);
}
