import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsUnmountService} from "./nfs-unmount.service";
@Component({
  selector: 'app-unmount',
  templateUrl: './nfs-unmount.component.html',
  styleUrls: ['./nfs-unmount.component.scss'],
  providers: [GlobalsService,NfsUnmountService]
})
export class NfsUnmountComponent implements OnInit{

  constructor(private unmountService: NfsUnmountService, private gs: GlobalsService){
  }
  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx[0]);
  }

}
