import { regExpCollection } from 'app/app.helpers';
import { AddNfs } from './subpages/add/nfs-add.service';
import debounce from 'just-debounce';

export class NfsComponentCommon {
  addForm;
  isAddPageOneNextDisabled;
  cdr;
  addFormGroup;

  /*  */
  latencyErrTips;
  bandwidthLimitErr;
  iopsLimitErr;
  matchErr;

  dorado;

  handlerAddFormGroupValueChange;

  constructor() {
    var vm = this;
    // this.handlerAddFormGroupValueChange = debounce(() => { vm.checkAddForm(); }, 300);
  }

  /**
   * @Description 创建并监听表单数据变动，检测数据符合校验规则
   * @date 2021-04-20
   * @returns {any}
   */

  createAddFormAndWatchFormChange() {
    this.addForm = new AddNfs();
    /* 监听名字变化 根据是否一样给剩下两个赋值 */

    this.addFormGroup.valueChanges.subscribe(this.handlerAddFormGroupValueChange);
  }

  setShareName(val) {
    /* 如果是v6默认是符合校验规则的固定值 */
    if (this.dorado) {
      if (this.addForm.shareName === '000_dorado') {
        return;
      } else {
        this.addForm.shareName = '000_dorado';
        return;
      }
    } else {
      /* 不是v6 如果是默认值则置空 */
      if (this.addForm.shareName === '000_dorado') {
        this.addForm.shareName = null;
        return;
      }
      /* setShareName 有值则赋值 ，如果是boolean则是定时检查 */
      if (typeof val !== 'boolean') {
        this.addForm.shareName = val;
      }
    }
  }

  checkAddForm(prop = ''): boolean {
    this.setShareName(false);
    (this as any).checkNfsName();
    (this as any).checkFsName();
    (this as any).checkShareName();

    const isStoragId = !!this.addForm.storagId;
    const isStoragePoolId = !!this.addForm.storagePoolId;
    const isCurrentPortId = !!this.addForm.currentPortId;
    const isNfsName = !!this.addForm.nfsName;
    const isSize = typeof this.addForm.size === 'number';
    const isHostObjectId = !!this.addForm.hostObjectId;
    const isVkernelIp = !!this.addForm.vkernelIp;
    const isAccessMode = !!this.addForm.accessMode;
    const isFsName = !!this.addForm.fsName;
    const isShareName = !!this.addForm.shareName;
    this.isAddPageOneNextDisabled = !(
      isStoragId &&
      isStoragePoolId &&
      isCurrentPortId &&
      isNfsName &&
      isSize &&
      isHostObjectId &&
      isVkernelIp &&
      isAccessMode &&
      isFsName &&
      isShareName
    );
    this.cdr.detectChanges();
    return true;
  }

  isDisableFormPageTwo(formPge) {
    if (!formPge.valid) {
      return true;
    }

    if (this.latencyErrTips) {
      return true;
    }
    if (this.bandwidthLimitErr) {
      return true;
    }
    if (this.iopsLimitErr) {
      return true;
    }
    return false;
  }
}
