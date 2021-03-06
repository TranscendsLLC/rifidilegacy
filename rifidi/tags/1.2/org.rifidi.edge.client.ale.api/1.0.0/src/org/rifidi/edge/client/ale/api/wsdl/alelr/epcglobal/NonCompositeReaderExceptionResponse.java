
package org.rifidi.edge.client.ale.api.wsdl.alelr.epcglobal;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.1.3
 * Mon Mar 02 15:28:33 CET 2009
 * Generated source version: 2.1.3
 * 
 */

@WebFault(name = "NonCompositeReaderException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class NonCompositeReaderExceptionResponse extends Exception {
    public static final long serialVersionUID = 20090302152833L;
    
    private org.rifidi.edge.client.ale.api.wsdl.alelr.epcglobal.NonCompositeReaderException nonCompositeReaderException;

    public NonCompositeReaderExceptionResponse() {
        super();
    }
    
    public NonCompositeReaderExceptionResponse(String message) {
        super(message);
    }
    
    public NonCompositeReaderExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public NonCompositeReaderExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.alelr.epcglobal.NonCompositeReaderException nonCompositeReaderException) {
        super(message);
        this.nonCompositeReaderException = nonCompositeReaderException;
    }

    public NonCompositeReaderExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.alelr.epcglobal.NonCompositeReaderException nonCompositeReaderException, Throwable cause) {
        super(message, cause);
        this.nonCompositeReaderException = nonCompositeReaderException;
    }

    public org.rifidi.edge.client.ale.api.wsdl.alelr.epcglobal.NonCompositeReaderException getFaultInfo() {
        return this.nonCompositeReaderException;
    }
}
