import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { NfsComponent } from './nfs.component';
import {LogicportComponent} from './logicport/logicport.component';
import {ShareComponent} from './share/share.component';
import {FileSystemComponent} from './file-system/file-system.component';

const routes: Routes = [
  { path: '', component: NfsComponent },
  { path: 'logicport', component: LogicportComponent },
  { path: 'share', component: ShareComponent },
  { path: 'fs', component: FileSystemComponent }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NfsRoutingModule { }
