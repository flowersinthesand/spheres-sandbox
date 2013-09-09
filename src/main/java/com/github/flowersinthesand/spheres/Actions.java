package com.github.flowersinthesand.spheres;

/**
 * A manager for a set of {@link Action}s. Inspired by jQuery's Callbacks object.
 * <p>
 * There are two main implementations: thread-safe {@link ConcurrentActions} and not thread-safe
 * {@link SimpleActions}.
 * 
 * @author Donghwan Kim
 */
public interface Actions<T> {

	/**
	 * Adds an action.
	 */
	void add(Action<T> action);

	/**
	 * Disables any operation on the actions. This method is useful when multiple events are
	 * mutually exclusive.
	 */
	void disable();

	/**
	 * Determines if the actions has been disabled.
	 */
	boolean disabled();

	/**
	 * Removes all of the actions.
	 */
	void empty();

	/**
	 * Fire all of the actions.
	 */
	void fire();

	/**
	 * Fire all of the actions with the given value.
	 */
	void fire(T data);

	/**
	 * Determines if the actions have been called at least once.
	 */
	boolean fired();

	/**
	 * Determines whether the actions contains the specified action.
	 */
	boolean has(Action<T> action);

	/**
	 * Removes an action.
	 */
	void remove(Action<T> action);

	/**
	 * Options to create an Actions. With the default options, an Action will work like a typical
	 * event manager.
	 * 
	 * @author Donghwan Kim
	 */
	class Options {

		private boolean once;
		private boolean memory;
		private boolean unique;

		public boolean once() {
			return once;
		}

		/**
		 * Ensures the actions can only be fired once. The default value is false.
		 */
		public Options once(boolean once) {
			this.once = once;
			return this;
		}

		public boolean memory() {
			return memory;
		}

		/**
		 * Keeps track of previous values and will call any action added after the actions has been
		 * fired right away with the latest "memorized" values. The default value is false.
		 */
		public Options memory(boolean memory) {
			this.memory = memory;
			return this;
		}

		public boolean unique() {
			return unique;
		}

		/**
		 * Ensures an action can only be added once. The default value is false.
		 */
		public Options unique(boolean unique) {
			this.unique = unique;
			return this;
		}

	}

}