package cz.gattserver.grass3.wexp.out.impl;

import cz.gattserver.grass3.wexp.out.TagElement;

public class FormElement extends TagElement {

	private static final String ADDRESS_ATTRIBUTE = "action";
	private static final String TARGET_ATTRIBUTE = "target";
	private static final String METHOD_ATTRIBUTE = "method";

	public FormElement(String address) {
		setAddress(address);
		setAttribute(METHOD_ATTRIBUTE, "GET");
	}

	public FormElement setAddress(String address) {
		setAttribute(ADDRESS_ATTRIBUTE, address);
		return this;
	}

	public String getAddress() {
		return getAttribute(ADDRESS_ATTRIBUTE);
	}

	public FormElement setTarget(String target) {
		setAttribute(TARGET_ATTRIBUTE, target);
		return this;
	}

	public String getTarget() {
		return getAttribute(TARGET_ATTRIBUTE);
	}

	@Override
	protected String getTagName() {
		return "form";
	}

}
