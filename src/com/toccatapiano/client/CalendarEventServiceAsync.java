package com.toccatapiano.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarEventServiceAsync
{
   void addEvent( CalendarEvent event, AsyncCallback<Void> callback );

   void getEvents( AsyncCallback<CalendarEvent[]> callback );

   void removeEvent( CalendarEvent event, AsyncCallback<Void> callback );
}
