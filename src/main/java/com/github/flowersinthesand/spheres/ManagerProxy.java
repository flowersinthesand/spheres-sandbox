package com.github.flowersinthesand.spheres;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ManagerProxy implements Manager {

	private final ManagerInside manager;
	private final Messenger messenger;

	public ManagerProxy(final Options o) {
		if (o.messenger() == null) {
			o.messenger(new FriendlessMessenger());
		}
		Options options = new LockedOptions(o);
		manager = new DefaultManager(options);
		manager.install();
		messenger = options.messenger();
		messenger.messageActions().add(new Action<MessageHolder>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void on(MessageHolder holder) {
				Map<String, Serializable> message = holder.get();
				String methodName = (String) message.get("methodName");
				if (methodName.equals("all")) {
					manager.all((Action) message.get("arg0"));
				} else if (methodName.equals("byId")) {
					manager.byId((String) message.get("arg0"), (Action) message.get("arg1"));
				} else if (methodName.equals("byTag")) {
					Object arg0 = message.get("arg0");
					if (arg0 instanceof String) {
						manager.byTag((String) arg0, (Action) message.get("arg1"));
					} else if (arg0 instanceof String[]) {
						manager.byTag((String[]) arg0, (Action) message.get("arg1"));
					} else {
						throw new UnsupportedOperationException();
					}
				} else {
					throw new UnsupportedOperationException();
				}
			}
		});

		for (Object comp : new Object[] { messenger }) {
			if (comp instanceof ManagerInsideAware) {
				((ManagerInsideAware) comp).setManagerInside(manager);
			}
			if (comp instanceof Initable) {
				((Initable) comp).init();
			}
		}
	}

	@Override
	public Manager all(Action<? extends SessionBase> action) {
		publishMessage("all", castSerializable(action));
		return this;
	}

	@Override
	public Manager byId(String id, Action<? extends SessionBase> action) {
		publishMessage("byId", id, castSerializable(action));
		return this;
	}

	@Override
	public Manager byTag(String name, Action<? extends SessionBase> action) {
		publishMessage("byTag", name, castSerializable(action));
		return this;
	}
	
	@Override
	public Manager byTag(String[] names, Action<? extends SessionBase> action) {
		publishMessage("byTag", names, castSerializable(action));
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private Serializable castSerializable(final Action action) {
		if (action instanceof Serializable) {
			return (Serializable) action;
		} else if (messenger instanceof FriendlessMessenger) {
			return new SerializableAction() {
				@Override
				public void on(Object object) {
					action.on(object);
				}
			};
		} else {
			throw new IllegalArgumentException(
					"Action should implement java.io.Serializable to cluster the application");
		}

	}

	private void publishMessage(String methodName, Serializable... args) {
		Map<String, Serializable> message = new LinkedHashMap<>();
		message.put("methodName", methodName);
		for (int i = 0; i < args.length; i++) {
			message.put("arg" + i, args[i]);
		}
		messenger.publish(message);
	}

	@Override
	public Manager httpAction(Action<HttpExchange> action) {
		return manager.httpAction(action);
	}

	@Override
	public Manager webSocketAction(Action<WebSocket> action) {
		return manager.webSocketAction(action);
	}

	@Override
	public Manager socketAction(Action<Socket> action) {
		return manager.socketAction(action);
	}

	@Override
	public Manager sessionAction(Action<? extends SessionBase> action) {
		return manager.sessionAction(action);
	}

	@Override
	public Manager closeAction(Action<Void> action) {
		return manager.closeAction(action);
	}

	@Override
	public void close() {
		manager.close();
	}

	private static class LockedOptions extends Options {

		public LockedOptions(Options o) {
			super.uri(o.uri());
			super.bridge(o.bridge());
			super.protocol(o.protocol());
			super.messenger(o.messenger());
		}

		@Override
		public Options bridge(Bridge bridge) {
			throw new IllegalStateException("Locked");
		}

		@Override
		public Options messenger(Messenger messenger) {
			throw new IllegalStateException("Locked");
		}

		@Override
		public Options protocol(Protocol protocol) {
			throw new IllegalStateException("Locked");
		}

		@Override
		public Options uri(String uri) {
			throw new IllegalStateException("Locked");
		}

	}
	
	private static class FriendlessMessenger extends MessengerSupport {

		@Override
		public void publish(final Map<String, Serializable> message) {
			messageActions.fire(new MessageHolder() {
				@Override
				public Map<String, Serializable> get() {
					return message;
				}
				@Override
				public <T> T unwrap(Class<T> clazz) {
					return null;
				}
			});
		}
		
	}

}
