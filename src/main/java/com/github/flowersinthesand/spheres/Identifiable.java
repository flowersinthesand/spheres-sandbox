package com.github.flowersinthesand.spheres;

/**
 * Interface for entity that is needed to be identifiable.
 * 
 * @author Donghwan Kim
 */
public interface Identifiable {

	/**
	 * The Id.
	 */
	String id();

	/**
	 * If two identifiables' id are equal, they are same.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * If two identifiables are equal, their hashCode must return the same value.
	 */
	@Override
	int hashCode();

}
