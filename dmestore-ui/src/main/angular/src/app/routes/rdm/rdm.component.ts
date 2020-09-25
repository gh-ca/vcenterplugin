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


  popShow = true;
  configModel = {
    name: '',
    size: '',
    unit: '1',
    storage_id: '',
    pool_raw_id: '',
    storageType: '1',
    quantity: '',
  };
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
