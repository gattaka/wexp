package cz.gattserver.grass3.wexp.in.impl;

import cz.gattserver.grass3.wexp.in.FormPart;
import cz.gattserver.grass3.wexp.out.IHeightElement;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.TextAreaElement;

public class TextArea extends FormPart implements IHeightElement {

	private static final long serialVersionUID = -593016947307067095L;

	private String height;

	public TextArea(String name) {
		super(name);
	}

	@Override
	public IHeightElement setHeight(String height) {
		this.height = height;
		return this;
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		TextAreaElement textAreaElement = new TextAreaElement();
		if (width != null)
			textAreaElement.setStyle("width", width);
		if (height != null)
			textAreaElement.setStyle("height", height);
		textAreaElement.setName(name);
		div.addChild(textAreaElement);
		return div;
	}

}
