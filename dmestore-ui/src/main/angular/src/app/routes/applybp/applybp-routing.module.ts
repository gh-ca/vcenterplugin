import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ApplybpComponent } from './applybp.component';

const routes: Routes = [
  { path: 'host/applybp', component: ApplybpComponent },
  { path: 'cluster/applybp', component: ApplybpComponent }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplybpRoutingModule { }
