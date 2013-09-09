package com.github.flowersinthesand.spheres.samples.chat;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.App;
import com.github.flowersinthesand.spheres.AppProxy;
import com.github.flowersinthesand.spheres.Options;
import com.github.flowersinthesand.spheres.SerializableAction;
import com.github.flowersinthesand.spheres.hazelcast.HazelcastMessenger;
import com.github.flowersinthesand.spheres.portal.PortalProtocol;
import com.github.flowersinthesand.spheres.portal.Session;
import com.github.flowersinthesand.spheres.vertx2.VertxBridge;
import com.hazelcast.config.Config;
import com.hazelcast.instance.HazelcastInstanceFactory;

public class ChatVerticle extends Verticle {

	private App app;

	@Override
	public void start() {
		HttpServer httpServer = vertx.createHttpServer();
		httpServer.requestHandler(new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String path = req.path();
				if (path.equals("/")) {
					path = "/index.html";
				}

				req.response().sendFile("webapp/" + path);
			}
		});

		Options options = new Options()
		.uri("/chat")
		.bridge(new VertxBridge(httpServer))
		.protocol(new PortalProtocol())
		.messenger(new HazelcastMessenger(HazelcastInstanceFactory.newHazelcastInstance(new Config())));
		
		app = new AppProxy(options);
		app.sessionAction(new Action<Session>() {
			@Override
			public void on(Session session) {
				session.on("message", new Action<Map<String, Object>>() {
					@Override
					public void on(Map<String, Object> data) {
						app.all(new SendEventAction("message", data));
					}
				});
			}
		});
		
		httpServer.listen(port());
	}

	private int port() {
		int port = 8080;
		try {
			port = Integer.valueOf(System.getProperty("port"));
		} catch (NumberFormatException e) {}
		return port;
	}

	@Override
	public void stop() {
		app.close();
	}

	public static class SendEventAction implements SerializableAction<Session> {

		private static final long serialVersionUID = 1395939237538798550L;

		private String type;
		private Object data;

		public SendEventAction(String type, Object data) {
			this.type = type;
			this.data = data;
		}

		@Override
		public void on(Session sess) {
			sess.send(type, data);
		}

	}

}
