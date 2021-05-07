import { SimpleChange } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';
import { STATUS_ARRAY, SYN_STATUS_ARRAY } from 'mock/URLS_STORAGE/DMESTORAGE_STORAGES';

const STATUS_MAP = STATUS_ARRAY.slice(1).reduce((target, i) => {
  target[i.value] = i;
  return target;
}, {});

const SYN_STATUS_MAP = SYN_STATUS_ARRAY.slice(1).reduce((target, i) => {
  target[i.value] = i;
  return target;
}, {});

@Component({
  selector: 'app-storage-status-cell',
  templateUrl: './storage-status-cell.component.html',
  styleUrls: ['./storage-status-cell.component.scss'],
})
export class StorageStatusCellComponent implements OnInit {
  @Input() item;

  itemChange;
  status;
  synStatus;

  constructor() {}

  ngOnInit(): void {}

  handleitemChange() {
    if (this.itemChange) {
      const content = this.itemChange.currentValue;
      const { status, synStatus } = content;
      /*  */
      const statusObj = STATUS_MAP[status];
      if (statusObj) {
        this.status = {
          color: statusObj.color,
          label: statusObj.label,
        };
      } else {
        this.status = false;
      }

      /*  */
      const synStatusObj = SYN_STATUS_MAP[synStatus];
      if (synStatusObj) {
        this.synStatus = {
          color: synStatusObj.color,
          label: synStatusObj.label,
        };
      } else {
        this.synStatus = false;
      }
    }
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.itemChange = changes['item'];
    this.handleitemChange();
  }
}
