import { ChangeDetectorRef, Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../common.service';
import { GlobalsService } from '../../shared/globals.service';
import { ClrForm } from '@clr/angular';
import { TranslatePipe } from "@ngx-translate/core";
import { isMockData, mockData } from 'mock/mock';

@Component({
  selector: 'app-iscsi',
  templateUrl: './iscsi.component.html',
  styleUrls: ['./iscsi.component.scss'],
  providers: [CommonService, TranslatePipe]
})
export class IscsiComponent implements OnInit, AfterViewInit {

  @ViewChild(ClrForm, { static: true }) rdmFormGroup;
  @ViewChild('myForm', { static: true }) myForm;
  testPortConnectedUrl = "accesshost/testconnectivity";
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

  hostObjectId = '';
  // port列表
  portLoading = false;
  portList = [];
  portTotal = 0;

  tipModalSuccess = false;
  tipModalFail = false;

  isSubmit = false;


  ipLoading = false;
  dsDeviceLoading = false;
  submitLoading = false;
  constructor(
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
    private commonService: CommonService,
    private gs: GlobalsService, private translatePipe: TranslatePipe) { }

  ngOnInit(): void { }

  ngAfterViewInit() {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    if (ctx == null) {
      this.hostObjectId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
    } else {
      this.hostObjectId = ctx[0].id;
    }
    this.ipsGetUrlParams.params.hostObjectId = this.hostObjectId;
    this.configModel.hostObjectId = this.hostObjectId;
    this.loadIps();
    this.loadStorageDevice();
  }

  loadIps() {
    this.ipLoading = true;
    const successHandler = (result: any) => {
      this.ipLoading = false;
      if (result.code === '200') {
        this.ips = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    };
    const errorHandler = err => {
      console.error('ERROR', err);
    };
    /* TODO: 2021年4月7日 切换不刷新 */
    if (isMockData) {
      successHandler(mockData.ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID)
    } else {
      this.http.get(this.ipsGetUrl, this.ipsGetUrlParams).subscribe(successHandler, errorHandler);
    }
    this.cdr.detectChanges();
  }

  loadStorageDevice() {
    this.dsDeviceLoading = true;
    const successHandler = (result: any) => {
      this.dsDeviceLoading = false;
      if (result.code === '200') {
        this.storageDevices = result.data;
        setTimeout(() => {
          this.loadPorts();
        }, 1000);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    };
    const errorHandler = err => {
      console.error('ERROR', err);
    };
    /* TODO: */
    if (isMockData) {
      successHandler(mockData.DMESTORAGE_STORAGES)
    } else {
      this.http.get(this.storageGetUrl, {}).subscribe(successHandler, errorHandler);
    }
  }

  resetListInfo() {
    // 端口列表中不展示名称为MGMT和MAINTENANCE的端口
    this.portList = []
    this.portTotal = 0;
    // 连通状态
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }


  loadPorts() {
    // 有存储 有ip才去load
    if (this.configModel.sn !== '' && this.configModel.vmKernel.device !== '') {
      this.portLoading = true;
      /* FIX:切换选项不清空列表 */
      this.resetListInfo();
      const isV6 = this.storageDevices.filter(item => item.sn == this.configModel.sn)[0].storageTypeShow.dorado;
      // V5设备访问
      if (!isV6) {
        this.portGetUrlParams.params.storageSn = this.configModel.sn;
        const successHandler = (result: any) => {
          this.portLoading = false;
          if (result.code === '200') {
            // result.data.forEach((item) => {
            //   item.connectStatus = '';
            // });
            // 端口列表中不展示名称为MGMT和MAINTENANCE的端口
            this.portList = result.data.filter(item => item.portName.toLowerCase().indexOf('mgmt') < 0
              && item.portName.toLowerCase().indexOf('maintenance') < 0 && item.mgmtIp);
            this.portTotal = result.data.length;
            // 连通状态
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
            this.testPortConnected();
          }
        };

        const errorHandler = err => {
          console.error('ERROR', err);
          this.portLoading = false;
        };

        /* TODO: */
        if (isMockData) {
          if (this.configModel.sn !== '2102351QLH9WK5800028_mock') {
            successHandler(mockData.DMESTORAGE_GETSTORAGEETHPORTS);
          } else {
            this.portLoading = false;
          }
        } else {
          this.http.get(this.portGetUrl, this.portGetUrlParams).subscribe(successHandler, errorHandler);
        }
      } else {// V6设备访问
        this.portLoading = true;
        const storageId = this.storageDevices.filter(item => item.sn == this.configModel.sn)[0].id;
        const successHandler = (result: any) => {
          this.portLoading = false;
          if (result.code === '200') {
            const logicDatas = [];
            for (let i = 0; i < result.data.length; i++) {
              let logicData = {
                id: '',
                location: '',
                portName: '',
                mgmtIp: '',
                mgmtIpv6: '',
                mac: '',
                maxSpeed: null,
                speed: null,
                status: '',
                connectStatusType: '',
              };
              logicData.location = result.data[i].currentPortName;
              logicData.id = result.data[i].id;
              logicData.portName = result.data[i].name;
              logicData.mgmtIp = result.data[i].mgmtIp;
              logicData.mgmtIpv6 = result.data[i].mgmtIpv6;

              logicDatas.push(logicData);
            }

            // 端口列表中不展示名称为MGMT和MAINTENANCE的端口
            this.portList = logicDatas.filter(item => item.portName.toLowerCase().indexOf('mgmt') < 0
              && item.portName.toLowerCase().indexOf('maintenance') < 0 && item.mgmtIp);
            // 连通状态
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
            this.testPortConnected();
          }
        };

        const errorHandler = err => {
          console.error('ERROR', err);
          this.portLoading = false;
        }
        /* TODO: */
        if (isMockData) {
          successHandler(mockData.DMESTORAGE_GETSTORAGEETHPORTS)
        } else {
          this.http.get('dmestorage/logicports?storageId=' + storageId + '&supportProtocol=iSCSI').subscribe(successHandler, errorHandler);
        }
      }
    }
  }

  testPortConnected() {
    const p = new testConnectParams();
    const testPortList = [];
    this.portList.forEach((item) => {
      // if (item.mgmtIp && item.mgmtIp != ""){
      testPortList.push(item);
      // }
    });
    p.ethPorts = testPortList;
    p.hostObjectId = this.configModel.hostObjectId;
    p.vmKernel = this.configModel.vmKernel;
    this.http.post(this.testPortConnectedUrl, p).subscribe((result: any) => {
      if (result.code === '200' && result.data) {
        result.data.forEach((i) => {
          this.portList.forEach((j) => {
            if (i.id == j.id) {
              j.connectStatus = i.connectStatus;
              j.connectStatusType = i.connectStatusType;
            }
          });
        });
      } else {
        alert("测试连通性出错");
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }, err => {
      console.error('ERROR', err);
    });
  }

  submit() {
    this.isSubmit = true;
    if (this.myForm.form.invalid) {
      this.rdmFormGroup.markAsTouched();
      return;
    } else {
      if (this.configModel.ethPorts.length == 0) {
        return;
      }
    }

    this.submitLoading = true;
    this.http.post(this.configIscsiUrl, this.configModel).subscribe((result: any) => {
      this.submitLoading = false;
      if (result.code == '200') {
        this.tipModalSuccess = true;
      } else {
        this.tipModalFail = true;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
    this.cdr.detectChanges();
  }

  closeWin() {
    this.gs.getClientSdk().modal.close();
  }
  footerTranslate() {
    if (document.getElementsByClassName("switch-header")[0]) {
      let transDom = document.getElementsByClassName("switch-header")[0] as HTMLElement;
      // let transHtml = transDom.innerHTML.replace(/Show Columns/, "展示列");
      transDom.innerText = this.translatePipe.transform('iscsi.showCol');
      let selectDom = document.getElementsByClassName("btn btn-sm btn-link switch-button")[0] as HTMLElement;
      // let selectHtml = selectDom.innerHTML.replace(/ Select All /, "全选");
      selectDom.innerText = this.translatePipe.transform('iscsi.selectAll');
    }
  }
  sortFunc(obj: any) {
    return !obj;
  }
}


class testConnectParams {
  ethPorts: any[];
  hostObjectId: string;
  vmKernel: object;
}
