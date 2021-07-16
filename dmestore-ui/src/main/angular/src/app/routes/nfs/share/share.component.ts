import {Component, OnInit,AfterViewChecked,ChangeDetectorRef} from '@angular/core';
import {ShareDetail, ShareService} from './share.service';
import {GlobalsService} from '../../../shared/globals.service';
import {isMockData} from "../../../../mock/mock";
import {TranslateService} from '@ngx-translate/core'

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.scss'],
  providers: [ShareService, GlobalsService]
})
export class ShareComponent implements OnInit ,AfterViewChecked{
  shareDetail: ShareDetail;
  isLoading = true;
  constructor(private remoteSrv: ShareService, private gs: GlobalsService,private translateService:TranslateService,private cdr:ChangeDetectorRef) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    this.getShareDetail(ctx[0].id);
    // this.getShareDetail('urn:vmomi:Datastore:datastore-12024:674908e5-ab21-4079-9cb1-596358ee5dd1');
  }
  ngAfterViewChecked(){
    this.getReadOnlyOrReadOrWrite()
  }
  getShareDetail(objectId){
    this.remoteSrv.getData(objectId)
      .subscribe((result: any) => {
        if(isMockData){
          this.isLoading=false;
          this.shareDetail=
          {"fsName":"zg_69的好地方大概v梵蒂冈v豆腐干反对的更好的风格",
            "name":"/zg_69",
            "sharePath":"/zg_69的好地方大概v梵蒂冈v豆腐干反对的更好的风格/",
            "description":"",
            "owningDtreeId":null,"owningDtreeName":null,
            "deviceName":"Huawei.Storage",
            "authClientList":
              [{"accessval":"read/write",
                "id":"4A82C62F7971394BB0F49DEA0CFCCEFC",
                "name":"10.143.133.17",
                "secure":null,
                "sync":null,
                "type":"Network group",
                "clientIdInStorage":"668"
              },
              { "accessval":"read-only",
                "id":"4A82C62F7971394BB0F49DEA0CFCCEFC",
                "name":"10.143.133.196",
                "secure":null,
                "sync":null,
                "type":"Host Name or IP Address",
                "clientIdInStorage":"669"
              }
              ]
          }
          // console.log(this.shareDetail)
        }
       else {
          this.isLoading = false;
          this.shareDetail = result.data;
          console.log('shareDetail:');
          console.log(this.shareDetail);
        }
      });
    this.cdr.detectChanges();
  }

//  authClientList中read-only国际化
  getReadOnlyOrReadOrWrite(){
    if (this.shareDetail){
      if (this.translateService.currentLang==='en-US'){
        for (let i=0;i< this.shareDetail.authClientList.length;i++){
          let item=this.shareDetail.authClientList[i]
          item.accessval=(item.accessval==="read-only"||item.accessval==="只读")?'read-only':'read/write'
          item.type=(item.type==="Host Name or IP Address"||item.type==="主机")?'Host Name or IP Address':'Network group'
        }
      }else {
        for (let i=0;i< this.shareDetail.authClientList.length;i++){
          let item=this.shareDetail.authClientList[i]
          item.accessval=(item.accessval==="read-only"||item.accessval==="只读")?'只读':'读写'
          item.type=(item.type==="Host Name or IP Address"||item.type==="主机")?'主机':'网络组'

        }
      }
    }
    this.cdr.detectChanges();
  }

}
