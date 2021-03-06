package org.rifidi.edge.core.readerplugin;

import java.io.Serializable;

/**
 * Abstract Description of a Reader. It defines all properties necessary to
 * create a new Instance of this ReaderPlugin type. All ReaderPlugins need to
 * provide a extended Version of this ReaderInfo in order to create new
 * ReaderSessions in the ReaderSessionService. The information in here is also
 * taken to create a physical connection to the reader.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public abstract class ReaderInfo implements Serializable {

	/**
	 * IPAddress of the Reader
	 */
	private String ipAddress;

	/**
	 * Port of the Reader
	 */
	private int port;

	/**
	 * Interval to wait before trying to reconnect
	 */
	private long reconnectionIntervall;

	/**
	 * Number of maximal attempts to reconnect to a reader
	 */
	private int maxNumConnectionsAttemps;

	/**
	 * Get the IpAdress of this reader
	 * 
	 * @return IPAddress of this reader
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set the IpAddress of this reader
	 * 
	 * @param ipAddress
	 *            of this reader
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Get the port of this reader
	 * 
	 * @return port of this reader
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the port of this reader
	 * 
	 * @param port
	 *            of this reader
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Get the interval to wait before trying to reconnect if the connection was
	 * interrupted
	 * 
	 * @return the reconnectionIntervall in ms
	 */
	public long getReconnectionIntervall() {
		return reconnectionIntervall;
	}

	/**
	 * Set the interval to wait before trying to reconnect if the connection was
	 * interrupted
	 * 
	 * @param reconnectionIntervall
	 *            the reconnectionIntervall to set in ms
	 */
	public void setReconnectionIntervall(long reconnectionIntervall) {
		this.reconnectionIntervall = reconnectionIntervall;
	}

	/**
	 * Get the maximal attempts to reconnect if the connection fails
	 * 
	 * @return the number of maximal attemps to reconnect
	 */
	public int getMaxNumConnectionsAttemps() {
		return maxNumConnectionsAttemps;
	}

	/**
	 * Set the maximal attempts to reconnect if the connection fails
	 * 
	 * @param maxNumConnectionsAttemps
	 *            the maxNumConnectionsAttemps to set
	 */
	public void setMaxNumConnectionsAttemps(int maxNumConnectionsAttemps) {
		this.maxNumConnectionsAttemps = maxNumConnectionsAttemps;
	}

}
