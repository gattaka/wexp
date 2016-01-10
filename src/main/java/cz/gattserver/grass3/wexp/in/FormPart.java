package cz.gattserver.grass3.wexp.in;

import java.io.Serializable;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.IWidthElement;

public abstract class FormPart extends Component implements Serializable, IWidthElement {

	private static final long serialVersionUID = -1696132859542748449L;

	protected String name;
	protected String value;
	protected String width;

	public FormPart(String name) {
		setName(name);
	}

	public String getValue() {
		return value;
	}

	public FormPart setValue(String value) {
		this.value = value;
		return this;
	}

	public String getName() {
		return name;
	}

	public FormPart setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IWidthElement setWidth(String width) {
		this.width = width;
		return this;
	}

}
