import { URLS_LIST_SERVICE } from '../app/routes/vmfs/list/list.service';
import ACCESSVMFS_LISTVMFS from './URLS_LIST_SERVICE/ACCESSVMFS_LISTVMFS';
import ACCESSVMFS_LISTVMFSPERFORMANCE from './URLS_LIST_SERVICE/ACCESSVMFS_LISTVMFSPERFORMANCE';

import { URLS_NFS } from './../app/routes/nfs/nfs.service';
import ACCESSNFS_LISTNFS from './URLS_NFS/ACCESSNFS_LISTNFS';

import { URLS_STORAGE } from './../app/routes/storage/storage.service';
import DMESTORAGE_STORAGES from './URLS_STORAGE/DMESTORAGE_STORAGES';

import ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID from './ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID';
import DMESTORAGE_GETSTORAGEETHPORTS from './DMESTORAGE_GETSTORAGEETHPORTS';
import SERVICELEVEL_LISTSERVICELEVEL from './SERVICELEVEL_LISTSERVICELEVEL';
import ACCESSVMWARE_LISTHOST from './ACCESSVMWARE_LISTHOST';
import ACCESSVMWARE_LISTCLUSTER from './ACCESSVMWARE_LISTCLUSTER';
import UI_TREE_CHILDREN_BY_OBJECT_IDS from './UI_TREE_CHILDREN_BY_OBJECT_IDS';
import DMESTORAGE_LOGICPORTS from './DMESTORAGE_LOGICPORTS';
import ACCESSVMFS_GETHOSTGROUPSBYSTORAGEID from './ACCESSVMFS_GETHOSTGROUPSBYSTORAGEID';
import ACCESSVMFS_GETHOSTSBYSTORAGEID from './ACCESSVMFS_GETHOSTSBYSTORAGEID';
import BESTPRACTICE_VIRTUAL_NIC from './BESTPRACTICE_VIRTUAL_NIC';
import BESTPRACTICE_RECORDS_ALL from './BESTPRACTICE_RECORDS_ALL';
import DMESTORAGE_STORAGE from './DMESTORAGE_STORAGE';
import ACCESSVMFS_QUERYVMFS_DATA_STORE_OBJECT_ID from './ACCESSVMFS_QUERYVMFS_DATA_STORE_OBJECT_ID';
import DMESTORAGE_STORAGESV6 from './DMESTORAGE_STORAGES';
import NFS_ACCESSVMWARE_LISTHOST from './NFS_ACCESSVMWARE_LISTHOST';
import NFS_DMESTORAGE_LOGICPORTS from './NFS_DMESTORAGE_LOGICPORTS';
import NFS_ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID from './NFS_ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID';

import VMFS_DMESTORAGE_STORAGE from './VMFS_DMESTORAGE_STORAGE';
import VMFS_ACCESSVMFS_QUERYVMFS from './VMFS_ACCESSVMFS_QUERYVMFS';

export const isMockData = false;
export const mockData = {
  [URLS_LIST_SERVICE.ACCESSVMFS_LISTVMFSPERFORMANCE]: ACCESSVMFS_LISTVMFSPERFORMANCE,
  [URLS_LIST_SERVICE.ACCESSVMFS_LISTVMFS]: ACCESSVMFS_LISTVMFS,
  [URLS_NFS.ACCESSNFS_LISTNFS]: ACCESSNFS_LISTNFS,
  [URLS_STORAGE.DMESTORAGE_STORAGES]: DMESTORAGE_STORAGES,
  ACCESSNFS_LISTNFS: ACCESSNFS_LISTNFS,
  /* vmfs list */
  ACCESSVMFS_LISTVMFS,
  DMESTORAGE_STORAGES,
  // iscsi
  ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID: ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID,
  DMESTORAGE_GETSTORAGEETHPORTS: DMESTORAGE_GETSTORAGEETHPORTS,
  /* v6接口 */
  DMESTORAGE_LOGICPORTS,
  /* DME存储策略 */
  SERVICELEVEL_LISTSERVICELEVEL,
  /* accessvmware/listhost */
  ACCESSVMWARE_LISTHOST,
  /* accessvmware/listcluster */
  ACCESSVMWARE_LISTCLUSTER,
  /* ui/tree/childrenByObjectIds */
  UI_TREE_CHILDREN_BY_OBJECT_IDS,
  /* vmfs 卸载 集群 */
  ACCESSVMFS_GETHOSTGROUPSBYSTORAGEID,
  /*  */
  ACCESSVMFS_GETHOSTSBYSTORAGEID,
  /* MTU查询增加设备IP返回 f1f6d63 */
  BESTPRACTICE_VIRTUAL_NIC,
  /* 最佳实践 all  针对MTU可以做修改 MTU的panel内容与其他的不一样*/
  BESTPRACTICE_RECORDS_ALL,
  /* vmfs 编辑 */
  DMESTORAGE_STORAGE,
  /* accessvmfs/queryvmfs?dataStoreObjectId */
  ACCESSVMFS_QUERYVMFS_DATA_STORE_OBJECT_ID,
  /* dmestorage/storages */
  DMESTORAGE_STORAGESV6,
  /* nfs /accessvmware/listhost */
  NFS_ACCESSVMWARE_LISTHOST,
  /* nfs dmestorage/logicports */
  NFS_DMESTORAGE_LOGICPORTS,
  /* nfs accessvmware/getvmkernelipbyhostobjectid  虚拟网卡IP*/
  NFS_ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID,
  /* vmfs  dmestorage/storage*/
  VMFS_DMESTORAGE_STORAGE,
  /* vmfs accessvmfs/queryvmfs */
  VMFS_ACCESSVMFS_QUERYVMFS
};
