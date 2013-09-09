package com.github.flowersinthesand.spheres;

/**
 * Action with no args.
 * 
 * @author Donghwan Kim
 */
public abstract class VoidAction implements Action<Void> {

	@Override
	public void on(Void _) {
		on();
	}

	public abstract void on();

}