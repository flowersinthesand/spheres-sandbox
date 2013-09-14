package com.github.flowersinthesand.spheres;

/**
 * Interface to inject the ManagerInside.
 * 
 * @author Donghwan Kim
 */
public interface ManagerInsideAware {

	/**
	 * Provides the ManagerInside instance.
	 */
	void setManagerInside(ManagerInside manager);

}
