package com.carbonfive.gis;

public class CalculatorException
  extends Exception
{
  public CalculatorException() { super(); }
  public CalculatorException(String msg) { super(msg); }
  public CalculatorException(Throwable cause) { super(cause); }
  public CalculatorException(String msg, Throwable cause) { super(msg, cause); }
}