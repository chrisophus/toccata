package com.toccatapiano.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LessonRates extends Composite
{

   private static LessonRatesUiBinder uiBinder = GWT
         .create(LessonRatesUiBinder.class);

   interface LessonRatesUiBinder extends UiBinder<Widget, LessonRates>
   {
   }

   public LessonRates()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }

}
