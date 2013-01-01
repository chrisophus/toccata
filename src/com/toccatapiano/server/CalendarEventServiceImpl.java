package com.toccatapiano.server;

import java.util.ArrayList;
import java.util.Date;
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
import com.toccatapiano.client.CalendarEventDTO;
import com.toccatapiano.client.CalendarEventService;
import com.toccatapiano.client.NotLoggedInException;

public class CalendarEventServiceImpl extends RemoteServiceServlet implements
      CalendarEventService
{

   private static final long serialVersionUID = 7177236070359718862L;

   private static final Logger LOG = Logger
         .getLogger(CalendarEventServiceImpl.class.getName());
   private static final PersistenceManagerFactory PMF = JDOHelper
         .getPersistenceManagerFactory("transactions-optional");

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

   CalendarEvent dtoToEvent( CalendarEventDTO event )
   {
      return new CalendarEvent(event.getUser(), event.getStartDate(),
            event.getEndDate(), event.getTitle(), event.getDetails());
   }

   @Override
   public void addEvent( CalendarEventDTO event ) throws NotLoggedInException
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      try
      {
         event.setUser(getUser().getEmail());
         pm.makePersistent(dtoToEvent(event));
      }
      finally
      {
         pm.close();
      }
   }

   @Override
   public void removeEvent( CalendarEventDTO event )
         throws NotLoggedInException
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      try
      {
         long deleteCount = 0;
         Query q = pm.newQuery(CalendarEvent.class, "user == u");
         q.declareParameters("java.lang.String u");
         List<CalendarEvent> events = (List<CalendarEvent>) q.execute(getUser()
               .getEmail());
         for (CalendarEvent stock : events)
         {
            if (false)
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

   @Override
   public CalendarEventDTO[] getEvents() throws NotLoggedInException
   {
      checkLoggedIn();
      PersistenceManager pm = getPersistenceManager();
      List<CalendarEventDTO> eventList = new ArrayList<CalendarEventDTO>();
      try
      {
         Query q = pm.newQuery(CalendarEvent.class, "user == u");
         q.declareParameters("java.lang.String u");
         q.setOrdering("startDate");
         List<CalendarEvent> events = (List<CalendarEvent>) q.execute(getUser()
               .getEmail());
         for (CalendarEvent event : events)
         {
            eventList.add(new CalendarEventDTO(event.getUser(), new Date(event
                  .getStartDate().getTime()), new Date(event.getEndDate()
                  .getTime()), event.getTitle(), event.getDetails()));
         }
      }
      finally
      {
         pm.close();
      }
      return (CalendarEventDTO[]) eventList.toArray(new CalendarEventDTO[0]);
   }
}
