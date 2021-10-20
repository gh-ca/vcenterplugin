import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-error-tips',
  templateUrl: './error-tips.component.html',
  styleUrls: ['./error-tips.component.scss'],
})
export class ErrorTipsComponent implements OnInit {
  @Input() isShow: boolean;
  @Input() tips: string;
  constructor() {}

  ngOnInit(): void {}
}
