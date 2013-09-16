package com.github.flowersinthesand.spheres;

/**
 * Interface to inject the SessionManagerInside.
 * 
 * @author Donghwan Kim
 */
public interface SessionManagerInsideAware {

	/**
	 * Provides the SessionManagerInside instance.
	 */
	void setManagerInside(SessionManagerInside manager);

}
