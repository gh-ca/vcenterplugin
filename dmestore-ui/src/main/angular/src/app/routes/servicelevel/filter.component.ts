import {ChangeDetectorRef, Component} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {SLStoragePool} from "./servicelevel.service";
import {ServicelevelService} from "./servicelevel.service";
import {HttpClient} from "@angular/common/http";

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
      return this.type == physicalType;
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
        <input type="radio" clrRadio name="name" (change)="changeFunc($event)" [(ngModel)]="name" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper *ngFor="let item of storageNames">
        <input type="radio" clrRadio name="name" (change)="changeFunc($event)" [(ngModel)]="name" value="{{item}}" />
        <label>{{item}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService, HttpClient],
})
export class SLSPStorageNameFilter implements ClrDatagridFilterInterface<SLStoragePool>{

  changes = new Subject<any>();
  name;
  storageNames:string[];
  readonly state: any;
  constructor() {}
  setStorageNameFilter(storageNames) {
    this.storageNames = storageNames;
  }
  accepts(item: SLStoragePool): boolean {
    if (!this.name || this.name == 'all') {
      return true;
    } else {
      const  physicalType = item.storageName;
      return this.name == physicalType;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initName() {
    this.name = undefined;
    this.changeFunc(this.name);
  }
}
