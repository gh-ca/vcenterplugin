import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-form-item-qos-group',
  templateUrl: './form-item-qos-group.component.html',
  styleUrls: ['./form-item-qos-group.component.scss'],
})
export class FormItemQosGroupComponent implements OnInit {
  @Output() onChange: EventEmitter<any>;

  constructor() {
    this.onChange = new EventEmitter();
  }

  ngOnInit(): void {}
}
