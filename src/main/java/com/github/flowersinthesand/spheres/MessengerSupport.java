package com.github.flowersinthesand.spheres;

public abstract class MessengerSupport implements Messenger, SessionManagerInsideAware, Initable {

	protected Actions<MessageHolder> messageActions = new ConcurrentActions<>(new Actions.Options());
	protected SessionManagerInside manager;
	
	@Override
	public void init() {}

	@Override
	public Actions<MessageHolder> messageActions() {
		return messageActions;
	}

	@Override
	public void setManagerInside(SessionManagerInside manager) {
		this.manager = manager;
	}

	protected String topicName() {
		return "spheres:" + manager.options().uri();
	}

}
