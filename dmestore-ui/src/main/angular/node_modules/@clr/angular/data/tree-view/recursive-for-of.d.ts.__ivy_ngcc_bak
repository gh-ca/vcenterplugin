import { ChangeDetectorRef, OnChanges, OnDestroy, TemplateRef } from '@angular/core';
import { TreeFeaturesService } from './tree-features.service';
import { TreeNodeModel } from './models/tree-node.model';
import { AsyncArray } from './models/async-array';
export interface ClrRecursiveForOfContext<T> {
    $implicit: T;
    clrModel: TreeNodeModel<T>;
}
export declare class ClrRecursiveForOf<T> implements OnChanges, OnDestroy {
    private template;
    private featuresService;
    private cdr;
    constructor(template: TemplateRef<ClrRecursiveForOfContext<T>>, featuresService: TreeFeaturesService<T>, cdr: ChangeDetectorRef);
    nodes: T | T[];
    getChildren: (node: T) => AsyncArray<T>;
    private childrenFetchSubscription;
    ngOnChanges(): void;
    ngOnDestroy(): void;
}
