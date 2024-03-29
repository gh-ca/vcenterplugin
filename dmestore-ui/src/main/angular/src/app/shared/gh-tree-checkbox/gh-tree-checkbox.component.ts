import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  Input,
  ChangeDetectorRef,
  SimpleChange,
  AfterViewChecked
} from '@angular/core';
import { getSelectedFromTree, helper, VMFS_CLUSTER_NODE ,vmfsGetSelectedFromTree} from 'app/app.helpers';
import debounce from 'just-debounce';
import { getLodash } from '@shared/lib';
import { getVmfsCreateTreeFilterBySelect, getSelectedFromList } from './../../app.helpers';

const _ = getLodash();

@Component({
  selector: 'app-gh-tree-checkbox',
  templateUrl: './gh-tree-checkbox.component.html',
  styleUrls: ['./gh-tree-checkbox.component.scss'],
})
export class GhTreeCheckboxComponent implements OnInit ,AfterViewChecked{
  @Input() tree: VMFS_CLUSTER_NODE[];
  // @Input() list: VMFS_CLUSTER_NODE[];
  @Input() isCreate: boolean;
  @Input() resType: string = 'normal';
  @Input() mountType:string;
  @Input() vmfsMount:boolean;
  @Input() checkNullData:boolean=false;
  @Output() valueChange: EventEmitter<any>;
  treeValue;
  resValue;
  selectedHost = false;
  selectedCluster = false;
  clusterInHostMount=false;
  treeLoading=false;
  nullData=false;

  // get isList() {
  //   return _.isArray(this.list) && this.list.length > 0;
  // }
  get isTree() {
    return _.isArray(this.tree) && this.tree.length > 0;
  }

  helper = helper;
  emitValueChange(res: any) {}

  constructor(private cdr: ChangeDetectorRef) {
    this.valueChange = new EventEmitter();
    (this.emitValueChange as any) = debounce(() => {
      /*
      1单主机
      2集群主机
      check是否是选择了单主机
      还是集群
      */

      this.resValue = this.checkSelectedAndSetDisable();
      this.resValue=this.resValue.map(i=>({
        ...i,
        deviceId:i.clusterId,
        deviceName:i.clusterName,

      }));
      this.valueChange.emit(this.resValue);
    }, 300);
  }

  getChildren = (node: VMFS_CLUSTER_NODE) => node.children;

  ngOnInit(): void {
    // console.log(this.mountType)

  }
  ngAfterViewChecked() {
    this.checkMountType()
    this.checkTreeLength()
    this.checkTreeData()
  }

  // ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
  //   const tree: any = changes['tree'] || {};
  //   const treeValue = tree.currentValue || [];
  //   if (_.isArray(treeValue) && treeValue.length > 0) {
  //     this.treeValue = treeValue;
  //   }
  //   console.log('tree', tree);
  // }



  checkSelectedAndSetDisable() {
    /* 挂载可以跨集群 */
    if (this.resType === 'mount'||this.resType==='unMount') {
      let res = [];

      // if (this.isList) {
      //   const listRes = getSelectedFromList(this.list);
      //   this.selectedHost = false;
      //   res = _.concat(res, listRes);
      // }
      if (this.vmfsMount===true){
        if (this.isTree) {
          const treeRes = vmfsGetSelectedFromTree(this.tree,this.mountType);
          this.selectedCluster = false;
          res = _.concat(res, treeRes);
        }
      }else {
        if (this.isTree) {
          const treeRes = getSelectedFromTree(this.tree, this.resType,this.mountType);
          this.selectedCluster = false;
          res = _.concat(res, treeRes);
        }
      }


      return res;
    } else {
      /* 如果有host ，但凡选了单主机，主机集群就不考虑了*/
      // if (this.isList) {
      //   const listRes = getSelectedFromList(this.list);
      //   this.selectedHost = listRes.length > 0;
      //   if (this.selectedHost) {
      //     return listRes;
      //   }
      // }

      /* 主机集群 */
      if (this.isTree) {
        const treeRes = getSelectedFromTree(this.tree);
        this.selectedCluster = treeRes.length > 0;
        if (this.selectedCluster) {
          return treeRes;
        }
      }
    }
    this.cdr.detectChanges();
    return [];
  }

//  当前树是否有长度，判断是否展示数据加载模态框
  checkTreeLength(){
    if(this.tree&&this.tree.length>0){
     this.treeLoading=false
    }else {
      this.treeLoading=true
    }
  }
//  挂载：当前挂载类型为主机时，集群置灰，只能选择主机进行挂载
  checkMountType(){
    if (this.mountType==='host'&&this.resType==='mount'){
      // debugger
      if(this.isTree){
        for (let firstNode of this.tree){
          if ((firstNode as any).deviceType==='cluster'){
            //  设置集群置灰
            (firstNode as any).isDisabled=true
          }
        }
    }
      }
    }
//判断当前树返回数据是否为空，为空则显示无数据提示，并关闭模态框
  checkTreeData(){
    if (this.checkNullData){
      this.treeLoading=false
      this.nullData=true
    }
  }
}
