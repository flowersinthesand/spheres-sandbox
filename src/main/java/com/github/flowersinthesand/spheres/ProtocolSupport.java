package com.github.flowersinthesand.spheres;

import java.util.Map;

public abstract class ProtocolSupport implements Protocol, AppInsideAware {

	protected Actions<Socket> socketActions = new ConcurrentActions<>(new Actions.Options());
	protected Actions<SessionBase> sessionActions = new ConcurrentActions<>(new Actions.Options());
	protected AppInside app;
	protected Map<String, Socket> sockets;

	@Override
	public Actions<Socket> socketActions() {
		return socketActions;
	}

	@Override
	public Actions<SessionBase> sessionActions() {
		return sessionActions;
	}

	@Override
	public void setAppInside(AppInside app) {
		this.app = app;
		this.sockets = app.sockets();
	}

}
