package com.github.flowersinthesand.spheres;

/**
 * As a bridge between the application and the real server, Bridge maps server-specific connection
 * objects to transport entities as typical glue code does.
 * <p>
 * Multiple Bridge instances with a separated URI should be able to share one real server to allow
 * to install multiple applications on one server.
 * 
 * @author Donghwan Kim
 */
public interface Bridge {

	/**
	 * The Actions for the HttpExchange.
	 */
	Actions<HttpExchange> httpActions();

	/**
	 * The Actions for the WebSocket.
	 */
	Actions<WebSocket> webSocketActions();

}