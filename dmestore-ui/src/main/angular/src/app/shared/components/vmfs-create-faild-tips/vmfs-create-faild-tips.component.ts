import { Component, OnInit ,Input,AfterViewChecked} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-vmfs-create-faild-tips',
  templateUrl: './vmfs-create-faild-tips.component.html',
  styleUrls: ['./vmfs-create-faild-tips.component.scss']
})
export class VmfsCreateFaildTipsComponent implements OnInit ,AfterViewChecked{
@Input() partSuccessData:any;
  constructor(private translateService:TranslateService) { }
  failStatus:number;
  partSuccess:number;
  partFail:number;
  descriptionContent:string;
  connectionResult:[];
  detailShow:boolean;
  // detailShow:boolean=false;
  ngOnInit(): void {
      this.getFailStatus()
      this.getDescriptionContent()
    this.getFailDetail()

    // console.log(this.translateService.currentLang)
  }
  ngAfterViewChecked() {
    // console.log(this.translateService.currentLang)
    this.getDescriptionContent()
  }

  getFailStatus(){
      this.failStatus=this.partSuccessData.code==='206'?0:1;
      this.partSuccess=this.partSuccessData.data.successNo;
      this.partFail=this.partSuccessData.data.failNo;
  }
  getDescriptionContent(){
    if(this.partSuccessData && this.partSuccessData.data){
      this.descriptionContent=this.translateService.currentLang==='en-US'?
        this.partSuccessData.data.descriptionEN:this.partSuccessData.data.descriptionCN
      this.connectionResult=this.partSuccessData.data.connectionResult
    }
  }
  getFailDetail(){
    if(this.partSuccessData.data.descriptionCN||this.partSuccessData.data.descriptionEN){
      this.detailShow=true
    }else {this.detailShow=false}

  }

}
