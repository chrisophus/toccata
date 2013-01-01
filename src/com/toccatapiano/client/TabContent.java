package com.toccatapiano.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabContent extends ResizeComposite
{

   private static TabContentUiBinder uiBinder = GWT
         .create(TabContentUiBinder.class);

   interface TabContentUiBinder extends UiBinder<Widget, TabContent>
   {
   }

   public TabContent()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }

   @UiField
   TabLayoutPanel tabPanel;
   
   TabLayoutPanel getTabPanel()
   {
      return tabPanel;
   }
}
