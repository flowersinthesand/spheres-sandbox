package com.github.flowersinthesand.spheres;

public abstract class AppBaseSupport<T> implements AppBase<T> {

	protected final Manager manager;

	public AppBaseSupport(Manager manager) {
		this.manager = manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T sessionAction(Action<? extends SessionBase> action) {
		manager.sessionAction(action);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T closeAction(Action<Void> action) {
		manager.closeAction(action);
		return (T) this;
	}

	@Override
	public void close() {
		manager.close();
	}

}
