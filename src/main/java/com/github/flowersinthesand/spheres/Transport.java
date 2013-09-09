package com.github.flowersinthesand.spheres;

/**
 * The Transport is entity carrying text message or binary message.
 * <p>
 * Though HTTP and WebSocket protocol are belong to the application protocol, in fact they are used
 * as a simple transport for the advanced protocol in realtime web application. Note that the word
 * "transport" doesn't mean techniques or hacks to simulate a duplex connection such as streaming
 * and long polling.
 * <p>
 * Transport implementations are not thread-safe.
 * 
 * @author Donghwan Kim
 */
public interface Transport extends UriAccessor, Wrapper {

}
