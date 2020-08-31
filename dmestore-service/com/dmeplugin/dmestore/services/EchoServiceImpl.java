/* Copyright (c) 2012-2018 VMware, Inc. All rights reserved. */
package com.dmeplugin.dmestore.services;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

/**
 * Implementation of the EchoService interface
 */
public class EchoServiceImpl implements EchoService {
   private final static Log _logger = LogFactory.getLog(EchoServiceImpl.class);
   /* (non-Javadoc)
    * @see com.dmeplugin.dmestore.EchoService#echo(java.lang.String)
    */
   public String echo(String message) {
     /* try {
         _logger.info("startme");
         URL serverUrl = new URL("https://10.143.132.248/sdk");
         ServiceInstance serviceInstance = new ServiceInstance(serverUrl, "administrator@vsphere.local", "Pbu4@123", true);

         ManagedEntity[] objs = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("Datastore");
         _logger.info("after");
         for (ManagedEntity managedEntity : objs) {
            _logger.info("in");
            _logger.info(managedEntity.getName());
         }
      }catch (Exception e){
         e.printStackTrace();
      }*/
      return message;
   }
}
