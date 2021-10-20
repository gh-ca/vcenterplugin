import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ClrDatagridFilterInterface } from '@clr/angular';
import { Subject } from 'rxjs';
import { List } from './nfs.service';
import { StorageList, StorageService } from '../storage/storage.service';
import { isMockData, mockData } from './../../../mock/mock';
import { getList as genDemStorageList } from 'mock/URLS_STORAGE/DMESTORAGE_STORAGES';
import {VmfsInfo, VmfsListService} from "../vmfs/list/list.service";

@Component({
  selector: 'device-filter',
  template: `
    <div class="over_flow" style="max-height: 500px;overflow: auto">
      <clr-radio-container style="margin-top: 0px; ">
        <clr-radio-wrapper>
          <input
            type="radio"
            clrRadio
            name="device"
            (change)="changeFunc($event)"
            [(ngModel)]="device"
            value=""
          />
          <label>{{ 'vmfs.filter.all' | translate }}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper *ngFor="let item of storageList">
          <input
            type="radio"
            clrRadio
            name="device"
            (change)="changeFunc($event)"
            [(ngModel)]="device"
            value="{{ item.id }}"
          />
          <label [title]="item.name">{{ item.name }}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
    </div>
  `,
  providers: [VmfsListService, StorageService],
})
export class DeviceFilter implements ClrDatagridFilterInterface<VmfsInfo>, OnInit {
  constructor(private storageService: StorageService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const successHandler = (s: any) => {
      if (s.code === '200') {
        this.storageList = s.data;
        this.storageList.forEach(item => {
          item.name = item.name + '(' + item.ip + ')';
          item.id = item.id.replace(/-/g, '').toLowerCase();
        });
      }
      this.cdr.detectChanges();
    };
    /* TODO: */
    if (isMockData) {
      successHandler(genDemStorageList(100));
    } else {
      this.storageService.getData(false).subscribe(successHandler);
    }
  }

  changes = new Subject<any>();
  device;
  storageList: StorageList[] = [];
  readonly status: any;

  accepts(item: VmfsInfo): boolean {
    if (!this.device) {
      return true;
    }
    const capital = item.device;
    if (this.device === '') {
      return true;
    } else {
      const storageId = item.deviceId.replace(/-/g, '').toLowerCase();
      return this.device == storageId;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initDevice() {
    this.device = undefined;
    this.changeFunc(this.device);
  }
}
