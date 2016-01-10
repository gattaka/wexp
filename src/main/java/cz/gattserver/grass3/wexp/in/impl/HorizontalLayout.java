package cz.gattserver.grass3.wexp.in.impl;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.in.Layout;
import cz.gattserver.grass3.wexp.out.WebElement;
import cz.gattserver.grass3.wexp.out.impl.DivElement;

public class HorizontalLayout extends Layout {

	private static final long serialVersionUID = 4009745412791333101L;

	@Override
	public WebElement construct() {
		DivElement div = new DivElement();
		div.setCSSClass("wexp-horizontal-layout");
		if (width != null)
			div.setStyle("width", width);
		if (height != null)
			div.setStyle("height", height);
		for (Component child : children) {
			DivElement subDiv = new DivElement();
			subDiv.setStyle("float", "left");
			subDiv.addChild(child.constructWithStyles());
			div.addChild(subDiv);
		}
		return div;
	}

}
