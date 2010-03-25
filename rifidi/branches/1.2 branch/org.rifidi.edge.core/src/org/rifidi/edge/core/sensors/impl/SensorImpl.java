package org.rifidi.edge.core.sensors.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rifidi.edge.core.sensors.Sensor;
import org.rifidi.edge.core.sensors.exceptions.DuplicateSubscriptionException;
import org.rifidi.edge.core.sensors.exceptions.ImmutableException;
import org.rifidi.edge.core.sensors.exceptions.InUseException;
import org.rifidi.edge.core.sensors.exceptions.NotSubscribedException;
import org.rifidi.edge.core.services.esper.internal.EsperEventContainer;
import org.rifidi.edge.core.services.notification.data.ReadCycle;
import org.rifidi.edge.core.services.notification.data.TagReadEvent;

/**
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class SensorImpl implements Sensor {

	/** Sensors connected to this connectedSensors. */
	protected final Set<Sensor> receivers = new CopyOnWriteArraySet<Sensor>();
	/** True if the sensor is currently in use. */
	protected AtomicBoolean inUse = new AtomicBoolean(false);
	/**
	 * Receivers are objects that need to gather tag reads. The tag reads are
	 * stored in a queue.
	 */
	protected final Map<Object, LinkedBlockingQueue<ReadCycle>> tagSubscriberToQueueMap = new ConcurrentHashMap<Object, LinkedBlockingQueue<ReadCycle>>();

	/**
	 * This queue is just like the tag subscriber queue, except that it stores
	 * all events which are not Tag Read Events.
	 */
	protected final Map<Object, LinkedBlockingQueue<Object>> eventSubscriberToQueueMap = new ConcurrentHashMap<Object, LinkedBlockingQueue<Object>>();
	private String sensorName;
	private final Boolean isImmutable;

	public SensorImpl(String sensorName, Boolean isImmutable) {
		this.sensorName = sensorName;
		this.isImmutable = isImmutable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#addReceiver(org.rifidi.edge.core.
	 * sensors.Sensor)
	 */
	@Override
	public void addReceiver(Sensor receiver) {
		receivers.add(receiver);
		inUse.compareAndSet(false, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#getName()
	 */
	@Override
	public String getName() {
		return sensorName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#isImmutable()
	 */
	@Override
	public Boolean isImmutable() {
		return isImmutable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#isInUse()
	 */
	@Override
	public Boolean isInUse() {
		return inUse.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#receive(java.lang.Object)
	 */
	@Override
	public EsperEventContainer receive(Object receiver)
			throws NotSubscribedException {
		LinkedBlockingQueue<ReadCycle> tagQueue = tagSubscriberToQueueMap
				.get(receiver);
		LinkedBlockingQueue<Object> eventQueue = eventSubscriberToQueueMap
				.get(receiver);
		if (tagQueue == null || eventQueue == null) {
			throw new NotSubscribedException(receiver + " is not subscribed.");
		}

		Set<ReadCycle> rcs = new HashSet<ReadCycle>();
		synchronized (tagQueue) {
			tagQueue.drainTo(rcs);
		}

		Set<Object> events = new HashSet<Object>();
		synchronized (eventQueue) {
			eventQueue.drainTo(events);
		}

		long time = System.currentTimeMillis();
		Set<TagReadEvent> tagReads = new HashSet<TagReadEvent>();
		for (ReadCycle cycle : rcs) {
			for (TagReadEvent event : cycle.getTags()) {
				tagReads.add(event);
			}
		}
		ReadCycle cycle = new ReadCycle(tagReads, getName(), time);
		EsperEventContainer eventContainer = new EsperEventContainer();
		eventContainer.setReadCycle(cycle);
		eventContainer.setOtherEvents(events);
		return eventContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#removeReceiver(org.rifidi.edge.core
	 * .sensors.Sensor)
	 */
	@Override
	public void removeReceiver(Sensor receiver) {
		receivers.remove(receiver);
		if (tagSubscriberToQueueMap.isEmpty() && receivers.isEmpty()) {
			inUse.compareAndSet(true, false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#send(org.rifidi.edge.core.services
	 * .notification.data.ReadCycle)
	 */
	@Override
	public void send(ReadCycle cycle) {
		for (Sensor receiver : receivers) {
			receiver.send(cycle);
		}
		for (LinkedBlockingQueue<ReadCycle> queue : tagSubscriberToQueueMap
				.values()) {
			queue.add(cycle);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#sendEvent(java.lang.Object)
	 */
	@Override
	public void sendEvent(Object event) {
		for (Sensor receiver : receivers) {
			receiver.sendEvent(event);
		}
		for (LinkedBlockingQueue<Object> queue : eventSubscriberToQueueMap
				.values()) {
			queue.add(event);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws ImmutableException, InUseException {
		if (isInUse()) {
			throw new InUseException(getName() + " is currently in use.");
		}
		if (isImmutable()) {
			throw new ImmutableException(getName() + " is immutable.");
		}
		this.sensorName = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#subscribe(java.lang.Object)
	 */
	@Override
	public void subscribe(Object receiver)
			throws DuplicateSubscriptionException {
		if (tagSubscriberToQueueMap.containsKey(receiver)) {
			throw new DuplicateSubscriptionException(receiver
					+ " is already subscribed.");
		}
		tagSubscriberToQueueMap.put(receiver,
				new LinkedBlockingQueue<ReadCycle>());
		eventSubscriberToQueueMap.put(receiver,
				new LinkedBlockingQueue<Object>());
		inUse.compareAndSet(false, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#unsubscribe(java.lang.Object)
	 */
	@Override
	public void unsubscribe(Object receiver) throws NotSubscribedException {
		if (!tagSubscriberToQueueMap.containsKey(receiver)) {
			throw new NotSubscribedException(receiver + " is not subscribed.");
		}
		tagSubscriberToQueueMap.remove(receiver);
		eventSubscriberToQueueMap.remove(receiver);
		if (tagSubscriberToQueueMap.isEmpty() && receivers.isEmpty()) {
			inUse.compareAndSet(true, false);
		}

	}
	
	public void destroy(){
		receivers.clear();
	}

}
