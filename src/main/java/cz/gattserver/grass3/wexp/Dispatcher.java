package cz.gattserver.grass3.wexp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cz.gattserver.grass3.wexp.in.impl.UI;
import cz.gattserver.grass3.wexp.servlet.WebApp;

public class Dispatcher {

	private static final String SESSION_ATTRIBUTE_NAME = "WEXP_DISPATCHER_INSTANCE";

	static final String WEXP_RESOURCE_PREFIX = "/wexp_resource";
	static final String WEXP_ACTION_PREFIX = "/wexp_action";

	private static final ThreadLocal<Dispatcher> threadDispatcher = new ThreadLocal<Dispatcher>();

	public static Dispatcher getCurrent() {
		return threadDispatcher.get();
	}

	/**
	 * Získá {@link Dispatcher} dle aktuální {@link HttpSession}, pokud není k aktuální session asociován žádny
	 * {@link Dispatcher}, je vytvořena nová instance a uložena do session pod SESSION_ATTRIBUTE_NAME klíčem
	 * 
	 * @param session
	 *            objekt aktuální session
	 * @return {@link Dispatcher} objekt asociovaný k aktuální session
	 */
	public static Dispatcher getSessionInstance(HttpSession session) {
		Dispatcher instance;
		Object obj = session.getAttribute(SESSION_ATTRIBUTE_NAME);
		if (obj != null) {
			if (obj instanceof Dispatcher) {
				instance = (Dispatcher) obj;
			} else {
				throw new IllegalStateException("Http session attribute " + SESSION_ATTRIBUTE_NAME
						+ " is taken by non-WEXP object: " + obj);
			}
		} else {
			instance = new Dispatcher();
			session.setAttribute(SESSION_ATTRIBUTE_NAME, instance);
			sessionMap.put(session, instance);
		}

		threadDispatcher.set(instance);
		return instance;
	}

	public static String addActionAndCreateAddress(DispatchAction action) {
		Integer hash = action.hashCode();
		getCurrent().actionMap.put("/" + hash, action);
		return Request.createActionPath(hash.toString());
	}

	private Dispatcher() {
	}

	private Map<String, DispatchAction> actionMap = new HashMap<String, DispatchAction>();
	private WebApp webApp;

	public void setWebApp(WebApp webApp) {
		this.webApp = webApp;
	}

	public void write(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String path = req.getPathInfo();
		List<String> pathChunks = new ArrayList<String>();
		if (path != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < path.length(); i++) {
				char ch = path.charAt(i);
				if (ch != '/') {
					sb.append(ch);
				} else {
					if (sb.length() > 0) {
						pathChunks.add(sb.toString());
						sb = new StringBuilder();
					}
				}
			}
			if (sb.length() > 0) {
				pathChunks.add(sb.toString());
			}
		}

		if (path == null || pathChunks.size() == 0) {
			/*
			 * Main UI
			 */
			UI mainUI = webApp.createMainUI();
			if (mainUI == null) {
				throw new IllegalStateException("Main UI must not be null");
			} else {
				OutputStream out = resp.getOutputStream();
				mainUI.construct().write(out);
				out.flush();
				out.close();
			}
		} else if (path.startsWith(WEXP_ACTION_PREFIX)) {
			/*
			 * Dynamická stránka
			 */
			String actionPath = path.substring(WEXP_ACTION_PREFIX.length());
			DispatchAction action = actionMap.get(actionPath);
			if (action == null) {
				// TODO 404
			} else {
				UI ui = action.dispatch(req);
				if (ui == null) {
					throw new IllegalStateException("Action result UI must not be null");
				} else {
					OutputStream out = resp.getOutputStream();
					ui.construct().write(out);
					out.flush();
					out.close();
				}
			}
		} else if (path.startsWith(WEXP_RESOURCE_PREFIX)) {
			/*
			 * CSS zdroje apod.
			 */
			String filename = path.substring(WEXP_RESOURCE_PREFIX.length());
			if (WEXP_STATUS_PREFIX.equals(filename.toLowerCase())) {
				PrintWriter writer = resp.getWriter();
				constructStatus(writer);
				writer.flush();
				writer.close();
			} else if (WEXP_GC_PREFIX.equals(filename.toLowerCase())) {
				PrintWriter writer = resp.getWriter();
				collectGarbage(writer);
				writer.flush();
				writer.close();
			} else {
				FileHandler.handleRequest(req, resp, filename);
			}
		} else {
			/*
			 * Statická bookmarkable stránka
			 */
			UI ui = webApp.createUI(pathChunks);
			if (ui == null) {
				throw new IllegalStateException("Action result UI must not be null");
			} else {
				OutputStream out = resp.getOutputStream();
				ui.construct().write(out);
				out.flush();
				out.close();
			}
		}
	}

	// ----------- DIAGNOSTICS -----------

	public static final String WEXP_STATUS_PREFIX = "/wexp_status";
	public static final String WEXP_GC_PREFIX = "/wexp_gc";

	private static Map<HttpSession, Dispatcher> sessionMap = new WeakHashMap<HttpSession, Dispatcher>();

	public static Map<HttpSession, Dispatcher> diagnostics_getInstances() {
		return sessionMap;
	}

	public int diagnostics_getActionMapSize() {
		return actionMap.size();
	}

	private void collectGarbage(PrintWriter writer) {
		System.gc();
		writer.println("Collected");
	}

	private void constructStatus(PrintWriter writer) {
		Map<HttpSession, Dispatcher> instances = Dispatcher.diagnostics_getInstances();
		writer.println("Sessions/Dispatchers: \t\t" + instances.size());
		for (HttpSession key : instances.keySet()) {
			// Session key
			writer.println("Session " + key.getId() + ":");
			Dispatcher instance = instances.get(key);
			writer.println("\tAction keys: \t\t" + instance.diagnostics_getActionMapSize());
			// LastAccessedTime
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(key.getLastAccessedTime());
				writer.println("\tLast accessed: \t\t" + cal.getTime());
				cal = Calendar.getInstance();
				cal.setTimeInMillis(key.getCreationTime());
				writer.println("\tCreation time: \t\t" + cal.getTime());
				writer.println("\tMax inactive: \t\t" + key.getMaxInactiveInterval() + "s");
			} catch (IllegalStateException e) {
				writer.println("\tInvalidated ... waiting for GC");
			}
		}
	}
}
