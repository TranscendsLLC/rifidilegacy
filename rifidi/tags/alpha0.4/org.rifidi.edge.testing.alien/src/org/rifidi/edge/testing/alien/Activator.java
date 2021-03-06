package org.rifidi.edge.testing.alien;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.edge.testing.service.TestingService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("== Bundle Alien Tests Started ==");
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("== Bundle Alien Tests Stopped ==");
	}

	/**
	 * Injects the testing service
	 * 
	 * @param service
	 */
	@Inject
	public void setTestingService(TestingService service){
		System.out.println("Starting to test the Alien plugin.");
		service.addJunitTests(AlienTestTagRead.class);
	}

}
