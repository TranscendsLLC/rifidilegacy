
package org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.1.3
 * Mon Mar 02 15:00:47 CET 2009
 * Generated source version: 2.1.3
 * 
 */

@WebFault(name = "NoSuchSubscriberException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class NoSuchSubscriberExceptionResponse extends Exception {
    public static final long serialVersionUID = 20090302150047L;
    
    private org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.NoSuchSubscriberException noSuchSubscriberException;

    public NoSuchSubscriberExceptionResponse() {
        super();
    }
    
    public NoSuchSubscriberExceptionResponse(String message) {
        super(message);
    }
    
    public NoSuchSubscriberExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchSubscriberExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.NoSuchSubscriberException noSuchSubscriberException) {
        super(message);
        this.noSuchSubscriberException = noSuchSubscriberException;
    }

    public NoSuchSubscriberExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.NoSuchSubscriberException noSuchSubscriberException, Throwable cause) {
        super(message, cause);
        this.noSuchSubscriberException = noSuchSubscriberException;
    }

    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.NoSuchSubscriberException getFaultInfo() {
        return this.noSuchSubscriberException;
    }
}
