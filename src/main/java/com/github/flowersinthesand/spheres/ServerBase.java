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
public interface ServerBase extends Wrapper {

	/**
	 * As an wrapper, the implementation should provide the underlying session manager at least.
	 */
	@Override
	<T> T unwrap(Class<T> clazz);

	/**
	 * Closes the application
	 */
	void close();

}
