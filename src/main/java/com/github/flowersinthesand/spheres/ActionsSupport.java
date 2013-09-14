package com.github.flowersinthesand.spheres;

import java.util.List;

public abstract class ActionsSupport<T> implements Actions<T> {

	protected final Actions.Options options;
	protected final List<Action<T>> actionList;

	protected ActionsSupport(Actions.Options o) {
		this.options = o;
		this.actionList = createList();
	}

	@Override
	public void add(Action<T> action) {
		throwIfDisabled();
		if (options.memory() && fired()) {
			fireOne(action, cachedData());
		}
		if (!options.unique() || (options.unique() && !actionList.contains(action))) {
			actionList.add(action);
		}
	}

	@Override
	public void empty() {
		actionList.clear();
	}

	@Override
	public void fire() {
		fire(null);
	}

	@Override
	public boolean has(Action<T> action) {
		return actionList.contains(action);
	}

	@Override
	public void remove(Action<T> action) {
		actionList.remove(action);
	}

	protected void fireList(T data) {
		for (Action<T> action : actionList) {
			fireOne(action, data);
		}
	}

	protected void fireOne(Action<T> action, T data) {
		action.on(data);
	}

	protected void throwIfDisabled() {
		if (disabled()) {
			throw new IllegalStateException("Actions is disabled");
		}
	}

	protected void throwIfFired() {
		throwIfDisabled();
		if (options.once() && fired()) {
			throw new IllegalStateException("Already fired");
		}
	}

	protected abstract List<Action<T>> createList();

	protected abstract T cachedData();

}
