package cz.gattserver.grass3.wexp.in;

import java.util.ArrayList;
import java.util.List;

import cz.gattserver.grass3.wexp.out.TagElement;
import cz.gattserver.grass3.wexp.out.WebElement;

public abstract class Component {

	protected List<String> classes;

	public Component setCSSClass(String name) {
		if (classes == null)
			classes = new ArrayList<String>();
		classes.add(name);
		return this;
	}

	public WebElement constructWithStyles() {
		WebElement element = construct();
		if (classes != null && element instanceof TagElement) {
			TagElement tagElement = (TagElement) element;
			for (String clas : classes)
				tagElement.setCSSClass(clas);
		}
		return element;
	}

	public abstract WebElement construct();

}
