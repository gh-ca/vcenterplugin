import { Component, OnInit ,Input} from '@angular/core';

@Component({
  selector: 'app-bp-repair-history-table',
  templateUrl: './bp-repair-history-table.component.html',
  styleUrls: ['./bp-repair-history-table.component.scss']
})
export class BpRepairHistoryTableComponent implements OnInit {
@Input() tableData
  constructor() { }

  ngOnInit(): void {
  }

  sortFunc(obj: any) {
    return !obj;
  }
}
export interface bpTableData{
  "objectName":string,
  "violationValue":string,
  "recommendedValue":string,
  "repairType":string,
  "repairTime":string,
  "repairResult":string,
  "executionLog":string
}
