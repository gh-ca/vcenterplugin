import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  Input,
  ChangeDetectorRef,
  SimpleChange,
} from '@angular/core';
import { getSelectedFromTree, helper, VMFS_CLUSTER_NODE } from 'app/app.helpers';
import debounce from 'just-debounce';
import { getLodash } from '@shared/lib';
import { getVmfsCreateTreeFilterBySelect, getSelectedFromList } from './../../app.helpers';

const _ = getLodash();

@Component({
  selector: 'app-gh-tree-checkbox',
  templateUrl: './gh-tree-checkbox.component.html',
  styleUrls: ['./gh-tree-checkbox.component.scss'],
})
export class GhTreeCheckboxComponent implements OnInit {
  @Input() tree: VMFS_CLUSTER_NODE[];
  @Input() list: VMFS_CLUSTER_NODE[];
  @Input() isCreate: boolean;
  @Input() resType: string = 'normal';
  @Output() valueChange: EventEmitter<any>;
  treeValue;
  resValue;
  selectedHost = false;
  selectedCluster = false;

  get isList() {
    return _.isArray(this.list) && this.list.length > 0;
  }
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
      this.valueChange.emit(this.resValue);
    }, 300);
  }

  getChildren = (node: VMFS_CLUSTER_NODE) => node.children;

  ngOnInit(): void {}

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
    if (this.resType === 'mount') {
      let res = [];

      if (this.isList) {
        const listRes = getSelectedFromList(this.list);
        this.selectedHost = false;
        res = _.concat(res, listRes);
      }

      if (this.isTree) {
        const treeRes = getSelectedFromTree(this.tree, this.resType);
        this.selectedCluster = false;
        res = _.concat(res, treeRes);
      }

      return res;
    } else {
      /* 如果有host ，但凡选了单主机，主机集群就不考虑了*/
      if (this.isList) {
        const listRes = getSelectedFromList(this.list);
        this.selectedHost = listRes.length > 0;
        if (this.selectedHost) {
          return listRes;
        }
      }

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
}
