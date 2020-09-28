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
  storageDevices = [];
  storagePools = [];
  levelCheck = 'level';
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    this.loadStorageDevice();
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
  }

  submit(): void {

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
      console.log(result);
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
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.storagePools = result.data.data;
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
    this.volume_specs = [new volume_specs()];
    this.tuning = new tuning();
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

  }
}
