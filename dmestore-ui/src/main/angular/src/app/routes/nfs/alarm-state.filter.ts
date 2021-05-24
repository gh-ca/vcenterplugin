import { Component, OnInit } from '@angular/core';
import { ClrDatagridFilterInterface } from '@clr/angular';
import { Subject } from 'rxjs';
import { List } from './nfs.service';

@Component({
  selector: 'filter-alarm-state',
  template: `
    <clr-radio-container [class]="'filter-alarm-state'">
      <clr-radio-wrapper>
        <input
          type="radio"
          clrRadio
          name="options"
          (change)="changeFunc($event)"
          [(ngModel)]="options"
          value=""
        />
        <label>{{ 'enum.status.all' | translate }}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input
          type="radio"
          clrRadio
          name="options"
          (change)="changeFunc($event)"
          [(ngModel)]="options"
          value="1"
        />
        <label>{{ 'enum.status.normal' | translate }}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input
          type="radio"
          clrRadio
          name="options"
          (change)="changeFunc($event)"
          [(ngModel)]="options"
          value="2"
        />
        <label>{{ 'enum.status.warning' | translate }}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input
          type="radio"
          clrRadio
          name="options"
          (change)="changeFunc($event)"
          [(ngModel)]="options"
          value="3"
        />
        <label>{{ 'enum.status.alert' | translate }}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
})
export class AlarmState implements ClrDatagridFilterInterface<List>, OnInit {
  ngOnInit(): void {}
  changes = new Subject<any>();
  options;
  normal = false;
  abnormal = false;

  readonly status: any;

  accepts(item: List): boolean {
    if (!this.options) {
      return true;
    }
    const capital = item.alarmState;
    if (!this.options) {
      return true;
    } else {
      return String(this.options) === String(capital);
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }
  initStatus() {
    this.options = undefined;
    this.changeFunc(this.options);
  }
}
