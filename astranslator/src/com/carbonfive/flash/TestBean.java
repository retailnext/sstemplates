package com.carbonfive.flash;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;

public class TestBean
  implements Serializable
{
  private int intField;
  private short shortField;
  private long longField;
  private double doubleField;
  private String strField;
  private Document xmlDocument;

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
  public Document getXmlField() { return this.xmlDocument; }
  public void setXmlField(Document d) { this.xmlDocument = d; }

  static TestBean getTestBean()
  {
    TestBean bean = new TestBean();
    bean.setIntField(1);
    bean.setStrField("A string");
    bean.setLongField(12345);
    bean.setDoubleField(1.1234);
    bean.setXmlField(getXmlDocument());
    return bean;
  }                                               

  static Document getXmlDocument()
  {
    Document doc= new DocumentImpl();
    Element root = doc.createElement("person");     
    Element item = doc.createElement("name");       
    item.appendChild( doc.createTextNode("Jeff") );
    root.appendChild( item );                       
    item = doc.createElement("age");                
    item.appendChild( doc.createTextNode("28" ) );  
    root.appendChild( item );                       
    item = doc.createElement("height");            
    item.appendChild( doc.createTextNode("1.80" ) );
    root.appendChild( item );                       
    doc.appendChild( root );  
    
    return doc; 
  }  
}
