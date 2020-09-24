import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SharedModule} from '../../shared';

import { RdmRoutingModule } from './rdm-routing.module';
import { RdmComponent } from './rdm.component';


@NgModule({
  declarations: [RdmComponent],
  imports: [
    CommonModule,
    SharedModule,
    RdmRoutingModule
  ]
})
export class RdmModule { }
