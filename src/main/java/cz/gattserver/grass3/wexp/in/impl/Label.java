package cz.gattserver.grass3.wexp.in.impl;

import java.io.Serializable;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.IHeightElement;
import cz.gattserver.grass3.wexp.out.IWidthElement;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.TextElement;

public class Label extends Component implements Serializable, IHeightElement, IWidthElement {

	private static final long serialVersionUID = -1765253789906039109L;

	private String caption;

	private String width;
	private String height;

	public Label(String caption) {
		this.caption = caption;
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		div.setCSSClass("wexp-label");
		div.addChild(new TextElement(caption));
		if (width != null)
			div.setStyle("width", width);
		if (height != null)
			div.setStyle("height", height);
		return div;
	}

	@Override
	public IWidthElement setWidth(String width) {
		this.width = width;
		return this;
	}

	@Override
	public IHeightElement setHeight(String height) {
		this.height = height;
		return this;
	}

}
