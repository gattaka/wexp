package cz.gattserver.grass3.wexp.out.impl;

public class InputElement extends FormPartElement {

	private static final String TYPE_ATTRIBUTE = "type";

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

	@Override
	protected String getTagName() {
		return "input";
	}

}
