package com.toccatapiano.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Biography extends Composite
{

   private static BiographyUiBinder uiBinder = GWT
         .create(BiographyUiBinder.class);

   interface BiographyUiBinder extends UiBinder<Widget, Biography>
   {
   }

   public Biography()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }

}
