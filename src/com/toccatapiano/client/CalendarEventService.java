package com.toccatapiano.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("event")
public interface CalendarEventService extends RemoteService
{
   public void addEvent( CalendarEventDTO event ) throws NotLoggedInException;

   public void removeEvent( CalendarEventDTO event ) throws NotLoggedInException;

   public CalendarEventDTO[] getEvents() throws NotLoggedInException;
}
