package com.github.flowersinthesand.spheres.portal;

public interface Reply<T> {

	T data();

	void done();

	void done(Object data);

	void fail();

	void fail(Object error);

}
