package com.github.flowersinthesand.spheres.hazelcast;

import java.io.Serializable;
import java.util.Map;

import com.github.flowersinthesand.spheres.MessageHolder;
import com.github.flowersinthesand.spheres.MessengerSupport;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

public class HazelcastMessenger extends MessengerSupport {

	private final HazelcastInstance hazelcast;
	private ITopic<Map<String, Serializable>> topic;

	public HazelcastMessenger(HazelcastInstance hazelcast) {
		this.hazelcast = hazelcast;
	}

	@Override
	public void init() {
		topic = hazelcast.getTopic(topicName());
		topic.addMessageListener(new MessageListener<Map<String, Serializable>>() {
			@Override
			public void onMessage(final com.hazelcast.core.Message<Map<String, Serializable>> message) {
				messageActions.fire(new MessageHolder() {
					@Override
					public Map<String, Serializable> get() {
						return message.getMessageObject();
					}
					@Override
					public <T> T unwrap(Class<T> clazz) {
						return com.hazelcast.core.Message.class.isAssignableFrom(clazz) ? clazz.cast(message) : null;
					}
				});
			}
		});
	}

	@Override
	public void publish(Map<String, Serializable> message) {
		topic.publish(message);
	}

}
