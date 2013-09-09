package com.github.flowersinthesand.spheres;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentActions<T> extends ActionsSupport<T> {

	private AtomicBoolean disabled = new AtomicBoolean();
	private AtomicBoolean fired = new AtomicBoolean();
	private AtomicReference<T> cached = new AtomicReference<>();

	public ConcurrentActions(Actions.Options o) {
		super(o);
	}

	@Override
	protected List<Action<T>> actionList() {
		return new CopyOnWriteArrayList<>();
	}

	@Override
	protected T cached() {
		return cached.get();
	}

	@Override
	public void disable() {
		if (disabled.compareAndSet(false, true)) {
			actionList.clear();
		} else {
			throw new IllegalStateException("Already disabled");
		}
	}

	@Override
	public boolean disabled() {
		return disabled.get();
	}

	@Override
	public void fire(T data) {
		throwIfFired();
		fired.set(true);
		if (options.memory()) {
			this.cached.set(data);
		}
		fireList(data);
	}

	@Override
	public boolean fired() {
		return fired.get();
	}

}
