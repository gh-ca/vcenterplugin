import { FormControl, FormGroup, Validators } from '@angular/forms';
import debounce from 'just-debounce';
import { CustomValidatorFaild, helper } from 'app/app.helpers';
import { getLodash } from '../../../shared/lib';

const _ = getLodash();

export class VmfsCommon {
  addForm;
  isPageNextDisabled = true;
  cdr;
  form;
  modifyFormGroup;
  helper = helper;

  deviceList;
  deviceList_list;
  chooseDevice;

  fnCheckNameExist;

  constructor() {
    this.createAddFormAndWatchFormChange();
    (this.checkPageOne as any) = debounce(this.checkPageOne.bind(this), 300);
    this.fnCheckNameExist = debounce(() => {
      try {
        /* name 数据存储 */
        (this as any).nameCheck(true);
        /* volumeName 卷 */
        (this as any).nameCheck(false);
      } catch (error) {}
    }, 500);
  }

  handlerValueChanges(addForm) {
    this.fnCheckNameExist();
    this.checkPageOne();
  }

  /**
   *
   * 添加页面名称相同按钮点击事件
   */
  addSameBtnChangeFunc(obj) {
    if (this.form.isSameName) {
      this.form.volumeName = this.form.name;
      this.checkPageOne();
    }
  }

  handleChooseDeviceChange(form: FormGroup, prop: string, val: any) {
    form.patchValue({ [prop]: val });
  }

  isDisableMountSubmit() {
    return this.addForm?.value?.chooseDevice?.length===0
  }

  handleChooseDeviceChangePatchToValue(prop: string, val: any) {
    this[prop] = val;
  }

  checkPageOne() {
    if (this.addForm.value.isSameName) {
      this.form.name = this.form.volumeName = this.addForm.value.name;
    } else {
      this.form.name = this.addForm.value.name;
      this.form.volumeName = this.addForm.value.volumeName;
    }

    this.form.version = this.addForm.value.version;
    this.form.capacity = this.addForm.value.capacity;
    this.form.count = this.addForm.value.count;
    this.form.blockSize = this.addForm.value.blockSize;
    this.form.spaceReclamationGranularity = this.addForm.value.spaceReclamationGranularity;
    this.form.spaceReclamationPriority = this.addForm.value.spaceReclamationPriority;
    this.form.chooseDevice = this.addForm.value.chooseDevice;

    try {
      /* 数量 */
      /* console.log("countBlur"); */
      (this as any).countBlur();
    } catch (error) {}
    const isname = !!this.form.name;
    const isvolumeName = !!this.form.volumeName;
    const isversion = !!this.form.version;
    const iscapacity = this.form.capacity > 0;
    const iscount = this.form.count > 0;
    const isblockSize = !!this.form.blockSize;
    const isspaceReclamationGranularity = !!this.form.spaceReclamationGranularity;
    const isspaceReclamationPriority = !!this.form.spaceReclamationPriority;
    const ischooseDevice = _.isArray(this.form.chooseDevice) && this.form.chooseDevice.length > 0;
    /* console.log( 'isname', isname, 'isvolumeName', isvolumeName, 'isversion', isversion, 'iscapacity', iscapacity, 'iscount', iscount, 'isblockSize', isblockSize, 'isspaceReclamationGranularity', isspaceReclamationGranularity, 'isspaceReclamationPriority', isspaceReclamationPriority, 'ischooseDevice', ischooseDevice ); */
    this.isPageNextDisabled = !(
      isname &&
      isvolumeName &&
      isversion &&
      iscapacity &&
      iscount &&
      isblockSize &&
      isspaceReclamationGranularity &&
      isspaceReclamationPriority &&
      ischooseDevice
    );
    this.cdr.detectChanges();
    return true;
  }

  createAddFormAndWatchFormChange() {
    this.addForm = new FormGroup({
      name: new FormControl('', [Validators.required]),
      isSameName: new FormControl(true, Validators.required),
      volumeName: new FormControl('', [Validators.required]),
      version: new FormControl('5', Validators.required),
      capacity: new FormControl('', Validators.required),
      capacityUnit: new FormControl('GB', Validators.required),
      count: new FormControl('', Validators.required),
      blockSize: new FormControl('', Validators.required),
      spaceReclamationGranularity: new FormControl('', Validators.required),
      spaceReclamationPriority: new FormControl('', Validators.required),
      chooseDevice: new FormControl(
        [],
        CustomValidatorFaild(value => {
          const isValid = _.isArray(value) && value.length > 0;
          return !isValid;
        })
      ),
    });
  }

  async setDeviceList_new(instance, fn: any = false) {
    // 初始化数据
    this.deviceList = [];
    this.deviceList_list = [];
    this.chooseDevice = [];
    this.deviceList = await instance?.commonService?.remoteGetVmfsDeviceList();
    fn && (fn as any)();
  }
}
