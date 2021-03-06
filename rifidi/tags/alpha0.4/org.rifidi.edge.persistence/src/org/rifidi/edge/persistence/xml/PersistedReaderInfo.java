/* 
 * PersistedReaderInfo.java
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.edge.persistence.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.exceptions.RifidiReaderInfoNotFoundException;
import org.rifidi.edge.core.readerplugin.ReaderInfo;
import org.rifidi.edge.persistence.utilities.JAXBUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Comments considered harmful.
 * 
 * TODO: Potential problem with this: IP and Port might lose uniqueness
 * depending on which readers were loaded at the time.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class PersistedReaderInfo {

	/**
	 * The log4j logger.debug.
	 */
	private static final Log logger = LogFactory
			.getLog(PersistedReaderInfo.class);

	/**
	 * The document object
	 */
	private Document doc = null;

	/**
	 * The root element.
	 */
	private Element root;

	/**
	 * The PersistedReaderInfo constructor.
	 */
	public PersistedReaderInfo() {
		doc = createDocument();
		root = doc.createElement(XMLTags.ROOT_TAG);
		doc.appendChild(root);
	}

	/**
	 * Adds the given reader information to the XML object.
	 * 
	 * @param readerInfo
	 */
	public void addReaderInfo(ReaderInfo readerInfo) {
		Element readerInfoTypeList = findReaderTypeList(readerInfo.getClass()
				.getName());
		if (readerInfoTypeList == null) {
			readerInfoTypeList = doc.createElement(XMLTags.ELEMENT_LIST_TAG);
			readerInfoTypeList.setAttribute(XMLTags.TYPE_TAG, readerInfo
					.getClass().getName());
			root.appendChild(readerInfoTypeList);
		}
		if (this.find(readerInfo, readerInfoTypeList) == null) {
			JAXBUtility.getInstance().save(readerInfo, readerInfoTypeList);
			printToFile();
		}
	}

	/**
	 * Gets a list of the readerInfos stored in the XML file under the given
	 * class.
	 * 
	 * @param readerInfoClass
	 * @return
	 */
	public List<ReaderInfo> getReaderInfos(String className) {
		List<ReaderInfo> retVal = new ArrayList<ReaderInfo>();
		Element ele = this.findReaderTypeList(className);
		if (ele != null) {
			NodeList nodes = ele.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node nod = nodes.item(i);
				if (nod instanceof Element) {
					try {
						// this.printToSTIO(nod);
						ReaderInfo ri = (ReaderInfo) JAXBUtility.getInstance()
								.load(nod);
						retVal.add(ri);
					} catch (JAXBException e) {
						// TODO: probably should throw something here
						e.printStackTrace();
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * Removes the reader from the persistence layer.
	 * 
	 * @param readerInfo
	 * @throws RifidiReaderInfoNotFoundException
	 */
	public void removeReader(ReaderInfo readerInfo)
			throws RifidiReaderInfoNotFoundException {
		Element readerInfoTypeList = this.findReaderTypeList(readerInfo
				.getClass().getName());
		if (readerInfoTypeList == null) {
			throw new RifidiReaderInfoNotFoundException();
		}
		Element readerInfoNode = find(readerInfo, root);
		if (readerInfoNode == null) {
			throw new RifidiReaderInfoNotFoundException();
		}
		readerInfoTypeList.removeChild(readerInfoNode);
		printToFile();
	}

	/**
	 * This will attempt to find a readerInfo in the XML data.
	 * 
	 * @param readerInfo
	 *            The reader we want to find. The information we will use from
	 *            this class is the class name, the IP, and the port.
	 * @param readerInfoTypeList
	 *            This is a list of ReaderInfo datas in XML form. The class
	 *            already matches
	 * @return
	 */
	private Element find(ReaderInfo readerInfo, Element readerInfoTypeList) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			String exp = new String("/*/*/*");
			logger.debug("XPATH expression in the FIND method is: \n" + exp);
			XPathExpression expr = xpath.compile(exp);
			Object oResult = expr.evaluate(readerInfoTypeList,
					XPathConstants.NODESET);
			if (oResult == null) {
				logger.debug("find returning null after the IPADDRESS search");
				return null;
			} else {
				NodeList nodeArray = (NodeList) oResult;

				Node xResult = this.findUniqueReader(readerInfo.getIpAddress(),
						readerInfo.getPort(), nodeArray);

				if (xResult == null) {
					logger.debug("find returning null");
					return null;
				} else {
					logger.debug("RETURNING A RESULT: " + xResult);
					return (Element) xResult;
				}
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Finds a unique reader based on the IP and port given.
	 * 
	 * @param ip
	 *            The IP of the reader you want to find.
	 * @param port
	 *            The port of the reader you want to find.
	 * @param readerList
	 *            The list of readers that will be searched to find the reader.
	 * @return The element representing the reader if it was found, null
	 *         otherwise.
	 */
	private Element findUniqueReader(String ip, int port, NodeList readerList) {
		for (int i = 0, n = readerList.getLength(); i < n; i++) {
			Node node = readerList.item(i);
			logger.debug("NODE: " + node);
			boolean ip_match = false;
			boolean port_match = false;
			NodeList childNodeList = node.getChildNodes();
			for (int x = 0, m = childNodeList.getLength(); x < m; x++) {
				Node child = childNodeList.item(x);
				if (child.getNodeName().equals(XMLTags.IP_ADDRESS)) {
					if (child.getFirstChild().getNodeValue().equals(ip)) {
						ip_match = true;
					}
				}
				if (child.getNodeName().equals(XMLTags.PORT)) {
					if (child.getFirstChild().getNodeValue().equals(
							String.valueOf(port))) {
						port_match = true;
					}
				}
				if (ip_match && port_match) {
					logger.debug("RETURNING: " + node);
					return (Element) node;
				}
			}
		}
		return null;
	}

	/**
	 * Find the readerTypeList.
	 * 
	 * @param readerInfoType
	 * @return
	 */
	private Element findReaderTypeList(String readerInfoType) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Node result = null;
		try {
			String exp = new String("//" + XMLTags.ELEMENT_LIST_TAG + "[@"
					+ XMLTags.TYPE_TAG + "='" + readerInfoType + "']");
			logger.debug("String expression is: \n" + exp);
			XPathExpression expr = xpath.compile(exp);
			Object oResult = expr.evaluate(this.doc, XPathConstants.NODE);
			if (oResult == null) {
				logger.debug("returning null");
				return null;
			} else {
				logger.debug("Not null, Casting the result");
				result = (Node) oResult;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		logger.debug("Just before the return in the reader type list.  ");
		return (Element) result;
	}

	/**
	 * Creates a new Dom document.
	 */
	private Document createDocument() {
		Document dom = null;
		// get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			// get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// create an instance of DOM
			dom = db.newDocument();

		} catch (ParserConfigurationException pce) {
			// dump it
			logger.debug("Error while trying to "
					+ "instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
		return dom;
	}

	/**
	 * Creates a document based on the xml file given.
	 */
	private Document createDocument(File xml) {
		Document dom = null;
		// get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			// get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// create an instance of DOM
			try {
				logger.debug("Before the parse");
				dom = db.parse(xml);
				logger.debug("After Parse, parse successful");
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (ParserConfigurationException pce) {
			// dump it
			logger.debug("Error while trying to "
					+ "instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
		return dom;
	}

	/**
	 * Loads everything into a dom object for testing.
	 */
	public void loadFromFile() {
		File xmlFile = null;

		xmlFile = JAXBUtility.getInstance().getXMLFile();

		if (xmlFile != null) {
			logger.debug("FILE NOT NULL, CREATING NEW DOCUMENT");
			this.doc = this.createDocument(xmlFile);
			this.root = (Element) doc.getFirstChild();
		} else {
			logger.debug("FILE IS NULL");
		}
	}

	/**
	 * Sets the file that this class will use to build the DOM document.
	 * 
	 * @param path
	 *            Sets the path for the folders that the file will use. The
	 *            folders have to be created separately.
	 * @param filename
	 *            The complete path for the file. This is the file that the
	 *            class will create.
	 * @throws IOException
	 */
	public void setFile(String path, String filename) throws IOException {
		JAXBUtility.getInstance().setFile(path, filename);
		this.loadFromFile();
	}

	/**
	 * Prints the document to a file.
	 */
	private void printToFile() {
		logger.debug("Printing the file");
		try {
			// print
			OutputFormat format = new OutputFormat(doc);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					JAXBUtility.getInstance().getXMLFile()), format);

			// XMLSerializer serializer = new XMLSerializer(System.out, format);

			serializer.serialize(doc);

		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * The tags for the XML document that we will search for via Dom and XPath.
	 * 
	 * @author Matthew Dean - matt@pramari.com
	 */
	private class XMLTags {
		/**
		 * 
		 */
		public static final String VALUE = "val";

		/**
		 * The Root tag for the xml.  
		 */
		public static final String ROOT_TAG = "PersistedReaderInfo";

		/**
		 * The tag which contains a list of several ReaderInfo objects, all of
		 * the same ReaderType.  
		 */
		public static final String ELEMENT_LIST_TAG = "ReaderInfoList";

		/**
		 * The ReaderType, which is represented by a package-qualified name of
		 * the ReaderInfo class.
		 */
		public static final String TYPE_TAG = "ReaderType";

		/**
		 * The ip tag we will look for
		 */
		public static final String IP_ADDRESS = "ipAddress";

		/**
		 * The port tag we will look for
		 */
		public static final String PORT = "port";
	}
}
