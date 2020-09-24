import { ElementRef, EventEmitter, OnDestroy, Renderer2 } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { DomAdapter } from '../../../utils/dom-adapter/dom-adapter';
import { ColumnResizerService } from '../providers/column-resizer.service';
import { DatagridRenderOrganizer } from './render-organizer';
import { ColumnState } from '../interfaces/column-state.interface';
import { ColumnsService } from '../providers/columns.service';
export declare class DatagridHeaderRenderer implements OnDestroy {
    private el;
    private renderer;
    private organizer;
    private domAdapter;
    private columnResizerService;
    private columnsService;
    private columnState;
    constructor(el: ElementRef, renderer: Renderer2, organizer: DatagridRenderOrganizer, domAdapter: DomAdapter, columnResizerService: ColumnResizerService, columnsService: ColumnsService, columnState: BehaviorSubject<ColumnState>);
    resizeEmitter: EventEmitter<number>;
    /**
     * Indicates if the column has a strict width, so it doesn't shrink or expand based on the content.
     */
    private widthSet;
    private autoSet;
    private subscriptions;
    ngOnDestroy(): void;
    private stateChanges;
    private clearWidth;
    private detectStrictWidth;
    private computeWidth;
    getColumnWidthState(): Partial<ColumnState>;
    setColumnState(index: number): void;
    private setWidth;
    private setHidden;
}
