package com.toccatapiano.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class MainPage implements EntryPoint, ValueChangeHandler<String>,
      SelectionHandler<Integer>
{
   private static final String HOME = "Home";
   private static final String BIOGRAPHY = "Biography";
   private static final String LESSON_RATES = "Lessons/Rates";
   private static final String CALENDAR = "Upcoming Shows/News";
   private static final String CONTACT = "Contact";
   private static final String USER = "User";

   private static final String TABS[] = { HOME, BIOGRAPHY, LESSON_RATES,
         CALENDAR, CONTACT, USER };

   private TabLayoutPanel tabPanel;
   private CalendarView calendarView;
   private UserPage userPage = new UserPage();

   public void onModuleLoad()
   {
      setupGui();
      startHistoryHandling();
   }

   private void startHistoryHandling()
   {
      History.addValueChangeHandler(this);
      History.fireCurrentHistoryState();
   }

   private void setupGui()
   {
      calendarView = new CalendarView();
      
      TabContent tabContent = new TabContent();
      tabPanel = tabContent.getTabPanel();
      tabPanel.addSelectionHandler(this);
      tabPanel.add(new Home(), HOME);
      tabPanel.add(new Biography(), BIOGRAPHY);
      tabPanel.add(new LessonRates(), LESSON_RATES);
      tabPanel.add(calendarView, CALENDAR);
      tabPanel.add(new Contact(), CONTACT);
      tabPanel.add(userPage, USER);
      tabPanel.setAnimationVertical(false);
      tabPanel.setAnimationDuration(500);
      
      
      RootLayoutPanel.get().add(tabContent);
   }

   @Override
   public void onValueChange( ValueChangeEvent<String> event )
   {
      String token = null;
      if (event.getValue() != null) token = event.getValue().trim();
      if (token == null || token.equals("")) showHome();
      boolean found = false;
      for (int j = 0; j < TABS.length; j++)
      {
         if (token.equals(TABS[j]))
         {
            tabPanel.selectTab(j);
            if (token.equals(CALENDAR))
               calendarView.show();
            if (token.equals(USER))
               userPage.show();
            found = true;
         }
      }
      if (!found) showHome();
   }

   private void showHome()
   {
      tabPanel.selectTab(0);
   }

   @Override
   public void onSelection( SelectionEvent<Integer> event )
   {
      int tab = event.getSelectedItem();

      History.newItem(TABS[tab]);
   }

}
