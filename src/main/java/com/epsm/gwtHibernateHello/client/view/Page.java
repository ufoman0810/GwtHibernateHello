package com.epsm.gwtHibernateHello.client.view;

import com.epsm.gwtHibernateHello.client.presenter.PagePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Page extends Composite implements PageView{
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private PagePresenter presenter;
	   
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
	Label greetingLabel;
	
	@UiField
	Label logoutLink;

	@UiField
	Label loginErrorLabel;
	
	@UiField
	Label logoutErrorLabel;
	
	@UiField
	Button buttonLogin;
	
	public Page() {
		resources = GWT.create(Resources.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void eraseLoginAndPassword() {
		loginBox.setText("");
		passwordBox.setText("");
	}

	@Override
	public void eraseLoginFillingErrorsFields() {
		loginErrorLabel.setText("");
	}

	@Override
	public void eraseGreetingFillingErrorField() {
		logoutErrorLabel.setText("");
	}

	public void displayPage(){
		RootLayoutPanel.get().add(this);
	}
	
	@Override
	public void hideLoginFilling() {
		DOM.getElementById("loginFilling").getStyle().setDisplay(Display.NONE);
	}
	
	@Override
	public void displayLoginFilling() {
		DOM.getElementById("loginFilling").getStyle().setDisplay(Display.BLOCK);
		loginBox.setFocus(true);
	}

	@Override
	public void hideGreetingFilling() {
		DOM.getElementById("greetingFilling").getStyle().setDisplay(Display.NONE);
	}
	
	@Override
	public void displayGreetingFilling(String greeting) {
		greetingLabel.setText(greeting);
		DOM.getElementById("greetingFilling").getStyle().setDisplay(Display.BLOCK);
	}

	@Override
	public void displayLoginError(String error) {
		loginErrorLabel.setText(error);
	}

	@Override
	public void displayLogoutError(String error) {
		logoutErrorLabel.setText(error);
	}

	@Override
	public void setPresenter(PagePresenter presenter) {
		this.presenter = presenter;
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
	
	@UiHandler("logoutLink")
	void doClick(ClickEvent event) {
		presenter.executeLogout();
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