package com.toccatapiano.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.bradrydzewski.gwt.calendar.client.event.MouseOverEvent;
import com.bradrydzewski.gwt.calendar.client.event.MouseOverHandler;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickEvent;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickHandler;
import com.bradrydzewski.gwt.calendar.client.event.UpdateEvent;
import com.bradrydzewski.gwt.calendar.client.event.UpdateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.widget.client.TextButton;

public class CalendarView extends Composite implements
      ValueChangeHandler<Date>, ShowRangeHandler<Date>
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
   private final Map<String, EventData> eventMap = new HashMap<String, EventData>();
   private Date currentDate = new Date();
   private TextButton currentMonth = new TextButton();
   private static final DateTimeFormat format = DateTimeFormat
         .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);

   private Calendar calendar;
   private DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMMM");

   final DecoratedPopupPanel datePickerPopup = new DecoratedPopupPanel(true);

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
            event.setCancelled(true); // Cancel Appointment update
         }
      });

      final DecoratedPopupPanel appointmentPopup = new DecoratedPopupPanel(true);
      appointmentPopup.setAutoHideEnabled(true);

      calendar.addMouseOverHandler(new MouseOverHandler<Appointment>() {
         @Override
         public void onMouseOver( MouseOverEvent<Appointment> event )
         {
            Appointment appt = event.getTarget();
            appointmentPopup.setWidget(createEventPopup(appt));

            Element element = (Element) event.getElement();
            int left = element.getAbsoluteLeft() + 10;
            int top = element.getAbsoluteTop() + 10;
            appointmentPopup.setPopupPosition(left, top);
            appointmentPopup.show();
         }
      });

      calendar.addTimeBlockClickHandler(new TimeBlockClickHandler<Date>() {
         @Override
         public void onTimeBlockClick( TimeBlockClickEvent<Date> event )
         {
            toggleView();
            updateDate(event.getTarget());
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
   }

   private void createCalendar()
   {
      calendar = new Calendar();

      CalendarSettings settings = calendar.getSettings();
      settings.setEnableDragDrop(false);
      settings.setOffsetHourLabels(false);
      calendar.setWidth("100%");
      calendar.setHeight("100%");

      loadAppointments();

      dockPanel.add(calendar);

      showMonthView();
   }

   private void loadAppointments()
   {
      String url = GWT.getModuleBaseURL() + "../appointments.json";
      // Send request to server and catch any errors.
      RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

      try
      {
         builder.sendRequest(null, new RequestCallback() {
            public void onError( Request request, Throwable exception )
            {
               displayError("Couldn't retrieve event data.");
            }

            public void onResponseReceived( Request request, Response response )
            {
               if (Response.SC_OK == response.getStatusCode())
               {
                  updateAppointments(asArrayOfEvents(response.getText()));
               }
               else
               {
                  displayError("Couldn't retrieve events.");
               }
            }
         });
      }
      catch (RequestException e)
      {
         displayError("Couldn't retrieve JSON");
      }
   }

   private void updateAppointments( JsArray<EventData> events )
   {
      for (int i = 0; i < events.length(); i++)
      {
         calendar.addAppointment(events.get(i).getAppointment());
      }
   }

   private static class EventData extends JavaScriptObject
   {
      protected EventData()
      {
      }

      public final native String getStartDate() /*-{
			return this.startdate;
      }-*/;

      public final native String getEndDate() /*-{
			return this.enddate;
      }-*/;

      public final native String getTitle() /*-{
			return this.title;
      }-*/;

      public final native String getDescription() /*-{
			return this.description;
      }-*/;

      // Non-JSNI method to return change percentage. // (4)
      public final Appointment getAppointment()
      {
         Appointment appt = new Appointment();
         appt.setStart(format.parse(getStartDate()));
         appt.setEnd(format.parse(getEndDate()));
         appt.setTitle(getTitle());
         appt.setDescription(getDescription());
         appt.setStyle(AppointmentStyle.BLUE);

         return appt;
      }
   }

   private final native JsArray<EventData> asArrayOfEvents( String json ) /*-{
		return eval(json);
   }-*/;

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
}
