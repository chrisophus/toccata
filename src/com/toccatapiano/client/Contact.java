package com.toccatapiano.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Contact extends Composite
{

   private static ContactUiBinder uiBinder = GWT.create(ContactUiBinder.class);

   interface ContactUiBinder extends UiBinder<Widget, Contact>
   {
   }

   public Contact()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }

}
