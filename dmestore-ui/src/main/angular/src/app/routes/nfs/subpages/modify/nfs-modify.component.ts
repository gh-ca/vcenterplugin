import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsModifyService} from "./nfs-modify.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [GlobalsService,NfsModifyService]
})
export class NfsModifyComponent implements OnInit{

  constructor(private deleteService: NfsModifyService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
  }

}
