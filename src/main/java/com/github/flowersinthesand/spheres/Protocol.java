package com.github.flowersinthesand.spheres;

/**
 * The Protocol provides {@link HttpSocket}, {@link WsSocket} and {@link SessionBase}.
 * 
 * @author Donghwan Kim
 */
public interface Protocol {

	/**
	 * Handles an HttpExchange.
	 */
	void on(HttpExchange http);

	/**
	 * Handles an Websocket.
	 */
	void on(WebSocket webSocket);

	/**
	 * The Actions for the Socket.
	 */
	Actions<Socket> socketActions();

	/**
	 * Handles a Socket. Usually creates a session based on the supplied socket.
	 */
	void on(Socket socket);

	/**
	 * The Actions for the Session.
	 */
	Actions<SessionBase> sessionActions();

}