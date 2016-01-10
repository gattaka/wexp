package cz.gattserver.grass3.wexp.in.impl;

import cz.gattserver.grass3.wexp.DispatchAction;
import cz.gattserver.grass3.wexp.Dispatcher;
import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.in.Layout;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.FormElement;
import cz.gattserver.grass3.wexp.servlet.WexpServlet;

public class Form extends Layout {

	private static final long serialVersionUID = -593016947307067095L;

	private String address;
	private DispatchAction action;
	private String target;

	public Form(String address) {
		this.address = address;
	}

	public Form(DispatchAction action) {
		this.action = action;
	}

	public String getAddress() {
		return address;
	}

	public Form setAddress(String address) {
		this.action = null;
		this.address = address;
		return this;
	}

	public DispatchAction getAction() {
		return action;
	}

	public Form setAction(DispatchAction action) {
		this.address = null;
		this.action = action;
		return this;
	}

	public String getTarget() {
		return target;
	}

	public Form setTarget(String target) {
		this.target = target;
		return this;
	}

	@Override
	public WebElement construct() {
		String paramAddress = "";
		if (address != null) {
			paramAddress = address;
		} else {
			paramAddress = WexpServlet.getPathPrefix() + Dispatcher.ACTION_CHUNK
					+ WexpServlet.getCurrentDispatcher().addActionAndCreateUUID(action); 
		}
		FormElement formElement = new FormElement(paramAddress);
		if (width != null)
			formElement.setStyle("width", width);
		if (height != null)
			formElement.setStyle("height", height);
		if (target != null)
			formElement.setTarget(target);
		for (Component child : children) {
			formElement.addChild(child.constructWithStyles());
		}
		return formElement;
	}

}
