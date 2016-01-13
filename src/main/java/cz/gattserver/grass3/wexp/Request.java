package cz.gattserver.grass3.wexp;

import java.lang.ref.WeakReference;

import javax.servlet.http.HttpServletRequest;

public class Request {

	private static final ThreadLocal<Request> threadRequest = new ThreadLocal<Request>() {
		protected Request initialValue() {
			return new Request();
		};
	};

	public static Request getCurrent() {
		return threadRequest.get();
	}

	public static void setHttpServletRequest(HttpServletRequest req) {
		getCurrent().request = new WeakReference<HttpServletRequest>(req);
	}

	public static HttpServletRequest getRequest() {
		return getCurrent().request.get();
	}

	public static String getPathPrefix() {
		HttpServletRequest request = getRequest();
		String prefix = request.getContextPath() + request.getServletPath();
		return prefix;
	}

	public static String createResourcePath(String... pathChunks) {
		return createPrefixedPath(Dispatcher.WEXP_RESOURCE_PREFIX, pathChunks);
	}

	public static String createActionPath(String... pathChunks) {
		return createPrefixedPath(Dispatcher.WEXP_ACTION_PREFIX, pathChunks);
	}

	public static String createPath(String... pathChunks) {
		return createPrefixedPath("", pathChunks);
	}

	private static String createPrefixedPath(String prefix, String... pathChunks) {
		String path = getPathPrefix() + prefix;
		for (String chunk : pathChunks)
			path += "/" + chunk;
		return path;
	}

	private WeakReference<HttpServletRequest> request;

	private Request() {
	}
}
