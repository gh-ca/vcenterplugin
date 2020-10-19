import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { VmfsListComponent } from './list/list.component';
import { AttributeComponent } from './volume-attribute/attribute.component';
import { PerformanceComponent } from './volume-performance/performance.component';
import {MountComponent} from "./mount/mount.component";

const routes: Routes = [
  { path: 'list', component: VmfsListComponent, data: { title: 'Vmfs List' } },
  { path: 'attribute', component: AttributeComponent, data: { title: 'Vmfs volumeAttribute' } },
  { path: 'performance', component: PerformanceComponent, data: { title: 'Vmfs volume-performance' } },
  { path: 'host/mount', component: MountComponent, data: { title: 'Vmfs host mount' } },
  { path: 'cluster/mount', component: MountComponent, data: { title: 'Vmfs cluster mount' } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VmfsRoutingModule {}
