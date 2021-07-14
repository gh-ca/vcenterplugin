import {Component, OnInit,Input} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {VmfsInfo} from "../../routes/vmfs/list/list.service";
import {Subject} from "rxjs";

@Component({
  selector:"repair-type-filter",
  template:`<clr-radio-container style="margin-top: 0">
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="type"
        (change)="changeFunc($event)"
        value="">
      <label>{{ 'vmfs.filter.all' | translate }}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="type"
        (change)="changeFunc($event)"
        value="手动">
      <label>{{'bestPractice.repairHistory.manual'|translate}}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="type"
        (change)="changeFunc($event)"
        value="自动">
      <label>{{'bestPractice.repairHistory.automatic'|translate}}</label>
    </clr-radio-wrapper>
  </clr-radio-container>`
})
export class RepairTypeFilter implements ClrDatagridFilterInterface<VmfsInfo>{
  options
  // repairType
  changes = new Subject<any>();
  /* 从 list 中 取的字段 */
  @Input() prop;
  /* 传入的list 与 datagrid row数据一致 */
  @Input() list;

  accepts(item: VmfsInfo): boolean {
    if (!this.options){
      return true
    }
    const capital=item[this.prop]
    if(this.options===''){
      return true
    }else {
      return this.options===capital
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next()
  }

  initrepairType() {
    this.options=undefined
    this.changeFunc(this.options)
  }
}
