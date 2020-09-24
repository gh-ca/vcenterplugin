import { ElementRef, NgZone, OnDestroy, Renderer2 } from '@angular/core';
import { DragEventListenerService } from './providers/drag-event-listener.service';
import { DraggableSnapshotService } from './providers/draggable-snapshot.service';
export declare class ClrDraggableGhost<T> implements OnDestroy {
    private el;
    private dragEventListener;
    private draggableSnapshot;
    private renderer;
    private ngZone;
    private draggableGhostEl;
    private subscriptions;
    leaveAnimConfig: {
        value: number;
        params: {
            top: string;
            left: string;
        };
    };
    constructor(el: ElementRef, dragEventListener: DragEventListenerService<T>, draggableSnapshot: DraggableSnapshotService<T>, renderer: Renderer2, ngZone: NgZone);
    private setDefaultGhostSize;
    private animateToOnLeave;
    private findTopLeftPosition;
    private findDropPointPosition;
    private setSizeStyle;
    private setPositionStyle;
    ngOnDestroy(): void;
}
