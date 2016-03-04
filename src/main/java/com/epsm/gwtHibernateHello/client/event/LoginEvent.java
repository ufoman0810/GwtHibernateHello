package com.epsm.gwtHibernateHello.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler>{
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}
}
