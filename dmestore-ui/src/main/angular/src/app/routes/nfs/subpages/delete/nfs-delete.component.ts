import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsDeleteService} from "./nfs-delete.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-delete.component.html',
  styleUrls: ['./nfs-delete.component.scss'],
  providers: [GlobalsService,NfsDeleteService]
})
export class NfsDeleteComponent implements OnInit{

  constructor(private deleteService: NfsDeleteService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
  }

}
