import { URLS_LIST_SERVICE } from '../app/routes/vmfs/list/list.service';
import ACCESSVMFS_LISTVMFS from "./URLS_LIST_SERVICE/ACCESSVMFS_LISTVMFS";
import ACCESSVMFS_LISTVMFSPERFORMANCE from "./URLS_LIST_SERVICE/ACCESSVMFS_LISTVMFSPERFORMANCE";

import { URLS_NFS } from './../app/routes/nfs/nfs.service';
import ACCESSNFS_LISTNFS from "./URLS_NFS/ACCESSNFS_LISTNFS"

import { URLS_STORAGE } from './../app/routes/storage/storage.service';
import DMESTORAGE_STORAGES from "./URLS_STORAGE/DMESTORAGE_STORAGES"

import ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID from "./ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID"
import DMESTORAGE_GETSTORAGEETHPORTS from "./DMESTORAGE_GETSTORAGEETHPORTS"
import SERVICELEVEL_LISTSERVICELEVEL from "./SERVICELEVEL_LISTSERVICELEVEL"
import ACCESSVMWARE_LISTHOST from "./ACCESSVMWARE_LISTHOST"
import ACCESSVMWARE_LISTCLUSTER from "./ACCESSVMWARE_LISTCLUSTER"
import UI_TREE_CHILDREN_BY_OBJECT_IDS from "./UI_TREE_CHILDREN_BY_OBJECT_IDS"
import DMESTORAGE_LOGICPORTS from "./DMESTORAGE_LOGICPORTS"

export const mockData = {
  [URLS_LIST_SERVICE.ACCESSVMFS_LISTVMFSPERFORMANCE]: ACCESSVMFS_LISTVMFSPERFORMANCE,
  [URLS_LIST_SERVICE.ACCESSVMFS_LISTVMFS]: ACCESSVMFS_LISTVMFS,
  [URLS_NFS.ACCESSNFS_LISTNFS]: ACCESSNFS_LISTNFS,
  [URLS_STORAGE.DMESTORAGE_STORAGES]: DMESTORAGE_STORAGES,
  ACCESSNFS_LISTNFS: ACCESSNFS_LISTNFS,
  ACCESSVMFS_LISTVMFS: ACCESSVMFS_LISTVMFS,
  DMESTORAGE_STORAGES: DMESTORAGE_STORAGES,
  // iscsi
  ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID: ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID,
  DMESTORAGE_GETSTORAGEETHPORTS: DMESTORAGE_GETSTORAGEETHPORTS,
  /* v6接口 */
  DMESTORAGE_LOGICPORTS,
  /* servicelevel/listservicelevel */
  SERVICELEVEL_LISTSERVICELEVEL,
  /* accessvmware/listhost */
  ACCESSVMWARE_LISTHOST,
  /* accessvmware/listcluster */
  ACCESSVMWARE_LISTCLUSTER,
  /* ui/tree/childrenByObjectIds */
  UI_TREE_CHILDREN_BY_OBJECT_IDS
}

export const isMockData = false;



// export const mockData = {};