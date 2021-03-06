package org.rifidi.edge.testing;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class JUnitListener extends RunListener {
	private static final Log logger = LogFactory.getLog(JUnitListener.class);
	
	/* (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testFinished(org.junit.runner.Description)
	 */
	public void testFinished(Description description){
		logger.info("JUnit Finished: " + description);
	}
	
	/* (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testFailure(org.junit.runner.notification.Failure)
	 */
	public void testFailure(Failure failure){
		logger.fatal("JUnit Failure: " + failure);
		//logger.error(failure.getMessage());
		logger.fatal("JUnit Failure: " + failure.getTrace());
	}
	
	/* (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testRunFinished(org.junit.runner.Result)
	 */
	public void testRunFinished(Result result) {
		logger.info("JUnits that ran: " + result.getRunCount());
		logger.info("JUnit runtime: " + ((double) result.getRunTime() / 1000) + " second(s)") ;
		
		if (result.wasSuccessful()) {
			logger.info("No Junits failed.");
		} else {
			logger.fatal("JUnits that failed: " + result.getFailureCount());
			List<Failure> failures = result.getFailures();
			for (Failure failure: failures){
				logger.fatal("JUnit Failure: " + failure);
				//logger.error("JUnit Failure (Stack Trace): " + failure.getTrace());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testRunStarted(org.junit.runner.Description)
	 */
	public void testRunStarted(Description description) {
		for (Description d: description.getChildren()){
			logger.info("Setting up to run Junit: " + d);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testStarted(org.junit.runner.Description)
	 */
	public void testStarted(Description description) {
		logger.info("Attempting to run Junit: " + description);
	}
}
