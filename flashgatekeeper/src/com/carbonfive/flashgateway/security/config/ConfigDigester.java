package com.carbonfive.flashgateway.security.config;

import java.io.*;
import org.xml.sax.*;
import org.apache.commons.digester.Digester;

/**
 * ConfigDigester uses Jakarta Commons Digester to produce a FlashGatekeeper
 * Config from an XML file.
 */
public class ConfigDigester
{

  public static Config digest(String fileName)
    throws IOException, SAXException
  {
    InputStream in = ConfigDigester.class.getClassLoader().getResource(fileName).openStream();
    return (Config) getConfigDigester().parse(in);
  }

	public static Digester getConfigDigester()
  {
    Digester digester = new Digester();
    digester.setValidating( false );

		digester.addObjectCreate("config", Config.class);

		digester.addObjectCreate("config/service", Service.class);
		digester.addBeanPropertySetter("config/service/name", "name");
		digester.addSetNext("config/service", "addService");

		digester.addObjectCreate("config/service/method", Method.class);
		digester.addBeanPropertySetter("config/service/method/name", "name");
		digester.addSetNext("config/service/method", "addMethod");

		digester.addObjectCreate("config/service/method/access-constraint", AccessConstraint.class);
    digester.addCallMethod("config/service/method/access-constraint/role-name", "addRoleName", 1);
    digester.addCallParam("config/service/method/access-constraint/role-name", 0);
		digester.addSetNext("config/service/method/access-constraint", "setConstraint");

    return digester;
  }

}
