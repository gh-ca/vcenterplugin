import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {SLStoragePool} from "./servicelevel.service";
import {ServicelevelService} from "./servicelevel.service";
import {HttpClient} from "@angular/common/http";
import {Volume} from "../storage/detail/detail.service";
import {StorageList, StorageService} from "../storage/storage.service";

@Component({
  selector: "sl-sp-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="offline"/>
        <label>{{'tier.offLine' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'tier.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="abnormal"/>
        <label>{{'tier.faulty' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'tier.unknown' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService],
})
export class SLSPStatusFilter implements ClrDatagridFilterInterface<SLStoragePool>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: SLStoragePool): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.runningStatus;
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
  selector: "sl-sp-disk-type-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SSD" />
        <label>SSD</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="FC"/>
        <label>FC</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SAS"/>
        <label>SAS</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="NL_SAS"/>
        <label>NL_SAS</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SATA"/>
        <label>SATA</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SLC_SSD"/>
        <label>SLC_SSD</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="MLC_SSD"/>
        <label>MLC_SSD</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="FC_SED"/>
        <label>FC_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SAS_SED"/>
        <label>SAS_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SATA_SED"/>
        <label>SATA_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SSD_SED"/>
        <label>SSD_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="NL-SAS_SED"/>
        <label>NL-SAS_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="SLC_SSD_SED"/>
        <label>SLC_SSD_SED</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="type" (change)="changeFunc($event)" [(ngModel)]="type" value="MLC_SSD_SED"/>
        <label>MLC_SSD_SED</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService],
})
export class SLSPDiskTypeFilter implements ClrDatagridFilterInterface<SLStoragePool>{

  changes = new Subject<any>();
  type;
  readonly state: any;

  accepts(item: SLStoragePool): boolean {
    if (!this.type || this.type == 'all') {
      return true;
    } else {
      const  physicalType = item.physicalType;
      return physicalType.toLowerCase().indexOf(this.type.toLowerCase()) >= 0;
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
  selector: "sl-sp-storage-name-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="storageId" (change)="changeFunc($event)" [(ngModel)]="storageId" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper *ngFor="let item of list">
        <input type="radio" clrRadio name="storageId" (change)="changeFunc($event)" [(ngModel)]="storageId" value="{{item.id}}" />
        <label>{{item.name}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService, HttpClient, StorageService],
})
export class SLSPStorageNameFilter implements ClrDatagridFilterInterface<SLStoragePool>, OnInit{

  changes = new Subject<any>();
  storageId;
  list: StorageList[] = []; // 数据列表
  readonly state: any;
  constructor(private storageService: StorageService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void {
    this.storageService.getData()
      .subscribe((result: any) => {
        if (result.code == '200') {
          this.list = result.data;

          this.list.forEach(item => {
            item.name = item.name + "(" + item.ip + ")";
            item.id = item.id.replace(/-/g, '').toLowerCase();
          })
        }
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
  }
  accepts(item: SLStoragePool): boolean {
    if (!this.storageId || this.storageId == 'all') {
      return true;
    } else {
      const  storageId = item.storageId.toLowerCase();
      return this.storageId == storageId;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initName() {
    this.storageId = undefined;
    this.changeFunc(this.storageId);
  }
}

@Component({
  selector: "lun-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal" />
        <label>{{'enum.status.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="creating"/>
        <label>{{'enum.status.creating' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="mapping" />
        <label>{{'enum.status.mapping' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unmapping" />
        <label>{{'enum.status.unmapping' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="deleting" />
        <label>{{'enum.status.deleting' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="error" />
        <label>{{'enum.status.error' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="expanding" />
        <label>{{'enum.status.expanding' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService, HttpClient],
})
export class LUNStatusFilter implements ClrDatagridFilterInterface<Volume>{

  changes = new Subject<any>();
  status;
  storageNames:string[];
  readonly state: any;
  constructor() {}
  accepts(item: Volume): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.status;
      return this.status == status;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initName() {
    this.status = undefined;
    this.changeFunc(this.status);
  }
}
