package cz.gattserver.grass3.wexp.servlet;

import java.util.List;

import cz.gattserver.grass3.wexp.in.impl.UI;

public interface WebApp {

	public void init();

	public UI createMainUI();

	public UI createUI(List<String> pathChunks);

}
