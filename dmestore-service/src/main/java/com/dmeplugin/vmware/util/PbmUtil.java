/*
 * *****************************************************
 * Copyright VMware, Inc. 2010-2012.  All Rights Reserved.
 * *****************************************************
 *
 * DISCLAIMER. THIS PROGRAM IS PROVIDED TO YOU "AS IS" WITHOUT
 * WARRANTIES OR CONDITIONS # OF ANY KIND, WHETHER ORAL OR WRITTEN,
 * EXPRESS OR IMPLIED. THE AUTHOR SPECIFICALLY # DISCLAIMS ANY IMPLIED
 * WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY # QUALITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.dmeplugin.vmware.util;

import com.vmware.pbm.*;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

/**
 * Utility class for PBM Samples
 * 
 */
public class PbmUtil {
   /**
    * Returns the Capability Metadata for the specified capability name
    * 
    * @param capabilityName
    * @param schema
    * @return
    */
   public static PbmCapabilityMetadata getCapabilityMeta(String capabilityName,
         List<PbmCapabilityMetadataPerCategory> schema) {
      for (PbmCapabilityMetadataPerCategory cat : schema) {
         for (PbmCapabilityMetadata cap : cat.getCapabilityMetadata()) {
            if (cap.getId().getId().equals(capabilityName)) {
               return cap;
            }
         }
      }
      return null;
   }

   /**
    * This method returns the Profile Spec for the given Storage Profile name
    * 
    * @return
    * @throws InvalidArgumentFaultMsg
    * @throws RuntimeFaultFaultMsg
    * @throws RuntimeFaultFaultMsg
    */
   public static PbmCapabilityProfile getPbmProfile(PbmServiceInstanceContent spbmsc, PbmPortType pbmPortType,
                                                    String name) throws InvalidArgumentFaultMsg,
         RuntimeFaultFaultMsg, RuntimeFaultFaultMsg {
      ManagedObjectReference profileMgr = spbmsc.getProfileManager();
      List<PbmProfileId> profileIds =
           pbmPortType.pbmQueryProfile(profileMgr,
                  PbmUtil.getStorageResourceType(), null);

      if (profileIds == null || profileIds.isEmpty()) {
         throw new RuntimeFaultFaultMsg("No storage Profiles exist.", null);
      }
      List<PbmProfile> pbmProfiles =
            pbmPortType.pbmRetrieveContent(profileMgr, profileIds);
      for (PbmProfile pbmProfile : pbmProfiles) {
         if (pbmProfile.getName().equals(name)) {
            PbmCapabilityProfile profile = (PbmCapabilityProfile) pbmProfile;
            return profile;
         }
      }
      throw new RuntimeFaultFaultMsg("Profile with the given name does not exist", null);
   }

   /**
    * Returns the Storage Resource Type Object
    * 
    * @return
    */
   public static PbmProfileResourceType getStorageResourceType() {
      PbmProfileResourceType resourceType = new PbmProfileResourceType();
      resourceType.setResourceType(PbmProfileResourceTypeEnum.STORAGE.value());
      return resourceType;
   }

   /**
    * Returns the Capability Metadata associated to a Tag Category
    * 
    * @param tagCategoryName
    * @param schema
    * @return
    */
   public static PbmCapabilityMetadata getTagCategoryMeta(
         String tagCategoryName, List<PbmCapabilityMetadataPerCategory> schema) {
      for (PbmCapabilityMetadataPerCategory cat : schema) {
         if ("tag".equals(cat.getSubCategory())) {
            for (PbmCapabilityMetadata cap : cat.getCapabilityMetadata()) {
               if (cap.getId().getId().equals(tagCategoryName)) {
                  return cap;
               }
            }
         }
      }
      return null;
   }
}
