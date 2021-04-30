import { FormControl, FormGroup, Validators } from '@angular/forms';
import debounce from 'just-debounce';

export class VmfsCommon {
  addForm;
  isPageNextDisabled = true;
  cdr;
  form;
  modifyFormGroup;

  constructor() {
    this.createAddFormAndWatchFormChange();
    this.checkPageOne = debounce(this.checkPageOne.bind(this), 300);
  }

  handlerValueChanges(addForm) {
    try {
      /* name 数据存储 */
      (this as any).nameChaeck(true);
      /* volumeName 卷 */
      (this as any).nameChaeck(false);
    } catch (error) {}
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

    const isname = !!this.form.name;
    const isvolumeName = !!this.form.volumeName;
    const isversion = !!this.form.version;
    const iscapacity = this.form.capacity > 0;
    const iscount = this.form.count > 0;
    const isblockSize = !!this.form.blockSize;
    const isspaceReclamationGranularity = !!this.form.spaceReclamationGranularity;
    const isspaceReclamationPriority = !!this.form.spaceReclamationPriority;
    const ischooseDevice = !!this.form.chooseDevice;

    console.log(
      'isname',
      isname,
      'isvolumeName',
      isvolumeName,
      'isversion',
      isversion,
      'iscapacity',
      iscapacity,
      'iscount',
      iscount,
      'isblockSize',
      isblockSize,
      'isspaceReclamationGranularity',
      isspaceReclamationGranularity,
      'isspaceReclamationPriority',
      isspaceReclamationPriority,
      'ischooseDevice',
      ischooseDevice
    );

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
      chooseDevice: new FormControl('', Validators.required),
    });
  }
}
