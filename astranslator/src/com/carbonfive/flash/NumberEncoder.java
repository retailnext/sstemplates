package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;

public class NumberEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    Number asNumber = (Number) decodedObject;
    return new Double( asNumber.doubleValue() );
  }
}