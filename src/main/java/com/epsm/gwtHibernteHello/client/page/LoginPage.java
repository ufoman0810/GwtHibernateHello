package com.epsm.gwtHibernteHello.client.page;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginPage extends Composite {
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private int loginLenght;
	private int passwordLenght;
	private final int MIN_LENGHT = 4;
	private static Logger logger = Logger.getLogger("Login");
	   
	@UiTemplate("LoginPage.ui.xml")
	interface LoginUiBinder extends UiBinder<Widget, LoginPage> {
	}

	@UiField(provided = true)
	final Resources resources;

	@UiField
	TextBox loginBox;

	@UiField
	TextBox passwordBox;

	@UiField
	Label errorLabel;
	
	public LoginPage() {
		resources = GWT.create(Resources.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("buttonLogin")
	void doClickLogin(ClickEvent event) {
		logger.fine("Pressed: logging button.");
		
		if (areLoginOrPasswordTooShort()) {
			String errorMessage = "Login or password can't be shorter than " + MIN_LENGHT + " chars.";
			errorLabel.setText(errorMessage);
			logger.fine("Result: login button pressed - error, " + errorMessage);
		} else {
			Window.alert("Login Successful!");
			logger.fine("Result: login button pressed - login successful");
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
		logger.finest("Invoked: getLoginAndPasswordLenghts() method, "
				+ "login lenght = '" + loginLenght + "', "
				+ "password lenght = '" + passwordLenght + "'.");
	}
	
	private boolean areLenghtsShorterThanMin(){
		boolean shorter = loginLenght < MIN_LENGHT || passwordLenght < MIN_LENGHT;
		logger.finest("Invoked: areLenghtsShorterThanMin() method, "
				+ " min value = '" + MIN_LENGHT + "', "
				+ "login lenght =  '" + loginLenght + "', "
				+ "password lenght = '" + passwordLenght + "', "
				+ "returned '" + shorter + "'.");
		
		return shorter;
	}
}