package com.carbonfive.flash;

public class NativeDecoder
  implements ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    return encodedObject;
  }
}