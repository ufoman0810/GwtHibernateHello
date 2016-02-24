package com.epsm.gwtHibernateHello.client.view;

import com.google.gwt.i18n.client.Messages;

public interface ErrorMessages extends Messages{
	String serverUnavaible();
	String tooShortLoginOrPassword(int minimalLenght);
	String wrongLoginOrPassword();
}
