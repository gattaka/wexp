package cz.gattserver.grass3.wexp;

import java.lang.ref.WeakReference;

import javax.servlet.http.HttpSession;

public class Session {

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>() {
		protected Session initialValue() {
			return new Session();
		};
	};

	public static Session getCurrent() {
		return threadSession.get();
	}

	public static void setHttpServletRequest(HttpSession session) {
		getCurrent().session = new WeakReference<HttpSession>(session);
	}

	public static HttpSession getSession() {
		return getCurrent().session.get();
	}

	private WeakReference<HttpSession> session;

	private Session() {
	}

}
