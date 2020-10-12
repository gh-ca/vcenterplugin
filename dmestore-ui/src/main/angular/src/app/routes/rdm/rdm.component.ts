import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';

@Component({
  selector: 'app-rdm',
  templateUrl: './rdm.component.html',
  styleUrls: ['./rdm.component.scss'],
  providers: [CommonService, GlobalsService]
})
export class RdmComponent implements OnInit {

  policyEnable = {
    smartTier: false,
    qosPolicy: false,
    resourceTuning: false
  };

  popShow = true;
  configModel = new customize_volumes();
  mapping = new mapping();
  storageDevices = [];
  storagePools = [];
  hostList = [];
  hostSelected = '';
  data_store_name = '';
  levelCheck = 'storage';
  dataStores = [];
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    this.loadStorageDevice();
    this.loadHosts();
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
  }

  submit(): void {
    console.log(this.configModel);
    const vm_objectId = 'vm_object_id:urn:vmomi:VirtualMachine:vm-229:f8e381d7-074b-4fa9-9962-9a68ab6106e1';
    this.http.post('v1/vmrdm/createRdm?host_id='+this.hostSelected+'&vm_objectId='+vm_objectId+'&data_store_name='+this.data_store_name
      , {
        customizeVolumesRequest: {
          customize_volumes: this.configModel,
          mapping: {
            host_id: this.hostSelected
          }
        }
      }).subscribe((result: any) => {
      console.log(result);
    }, err => {
      console.error('ERROR', err);
    });
  }

  basAdd(){
    this.configModel.volume_specs.push(new volume_specs());
    console.log(this.configModel.volume_specs);
  }

  basRemove(item){
    this.configModel.volume_specs = this.configModel.volume_specs.filter((i) => {
        return i != item;
    });
  }

  check() {}

  loadStorageDevice(){
    this.http.get('dmestorage/storages', {}).subscribe((result: any) => {

      if (result.code === '0' || result.code === '200'){
        this.storageDevices = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadStoragePool(storageId: string){
    this.http.get('dmestorage/storagepools', {params: {storageId, media_type: "all"}}).subscribe((result: any) => {

      if (result.code === '0' || result.code === '200'){
        this.storagePools = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadHosts(){
    this.http.get('v1/vmrdm/dmeHosts').subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.hostList = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadDataStore(id: string){
    this.http.get('v1/vmrdm/vCenter/datastoreOnHost', { params: {host_id : id}}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
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
  tuning: tuning;
  volume_specs: volume_specs[];
  constructor(){
    this.storageType = '2';
    this.volume_specs = [new volume_specs()];
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
  constructor(){
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
