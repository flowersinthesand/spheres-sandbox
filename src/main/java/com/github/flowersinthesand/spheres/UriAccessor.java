package com.github.flowersinthesand.spheres;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * A mixin interface for an object that has a URI.
 * 
 * @author Donghwan Kim
 */
public interface UriAccessor {

	/**
	 * The URI.
	 */
	URI uri();

	/**
	 * A map of all parameters from the uri's query.
	 */
	Map<String, List<String>> params();

}