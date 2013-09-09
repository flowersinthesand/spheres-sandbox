package com.github.flowersinthesand.spheres;

import java.net.URI;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class SocketSupport implements Socket {

	protected static final String padding2K = CharBuffer.allocate(2048).toString().replace('\0', ' ');

	private Set<String> tags = new CopyOnWriteArraySet<>();
	protected Actions<Void> closeActions = new ConcurrentActions<>(new Actions.Options().once(true).memory(true));
	protected Actions<String> textActions = new ConcurrentActions<>(new Actions.Options());
	private Transport transport;

	protected SocketSupport(Transport transport) {
		this.transport = transport;
	}

	@Override
	public URI uri() {
		return transport.uri();
	}

	@Override
	public Map<String, List<String>> params() {
		return transport.params();
	}

	@Override
	public Set<String> tags() {
		return tags;
	}

	@Override
	public void textAction(Action<String> action) {
		textActions.add(action);
	}

	@Override
	public void closeAction(Action<Void> action) {
		closeActions.add(action);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id() == null) ? 0 : id().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocketSupport other = (SocketSupport) obj;
		if (id() == null) {
			if (other.id() != null)
				return false;
		} else if (!id().equals(other.id()))
			return false;
		return true;
	}

}
