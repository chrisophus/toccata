package com.toccatapiano.client;

public class NotLoggedInException extends Exception
{
   private static final long serialVersionUID = 1L;

   public NotLoggedInException()
   {
      super();
      // TODO Auto-generated constructor stub
   }

   public NotLoggedInException(String arg)
   {
   }
   
   public NotLoggedInException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
      // TODO Auto-generated constructor stub
   }

   public NotLoggedInException(Throwable arg0)
   {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

}
