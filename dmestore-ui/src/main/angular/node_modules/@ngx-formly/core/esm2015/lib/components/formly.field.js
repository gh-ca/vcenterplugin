/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, EventEmitter, Input, Output, ViewContainerRef, ViewChild, Attribute, ComponentFactoryResolver, Renderer2, ElementRef, } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyConfig } from '../services/formly.config';
import { defineHiddenProp, wrapProperty } from '../utils';
import { isObservable } from 'rxjs';
export class FormlyField {
    /**
     * @param {?} formlyConfig
     * @param {?} renderer
     * @param {?} resolver
     * @param {?} elementRef
     * @param {?} hideDeprecation
     */
    constructor(formlyConfig, renderer, resolver, elementRef, 
    // tslint:disable-next-line
    hideDeprecation) {
        this.formlyConfig = formlyConfig;
        this.renderer = renderer;
        this.resolver = resolver;
        this.elementRef = elementRef;
        this.warnDeprecation = false;
        this.modelChange = new EventEmitter();
        this.hostObservers = [];
        this.componentRefs = [];
        this.hooksObservers = [];
        this.warnDeprecation = hideDeprecation === null;
    }
    /**
     * @param {?} m
     * @return {?}
     */
    set model(m) {
        this.warnDeprecation && console.warn(`NgxFormly: passing 'model' input to '${this.constructor.name}' component is not required anymore, you may remove it!`);
    }
    /**
     * @param {?} form
     * @return {?}
     */
    set form(form) {
        this.warnDeprecation && console.warn(`NgxFormly: passing 'form' input to '${this.constructor.name}' component is not required anymore, you may remove it!`);
    }
    /**
     * @param {?} options
     * @return {?}
     */
    set options(options) {
        this.warnDeprecation && console.warn(`NgxFormly: passing 'options' input to '${this.constructor.name}' component is not required anymore, you may remove it!`);
    }
    /**
     * @return {?}
     */
    ngAfterContentInit() {
        this.triggerHook('afterContentInit');
    }
    /**
     * @return {?}
     */
    ngAfterContentChecked() {
        this.triggerHook('afterContentChecked');
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        this.triggerHook('afterViewInit');
    }
    /**
     * @return {?}
     */
    ngAfterViewChecked() {
        this.triggerHook('afterViewChecked');
    }
    /**
     * @return {?}
     */
    ngDoCheck() {
        this.triggerHook('doCheck');
    }
    /**
     * @return {?}
     */
    ngOnInit() {
        this.triggerHook('onInit');
    }
    /**
     * @param {?} changes
     * @return {?}
     */
    ngOnChanges(changes) {
        this.triggerHook('onChanges', changes);
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.resetRefs(this.field);
        this.hostObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        unsubscribe => unsubscribe()));
        this.hooksObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        unsubscribe => unsubscribe()));
        this.triggerHook('onDestroy');
    }
    /**
     * @private
     * @param {?} containerRef
     * @param {?} f
     * @param {?} wrappers
     * @return {?}
     */
    renderField(containerRef, f, wrappers) {
        if (this.containerRef === containerRef) {
            this.resetRefs(this.field);
            this.containerRef.clear();
        }
        if (wrappers && wrappers.length > 0) {
            const [wrapper, ...wps] = wrappers;
            const { component } = this.formlyConfig.getWrapper(wrapper);
            /** @type {?} */
            const ref = containerRef.createComponent(this.resolver.resolveComponentFactory(component));
            this.attachComponentRef(ref, f);
            wrapProperty(ref.instance, 'fieldComponent', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ firstChange, previousValue, currentValue }) => {
                if (currentValue) {
                    /** @type {?} */
                    const viewRef = previousValue ? previousValue.detach() : null;
                    if (viewRef && !viewRef.destroyed) {
                        currentValue.insert(viewRef);
                    }
                    else {
                        this.renderField(currentValue, f, wps);
                    }
                    !firstChange && ref.changeDetectorRef.detectChanges();
                }
            }));
        }
        else if (f && f.type) {
            const { component } = this.formlyConfig.getType(f.type);
            /** @type {?} */
            const ref = containerRef.createComponent(this.resolver.resolveComponentFactory(component));
            this.attachComponentRef(ref, f);
        }
    }
    /**
     * @private
     * @param {?} name
     * @param {?=} changes
     * @return {?}
     */
    triggerHook(name, changes) {
        if (this.field && this.field.hooks && this.field.hooks[name]) {
            if (!changes || changes.field) {
                /** @type {?} */
                const r = this.field.hooks[name](this.field);
                if (isObservable(r) && ['onInit', 'afterContentInit', 'afterViewInit'].indexOf(name) !== -1) {
                    /** @type {?} */
                    const sub = r.subscribe();
                    this.hooksObservers.push((/**
                     * @return {?}
                     */
                    () => sub.unsubscribe()));
                }
            }
        }
        if (this.field && this.field.lifecycle && this.field.lifecycle[name]) {
            this.field.lifecycle[name](this.field.form, this.field, this.field.model, this.field.options);
        }
        if (name === 'onChanges' && changes.field) {
            this.renderHostBinding();
            this.resetRefs(changes.field.previousValue);
            this.renderField(this.containerRef, this.field, this.field ? this.field.wrappers : []);
        }
    }
    /**
     * @private
     * @template T
     * @param {?} ref
     * @param {?} field
     * @return {?}
     */
    attachComponentRef(ref, field) {
        this.componentRefs.push(ref);
        field._componentRefs.push(ref);
        Object.assign(ref.instance, { field });
    }
    /**
     * @private
     * @return {?}
     */
    renderHostBinding() {
        if (!this.field) {
            return;
        }
        this.hostObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        unsubscribe => unsubscribe()));
        this.hostObservers = [
            wrapProperty(this.field, 'hide', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ firstChange, currentValue }) => {
                if (!firstChange || (firstChange && currentValue)) {
                    this.renderer.setStyle(this.elementRef.nativeElement, 'display', currentValue ? 'none' : '');
                }
            })),
            wrapProperty(this.field, 'className', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ firstChange, currentValue }) => {
                if (!firstChange || (firstChange && currentValue)) {
                    this.renderer.setAttribute(this.elementRef.nativeElement, 'class', currentValue);
                }
            })),
        ];
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    resetRefs(field) {
        if (field) {
            if (field._componentRefs) {
                field._componentRefs = field._componentRefs.filter((/**
                 * @param {?} ref
                 * @return {?}
                 */
                ref => this.componentRefs.indexOf(ref) === -1));
            }
            else {
                defineHiddenProp(this.field, '_componentRefs', []);
            }
        }
        this.componentRefs = [];
    }
}
FormlyField.decorators = [
    { type: Component, args: [{
                selector: 'formly-field',
                template: `<ng-template #container></ng-template>`
            }] }
];
/** @nocollapse */
FormlyField.ctorParameters = () => [
    { type: FormlyConfig },
    { type: Renderer2 },
    { type: ComponentFactoryResolver },
    { type: ElementRef },
    { type: undefined, decorators: [{ type: Attribute, args: ['hide-deprecation',] }] }
];
FormlyField.propDecorators = {
    field: [{ type: Input }],
    model: [{ type: Input }],
    form: [{ type: Input }],
    options: [{ type: Input }],
    modelChange: [{ type: Output }],
    containerRef: [{ type: ViewChild, args: ['container', (/** @type {?} */ ({ read: ViewContainerRef, static: true })),] }]
};
if (false) {
    /** @type {?} */
    FormlyField.prototype.field;
    /** @type {?} */
    FormlyField.prototype.warnDeprecation;
    /** @type {?} */
    FormlyField.prototype.modelChange;
    /** @type {?} */
    FormlyField.prototype.containerRef;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.hostObservers;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.componentRefs;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.hooksObservers;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.formlyConfig;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.renderer;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.resolver;
    /**
     * @type {?}
     * @private
     */
    FormlyField.prototype.elementRef;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZpZWxkLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvY29yZS8iLCJzb3VyY2VzIjpbImxpYi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUNMLFNBQVMsRUFBRSxZQUFZLEVBQUUsS0FBSyxFQUFFLE1BQU0sRUFDdEMsZ0JBQWdCLEVBQUUsU0FBUyxFQUErQixTQUFTLEVBQUUsd0JBQXdCLEVBQ2tCLFNBQVMsRUFBRSxVQUFVLEdBQ3JJLE1BQU0sZUFBZSxDQUFDO0FBQ3ZCLE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUMzQyxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0sMkJBQTJCLENBQUM7QUFFekQsT0FBTyxFQUFFLGdCQUFnQixFQUFFLFlBQVksRUFBRSxNQUFNLFVBQVUsQ0FBQztBQUcxRCxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0sTUFBTSxDQUFDO0FBTXBDLE1BQU0sT0FBTyxXQUFXOzs7Ozs7OztJQXdCdEIsWUFDVSxZQUEwQixFQUMxQixRQUFtQixFQUNuQixRQUFrQyxFQUNsQyxVQUFzQjtJQUM5QiwyQkFBMkI7SUFDSSxlQUFlO1FBTHRDLGlCQUFZLEdBQVosWUFBWSxDQUFjO1FBQzFCLGFBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsYUFBUSxHQUFSLFFBQVEsQ0FBMEI7UUFDbEMsZUFBVSxHQUFWLFVBQVUsQ0FBWTtRQXpCaEMsb0JBQWUsR0FBRyxLQUFLLENBQUM7UUFjZCxnQkFBVyxHQUFzQixJQUFJLFlBQVksRUFBRSxDQUFDO1FBR3RELGtCQUFhLEdBQWUsRUFBRSxDQUFDO1FBQy9CLGtCQUFhLEdBQVUsRUFBRSxDQUFDO1FBQzFCLG1CQUFjLEdBQWUsRUFBRSxDQUFDO1FBVXRDLElBQUksQ0FBQyxlQUFlLEdBQUcsZUFBZSxLQUFLLElBQUksQ0FBQztJQUNsRCxDQUFDOzs7OztJQTVCRCxJQUFhLEtBQUss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QUFJLEtBQUssRUFBRTtZQUNULElBQUksS0FBSyxDQUFDLGNBQWMsRUFBRTtnQkFDeEIsS0FBSyxDQUFDLGNBQWMsR0FBRyxLQUFLLENBQUMsY0FBYyxDQUFDLE1BQU07Ozs7Z0JBQUMsR0FBRyxDQUFDLEVBQUUsQ0FBQyxJQUFJLENBQUMsYUFBYSxDQUFDLE9BQU8sQ0FBQyxHQUFHLENBQUMsS0FBSyxDQUFDLENBQUMsRUFBQyxDQUFDO2FBQ25HO2lCQUFNO2dCQUNMLGdCQUFnQixDQUFDLElBQUksQ0FBQyxLQUFLLEVBQUUsZ0JBQWdCLEVBQUUsRUFBRSxDQUFDLENBQUM7YUFDcEQ7U0FDRjtRQUVELElBQUksQ0FBQyxhQUFhLEdBQUcsRUFBRSxDQUFDO0lBQzFCLENBQUM7OztZQXhLRixTQUFTLFNBQUM7Z0JBQ1QsUUFBUSxFQUFFLGNBQWM7Z0JBQ3hCLFFBQVEsRUFBRSx3Q0FBd0M7YUFDbkQ7Ozs7WUFWUSxZQUFZO1lBSDRGLFNBQVM7WUFEbkQsd0JBQXdCO1lBQzZCLFVBQVU7NENBNENqSSxTQUFTLFNBQUMsa0JBQWtCOzs7b0JBN0I5QixLQUFLO29CQUlMLEtBQUs7bUJBSUwsS0FBSztzQkFJTCxLQUFLOzBCQUlMLE1BQU07MkJBRU4sU0FBUyxTQUFDLFdBQVcsRUFBRSxtQkFBTSxFQUFDLElBQUksRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLEVBQUUsSUFBSSxFQUFFLEVBQUE7Ozs7SUFsQnJFLDRCQUFrQzs7SUFFbEMsc0NBQXdCOztJQWN4QixrQ0FBOEQ7O0lBRTlELG1DQUFzRzs7Ozs7SUFDdEcsb0NBQXVDOzs7OztJQUN2QyxvQ0FBa0M7Ozs7O0lBQ2xDLHFDQUF3Qzs7Ozs7SUFHdEMsbUNBQWtDOzs7OztJQUNsQywrQkFBMkI7Ozs7O0lBQzNCLCtCQUEwQzs7Ozs7SUFDMUMsaUNBQThCIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHtcbiAgQ29tcG9uZW50LCBFdmVudEVtaXR0ZXIsIElucHV0LCBPdXRwdXQsXG4gIFZpZXdDb250YWluZXJSZWYsIFZpZXdDaGlsZCwgQ29tcG9uZW50UmVmLCBTaW1wbGVDaGFuZ2VzLCBBdHRyaWJ1dGUsIENvbXBvbmVudEZhY3RvcnlSZXNvbHZlcixcbiAgT25Jbml0LCBPbkNoYW5nZXMsIE9uRGVzdHJveSwgRG9DaGVjaywgQWZ0ZXJDb250ZW50SW5pdCwgQWZ0ZXJDb250ZW50Q2hlY2tlZCwgQWZ0ZXJWaWV3SW5pdCwgQWZ0ZXJWaWV3Q2hlY2tlZCwgUmVuZGVyZXIyLCBFbGVtZW50UmVmLFxufSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1Hcm91cCB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZyB9IGZyb20gJy4uL3NlcnZpY2VzL2Zvcm1seS5jb25maWcnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seUZvcm1PcHRpb25zLCBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlIH0gZnJvbSAnLi9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IGRlZmluZUhpZGRlblByb3AsIHdyYXBQcm9wZXJ0eSB9IGZyb20gJy4uL3V0aWxzJztcbmltcG9ydCB7IEZpZWxkV3JhcHBlciB9IGZyb20gJy4uL3RlbXBsYXRlcy9maWVsZC53cmFwcGVyJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJy4uL3RlbXBsYXRlcy9maWVsZC50eXBlJztcbmltcG9ydCB7IGlzT2JzZXJ2YWJsZSB9IGZyb20gJ3J4anMnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQnLFxuICB0ZW1wbGF0ZTogYDxuZy10ZW1wbGF0ZSAjY29udGFpbmVyPjwvbmctdGVtcGxhdGU+YCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGQgaW1wbGVtZW50cyBPbkluaXQsIE9uQ2hhbmdlcywgRG9DaGVjaywgQWZ0ZXJDb250ZW50SW5pdCwgQWZ0ZXJDb250ZW50Q2hlY2tlZCwgQWZ0ZXJWaWV3SW5pdCwgQWZ0ZXJWaWV3Q2hlY2tlZCwgT25EZXN0cm95IHtcbiAgQElucHV0KCkgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnO1xuXG4gIHdhcm5EZXByZWNhdGlvbiA9IGZhbHNlO1xuXG4gIEBJbnB1dCgpIHNldCBtb2RlbChtOiBhbnkpIHtcbiAgICB0aGlzLndhcm5EZXByZWNhdGlvbiAmJiBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnbW9kZWwnIGlucHV0IHRvICcke3RoaXMuY29uc3RydWN0b3IubmFtZX0nIGNvbXBvbmVudCBpcyBub3QgcmVxdWlyZWQgYW55bW9yZSwgeW91IG1heSByZW1vdmUgaXQhYCk7XG4gIH1cblxuICBASW5wdXQoKSBzZXQgZm9ybShmb3JtOiBGb3JtR3JvdXApIHtcbiAgICB0aGlzLndhcm5EZXByZWNhdGlvbiAmJiBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnZm9ybScgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTtcbiAgfVxuXG4gIEBJbnB1dCgpIHNldCBvcHRpb25zKG9wdGlvbnM6IEZvcm1seUZvcm1PcHRpb25zKSB7XG4gICAgdGhpcy53YXJuRGVwcmVjYXRpb24gJiYgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ29wdGlvbnMnIGlucHV0IHRvICcke3RoaXMuY29uc3RydWN0b3IubmFtZX0nIGNvbXBvbmVudCBpcyBub3QgcmVxdWlyZWQgYW55bW9yZSwgeW91IG1heSByZW1vdmUgaXQhYCk7XG4gIH1cblxuICBAT3V0cHV0KCkgbW9kZWxDaGFuZ2U6IEV2ZW50RW1pdHRlcjxhbnk+ID0gbmV3IEV2ZW50RW1pdHRlcigpO1xuICAvLyBUT0RPOiByZW1vdmUgYGFueWAsIG9uY2UgZHJvcHBpbmcgYW5ndWxhciBgVjdgIHN1cHBvcnQuXG4gIEBWaWV3Q2hpbGQoJ2NvbnRhaW5lcicsIDxhbnk+IHtyZWFkOiBWaWV3Q29udGFpbmVyUmVmLCBzdGF0aWM6IHRydWUgfSkgY29udGFpbmVyUmVmOiBWaWV3Q29udGFpbmVyUmVmO1xuICBwcml2YXRlIGhvc3RPYnNlcnZlcnM6IEZ1bmN0aW9uW10gPSBbXTtcbiAgcHJpdmF0ZSBjb21wb25lbnRSZWZzOiBhbnlbXSA9IFtdO1xuICBwcml2YXRlIGhvb2tzT2JzZXJ2ZXJzOiBGdW5jdGlvbltdID0gW107XG5cbiAgY29uc3RydWN0b3IoXG4gICAgcHJpdmF0ZSBmb3JtbHlDb25maWc6IEZvcm1seUNvbmZpZyxcbiAgICBwcml2YXRlIHJlbmRlcmVyOiBSZW5kZXJlcjIsXG4gICAgcHJpdmF0ZSByZXNvbHZlcjogQ29tcG9uZW50RmFjdG9yeVJlc29sdmVyLFxuICAgIHByaXZhdGUgZWxlbWVudFJlZjogRWxlbWVudFJlZixcbiAgICAvLyB0c2xpbnQ6ZGlzYWJsZS1uZXh0LWxpbmVcbiAgICBAQXR0cmlidXRlKCdoaWRlLWRlcHJlY2F0aW9uJykgaGlkZURlcHJlY2F0aW9uLFxuICApIHtcbiAgICB0aGlzLndhcm5EZXByZWNhdGlvbiA9IGhpZGVEZXByZWNhdGlvbiA9PT0gbnVsbDtcbiAgfVxuXG4gIG5nQWZ0ZXJDb250ZW50SW5pdCgpIHtcbiAgICB0aGlzLnRyaWdnZXJIb29rKCdhZnRlckNvbnRlbnRJbml0Jyk7XG4gIH1cblxuICBuZ0FmdGVyQ29udGVudENoZWNrZWQoKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnYWZ0ZXJDb250ZW50Q2hlY2tlZCcpO1xuICB9XG5cbiAgbmdBZnRlclZpZXdJbml0KCkge1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ2FmdGVyVmlld0luaXQnKTtcbiAgfVxuXG4gIG5nQWZ0ZXJWaWV3Q2hlY2tlZCgpIHtcbiAgICB0aGlzLnRyaWdnZXJIb29rKCdhZnRlclZpZXdDaGVja2VkJyk7XG4gIH1cblxuICBuZ0RvQ2hlY2soKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnZG9DaGVjaycpO1xuICB9XG5cbiAgbmdPbkluaXQoKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnb25Jbml0Jyk7XG4gIH1cblxuICBuZ09uQ2hhbmdlcyhjaGFuZ2VzOiBTaW1wbGVDaGFuZ2VzKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnb25DaGFuZ2VzJywgY2hhbmdlcyk7XG4gIH1cblxuICBuZ09uRGVzdHJveSgpIHtcbiAgICB0aGlzLnJlc2V0UmVmcyh0aGlzLmZpZWxkKTtcbiAgICB0aGlzLmhvc3RPYnNlcnZlcnMuZm9yRWFjaCh1bnN1YnNjcmliZSA9PiB1bnN1YnNjcmliZSgpKTtcbiAgICB0aGlzLmhvb2tzT2JzZXJ2ZXJzLmZvckVhY2godW5zdWJzY3JpYmUgPT4gdW5zdWJzY3JpYmUoKSk7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnb25EZXN0cm95Jyk7XG4gIH1cblxuICBwcml2YXRlIHJlbmRlckZpZWxkKGNvbnRhaW5lclJlZjogVmlld0NvbnRhaW5lclJlZiwgZjogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgd3JhcHBlcnM6IHN0cmluZ1tdKSB7XG4gICAgaWYgKHRoaXMuY29udGFpbmVyUmVmID09PSBjb250YWluZXJSZWYpIHtcbiAgICAgIHRoaXMucmVzZXRSZWZzKHRoaXMuZmllbGQpO1xuICAgICAgdGhpcy5jb250YWluZXJSZWYuY2xlYXIoKTtcbiAgICB9XG5cbiAgICBpZiAod3JhcHBlcnMgJiYgd3JhcHBlcnMubGVuZ3RoID4gMCkge1xuICAgICAgY29uc3QgW3dyYXBwZXIsIC4uLndwc10gPSB3cmFwcGVycztcbiAgICAgIGNvbnN0IHsgY29tcG9uZW50IH0gPSB0aGlzLmZvcm1seUNvbmZpZy5nZXRXcmFwcGVyKHdyYXBwZXIpO1xuXG4gICAgICBjb25zdCByZWYgPSBjb250YWluZXJSZWYuY3JlYXRlQ29tcG9uZW50PEZpZWxkV3JhcHBlcj4odGhpcy5yZXNvbHZlci5yZXNvbHZlQ29tcG9uZW50RmFjdG9yeShjb21wb25lbnQpKTtcbiAgICAgIHRoaXMuYXR0YWNoQ29tcG9uZW50UmVmKHJlZiwgZik7XG4gICAgICB3cmFwUHJvcGVydHk8Vmlld0NvbnRhaW5lclJlZj4ocmVmLmluc3RhbmNlLCAnZmllbGRDb21wb25lbnQnLCAoeyBmaXJzdENoYW5nZSwgcHJldmlvdXNWYWx1ZSwgY3VycmVudFZhbHVlIH0pID0+IHtcbiAgICAgICAgaWYgKGN1cnJlbnRWYWx1ZSkge1xuICAgICAgICAgIGNvbnN0IHZpZXdSZWYgPSBwcmV2aW91c1ZhbHVlID8gcHJldmlvdXNWYWx1ZS5kZXRhY2goKSA6IG51bGw7XG4gICAgICAgICAgaWYgKHZpZXdSZWYgJiYgIXZpZXdSZWYuZGVzdHJveWVkKSB7XG4gICAgICAgICAgICBjdXJyZW50VmFsdWUuaW5zZXJ0KHZpZXdSZWYpO1xuICAgICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICB0aGlzLnJlbmRlckZpZWxkKGN1cnJlbnRWYWx1ZSwgZiwgd3BzKTtcbiAgICAgICAgICB9XG5cbiAgICAgICAgICAhZmlyc3RDaGFuZ2UgJiYgcmVmLmNoYW5nZURldGVjdG9yUmVmLmRldGVjdENoYW5nZXMoKTtcbiAgICAgICAgfVxuICAgICAgfSk7XG4gICAgfSBlbHNlIGlmIChmICYmIGYudHlwZSkge1xuICAgICAgY29uc3QgeyBjb21wb25lbnQgfSA9IHRoaXMuZm9ybWx5Q29uZmlnLmdldFR5cGUoZi50eXBlKTtcbiAgICAgIGNvbnN0IHJlZiA9IGNvbnRhaW5lclJlZi5jcmVhdGVDb21wb25lbnQ8RmllbGRXcmFwcGVyPih0aGlzLnJlc29sdmVyLnJlc29sdmVDb21wb25lbnRGYWN0b3J5KGNvbXBvbmVudCkpO1xuICAgICAgdGhpcy5hdHRhY2hDb21wb25lbnRSZWYocmVmLCBmKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHRyaWdnZXJIb29rKG5hbWU6IHN0cmluZywgY2hhbmdlcz86IFNpbXBsZUNoYW5nZXMpIHtcbiAgICBpZiAodGhpcy5maWVsZCAmJiB0aGlzLmZpZWxkLmhvb2tzICYmIHRoaXMuZmllbGQuaG9va3NbbmFtZV0pIHtcbiAgICAgIGlmICghY2hhbmdlcyB8fCBjaGFuZ2VzLmZpZWxkKSB7XG4gICAgICAgIGNvbnN0IHIgPSB0aGlzLmZpZWxkLmhvb2tzW25hbWVdKHRoaXMuZmllbGQpO1xuICAgICAgICBpZiAoaXNPYnNlcnZhYmxlKHIpICYmIFsnb25Jbml0JywgJ2FmdGVyQ29udGVudEluaXQnLCAnYWZ0ZXJWaWV3SW5pdCddLmluZGV4T2YobmFtZSkgIT09IC0xKSB7XG4gICAgICAgICAgY29uc3Qgc3ViID0gci5zdWJzY3JpYmUoKTtcbiAgICAgICAgICB0aGlzLmhvb2tzT2JzZXJ2ZXJzLnB1c2goKCkgPT4gc3ViLnVuc3Vic2NyaWJlKCkpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuXG4gICAgaWYgKHRoaXMuZmllbGQgJiYgdGhpcy5maWVsZC5saWZlY3ljbGUgJiYgdGhpcy5maWVsZC5saWZlY3ljbGVbbmFtZV0pIHtcbiAgICAgIHRoaXMuZmllbGQubGlmZWN5Y2xlW25hbWVdKFxuICAgICAgICB0aGlzLmZpZWxkLmZvcm0sXG4gICAgICAgIHRoaXMuZmllbGQsXG4gICAgICAgIHRoaXMuZmllbGQubW9kZWwsXG4gICAgICAgIHRoaXMuZmllbGQub3B0aW9ucyxcbiAgICAgICk7XG4gICAgfVxuXG4gICAgaWYgKG5hbWUgPT09ICdvbkNoYW5nZXMnICYmIGNoYW5nZXMuZmllbGQpIHtcbiAgICAgIHRoaXMucmVuZGVySG9zdEJpbmRpbmcoKTtcbiAgICAgIHRoaXMucmVzZXRSZWZzKGNoYW5nZXMuZmllbGQucHJldmlvdXNWYWx1ZSk7XG4gICAgICB0aGlzLnJlbmRlckZpZWxkKHRoaXMuY29udGFpbmVyUmVmLCB0aGlzLmZpZWxkLCB0aGlzLmZpZWxkID8gdGhpcy5maWVsZC53cmFwcGVycyA6IFtdKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIGF0dGFjaENvbXBvbmVudFJlZjxUIGV4dGVuZHMgRmllbGRUeXBlPihyZWY6IENvbXBvbmVudFJlZjxUPiwgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICB0aGlzLmNvbXBvbmVudFJlZnMucHVzaChyZWYpO1xuICAgIGZpZWxkLl9jb21wb25lbnRSZWZzLnB1c2gocmVmKTtcbiAgICBPYmplY3QuYXNzaWduKHJlZi5pbnN0YW5jZSwgeyBmaWVsZCB9KTtcbiAgfVxuXG4gIHByaXZhdGUgcmVuZGVySG9zdEJpbmRpbmcoKSB7XG4gICAgaWYgKCF0aGlzLmZpZWxkKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgdGhpcy5ob3N0T2JzZXJ2ZXJzLmZvckVhY2godW5zdWJzY3JpYmUgPT4gdW5zdWJzY3JpYmUoKSk7XG4gICAgdGhpcy5ob3N0T2JzZXJ2ZXJzID0gW1xuICAgICAgd3JhcFByb3BlcnR5KHRoaXMuZmllbGQsICdoaWRlJywgKHsgZmlyc3RDaGFuZ2UsIGN1cnJlbnRWYWx1ZSB9KSA9PiB7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UgfHwgKGZpcnN0Q2hhbmdlICYmIGN1cnJlbnRWYWx1ZSkpIHtcbiAgICAgICAgICB0aGlzLnJlbmRlcmVyLnNldFN0eWxlKHRoaXMuZWxlbWVudFJlZi5uYXRpdmVFbGVtZW50LCAnZGlzcGxheScsIGN1cnJlbnRWYWx1ZSA/ICdub25lJyA6ICcnKTtcbiAgICAgICAgfVxuICAgICAgfSksXG4gICAgICB3cmFwUHJvcGVydHkodGhpcy5maWVsZCwgJ2NsYXNzTmFtZScsICh7IGZpcnN0Q2hhbmdlLCBjdXJyZW50VmFsdWUgfSkgPT4ge1xuICAgICAgICBpZiAoIWZpcnN0Q2hhbmdlIHx8IChmaXJzdENoYW5nZSAmJiBjdXJyZW50VmFsdWUpKSB7XG4gICAgICAgICAgdGhpcy5yZW5kZXJlci5zZXRBdHRyaWJ1dGUodGhpcy5lbGVtZW50UmVmLm5hdGl2ZUVsZW1lbnQsICdjbGFzcycsIGN1cnJlbnRWYWx1ZSk7XG4gICAgICAgIH1cbiAgICAgIH0pLFxuICAgIF07XG4gIH1cblxuICBwcml2YXRlIHJlc2V0UmVmcyhmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIGlmIChmaWVsZCkge1xuICAgICAgaWYgKGZpZWxkLl9jb21wb25lbnRSZWZzKSB7XG4gICAgICAgIGZpZWxkLl9jb21wb25lbnRSZWZzID0gZmllbGQuX2NvbXBvbmVudFJlZnMuZmlsdGVyKHJlZiA9PiB0aGlzLmNvbXBvbmVudFJlZnMuaW5kZXhPZihyZWYpID09PSAtMSk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBkZWZpbmVIaWRkZW5Qcm9wKHRoaXMuZmllbGQsICdfY29tcG9uZW50UmVmcycsIFtdKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICB0aGlzLmNvbXBvbmVudFJlZnMgPSBbXTtcbiAgfVxufVxuIl19