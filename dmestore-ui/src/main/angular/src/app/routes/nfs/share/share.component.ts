import {Component, OnInit} from '@angular/core';
import {ShareDetail, ShareService} from './share.service';
import {GlobalsService} from '../../../shared/globals.service';

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.scss'],
  providers: [ShareService, GlobalsService]
})
export class ShareComponent implements OnInit {
  shareDetail: ShareDetail;
  isLoading = true;
  constructor(private remoteSrv: ShareService, private gs: GlobalsService) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    this.getShareDetail(ctx[0].id);
    // this.getShareDetail('urn:vmomi:Datastore:datastore-12024:674908e5-ab21-4079-9cb1-596358ee5dd1');
  }
  getShareDetail(objectId){
    this.remoteSrv.getData(objectId)
      .subscribe((result: any) => {
        this.isLoading = false;
       this.shareDetail = result.data;
       console.log('shareDetail:');
       console.log(this.shareDetail);
      });
  }
}
