package com.github.flowersinthesand.spheres;

import java.util.concurrent.ConcurrentMap;

/**
 * This interface provides services for internal use only and any operation on this instance affects
 * only local application.
 * <p>
 * This interface is not designed to be used by the application developer.
 * 
 * @author Donghwan Kim
 */
public interface ManagerInside extends Manager {

	/**
	 * Initializes the application.
	 */
	void install();

	/**
	 * Returns the read-only options used to create.
	 */
	Options options();

	/**
	 * Returns a map of WsSocket and HttpSocket.
	 */
	ConcurrentMap<String, Socket> sockets();

}
