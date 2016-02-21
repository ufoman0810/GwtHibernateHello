package com.epsm.gwtHibernateHello.client.service;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void loginServer(String login, String password, AsyncCallback<UserDTO> callback);
	void isSessionStillLegal(String sessionId, AsyncCallback<UserDTO> callback);
	void logout(String sessionId, AsyncCallback<Void> callback);
}
