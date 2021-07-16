import { SimpleChange } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';
import { formatCapacity } from 'app/app.helpers';
import { print } from './../../app.helpers';

const DEFAULT_ITEM = {
  capabilities: {
    resourceType: '',
    compression: false,
    deduplication: 'enabled',
    smarttier: null,
    iopriority: null,
    qos: {
      smartQos: null,
      qosParam: {
        enabled: null,
        latency: 0,
        latencyUnit: '',
        minBandWidth: 0,
        minIOPS: 0,
        maxBandWidth: 0,
        maxIOPS: 0,
        smartQos: null,
      },
      enabled: true,
    },
  },
  id: '',
  name: '',
  description: '',
  type: '',
  protocol: null,
  totalCapacity: 0,
  freeCapacity: 0,
  itemCapacity: 0,
};

@Component({
  selector: 'app-service-level-table',
  templateUrl: './service-level-table.component.html',
  styleUrls: ['./service-level-table.component.scss'],
})
export class ServiceLevelTableComponent implements OnInit {
  changes;
  @Input() item;
  @Input() isGB;
  _item;
  print;

  constructor() {}
  formatCapacity = formatCapacity;

  ngOnInit(): void {
    this.print = print;
    this._item = DEFAULT_ITEM;
  }

  itemChangeHandler() {
    if (this.changes['item']) {
      const item = this.changes['item'].currentValue;
      /*  */
      if (item) {
        this._item = item;
      } else {
        this._item = DEFAULT_ITEM;
      }
    }
  }

  check(prop) {
    return (
      this.item.capabilities  &&
      this.item.capabilities.qos  &&
      this.item.capabilities.qos.qosParam  &&
      this.item.capabilities.qos.qosParam[prop] &&
      this.item.capabilities.qos.qosParam[prop] !== "NaN"
    );
  }

  getValue(prop) {
    return this.item.capabilities.qos?.qosParam[prop];
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.changes = changes;
    this.itemChangeHandler();
  }
}
