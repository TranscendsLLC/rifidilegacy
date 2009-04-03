/**
 * 
 */
package org.rifidi.edge.epcglobal.aleread;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.epcglobal.ale.api.read.data.ECSpec;
import org.rifidi.edge.epcglobal.ale.api.read.ws.DuplicateNameExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.read.ws.DuplicateSubscriptionExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.read.ws.ECSpecValidationExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.read.ws.InvalidURIExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.read.ws.NoSuchNameExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.read.ws.NoSuchSubscriberExceptionResponse;
import org.rifidi.edge.esper.EsperManagementService;
import org.rifidi.edge.esper.ProcessedEvent;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class ECSPECManagerServiceImpl implements ECSPECManagerService {
	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(ECSPECManagerService.class);
	/** Map containing the spec name as key and the spec as value. */
	private Map<String, RifidiECSpec> nameToSpec;
	/** Map containing the spec name as key and a set of target uris as value. */
	private Map<String, Set<URI>> nameToURIs;
	/** Map containing the URI as key and the target as value. */
	private Map<URI, TestListener> uriToListener;
	/** Esper engine isntance. */
	private EPServiceProvider esper;
	/** Threadpool for executing the scheduled triggers. */
	private ScheduledExecutorService triggerpool;

	/**
	 * Constructor.
	 */
	public ECSPECManagerServiceImpl() {
		nameToSpec = new HashMap<String, RifidiECSpec>();
		nameToURIs = new HashMap<String, Set<URI>>();
		uriToListener = new HashMap<URI, TestListener>();
		// TODO: we might have to manage the size of the pool size but that
		// requires profiling
		triggerpool = new ScheduledThreadPoolExecutor(10);
	}

	/**
	 * @param esperManagement
	 *            the esperManagement to set
	 */
	public void setEsperManagement(EsperManagementService esperManagement) {
		esper = esperManagement.getProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#createSpec(java
	 * .lang.String, org.rifidi.edge.epcglobal.ale.api.read.data.ECSpec)
	 */
	@Override
	public synchronized void createSpec(String name, ECSpec spec)
			throws DuplicateNameExceptionResponse,
			ECSpecValidationExceptionResponse {
		synchronized (this) {
			logger.debug("Defining " + name);
			if (!nameToSpec.containsKey(name)) {
				try {
					RifidiECSpec ecSpec = new RifidiECSpec(name, spec, esper,
							triggerpool);
					nameToSpec.put(name, ecSpec);
					return;
				} catch (InvalidURIExceptionResponse e) {
					throw new ECSpecValidationExceptionResponse(e.toString());
				}
			}
			throw new DuplicateNameExceptionResponse("A spec named " + name
					+ " already exists.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#destroySpec(java
	 * .lang.String)
	 */
	@Override
	public void destroySpec(String name) throws NoSuchNameExceptionResponse {
		synchronized (this) {
			if (!nameToSpec.containsKey(name)) {
				throw new NoSuchNameExceptionResponse(name + " doesn't exist.");
			}
			RifidiECSpec spec = nameToSpec.remove(name);
			nameToURIs.remove(name);
			spec.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#getNames()
	 */
	@Override
	public Set<String> getNames() {
		synchronized (this) {
			return new HashSet<String>(nameToSpec.keySet());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#getSpecByName(
	 * java.lang.String)
	 */
	@Override
	public ECSpec getSpecByName(String name) throws NoSuchNameExceptionResponse {
		synchronized (this) {
			RifidiECSpec ret = nameToSpec.get(name);
			if (ret == null) {
				throw new NoSuchNameExceptionResponse("No spec named " + name
						+ " exists.");
			}
			return ret.getSpec();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#subscribe(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void subscribe(String specName, String uri)
			throws NoSuchNameExceptionResponse,
			DuplicateSubscriptionExceptionResponse, InvalidURIExceptionResponse {
		synchronized (this) {
			try {
				URI target = new URI(uri);
				// check if the spec actually exists
				if (nameToSpec.containsKey(specName)) {
					// create the list for holding the uri if not yet done
					if (!nameToURIs.containsKey(specName)) {
						nameToURIs.put(specName,
								new ConcurrentSkipListSet<URI>());
					}
					// check if the uri is already registered
					if (!nameToURIs.get(specName).contains(target)) {
						nameToURIs.get(specName).add(target);
						TestListener testListener = new TestListener();
						uriToListener.put(target, testListener);
						// start has to happen here to ensure that all
						// statements are created
						nameToSpec.get(specName).start();
						nameToSpec.get(specName).registerUpdateListener(
								testListener);
						return;
					}
					throw new DuplicateSubscriptionExceptionResponse(uri
							+ " is already registered to " + uri);
				}
				throw new NoSuchNameExceptionResponse("A spec named "
						+ specName + " doesn't exist. ");
			} catch (URISyntaxException e) {
				throw new InvalidURIExceptionResponse(e.toString());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.epcglobal.aleread.ECSPECManagerService#unsubscribe(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void unsubscribe(String specName, String uri)
			throws NoSuchNameExceptionResponse,
			NoSuchSubscriberExceptionResponse, InvalidURIExceptionResponse {
		synchronized (this) {
			try {
				URI target = new URI(uri);
				// check if the spec actually exists
				if (nameToSpec.containsKey(specName)) {
					// get the list holding the uris
					if (nameToURIs.containsKey(specName)) {
						// try to remove the uri
						boolean rem = nameToURIs.get(specName).remove(target);
						if (rem) {
							if (nameToURIs.get(specName).isEmpty()) {
								// clean up if the set is now empty
								nameToURIs.remove(specName);
								nameToSpec.get(specName)
										.unregisterUpdateListener(
												uriToListener.remove(target));
							}
							return;
						}
					}
					throw new NoSuchSubscriberExceptionResponse(
							"No subscription from " + uri + " to " + specName);
				}
				throw new NoSuchNameExceptionResponse("A spec named "
						+ specName + " doesn't exist. ");
			} catch (URISyntaxException e) {
				throw new InvalidURIExceptionResponse(e.toString());
			}
		}
	}

	private class TestListener implements UpdateListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.espertech.esper.client.UpdateListener#update(com.espertech.esper
		 * .client.EventBean[], com.espertech.esper.client.EventBean[])
		 */
		@Override
		public void update(EventBean[] arg0, EventBean[] arg1) {
			esper.getEPRuntime().sendEvent(new StartEvent("wuhu"));
			try {
				System.out.println("start");
				for (EventBean bean : arg0) {
					System.out.println(bean);
				}
				System.out.println("stop");
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

	}
}
