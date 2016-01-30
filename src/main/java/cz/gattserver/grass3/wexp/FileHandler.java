package cz.gattserver.grass3.wexp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

public class FileHandler {

	private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.

	protected static String getMimeType(String filename) {
		InputStream stream = null;
		try {
			stream = FileHandler.class.getResourceAsStream(filename);
			AutoDetectParser parser = new AutoDetectParser();
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			parser.parse(stream, handler, metadata);
			return handler.toString();
		} catch (Exception e) {
			return "application/octet-stream";
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// není moc, co dělat...
			}
		}
	}

	public static boolean handleRequest(HttpServletRequest request, HttpServletResponse response, String filename)
			throws IOException {

		boolean content = "HEAD".equals(request.getMethod()) == false;

		// credit:
		// http://balusc.omnifaces.org/2009/02/fileservlet-supporting-resume-and.html

		InputStream input = null;
		try {
			input = FileHandler.class.getResourceAsStream(filename);
		} catch (Exception e) {
			// Do your thing if the file appears to be non-existing.
			// Throw an exception, or send 404, or show default/warning page, or just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "SC_NOT_FOUND");
			return true;
		}

		// Get content type by file name and set default GZIP support and content disposition.
		boolean acceptsGzip = false;
		String disposition = "inline";
		String contentType = getMimeType(filename);

		// If content type is text, then determine whether GZIP content encoding is supported by
		// the browser and expand content type with the one and right character encoding.
		if (contentType.startsWith("text")) {
			String acceptEncoding = request.getHeader("Accept-Encoding");
			acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip");
			contentType += ";charset=UTF-8";
		}

		// Else, expect for images, determine content disposition. If content type is supported by
		// the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
		else if (!contentType.startsWith("image")) {
			String accept = request.getHeader("Accept");
			disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
		}

		// Initialize response.
		// response.reset();
		// response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setHeader("Content-Disposition", disposition + ";filename=\"" + filename + "\"");
		response.setHeader("Accept-Ranges", "bytes");

		// Send requested file (part(s)) to client ------------------------------------------------

		// Prepare streams.
		OutputStream output = null;

		try {
			// Open streams.
			output = response.getOutputStream();

			response.setContentType(contentType);

			if (content) {
				if (acceptsGzip) {
					// The browser accepts GZIP, so GZIP the content.
					response.setHeader("Content-Encoding", "gzip");
					output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
				}

				// Copy full range.
				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int read;

				// Write full range.
				while ((read = input.read(buffer)) > 0) {
					output.write(buffer, 0, read);
				}
			}
		} finally {
			// Gently close streams.
			close(output);
			close(input);
		}

		return true; // We wrote a response
	}

	/**
	 * Returns true if the given accept header accepts the given value.
	 * 
	 * @param acceptHeader
	 *            The accept header.
	 * @param toAccept
	 *            The value to be accepted.
	 * @return True if the given accept header accepts the given value.
	 */
	private static boolean accepts(String acceptHeader, String toAccept) {
		String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
		Arrays.sort(acceptValues);
		return Arrays.binarySearch(acceptValues, toAccept) > -1
				|| Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
				|| Arrays.binarySearch(acceptValues, "*/*") > -1;
	}

	/**
	 * Close the given resource.
	 * 
	 * @param resource
	 *            The resource to be closed.
	 */
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {
				// Ignore IOException. If you want to handle this anyway, it might be useful to know
				// that this will generally only be thrown when the client aborted the request.
			}
		}
	}

}
