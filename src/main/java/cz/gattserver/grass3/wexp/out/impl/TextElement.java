package cz.gattserver.grass3.wexp.out.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;

import cz.gattserver.grass3.wexp.out.WebElement;

public class TextElement extends WebElement {

	private String text;

	public TextElement(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public TextElement setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public void write(OutputStreamWriter o) throws IOException {
		o.write(text);
	}

}
