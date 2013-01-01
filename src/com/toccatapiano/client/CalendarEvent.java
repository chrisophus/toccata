package com.toccatapiano.client;

import java.io.Serializable;
import java.util.Date;

public class CalendarEvent implements Serializable
{
   Date startDate;
   Date endDate;
   String title;
   String details;
   
   public Date getStartDate()
   {
      return startDate;
   }
   public void setStartDate( Date startDate )
   {
      this.startDate = startDate;
   }
   public Date getEndDate()
   {
      return endDate;
   }
   public void setEndDate( Date endDate )
   {
      this.endDate = endDate;
   }
   public String getTitle()
   {
      return title;
   }
   public void setTitle( String title )
   {
      this.title = title;
   }
   public String getDetails()
   {
      return details;
   }
   public void setDetails( String details )
   {
      this.details = details;
   }
}
