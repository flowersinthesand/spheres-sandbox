package com.github.flowersinthesand.spheres.vertx2;

import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.http.impl.WebSocketMatcher;
import org.vertx.java.core.http.impl.WebSocketMatcher.Match;

import com.github.flowersinthesand.spheres.BridgeSupport;
import com.github.flowersinthesand.spheres.HttpExchangeSupport;
import com.github.flowersinthesand.spheres.Initable;
import com.github.flowersinthesand.spheres.WebSocketSupport;

public class VertxBridge extends BridgeSupport implements Initable {

	private final HttpServer httpServer;

	public VertxBridge(HttpServer httpServer) {
		this.httpServer = httpServer;
	}

	@Override
	public void init() {
		httpServer.requestHandler(getRequestHandler()).websocketHandler(getWebSocketHandler());
	}

	protected RouteMatcher getRequestHandler() {
		RouteMatcher httpMatcher = new RouteMatcher();
		httpMatcher.all(uri(), new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest request) {
				httpActions.fire(new VertxHttpExchange(request));
			}
		});
		httpMatcher.noMatch(httpServer.requestHandler());
		return httpMatcher;
	}

	protected WebSocketMatcher getWebSocketHandler() {
		WebSocketMatcher wsMatcher = new WebSocketMatcher();
		wsMatcher.addPattern(uri(), new Handler<WebSocketMatcher.Match>() {
			@Override
			public void handle(Match match) {
				webSocketActions.fire(new VertxWebSocket(match.ws));
			}
		});
		wsMatcher.noMatch(new Handler<WebSocketMatcher.Match>() {
			Handler<ServerWebSocket> old = httpServer.websocketHandler();

			public void handle(WebSocketMatcher.Match match) {
				if (old != null) {
					old.handle(match.ws);
				}
			}
		});
		return wsMatcher;
	}

	static class VertxWebSocket extends WebSocketSupport {

		private ServerWebSocket ws;

		public VertxWebSocket(ServerWebSocket ws) {
			this.ws = ws.closeHandler(new VoidHandler() {
				@Override
				protected void handle() {
					closeActions.fire();
				}
			});
		}

		@Override
		protected URI lazyUri() {
			return URI.create(ws.path() + "?" + ws.query());
		}

		@Override
		protected Map<String, List<String>> lazyParams() {
			return new QueryStringDecoder("?" + ws.query()).parameters();
		}

		@Override
		protected void registerTextHandler() {
			ws.dataHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buffer) {
					textActions.fire(buffer.toString());
				}
			});
		}

		@Override
		protected void registerBinaryHandler() {
			ws.dataHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buffer) {
					binaryActions.fire(ByteBuffer.wrap(buffer.getBytes()));
				}
			});
		}

		@Override
		public void send(String data) {
			ws.writeTextFrame(data);
		}

		@Override
		public void send(ByteBuffer data) {
			ws.writeBinaryFrame(new Buffer(data.array()));
		}

		@Override
		public void close() {
			ws.close();
		}

		@Override
		public <T> T unwrap(Class<T> clazz) {
			return ServerWebSocket.class.isAssignableFrom(clazz) ? clazz.cast(ws) : null;
		}

	}

	static class VertxHttpExchange extends HttpExchangeSupport {

		private HttpServerRequest request;
		private HttpServerResponse response;

		public VertxHttpExchange(HttpServerRequest request) {
			this.request = request;
			this.response = request.response();
			response.setChunked(true).closeHandler(new VoidHandler() {
				@Override
				protected void handle() {
					closeActions.fire();
				}
			});
		}

		@Override
		protected URI lazyUri() {
			return request.absoluteURI();
		}

		@Override
		protected Map<String, List<String>> lazyParams() {
			Map<String, List<String>> params = new LinkedHashMap<>();
			for (String name : request.params().names()) {
				params.put(name, request.params().getAll(name));
			}
			return Collections.unmodifiableMap(params);
		}

		@Override
		protected Map<String, List<String>> lazyHeaders() {
			Map<String, List<String>> headers = new LinkedHashMap<>();
			for (String name : request.headers().names()) {
				headers.put(name.toLowerCase(), request.headers().getAll(name));
			}
			return Collections.unmodifiableMap(headers);
		}

		@Override
		public Method method() {
			return Method.valueOf(request.method());
		}

		@Override
		protected void readBody() {
			request.bodyHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buffer) {
					bodyActions.fire(buffer.toString());
				}
			});
		}

		@Override
		public void status(Status status) {
			response.setStatusCode(status.code());
		}

		@Override
		public void header(String name, String value) {
			response.putHeader(name, value);
		}

		@Override
		public void write(String chunk) {
			response.write(chunk);
		}

		@Override
		public void write(ByteBuffer chunk) {
			response.write(new Buffer(chunk.array()));
		}

		@Override
		public void close() {
			response.end();
			response.close();
		}

		@Override
		public <T> T unwrap(Class<T> clazz) {
			return HttpServerRequest.class.isAssignableFrom(clazz) ? clazz.cast(request) : null;
		}

	}
	
}
