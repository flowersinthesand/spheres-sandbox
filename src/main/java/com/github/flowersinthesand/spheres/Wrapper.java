package com.github.flowersinthesand.spheres;

/**
 * Interface to retrieve the provider-specific components.
 * 
 * @author Donghwan Kim
 */
public interface Wrapper {

	/**
	 * Returns the provider-specific component.
	 */
	<T> T unwrap(Class<T> clazz);

}
