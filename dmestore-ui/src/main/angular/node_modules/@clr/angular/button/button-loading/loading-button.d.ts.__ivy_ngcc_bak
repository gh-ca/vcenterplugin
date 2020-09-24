import { ElementRef, EventEmitter, Renderer2 } from '@angular/core';
import { ClrLoadingState } from '../../utils/loading/loading';
import { LoadingListener } from '../../utils/loading/loading-listener';
export declare class ClrLoadingButton implements LoadingListener {
    el: ElementRef;
    private renderer;
    buttonState: typeof ClrLoadingState;
    state: ClrLoadingState;
    disabled: boolean;
    clrLoadingChange: EventEmitter<ClrLoadingState>;
    constructor(el: ElementRef, renderer: Renderer2);
    loadingStateChange(state: ClrLoadingState): void;
    private setExplicitButtonWidth;
}
