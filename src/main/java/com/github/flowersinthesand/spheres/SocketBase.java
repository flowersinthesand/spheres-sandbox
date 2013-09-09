package com.github.flowersinthesand.spheres;

/**
 * A base interface for all socket interface.
 * 
 * @author Donghwan Kim
 */
public interface SocketBase extends UriAccessor {

	/**
	 * Attaches an action for the text message event.
	 */
	void textAction(Action<String> action);

	/**
	 * Attaches an action to be called on close.
	 */
	void closeAction(Action<Void> action);

	/**
	 * Sends a text message.
	 */
	void send(String data);

	/**
	 * Closes the socket.
	 */
	void close();

}