package com.github.flowersinthesand.spheres;

/**
 * Interface to inject the AppInside.
 * 
 * @author Donghwan Kim
 */
public interface AppInsideAware {

	/**
	 * Provides the AppInside instance.
	 */
	void setAppInside(AppInside app);

}
