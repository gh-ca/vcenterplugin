import {ChangeDetectionStrategy, Component} from "@angular/core";
import {ClrDatagridFilter, ClrDatagridFilterInterface, ClrDatagridStringFilterInterface} from "@clr/angular";
import {VmfsInfo, VmfsListService} from "./list.service";
import {Subject} from "rxjs";

@Component({
  selector: "color-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="" />
          <label>ALL</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="normal"/>
          <label>normal</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="abnormal" />
          <label>abnormal</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `
})
export class ColorFilter implements ClrDatagridFilterInterface<VmfsInfo> {
  changes = new Subject<any>();
  options;
  normal = false;
  abnormal = false;

  readonly status: any;

  accepts(item: VmfsInfo): boolean {
    if (!this.options) {
      return true;
    }
    const  capital  = item.status;
    console.log("capital", capital);
    if (this.options === '') {
      return true;
    } else {
      return this.options === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }
  changeFunc1() {
    if (this.normal) {
      this.options = 'normal';
      this.changes.next();
    }
  }
  changeFunc2() {
    if (this.abnormal) {
      this.options = 'abnormal';
      this.changes.next();
    }
  }
}
