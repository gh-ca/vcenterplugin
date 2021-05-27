import { SimpleChange } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';
const CLASS_NAME = 'flex progress';

@Component({
  selector: 'app-progress-bar-cell',
  templateUrl: './progress-bar-cell.component.html',
  styleUrls: ['./progress-bar-cell.component.scss'],
})
export class ProgressBarCellComponent implements OnInit {
  @Input() color;
  className = CLASS_NAME;
  @Input() used;
  _used;

  changes;

  constructor() {}


  ngOnInit(): void {}

  usedChangeHandler() {
    if (this.changes['used']) {
      const used = this.changes['used'].currentValue;
      /*  */
      if (used) {
        this._used = used;
      } else {
        this._used = 0;
      }
    }
  }
  colorChangeHandler() {
    if (this.changes['color']) {
      const color = this.changes['color'].currentValue;
      this.className = `${CLASS_NAME} ${color ? color : ''}`;
    }
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.changes = changes;
    this.usedChangeHandler();
    this.colorChangeHandler();
  }
}
