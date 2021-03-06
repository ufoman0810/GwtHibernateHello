package com.epsm.gwtHibernateHello.client.service;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void loginServer(String login, String password, AsyncCallback<UserDTO> callback);
	void isSessionStillLegal(String token, AsyncCallback<UserDTO> callback);
	void logout(String token, AsyncCallback<Void> callback);
}
