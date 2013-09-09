package com.github.flowersinthesand.spheres;

/**
 * Options to create an App.
 * 
 * @author Donghwan Kim
 */
public class Options {

	private String uri;
	private Bridge bridge;
	private Protocol protocol;
	private Messenger messenger;

	public Options() {}

	public String uri() {
		return uri;
	}

	public Options uri(String uri) {
		this.uri = uri;
		return this;
	}

	public Bridge bridge() {
		return bridge;
	}

	public Options bridge(Bridge bridge) {
		this.bridge = bridge;
		return this;
	}

	public Protocol protocol() {
		return protocol;
	}

	public Options protocol(Protocol protocol) {
		this.protocol = protocol;
		return this;
	}

	public Messenger messenger() {
		return messenger;
	}

	public Options messenger(Messenger messenger) {
		this.messenger = messenger;
		return this;
	}

}