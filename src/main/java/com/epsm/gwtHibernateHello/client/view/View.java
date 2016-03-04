package com.epsm.gwtHibernateHello.client.view;

import com.epsm.gwtHibernateHello.client.presenter.Presenter;
import com.google.gwt.user.client.ui.Composite;

public interface View {
	void eraseErrorsField();
	void displayError(String error);
	Composite asComposite();
	void resetState();
	void setPresenter(Presenter presenter);
}
