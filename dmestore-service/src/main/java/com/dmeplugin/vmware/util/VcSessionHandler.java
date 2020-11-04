/*
 * ******************************************************
 * Copyright VMware, Inc. 2010-2013.  All Rights Reserved.
 * ******************************************************
 *
 * DISCLAIMER. THIS PROGRAM IS PROVIDED TO YOU "AS IS"
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, WHETHER
 * ORAL OR WRITTEN, EXPRESS OR IMPLIED. THE AUTHOR
 * SPECIFICALLY DISCLAIMS ANY IMPLIED WARRANTIES OR
 * CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.dmeplugin.vmware.util;

import org.w3c.dom.DOMException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Handler class to add the vcSessionCookie element inside the soap header
 * 
 * @author Ecosystem Engineering
 */
public final class VcSessionHandler implements SOAPHandler<SOAPMessageContext> {

   private final String vcSessionCookie;

   public VcSessionHandler(String vcSessionCookie) {
      this.vcSessionCookie = vcSessionCookie;
   }

   @Override
   public boolean handleMessage(SOAPMessageContext smc) {
      if (isOutgoingMessage(smc)) {
         try {
            SOAPHeader header = getSoapHeader(smc);

            SOAPElement vcsessionHeader =
                  header.addChildElement(new QName("#",
                        "vcSessionCookie"));
            vcsessionHeader.setValue(vcSessionCookie);

         } catch (DOMException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
         } catch (SOAPException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
         }
      }
      return true;
   }

   @Override
   public void close(MessageContext arg0) {
   }

   @Override
   public boolean handleFault(SOAPMessageContext arg0) {
      return false;
   }

   @Override
   public Set<QName> getHeaders() {
      return null;
   }

   /**
    * Returns the header. If not present then adds one and return the same
    * 
    * @param smc
    * @return
    * @throws SOAPException
    */
   SOAPHeader getSoapHeader(SOAPMessageContext smc) throws SOAPException {
      return smc.getMessage().getSOAPPart().getEnvelope().getHeader() == null ? smc
            .getMessage().getSOAPPart().getEnvelope().addHeader()
            : smc.getMessage().getSOAPPart().getEnvelope().getHeader();
   }

   /**
    * Returns true if the {@link SOAPMessageContext} is part of the request
    * 
    * @param smc
    * @return
    */
   boolean isOutgoingMessage(SOAPMessageContext smc) {
      Boolean outboundProperty =
            (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
      return outboundProperty.booleanValue();
   }

}

/**
 * 
 * @author Ecosystem Engineering
 */
@SuppressWarnings("rawtypes")
final class HeaderHandlerResolver implements HandlerResolver {

   private final List<Handler> handlerChain = new ArrayList<Handler>();

   @Override
   public List<Handler> getHandlerChain(PortInfo arg0) {
      return Collections.unmodifiableList(handlerChain);
   }

   /**
    * Adds a specific {@link Handler} to the handler chain
    * 
    * @param handler
    */
   public void addHandler(SOAPHandler<SOAPMessageContext> handler) {
      handlerChain.add(handler);
   }

   /**
    * Clears the current list of {@link Handler} in the handler chain
    */
   public void clearHandlerChain() {
      handlerChain.clear();
   }
}
