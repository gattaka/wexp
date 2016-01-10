package cz.gattserver.grass3.wexp.in;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.IHeightElement;
import cz.gattserver.grass3.wexp.out.IWidthElement;

public abstract class Layout extends Component implements IHeightElement, IWidthElement, Serializable {

	private static final long serialVersionUID = -6356421394623117195L;

	protected List<Component> children;

	protected String width;
	protected String height;

	public Layout addChild(Component... childList) {
		if (children == null)
			children = new ArrayList<Component>();
		for (Component child : childList)
			children.add(child);
		return this;
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
