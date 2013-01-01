package com.toccatapiano.client;

import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class CalendarEventDTO implements Serializable
{

   private Long id;
   private String user;
   private Date startDate;
   private Date endDate;
   String title;
   String details;

   public CalendarEventDTO(String user, Date startDate, Date endDate,
         String title, String details)
   {
      super();
      this.user = user;
      this.startDate = startDate;
      this.endDate = endDate;
      this.title = title;
      this.details = details;
   }

   public Long getId()
   {
      return id;
   }

   public void setId( Long id )
   {
      this.id = id;
   }

   public String getUser()
   {
      return user;
   }

   public void setUser( String user )
   {
      this.user = user;
   }

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

   public CalendarEventDTO()
   {
      super();
      // TODO Auto-generated constructor stub
   }
}