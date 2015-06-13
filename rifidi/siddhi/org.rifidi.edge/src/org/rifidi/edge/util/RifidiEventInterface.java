package org.rifidi.edge.util;

import java.io.Serializable;

public interface RifidiEventInterface 
		extends Serializable {
	
	Object[] getEventAttributes();
	String getEventName();

}
