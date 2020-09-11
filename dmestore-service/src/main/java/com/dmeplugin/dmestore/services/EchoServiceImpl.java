/* Copyright (c) 2012-2018 VMware, Inc. All rights reserved. */
package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.RootFsMO;
import com.dmeplugin.vmware.util.Configuration;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwarePluginContextFactory;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Implementation of the EchoService interface
 */
public class EchoServiceImpl implements EchoService {

   private UserSessionService _userSessionService;
   public UserSessionService get_userSessionService() {
      return _userSessionService;
   }

   public void set_userSessionService(UserSessionService _userSessionService) {
      this._userSessionService = _userSessionService;
   }

   private  VimObjectReferenceService _vimObjectReferenceService;

   public VimObjectReferenceService get_vimObjectReferenceService() {
      return _vimObjectReferenceService;
   }

   public void set_vimObjectReferenceService(VimObjectReferenceService _userSessionService) {
      this._vimObjectReferenceService = _userSessionService;
   }

   private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);
   /* (non-Javadoc)
    * @see com.dmeplugin.dmestore.EchoService#echo(java.lang.String)
    */
   public String echo(String message) {
      try {
         VmwareContext[] vmwareContextList=VmwarePluginContextFactory.getAllContext(_userSessionService,_vimObjectReferenceService);
         for (VmwareContext vmwareContext:vmwareContextList){
            RootFsMO rootFsMO=new RootFsMO(vmwareContext,vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hostMOList=rootFsMO.getAllHostOnRootFs();
            for (Pair<ManagedObjectReference, String> host:hostMOList){
               _logger.info("host="+host.first().getValue());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return message;
   }
}
