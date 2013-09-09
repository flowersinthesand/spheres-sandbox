package com.github.flowersinthesand.spheres;

/**
 * An action.
 * 
 * @author Donghwan Kim
 */
public interface Action<T> {

	/**
	 * Some action is taken.
	 */
	void on(T object);

}
