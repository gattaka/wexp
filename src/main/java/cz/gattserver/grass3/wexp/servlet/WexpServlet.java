package cz.gattserver.grass3.wexp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.gattserver.grass3.wexp.Dispatcher;
import cz.gattserver.grass3.wexp.in.impl.UI;

public class WexpServlet extends HttpServlet {
	private static final long serialVersionUID = 7172666112080085629L;

	private static final String MAIN_UI_PARAM = "mainui";
	private static final ThreadLocal<Dispatcher> threadDispatcher = new ThreadLocal<Dispatcher>();
	private static final ThreadLocal<HttpServletRequest> threadHttpServletRequest = new ThreadLocal<HttpServletRequest>();

	private Class<? extends UI> mainUIClass;

	public static final String WEXP_RESOURCE_PREFIX = "WEXP";
	public static final String WEXP_RESOURCE_PATH = "/" + WEXP_RESOURCE_PREFIX;

	@Override
	public void init() throws ServletException {

		String className = getServletConfig().getInitParameter(MAIN_UI_PARAM);
		try {
			@SuppressWarnings("unchecked")
			Class<? extends UI> forName = (Class<? extends UI>) Class.forName(className);
			mainUIClass = forName;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.init();
	}

	public static Dispatcher getCurrentDispatcher() {
		return threadDispatcher.get();
	}

	public static HttpServletRequest getCurrentHttpServletRequest() {
		return threadHttpServletRequest.get();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		threadHttpServletRequest.set(req);

		// Nejde o request na CSS zdroje apod.?
		if (req.getPathInfo() != null && req.getPathInfo().startsWith(WEXP_RESOURCE_PATH)) {

			String filename = req.getPathInfo().substring(WEXP_RESOURCE_PATH.length());
			InputStream is = WexpServlet.class.getResourceAsStream(filename);
			if (is != null) {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr);
				PrintWriter writer = resp.getWriter();
				String text = "";

				//
				// We read the file line by line and later will be displayed on the
				// browser page.
				//
				while ((text = reader.readLine()) != null) {
					writer.println(text);
				}
				writer.flush();
				writer.close();
			}

		} else {

			// získej dispatcher dle session (každá session má jeden dispatcher)
			Dispatcher disp = Dispatcher.getSessionInstance(req.getSession().getId());
			// aby byl dostupný z celého kontextu vlákna
			threadDispatcher.set(disp);

			if (disp.getMainUI() == null) {
				try {
					UI mainUI = mainUIClass.getConstructor().newInstance();
					disp.setMainUI(mainUI);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			disp.write(req, resp);
		}
	}
}