import { Component, OnInit } from '@angular/core';
import {FileSystemService, FsDetail} from './file-system.service';
import {GlobalsService} from '../../../shared/globals.service';
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-file-system',
  templateUrl: './file-system.component.html',
  styleUrls: ['./file-system.component.scss'],
  providers: [FileSystemService, GlobalsService,TranslatePipe]
})
export class FileSystemComponent implements OnInit {
  fsDetails: FsDetail[];
  fsDetail: FsDetail = new FsDetail();
  fsNames: string[] = [];
  defaultSelect: string;
  constructor(private fsService: FileSystemService, private gs: GlobalsService, private translatePipe:TranslatePipe) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
   this.getFsDetail(ctx[0].id);
   //this.getFsDetail('urn:vmomi:Datastore:datastore-6019:674908e5-ab21-4079-9cb1-596358ee5dd1');
  }
  getFsDetail(objectId){
    this.fsService.getData(objectId).subscribe((result: any) => {
      this.fsDetails = result.data;
      this.fsDetails.forEach(f => {
        this.fsNames.push(f.name);
      });
      this.defaultSelect = this.fsNames[0];
      this.fsDetail = this.getFsDetailByName(this.defaultSelect);
    });
  }

  getFsDetailByName(name): any {
    const detail = this.fsDetails.filter(item => item.name === name)[0];
    if (detail.applicationScenario=="database"){
      detail.applicationScenario=this.translatePipe.transform("nfs.fs.database");
    }else if(detail.applicationScenario=="vm"){
      detail.applicationScenario=this.translatePipe.transform("nfs.fs.vm");
    }else if(detail.applicationScenario=="user_defined"){
      detail.applicationScenario=this.translatePipe.transform("nfs.fs.userDefine");
    }
    return detail;
  }
  changeFs(){
    this.fsDetail = this.getFsDetailByName(this.defaultSelect);
  }
}
