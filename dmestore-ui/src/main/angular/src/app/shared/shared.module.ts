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
import { AlarmState } from 'app/routes/nfs/alarm-state.filter';
import { BpCellMulRowComponent } from './bp-cell-mul-row/bp-cell-mul-row.component';
import { StorageDjAzCellComponent } from './storage-dj-az-cell/storage-dj-az-cell.component';
import { FormItemQosGroupComponent } from './form-item-qos-group/form-item-qos-group.component';

const THIRD_MODULES = [MaterialModule, TranslateModule];
const COMPONENTS = [
  BreadcrumbComponent,
  PageHeaderComponent,
  ErrorCodeComponent,
  VmfsCreateFaildTipsComponent,
  /* 最佳实践MTU专属弹窗列表 */
  BpPanelListMtuComponent,
  /* 期望值多行 */
  BpCellMulRowComponent,
  /* 存储列表可用分区 */
  StorageDjAzCellComponent,
  /* qos策略 */
  FormItemQosGroupComponent,
];
const COMPONENTS_DYNAMIC = [];
const DIRECTIVES = [];
const PIPES = [];
const FILTER = [
  /* 状态 正常 告警 告示 */
  AlarmState,
];

@NgModule({
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, ...DIRECTIVES, ...PIPES, ...FILTER],
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule, ...THIRD_MODULES],
  exports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    ...THIRD_MODULES,
    ...COMPONENTS,
    ...FILTER,
    ...DIRECTIVES,
    ...PIPES,
  ],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class SharedModule {}
