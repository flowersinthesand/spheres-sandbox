package com.github.flowersinthesand.spheres;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP request-response exchange.
 * <p>
 * A response is regarded to be chunked automatically. A multipart/form-data POST request is not
 * supported, but you can handle those requests by calling {@link #unwrap(Class)} and using provider
 * specific API.
 * 
 * @author Donghwan Kim
 */
public interface HttpExchange extends Transport {

	/**
	 * The HTTP request method.
	 */
	Method method();

	/**
	 * A map of all request headers. The returned map is case insensitive.
	 */
	Map<String, List<String>> headers();

	/**
	 * The action to read the request body.
	 */
	void bodyAction(Action<String> action);

	/**
	 * Sets the status code for the response. The default value is OK(200).
	 */
	void status(Status status);

	/**
	 * Sets a response header.
	 */
	void header(String name, String value);

	/**
	 * Sets a set of headers that prevent the response from being cached.
	 */
	void setNoCacheHeaders();

	/**
	 * Sets a set of headers for Cross Origin Resource Sharing.
	 */
	void setCorsHeaders();

	/**
	 * Writes a string chunk to the response body.
	 */
	void write(String chunk);

	/**
	 * Writes a binary chunk to the response body.
	 */
	void write(ByteBuffer chunk);

	/**
	 * The action called when the response is over.
	 */
	void closeAction(Action<Void> action);

	/**
	 * Closes the response.
	 */
	void close();

	/**
	 * HTTP request methods.
	 */
	enum Method {
		OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
	}

	/**
	 * HTTP status codes.
	 */
	enum Status {
		CONTINUE(100), SWITCHING_PROTOCOLS(101), OK(200), CREATED(201), ACCEPTED(202), NON_AUTHORITATIVE_INFORMATION(203), NO_CONTENT(204), RESET_CONTENT(205), PARTIAL_CONTENT(206), MULTIPLE_CHOICES(300), MOVED_PERMANENTLY(301), MOVED_TEMPORARILY(302), FOUND(302), SEE_OTHER(303), NOT_MODIFIED(304), USE_PROXY(305), TEMPORARY_REDIRECT(307), BAD_REQUEST(400), UNAUTHORIZED(401), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404), METHOD_NOT_ALLOWED(405), NOT_ACCEPTABLE(406), PROXY_AUTHENTICATION_REQUIRED(407), REQUEST_TIMEOUT(408), CONFLICT(409), GONE(410), LENGTH_REQUIRED(411), PRECONDITION_FAILED(412), REQUEST_ENTITY_TOO_LARGE(413), REQUEST_URI_TOO_LONG(414), UNSUPPORTED_MEDIA_TYPE(415), REQUESTED_RANGE_NOT_SATISFIABLE(416), EXPECTATION_FAILED(417), INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501), BAD_GATEWAY(502), SERVICE_UNAVAILABLE(503), GATEWAY_TIMEOUT(504), HTTP_VERSION_NOT_SUPPORTED(505);

		private final int code;

		private Status(int code) {
			this.code = code;
		}

		/**
		 * Status code
		 */
		public int code() {
			return code;
		}

		/**
		 * Returns the enum constant of this type with the specified code.
		 */
		public static Status valueOf(int code) {
			for (Status status : values()) {
				if (status.code == code) {
					return status;
				}
			}
			throw new IllegalArgumentException("No matching HTTP status code for [" + code + "]");
		}
	}

}