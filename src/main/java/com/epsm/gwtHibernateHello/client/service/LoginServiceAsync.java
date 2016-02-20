package com.epsm.gwtHibernateHello.client.service;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void loginServer(String name, String password, AsyncCallback<UserDTO> callback);
	void isSessionIdStillLegal(String sessionId, AsyncCallback<UserDTO> callback);
}
