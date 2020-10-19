import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { VmfsRoutingModule } from './vmfs-routing.module';

import { VmfsListComponent } from './list/list.component';
import { AttributeComponent } from './volume-attribute/attribute.component';
import { PerformanceComponent } from './volume-performance/performance.component';
import { MountComponent } from './mount/mount.component';
import { NgxEchartsModule } from 'ngx-echarts';
import {FormsModule} from "@angular/forms";
import {ClarityModule} from "@clr/angular";
import {CommonModule} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";

const COMPONENTS = [VmfsListComponent];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  imports: [SharedModule, VmfsRoutingModule, NgxEchartsModule, FormsModule, ClarityModule, CommonModule, TranslateModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, AttributeComponent, PerformanceComponent, MountComponent],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class VmfsModule {}
