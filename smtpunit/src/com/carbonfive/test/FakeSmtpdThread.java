package com.carbonfive.smtp; 

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer; 

/**
* Simple multithreaded Http daemon that exclusively uses
* the 'old ' I/O API.
*
* @author hendrik.schreiber@innoq.com
*/
public class FakeSmtpdThread extends Thread
{ 
  public static final int SMTPD_PORT = 25;
  private static int instance;

  private ServerSocket   serverSocket;
  private byte[]         buf = new byte[1024 * 8];
  private String         protocol;
  private InputStream    in;
  private BufferedReader reader;
  private OutputStream   out;
  private String         uri; 

  /**
  * Starts the Smtpd thread.
  */
  public FakeSmtpdThread(ServerSocket serverSocket)
    throws IOException 
  {
    super("FakeSmtpdThread " + (instance++));
    this.serverSocket = serverSocket;
  } 


  /**
  * Waits for incoming connections and then calls
  * handleRequest.
  */
  public void run()
  {
    System.out.println("Starting thread " + this);
    Socket socket = null;
    while (true)
    {
      try
      {
        socket = serverSocket.accept();
        System.out.println("Incoming connection from " + socket.getInetAddress());
        socket.setTcpNoDelay(true);
        in = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        out = socket.getOutputStream();
        handleRequest();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (socket != null)
        {
          try
          {
            // this also closes the in and outputStream.
            socket.close();
          }
          catch (IOException ioe){ /* ignore */ }
        }
        socket = null;
      }
    }
  } 

  /**
  * Reads the request and sends either the file
  * or an error message back to the client.
  */
  private void handleRequest()
    throws IOException
  {
    boolean conversing = true;
    String command = null;
    sendLine(220 + " Service ready");
    while(conversing)
    {
      command = readLine();
      System.out.println(command);
      if( command == null || command.equals("") || command.equals("QUIT"))
      {
        sendLine("221 Bye");
        conversing = false;
      }
      else if(command.equals("DATA"))
      {
			  sendLine("354 Ready for data");
			  String data = receiveData();
			  sendLine("250 Thanks for the data");
		  }
		  else
      {
			  sendLine("250 Whatever");
		  }
    }
    return;
  } 

  /**
  * Sends a message to the client.
  */
  private void sendLine(String message)
    throws IOException 
  {
    System.out.println(message);
    message += "\r\n";
    out.write(message.getBytes("ASCII"));
    out.flush();
  }

  /**
   * Reads a line from the client.
   */
  private String readLine()
    throws IOException 
  {
    String line = reader.readLine();
    return line;
  }

  /**
   * Accepts data from teh client until a single '.' line is entered.
   */
  private String receiveData()
    throws IOException 
  {
    StringBuffer data = new StringBuffer();
    boolean reading = true;
    while(reading)
    {
      String line = reader.readLine();
      if(line.equals("."))
      {
        reading = false;
      }
      else
      {
        data.append(line + '\n');
        System.out.println(line);
      }
    }
    return data.toString();
  }

  private String decodeLine(String line)
  {
    return line;
  }
} 

