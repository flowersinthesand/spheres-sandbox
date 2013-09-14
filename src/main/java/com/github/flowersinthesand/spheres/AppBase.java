package com.github.flowersinthesand.spheres;

/**
 * The AppBase is provided as an amorphous base interface for the App interface.
 * <p>
 * The App represents the Spheres application following the specific protocol. A Spheres application
 * is a realtime web application that consists of sockets and actions handling them and runs within
 * a web server on JVM.
 * <p>
 * This concept is resulted from the thought that to support various application protocols and their
 * all the fine features, abstracting into a single interface is not possible and not useful if
 * ever.
 * 
 * @author Donghwan Kim
 */
public interface AppBase<T> {

	/**
	 * Registers an action to be called when the Session is opened.
	 */
	T sessionAction(Action<? extends SessionBase> action);

	/**
	 * Registers an action to be called when the application closes.
	 */
	T closeAction(Action<Void> action);

	/**
	 * Closes the application
	 */
	void close();

}
