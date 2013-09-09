package com.github.flowersinthesand.spheres.portal;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.Identifiable;
import com.github.flowersinthesand.spheres.SessionBase;
import com.github.flowersinthesand.spheres.Taggable;
import com.github.flowersinthesand.spheres.UriAccessor;

public interface Session extends SessionBase, Identifiable, UriAccessor, Taggable {

	Session on(String event, Action<?> action);

	Session tag(String... name);

	Session untag(String... name);

	Session send(String event);

	Session send(String event, Object data);

	Session send(String event, Object data, Action<?> reply);

	void close();

}
