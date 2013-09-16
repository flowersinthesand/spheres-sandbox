package com.github.flowersinthesand.spheres;

import java.util.Map;

public abstract class ProtocolSupport implements Protocol, SessionManagerInsideAware, Initable {

	protected Actions<Socket> socketActions = new ConcurrentActions<>(new Actions.Options());
	protected Actions<SessionBase> sessionActions = new ConcurrentActions<>(new Actions.Options());
	protected SessionManagerInside manager;
	protected Map<String, Socket> sockets;
	
	@Override
	public void init() {}

	@Override
	public Actions<Socket> socketActions() {
		return socketActions;
	}

	@Override
	public Actions<SessionBase> sessionActions() {
		return sessionActions;
	}

	@Override
	public void setManagerInside(SessionManagerInside manager) {
		this.manager = manager;
		this.sockets = manager.sockets();
	}

}
