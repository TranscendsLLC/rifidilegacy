/**
 * 
 */
package org.rifidi.edge.newcore.internal.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceRegistration;
import org.rifidi.edge.core.api.readerplugin.messageQueue.MessageQueue;
import org.rifidi.edge.newcore.commands.Command;
import org.rifidi.edge.newcore.commands.CommandFactory;
import org.rifidi.edge.newcore.commands.CommandState;
import org.rifidi.edge.newcore.exceptions.NoReaderAvailableException;
import org.rifidi.edge.newcore.internal.ReaderSession;
import org.rifidi.edge.newcore.readers.Reader;
import org.rifidi.edge.newcore.readers.ReaderConfiguration;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class ReaderSessionImpl implements ReaderSession {
	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(ReaderSessionImpl.class);
	/** Command the session is executing. */
	private CommandFactory<?> commandFactory;
	/** Factory used for aquiring readers. */
	private ReaderConfiguration<?> factory;
	/** True if the command is currently executing on a reader. */
	private AtomicBoolean running;
	/** Service registration for the service. */
	private ServiceRegistration registration;
	/** Message queue for outgoing messages. */
	private MessageQueue messageQueue;
	/** Currently executing command. */
	private Command command;
	/** Set to true if someone attempted to kill the command. */
	private AtomicBoolean dying;

	/**
	 * Constructor.
	 */
	public ReaderSessionImpl() {
		running = new AtomicBoolean(false);
		dying = new AtomicBoolean(false);
		logger.debug("Reader session created.");
	}

	/**
	 * @param registration
	 *            the registration to set
	 */
	public void setRegistration(ServiceRegistration registration) {
		this.registration = registration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.newcore.internal.ReaderSession#destroy()
	 */
	@Override
	public void destroy() {
		stop();
		registration.unregister();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.newcore.internal.ReaderSession#getReaderFactory()
	 */
	@Override
	public ReaderConfiguration<?> getReaderFactory() {
		return factory;
	}

	/*
	 * (non-Javadoc) logger.warn("Unable to aquire reader: " + e);
	 * 
	 * @see org.rifidi.edge.newcore.internal.ReaderSession#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.newcore.internal.ReaderSession#getCommandFactory()
	 */
	@Override
	public CommandFactory<?> getCommandFactory() {
		// TODO Auto-generated method stub
		return commandFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.newcore.internal.ReaderSession#setCommmandFactory(org
	 * .rifidi.edge.newcore.commands.CommandFactory)
	 */
	@Override
	public void setCommmandFactory(CommandFactory<?> commandFactory) {
		assert (!running.get());
		assert (commandFactory != null);
		logger.debug("Setting command factory: " + commandFactory);
		this.commandFactory = commandFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.newcore.internal.ReaderSession#setReaderFactory(org.rifidi
	 * .edge.core.api.readerplugin.ReaderFactory)
	 */
	@Override
	public void setReaderFactory(ReaderConfiguration<?> readerFactory) {
		assert (!running.get());
		assert (readerFactory != null);
		logger.debug("Setting reader factory: " + readerFactory);
		this.factory = readerFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.newcore.internal.ReaderSession#setMessageQueue(org.rifidi
	 * .edge.core.api.readerplugin.messageQueue.MessageQueue)
	 */
	@Override
	public void setMessageQueue(MessageQueue messageQueue) {
		assert (!running.get());
		assert (messageQueue != null);
		logger.debug("Setting message queue: " + messageQueue);
		this.messageQueue = messageQueue;
	}

	private boolean canStart() {
		return commandFactory != null && factory != null
				&& messageQueue == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Boolean call() throws Exception {
		if (!canStart())
			return false;
		if (running.compareAndSet(false, true)) {
			try {
				logger
						.debug("Starting with: " + commandFactory + " "
								+ factory);
				try {
					Reader reader = factory.aquireReader();
					command = commandFactory.getCommand();
					command.setReader(reader);
					command.setMessageQueue(null);

					Future<CommandState> future = reader.execute(command);
					CommandState state = future.get();
					switch (state) {
					case DONE:
						break;
					case KILLED:
						break;
					case FAILED:
						break;
					case LOSTCONNECTION:
						return call();
					}
				} catch (NoReaderAvailableException e) {
					logger.warn("Unable to aquire reader: " + e);
				} catch (InterruptedException e) {
					logger.debug("Terminated: " + e);
					Thread.currentThread().interrupt();
				} catch (ExecutionException e) {
					logger.error("Failed executing: " + e);
				}
				return true;
			} finally {
				// reset so that the session can be started again
				running.compareAndSet(true, false);
				dying.compareAndSet(true, false);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.newcore.internal.ReaderSession#stop()
	 */
	@Override
	public void stop() {
		if (dying.compareAndSet(false, true)) {
			logger.debug("Stopping command: " + command);
			command.stop();
		}
	}

}
