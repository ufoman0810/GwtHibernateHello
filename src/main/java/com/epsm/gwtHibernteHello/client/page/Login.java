package com.epsm.gwtHibernteHello.client.page;

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

public class Login extends Composite {
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private int loginLenght;
	private int passwordLenght;
	   
	@UiTemplate("Login.ui.xml")
	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	@UiField(provided = true)
	final Resources resources;

	@UiField
	TextBox loginBox;

	@UiField
	TextBox passwordBox;

	@UiField
	Label errorLabel;
	
	public Login() {
		resources = GWT.create(Resources.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("buttonLogin")
	void doClickLogin(ClickEvent event) {
		if (areLoginOrPasswordTooShort()) {
			errorLabel.setText("Login or password can't be shorter than 4 chars.");
		} else {
			Window.alert("Login Successful!");
		}
	}
	
	private boolean areLoginOrPasswordTooShort(){
		getLoginAndPasswordLenghts();
		
		return compareLenghtsWithMinimalValue();
	}
	
	private void getLoginAndPasswordLenghts(){
		loginLenght = loginBox.getText().length();
		passwordLenght = passwordBox.getText().length();
	}
	
	private boolean compareLenghtsWithMinimalValue(){
		final int minValue = 4;
		
		return loginLenght < minValue || passwordLenght < minValue;
	}
}