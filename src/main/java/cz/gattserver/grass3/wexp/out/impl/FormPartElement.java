package cz.gattserver.grass3.wexp.out.impl;

import cz.gattserver.grass3.wexp.out.TagElement;

public abstract class FormPartElement extends TagElement {

	private static final String NAME_ATTRIBUTE = "name";
	private static final String VALUE_ATTRIBUTE = "value";

	public FormPartElement setName(String name) {
		setAttribute(NAME_ATTRIBUTE, name);
		return this;
	}

	public String getName() {
		return getAttribute(NAME_ATTRIBUTE);
	}

	public FormPartElement setValue(String value) {
		setAttribute(VALUE_ATTRIBUTE, value);
		return this;
	}

	public String getValue() {
		return getAttribute(VALUE_ATTRIBUTE);
	}

}
