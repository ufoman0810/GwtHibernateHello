package com.epsm.gwtHibernteHello.client.service;

import com.epsm.gwtHibernteHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void loginServer(String name, String password, AsyncCallback<UserDTO> callback);
	void loginFromSessionServer(AsyncCallback<UserDTO> callback);
}
