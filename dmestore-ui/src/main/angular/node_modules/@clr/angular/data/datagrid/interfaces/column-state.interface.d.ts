import { TemplateRef } from '@angular/core';
import { DatagridColumnChanges } from '../enums/column-changes.enum';
export interface ColumnState {
    changes?: DatagridColumnChanges[];
    width?: number;
    strictWidth?: number;
    hideable?: boolean;
    hidden?: boolean;
    titleTemplateRef?: TemplateRef<any>;
}
export interface ColumnStateDiff extends ColumnState {
    changes: DatagridColumnChanges[];
}
