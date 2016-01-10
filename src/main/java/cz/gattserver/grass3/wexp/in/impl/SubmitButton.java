package cz.gattserver.grass3.wexp.in.impl;

import java.io.Serializable;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.InputElement;

public class SubmitButton extends Component implements Serializable {

	private static final long serialVersionUID = -593016947307067095L;

	private static final String INPUT_TYPE = "submit";

	private String caption;

	public SubmitButton(String caption) {
		setCaption(caption);
	}

	public String getCaption() {
		return caption;
	}

	public SubmitButton setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		InputElement inputElement = new InputElement(INPUT_TYPE);
		inputElement.setValue(caption);
		div.addChild(inputElement);
		return div;
	}

}
