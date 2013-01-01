package com.toccatapiano.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserPage extends Composite
{

   private static UserPageUiBinder uiBinder = GWT
         .create(UserPageUiBinder.class);

   interface UserPageUiBinder extends UiBinder<Widget, UserPage>
   {
   }

   public UserPage()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }

   @UiField
   SimpleLayoutPanel panel;
   private LoginInfo loginInfo = null;
   private VerticalPanel loginPanel = new VerticalPanel();
   private Label loginLabel = new Label(
         "Please sign in to your Google Account to access the Toccata Piano use page.");
   private Anchor signInLink = new Anchor("Sign In");

   public void show()
   {
      Window.alert("here");
      // Check login status using login service.
      LoginServiceAsync loginService = GWT.create(LoginService.class);
      loginService.login(GWT.getModuleBaseURL(),
            new AsyncCallback<LoginInfo>() {
               public void onFailure( Throwable error )
               {
                  Window.alert("Failed to contact login service");
               }

               public void onSuccess( LoginInfo result )
               {
                  loginInfo = result;
                  if (loginInfo.isLoggedIn())
                  {
                     loadUserData();
                  }
                  else
                  {
                     loadLogin();
                  }
               }

            });
      Window.alert("here");
   }

   private void loadUserData()
   {
      Window.alert("Logged in as: " + loginInfo.getEmailAddress());
   }

   private void loadLogin()
   {
      Window.alert("not logged in");
      // Assemble login panel.
      signInLink.setHref(loginInfo.getLoginUrl());
      loginPanel.add(loginLabel);
      loginPanel.add(signInLink);
      panel.add(loginPanel);
   }
}
