package com.toccatapiano.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class EventDataObject
{

   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Long id;
   @Persistent
   private String user;
   @Persistent
   private Date startDate;
   @Persistent
   private Date endDate;
   @Persistent
   private Date createDate;
   @Persistent
   String title;
   @Persistent
   String details;

   public EventDataObject(Long id, String user, Date startDate, Date endDate,
         String title, String details)
   {
      super();
      this.id = id;
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

   public Date getCreateDate()
   {
      return createDate;
   }

   public EventDataObject()
   {
      this.createDate = new Date();
   }
}