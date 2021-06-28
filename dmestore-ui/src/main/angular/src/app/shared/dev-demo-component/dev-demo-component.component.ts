import { Component, OnInit } from '@angular/core';
import {
  GhDropdown,
  GhFormItemBase,
  GhTextbox,
} from '@shared/gh-dynamic-form-item/gh-dynamic-form-item.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomValidatorFaild, helper } from '../../app.helpers';
import { regExpCollection } from 'app/app.helpers';
import { vmfsClusterTreeData } from './../../../mock/vmfsClusterTree';
import { mockServerData } from './../../app.helpers';

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
  vmfsTree;
  vmfsList;
  partSuccessData;
  serviceItem;
  firstData;
  secondData;
  thirdData;
  buttonDisableOrNot:boolean=true;

  constructor() {
    this.initForm();

    this.serviceItem = {
      "capabilities": {
        "resourceType": null,
        "compression": null,
        "deduplication": null,
        "smarttier": null,
        "iopriority": null,
        "qos": {
          "smartQos": null,
          "qosParam": {
            "enabled": null,
            "latency": 0,
            "latencyUnit": "ms",
            "minBandWidth": 0,
            "minIOPS": 0,
            "maxBandWidth": 200,
            "maxIOPS": 500,
            "smartQos": null
          },
          "enabled": true
        }
      },
      "id": "292ae048-486d-4b1b-822b-0c84a99be342",
      "name": "zg_610",
      "description": "block service-level for dj",
      "type": "BLOCK",
      "protocol": null,
      "totalCapacity": 2208768.0,
      "freeCapacity": 2064064.0,
      "usedCapacity": 144704.0
    };

    (async () => {
      this.vmfsTree = await mockServerData(vmfsClusterTreeData);
      this.vmfsList = await mockServerData(vmfsClusterTreeData,1000);
    })();
  }
  // "10.12.123.1","10.23.234.1"
  initForm() {
    //模拟返回数据
    this.partSuccessData={
      code:"206",
      data:{
        successNo:2,
        failNo:1,
        connectionResult:["10.12.123.1","10.23.234.1"],
        // descriptionEN:"loream....",
        // descriptionCN:"这里有一段内容",
        partialSuccess:1,
      },
      description: "asdsadasdasdsa"
    }
    /*初始化和校验规则*/
    this.form = new FormGroup({
      brave: new FormControl('', Validators.required),
      tree: new FormControl('', Validators.required),
      firstName: new FormControl('', Validators.required),
      emailAddress: new FormControl('', [
        Validators.required,
        CustomValidatorFaild(value => regExpCollection.integer().test(value)),
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

  handleTreeValueChange(prop, val) {
    this.form.patchValue({ [prop]: val });
  }

  ngOnInit(): void {
  }
  // ngDoCheck() {
  //   this.changeButtonAttr()
  // }

  changeButtonAttr(){

    if(this.firstData&&this.secondData&&this.thirdData){
      return false
    }else {
      return true
    }
  }
}


