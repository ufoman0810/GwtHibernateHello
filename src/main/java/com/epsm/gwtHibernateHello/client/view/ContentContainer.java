package com.epsm.gwtHibernateHello.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentContainer {
	private static ContentContainer container = new ContentContainer();
	
	private ContentContainer(){
	}
	
	public static ContentContainer getInstance(){
		return container;
	}
	
	public void setContent(Composite content){
		clearContentDiv();
		insertNewContent(content);
	}
	
	private void clearContentDiv(){
		RootPanel.get("content").clear();
	}
	
	private void insertNewContent(Composite content){
		RootPanel.get("content").add(content);
	}
}
