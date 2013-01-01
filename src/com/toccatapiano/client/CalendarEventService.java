package com.toccatapiano.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.toccatapiano.server.NotLoggedInException;

@RemoteServiceRelativePath("stock")
public interface CalendarEventService extends RemoteService
{
   public void addEvent( CalendarEvent event ) throws NotLoggedInException;

   public void removeEvent( CalendarEvent event ) throws NotLoggedInException;

   public CalendarEvent[] getEvents() throws NotLoggedInException;
}
