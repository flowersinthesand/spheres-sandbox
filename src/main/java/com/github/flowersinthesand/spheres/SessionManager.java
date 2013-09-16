package com.github.flowersinthesand.spheres;

/**
 * Session manager.
 * 
 * @author Donghwan Kim
 */
public interface SessionManager {

	/**
	 * Retrieves all the session and executes the given action. See, {@link SerializableAction} if
	 * you are going to cluster the application.
	 */
	SessionManager all(Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has a given id and executes the given action.
	 * See, {@link SerializableAction} if you are going to cluster the application.
	 */
	SessionManager byId(String id, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag name
	 * and executes the given action. See, {@link SerializableAction} if you are going to cluster
	 * the application.
	 */
	SessionManager byTag(String name, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag
	 * names and executes the given action. See, {@link SerializableAction} if you are going to
	 * cluster the application.
	 */
	SessionManager byTag(String[] names, Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the HttpExchange is prepared.
	 */
	SessionManager httpAction(Action<HttpExchange> action);

	/**
	 * Registers an action to be called when the WebSocket is opened.
	 */
	SessionManager webSocketAction(Action<WebSocket> action);

	/**
	 * Registers an action to be called when the Socket is opened.
	 */
	SessionManager socketAction(Action<Socket> action);

	/**
	 * Registers an action to be called when the Session is opened.
	 */
	SessionManager sessionAction(Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the application closes.
	 */
	SessionManager closeAction(Action<Void> action);

	/**
	 * Closes the application
	 */
	void close();

}