import { ElementRef, EventEmitter, OnDestroy, AfterViewInit } from '@angular/core';
import { ClrPopoverEventsService } from './providers/popover-events.service';
import { ClrPopoverToggleService } from './providers/popover-toggle.service';
export declare class ClrPopoverCloseButton implements OnDestroy, AfterViewInit {
    private elementRef;
    private smartEventsService;
    private smartOpenService;
    private subscriptions;
    constructor(elementRef: ElementRef, smartEventsService: ClrPopoverEventsService, smartOpenService: ClrPopoverToggleService);
    closeChange: EventEmitter<void>;
    handleClick(event: MouseEvent): void;
    ngAfterViewInit(): void;
    ngOnDestroy(): void;
}
