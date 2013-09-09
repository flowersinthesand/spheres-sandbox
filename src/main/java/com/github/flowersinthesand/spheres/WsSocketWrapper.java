package com.github.flowersinthesand.spheres;

import java.nio.ByteBuffer;

public abstract class WsSocketWrapper extends SocketSupport implements WsSocket {

	private WebSocket webSocket;

	public WsSocketWrapper(WebSocket webSocket) {
		super(webSocket);
		this.webSocket = webSocket;
	}

	@Override
	public synchronized void textAction(Action<String> action) {
		webSocket.textAction(action);
	}

	@Override
	public synchronized void binaryAction(Action<ByteBuffer> action) {
		webSocket.binaryAction(action);
	}

	@Override
	public synchronized void closeAction(Action<Void> action) {
		webSocket.closeAction(action);
	}

	@Override
	public synchronized void send(ByteBuffer data) {
		webSocket.send(data);
	}

	@Override
	public synchronized void send(String data) {
		webSocket.send(data);
	}

	@Override
	public synchronized void write(String data) {
		webSocket.send(data);
	}

	@Override
	public synchronized void close() {
		webSocket.close();
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		return WebSocket.class.isAssignableFrom(clazz) ? clazz.cast(webSocket) : null;
	}

}
