
package org.rifidi.edge.core.exceptions;

/**
 * Thrown if a readerSession is not available.
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class NoReaderAvailableException extends Exception {

	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.  
	 */
	public NoReaderAvailableException() {
		super();
	}

	/**
	 * Constructor.  
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public NoReaderAvailableException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor.  
	 * 
	 * @param arg0
	 */
	public NoReaderAvailableException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor.  
	 * 
	 * @param arg0
	 */
	public NoReaderAvailableException(Throwable arg0) {
		super(arg0);
	}

}
