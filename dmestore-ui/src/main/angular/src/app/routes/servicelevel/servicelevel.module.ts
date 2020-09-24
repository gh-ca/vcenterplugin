import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ServicelevelRoutingModule } from './servicelevel-routing.module';
import { ServicelevelComponent } from './servicelevel.component';
import {SharedModule} from '../../shared';


@NgModule({
  declarations: [ServicelevelComponent],
  imports: [
    CommonModule,
    SharedModule,
    ServicelevelRoutingModule
  ]
})
export class ServicelevelModule { }
