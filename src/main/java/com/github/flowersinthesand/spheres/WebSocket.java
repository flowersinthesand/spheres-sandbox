package com.github.flowersinthesand.spheres;

import java.nio.ByteBuffer;

/**
 * Represents the WebSocket API by W3C.
 * <p>
 * Though WebSocket is Socket literally, it is treated as a transport here.
 * 
 * @author Donghwan Kim
 */
public interface WebSocket extends Transport, SocketBase {

	/**
	 * Attaches an action for the binary message event.
	 */
	void binaryAction(Action<ByteBuffer> action);

	/**
	 * Sends a binary message.
	 */
	void send(ByteBuffer data);

}