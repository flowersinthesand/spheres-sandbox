package com.github.flowersinthesand.spheres;

/**
 * The Socket is an interface to generalize identifiable physical connectivity between the endpoints
 * established on any transport.
 * <p>
 * This interface is designed to be used by multi-threads.
 * 
 * @author Donghwan Kim
 */
public interface Socket extends SocketBase, Identifiable, Taggable, Wrapper {}
