import {Component} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {bpTableData} from './bp-repair-history-table.component'

@Component({
  selector:"repair-type-filter",
  template:`<clr-radio-container style="margin-top: 0">
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="repairType"
        (change)="changeFunc($event)"
        value="">
      <label>{{ 'vmfs.filter.all' | translate }}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="repairType"
        (change)="changeFunc($event)"
        value="0">
      <label>{{'bestPractice.repairHistory.manual'|translate}}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="options"
        type="radio"
        name="repairType"
        (change)="changeFunc($event)"
        value="1">
      <label>{{'bestPractice.repairHistory.automatic'|translate}}</label>
    </clr-radio-wrapper>
  </clr-radio-container>`
})
export class RepairTypeFilter implements ClrDatagridFilterInterface<bpTableData>{
  options
  // repairType
  changes = new Subject<any>();

  accepts(item: bpTableData): boolean {
    if (!this.options){
      return true
    }
    const capital=item.repairType
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

@Component({
  selector:"repair-result-filter",
  template:`<clr-radio-container style="margin-top: 0">
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="repairResult"
        type="radio"
        name="repairResult"
        (change)="changeFunc($event)"
        value="">
      <label>{{ 'vmfs.filter.all' | translate }}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="repairResult"
        type="radio"
        name="repairResult"
        (change)="changeFunc($event)"
        value="true">
      <label>{{'bestPractice.repairHistory.success'|translate}}</label>
    </clr-radio-wrapper>
    <clr-radio-wrapper>
      <input
        clrRadio
        [(ngModel)]="repairResult"
        type="radio"
        name="repairResult"
        (change)="changeFunc($event)"
        value="false">
      <label>{{'bestPractice.repairHistory.fail'|translate}}</label>
    </clr-radio-wrapper>
  </clr-radio-container>`
})
export class RepairResultFilter implements ClrDatagridFilterInterface<bpTableData>{
  // options
  repairResult
  changes = new Subject<any>();

  accepts(item: bpTableData): boolean {
    if (!this.repairResult){
      return true
    }
    const capital=item.repairResult
    if(this.repairResult===''){
      return true
    }else {
      return this.repairResult===String(capital)
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next()
  }

  initrepairType() {
    this.repairResult=undefined
    this.changeFunc(this.repairResult)
  }
}
