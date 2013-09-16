package com.github.flowersinthesand.spheres;

import java.io.Serializable;

/**
 * In the case where the application is clustered, to call SessionManager's methods retrieving session like
 * {@link SessionManager#all(Action)}, action to be passed should be able to be serialized. In other words,
 * only in those situations, an action should implement {@link Serializable}.
 * <p>
 * See the provided link, serialization of inner classes including local and anonymous classes, is
 * discouraged and doesn't work in some cases.
 * 
 * @author Donghwan Kim
 * @see <a
 *      href="http://docs.oracle.com/javase/7/docs/platform/serialization/spec/serial-arch.html#4539">Note
 *      of the Serializable Interface</a>
 */
public interface SerializableAction<T> extends Action<T>, Serializable {}