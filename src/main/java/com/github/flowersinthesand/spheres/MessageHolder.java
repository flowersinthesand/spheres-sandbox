package com.github.flowersinthesand.spheres;

import java.io.Serializable;
import java.util.Map;

/**
 * Holder wrapping a message object.
 * 
 * @author Donghwan Kim
 */
public interface MessageHolder extends Wrapper {

	/**
	 * The published message object.
	 */
	Map<String, Serializable> get();

}
