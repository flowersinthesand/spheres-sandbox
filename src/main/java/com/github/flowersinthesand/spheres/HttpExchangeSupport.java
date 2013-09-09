package com.github.flowersinthesand.spheres;

import java.util.List;
import java.util.Map;

public abstract class HttpExchangeSupport extends TransportSupport implements HttpExchange {

	private Map<String, List<String>> headers;
	private boolean bodyRead;
	protected Actions<String> bodyActions = new SimpleActions<>(new Actions.Options());
	protected Actions<Void> closeActions = new SimpleActions<>(new Actions.Options().once(true).memory(true));
	
	@Override
	public Map<String, List<String>> headers() {
		if (headers == null) {
			headers = lazyHeaders();
		}
		return headers;
	}

	protected abstract Map<String, List<String>> lazyHeaders();

	@Override
	public void bodyAction(Action<String> action) {
		bodyActions.add(action);
		if (bodyRead == false) {
			bodyRead = true;
			readBody();
		}
	}

	protected abstract void readBody();

	@Override
	public void closeAction(Action<Void> action) {
		closeActions.add(action);
	}

	@Override
	public void setNoCacheHeaders() {
		header("Cache-Control", "no-cache, no-store, must-revalidate");
		header("Pragma", "no-cache");
		header("Expires", "0");
	}

	@Override
	public void setCorsHeaders() {
		header("Access-Control-Allow-Origin", headers().containsKey("origin") ? headers().get("origin").get(0) : "*");
		header("Access-Control-Allow-Credentials", "true");
		if (headers().containsKey("Access-Control-Request-Headers")) {
			header("Access-Control-Allow-Headers", headers().get("Access-Control-Request-Headers").get(0));
		}
	}

}