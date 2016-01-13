package cz.gattserver.grass3.wexp.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.gattserver.grass3.wexp.Dispatcher;
import cz.gattserver.grass3.wexp.Request;
import cz.gattserver.grass3.wexp.Session;

public class WexpServlet extends HttpServlet {

	private static final long serialVersionUID = 7172666112080085629L;

	private static final String WEBAPP_PARAM = "webapp";

	private WebApp webApp;

	@Override
	public void init() throws ServletException {

		// zjisti název WebAapp třídy
		String className = getServletConfig().getInitParameter(WEBAPP_PARAM);
		if (className == null) {
			throw new ServletException("Failed to initialize WexpServlet, WebApp was not specified");
		}

		// Zkus podle názvu třídu najít
		Class<? extends WebApp> webAppClass;
		try {
			@SuppressWarnings("unchecked")
			Class<? extends WebApp> clazz = (Class<? extends WebApp>) Class.forName(className);
			webAppClass = clazz;
		} catch (ClassNotFoundException e) {
			throw new ServletException("Failed to initialize WexpServlet, WebApp '" + className + "' not found", e);
		}

		// Zjisti, zda má požadovaný konstruktor
		Constructor<? extends WebApp> constructor = null;
		try {
			constructor = webAppClass.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new ServletException("Failed to initialize WexpServlet, WebApp '" + className
					+ "' does not have acceptable construtor", e);
		} catch (SecurityException e) {
			throw new ServletException("Failed to initialize WexpServlet, WebApp '" + className
					+ "' construtor check failed due to security reasons", e);
		}

		try {
			webApp = constructor.newInstance();
		} catch (Exception e) {
			throw new ServletException("Failed to initialize WexpServlet, failed to create a WebApp '" + className
					+ "' instance", e);
		}

		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Nastav aktuální request do thread-local viditelné helper objetku
		Request.setHttpServletRequest(req);

		// Nastav aktuální session do thread-local viditelné helper objetku
		Session.setHttpServletRequest(Request.getRequest().getSession());

		// získej dispatcher dle session (každá session má jeden dispatcher)
		Dispatcher disp = Dispatcher.getSessionInstance(Session.getSession());

		disp.setWebApp(webApp);
		disp.write(req, resp);
	}

}
