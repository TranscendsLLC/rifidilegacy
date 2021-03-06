/*
 * ECSpecValidationExceptionResponse.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.1.3
 * Mon Mar 02 15:00:47 CET 2009
 * Generated source version: 2.1.3
 * 
 */

@WebFault(name = "ECSpecValidationException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class ECSpecValidationExceptionResponse extends Exception {
    public static final long serialVersionUID = 20090302150047L;
    
    private org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ECSpecValidationException ecSpecValidationException;

    public ECSpecValidationExceptionResponse() {
        super();
    }
    
    public ECSpecValidationExceptionResponse(String message) {
        super(message);
    }
    
    public ECSpecValidationExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public ECSpecValidationExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ECSpecValidationException ecSpecValidationException) {
        super(message);
        this.ecSpecValidationException = ecSpecValidationException;
    }

    public ECSpecValidationExceptionResponse(String message, org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ECSpecValidationException ecSpecValidationException, Throwable cause) {
        super(message, cause);
        this.ecSpecValidationException = ecSpecValidationException;
    }

    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ECSpecValidationException getFaultInfo() {
        return this.ecSpecValidationException;
    }
}
