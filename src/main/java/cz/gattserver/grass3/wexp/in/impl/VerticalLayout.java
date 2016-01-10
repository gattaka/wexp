package cz.gattserver.grass3.wexp.in.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.IHeightElement;
import cz.gattserver.grass3.wexp.out.IWidthElement;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;

public class VerticalLayout extends Component implements IHeightElement, IWidthElement, Serializable {

	private static final long serialVersionUID = 7896360454284020046L;

	protected List<Component> children;

	private String width;
	private String height;

	public VerticalLayout addChild(Component... childList) {
		if (children == null)
			children = new ArrayList<Component>();
		for (Component child : childList)
			children.add(child);
		return this;
	}

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		div.setCSSClass("wexp-vertical-layout");
		if (width != null)
			div.setStyle("width", width);
		if (height != null)
			div.setStyle("height", height);
		for (Component child : children) {
			DivElement subDiv = new DivElement();
			subDiv.addChild(child.constructWithStyles());
			div.addChild(subDiv);
		}
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
