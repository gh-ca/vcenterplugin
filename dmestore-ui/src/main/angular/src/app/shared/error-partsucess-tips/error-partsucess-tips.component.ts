import { Component, OnInit ,Input,AfterViewChecked} from '@angular/core';
import{ConnFaildData} from '../../routes/vmfs/list/list.service'

@Component({
  selector: 'app-error-partsucess-tips',
  templateUrl: './error-partsucess-tips.component.html',
  styleUrls: ['./error-partsucess-tips.component.scss']
})
export class ErrorPartsucessTipsComponent implements OnInit,AfterViewChecked{
@Input() status:string;
@Input() description:string;
@Input() partSuccessData:any;
@Input() operatingType;
// @Input() ConnFaildData:ConnFaildData[];

  errorOrPartSucess;
  constructor() { }

  ngOnInit(): void {
    this.getstatus()
    console.log(this.errorOrPartSucess)
  }
  ngAfterViewChecked() {
    this.getstatus()
  }

  getstatus(){
  this.errorOrPartSucess=this.status==='error'?0:1
  }
}
