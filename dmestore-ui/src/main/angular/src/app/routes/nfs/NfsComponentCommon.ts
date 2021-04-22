import { AddNfs } from './subpages/add/nfs-add.service';

export class NfsComponentCommon {
  addForm;
  isAddPageOneNextDisabled;
  cdr;
  addFormGroup;

  /**
   * @Description 创建并监听表单数据变动，检测数据符合校验规则
   * @date 2021-04-20
   * @returns {any}
   */
  createAddFormAndWatchFormChange() {
    this.addForm = new AddNfs();

    /* 监听名字变化 根据是否一样给剩下两个赋值 */
    this.addFormGroup.valueChanges.subscribe(addForm => {
      if (addForm.sameName) {
        const isSameShare = this.addForm.shareName === addForm.nfsName;
        const isSameFs = (this.addForm.fsName = addForm.nfsName);
        if (!(isSameShare && isSameFs)) {
          this.addForm.fsName = this.addForm.shareName = addForm.nfsName;
        }
      }
      this.checkAddForm();
    });
  }

  checkAddForm() {
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
  }
}
