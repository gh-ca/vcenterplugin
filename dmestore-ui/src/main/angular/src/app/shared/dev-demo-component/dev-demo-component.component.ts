import { Component, OnInit } from '@angular/core';
import {
  GhDropdown,
  GhFormItemBase,
  GhTextbox,
} from '@shared/gh-dynamic-form-item/gh-dynamic-form-item.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomValidatorFaild, helper } from '../../app.helpers';
import { regExpCollection } from 'app/app.helpers';

@Component({
  selector: 'app-dev-demo-component',
  templateUrl: './dev-demo-component.component.html',
  styleUrls: ['./dev-demo-component.component.scss'],
})
export class DevDemoComponentComponent implements OnInit {
  formItemInfos: GhFormItemBase<string>[];
  form: FormGroup;
  value;
  helper = helper;

  constructor() {
    this.initForm();
  }

  initForm() {
    /*初始化和校验规则*/
    this.form = new FormGroup({
      brave: new FormControl('', Validators.required),
      firstName: new FormControl('', Validators.required),
      emailAddress: new FormControl('', [
        Validators.required,
        CustomValidatorFaild(value => {
          const res = regExpCollection.integer().test(value);
          console.log('🚀DevDemoComponentComponent ', value, res);
          return res;
        }),
      ]),
    });

    this.formItemInfos = [
      new GhDropdown({
        prop: 'brave',
        label: 'Bravery Rating',
        options: [
          { value: 'solid', label: 'Solid' },
          { value: 'great', label: 'Great' },
          { value: 'good', label: 'Good' },
          { value: 'unproven', label: 'Unproven' },
        ],
        order: 3,
        validTips_i18n: ['validations.required_vmfs_name', { length: 27 }],
      }),
      new GhTextbox({
        prop: 'firstName',
        label: 'First name',
        value: 'Bombasto',
        required: true,
        order: 1,
        validTips: '字符串直接显示，数组会利用翻译器',
      }),
      new GhTextbox({
        prop: 'emailAddress',
        label: 'Email',
        type: 'email',
        order: 2,
        validTips_i18n: ['validations.required_vmfs_name', { length: 27 }],
      }),
    ];
    /*  
      let firstName = 0;
      setInterval(() => {
        this.form.patchValue({firstName: ++firstName});
        this.formItemInfos[0] = new GhDropdown({
          prop: 'brave',
          label: 'Bravery Rating',
          options: [
            {value: 'solid', label: ++firstName + ''},
            {value: 'great', label: firstName + ''},
            {value: 'good', label: 'Good'},
            {value: 'unproven', label: 'Unproven'}
          ],
          order: 3,
        });
      }, 1000 * 4);
    */
  }

  handlePayLoadChange(value) {
    this.value = value;
  }

  ngOnInit(): void {}
}
