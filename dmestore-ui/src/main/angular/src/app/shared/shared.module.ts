import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MaterialModule } from '../material.module';
import { TranslateModule } from '@ngx-translate/core';

import { BreadcrumbComponent } from './components/breadcrumb/breadcrumb.component';
import { PageHeaderComponent } from './components/page-header/page-header.component';
import { ErrorCodeComponent } from './components/error-code/error-code.component';
/*  */
import { BpPanelListMtuComponent } from './../routes/bestpractice/bp-panel-list-mtu/bp-panel-list-mtu.component';
import { VmfsCreateFaildTipsComponent } from './components/vmfs-create-faild-tips/vmfs-create-faild-tips.component';

const THIRD_MODULES = [
  MaterialModule,
  TranslateModule,
];
const COMPONENTS = [BreadcrumbComponent, PageHeaderComponent, ErrorCodeComponent, VmfsCreateFaildTipsComponent];
const COMPONENTS_DYNAMIC = [];
const DIRECTIVES = [];
const PIPES = [];

@NgModule({
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, ...DIRECTIVES, ...PIPES, BpPanelListMtuComponent],
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule, ...THIRD_MODULES],
  exports: [
    BpPanelListMtuComponent,
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    ...THIRD_MODULES,
    ...COMPONENTS,
    ...DIRECTIVES,
    ...PIPES,
  ],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class SharedModule { }
