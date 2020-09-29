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
  hostSelected = {};
  levelCheck = 'storage';
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
    console.log(this.mapping);
    this.http.post('', {}).subscribe((result: any) => {

      if (result.code === '0' || result.code === '200'){
        this.storageDevices = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
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
    this.http.get('dmestorage/storagepools', {params: {storageId}}).subscribe((result: any) => {

      if (result.code === '0' || result.code === '200'){
        this.storagePools = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadHosts(){
    this.http.get('accessvmware/listhost').subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.hostList = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
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
  compression_enabled: string;
  dedupe_enabled: string;
  smartqos: smartqos;
  smarttier: string;
  workload_type_id: string;
  constructor(){
    this.smartqos = new smartqos();
    this.alloctype = 'thick';
    this.smarttier = '0';
    this.compression_enabled = '0';
    this.dedupe_enabled = '0';
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
