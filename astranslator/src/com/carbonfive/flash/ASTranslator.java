package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import java.text.*;
import java.beans.*;
import java.lang.reflect.*;
import flashgateway.io.ASObject;


 /**
  * ASTranslator provides the ability to translate between ASObjects used by
  * Macromedia Flash Remoting and Java objects in your application. ASObjects are
  * essentially HashMaps with an additional field "type".
  * <p>
  * The ASObject.getType() field has a special role in ASTranslator. It identifies
  * which Java object an ASObject should be translated to, and when an ASObject
  * is created from a Java object it is set to the class name of the source Java object.
  * <p>
  * To achieve the greatest value from ASTranslator, you should define classes in Flash that
  * map to server-side Java objects and register the ActionScript classes with the 
  * corresponding Java class name. With this, objects sent and received from Flash
  * via Flash Remoting retain their class definitions and behaviors.
  * 
  * <h3>Example of Flash to Server</h3>
  *
  * <h4>In Flash 6 with Flash Remoting installed</h4>
  * 
  * <pre>
  * #include "NetServices.as"
  * 
  * Game = function() {}
  * Object.registerClass("com.server.Game", Game);
  * 
  * Round = function() {}
  * Object.registerClass("com.server.Round", Round);
  * 
  * var game = new Game();
  * game.rounds = new Array();
  * game.rounds.push(new Round());
  * game.rounds[0].score = 300;
  * game.rounds.push(new Round());
  * game.rounds[1].score = 500;
  * 
  * NetServices.setDefaultGatewayUrl("http://localhost/gateway");
  * var gatewayConnection = NetServices.createGatewayConnection();
  * var service = gatewayConnection.getService("com.server.Service", this);
  * service.saveGame(game);
  * </pre>
  * 
  * <h4>And on the server side</h4>
  * 
  * <pre>
  * package com.server;
  * 
  * import flashgateway.io.ASObject;
  * import com.carbonfive.flash.*;
  * 
  * public class Service
  * {
  *   public void saveGame(ASObject aso)
  *   {
  *     Game game = (Game) new ASTranslator().fromActionScript(aso);
  *     
  *     // do whatever you need to with the Game
  *     
  *     for (Iterator i = game.getRounds().iterator(); i.hasNext(); )
  *     {
  *       Round round = (Round) i.next();
  *     }
  *   }
  * }
  * </pre>
  *
  * <h3>Example of Flash to Server and Back</h3>
  *
  * <h4>In Flash 6 with Flash Remoting installed</h4>
  * 
  * <pre>
  * #include "NetServices.as"
  * 
  * Player = function() {}
  * Object.registerClass("com.server.Player", Player);
  * 
  * Game = function() {}
  * Object.registerClass("com.server.Game", Game);
  * 
  * var game = new Game();
  * game.rounds = new Array();
  * game.rounds.push(new Round());
  * game.rounds[0].score = 300;
  * game.rounds.push(new Round());
  * game.rounds[1].score = 500;
  * 
  * NetServices.setDefaultGatewayUrl("http://localhost/gateway");
  * var gatewayConnection = NetServices.createGatewayConnection();
  * var service = gatewayConnection.getService("com.server.Service", this);
  * service.getPlayer("JoeWinner");
  *
  * getPlayer_Result = function (player)
  * {
  *   trace("Got player: " + player.name + " with " + player.games.length + " games played");    
  * }
  * </pre>
  * 
  * <h4>And on the server side</h4>
  * 
  * <pre>
  * package com.server;
  * 
  * import java.util.*;
  * import flashgateway.io.ASObject;
  * import com.carbonfive.flash.*;
  * 
  * public class Service
  * {
  *   public ASObject getPlayer(String name)
  *   {
  *     Player player = Roster.getPlayer(name);
  *     List games    = Schedule.getGamesForPlayer(player);
  *     player.setGames(games);
  *     
  *     ASObject aso = (ASObject) new ASTranslator().toActionScript(player);
  *     return aso;
  *   }
  * }
  * </pre>
  *
  * $Id$
  */
public class ASTranslator
{

  // these maps are used to maintain references
  private ReferenceBasedCache asToBeanCache = new ReferenceBasedCache();
  private ReferenceBasedCache beanToASCache = new ReferenceBasedCache();


  /**
   * Given an Object, toActionScript creates a corresponding ASObject
   * or Collection of ASObjects that map the source object's JavaBean
   * properties to ASObject fields, Collections and Sets to
   * ArrayLists, and all Numbers to Doubles while maintaining object
   * references.  
   * <p> 
   * These mappings are consistent with Flash Remoting's rules for
   * converting Objects to ASObjects. They just add the behavior of
   * using JavaBean-style introspection to determine property
   * names. Additionally, toActionScript sets the "type" field of all
   * ASObjects created to be the class name of the source
   * JavaBean. This enables two-way translation between ASObjects and
   * JavaBeans.
   *
   * @param serverObject  an Object to translate to ASObjects or 
   *                      corresponding primitive or Collection classes
   * @return              an Object that may be an ASObject or nested 
   *                      Collections of ASObjects or null if conversion fails
   */
  public Object toActionScript( Object serverObject )
  {
    if (serverObject == null) return null;

    // check references map
    if ( beanToASCache.containsKey(serverObject) )
    {
      return beanToASCache.get( serverObject );
    }

    Translator translator = TranslatorFactory.getInstance().getServerTranslator( this, serverObject );
    Object result = translator.translateToActionScript( serverObject );


    if (result != null)
    {
      beanToASCache.put( serverObject, result );
      return result;
    }

    return null;
  }


  /**
   * Given an Object that is either an ASObject or Collection of
   * ASObjects, fromActionScript creates a corresponding JavaBean or
   * Collections of JavaBeans.  
   * <p> 
   * The "type" field of an ASObject is identifies the class name of
   * the JavaBean to create. If the type field is null, an ASObject
   * becomes a HashMap. The interface of the JavaBean is more specific
   * that the relatively loosely-typed ASObject and therefore drives
   * the translation of ASObject fields.

   *
   * @param aso an Object that is usually an ASObject but may also be
   *            a Collection or primitive
   * @return    an Object value that is either a JavaBean or Collection
   *            of JavaBeans or null if translation fails
   */
  public Object fromActionScript(Object actionScriptObject )
  {
    try
    {
      Class desiredBeanClass = decideClassToTranslateInto( actionScriptObject );
      return mapFromActionScriptObject( actionScriptObject, desiredBeanClass );
    }
    catch( ASTranslationException aste )
    {
      return null;
    }
  }

////////////////////////////////////////////////////////////////////////////////
//
// p r o t e c t e d
//
////////////////////////////////////////////////////////////////////////////////

  protected Object fromActionScript(Object actionScriptObject, Class desiredBeanClass )
  {
    return mapFromActionScriptObject( actionScriptObject, desiredBeanClass );
  }

////////////////////////////////////////////////////////////////////////////////
//
// p r i v a t e
//
////////////////////////////////////////////////////////////////////////////////

  private Class decideClassToTranslateInto( Object aso ) throws ASTranslationException
  {
    Class asoClass = null;

    if (aso instanceof ASObject)
    {
      String classOfActionScriptObject = ( (ASObject) aso).getType();
      try
      {
        asoClass = Class.forName( classOfActionScriptObject );
      }
      catch ( ClassNotFoundException cnfe )
      {
        throw new ASTranslationException( "Unable to find Server-Side Class to match type indicated by ActionScript Object: " + classOfActionScriptObject, cnfe );
      }
    }
    else
    {
      asoClass = aso.getClass();
    }

    return asoClass;
  }

//------------------------------------------------------------------------------

  /**
   * Translate an object to another object of type clazz
   * obj types should be ASObject, Boolean, String, Double, Date, ArrayList
   */
  private Object mapFromActionScriptObject(Object clientObject, Class clazz)
  {
    if (clientObject == null) return null;

    // check references map
    if ( asToBeanCache.containsKey(clientObject) )
    {
      return asToBeanCache.get(clientObject);
    }

    Translator translator = TranslatorFactory.getInstance().getClientTranslator( this, clientObject );
    Object result = translator.translateFromActionScript( clientObject, clazz );

    // add to references map
    if (result != null)
    {
      asToBeanCache.put(clientObject, result);
      return result;
    }

    return null;
  }

}
