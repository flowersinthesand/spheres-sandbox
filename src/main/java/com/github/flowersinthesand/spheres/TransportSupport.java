package com.github.flowersinthesand.spheres;

import java.net.URI;
import java.util.List;
import java.util.Map;

public abstract class TransportSupport implements Transport {

	private URI uri;
	private Map<String, List<String>> params;

	@Override
	public URI uri() {
		if (uri == null) {
			uri = lazyUri();
		}
		return uri;
	}

	protected abstract URI lazyUri();

	@Override
	public Map<String, List<String>> params() {
		if (params == null) {
			params = lazyParams();
		}
		return params;
	}

	protected abstract Map<String, List<String>> lazyParams();

}