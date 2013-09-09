package com.github.flowersinthesand.spheres;

public abstract class MessengerSupport implements Messenger, AppInsideAware {

	protected Actions<MessageHolder> messageActions = new ConcurrentActions<>(new Actions.Options());
	protected AppInside app;

	@Override
	public Actions<MessageHolder> messageActions() {
		return messageActions;
	}

	@Override
	public void setAppInside(AppInside app) {
		this.app = app;
	}

	protected String topicName() {
		return "spheres:" + app.options().uri();
	}

}
