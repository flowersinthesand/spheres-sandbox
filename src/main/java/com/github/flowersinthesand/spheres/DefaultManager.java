package com.github.flowersinthesand.spheres;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultManager implements SessionManager, SessionManagerInside {

	private final Options options;
	private final Bridge bridge;
	private final Protocol protocol;
	private ConcurrentMap<String, Socket> sockets = new ConcurrentHashMap<>();
	private ConcurrentMap<Socket, SessionBase> sessions = new ConcurrentHashMap<>();
	private Actions<Void> closeActions = new ConcurrentActions<>(new Actions.Options().once(true).memory(true));

	DefaultManager(Options options) {
		this.options = options;
		bridge = options.bridge();
		protocol = options.protocol();
	}

	@Override
	public void install() {
		closeAction(new VoidAction() {
			@Override
			public void on() {
				for (Socket socket : sockets.values()) {
					socket.close();
				}
			}
		});
		httpAction(new Action<HttpExchange>() {
			@Override
			public void on(HttpExchange http) {
				protocol.on(http);
			}
		});
		webSocketAction(new Action<WebSocket>() {
			@Override
			public void on(WebSocket ws) {
				protocol.on(ws);
			}
		});
		socketAction(new Action<Socket>() {
			@Override
			public void on(final Socket socket) {
				sockets.put(socket.id(), socket);
				socket.closeAction(new VoidAction() {
					@Override
					public void on() {
						sockets.remove(socket.id());
					}
				});
				protocol.on(socket);
			}
		});
		sessionAction(new Action<SessionBase>() {
			@Override
			public void on(SessionBase session) {
				final Socket socket = session.unwrap(Socket.class);
				sessions.put(socket, session);
				socket.closeAction(new VoidAction() {
					@Override
					public void on() {
						sessions.remove(socket);
					}
				});
			}
		});

		for (Object comp : new Object[] { bridge, protocol }) {
			if (comp instanceof SessionManagerInsideAware) {
				((SessionManagerInsideAware) comp).setManagerInside(this);
			}
			if (comp instanceof Initable) {
				((Initable) comp).init();
			}
		}
	}

	@Override
	public Options options() {
		return options;
	}

	@Override
	public ConcurrentMap<String, Socket> sockets() {
		return sockets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SessionManager all(Action<? extends SessionBase> action) {
		for (Socket socket : sockets.values()) {
			((Action<SessionBase>) action).on(sessions.get(socket));
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SessionManager byId(String id, Action<? extends SessionBase> action) {
		Socket socket = sockets.get(id);
		if (socket != null) {
			((Action<SessionBase>) action).on(sessions.get(socket));
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SessionManager byTag(String name, Action<? extends SessionBase> action) {
		for (Socket socket : sockets.values()) {
			if (socket.tags().contains(name)) {
				((Action<SessionBase>) action).on(sessions.get(socket));
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SessionManager byTag(String[] names, Action<? extends SessionBase> action) {
		List<String> nameList = Arrays.asList(names);
		for (Socket socket : sockets.values()) {
			if (socket.tags().containsAll(nameList)) {
				((Action<SessionBase>) action).on(sessions.get(socket));
			}
		}
		return this;
	}

	@Override
	public SessionManager httpAction(Action<HttpExchange> action) {
		bridge.httpActions().add(action);
		return this;
	}

	@Override
	public SessionManager webSocketAction(Action<WebSocket> action) {
		bridge.webSocketActions().add(action);
		return this;
	}

	@Override
	public SessionManager socketAction(Action<Socket> action) {
		protocol.socketActions().add(action);
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SessionManager sessionAction(Action<? extends SessionBase> action) {
		protocol.sessionActions().add((Action) action);
		return this;
	}

	@Override
	public SessionManager closeAction(Action<Void> action) {
		closeActions.add(action);
		return this;
	}

	@Override
	public void close() {
		closeActions.fire();
	}

}
