package com.github.flowersinthesand.spheres;

/**
 * Session manager.
 * 
 * @author Donghwan Kim
 */
public interface Manager extends AppBase<Manager> {

	/**
	 * Retrieves all the session and executes the given action. See, {@link SerializableAction} if
	 * you are going to cluster the application.
	 */
	Manager all(Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has a given id and executes the given action.
	 * See, {@link SerializableAction} if you are going to cluster the application.
	 */
	Manager byId(String id, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag name
	 * and executes the given action. See, {@link SerializableAction} if you are going to cluster
	 * the application.
	 */
	Manager byTag(String name, Action<? extends SessionBase> action);

	/**
	 * Finds the session whose the underlying socket has been tagged with the the specified tag
	 * names and executes the given action. See, {@link SerializableAction} if you are going to
	 * cluster the application.
	 */
	Manager byTag(String[] names, Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the HttpExchange is prepared.
	 */
	Manager httpAction(Action<HttpExchange> action);

	/**
	 * Registers an action to be called when the WebSocket is opened.
	 */
	Manager webSocketAction(Action<WebSocket> action);

	/**
	 * Registers an action to be called when the Socket is opened.
	 */
	Manager socketAction(Action<Socket> action);

}