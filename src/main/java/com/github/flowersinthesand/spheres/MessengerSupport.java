package com.github.flowersinthesand.spheres;

public abstract class MessengerSupport implements Messenger, ManagerInsideAware, Initable {

	protected Actions<MessageHolder> messageActions = new ConcurrentActions<>(new Actions.Options());
	protected ManagerInside manager;
	
	@Override
	public void init() {}

	@Override
	public Actions<MessageHolder> messageActions() {
		return messageActions;
	}

	@Override
	public void setManagerInside(ManagerInside manager) {
		this.manager = manager;
	}

	protected String topicName() {
		return "spheres:" + manager.options().uri();
	}

}
