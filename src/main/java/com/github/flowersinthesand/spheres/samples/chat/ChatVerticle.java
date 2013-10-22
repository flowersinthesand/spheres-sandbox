package com.github.flowersinthesand.spheres.samples.chat;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.SessionManager;
import com.github.flowersinthesand.spheres.SessionManagerProxy;
import com.github.flowersinthesand.spheres.Options;
import com.github.flowersinthesand.spheres.hazelcast.HazelcastMessenger;
import com.github.flowersinthesand.spheres.portal.Server;
import com.github.flowersinthesand.spheres.portal.PortalServer;
import com.github.flowersinthesand.spheres.portal.PortalProtocol;
import com.github.flowersinthesand.spheres.portal.Session;
import com.github.flowersinthesand.spheres.vertx2.VertxBridge;
import com.hazelcast.config.Config;
import com.hazelcast.instance.HazelcastInstanceFactory;

public class ChatVerticle extends Verticle {

	private Server server;

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
		SessionManager sessionManager = new SessionManagerProxy(options);
		
		server = new PortalServer(sessionManager);
		server.sessionAction(new Action<Session>() {
			@Override
			public void on(Session session) {
				session.on("message", new Action<Map<String, Object>>() {
					@Override
					public void on(Map<String, Object> data) {
						server.all().send("message", data);
					}
				});
			}
		});
		
		httpServer.listen(port());
	}

	private int port() {
		try {
			return Integer.valueOf(System.getProperty("port"));
		} catch (NumberFormatException e) {
			return 8080;
		}
	}

	@Override
	public void stop() {
		server.close();
	}

}
