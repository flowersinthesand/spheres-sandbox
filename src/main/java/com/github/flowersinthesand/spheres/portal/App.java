package com.github.flowersinthesand.spheres.portal;

import com.github.flowersinthesand.spheres.Action;
import com.github.flowersinthesand.spheres.AppBase;

public interface App extends AppBase {

	Sessions all();

	App all(Action<Session> action);

	Sessions byId(String id);

	App byId(String id, Action<Session> action);

	Sessions byTag(String... names);

	App byTag(String name, Action<Session> action);

	App byTag(String name1, String name2, Action<Session> action);

	App byTag(String name1, String name2, String name3, Action<Session> action);

	App byTag(String[] names, Action<Session> action);

	App sessionAction(Action<Session> action);

	interface Sessions extends SharedSessionContract<Sessions> {}

}
