import {
  Component,
  OnInit,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
  Input,
  SimpleChange,
  Output,
  EventEmitter,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonService } from './../../common.service';
import { Host } from '../bestpractice.component';
import { HttpClient } from '@angular/common/http';
import { handleRes, stringParseObj, valueOrDefault } from './../../../app.helpers';
import { isMockData, mockData } from 'mock/mock';

/*  */
const URL_GET_VIRTUAL_NIC_LIST = 'v1/bestpractice/virtual-nic';
const URL_UPDATE_VIRTUAL_NIC_LIST = 'v1/bestpractice/virtual-nic/update';

@Component({
  selector: 'app-bp-panel-list-mtu',
  templateUrl: './bp-panel-list-mtu.component.html',
  styleUrls: ['./bp-panel-list-mtu.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [TranslatePipe],
})
export class BpPanelListMtuComponent implements OnInit {
  @Input() hostList: Host[];
  @Input() currentItem;
  @Output() onApply: EventEmitter<any>;

  /* 外部传入的hostList 请求之后获取的需要展示的list */
  dataTableList: Host[] = [];
  hostTotal = 0;
  hostIsLoading = false;
  /* 主机选中列表 */
  hostSelected = [];
  hostListChange;

  constructor(
    private http: HttpClient,
    private $utils: CommonService,
    private translatePipe: TranslatePipe,
    private cdr: ChangeDetectorRef
  ) {
    this.onApply = new EventEmitter();
  }

  ngOnInit(): void {}

  resetVariables() {
    this.hostSelected = [];
    this.hostTotal = 0;
  }

  async getVirtualNicList(params) {
    return new Promise((resolve, reject) => {
      this.http.post(URL_GET_VIRTUAL_NIC_LIST, params).subscribe(resolve, reject);
    });
  }
  async applyBP(params) {
    return new Promise((resolve, reject) => {
      this.http.post(URL_UPDATE_VIRTUAL_NIC_LIST, params).subscribe(resolve, reject);
    });
  }

  async handleCurrentItemChange() {}

  async handleHostListChange() {
    const isHostListChange = this.hostListChange && Array.isArray(this.hostListChange.currentValue);
    if (isHostListChange) {
      const _hostList = this.hostListChange.currentValue;
      this.resetVariables();
      if (_hostList.length > 0) {
        const targetObjectIds = this.hostListChange?.currentValue?.map(i => i.hostObjectId);
        let res;
        if (isMockData) {
          res = mockData.BESTPRACTICE_VIRTUAL_NIC;
        } else {
          this.hostIsLoading = true;
          try {
            res = await this.getVirtualNicList(targetObjectIds);
          } catch (error) {
            console.log(error);
          } finally {
            this.hostIsLoading = false;
          }
        }
        handleRes(res, {
          fn: data => {
            const dataTableList = stringParseObj(data, []);
            this.dataTableList = dataTableList.map(i => {
              return {
                ...i,
                host: valueOrDefault(i.hostObjIp),
                device: valueOrDefault(i.device),
                nicIp: valueOrDefault(i.nicIp),
                mtu: valueOrDefault(i.mtu),
              };
            });
            this.hostTotal = this.dataTableList.length;
            this.cdr.detectChanges();
          },
        });
      } else {
        this.dataTableList = [];
      }
      this.cdr.detectChanges();
    }
  }

  /* 监听prop的变动 */
  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.hostListChange = changes['hostList'];
    /* handle hostList changed */
    this.handleHostListChange();
  }

  $t(prop) {
    return this.translatePipe.transform(prop);
  }
  /**
   * @Description 实施
   * @date 2021-04-13
   * @returns {any}
   */
  async applyClick() {
    console.log(this.hostSelected);

    const paramArray = this.hostSelected.map(i => ({
      device: i.device,
      hostObjectId: i.hostObjId,
    }));
    try {
      const res = await this.applyBP(paramArray);
      this.onApply.emit(res);
      /* refresh */
      this.handleHostListChange();
      console.log('handleHostListChange', res);
    } catch (error) {
      this.onApply.emit(error);
      console.log('实施失败', error);
    }
  }

  sortFunc(obj: any) {
    return !obj;
  }
}
