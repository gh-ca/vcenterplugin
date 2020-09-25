import {ChangeDetectorRef, Component, OnInit, AfterViewInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';

@Component({
  selector: 'app-iscsi',
  templateUrl: './iscsi.component.html',
  styleUrls: ['./iscsi.component.scss'],
  providers: [CommonService, GlobalsService]
})
export class IscsiComponent implements OnInit, AfterViewInit {

  portGetUrl = 'dmestorage/getstorageethports';
  portGetUrlParams = {
    params: {
      storageSn: ''
    }
  };
  ipsGetUrl = 'accessvmware/getvmkernelipbyhostobjectid';
  ipsGetUrlParams = {
    params: {
      hostObjectId: ''
    }
  };

  storageGetUrl = 'dmestorage/storages';

  configIscsiUrl = 'accesshost/configureiscsi';

  // 存储下拉框数据
  storageDevices = [];

  // ip下拉框数据
  ips = [];

  // 提交数据
  configModel = {
    hostObjectId: '',
    vmKernel: {
      device: ''
    },
    ethPorts: [],
    sn: ''
  };

  hostObjectId = 'urn:vmomi:HostSystem:host-9:f8e381d7-074b-4fa9-9962-9a68ab6106e1';
  // port列表
  portLoading = false;
  portList = [];
  portTotal = 0;
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    console.log('12312');
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    this.ipsGetUrlParams.params.hostObjectId = this.hostObjectId;
    this.configModel.hostObjectId = this.hostObjectId;
    this.loadIps();
    this.loadStorageDevice();
  }

  loadIps(){
    this.http.get(this.ipsGetUrl, this.ipsGetUrlParams).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.ips = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadStorageDevice(){
    this.http.get(this.storageGetUrl, {}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.storageDevices = result.data.data;
        setTimeout(() => {
          if (this.configModel.sn !== ''){
            this.loadPorts();
          }
        }, 1000);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadPorts(){
    this.portLoading = true;
    this.portGetUrlParams.params.storageSn = this.configModel.sn;
    this.http.get(this.portGetUrl, this.portGetUrlParams).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.portList = result.data;
        this.portTotal = result.data.length;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        this.portLoading = false;
      }
    }, err => {
      console.error('ERROR', err);
      this.portLoading = false;
    });
  }

  submit(){
    console.log(this.configModel);
    this.http.post(this.configIscsiUrl, this.configModel).subscribe((result: any) => {
      console.log(result);
    }, err => {
      console.error('ERROR', err);
    });
  }
}
