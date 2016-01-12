package cz.gattserver.grass3.wexp.out.impl;

public class TextAreaElement extends FormPartElement {

	private TextElement valueChild;

	@Override
	public FormPartElement setValue(String value) {
		if (value != null) {
			if (valueChild == null) {
				valueChild = new TextElement(value);
				addChild(valueChild);
			}
			valueChild.setText(value);
		}
		return this;
	}

	@Override
	public String getValue() {
		return valueChild.getText();
	}

	@Override
	protected String getTagName() {
		return "textarea";
	}

}
