package com.github.flowersinthesand.spheres;

/**
 * Interface representing the Spheres application.
 * <p>
 * A Spheres application is a realtime web application that consists of sockets and actions handling
 * them and runs within a web server on JVM. An App instance is used to find and handle session
 * which is a final interface for the application developer.
 * 
 * @author Donghwan Kim
 */
public interface App {

	/**
	 * Retrieves all the session and executes the given action. See, {@link SerializableAction} if
	 * you are going to cluster the application.
	 */
	App all(Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has a given id and executes the given action.
	 * See, {@link SerializableAction} if you are going to cluster the application.
	 */
	App byId(String id, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag name
	 * and executes the given action. See, {@link SerializableAction} if you are going to cluster
	 * the application.
	 */
	App byTag(String name, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag
	 * names and executes the given action. See, {@link SerializableAction} if you are going to
	 * cluster the application.
	 */
	App byTag(String[] names, Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the HttpExchange is prepared.
	 */
	App httpAction(Action<HttpExchange> action);

	/**
	 * Registers an action to be called when the WebSocket is opened.
	 */
	App webSocketAction(Action<WebSocket> action);

	/**
	 * Registers an action to be called when the Socket is opened.
	 */
	App socketAction(Action<Socket> action);

	/**
	 * Registers an action to be called when the Session is opened.
	 */
	App sessionAction(Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the application closes.
	 */
	App closeAction(Action<Void> action);

	/**
	 * Closes the application
	 */
	void close();

}