export class NfsComponentCommon {
  addForm;
  isAddPageOneNextDisabled;
  cdr;
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
