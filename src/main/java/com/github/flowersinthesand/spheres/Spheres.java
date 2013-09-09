package com.github.flowersinthesand.spheres;

/**
 * Bootstrap class which is used to create a Spheres application.
 * 
 * @author Donghwan Kim
 */
public class Spheres {

	/**
	 * Creates an App with the given server, protocol and store.
	 */
	public static App createApp(Options options) {
		return new AppProxy(options);
	}

}