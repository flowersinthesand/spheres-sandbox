package com.github.flowersinthesand.spheres;

public abstract class SessionBaseSupport implements SessionBase {

	protected final Socket socket;

	protected SessionBaseSupport(Socket socket) {
		this.socket = socket;
	}

	@Override
	public boolean equals(Object obj) {
		return socket.equals(obj);
	}

	@Override
	public int hashCode() {
		return socket.hashCode();
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		return Socket.class.isAssignableFrom(clazz) ? clazz.cast(socket) : null;
	}

}
