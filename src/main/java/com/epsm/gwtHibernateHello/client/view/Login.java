package com.epsm.gwtHibernateHello.client.view;

import com.epsm.gwtHibernateHello.client.presenter.LoginPresenter;
import com.epsm.gwtHibernateHello.client.presenter.Presenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Login extends Composite implements LoginView{
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private LoginPresenter presenter;
	   
	@UiTemplate("Login.ui.xml")
	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	@UiField
	TextBox loginBox;

	@UiField
	TextBox passwordBox;

	@UiField
	Label errorLabel;
	
	@UiField
	Button buttonLogin;
	
	public Login() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void eraseErrorsField() {
		errorLabel.setText("");
	}

	@Override
	public void displayError(String error) {
		errorLabel.setText(error);
	}
	
	@Override
	public Composite asComposite() {
		return this;
	}
	
	@Override
	public void resetState() {
		loginBox.setText("");
		passwordBox.setText("");
		errorLabel.setText("");
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (LoginPresenter) presenter;
	}
	
	@Override
	public void eraseLoginAndPassword() {
		loginBox.setText("");
		passwordBox.setText("");
	}
	
	@UiHandler("buttonLogin")
	void doClickLogin(ClickEvent event) {
		loginWithPresenter();
	}
	
	private void loginWithPresenter(){
		String login = loginBox.getText();
		String password = passwordBox.getText();
		presenter.logIn(login, password);
	}
	
	@UiHandler("loginBox")
	void handleLoginBoxKeys(KeyUpEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_DOWN || event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			passwordBox.setFocus(true);
		}
	}
	
	@UiHandler("passwordBox")
	void handlePasswordBoxKeys(KeyUpEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_UP){
			loginBox.setFocus(true);
		}else if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			loginWithPresenter();
		}
	}
}