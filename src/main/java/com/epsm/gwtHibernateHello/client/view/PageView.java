package com.epsm.gwtHibernateHello.client.view;

import com.epsm.gwtHibernateHello.client.presenter.PagePresenter;

public interface PageView {
	void eraseLoginAndPassword();
	void eraseLoginFillingErrorsFields();
	void eraseGreetingFillingErrorField();
	void displayPage();
	void hideLoginFilling();
	void displayLoginFilling();
	void hideGreetingFilling();
	void displayGreetingFilling(String greeting);
	void displayLoginError(String error);
	void displayLogoutError(String error);
	void setPresenter(PagePresenter presenter);
}
