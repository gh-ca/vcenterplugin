import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AttributeService, VolumeInfo } from './attribute.service';
import { GlobalsService } from '@shared/globals.service';
import { handlerResponseErrorSimple } from 'app/app.helpers';
import { isMockData, mockData } from 'mock/mock';

@Component({
  selector: 'app-attribute',
  templateUrl: './attribute.component.html',
  styleUrls: ['./attribute.component.scss'],
  providers: [AttributeService],
})
export class AttributeComponent implements OnInit {
  // 卷信息
  volumeInfoList: VolumeInfo[];
  // 选中的卷数据
  selectVolume: VolumeInfo = new VolumeInfo();
  // 选中的卷名称
  selectVolName: string;
  // 卷名称集合
  volNames: string[] = [];
  isLoading = true;

  constructor(private attribute: AttributeService, private cdr: ChangeDetectorRef, private gs: GlobalsService) { }

  ngOnInit(): void {
    // this.gs.loading=true;
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    const objectId = ctx
      ? ctx[0].id
      : `urn:vmomi:Datastore:datastore-2049:674908e5-ab21-4079-9cb1-596358ee5dd1`;
    console.log('objectId:', objectId);
    this.getVolumeInfoByVolID(objectId);
  }

  getVolumeInfoByVolID(objectId: string) {
    console.log('objectId: ' + objectId);
    const handlerGetDataSuccess = (result: any) => {
      this.isLoading = false;
      if (result.code === '200') {
        this.volumeInfoList = result.data;
        this.volumeInfoList.forEach(item => {
          this.volNames.push(item.name);
        });
        // 设置默认选中数据
        this.selectVolName = this.volNames[0];
        this.selectVolume = this.getVolByName(this.selectVolName);
      } else {
        console.log(result.description);
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      console.log('this.selectVolName', this.selectVolume);
    };

    if (isMockData) {
      handlerGetDataSuccess(mockData.ATTRIBUTE_ACCESSVMFS_VOLUME);
    } else {
      this.attribute.getData(objectId).subscribe(handlerGetDataSuccess, handlerResponseErrorSimple);
    }
  }

  // 通过名称获取卷信息
  getVolByName(name): any {
    const volumeInfo = this.volumeInfoList.filter(item  => item.name === name)[0];
    return volumeInfo;
  }

  // 切换卷函数
  changeVolFunc() {
    console.log(this.selectVolName);
    this.selectVolume = this.getVolByName(this.selectVolName);
  }
}
