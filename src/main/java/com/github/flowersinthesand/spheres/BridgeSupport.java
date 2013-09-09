package com.github.flowersinthesand.spheres;

public abstract class BridgeSupport implements Bridge, AppInsideAware {

	protected Actions<HttpExchange> httpActions = new SimpleActions<>(new Actions.Options());
	protected Actions<WebSocket> webSocketActions = new SimpleActions<>(new Actions.Options());
	protected AppInside app;

	@Override
	public Actions<HttpExchange> httpActions() {
		return httpActions;
	}

	@Override
	public Actions<WebSocket> webSocketActions() {
		return webSocketActions;
	}

	@Override
	public void setAppInside(AppInside app) {
		this.app = app;
	}

	protected String uri() {
		return app.options().uri();
	}

}
