package com.carbonfive.smtp;

import java.net.*;
import java.io.*;
import java.util.*;

public class FakeSmtpd
{
  public static void main(String[] args)
    throws IOException
  {
    int threads = 1;
    if(args.length < 1)
    {
      System.out.println("This class takes one optional argument: the number of listening threads. Defaulting to one.");
    }
    else
    {
      try
      {
        threads = Integer.parseInt(args[0]);
      }
      catch(NumberFormatException nfe){ /* ignore */ }
    }
    ServerSocket serverSocket = new ServerSocket(FakeSmtpdThread.SMTPD_PORT);
    System.out.println("OK starting...");
    for (int i=0; i < threads; i++)
    {
      FakeSmtpdThread smtpd = new FakeSmtpdThread(serverSocket);
      smtpd.start();
    }
    while(true)
    {
      try
      {
        Thread.sleep(5000);
      }
      catch( InterruptedException ie)
      {
        System.exit(1);
      }
    }
  }
}