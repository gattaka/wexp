package cz.gattserver.grass3.wexp.in.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.gattserver.grass3.wexp.in.Component;
import cz.gattserver.grass3.wexp.out.impl.DivElement;
import cz.gattserver.grass3.wexp.out.impl.Page;
import cz.gattserver.grass3.wexp.out.impl.StyleElement;
import cz.gattserver.grass3.wexp.out.impl.TextElement;

public class UI implements Serializable {

	private static final long serialVersionUID = 7601481916429700121L;

	protected Component content;

	protected Map<String, CSSRule> css;
	protected String favicon;
	protected Set<String> cssFiles;

	private String charset = "UTF-8";

	public UI setCSSClass(CSSRule rule) {
		if (css == null)
			css = new HashMap<String, CSSRule>();
		css.put(rule.getName(), rule);
		return this;
	}

	public UI setCSSFile(String file) {
		if (cssFiles == null)
			cssFiles = new HashSet<String>();
		cssFiles.add(file);
		return this;
	}

	public UI setFavicon(String favicon) {
		this.favicon = favicon;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public UI setContent(Component content) {
		this.content = content;
		return this;
	}

	public Page construct() {
		Page page = new Page(charset);
		if (css != null) {
			StyleElement style = new StyleElement();
			String text = "";
			for (String cssRuleKey : css.keySet()) {
				CSSRule c = css.get(cssRuleKey);
				text += c.getName() + " { ";
				for (String key : c.getStyles().keySet()) {
					text += key + ": " + c.getStyles().get(key) + ";";
				}
				text += " } ";
			}
			TextElement cssText = new TextElement(text);
			style.addChild(cssText);
			page.addHeader(style);
		}
		if (cssFiles != null) {
			for (String file : cssFiles) {
				String cssLink = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + file + "\" >";
				TextElement textElement = new TextElement(cssLink);
				page.addHeader(textElement);
			}
			page.addHeader(new TextElement(
					"<meta name=\"viewport\" content=\"initial-scale=1.0, width=device-width, user-scalable=no\">"));
			page.addHeader(new TextElement("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"));

		}
		if (favicon != null) {
			page.addHeader(new TextElement("<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\""
					+ favicon + "\" />"));
			page.addHeader(new TextElement("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\"" + favicon
					+ "\" />"));
		}

		DivElement mainLayout = new DivElement();
		mainLayout.setCSSClass("wexp-main-layout");
		mainLayout.addChild(content.constructWithStyles());
		page.addChild(mainLayout);
		return page;
	}
}
