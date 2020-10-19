import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsMountService} from "./nfs-mount.service";
@Component({
  selector: 'app-mount',
  templateUrl: './nfs-mount.component.html',
  styleUrls: ['./nfs-mount.component.scss'],
  providers: [GlobalsService,NfsMountService]
})
export class NfsMountComponent implements OnInit{

  constructor(private mountService: NfsMountService, private gs: GlobalsService){
  }
  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx[0]);
  }

}
