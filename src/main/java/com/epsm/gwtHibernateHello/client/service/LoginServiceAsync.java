package com.epsm.gwtHibernateHello.client.service;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void loginServer(String login, String password, AsyncCallback<UserDTO> callback);
	void isSessionIdStillLegal(AsyncCallback<UserDTO> callback);
	void logout(AsyncCallback<Void> callback);
}
