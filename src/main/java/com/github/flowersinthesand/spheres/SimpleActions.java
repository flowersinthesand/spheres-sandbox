package com.github.flowersinthesand.spheres;

import java.util.ArrayList;
import java.util.List;

public class SimpleActions<T> extends ActionsSupport<T> {

	private boolean disabled;
	private boolean fired;
	private T cached;

	public SimpleActions(Actions.Options o) {
		super(o);
	}

	@Override
	protected List<Action<T>> actionList() {
		return new ArrayList<>();
	}

	@Override
	protected T cached() {
		return cached;
	}

	@Override
	public void disable() {
		if (!disabled) {
			disabled = true;
			actionList.clear();
		} else {
			throw new IllegalStateException("Already disabled");
		}
	}

	@Override
	public boolean disabled() {
		return disabled;
	}

	@Override
	public void fire(T data) {
		throwIfFired();
		fired = true;
		if (options.memory()) {
			this.cached = data;
		}
		fireList(data);
	}

	@Override
	public boolean fired() {
		return fired;
	}

}
