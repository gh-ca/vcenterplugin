import { Component, OnInit ,Input} from '@angular/core';

@Component({
  selector: 'app-bp-repair-history-table',
  templateUrl: './bp-repair-history-table.component.html',
  styleUrls: ['./bp-repair-history-table.component.scss']
})
export class BpRepairHistoryTableComponent implements OnInit {
@Input() table
  constructor() { }

  ngOnInit(): void {
  }

}
