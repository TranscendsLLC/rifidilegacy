/**
 * 
 */
package org.rifidi.edge.epcglobal.aleread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.messages.DatacontainerEvent;
import org.rifidi.edge.core.messages.TagReadEvent;
import org.rifidi.edge.epcglobal.ale.api.read.data.ECReport;
import org.rifidi.edge.epcglobal.ale.api.read.data.ECReports;
import org.rifidi.edge.epcglobal.ale.api.read.data.ECSpec;
import org.rifidi.edge.epcglobal.ale.api.read.data.ECReports.Reports;
import org.rifidi.edge.epcglobal.aleread.ALEReadAPI.TriggerCondition;
import org.rifidi.edge.epcglobal.aleread.wrappers.ReportAnswer;
import org.rifidi.edge.epcglobal.aleread.wrappers.RifidiECSpec;
import org.rifidi.edge.epcglobal.aleread.wrappers.RifidiReport;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.event.map.MapEventBean;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class ECReportmanager implements Runnable, StatementAwareUpdateListener {
	/** Logger for this class. */
	private static final Log logger = LogFactory.getLog(ECReportmanager.class);
	/** Reports that need to be processed. */
	private List<RifidiReport> rifidiReports;

	// TODO: synchronization?
	/** List containing statements that trigger when duration ran out. */
	private List<EPStatement> durationStatements;
	/** List containing statements that trigger when the tag set is stable. */
	private List<EPStatement> stableSetIntervalStatements;
	/** List containing statements that trigger when a stop trigger arrived. */
	private List<EPStatement> stopTriggerStatements;
	/** List containing statements that trigger when data is available. */
	private List<EPStatement> whenDataAvailableStatements;
	/**
	 * List containing statements that trigger when a destroy event was
	 * received.
	 */
	private List<EPStatement> destroyStatements;
	/** Incoming events. */
	private LinkedBlockingQueue<EventTuple> eventQueue;

	/** Target uris for the rifidiReports. */
	private List<String> uris;
	/** Name of the ec spec. */
	private String specName;
	/** True if the spec has been destroied by a delete event. */
	private boolean destroied;
	/**
	 * The spec associated with this reportmanager. Null if the spec is not sent
	 * back with the rifidiReports.
	 */
	private ECSpec spec;
	/** Rifidi ec spec owning this manager. */
	private RifidiECSpec owner;
	/** Timer for triggers and intervals. */
	private Timer timer;

	/** Thread pool for sending reports. */
	private ScheduledThreadPoolExecutor threadPoolExecutor;

	private JAXBContext cont;
	private Marshaller marsh;

	/**
	 * Constructor.
	 */
	public ECReportmanager(String specName, EPServiceProvider esper,
			List<RifidiReport> reports, RifidiECSpec owner, List<String> uris) {
		rifidiReports = reports;
		eventQueue = new LinkedBlockingQueue<EventTuple>();
		destroied = false;
		timer = new Timer(specName, owner.getRifidiBoundarySpec()
				.getRepeatInterval(), owner.getRifidiBoundarySpec()
				.getStartTriggers(), owner.getRifidiBoundarySpec()
				.getStopTriggers(), esper);

		this.uris = uris;
		this.specName = specName;
		this.owner = owner;
		try {
			cont = JAXBContext.newInstance(ReportAnswer.class, ECReports.class);
			marsh = cont.createMarshaller();
		} catch (JAXBException e) {
			logger.fatal("Unabel to create JAXB marshaller: " + e);
		}

		threadPoolExecutor = new ScheduledThreadPoolExecutor(10);
	}

	/**
	 * Constructor.
	 */
	public ECReportmanager(String specName, EPServiceProvider esper,
			ECSpec spec, List<RifidiReport> reports, RifidiECSpec owner,
			List<String> uris) {
		this(specName, esper, reports, owner, uris);
		this.spec = spec;
	}

	/**
	 * Add the incoming list of events to the processing queue.
	 * 
	 * @param eventTuple
	 */
	public void addEvents(EventTuple eventTuple) {
		eventQueue.add(eventTuple);
	}

	/**
	 * @param durationStatements
	 *            the durationStatements to set
	 */
	public void setDurationStatements(List<EPStatement> durationStatements) {
		this.durationStatements = durationStatements;
	}

	/**
	 * @param stableSetIntervalStatements
	 *            the stableSetIntervalStatements to set
	 */
	public void setStableSetIntervalStatements(
			List<EPStatement> stableSetIntervalStatements) {
		this.stableSetIntervalStatements = stableSetIntervalStatements;
	}

	/**
	 * @param stopTriggerStatements
	 *            the stopTriggerStatements to set
	 */
	public void setStopTriggerStatements(List<EPStatement> stopTriggerStatements) {
		this.stopTriggerStatements = stopTriggerStatements;
	}

	/**
	 * @param whenDataAvailableStatements
	 *            the whenDataAvailableStatements to set
	 */
	public void setWhenDataAvailableStatements(
			List<EPStatement> whenDataAvailableStatements) {
		this.whenDataAvailableStatements = whenDataAvailableStatements;
	}

	/**
	 * @param destroyStatements
	 *            the destroyStatements to set
	 */
	public void setDestroyStatements(List<EPStatement> destroyStatements) {
		this.destroyStatements = destroyStatements;
	}

	protected void stopTrigger(String uri) {
		logger.debug("Stop trigger went off: " + uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.espertech.esper.client.StatementAwareUpdateListener#update(com
	 * .espertech.esper.client.EventBean[],
	 * com.espertech.esper.client.EventBean[],
	 * com.espertech.esper.client.EPStatement,
	 * com.espertech.esper.client.EPServiceProvider)
	 */
	@Override
	public synchronized void update(EventBean[] arg0, EventBean[] arg1,
			EPStatement arg2, EPServiceProvider arg3) {
		for (EventBean tagevent : arg0) {
			if (((MapEventBean) tagevent).getProperties().containsKey(
					"count(whine)")) {
				System.out.println((Long) ((MapEventBean) tagevent).getProperties()
						.get("count(whine)"));
				if ((Long) ((MapEventBean) tagevent).getProperties().get(
						"count(whine)") > 0) {
					logger.debug("count "
							+ (Long) ((MapEventBean) tagevent).getProperties()
									.get("count(whine)"));
					return;
				}
			}
		}
		if (destroied) {
			// ECSpec is destroied.
			return;
		}
		EventTuple tuple = new EventTuple();
		tuple.report = timer.callback();
		if (tuple.report == null
				|| tuple.report.getInitiationCondition() == null) {
			// we got a really fast stop trigger
			return;
		}
		if (destroyStatements.contains(arg2)) {
			destroied = true;
			tuple.report.setTerminationCondition(ALEReadAPI.conditionToName
					.get(TriggerCondition.UNDEFINE));
			// callback for destroying the statements
			owner.discard();
		} else {
			if (!ALEReadAPI.conditionToName.get(
					ALEReadAPI.TriggerCondition.TRIGGER).equals(
					tuple.report.getTerminationCondition())) {
				if (whenDataAvailableStatements.contains(arg2)) {
					tuple.report
							.setTerminationCondition(ALEReadAPI.conditionToName
									.get(TriggerCondition.DATA_AVAILABLE));
				}
				if (stableSetIntervalStatements.contains(arg2)) {
					tuple.report
							.setTerminationCondition(ALEReadAPI.conditionToName
									.get(TriggerCondition.STABLE_SET));
				}
				if (durationStatements.contains(arg2)) {
					tuple.report
							.setTerminationCondition(ALEReadAPI.conditionToName
									.get(TriggerCondition.DURATION));
				}
			}
		}

		tuple.beans = arg0;
		addEvents(tuple);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// subscribe to all statements
		for (EPStatement statement : durationStatements) {
			statement.addListener(this);
		}
		for (EPStatement statement : stableSetIntervalStatements) {
			statement.addListener(this);
		}
		for (EPStatement statement : stopTriggerStatements) {
			statement.addListener(this);
		}
		for (EPStatement statement : whenDataAvailableStatements) {
			statement.addListener(this);
		}
		for (EPStatement statement : destroyStatements) {
			statement.addListener(this);
		}
		timer.start();

		// Always true because we hit this one only if run was hit and that
		// means the spec was requested.
		while (!Thread.currentThread().isInterrupted()) {
			GregorianCalendar gc = (GregorianCalendar) GregorianCalendar
					.getInstance();
			DatatypeFactory dataTypeFactory = null;
			try {
				dataTypeFactory = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException ex) {
				logger.fatal("epic fail: " + ex);
			}
			try {

				// get the eventtuple submitted last
				EventTuple tagevents = eventQueue.take();
				Set<DatacontainerEvent> events = new HashSet<DatacontainerEvent>();
				// collect tags from event beans
				for (EventBean tagevent : tagevents.beans) {
					if (((MapEventBean) tagevent).getProperties().containsKey(
							"tags")) {
						events.add(((TagReadEvent) ((MapEventBean) tagevent)
								.get("tags")).getTag());
					} else {
						break;
					}
				}
				// process the set of events
				for (RifidiReport rifidiReport : rifidiReports) {
					rifidiReport.processEvents(events);
				}
				// send the report
				ECReports ecreports = tagevents.report;
				ecreports.setALEID("Rifidi Edge Server");
				ecreports.setDate(dataTypeFactory.newXMLGregorianCalendar(gc));
				if (spec != null) {
					ecreports.setECSpec(spec);
				}

				ecreports.setSpecName(specName);
				// running time
				ecreports.setTotalMilliseconds(10);
				Reports reportsPoltergeist = new Reports();
				ecreports.setReports(reportsPoltergeist);

				StringBuilder buildy = new StringBuilder("startcondition: "
						+ ecreports.getInitiationCondition() + "\n");
				buildy.append("starttrigger: "
						+ ecreports.getInitiationTrigger() + "\n");
				buildy.append("stopcondition: "
						+ ecreports.getTerminationCondition() + "\n");
				buildy.append("stoptrigger: "
						+ ecreports.getTerminationTrigger() + "\n");
				logger.debug(buildy.toString());
				for (RifidiReport rifidiReport : rifidiReports) {
					ECReport rep = rifidiReport.send();
					if (rep != null) {
						ecreports.getReports().getReport().add(rep);
					}
				}
				for (String uri : uris) {
					logger.debug("Sending to " + uri);
					threadPoolExecutor.submit(new SendRunnable(uri, ecreports));
				}
			} catch (InterruptedException e) {
				timer.stop();
				Thread.currentThread().interrupt();
			}
		}
		timer.stop();
	}

	private class EventTuple {
		public EventBean[] beans;
		public ECReports report;
	}

	private class SendRunnable implements Runnable {
		/** Target of the report */
		private String uri;
		private ECReports reports;

		/**
		 * @param uri
		 * @param reports
		 */
		public SendRunnable(String uri, ECReports reports) {
			this.uri = uri;
			this.reports = reports;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			String[] str = uri.toString().split(":");
			try {
				Socket socket = new Socket(str[0], Integer.parseInt(str[1]));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),
						true);

				try {
					reports.setECSpec(spec);
					ReportAnswer answer = new ReportAnswer();
					answer.reports = reports;
					marsh.marshal(answer, out);
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.flush();
				out.close();
				socket.close();
			} catch (NumberFormatException e) {
				logger.warn(e);
			} catch (UnknownHostException e) {
				logger.warn(e);
			} catch (IOException e) {
				logger.warn(e);
			}
		}

	}
}
