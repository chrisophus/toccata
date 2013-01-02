package com.toccatapiano.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.bradrydzewski.gwt.calendar.client.event.DeleteEvent;
import com.bradrydzewski.gwt.calendar.client.event.DeleteHandler;
import com.bradrydzewski.gwt.calendar.client.event.MouseOverEvent;
import com.bradrydzewski.gwt.calendar.client.event.MouseOverHandler;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickEvent;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickHandler;
import com.bradrydzewski.gwt.calendar.client.event.UpdateEvent;
import com.bradrydzewski.gwt.calendar.client.event.UpdateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CalendarView extends Composite implements
      ValueChangeHandler<Date>, ShowRangeHandler<Date>, AsyncCallback<Void>
{

   private static CalendarUiBinder uiBinder = GWT
         .create(CalendarUiBinder.class);

   interface CalendarUiBinder extends UiBinder<Widget, CalendarView>
   {
   }

   @UiField
   DockLayoutPanel dockPanel;

   @UiField
   Button monthButton;

   @UiField
   CheckBox viewMonth;

   @UiField
   CheckBox showPicker;

   private DatePicker datePicker = new DatePicker();

   private static final DateTimeFormat timeFormat = DateTimeFormat
         .getFormat(DateTimeFormat.PredefinedFormat.TIME_SHORT);
   private final Map<String, CalendarEventDTO> eventMap = new HashMap<String, CalendarEventDTO>();
   private Date currentDate = new Date();
   private static final DateTimeFormat format = DateTimeFormat
         .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);

   private Calendar calendar;
   private DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMMM");

   final DecoratedPopupPanel datePickerPopup = new DecoratedPopupPanel(true);

   private String user = "toccatapiano@gmail.com";

   private CalendarEventServiceAsync service = GWT
         .create(CalendarEventService.class);

   private void showMonthView()
   {
      if (!viewMonth.getValue())
      {
         calendar.setView(CalendarViews.MONTH);
         viewMonth.setValue(true);
      }
   }

   private void showDayView()
   {
      if (viewMonth.getValue())
      {
         calendar.setView(CalendarViews.DAY);
         viewMonth.setValue(false);
      }
   }

   private void toggleView()
   {
      if (viewMonth.getValue())
      {
         showDayView();
      }
      else
      {
         showMonthView();
      }
   }

   public CalendarView()
   {
      initWidget(uiBinder.createAndBindUi(this));

      datePicker.addValueChangeHandler(this);
      datePicker.addShowRangeHandler(this);
   }

   private void initalize()
   {
      if (calendar == null)
      {
         createCalendar();

         setupEventHandling();

         datePickerPopup.setAutoHideEnabled(false);
         datePickerPopup.add(datePicker);
      }
   }

   public void show()
   {
      initalize();

      Timer timer = new Timer() {
         public void run()
         {
            updateDate(new Date());
         }
      };

      timer.schedule(100);
   }

   private void updateDate( Date date )
   {
      currentDate = date;
      monthButton.setText(monthFormat.format(currentDate));
      calendar.setDate(currentDate);
   }

   @UiHandler("monthButton")
   void onMonthButtonClick( ClickEvent event )
   {
      showDatePicker();
   }

   private void showDatePicker()
   {
      datePicker.setCurrentMonth(currentDate);
      showPicker.setValue(true);
      datePickerPopup.show();
   }

   @Override
   public void onValueChange( ValueChangeEvent<Date> event )
   {
      showDayView();
      updateDate(event.getValue());
   }

   private Widget createEventPopup( Appointment appt )
   {
      StringBuilder sb = new StringBuilder();
      sb.append(timeFormat.format(appt.getStart()));
      sb.append(" - ");
      sb.append(timeFormat.format(appt.getEnd()));
      sb.append("<br/>");
      sb.append(appt.getTitle());
      sb.append("<br/>");
      sb.append(appt.getDescription());

      return new HTML(sb.toString());
   }

   private void setupEventHandling()
   {
      calendar.addUpdateHandler(new UpdateHandler<Appointment>() {
         @Override
         public void onUpdate( UpdateEvent<Appointment> event )
         {
//            event.setCancelled(true); // Cancel Appointment update
         }
      });

      final DecoratedPopupPanel appointmentPopup = new DecoratedPopupPanel(true);
      appointmentPopup.setAutoHideEnabled(true);

      calendar.addMouseOverHandler(new MouseOverHandler<Appointment>() {
         @Override
         public void onMouseOver( MouseOverEvent<Appointment> event )
         {
            if (viewMonth.getValue())
            {
               Appointment appt = event.getTarget();
               appointmentPopup.setWidget(createEventPopup(appt));

               Element element = (Element) event.getElement();
               int left = element.getAbsoluteLeft() + 10;
               int top = element.getAbsoluteTop() + 10;
               appointmentPopup.setPopupPosition(left, top);
               appointmentPopup.show();
            }
         }
      });

      calendar.addTimeBlockClickHandler(new TimeBlockClickHandler<Date>() {
         @Override
         public void onTimeBlockClick( TimeBlockClickEvent<Date> event )
         {
            updateDate(event.getTarget());
            if (!viewMonth.getValue())
            {
               createEvent(event.getTarget());
            }
         }
      });

      calendar.addSelectionHandler(new SelectionHandler<Appointment>() {
         @Override
         public void onSelection( SelectionEvent<Appointment> event )
         {
            showDayView();
            updateDate(event.getSelectedItem().getStart());
         }
      });
      
      calendar.addDeleteHandler(new DeleteHandler<Appointment>() {
         
         @Override
         public void onDelete( DeleteEvent<Appointment> event )
         {
            CalendarEventDTO ce = eventMap.get(event.getTarget().getTitle());
            service.removeEvent(ce, new AsyncCallback<Void>() {
               @Override
               public void onSuccess( Void result )
               {
               }
               
               @Override
               public void onFailure( Throwable caught )
               {
               }
            });
         }
      });
   }
   
   private static final long MILLISECONDS_IN_SECOND = 1000l;
   private static final long SECONDS_IN_MINUTE = 60l;
   private static final long MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;
   private Date addMinutes(final Date start, int minutes)
   {
      return new Date(start.getTime() + minutes * MILLISECONDS_IN_MINUTE);
   }

   private void createEvent( Date target )
   {
      String title = Window.prompt("Enter event title", "New Event");
      CalendarEventDTO event = new CalendarEventDTO("", target, addMinutes(target, 120), title, "");
      service.addEvent(event, new AsyncCallback<Void>() {
         
         @Override
         public void onSuccess( Void result )
         {
            Window.alert("added new event");
         }
         
         @Override
         public void onFailure( Throwable caught )
         {
            Window.alert("failed to add event: " + caught.getMessage());
         }
      });
   }

   private void createCalendar()
   {
      calendar = new Calendar();

      CalendarSettings settings = calendar.getSettings();
      settings.setEnableDragDrop(true);
      settings.setOffsetHourLabels(false);
      calendar.setWidth("100%");
      calendar.setHeight("100%");

      loadAppointments();

      dockPanel.add(calendar);

      showMonthView();
   }

   private void loadAppointments()
   {
      service.getEvents(new AsyncCallback<CalendarEventDTO[]>() {

         @Override
         public void onSuccess( CalendarEventDTO[] result )
         {
            updateAppointments(result);
         }

         @Override
         public void onFailure( Throwable caught )
         {
            displayError("Couldn't retrieve event data.");
         }
      });
   }

   private void updateAppointments( CalendarEventDTO[] events )
   {
      for (int i = 0; i < events.length; i++)
      {
         CalendarEventDTO event = events[i];
         calendar.addAppointment(getAppointment(event));
      }
   }

   private Appointment getAppointment( CalendarEventDTO event )
   {
      Appointment appt = new Appointment();
      appt.setStart(event.getStartDate());
      appt.setEnd(event.getEndDate());
      appt.setTitle(event.getTitle());
      appt.setDescription(event.getDetails());
      appt.setStyle(AppointmentStyle.BLUE);

      return appt;
   }

   private void displayError( String string )
   {
      Window.alert(string);
   }

   @UiHandler("viewMonth")
   void onViewMonthValueChange( ValueChangeEvent<Boolean> event )
   {
      if (event.getValue())
      {
         calendar.setView(CalendarViews.MONTH);
      }
      else
      {
         calendar.setView(CalendarViews.DAY);
      }
   }

   @UiHandler("showPicker")
   void onShowPickerValueChange( ValueChangeEvent<Boolean> event )
   {
      if (event.getValue())
      {
         showDatePicker();
      }
      else
      {
         datePickerPopup.hide();
      }
   }

   @Override
   public void onShowRange( ShowRangeEvent<Date> event )
   {
      showMonthView();
      updateDate(datePicker.getCurrentMonth());
   }

   @Override
   public void onFailure( Throwable caught )
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void onSuccess( Void result )
   {
      displayError("created event");
   }
}
