
package org.rifidi.edge.epcglobal.ale.api.lr.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.1.3
 * Thu Jan 29 11:03:18 EST 2009
 * Generated source version: 2.1.3
 * 
 */

@WebFault(name = "InUseException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class InUseExceptionResponse extends Exception {
    public static final long serialVersionUID = 20090129110318L;
    
    private org.rifidi.edge.epcglobal.ale.api.lr.ws.InUseException inUseException;

    public InUseExceptionResponse() {
        super();
    }
    
    public InUseExceptionResponse(String message) {
        super(message);
    }
    
    public InUseExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public InUseExceptionResponse(String message, org.rifidi.edge.epcglobal.ale.api.lr.ws.InUseException inUseException) {
        super(message);
        this.inUseException = inUseException;
    }

    public InUseExceptionResponse(String message, org.rifidi.edge.epcglobal.ale.api.lr.ws.InUseException inUseException, Throwable cause) {
        super(message, cause);
        this.inUseException = inUseException;
    }

    public org.rifidi.edge.epcglobal.ale.api.lr.ws.InUseException getFaultInfo() {
        return this.inUseException;
    }
}