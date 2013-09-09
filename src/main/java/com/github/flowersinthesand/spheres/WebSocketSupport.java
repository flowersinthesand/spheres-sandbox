package com.github.flowersinthesand.spheres;

import java.nio.ByteBuffer;

public abstract class WebSocketSupport extends TransportSupport implements WebSocket {

	private Class<?> dataType;
	protected Actions<Void> closeActions = new SimpleActions<>(new Actions.Options().once(true).memory(true));
	protected Actions<String> textActions = new SimpleActions<>(new Actions.Options());
	protected Actions<ByteBuffer> binaryActions = new SimpleActions<>(new Actions.Options());

	@Override
	public void textAction(Action<String> action) {
		setAndThrowIfDataTypeIsNot(String.class);
		registerTextHandler();
		textActions.add(action);
	}

	protected abstract void registerTextHandler();

	@Override
	public void binaryAction(Action<ByteBuffer> action) {
		setAndThrowIfDataTypeIsNot(ByteBuffer.class);
		registerBinaryHandler();
		binaryActions.add(action);
	}

	private void setAndThrowIfDataTypeIsNot(Class<?> clazz) {
		if (dataType == null) {
			dataType = clazz;
		} else if (dataType != clazz) {
			throw new IllegalArgumentException("This WebSocket's data type is already set to [" + dataType + "]");
		}
	}

	protected abstract void registerBinaryHandler();

	@Override
	public void closeAction(Action<Void> action) {
		closeActions.add(action);
	}

}