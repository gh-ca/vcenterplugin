import { TreeNodeModel } from './tree-node.model';
export declare class DeclarativeTreeNodeModel<T> extends TreeNodeModel<T> {
    constructor(parent: DeclarativeTreeNodeModel<T> | null);
    parent: DeclarativeTreeNodeModel<T> | null;
    children: DeclarativeTreeNodeModel<T>[];
    _addChild(child: DeclarativeTreeNodeModel<T>): void;
    _removeChild(child: DeclarativeTreeNodeModel<T>): void;
    destroy(): void;
}
