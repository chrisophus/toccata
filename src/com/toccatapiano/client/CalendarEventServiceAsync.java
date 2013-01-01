package com.toccatapiano.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarEventServiceAsync
{
   void addEvent( CalendarEventDTO event, AsyncCallback<Void> callback );

   void getEvents( AsyncCallback<CalendarEventDTO[]> callback );

   void removeEvent( CalendarEventDTO event, AsyncCallback<Void> callback );
}
