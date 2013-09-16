package com.github.flowersinthesand.spheres;

public abstract class AppBaseSupport implements AppBase {

	protected final Manager manager;

	public AppBaseSupport(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void close() {
		manager.close();
	}
	
	@Override
	public <T> T unwrap(Class<T> clazz) {
		return Manager.class.isAssignableFrom(clazz) ? clazz.cast(manager) : null;
	}

}
