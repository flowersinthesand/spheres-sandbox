package com.github.flowersinthesand.spheres.portal;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.Actions;
import com.github.flowersinthesand.spheres.HttpExchange;
import com.github.flowersinthesand.spheres.HttpSocket;
import com.github.flowersinthesand.spheres.Initable;
import com.github.flowersinthesand.spheres.ProtocolSupport;
import com.github.flowersinthesand.spheres.Socket;
import com.github.flowersinthesand.spheres.SocketSupport;
import com.github.flowersinthesand.spheres.Transport;
import com.github.flowersinthesand.spheres.VoidAction;
import com.github.flowersinthesand.spheres.WebSocket;
import com.github.flowersinthesand.spheres.WsSocketWrapper;

public class PortalProtocol extends ProtocolSupport implements Initable {

	private Pattern socketIdRegex = Pattern.compile("\"socket\":\"([^\"]+)\"");
	private ScheduledExecutorService heartbeatExecutor;

	@Override
	public void init() {
		heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
		app.closeAction(new VoidAction() {
			@Override
			public void on() {
				heartbeatExecutor.shutdown();
			}
		});
	}

	@Override
	public void on(HttpExchange http) {
		switch (http.method()) {
		case GET:
			http.setCorsHeaders();
			http.setNoCacheHeaders();
			String when = http.params().get("when").get(0);
			if (when.equals("open") || when.equals("poll")) {
				openHttpSocket(http);
			} else if (when.equals("abort")) {
				abort(http.params().get("id").get(0));
			}
			break;
		case POST:
			http.setCorsHeaders();
			http.setNoCacheHeaders();
			fireHttpTextActions(http);
			break;
		default:
			throw new UnsupportedOperationException("[" + http.method() + "] is not supported");
		}
	}

	private void openHttpSocket(HttpExchange http) {
		Map<String, List<String>> params = http.params();
		String transport = params.get("transport").get(0);
	
		if (transport.equals("sse") || transport.startsWith("stream")) {
			socketActions.fire(new PortalHttpStreamSocket(http));
		} else if (transport.startsWith("longpoll")) {
			String id = params.get("id").get(0);
			String when = params.get("when").get(0);

			if (when.equals("open")) {
				socketActions.fire(new PortalHttpLongPollSocket(http));
			} else if (when.equals("poll")) {
				((PortalHttpLongPollSocket) sockets.get(id)).refresh(http);
			}
		}
	}

	private void abort(String id) {
		Socket socket = sockets.get(id);
		if (socket != null) {
			socket.close();
		}
	}

	private void fireHttpTextActions(final HttpExchange http) {
		http.bodyAction(new Action<String>() {
			@Override
			public void on(String body) {
				body = body.toString().substring("data=".length());
				((AbstractHttpSocket) sockets.get(findSocketId(body))).textActions().fire(body);
				http.close();
			}
		});
	}

	private String findSocketId(String text) {
		Matcher matcher = socketIdRegex.matcher(text);
		matcher.find();
		return matcher.group(1);
	}

	@Override
	public void on(WebSocket ws) {
		socketActions.fire(new PortalWsSocket(ws));
	}

	@Override
	public void on(Socket socket) {
		Session session = new DefaultSession(socket);
		HeartbeatHelper heartbeatHelper = new HeartbeatHelper(session);

		heartbeatHelper.open();
		session.on("close", heartbeatHelper.closeAction());
		session.on("heartbeat", heartbeatHelper.heartbeatAction());

		sessionActions.fire(session);
	}

	private class HeartbeatHelper {
		private Session session;
		private boolean enabled;
		private long delay;
		private ScheduledFuture<?> future;

		public HeartbeatHelper(Session session) {
			try {
				delay = Long.valueOf(session.unwrap(Socket.class).params().get("heartbeat").get(0));
				enabled = true;
				this.session = session;
			} catch (NumberFormatException e) {}
		}

		public void open() {
			if (enabled) {
				future = heartbeatExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						session.close();
					}
				}, delay, TimeUnit.MILLISECONDS);
			}
		}

		private void close() {
			if (enabled) {
				future.cancel(true);
			}
		}

		public Action<Void> closeAction() {
			return new VoidAction() {
				@Override
				public void on() {
					close();
				}
			};
		}

		public Action<Void> heartbeatAction() {
			return new VoidAction() {
				@Override
				public void on() {
					if (enabled) {
						close();
						open();
						session.send("heartbeat");
					}
				}
			};
		}
	}

	static class PortalWsSocket extends WsSocketWrapper {

		public PortalWsSocket(WebSocket webSocket) {
			super(webSocket);
		}

		@Override
		public String id() {
			return params().get("id").get(0);
		}

	}

	static abstract class AbstractHttpSocket extends SocketSupport implements HttpSocket {

		protected AbstractHttpSocket(Transport transport) {
			super(transport);
		}

		@Override
		public String id() {
			return params().get("id").get(0);
		}

		public Actions<String> textActions() {
			return textActions;
		}

	}

	static class PortalHttpStreamSocket extends AbstractHttpSocket {

		private HttpExchange http;
		private boolean isAndroid;

		public PortalHttpStreamSocket(HttpExchange http) {
			super(http);
			this.http = http;
			this.isAndroid = http.headers().get("user-agent").get(0).matches(".*Android\\s[23]\\..*");
			init();
		}

		private void init() {
			http.header("content-type", "text/" + ("sse".equals(http.params().get("transport").get(0)) ? "event-stream" : "plain") + "; charset=utf-8");
			http.write(padding2K);
			if (isAndroid) {
				http.write(padding2K);
			}
			http.write("\n");
			http.closeAction(new VoidAction() {
				@Override
				public void on() {
					closeActions.fire();
				}
			});
		}

		@Override
		public void send(String data) {
			StringBuilder builder = new StringBuilder();
			if (isAndroid) {
				builder.append(padding2K).append(padding2K);
			}
			for (String datum : data.split("\r\n|\r|\n")) {
				builder.append("data: ").append(datum).append("\n");
			}
			builder.append("\n");
			write(builder.toString());
		}

		@Override
		public void write(String data) {
			http.write(data);
		}

		@Override
		public void close() {
			http.close();
		}
		
		@Override
		public <T> T unwrap(Class<T> clazz) {
			return HttpExchange.class.isAssignableFrom(clazz) ? clazz.cast(http) : null;
		}

	}

	static class PortalHttpLongPollSocket extends AbstractHttpSocket {

		private Pattern eventIdRegex = Pattern.compile("\"id\":\"([^\"]+)\"");
		
		private ObjectMapper mapper = new ObjectMapper();
		private Set<String> buffer = new CopyOnWriteArraySet<String>();
		private AtomicReference<HttpExchange> httpRef = new AtomicReference<HttpExchange>();

		public PortalHttpLongPollSocket(HttpExchange http) {
			super(http);
			refresh(http, true);
		}

		public void refresh(HttpExchange http) {
			refresh(http, false);
		}

		private void refresh(HttpExchange http, final boolean first) {
			httpRef.set(http);
			http.header("content-type", "text/" + ("longpolljsonp".equals(params().get("transport").get(0)) ? "javascript" : "plain") + "; charset=utf-8");
			
			http.closeAction(new VoidAction() {
				@Override
				public void on() {
					if (!first && httpRef.get() != null) {
						closeActions.fire();
					}
				}
			});
			
			if (first) {
				http.close();
			} else {
				String lastEventIds = http.params().get("lastEventIds").get(0);
				if (lastEventIds != null) {
					List<String> eventIds = Arrays.asList(lastEventIds.split(","));
					for (String eventId : eventIds) {
						for (String message : buffer) {
							if (eventId.equals(findEventId(message))) {
								buffer.remove(message);
							}
						}
					}
					if (!buffer.isEmpty()) {
						send(join(buffer));
					}
				}
			}
		}

		private String findEventId(String text) {
			Matcher matcher = eventIdRegex.matcher(text);
			matcher.find();
			return matcher.group(1);
		}

		private String join(Set<String> buf) {
			StringBuilder builder = new StringBuilder("[");
			for (String message : buf) {
				builder.append(message).append(",");
			}
			return builder.deleteCharAt(builder.length() - 1).append("]").toString();
		}

		@Override
		public void send(String data) {
			if (!data.startsWith("[")) {
				buffer.add(data);
			}
			
			StringBuilder builder = new StringBuilder();
			if (params().get("transport").get(0).equals("longpolljsonp")) {
				try {
					builder.append(params().get("callback").get(0)).append("(").append(mapper.writeValueAsString(data)).append(");");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				builder.append(data);
			}

			write(builder.toString());
			close();
		}

		@Override
		public void write(String data) {
			HttpExchange http = httpRef.get();
			if (http != null) {
				http.write(data);
			}
		}

		@Override
		public void close() {
			HttpExchange http = httpRef.getAndSet(null);
			if (http != null) {
				http.close();
			}
		}
		
		@Override
		public <T> T unwrap(Class<T> clazz) {
			return null;
		}

	}

}