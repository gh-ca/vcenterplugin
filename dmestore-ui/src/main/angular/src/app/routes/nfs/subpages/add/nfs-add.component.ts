import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsAddService} from "./nfs-add.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-add.component.html',
  styleUrls: ['./nfs-add.component.scss'],
  providers: [GlobalsService,NfsAddService]
})
export class NfsAddComponent implements OnInit{

  constructor(private addService: NfsAddService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
  }

}
