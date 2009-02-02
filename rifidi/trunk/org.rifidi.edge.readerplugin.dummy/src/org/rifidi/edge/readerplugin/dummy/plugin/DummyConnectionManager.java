package org.rifidi.edge.readerplugin.dummy.plugin;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.api.communication.Connection;
import org.rifidi.edge.core.api.exceptions.RifidiConnectionException;
import org.rifidi.edge.core.readerplugin.ReaderInfo;
import org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager;
import org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionStreams;
import org.rifidi.edge.core.readerplugin.protocol.CommunicationProtocol;
import org.rifidi.edge.readerplugin.dummy.protocol.DummyCommunicationProtocol;
import org.rifidi.edge.readerplugin.dummy.readerServer.DummyReaderServer;

public class DummyConnectionManager extends ConnectionManager {

	private static final Log logger = LogFactory
			.getLog(DummyConnectionManager.class);
	DummyReaderInfo info;

	private DummyReaderServer server;

	/**
	 * The socket for this connection
	 */
	private Socket socket;

	public DummyConnectionManager(ReaderInfo readerInfo) {
		super(readerInfo);
		this.info = (DummyReaderInfo) readerInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #connect()
	 */
	@Override
	public void connect(Connection connection) throws RifidiConnectionException {
		// always succeeds
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #disconnect()
	 */
	@Override
	public void disconnect(Connection connection) {

		if (server != null) {
			server.stop();
		}

		// logical disconnect
		try {
			connection.sendMessage(new String("quit\n"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// physical disconnect
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			logger.debug("Socket already closed");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #getCommunicationProtocol()
	 */
	@Override
	public CommunicationProtocol getCommunicationProtocol() {
		return new DummyCommunicationProtocol(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #startKeepAlive()
	 */
	@Override
	public void startKeepAlive(Connection connection) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #createCommunication()
	 */
	@Override
	public ConnectionStreams createCommunication()
			throws RifidiConnectionException {

		server = new DummyReaderServer(info.getPort(), false);
		server.start();

		ConnectionStreams streams = null;
		try {
			socket = new Socket(info.getIpAddress(), info.getPort());
			streams = new ConnectionStreams(socket.getInputStream(), socket
					.getOutputStream());
		} catch (UnknownHostException e) {
			throw new RifidiConnectionException("Cannot create socket: "
					+ info.getIpAddress() + ":" + info.getPort()
					+ " is an unknown host", e);
		} catch (IOException e) {
			throw new RifidiConnectionException(
					"Cannot create socket due to IOException ", e);
		}
		return streams;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #getMaxNumConnectionsAttemps()
	 */
	@Override
	public int getMaxNumConnectionsAttempts() {
		return (info.getMaxNumConnectionsAttempts() >= 1) ? info
				.getMaxNumConnectionsAttempts() : 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.connectionmanager.ConnectionManager
	 * #getReconnectionIntervall()
	 */
	@Override
	public long getReconnectionInterval() {
		return (info.getReconnectionInterval() >= 0) ? info
				.getReconnectionInterval() : 1000;
	}

	@Override
	public void stopKeepAlive(Connection connection) {
		// ignore this.

	}

}
