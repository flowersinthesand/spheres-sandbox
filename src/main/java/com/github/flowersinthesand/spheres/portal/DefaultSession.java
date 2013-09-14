package com.github.flowersinthesand.spheres.portal;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.Actions;
import com.github.flowersinthesand.spheres.ConcurrentActions;
import com.github.flowersinthesand.spheres.SerializableAction;
import com.github.flowersinthesand.spheres.SessionBaseSupport;
import com.github.flowersinthesand.spheres.Socket;
import com.github.flowersinthesand.spheres.VoidAction;

public class DefaultSession extends SessionBaseSupport implements Session {

	private static ObjectMapper mapper = new ObjectMapper();

	private ConcurrentMap<String, ActionsHolder<?>> holders = new ConcurrentHashMap<>();
	private ConcurrentMap<String, Action<Object>> replies = new ConcurrentHashMap<>();

	public DefaultSession(Socket socket) {
		super(socket);
		
		final Actions<Void> closeActions = new ConcurrentActions<Void>(new Actions.Options().once(true).memory(true));
		holders.put("close", new ActionsHolder<>(Void.class, closeActions));

		socket.closeAction(new VoidAction() {
			@Override
			public void on() {
				closeActions.fire();
			}
		});
		socket.textAction(new Action<String>() {
			@Override
			public void on(String text) {
				Map<String, Object> m;
				try {
					m = mapper.readValue(text, new TypeReference<Map<String, Object>>() {});
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				@SuppressWarnings("unchecked")
				ActionsHolder<Object> holder = (ActionsHolder<Object>) holders.get((String) m.get("type"));
				if (holder != null) {
					Object data;
					if (Reply.class == holder.dataType.getRawClass()) {
						data = new SimpleReply(m, holder.dataType.containedType(0));
					} else {
						data = mapper.convertValue(m.get("data"), holder.dataType);
					}
					holder.actions.fire(data);
				}
			}
		});

		on("reply", new Action<Map<String, Object>>() {
			@Override
			public void on(Map<String, Object> data) {
				String eventId = (String) data.get("id");
				Object response = data.get("data");

				Action<Object> reply = replies.remove(eventId);
				if (reply != null) {
					reply.on(mapper.convertValue(response, mapper.constructType(findRequiredDataType(reply.getClass()))));
				}
			}
		});
	}

	private class SimpleReply implements Reply<Object> {
		private AtomicBoolean sent = new AtomicBoolean();
		private Map<String, Object> m;
		private Object data;

		public SimpleReply(Map<String, Object> m, JavaType type) {
			this.m = m;
			data = mapper.convertValue(m.get("data"), type);
		}

		@Override
		public Object data() {
			return data;
		}

		@Override
		public void done() {
			done(null);
		}

		@Override
		public void done(Object value) {
			sendReply(value, false);
		}

		@Override
		public void fail() {
			fail(null);
		}

		@Override
		public void fail(Object value) {
			sendReply(value, true);
		}

		private void sendReply(Object value, boolean exception) {
			if (sent.compareAndSet(false, true)) {
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				result.put("id", m.get("id"));
				result.put("data", value);
				result.put("exception", exception);
				DefaultSession.this.send("reply", result);
			} else {
				throw new IllegalStateException("The reply for the event [" + m + "] is already sent");
			}
		}
	}

	@Override
	public String id() {
		return socket.id();
	}

	@Override
	public URI uri() {
		return socket.uri();
	}

	@Override
	public Map<String, List<String>> params() {
		return socket.params();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Session on(String event, Action<?> action) {
		ActionsHolder<?> holder = holders.get(event);
		if (holder == null) {
			Type type = findRequiredDataType(action.getClass());
			ActionsHolder<?> value = new ActionsHolder<>(type, new ConcurrentActions<>(new Actions.Options()));
			holder = holders.putIfAbsent(event, value);
			if (holder == null) {
				holder = value;
			}
		}
		holder.actions.add((Action) action);
		return this;
	}

	private Type findRequiredDataType(Class<?> clazz) {
		for (Type genericInterface : clazz.getGenericInterfaces()) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
				Type rawType = parameterizedType.getRawType(); 
				if (rawType == Action.class || rawType == SerializableAction.class) {
					return parameterizedType.getActualTypeArguments()[0];
				}
			}
		}

		Class<?> superClass = clazz.getSuperclass();
		return superClass != null ? findRequiredDataType(superClass) : null;
	}
	
	@Override
	public Set<String> tags() {
		return socket.tags();
	}

	@Override
	public Session tag(String... name) {
		socket.tags().addAll(Arrays.asList(name));
		return this;
	}

	@Override
	public Session untag(String... name) {
		socket.tags().removeAll(Arrays.asList(name));
		return this;
	}

	@Override
	public Session send(String event) {
		return send(event, null);
	}

	@Override
	public Session send(String event, Object data) {
		return send(event, data, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Session send(String event, Object data, Action<?> reply) {
		String eventId = UUID.randomUUID().toString();
		Map<String, Object> message = new LinkedHashMap<String, Object>();

		message.put("id", eventId);
		message.put("type", event);
		message.put("data", data);
		message.put("reply", reply != null);

		String text;
		try {
			text = mapper.writeValueAsString(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		socket.send(text);
		if (reply != null) {
			replies.put(eventId, (Action<Object>) reply);
		}

		return this;
	}

	@Override
	public void close() {
		socket.close();
	}

	private static class ActionsHolder<T> {
		JavaType dataType;
		Actions<T> actions;

		public ActionsHolder(Type dataType, Actions<T> actions) {
			this.dataType = mapper.constructType(dataType);
			this.actions = actions;
		}
	}

}