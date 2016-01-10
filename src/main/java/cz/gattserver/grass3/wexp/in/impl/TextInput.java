package cz.gattserver.grass3.wexp.in.impl;

import java.io.Serializable;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.InputElement;

public class TextInput extends Component implements Serializable {

	private static final long serialVersionUID = -593016947307067095L;

	private static final String INPUT_TYPE = "text";

	private String name;

	public TextInput(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public TextInput setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		InputElement inputElement = new InputElement(INPUT_TYPE);
		inputElement.setName(name);
		div.addChild(inputElement);
		return div;
	}

}
