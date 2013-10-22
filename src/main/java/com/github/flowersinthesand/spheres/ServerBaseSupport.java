package com.github.flowersinthesand.spheres;

public abstract class ServerBaseSupport implements ServerBase {

	protected final SessionManager sessionManager;

	public ServerBaseSupport(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void close() {
		sessionManager.close();
	}
	
	@Override
	public <T> T unwrap(Class<T> clazz) {
		return SessionManager.class.isAssignableFrom(clazz) ? clazz.cast(sessionManager) : null;
	}

}
