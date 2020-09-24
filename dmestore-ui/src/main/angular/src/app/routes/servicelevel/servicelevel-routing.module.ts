import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ServicelevelComponent } from './servicelevel.component';

const routes: Routes = [{ path: '', component: ServicelevelComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServicelevelRoutingModule { }
