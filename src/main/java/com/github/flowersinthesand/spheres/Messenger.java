package com.github.flowersinthesand.spheres;

import java.io.Serializable;
import java.util.Map;

/**
 * Messenger follows the publish and subscribe model from Java Message Service. Applications whose
 * URI used to create is same correspond to a topic so that messages are broadcasted to all such
 * applications' Messenger.
 * <p>
 * All the value of message map and map itself to be published are {@link Serializable}.
 * 
 * @author Donghwan Kim
 */
public interface Messenger {

	/**
	 * Publishes the message to all applications including the messenger that published it.
	 */
	void publish(Map<String, Serializable> message);

	/**
	 * The Actions for the MessageHolder.
	 */
	Actions<MessageHolder> messageActions();

}
