import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';
import {ClrForm} from "@clr/angular";

@Component({
  selector: 'app-rdm',
  templateUrl: './rdm.component.html',
  styleUrls: ['./rdm.component.scss'],
  providers: [CommonService]
})
export class RdmComponent implements OnInit {

  @ViewChild(ClrForm, {static: true}) rdmFormGroup;
  @ViewChild('rdmForm', {static: true}) rdmForm;


  policyEnable = {
    smartTier: false,
    qosPolicy: false,
    resourceTuning: false
  };

  configModel = new customize_volumes();
  storageDevices = [];
  storagePools = [];
  hostList = [];
  hostSelected = '';
  dataStoreName = '';
  levelCheck = 'level';
  dataStores = [];

  searchName = '';
  serviceLevelsRes = [];
  serviceLevels = [];
  service_level_id = '';

  diskNum = 1;

  vmObjectId = '';

  //qos 框控制
  options1 = null;
  options2 = null;
  options3 = null;
  options4 = null;
  options5= null;

  qos1Show = false;
  qos2Show = false;
  qos3Show = false;
  qos4Show = false;
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    this.loadStorageDevice();
    this.loadHosts();
    this.tierFresh();
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    if(ctx != null){
      this.vmObjectId = ctx[0].id;
    } else{
      this.vmObjectId = 'urn:vmomi:VirtualMachine:vm-1046:674908e5-ab21-4079-9cb1-596358ee5dd1';
    }
  }

  // 刷新服务等级列表
  tierFresh(){
    this.gs.loading = true;
    this.http.post('servicelevel/listservicelevel', {}).subscribe((response: any) => {
      this.gs.loading = false;
      if(response.code == '200'){
        this.serviceLevelsRes = this.recursiveNullDelete(response.data);
        for (const i of this.serviceLevelsRes){
          if (i.totalCapacity == 0){
            i.usedRate = 0.0;
          } else {
            i.usedRate =  ((i.usedCapacity / i.totalCapacity * 100).toFixed(2));
          }
          i.usedCapacity = (i.usedCapacity/1024).toFixed(2);
          i.totalCapacity = (i.totalCapacity/1024).toFixed(2);
          i.freeCapacity = (i.freeCapacity/1024).toFixed(2);

          if(!i.capabilities){
            i.capabilities = {};
          }
        }
        this.search();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  serviceLevelClick(id: string, name: string){
     this.service_level_id = id;
  }

  // 服务等级列表搜索
  search(){
    if (this.searchName !== ''){
      this.serviceLevels = this.serviceLevelsRes.filter(item => item.name.indexOf(this.searchName) > -1);
    } else{
      this.serviceLevels = this.serviceLevelsRes;
    }
  }

  changeQosRedio(){
    this.qos1Show = false;
    this.qos2Show = false;
    this.qos3Show = false;
    this.qos4Show = false;
  }

  changeQosInput(type: string){
    const c = this.configModel.tuning.smartqos;
    if(c.control_policy == '1'){
      if(type == 'box'){
        this.qos1Show = (!this.options1 && !this.options2);
        return this.qos1Show;
      }
      if(type == 'band'){
        this.qos2Show = this.options1 && (c.maxbandwidth == '' || c.maxbandwidth == null);
        return this.qos2Show;
      }
      if(type == 'iops'){
        this.qos3Show = this.options2 && (c.maxiops == '' || c.maxiops == null);
        return this.qos3Show;
      }
    }
    if(this.configModel.tuning.smartqos.control_policy == '0'){
      if(type == 'box'){
        this.qos1Show = (!this.options3 && !this.options4 && !this.options5);
        return this.qos1Show;
      }
      if(type == 'band') {
        this.qos2Show = this.options3 && (c.minbandwidth == '' || c.minbandwidth == null);
        return this.qos2Show;
      }
      if(type == 'iops'){
        this.qos3Show = this.options4 && (c.miniops == '' || c.miniops == null);
        return this.qos3Show;
      }
      if(type == 'latency'){
        this.qos4Show = this.options5 && (c.latency == '' || c.latency == null);
        return this.qos4Show;
      }
    }
  }

  submit(): void {
    if (this.rdmForm.form.invalid) {
      this.rdmFormGroup.markAsTouched();
      return;
    }
    let b = JSON.parse(JSON.stringify(this.configModel));
    let body = {};
    if (this.configModel.storageType == '2'){
      if(!this.policyEnable.smartTier){
        b.tuning.smarttier = null;
      }
      if(!this.policyEnable.qosPolicy){
        b.tuning.smartqos = null;
      } else{
        let box = this.changeQosInput('box');
        let band = this.changeQosInput('band');
        let iops = this.changeQosInput('iops');
        let latency = this.changeQosInput('latency');
        if(box || band || iops || latency){
          return;
        }
        if(this.configModel.tuning.smartqos.control_policy == '1'){
          b.tuning.smartqos.minbandwidth = null;
          b.tuning.smartqos.miniops = null;
        } else{
          b.tuning.smartqos.maxbandwidth = null;
          b.tuning.smartqos.maxiops = null;
        }
      }
      if(!this.policyEnable.resourceTuning){
        b.tuning.alloctype = null;
        b.tuning.dedupe_enabled = null;
        b.tuning.compression_enabled = null;
      }
      if(!this.policyEnable.smartTier && !this.policyEnable.qosPolicy && !this.policyEnable.resourceTuning){
        b.tuning = null;
      }

      body = {
        customizeVolumesRequest: {
          customize_volumes: b,
            mapping: {
            host_id: this.hostSelected
          }
        }
      };
    }
    if (this.configModel.storageType == '1'){
      body = {
        createVolumesRequest: {
          service_level_id: this.service_level_id,
          volumes: this.configModel.volume_specs,
          mapping: {
            host_id: this.hostSelected
          }
        }
      };
    }
    console.log(b);
    this.http.post('v1/vmrdm/createRdm?hostId='+this.hostSelected+'&vmObjectId='+this.vmObjectId+'&dataStoreName='+this.dataStoreName
      , body).subscribe((result: any) => {
    }, err => {
      console.error('ERROR', err);
    });
  }

  basAdd(){
    this.diskNum = this.diskNum + 1;
    const i = this.diskNum;
    this.configModel.volume_specs.push(new volume_specs("name"+i,"unit"+i,"count"+i,"capacity"+i));
  }

  basRemove(item){
    this.configModel.volume_specs = this.configModel.volume_specs.filter((i) => {
        return i != item;
    });
  }

  recursiveNullDelete(obj: any){
    for (const property in obj){
      if (obj[property] === null){
        delete obj[property];
      } else if (obj[property] instanceof Object){
        this.recursiveNullDelete(obj[property]);
      } else if (property == 'minBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'minIOPS' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxIOPS' && obj[property] == 0){
        delete obj[property];
      } else if(property == 'latency' && obj[property] == 0){
        delete obj[property];
      }
    }
    return obj;
  }

  loadStorageDevice(){
    this.gs.loading = true;
    this.http.get('dmestorage/storages', {}).subscribe((result: any) => {
      this.gs.loading = false;
      if (result.code === '200'){
        this.storageDevices = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadStoragePool(storageId: string){
    this.gs.loading = true;
    this.http.get('dmestorage/storagepools', {params: {storageId, media_type: "all"}}).subscribe((result: any) => {
      this.gs.loading = false;
      if (result.code === '200'){
        this.storagePools = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadHosts(){
    this.gs.loading = true;
    this.http.get('v1/vmrdm/dmeHosts').subscribe((result: any) => {
      this.gs.loading = false;
      if (result.code === '200'){
        this.hostList = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadDataStore(id: string){
    this.gs.loading = true;
    this.http.get('v1/vmrdm/vCenter/datastoreOnHost', { params: {hostId : id}}).subscribe((result: any) => {
      this.gs.loading = false;
      if (result.code === '200'){
        this.dataStores = result.data;
      } else{
        this.dataStores = [];
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }, err => {
      console.error('ERROR', err);
    });
  }

}


class customize_volumes{
  storageType: string;
  availability_zone: string;
  initial_distribute_policy: string;
  owner_controller: string;
  pool_raw_id: string;
  prefetch_policy: string;
  prefetch_value: string;
  storage_id: string;
  tuning: any;
  volume_specs: volume_specs[];
  constructor(){
    this.storageType = '1';
    this.volume_specs = [new volume_specs("name1","unit1","count1", "capacity1")];
    this.tuning = new tuning();
    this.initial_distribute_policy = '0';
    this.owner_controller = '0';
    this.prefetch_policy = '3';
  }
}

class volume_specs{
  capacity: number;
  count: number;
  name: string;
  unit: string;
  start_lun_id: number;
  start_suffix: number;
  inputName: string;
  inputUnit: string;
  inputCount: string;
  inputCapacity: string;
  constructor(inputName, inputUnit, inputCount, inputCapacity){
     this.inputCapacity = inputCapacity;
     this.inputCount = inputCount;
     this.inputName = inputName;
     this.inputUnit = inputUnit;

     this.name = '';
     this.capacity = null;
     this.count = 1;
     this.unit = 'GB';
  }
}


class tuning{
  alloctype:string;
  compression_enabled: boolean;
  dedupe_enabled: boolean;
  smartqos: smartqos;
  smarttier: string;
  workload_type_id: string;
  constructor(){
    this.smartqos = new smartqos();
    this.alloctype = 'thick';
    this.smarttier = '0';
    this.compression_enabled = null;
    this.dedupe_enabled = null;
  }
}

class smartqos{
  control_policy: string;
  latency: number;
  maxbandwidth: number;
  maxiops: number;
  minbandwidth: number;
  miniops: number;
  name:string;
  constructor(){
    this.control_policy = '1'
  }
}

class mapping{
  auto_zoning: boolean;
  host_id: string;
  hostgroup_id: string;
  constructor(){
  }
}
