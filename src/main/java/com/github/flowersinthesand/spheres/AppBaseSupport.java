package com.github.flowersinthesand.spheres;

public abstract class AppBaseSupport implements AppBase {

	protected final SessionManager sessionManager;

	public AppBaseSupport(SessionManager sessionManager) {
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
