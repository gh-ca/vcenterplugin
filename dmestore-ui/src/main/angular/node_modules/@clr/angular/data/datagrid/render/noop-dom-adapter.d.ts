import { DomAdapter } from '../../../utils/dom-adapter/dom-adapter';
export declare class NoopDomAdapter implements DomAdapter {
    userDefinedWidth(element: any): number;
    scrollBarWidth(element: any): number;
    scrollWidth(element: any): number;
    computedHeight(element: any): number;
    clientRect(element: any): ClientRect;
    minWidth(element: any): number;
    focus(element: any): void;
}
