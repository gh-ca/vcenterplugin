import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@shared/shared.module';
import { NfsRoutingModule } from './nfs-routing.module';
import { NfsComponent } from './nfs.component';
import { LogicportComponent } from './logicport/logicport.component';
import { ShareComponent } from './share/share.component';
import { FileSystemComponent } from './file-system/file-system.component';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [NfsComponent, LogicportComponent, ShareComponent, FileSystemComponent],
  imports: [
    FormsModule,
    CommonModule,
    SharedModule,
    NfsRoutingModule
  ]
})
export class NfsModule { }
