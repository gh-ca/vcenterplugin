import {Component, OnInit} from '@angular/core';
import {LogicDetail, LogicportService} from './logicport.service';
import {GlobalsService} from '../../../shared/globals.service';

@Component({
  selector: 'app-logicport',
  templateUrl: './logicport.component.html',
  styleUrls: ['./logicport.component.scss'],
  providers: [ LogicportService, GlobalsService]
})
export class LogicportComponent implements OnInit {
  logicDetail: LogicDetail;
  constructor(private remoteSrv: LogicportService, private gs: GlobalsService) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    const objectId = '1234';
    // objectId=ctx[0].id;
    this.logicportDetail(objectId);
  }

  logicportDetail(objectId){
    this.remoteSrv.getData(objectId)
      .subscribe((result: any) => {
          this.logicDetail = result.data;
      });
  }
}
