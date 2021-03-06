package com.github.flowersinthesand.spheres.portal;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.ServerBaseSupport;
import com.github.flowersinthesand.spheres.SessionManager;
import com.github.flowersinthesand.spheres.SerializableAction;

public class PortalServer extends ServerBaseSupport implements Server {

	public PortalServer(SessionManager sessionManager) {
		super(sessionManager);
	}

	@Override
	public Sessions all() {
		return new SimpleSessions() {
			@Override
			protected void apply(Action<Session> action) {
				all(action);
			}
		};
	}

	@Override
	public Server all(Action<Session> action) {
		sessionManager.all(action);
		return this;
	}

	@Override
	public Sessions byId(final String id) {
		return new SimpleSessions() {
			@Override
			protected void apply(Action<Session> action) {
				byId(id, action);
			}
		};
	}

	@Override
	public Server byId(String id, Action<Session> action) {
		sessionManager.byId(id, action);
		return this;
	}

	@Override
	public Sessions byTag(final String... names) {
		return new SimpleSessions() {
			@Override
			protected void apply(Action<Session> action) {
				byTag(names, action);
			}
		};
	}

	@Override
	public Server byTag(String name, Action<Session> action) {
		return byTag(new String[] { name }, action);
	}

	@Override
	public Server byTag(String name1, String name2, Action<Session> action) {
		return byTag(new String[] { name1, name2 }, action);
	}

	@Override
	public Server byTag(String name1, String name2, String name3, Action<Session> action) {
		return byTag(new String[] { name1, name2, name3 }, action);
	}

	@Override
	public Server byTag(String[] names, Action<Session> action) {
		sessionManager.byTag(names, action);
		return this;
	}

	@Override
	public Server sessionAction(Action<Session> action) {
		sessionManager.sessionAction(action);
		return this;
	}

	private abstract static class SimpleSessions implements Sessions {

		@Override
		public Sessions tag(String... name) {
			apply(new TagAction(name));
			return this;
		}

		@Override
		public Sessions untag(String... name) {
			apply(new UntagAction(name));
			return this;
		}

		@Override
		public Sessions send(String event, Object data) {
			apply(new SendAction(event, data));
			return this;
		}

		@Override
		public Sessions send(String event) {
			apply(new SendAction(event, null));
			return this;
		}

		@Override
		public void close() {
			apply(new CloseAction());
		}

		protected abstract void apply(Action<Session> action);

	}

	private static class TagAction implements SerializableAction<Session> {

		private static final long serialVersionUID = -5706069254612745470L;

		private String[] names;

		public TagAction(String[] names) {
			super();
			this.names = names;
		}

		@Override
		public void on(Session session) {
			session.tag(names);
		}

	}

	private static class UntagAction implements SerializableAction<Session> {

		private static final long serialVersionUID = -4124195145409336436L;

		private String[] names;

		public UntagAction(String[] names) {
			super();
			this.names = names;
		}

		@Override
		public void on(Session session) {
			session.untag(names);
		}

	}

	private static class SendAction implements SerializableAction<Session> {

		private static final long serialVersionUID = 73369264239785824L;

		private String event;
		private Object data;

		public SendAction(String event, Object data) {
			this.event = event;
			this.data = data;
		}

		@Override
		public void on(Session session) {
			session.send(event, data);
		}

	}

	private static class CloseAction implements SerializableAction<Session> {

		private static final long serialVersionUID = -5777771896345897727L;

		@Override
		public void on(Session session) {
			session.close();
		}

	}

}
