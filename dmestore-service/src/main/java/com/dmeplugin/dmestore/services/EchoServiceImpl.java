/* Copyright (c) 2012-2018 VMware, Inc. All rights reserved. */
package com.dmeplugin.dmestore.services;


import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Implementation of the EchoService interface
 */
public class EchoServiceImpl implements EchoService {
   private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);
   /* (non-Javadoc)
    * @see com.dmeplugin.dmestore.EchoService#echo(java.lang.String)
    */
   public String echo(String message) {

      return message;
   }
}
