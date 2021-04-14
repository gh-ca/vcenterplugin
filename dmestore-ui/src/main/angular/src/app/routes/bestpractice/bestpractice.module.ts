import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { BestpracticeRoutingModule } from './bestpractice-routing.module';

import { BestpracticeComponent } from './bestpractice.component';
import { TranslateModule } from '@ngx-translate/core';
import { ClrDatagridModule } from '@clr/angular';
import { BpPanelListMtuComponent } from './bp-panel-list-mtu/bp-panel-list-mtu.component';

const COMPONENTS = [BestpracticeComponent];
const COMPONENTS_DYNAMIC = [];
/* 添加针对 MTU 的弹窗组件  主机名 适配器 实际值  */
@NgModule({
  imports: [SharedModule, BestpracticeRoutingModule, TranslateModule, ClrDatagridModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, BpPanelListMtuComponent],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class BestpracticeModule { }
