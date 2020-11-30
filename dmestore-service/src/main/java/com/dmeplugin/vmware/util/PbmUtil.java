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
