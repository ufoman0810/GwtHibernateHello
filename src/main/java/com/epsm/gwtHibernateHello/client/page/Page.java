package com.epsm.gwtHibernateHello.client.page;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Page extends Composite {
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private int loginLenght;
	private int passwordLenght;
	private final int MIN_LENGHT = 4;
	private static Logger logger = Logger.getLogger("Page");
	   
	@UiTemplate("Page.ui.xml")
	interface LoginUiBinder extends UiBinder<Widget, Page> {
	}

	@UiField(provided = true)
	final Resources resources;

	@UiField
	TextBox loginBox;

	@UiField
	TextBox passwordBox;
	
	@UiField
	Label logoutLink;

	@UiField
	Label loginErrorLabel;
	
	@UiField
	Label logoutErrorLabel;
	
	public Page() {
		resources = GWT.create(Resources.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("buttonLogin")
	void doClickLogin(ClickEvent event) {
		logger.fine("Pressed: login button.");
		
		if (areLoginOrPasswordTooShort()) {
			showTooShortErrorMessage();
		}else{
			if(tryToLoginOnServer()){
				displayGreetUserFilling();
			}else{
				showWrongCredentialsMessage();
			}
		}
	}
	
	private boolean areLoginOrPasswordTooShort(){
		getLoginAndPasswordLenghts();
		boolean tooShort = areLenghtsShorterThanMin();
		logger.finest("Invoked: areLoginOrPasswordTooShort() method, returned '" + tooShort + "'.");
		
		return tooShort;
	}
	
	private void getLoginAndPasswordLenghts(){
		loginLenght = loginBox.getText().length();
		passwordLenght = passwordBox.getText().length();
	}
	
	private boolean areLenghtsShorterThanMin(){
		boolean shorter = loginLenght < MIN_LENGHT || passwordLenght < MIN_LENGHT;
		logger.finest("Invoked: areLenghtsShorterThanMin() method, "
				+ "min value = '" + MIN_LENGHT + "', "
				+ "login lenght =  '" + loginLenght + "', "
				+ "password lenght = '" + passwordLenght + "', "
				+ "returned '" + shorter + "'.");
		
		return shorter;
	}
	
	private void showTooShortErrorMessage(){
		String errorMessage = "Login or password can't be shorter than " + MIN_LENGHT + " chars.";
		loginErrorLabel.setText(errorMessage);
		logger.fine("Message: + " + errorMessage + ".");
	}
	
	public void displayLoginFilling(){
		clearLoginFillingFields();
		makeGreetDivInvisible();
		makeLoginDivVisible();
		logger.fine("Displayed: login filling.");
	}
	
	private void clearLoginFillingFields(){
		loginBox.setText("");
		passwordBox.setText("");
		loginErrorLabel.setText("");
	}
	
	private void makeGreetDivInvisible(){
		DOM.getElementById("greet").getStyle().setDisplay(Display.NONE);
	}
	
	private void makeLoginDivVisible(){
		DOM.getElementById("login").getStyle().setDisplay(Display.BLOCK);
	}
	
	private boolean tryToLoginOnServer(){
		boolean logedIn = true;
		logger.fine("Invoked: tryToLoginOnServer() method, returned '" + logedIn + "'.");
		
		return logedIn;
	}
	
	public void displayGreetUserFilling(){
		clearGreetFillingErrorField();
		makeLoginDivInvisible();
		makeGreetDivVisible();
		logger.fine("Displayed: greet user filling.");
	}
	
	private void clearGreetFillingErrorField(){
		logoutErrorLabel.setText("");
	}
	
	private void makeLoginDivInvisible(){
		DOM.getElementById("login").getStyle().setDisplay(Display.NONE);
	}
	
	private void makeGreetDivVisible(){
		DOM.getElementById("greet").getStyle().setDisplay(Display.BLOCK);
	}
	
	private void showWrongCredentialsMessage(){
		String errorMessage = "Wrong login or password.";
		loginErrorLabel.setText(errorMessage);
		logger.fine("Message: + " + errorMessage + ".");
	}
	
	@UiHandler("logoutLink")
	void doClick(ClickEvent event) {
		logger.fine("Pressed: logout link.");
		displayLoginFilling();
	}
}