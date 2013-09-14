package com.github.flowersinthesand.spheres;

public abstract class BridgeSupport implements Bridge, ManagerInsideAware, Initable {

	protected Actions<HttpExchange> httpActions = new SimpleActions<>(new Actions.Options());
	protected Actions<WebSocket> webSocketActions = new SimpleActions<>(new Actions.Options());
	protected ManagerInside manager;

	@Override
	public void init() {}

	@Override
	public Actions<HttpExchange> httpActions() {
		return httpActions;
	}

	@Override
	public Actions<WebSocket> webSocketActions() {
		return webSocketActions;
	}

	@Override
	public void setManagerInside(ManagerInside manager) {
		this.manager = manager;
	}

	protected String uri() {
		return manager.options().uri();
	}

}
