import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {StorageList, StorageService} from "./storage.service";
import {ClrDatagridFilter, ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {ServiceLevelList, VmfsInfo, VmfsListService} from "../vmfs/list/list.service";
import {StorageComponent} from "./storage.component";
import {DetailService, Dtrees, StorageController, StorageDisk, StoragePool, Volume} from "./detail/detail.service";
import {FileSystem} from "../nfs/nfs.service";
import {BondPort, EthernetPort, FailoverGroup, FCoEPort, FCPort, LogicPort} from "./detail/port.service";

@Component({
  selector: "storage-status-filter",
  template: `
    <div style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-container>
        <label>{{'storage.list.status' | translate}}</label>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="" />
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="0" />
          <label>{{'storage.list.offLine' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="1"/>
          <label>{{'storage.list.normal' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="2" />
          <label>{{'storage.list.fault' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="9" />
          <label>{{'storage.list.notMgr' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
      <clr-radio-container>
        <label>————————————————</label>
      </clr-radio-container>
      <clr-radio-container>
        <label>{{'storage.list.synStatus' | translate}}</label>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="synStatus" (change)="changeFunc($event)" [(ngModel)]="synStatus" value="" />
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="synStatus" (change)="changeFunc($event)" [(ngModel)]="synStatus" value="0" />
          <label>{{'storage.list.notSync' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="synStatus" (change)="changeFunc($event)" [(ngModel)]="synStatus" value="1" />
          <label>{{'storage.list.inSync' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="synStatus" (change)="changeFunc($event)" [(ngModel)]="synStatus" value="2" />
          <label>{{'storage.list.synced' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
    </div>
  `,
})
export class StorageStatusFilter implements ClrDatagridFilterInterface<StorageList>{

  changes = new Subject<any>();
  status;
  synStatus;
  readonly state: any;

  accepts(item: StorageList): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.status.toString();
      const  synStatus = item.synStatus.toString();
      if (!this.status&& this.synStatus) {
        return this.synStatus == synStatus;
      } else if (this.status && !this.synStatus) {
        return this.status == status;
      } else {
        return this.status == status && this.synStatus == synStatus;
      }
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }
  initStatus() {
    this.status = undefined;
    this.synStatus = undefined;
    this.changeFunc(this.status);
    this.changeFunc(this.synStatus);
  }
}

@Component({
  selector: "storage-pool-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="mediaType" (change)="changeFunc($event)" [(ngModel)]="mediaType" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="mediaType" (change)="changeFunc($event)" [(ngModel)]="mediaType" value="file"/>
        <label>{{'enum.type.file' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="mediaType" (change)="changeFunc($event)" [(ngModel)]="mediaType" value="block"/>
        <label>{{'enum.type.block' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="mediaType" (change)="changeFunc($event)" [(ngModel)]="mediaType" value="block/file"/>
        <label>{{'enum.type.blockAndFile' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class StoragePoolTypeFilter implements ClrDatagridFilterInterface<StoragePool>{

  changes = new Subject<any>();
  mediaType;
  readonly state: any;

  accepts(item: StoragePool): boolean {
    if (!this.mediaType || this.mediaType == 'all') {
      return true;
    } else {
      const  mediaType = item.mediaType.toString();
      return this.mediaType == mediaType;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }
  initType() {
    this.mediaType = undefined;
    this.changeFunc(this.mediaType);
  }
}
@Component({
  selector: "storage-pool-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'storage.detail.storagePool.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="fault"/>
        <label>{{'storage.detail.storagePool.fault' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="degraded"/>
        <label>{{'storage.detail.storagePool.degraded' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class StoragePoolStatusFilter implements ClrDatagridFilterInterface<StoragePool>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: StoragePool): boolean {
    if (!this.status || this.status == 'a;;') {
      return true;
    } else {
      const  status = item.healthStatus.toString();
      return this.status == status;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}

@Component({
  selector: "vol-status-filter",
  template: `
    <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="creating"/>
        <label>{{'enum.status.creating' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="mapping"/>
        <label>{{'enum.status.mapping' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unmapping"/>
        <label>{{'enum.status.unmapping' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="deleting"/>
        <label>{{'enum.status.deleting' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="error"/>
        <label>{{'enum.status.error' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="expanding"/>
        <label>{{'enum.status.expanding' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class VolStatusFilter implements ClrDatagridFilterInterface<Volume>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: Volume): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.status.toString();
      return this.status == status;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}

@Component({
  selector: "por-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="thick"/>
        <label>Thick</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="thin"/>
        <label>Thin</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class ProTypeFilter implements ClrDatagridFilterInterface<Volume>{

  changes = new Subject<any>();
  type;
  readonly state: any;

  accepts(item: Volume): boolean {
    if (!this.type || this.type == 'all') {
      return true;
    } else {
      const  type = item.alloctype.toString();
      return this.type == type;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initType() {
    this.type = undefined;
    this.changeFunc(this.type);
  }
}

@Component({
  selector: "map-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="false"/>
        <label>{{'enum.status.unmapped' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="true"/>
        <label>{{'enum.status.mapped' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class MapStatusFilter implements ClrDatagridFilterInterface<Volume>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: Volume): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.attached.toString();
      return this.status == status;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
@Component({
  selector: "vol-pool-filter",
  template: `
    <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="storagePoolName" (change)="changeFunc($event)" [(ngModel)]="storagePoolName" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper *ngFor="let item of allName">
        <input type="radio" clrRadio name="storagePoolName" (change)="changeFunc($event)" [(ngModel)]="storagePoolName" value="{{item}}"/>
        <label>{{item}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class VolStoragePoolFilter implements ClrDatagridFilterInterface<Volume>{

  changes = new Subject<any>();
  storagePoolName;
  allName:string[];
  readonly state: any;

  initAllName(allName) {
    this.allName = allName;
  }

  accepts(item: Volume): boolean {
    if (!this.storagePoolName || this.storagePoolName == 'all') {
      return true;
    } else {
      const  storagePoolName = item.storagePoolName.toString();
      return this.storagePoolName == storagePoolName;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initPoolNameStatus() {
    this.storagePoolName = undefined;
    this.changeFunc(this.storagePoolName);
  }
}

@Component({
  selector: "vol-serviceLevel-filter",
  template: `
      <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="all" />
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="" />
          <label>{{'vmfs.filter.empty' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper *ngFor="let item of serviceLevelList">
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="{{item}}"/>
          <label>{{item}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class VolServiceLevelFilter implements ClrDatagridFilterInterface<Volume>, OnInit{


  changes = new Subject<any>();
  serviceLevel;
  serviceLevelList: string[]; // 服务等级列表

  readonly status: any;

  constructor(private vmfsListService: VmfsListService,private cdr: ChangeDetectorRef){}

  ngOnInit(): void {
    this.vmfsListService.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0).map(item => item.name);
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  accepts(item: Volume): boolean {
    if (!this.serviceLevel || this.serviceLevel=='all') {
      return true;
    } else {
      const  capital  = item.serviceLevelName;
      return this.serviceLevel == capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initServiceLevel() {
    this.serviceLevel = undefined;
    this.changeFunc(this.serviceLevel);
  }

}

@Component({
  selector: "vol-protection-status-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value=""/>
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value="true" />
          <label>{{'enum.status.protected' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value="false" />
          <label>{{'enum.status.unprotected' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class VolProtectionStatusFilter implements ClrDatagridFilterInterface<Volume>{
  changes = new Subject<any>();
  protectionStatus;

  readonly status: any;

  accepts(item: Volume): boolean {
    if (!this.protectionStatus || this.protectionStatus == 'all') {
      return true;
    } else {
      const  capital  = item.protectionStatus.toString();
      return this.protectionStatus === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initProtectionStatus() {
    this.protectionStatus = undefined;
    this.changeFunc(this.protectionStatus);
  }
}
@Component({
  selector: "fs-status-filter",
  template: `
      <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="fsStatus" (change)="changeFunc($event)" [(ngModel)]="fsStatus" value=""/>
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="fsStatus" (change)="changeFunc($event)" [(ngModel)]="fsStatus" value="normal" />
          <label>{{'enum.status.normal' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="fsStatus" (change)="changeFunc($event)" [(ngModel)]="fsStatus" value="faulty" />
          <label>{{'enum.status.fault' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class FsStatusFilter implements ClrDatagridFilterInterface<FileSystem> {
  changes = new Subject<any>();
  fsStatus;

  readonly status: any;

  accepts(item: FileSystem): boolean {
    if (!this.fsStatus || this.fsStatus == 'all') {
      return true;
    } else {
      const capital = item.healthStatus.toString();
      return this.fsStatus === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    console.log("model2", this.changes.next())
    console.log("model2Value", value)
    console.log("model2Value2", this.changes.next() == value)
    this.changes.next();
  }

  initStatus() {
    this.fsStatus = undefined;
    this.changeFunc(this.fsStatus);
  }
}

@Component({
  selector: "fs-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="thick"/>
        <label>Thick</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="thin"/>
        <label>Thin</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class FsTypeFilter implements ClrDatagridFilterInterface<FileSystem>{

  changes = new Subject<any>();
  type;
  readonly state: any;

  accepts(item: FileSystem): boolean {
    if (!this.type || this.type == 'all') {
      return true;
    } else {
      const type = item.allocType;
      return type.toLowerCase().indexOf(this.type.toLowerCase()) >= 0;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initType() {
    this.type = undefined;
    this.changeFunc(this.type);
  }
}

@Component({
  selector: "dtree-secMod-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="secMod" (change)="changeFunc($event)" [(ngModel)]="secMod" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="secMod" (change)="changeFunc($event)" [(ngModel)]="secMod" value="Mixed"/>
        <label>Mixed</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="secMod" (change)="changeFunc($event)" [(ngModel)]="secMod" value="Native"/>
        <label>Native</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="secMod" (change)="changeFunc($event)" [(ngModel)]="secMod" value="NTFS"/>
        <label>NTFS</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="secMod" (change)="changeFunc($event)" [(ngModel)]="secMod" value="UNIX"/>
        <label>UNIX</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class DtreeSecModFilter implements ClrDatagridFilterInterface<Dtrees>{

  changes = new Subject<any>();
  secMod;
  readonly state: any;

  accepts(item: Dtrees): boolean {
    console.log("this.secMod", this.secMod, item.securityStyle.toString())
    if (!this.secMod || this.secMod == 'all') {
      return true;
    } else {
      const  secMod = item.securityStyle.toString();
      return secMod.toLowerCase().indexOf(this.secMod.toLowerCase()) >= 0;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initSecMod() {
    this.secMod = undefined;
    this.changeFunc(this.secMod);
  }
}

@Component({
  selector: "dtree-quota-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="quota" (change)="changeFunc($event)" [(ngModel)]="quota" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="quota" (change)="changeFunc($event)" [(ngModel)]="quota" value="true"/>
        <label>{{'enum.status.enabled' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="quota" (change)="changeFunc($event)" [(ngModel)]="quota" value="false"/>
        <label>{{'enum.status.notEnabled' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class DtreeQuotaFilter implements ClrDatagridFilterInterface<Dtrees>{

  changes = new Subject<any>();
  quota;
  readonly state: any;

  accepts(item: Dtrees): boolean {
    if (!this.quota || this.quota == 'all') {
      return true;
    } else {
      const  quota = item.quotaSwitch.toString();
      return this.quota == quota;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initQuota() {
    this.quota = undefined;
    this.changeFunc(this.quota);
  }
}

@Component({
  selector: "hd-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="running"/>
        <label>{{'enum.status.running' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="connected"/>
        <label>{{'enum.status.connected' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="disconnected"/>
        <label>{{'enum.status.disconnected' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class HardwareStatusFilter implements ClrDatagridFilterInterface<StorageController>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: StorageController): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  quota = item.status.toString();
      return this.status == quota;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}

@Component({
  selector: "disk-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="fault"/>
        <label>{{'enum.status.fault' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class DiskStatusFilter implements ClrDatagridFilterInterface<StorageDisk>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: StorageDisk): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  quota = item.status.toString();
      if (this.status == 'fault') {
        return quota.toLowerCase().indexOf('offline') >= 0 || quota.toLowerCase().indexOf(this.status.toLowerCase()) >= 0;
      } else {
        return quota.toLowerCase().indexOf(this.status.toLowerCase()) >= 0;
      }
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
@Component({
  selector: "disk-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SATA"/>
        <label>SATA</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SAS"/>
        <label>SAS</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SSD"/>
        <label>SSD</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="NL-SAS"/>
        <label>NL-SAS</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="UNKNOWN"/>
        <label>{{'storage.detail.storagePool.unknown' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class DiskTypeFilter implements ClrDatagridFilterInterface<StorageDisk>{

  changes = new Subject<any>();
  type;
  readonly state: any;

  accepts(item: StorageDisk): boolean {
    if (!this.type || this.type == 'all') {
      return true;
    } else {
      const  physicalType = item.physicalType;
      return this.type == physicalType;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.type = undefined;
    this.changeFunc(this.type);
  }
}
@Component({
  selector: "fc-status-filter",
  template: `
    <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="running"/>
        <label>{{'enum.status.running' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="connected"/>
        <label>{{'enum.status.connected' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="disconnected"/>
        <label>{{'enum.status.disconnected' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class FcStatusFilter implements ClrDatagridFilterInterface<FCPort>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: FCPort): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  connectStatus = item.connectStatus;
      return this.status == connectStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
@Component({
  selector: "eth-status-filter",
  template: `
    <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="running"/>
        <label>{{'enum.status.running' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="connected"/>
        <label>{{'enum.status.connected' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="disconnected"/>
        <label>{{'enum.status.disconnected' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class EthStatusFilter implements ClrDatagridFilterInterface<EthernetPort>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: EthernetPort): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  connectStatus = item.connectStatus;
      return this.status == connectStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}

@Component({
  selector: "fcoe-status-filter",
  template: `
    <clr-radio-container style="max-height: 6.25rem;overflow-y: auto">
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="running"/>
        <label>{{'enum.status.running' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="connected"/>
        <label>{{'enum.status.connected' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="disconnected"/>
        <label>{{'enum.status.disconnected' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class FcoeStatusFilter implements ClrDatagridFilterInterface<FCoEPort>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: FCoEPort): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  connectStatus = item.connectStatus;
      return this.status == connectStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
@Component({
  selector: "logic-running-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="LINK_DOWN"/>
        <label>{{'enum.status.linkDown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="LINK_UP"/>
        <label>{{'enum.status.linkUp' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class LogicRunningStatusFilter implements ClrDatagridFilterInterface<LogicPort>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: LogicPort): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  connectStatus = item.runningStatus;
      return this.status == connectStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
@Component({
  selector: "logic-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="ACTIVATED"/>
        <label>{{'enum.status.activated' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="others"/>
        <label>{{'enum.status.unActivated' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class LogicStatusFilter implements ClrDatagridFilterInterface<LogicPort>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: LogicPort): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  connectStatus = item.operationalStatus;
      if (this.status == 'others') {
        return connectStatus != 'ACTIVATED';
      } else {
        return this.status == connectStatus;
      }
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}

@Component({
  selector: "logic-ddns-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="ddnsStatus" (change)="changeFunc($event)" [(ngModel)]="ddnsStatus" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="ddnsStatus" (change)="changeFunc($event)" [(ngModel)]="ddnsStatus" value="ENABLE"/>
        <label>{{'enum.status.enable' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="ddnsStatus" (change)="changeFunc($event)" [(ngModel)]="ddnsStatus" value="INVALID"/>
        <label>{{'enum.status.invalid' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="ddnsStatus" (change)="changeFunc($event)" [(ngModel)]="ddnsStatus" value="notActivated"/>
        <label>{{'enum.status.notActivated' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class LogicDdnsStatusFilter implements ClrDatagridFilterInterface<LogicPort>{

  changes = new Subject<any>();
  ddnsStatus;
  readonly state: any;

  accepts(item: LogicPort): boolean {
    if (!this.ddnsStatus || this.ddnsStatus == 'all') {
      return true;
    } else {
      const  ddnsStatus = item.ddnsStatus;
      if (this.ddnsStatus == 'notActivated') {
        return ddnsStatus != 'ENABLE' && ddnsStatus != 'INVALID';
      } else {
        return this.ddnsStatus == ddnsStatus;
      }
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.ddnsStatus = undefined;
    this.changeFunc(this.ddnsStatus);
  }
}

@Component({
  selector: "logic-role-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="role" (change)="changeFunc($event)" [(ngModel)]="role" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="role" (change)="changeFunc($event)" [(ngModel)]="role" value="1"/>
        <label>{{'enum.status.service' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="role" (change)="changeFunc($event)" [(ngModel)]="role" value="2"/>
        <label>{{'enum.status.management' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="role" (change)="changeFunc($event)" [(ngModel)]="role" value="3"/>
        <label>{{'enum.status.managementAndService' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class LogicRoleFilter implements ClrDatagridFilterInterface<LogicPort>{

  changes = new Subject<any>();
  role;
  readonly state: any;

  accepts(item: LogicPort): boolean {
    if (!this.role || this.role == 'all') {
      return true;
    } else {
      const  ddnsStatus = item.role;
      return this.role == ddnsStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.role = undefined;
    this.changeFunc(this.role);
  }
}
@Component({
  selector: "failover-group-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="failoverGroupType" (change)="changeFunc($event)" [(ngModel)]="failoverGroupType" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="failoverGroupType" (change)="changeFunc($event)" [(ngModel)]="failoverGroupType" value="system"/>
        <label>{{'enum.status.system' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="failoverGroupType" (change)="changeFunc($event)" [(ngModel)]="failoverGroupType" value="VLAN"/>
        <label>{{'enum.status.vlan' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="failoverGroupType" (change)="changeFunc($event)" [(ngModel)]="failoverGroupType" value="customized"/>
        <label>{{'enum.status.customized' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class FailoverGroupTypeFilter implements ClrDatagridFilterInterface<FailoverGroup>{

  changes = new Subject<any>();
  failoverGroupType;
  readonly state: any;

  accepts(item: FailoverGroup): boolean {
    if (!this.failoverGroupType || this.failoverGroupType == 'all') {
      return true;
    } else {
      const  failoverGroupType = item.failoverGroupType;
      return this.failoverGroupType == failoverGroupType;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.failoverGroupType = undefined;
    this.changeFunc(this.failoverGroupType);
  }
}

@Component({
  selector: "health-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="healthStatus" (change)="changeFunc($event)" [(ngModel)]="healthStatus" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="healthStatus" (change)="changeFunc($event)" [(ngModel)]="healthStatus" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="healthStatus" (change)="changeFunc($event)" [(ngModel)]="healthStatus" value="normal"/>
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="healthStatus" (change)="changeFunc($event)" [(ngModel)]="healthStatus" value="abnormal"/>
        <label>{{'enum.status.abnormal' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class HealthStatusFilter implements ClrDatagridFilterInterface<BondPort>{

  changes = new Subject<any>();
  healthStatus;
  readonly state: any;

  accepts(item: BondPort): boolean {
    if (!this.healthStatus || this.healthStatus == 'all') {
      return true;
    } else {
      const  healthStatus = item.healthStatus;
      return this.healthStatus == healthStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.healthStatus = undefined;
    this.changeFunc(this.healthStatus);
  }
}
@Component({
  selector: "ports-running-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="runningStatus" (change)="changeFunc($event)" [(ngModel)]="runningStatus" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="runningStatus" (change)="changeFunc($event)" [(ngModel)]="runningStatus" value="unknown"/>
        <label>{{'enum.status.unknown' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="runningStatus" (change)="changeFunc($event)" [(ngModel)]="runningStatus" value="connected"/>
        <label>{{'enum.status.connected' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="runningStatus" (change)="changeFunc($event)" [(ngModel)]="runningStatus" value="disconnected"/>
        <label>{{'enum.status.disconnected' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class PortsRunningStatusFilter implements ClrDatagridFilterInterface<BondPort>{

  changes = new Subject<any>();
  runningStatus;
  readonly state: any;

  accepts(item: BondPort): boolean {
    if (!this.runningStatus || this.runningStatus == 'all') {
      return true;
    } else {
      const  runningStatus = item.runningStatus;
      return this.runningStatus == runningStatus;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.runningStatus = undefined;
    this.changeFunc(this.runningStatus);
  }
}
