import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { BestpracticeRoutingModule } from './bestpractice-routing.module';

import { BestpracticeComponent } from './bestpractice.component';
import { TranslateModule } from '@ngx-translate/core';
import { ClrDatagridModule } from '@clr/angular';
import { BpPanelListMtuComponent } from './bp-panel-list-mtu/bp-panel-list-mtu.component';

const COMPONENTS = [BestpracticeComponent];
const COMPONENTS_DYNAMIC = [];
/* ������ MTU �ĵ������  ������ ������ ʵ��ֵ  */
@NgModule({
  imports: [SharedModule, BestpracticeRoutingModule, TranslateModule, ClrDatagridModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, BpPanelListMtuComponent],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class BestpracticeModule { }
