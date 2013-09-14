package com.github.flowersinthesand.spheres.portal;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.Identifiable;
import com.github.flowersinthesand.spheres.SessionBase;
import com.github.flowersinthesand.spheres.Taggable;
import com.github.flowersinthesand.spheres.UriAccessor;

public interface Session extends SessionBase, Identifiable, UriAccessor, Taggable,
		SharedSessionContract<Session> {

	Session on(String event, Action<?> action);

	Session off(String event, Action<?> action);

	Session once(String event, Action<?> action);

	Session send(String event, Object data, Action<?> reply);

}
