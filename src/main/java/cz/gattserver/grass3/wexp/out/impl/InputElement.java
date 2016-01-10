package cz.gattserver.grass3.wexp.out.impl;

import cz.gattserver.grass3.wexp.out.TagElement;

public class InputElement extends TagElement {

	private static final String TYPE_ATTRIBUTE = "type";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String VALUE_ATTRIBUTE = "value";

	public InputElement(String type) {
		setType(type);
	}

	public InputElement setType(String type) {
		setAttribute(TYPE_ATTRIBUTE, type);
		return this;
	}

	public String getType() {
		return getAttribute(TYPE_ATTRIBUTE);
	}

	public InputElement setName(String name) {
		setAttribute(NAME_ATTRIBUTE, name);
		return this;
	}

	public String getName() {
		return getAttribute(NAME_ATTRIBUTE);
	}

	public InputElement setValue(String value) {
		setAttribute(VALUE_ATTRIBUTE, value);
		return this;
	}

	public String getValue() {
		return getAttribute(VALUE_ATTRIBUTE);
	}

	@Override
	protected String getTagName() {
		return "a";
	}

}
