
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.1.3
 * Mon Mar 02 15:00:47 CET 2009
 * Generated source version: 2.1.3
 * 
 */

@javax.jws.WebService(
                      serviceName = "ALEService",
                      portName = "ALEServicePort",
                      targetNamespace = "urn:epcglobal:ale:wsdl:1",
                      wsdlLocation = "file:EPCglobal-ale-1_1-ale.wsdl",
                      endpointInterface = "org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType")
                      
public class ALEServicePortTypeImpl implements ALEServicePortType {

    private static final Logger LOG = Logger.getLogger(ALEServicePortTypeImpl.class.getName());

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#undefine(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Undefine  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder undefine(Undefine parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , SecurityExceptionResponse    { 
        LOG.info("Executing operation undefine");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#getECSpecNames(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.EmptyParms  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ArrayOfString getECSpecNames(EmptyParms parms) throws ImplementationExceptionResponse , SecurityExceptionResponse    { 
        LOG.info("Executing operation getECSpecNames");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ArrayOfString _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#immediate(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Immediate  parms )*
     */
    public org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECReports immediate(Immediate parms) throws ImplementationExceptionResponse , SecurityExceptionResponse , ECSpecValidationExceptionResponse    { 
        LOG.info("Executing operation immediate");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECReports _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
        //throw new ECSpecValidationExceptionResponse("ECSpecValidationExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#define(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Define  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder define(Define parms) throws ImplementationExceptionResponse , SecurityExceptionResponse , ECSpecValidationExceptionResponse , DuplicateNameExceptionResponse    { 
        LOG.info("Executing operation define");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
        //throw new ECSpecValidationExceptionResponse("ECSpecValidationExceptionResponse...");
        //throw new DuplicateNameExceptionResponse("DuplicateNameExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#unsubscribe(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Unsubscribe  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder unsubscribe(Unsubscribe parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , NoSuchSubscriberExceptionResponse , SecurityExceptionResponse , InvalidURIExceptionResponse    { 
        LOG.info("Executing operation unsubscribe");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new NoSuchSubscriberExceptionResponse("NoSuchSubscriberExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
        //throw new InvalidURIExceptionResponse("InvalidURIExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#getECSpec(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.GetECSpec  parms )*
     */
    public org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECSpec getECSpec(GetECSpec parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , SecurityExceptionResponse    { 
        LOG.info("Executing operation getECSpec");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECSpec _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#getVendorVersion(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.EmptyParms  parms )*
     */
    public java.lang.String getVendorVersion(EmptyParms parms) throws ImplementationExceptionResponse    { 
        LOG.info("Executing operation getVendorVersion");
        System.out.println(parms);
        try {
            java.lang.String _return = "";
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#getSubscribers(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.GetSubscribers  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ArrayOfString getSubscribers(GetSubscribers parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , SecurityExceptionResponse    { 
        LOG.info("Executing operation getSubscribers");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ArrayOfString _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#poll(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Poll  parms )*
     */
    public org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECReports poll(Poll parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , SecurityExceptionResponse    { 
        LOG.info("Executing operation poll");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.xsd.ale.epcglobal.ECReports _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#getStandardVersion(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.EmptyParms  parms )*
     */
    public java.lang.String getStandardVersion(EmptyParms parms) throws ImplementationExceptionResponse    { 
        LOG.info("Executing operation getStandardVersion");
        System.out.println(parms);
        try {
            java.lang.String _return = "";
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
    }

    /* (non-Javadoc)
     * @see org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.ALEServicePortType#subscribe(org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.Subscribe  parms )*
     */
    public org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder subscribe(Subscribe parms) throws ImplementationExceptionResponse , NoSuchNameExceptionResponse , DuplicateSubscriptionExceptionResponse , SecurityExceptionResponse , InvalidURIExceptionResponse    { 
        LOG.info("Executing operation subscribe");
        System.out.println(parms);
        try {
            org.rifidi.edge.client.ale.api.wsdl.ale.epcglobal.VoidHolder _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ImplementationExceptionResponse("ImplementationExceptionResponse...");
        //throw new NoSuchNameExceptionResponse("NoSuchNameExceptionResponse...");
        //throw new DuplicateSubscriptionExceptionResponse("DuplicateSubscriptionExceptionResponse...");
        //throw new SecurityExceptionResponse("SecurityExceptionResponse...");
        //throw new InvalidURIExceptionResponse("InvalidURIExceptionResponse...");
    }

}
