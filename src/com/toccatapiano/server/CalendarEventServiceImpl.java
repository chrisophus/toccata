package com.toccatapiano.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.toccatapiano.client.CalendarEventService;

@SuppressWarnings("serial")
public class CalendarEventServiceImpl extends RemoteServiceServlet implements
      CalendarEventService
{

   private static final Logger LOG = Logger
         .getLogger(CalendarEventServiceImpl.class.getName());
   private static final PersistenceManagerFactory PMF = JDOHelper
         .getPersistenceManagerFactory("transactions-optional");

   public void addStock( CalendarEvent event )
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      try
      {
         pm.makePersistent(new EventDataObject());
      }
      finally
      {
         pm.close();
      }
   }

   public void removeStock( String symbol ) throws NotLoggedInException
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      try
      {
         long deleteCount = 0;
         Query q = pm.newQuery(Stock.class, "user == u");
         q.declareParameters("com.google.appengine.api.users.User u");
         List<Stock> stocks = (List<Stock>) q.execute(getUser());
         for (Stock stock : stocks)
         {
            if (symbol.equals(stock.getSymbol()))
            {
               deleteCount++;
               pm.deletePersistent(stock);
            }
         }
         if (deleteCount != 1)
         {
            LOG.log(Level.WARNING, "removeStock deleted " + deleteCount
                  + " Stocks");
         }
      }
      finally
      {
         pm.close();
      }
   }

   public String[] getStocks() throws NotLoggedInException
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      List<String> symbols = new ArrayList<String>();
      try
      {
         Query q = pm.newQuery(Stock.class, "user == u");
         q.declareParameters("com.google.appengine.api.users.User u");
         q.setOrdering("createDate");
         List<Stock> stocks = (List<Stock>) q.execute(getUser());
         for (Stock stock : stocks)
         {
            symbols.add(stock.getSymbol());
         }
      }
      finally
      {
         pm.close();
      }
      return (String[]) symbols.toArray(new String[0]);
   }

   private void checkLoggedIn() throws NotLoggedInException
   {
      if (getUser() == null) { throw new NotLoggedInException("Not logged in."); }
   }

   private User getUser()
   {
      UserService userService = UserServiceFactory.getUserService();
      return userService.getCurrentUser();
   }

   private PersistenceManager getPersistenceManager()
   {
      return PMF.getPersistenceManager();
   }
}