import { SimpleChange } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-storage-dj-az-cell',
  templateUrl: './storage-dj-az-cell.component.html',
  styleUrls: ['./storage-dj-az-cell.component.scss'],
})
export class StorageDjAzCellComponent implements OnInit {
  @Input() items;

  itemsChange;
  contentArray;

  constructor() {}

  ngOnInit(): void {}

  handleItemsChange() {
    const isItemsChange = this.itemsChange && Array.isArray(this.itemsChange.currentValue);
    if (isItemsChange) {
      this.contentArray = this.itemsChange.currentValue.map(i => (i.name ? i.name : '--'));
    } else {
      this.contentArray = ['--'];
    }
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.itemsChange = changes['items'];
    this.handleItemsChange();
  }
}
