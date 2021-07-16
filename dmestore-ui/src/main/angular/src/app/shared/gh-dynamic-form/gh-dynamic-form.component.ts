import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-gh-dynamic-form',
  templateUrl: './gh-dynamic-form.component.html',
  styleUrls: ['./gh-dynamic-form.component.scss'],
})
export class GhDynamicFormComponent implements OnInit {
  @Input() formInfo;
  @Input() form: FormGroup;
  @Output() onPayLoadChange: EventEmitter<any>;

  payLoad = '';

  constructor() {
    this.onPayLoadChange = new EventEmitter<any>();
  }

  handleItemValueChange(value) {
    this.onPayLoadChange.emit(value);
  }

  ngOnInit(): void {
  }
}
