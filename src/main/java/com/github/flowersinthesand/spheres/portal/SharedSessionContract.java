package com.github.flowersinthesand.spheres.portal;

public interface SharedSessionContract<T> {

	T tag(String... name);

	T untag(String... name);

	T send(String event);

	T send(String event, Object data);

	void close();

}
