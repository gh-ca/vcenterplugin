import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { RoutesRoutingModule } from './routes-routing.module';

import { DashboardComponent } from './dashboard/dashboard.component';
import { StorageComponent } from './storage/storage.component';
import { DetailComponent } from './storage/detail/detail.component';

const COMPONENTS = [DashboardComponent, StorageComponent];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  imports: [SharedModule, RoutesRoutingModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, DetailComponent],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class RoutesModule {}
