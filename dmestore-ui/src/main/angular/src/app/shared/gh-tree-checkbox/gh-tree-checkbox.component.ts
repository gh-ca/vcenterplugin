import { Component, OnInit, Output, EventEmitter, Input, ChangeDetectorRef } from '@angular/core';
import { getSelectedFromTree, helper, VMFS_CLUSTER_NODE } from 'app/app.helpers';
import debounce from 'just-debounce';

@Component({
  selector: 'app-gh-tree-checkbox',
  templateUrl: './gh-tree-checkbox.component.html',
  styleUrls: ['./gh-tree-checkbox.component.scss'],
})
export class GhTreeCheckboxComponent implements OnInit {
  @Input() tree: VMFS_CLUSTER_NODE[];
  @Output() valueChange: EventEmitter<any>;

  helper = helper;
  emitValueChange(res: any) {}

  constructor(/* private cdr: ChangeDetectorRef */) {
    this.valueChange = new EventEmitter();
    (this.emitValueChange as any) = debounce(() => {
      this.valueChange.emit(getSelectedFromTree(this.tree));
    }, 300);
  }

  getChildren = (node: VMFS_CLUSTER_NODE) => node.children;

  ngOnInit(): void {}
}
