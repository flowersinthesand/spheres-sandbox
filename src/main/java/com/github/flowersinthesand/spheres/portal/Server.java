package com.github.flowersinthesand.spheres.portal;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.ServerBase;

public interface Server extends ServerBase {

	Sessions all();

	Server all(Action<Session> action);

	Sessions byId(String id);

	Server byId(String id, Action<Session> action);

	Sessions byTag(String... names);

	Server byTag(String name, Action<Session> action);

	Server byTag(String name1, String name2, Action<Session> action);

	Server byTag(String name1, String name2, String name3, Action<Session> action);

	Server byTag(String[] names, Action<Session> action);

	Server sessionAction(Action<Session> action);

	interface Sessions extends SharedSessionContract<Sessions> {}

}
