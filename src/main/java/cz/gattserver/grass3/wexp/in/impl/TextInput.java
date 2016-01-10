package cz.gattserver.grass3.wexp.in.impl;

import cz.gattserver.grass3.wexp.in.FormPart;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.InputElement;

public class TextInput extends FormPart {

	private static final long serialVersionUID = -593016947307067095L;

	private static final String INPUT_TYPE = "text";

	public TextInput(String name) {
		super(name);
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		InputElement inputElement = new InputElement(INPUT_TYPE);
		if (width != null)
			inputElement.setStyle("width", width);
		inputElement.setName(name);
		div.addChild(inputElement);
		return div;
	}

}
