package com.epsm.gwtHibernateHello.client.view;

import com.epsm.gwtHibernateHello.client.presenter.GreetingPresenter;
import com.epsm.gwtHibernateHello.client.presenter.Presenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Greeting extends Composite implements GreetingView{
	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private GreetingPresenter presenter;
	   
	@UiTemplate("Greeting.ui.xml")
	interface LoginUiBinder extends UiBinder<Widget, Greeting> {
	}
	
	@UiField
	Label greetingLabel;
	
	@UiField
	Label logoutLink;
	
	@UiField
	Label errorLabel;
	
	public Greeting() {
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
		greetingLabel.setText("");
		errorLabel.setText("");
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = (GreetingPresenter) presenter;
	}
	
	@Override
	public void displayGreeting(String greeting) {
		greetingLabel.setText(greeting);
	}
	
	@UiHandler("logoutLink")
	void doClick(ClickEvent event) {
		presenter.executeLogout();
	}
}