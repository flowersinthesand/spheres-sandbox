package com.github.flowersinthesand.spheres;

/**
 * The SessionBase is provided as an amorphous base interface for the Session interface that is a
 * final interface for the application developer to interact a remote endpoint in realtime following
 * the protocol.
 * <p>
 * This concept is resulted from the thought that to support various application protocols and their
 * all the fine features, abstracting into a single interface is not possible and not useful if
 * ever.
 * 
 * @author Donghwan Kim
 */
public interface SessionBase extends Wrapper {

	/**
	 * As an wrapper, the implementation should provide the underlying socket at least.
	 */
	@Override
	<T> T unwrap(Class<T> clazz);

}
