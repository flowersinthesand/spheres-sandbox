package com.github.flowersinthesand.spheres;

import java.util.Set;

/**
 * Interface for entity that is able to be tagged.
 * 
 * @author Donghwan Kim
 */
public interface Taggable {

	/**
	 * Returns the tag set.
	 */
	Set<String> tags();

}
