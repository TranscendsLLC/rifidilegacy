/**
 * 
 */
package org.rifidi.edge.lr;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.rifidi.edge.epcglobal.ale.api.lr.data.LRProperty;
import org.rifidi.edge.epcglobal.ale.api.lr.data.LRSpec;
import org.rifidi.edge.epcglobal.ale.api.lr.data.LRSpec.Properties;
import org.rifidi.edge.epcglobal.ale.api.lr.ws.ImmutableReaderExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.lr.ws.InUseExceptionResponse;
import org.rifidi.edge.epcglobal.ale.api.lr.ws.ReaderLoopExceptionResponse;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class LogicalReaderImpl implements LogicalReader {
	/** Objects using the reader. */
	private Set<Object> users;
	/** Name of the reader. */
	private String name;
	/** Properties of the reader. */
	private Map<String, String> properties;
	/** True if the reader can't be changed or deleted. */
	private Boolean immutable;
	/** Set containing all parents of this reader. */
	private Set<LogicalReader> parents;

	/**
	 * Constructor.
	 * 
	 * @param immutable
	 * @param name
	 * @param properties
	 */
	public LogicalReaderImpl(Boolean immutable, String name,
			Set<LogicalReader> readers, Map<String, String> properties) {
		if (immutable == null) {
			throw new IllegalArgumentException("immutable must not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		if(readers != null && readers.size()>0){
			throw new IllegalArgumentException("This type of reader can't have sub readers.");
		}
		if (properties == null) {
			throw new IllegalArgumentException("properties must not be null");
		}
		this.immutable = immutable;
		this.name = name;
		this.properties = new ConcurrentHashMap<String, String>(properties);
		this.users = new HashSet<Object>();
		this.parents = new HashSet<LogicalReader>();
		for(LogicalReader reader:readers){
			reader.makeChildOf(this);	
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#aquire(java.lang.Object)
	 */
	@Override
	public void aquire(Object user) {
		synchronized (users) {
			users.add(user);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#destroy()
	 */
	@Override
	public void destroy() throws ImmutableReaderExceptionResponse,
			InUseExceptionResponse {
		if (isImmutable()) {
			throw new ImmutableReaderExceptionResponse("Reader is immutable.");
		}
		if (!users.isEmpty()) {
			throw new InUseExceptionResponse("There are " + users.size()
					+ " users using the reader.");
		}
		if (hasParents()){
			throw new InUseExceptionResponse("There are " + parents.size()
					+ " readers using the reader.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#getLRSpec()
	 */
	@Override
	public LRSpec getLRSpec() {
		LRSpec lrSpec = new LRSpec();
		lrSpec.setIsComposite(false);
		Properties props = new Properties();
		for (String key : properties.keySet()) {
			LRProperty prop = new LRProperty();
			prop.setName(key);
			prop.setValue(properties.get(key));
			props.getProperty().add(prop);
		}
		lrSpec.setProperties(props);
		// no readers in a non composite reader
		lrSpec.setReaders(new LRSpec.Readers());
		return lrSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#getUsers()
	 */
	@Override
	public Set<Object> getUsers() {
		synchronized (users) {
			return new HashSet<Object>(users);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#isImmutable()
	 */
	@Override
	public boolean isImmutable() {
		return immutable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#isInUse()
	 */
	@Override
	public boolean isInUse() {
		return !users.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#release(java.lang.Object)
	 */
	@Override
	public void release(Object user) {
		synchronized (users) {
			users.remove(user);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setProperty(String propertyName, String propertyValue)
			throws ImmutableReaderExceptionResponse, InUseExceptionResponse {
		synchronized (users) {
			if (!isInUse()) {
				if (!isImmutable()) {
					this.properties.put(propertyName, propertyValue);
					return;
				} else {
					throw new ImmutableReaderExceptionResponse(
							"Reader is immutable.");
				}
			}
			throw new InUseExceptionResponse("Reader is in use.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#update(java.util.Map,
	 * java.util.Set)
	 */
	@Override
	public void update(Map<String, String> properties,
			Set<LogicalReader> readers)
			throws ImmutableReaderExceptionResponse, InUseExceptionResponse,
			ReaderLoopExceptionResponse {
		if(readers != null && readers.size()>0){
			throw new IllegalArgumentException("This type of reader can't have sub readers.");
		}
		synchronized (users) {
			if (!isInUse()) {
				if (!isImmutable()) {
					this.properties.clear();
					for (String key : properties.keySet()) {
						this.properties.put(key, properties.get(key));
					}
					return;
				} else {
					throw new ImmutableReaderExceptionResponse(
							"Reader is immutable.");
				}
			}
			throw new InUseExceptionResponse("Reader is in use.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.lr.LogicalReader#getReaders()
	 */
	@Override
	public Set<LogicalReader> getReaders() {
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.lr.LogicalReader#growUp(org.rifidi.edge.lr.LogicalReader)
	 */
	@Override
	public void growUp(LogicalReader parent) {
		parents.remove(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.lr.LogicalReader#makeChildOf(org.rifidi.edge.lr.LogicalReader
	 * )
	 */
	@Override
	public void makeChildOf(LogicalReader parent) {
		parents.add(parent);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.edge.lr.LogicalReader#hasparents()
	 */
	@Override
	public boolean hasParents() {
		return !parents.isEmpty();
	}
	
}
