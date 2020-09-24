import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';

@Component({
  selector: 'app-iscsi',
  templateUrl: './iscsi.component.html',
  styleUrls: ['./iscsi.component.scss'],
  providers: [CommonService, GlobalsService]
})
export class IscsiComponent implements OnInit {


  popShow = true;
  configModel = {
    hostId: '',
    hostIp: '',
    storageDeviceId: ''
  };
  // 存储下拉框数据
  storageDevices = [];

  portSelected = [];
  portLoading = false;
  portList = [];
  portTotal = 0;
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    this.loadStorageDevice();
  }

  loadStorageDevice(){
    this.http.get('http://localhost:8080/dmestorage/storages', {}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.storageDevices = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadPorts(){
    this.portLoading = true;
  }

  submit(){

  }
}
