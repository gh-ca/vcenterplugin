import { SimpleChange } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';
const SEPARATOR = '$$';

@Component({
  selector: 'app-bp-cell-mul-row',
  templateUrl: './bp-cell-mul-row.component.html',
  styleUrls: ['./bp-cell-mul-row.component.scss'],
})
export class BpCellMulRowComponent implements OnInit {
  @Input() items;

  itemsChange;
  contentArray;

  constructor() {}

  ngOnInit(): void {}

  handleItemsChange() {
    const isItemsChange =
      this.itemsChange &&
      typeof this.itemsChange.currentValue === 'string' &&
      this.itemsChange.currentValue;
    if (isItemsChange) {
      this.contentArray = this.itemsChange.currentValue.split(SEPARATOR);
    } else {
      this.contentArray = [this.items];
    }
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.itemsChange = changes['items'];
    this.handleItemsChange();
  }
}
