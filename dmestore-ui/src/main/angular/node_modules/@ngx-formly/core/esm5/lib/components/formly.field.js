/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, EventEmitter, Input, Output, ViewContainerRef, ViewChild, Attribute, ComponentFactoryResolver, Renderer2, ElementRef, } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyConfig } from '../services/formly.config';
import { defineHiddenProp, wrapProperty } from '../utils';
import { isObservable } from 'rxjs';
var FormlyField = /** @class */ (function () {
    function FormlyField(formlyConfig, renderer, resolver, elementRef, 
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
    Object.defineProperty(FormlyField.prototype, "model", {
        set: /**
         * @param {?} m
         * @return {?}
         */
        function (m) {
            this.warnDeprecation && console.warn("NgxFormly: passing 'model' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!");
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyField.prototype, "form", {
        set: /**
         * @param {?} form
         * @return {?}
         */
        function (form) {
            this.warnDeprecation && console.warn("NgxFormly: passing 'form' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!");
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyField.prototype, "options", {
        set: /**
         * @param {?} options
         * @return {?}
         */
        function (options) {
            this.warnDeprecation && console.warn("NgxFormly: passing 'options' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!");
        },
        enumerable: true,
        configurable: true
    });
    /**
     * @return {?}
     */
    FormlyField.prototype.ngAfterContentInit = /**
     * @return {?}
     */
    function () {
        this.triggerHook('afterContentInit');
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngAfterContentChecked = /**
     * @return {?}
     */
    function () {
        this.triggerHook('afterContentChecked');
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        this.triggerHook('afterViewInit');
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngAfterViewChecked = /**
     * @return {?}
     */
    function () {
        this.triggerHook('afterViewChecked');
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngDoCheck = /**
     * @return {?}
     */
    function () {
        this.triggerHook('doCheck');
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngOnInit = /**
     * @return {?}
     */
    function () {
        this.triggerHook('onInit');
    };
    /**
     * @param {?} changes
     * @return {?}
     */
    FormlyField.prototype.ngOnChanges = /**
     * @param {?} changes
     * @return {?}
     */
    function (changes) {
        this.triggerHook('onChanges', changes);
    };
    /**
     * @return {?}
     */
    FormlyField.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.resetRefs(this.field);
        this.hostObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        function (unsubscribe) { return unsubscribe(); }));
        this.hooksObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        function (unsubscribe) { return unsubscribe(); }));
        this.triggerHook('onDestroy');
    };
    /**
     * @private
     * @param {?} containerRef
     * @param {?} f
     * @param {?} wrappers
     * @return {?}
     */
    FormlyField.prototype.renderField = /**
     * @private
     * @param {?} containerRef
     * @param {?} f
     * @param {?} wrappers
     * @return {?}
     */
    function (containerRef, f, wrappers) {
        var _this = this;
        if (this.containerRef === containerRef) {
            this.resetRefs(this.field);
            this.containerRef.clear();
        }
        if (wrappers && wrappers.length > 0) {
            var _a = tslib_1.__read(wrappers), wrapper = _a[0], wps_1 = _a.slice(1);
            var component = this.formlyConfig.getWrapper(wrapper).component;
            /** @type {?} */
            var ref_1 = containerRef.createComponent(this.resolver.resolveComponentFactory(component));
            this.attachComponentRef(ref_1, f);
            wrapProperty(ref_1.instance, 'fieldComponent', (/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var firstChange = _a.firstChange, previousValue = _a.previousValue, currentValue = _a.currentValue;
                if (currentValue) {
                    /** @type {?} */
                    var viewRef = previousValue ? previousValue.detach() : null;
                    if (viewRef && !viewRef.destroyed) {
                        currentValue.insert(viewRef);
                    }
                    else {
                        _this.renderField(currentValue, f, wps_1);
                    }
                    !firstChange && ref_1.changeDetectorRef.detectChanges();
                }
            }));
        }
        else if (f && f.type) {
            var component = this.formlyConfig.getType(f.type).component;
            /** @type {?} */
            var ref = containerRef.createComponent(this.resolver.resolveComponentFactory(component));
            this.attachComponentRef(ref, f);
        }
    };
    /**
     * @private
     * @param {?} name
     * @param {?=} changes
     * @return {?}
     */
    FormlyField.prototype.triggerHook = /**
     * @private
     * @param {?} name
     * @param {?=} changes
     * @return {?}
     */
    function (name, changes) {
        if (this.field && this.field.hooks && this.field.hooks[name]) {
            if (!changes || changes.field) {
                /** @type {?} */
                var r = this.field.hooks[name](this.field);
                if (isObservable(r) && ['onInit', 'afterContentInit', 'afterViewInit'].indexOf(name) !== -1) {
                    /** @type {?} */
                    var sub_1 = r.subscribe();
                    this.hooksObservers.push((/**
                     * @return {?}
                     */
                    function () { return sub_1.unsubscribe(); }));
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
    };
    /**
     * @private
     * @template T
     * @param {?} ref
     * @param {?} field
     * @return {?}
     */
    FormlyField.prototype.attachComponentRef = /**
     * @private
     * @template T
     * @param {?} ref
     * @param {?} field
     * @return {?}
     */
    function (ref, field) {
        this.componentRefs.push(ref);
        field._componentRefs.push(ref);
        Object.assign(ref.instance, { field: field });
    };
    /**
     * @private
     * @return {?}
     */
    FormlyField.prototype.renderHostBinding = /**
     * @private
     * @return {?}
     */
    function () {
        var _this = this;
        if (!this.field) {
            return;
        }
        this.hostObservers.forEach((/**
         * @param {?} unsubscribe
         * @return {?}
         */
        function (unsubscribe) { return unsubscribe(); }));
        this.hostObservers = [
            wrapProperty(this.field, 'hide', (/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var firstChange = _a.firstChange, currentValue = _a.currentValue;
                if (!firstChange || (firstChange && currentValue)) {
                    _this.renderer.setStyle(_this.elementRef.nativeElement, 'display', currentValue ? 'none' : '');
                }
            })),
            wrapProperty(this.field, 'className', (/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var firstChange = _a.firstChange, currentValue = _a.currentValue;
                if (!firstChange || (firstChange && currentValue)) {
                    _this.renderer.setAttribute(_this.elementRef.nativeElement, 'class', currentValue);
                }
            })),
        ];
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    FormlyField.prototype.resetRefs = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        if (field) {
            if (field._componentRefs) {
                field._componentRefs = field._componentRefs.filter((/**
                 * @param {?} ref
                 * @return {?}
                 */
                function (ref) { return _this.componentRefs.indexOf(ref) === -1; }));
            }
            else {
                defineHiddenProp(this.field, '_componentRefs', []);
            }
        }
        this.componentRefs = [];
    };
    FormlyField.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field',
                    template: "<ng-template #container></ng-template>"
                }] }
    ];
    /** @nocollapse */
    FormlyField.ctorParameters = function () { return [
        { type: FormlyConfig },
        { type: Renderer2 },
        { type: ComponentFactoryResolver },
        { type: ElementRef },
        { type: undefined, decorators: [{ type: Attribute, args: ['hide-deprecation',] }] }
    ]; };
    FormlyField.propDecorators = {
        field: [{ type: Input }],
        model: [{ type: Input }],
        form: [{ type: Input }],
        options: [{ type: Input }],
        modelChange: [{ type: Output }],
        containerRef: [{ type: ViewChild, args: ['container', (/** @type {?} */ ({ read: ViewContainerRef, static: true })),] }]
    };
    return FormlyField;
}());
export { FormlyField };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZpZWxkLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvY29yZS8iLCJzb3VyY2VzIjpbImxpYi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFDTCxTQUFTLEVBQUUsWUFBWSxFQUFFLEtBQUssRUFBRSxNQUFNLEVBQ3RDLGdCQUFnQixFQUFFLFNBQVMsRUFBK0IsU0FBUyxFQUFFLHdCQUF3QixFQUNrQixTQUFTLEVBQUUsVUFBVSxHQUNySSxNQUFNLGVBQWUsQ0FBQztBQUN2QixPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFDM0MsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLDJCQUEyQixDQUFDO0FBRXpELE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxZQUFZLEVBQUUsTUFBTSxVQUFVLENBQUM7QUFHMUQsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUVwQztJQTRCRSxxQkFDVSxZQUEwQixFQUMxQixRQUFtQixFQUNuQixRQUFrQyxFQUNsQyxVQUFzQjtJQUM5QiwyQkFBMkI7SUFDSSxlQUFlO1FBTHRDLGlCQUFZLEdBQVosWUFBWSxDQUFjO1FBQzFCLGFBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsYUFBUSxHQUFSLFFBQVEsQ0FBMEI7UUFDbEMsZUFBVSxHQUFWLFVBQVUsQ0FBWTtRQXpCaEMsb0JBQWUsR0FBRyxLQUFLLENBQUM7UUFjZCxnQkFBVyxHQUFzQixJQUFJLFlBQVksRUFBRSxDQUFDO1FBR3RELGtCQUFhLEdBQWUsRUFBRSxDQUFDO1FBQy9CLGtCQUFhLEdBQVUsRUFBRSxDQUFDO1FBQzFCLG1CQUFjLEdBQWUsRUFBRSxDQUFDO1FBVXRDLElBQUksQ0FBQyxlQUFlLEdBQUcsZUFBZSxLQUFLLElBQUksQ0FBQztJQUNsRCxDQUFDO0lBNUJELHNCQUFhLDhCQUFLOzs7OztRQUFsQixVQUFtQixDQUFNO1lB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QTJCOzs7OztJQUMzQiwrQkFBMEM7Ozs7O0lBQzFDLGlDQUE4QiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7XG4gIENvbXBvbmVudCwgRXZlbnRFbWl0dGVyLCBJbnB1dCwgT3V0cHV0LFxuICBWaWV3Q29udGFpbmVyUmVmLCBWaWV3Q2hpbGQsIENvbXBvbmVudFJlZiwgU2ltcGxlQ2hhbmdlcywgQXR0cmlidXRlLCBDb21wb25lbnRGYWN0b3J5UmVzb2x2ZXIsXG4gIE9uSW5pdCwgT25DaGFuZ2VzLCBPbkRlc3Ryb3ksIERvQ2hlY2ssIEFmdGVyQ29udGVudEluaXQsIEFmdGVyQ29udGVudENoZWNrZWQsIEFmdGVyVmlld0luaXQsIEFmdGVyVmlld0NoZWNrZWQsIFJlbmRlcmVyMiwgRWxlbWVudFJlZixcbn0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGb3JtR3JvdXAgfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBGb3JtbHlDb25maWcgfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnLCBGb3JtbHlGb3JtT3B0aW9ucywgRm9ybWx5RmllbGRDb25maWdDYWNoZSB9IGZyb20gJy4vZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBkZWZpbmVIaWRkZW5Qcm9wLCB3cmFwUHJvcGVydHkgfSBmcm9tICcuLi91dGlscyc7XG5pbXBvcnQgeyBGaWVsZFdyYXBwZXIgfSBmcm9tICcuLi90ZW1wbGF0ZXMvZmllbGQud3JhcHBlcic7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICcuLi90ZW1wbGF0ZXMvZmllbGQudHlwZSc7XG5pbXBvcnQgeyBpc09ic2VydmFibGUgfSBmcm9tICdyeGpzJztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnZm9ybWx5LWZpZWxkJyxcbiAgdGVtcGxhdGU6IGA8bmctdGVtcGxhdGUgI2NvbnRhaW5lcj48L25nLXRlbXBsYXRlPmAsXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seUZpZWxkIGltcGxlbWVudHMgT25Jbml0LCBPbkNoYW5nZXMsIERvQ2hlY2ssIEFmdGVyQ29udGVudEluaXQsIEFmdGVyQ29udGVudENoZWNrZWQsIEFmdGVyVmlld0luaXQsIEFmdGVyVmlld0NoZWNrZWQsIE9uRGVzdHJveSB7XG4gIEBJbnB1dCgpIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZztcblxuICB3YXJuRGVwcmVjYXRpb24gPSBmYWxzZTtcblxuICBASW5wdXQoKSBzZXQgbW9kZWwobTogYW55KSB7XG4gICAgdGhpcy53YXJuRGVwcmVjYXRpb24gJiYgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ21vZGVsJyBpbnB1dCB0byAnJHt0aGlzLmNvbnN0cnVjdG9yLm5hbWV9JyBjb21wb25lbnQgaXMgbm90IHJlcXVpcmVkIGFueW1vcmUsIHlvdSBtYXkgcmVtb3ZlIGl0IWApO1xuICB9XG5cbiAgQElucHV0KCkgc2V0IGZvcm0oZm9ybTogRm9ybUdyb3VwKSB7XG4gICAgdGhpcy53YXJuRGVwcmVjYXRpb24gJiYgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ2Zvcm0nIGlucHV0IHRvICcke3RoaXMuY29uc3RydWN0b3IubmFtZX0nIGNvbXBvbmVudCBpcyBub3QgcmVxdWlyZWQgYW55bW9yZSwgeW91IG1heSByZW1vdmUgaXQhYCk7XG4gIH1cblxuICBASW5wdXQoKSBzZXQgb3B0aW9ucyhvcHRpb25zOiBGb3JtbHlGb3JtT3B0aW9ucykge1xuICAgIHRoaXMud2FybkRlcHJlY2F0aW9uICYmIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBwYXNzaW5nICdvcHRpb25zJyBpbnB1dCB0byAnJHt0aGlzLmNvbnN0cnVjdG9yLm5hbWV9JyBjb21wb25lbnQgaXMgbm90IHJlcXVpcmVkIGFueW1vcmUsIHlvdSBtYXkgcmVtb3ZlIGl0IWApO1xuICB9XG5cbiAgQE91dHB1dCgpIG1vZGVsQ2hhbmdlOiBFdmVudEVtaXR0ZXI8YW55PiA9IG5ldyBFdmVudEVtaXR0ZXIoKTtcbiAgLy8gVE9ETzogcmVtb3ZlIGBhbnlgLCBvbmNlIGRyb3BwaW5nIGFuZ3VsYXIgYFY3YCBzdXBwb3J0LlxuICBAVmlld0NoaWxkKCdjb250YWluZXInLCA8YW55PiB7cmVhZDogVmlld0NvbnRhaW5lclJlZiwgc3RhdGljOiB0cnVlIH0pIGNvbnRhaW5lclJlZjogVmlld0NvbnRhaW5lclJlZjtcbiAgcHJpdmF0ZSBob3N0T2JzZXJ2ZXJzOiBGdW5jdGlvbltdID0gW107XG4gIHByaXZhdGUgY29tcG9uZW50UmVmczogYW55W10gPSBbXTtcbiAgcHJpdmF0ZSBob29rc09ic2VydmVyczogRnVuY3Rpb25bXSA9IFtdO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcsXG4gICAgcHJpdmF0ZSByZW5kZXJlcjogUmVuZGVyZXIyLFxuICAgIHByaXZhdGUgcmVzb2x2ZXI6IENvbXBvbmVudEZhY3RvcnlSZXNvbHZlcixcbiAgICBwcml2YXRlIGVsZW1lbnRSZWY6IEVsZW1lbnRSZWYsXG4gICAgLy8gdHNsaW50OmRpc2FibGUtbmV4dC1saW5lXG4gICAgQEF0dHJpYnV0ZSgnaGlkZS1kZXByZWNhdGlvbicpIGhpZGVEZXByZWNhdGlvbixcbiAgKSB7XG4gICAgdGhpcy53YXJuRGVwcmVjYXRpb24gPSBoaWRlRGVwcmVjYXRpb24gPT09IG51bGw7XG4gIH1cblxuICBuZ0FmdGVyQ29udGVudEluaXQoKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnYWZ0ZXJDb250ZW50SW5pdCcpO1xuICB9XG5cbiAgbmdBZnRlckNvbnRlbnRDaGVja2VkKCkge1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ2FmdGVyQ29udGVudENoZWNrZWQnKTtcbiAgfVxuXG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICB0aGlzLnRyaWdnZXJIb29rKCdhZnRlclZpZXdJbml0Jyk7XG4gIH1cblxuICBuZ0FmdGVyVmlld0NoZWNrZWQoKSB7XG4gICAgdGhpcy50cmlnZ2VySG9vaygnYWZ0ZXJWaWV3Q2hlY2tlZCcpO1xuICB9XG5cbiAgbmdEb0NoZWNrKCkge1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ2RvQ2hlY2snKTtcbiAgfVxuXG4gIG5nT25Jbml0KCkge1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ29uSW5pdCcpO1xuICB9XG5cbiAgbmdPbkNoYW5nZXMoY2hhbmdlczogU2ltcGxlQ2hhbmdlcykge1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ29uQ2hhbmdlcycsIGNoYW5nZXMpO1xuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgdGhpcy5yZXNldFJlZnModGhpcy5maWVsZCk7XG4gICAgdGhpcy5ob3N0T2JzZXJ2ZXJzLmZvckVhY2godW5zdWJzY3JpYmUgPT4gdW5zdWJzY3JpYmUoKSk7XG4gICAgdGhpcy5ob29rc09ic2VydmVycy5mb3JFYWNoKHVuc3Vic2NyaWJlID0+IHVuc3Vic2NyaWJlKCkpO1xuICAgIHRoaXMudHJpZ2dlckhvb2soJ29uRGVzdHJveScpO1xuICB9XG5cbiAgcHJpdmF0ZSByZW5kZXJGaWVsZChjb250YWluZXJSZWY6IFZpZXdDb250YWluZXJSZWYsIGY6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHdyYXBwZXJzOiBzdHJpbmdbXSkge1xuICAgIGlmICh0aGlzLmNvbnRhaW5lclJlZiA9PT0gY29udGFpbmVyUmVmKSB7XG4gICAgICB0aGlzLnJlc2V0UmVmcyh0aGlzLmZpZWxkKTtcbiAgICAgIHRoaXMuY29udGFpbmVyUmVmLmNsZWFyKCk7XG4gICAgfVxuXG4gICAgaWYgKHdyYXBwZXJzICYmIHdyYXBwZXJzLmxlbmd0aCA+IDApIHtcbiAgICAgIGNvbnN0IFt3cmFwcGVyLCAuLi53cHNdID0gd3JhcHBlcnM7XG4gICAgICBjb25zdCB7IGNvbXBvbmVudCB9ID0gdGhpcy5mb3JtbHlDb25maWcuZ2V0V3JhcHBlcih3cmFwcGVyKTtcblxuICAgICAgY29uc3QgcmVmID0gY29udGFpbmVyUmVmLmNyZWF0ZUNvbXBvbmVudDxGaWVsZFdyYXBwZXI+KHRoaXMucmVzb2x2ZXIucmVzb2x2ZUNvbXBvbmVudEZhY3RvcnkoY29tcG9uZW50KSk7XG4gICAgICB0aGlzLmF0dGFjaENvbXBvbmVudFJlZihyZWYsIGYpO1xuICAgICAgd3JhcFByb3BlcnR5PFZpZXdDb250YWluZXJSZWY+KHJlZi5pbnN0YW5jZSwgJ2ZpZWxkQ29tcG9uZW50JywgKHsgZmlyc3RDaGFuZ2UsIHByZXZpb3VzVmFsdWUsIGN1cnJlbnRWYWx1ZSB9KSA9PiB7XG4gICAgICAgIGlmIChjdXJyZW50VmFsdWUpIHtcbiAgICAgICAgICBjb25zdCB2aWV3UmVmID0gcHJldmlvdXNWYWx1ZSA/IHByZXZpb3VzVmFsdWUuZGV0YWNoKCkgOiBudWxsO1xuICAgICAgICAgIGlmICh2aWV3UmVmICYmICF2aWV3UmVmLmRlc3Ryb3llZCkge1xuICAgICAgICAgICAgY3VycmVudFZhbHVlLmluc2VydCh2aWV3UmVmKTtcbiAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgdGhpcy5yZW5kZXJGaWVsZChjdXJyZW50VmFsdWUsIGYsIHdwcyk7XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgIWZpcnN0Q2hhbmdlICYmIHJlZi5jaGFuZ2VEZXRlY3RvclJlZi5kZXRlY3RDaGFuZ2VzKCk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH0gZWxzZSBpZiAoZiAmJiBmLnR5cGUpIHtcbiAgICAgIGNvbnN0IHsgY29tcG9uZW50IH0gPSB0aGlzLmZvcm1seUNvbmZpZy5nZXRUeXBlKGYudHlwZSk7XG4gICAgICBjb25zdCByZWYgPSBjb250YWluZXJSZWYuY3JlYXRlQ29tcG9uZW50PEZpZWxkV3JhcHBlcj4odGhpcy5yZXNvbHZlci5yZXNvbHZlQ29tcG9uZW50RmFjdG9yeShjb21wb25lbnQpKTtcbiAgICAgIHRoaXMuYXR0YWNoQ29tcG9uZW50UmVmKHJlZiwgZik7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSB0cmlnZ2VySG9vayhuYW1lOiBzdHJpbmcsIGNoYW5nZXM/OiBTaW1wbGVDaGFuZ2VzKSB7XG4gICAgaWYgKHRoaXMuZmllbGQgJiYgdGhpcy5maWVsZC5ob29rcyAmJiB0aGlzLmZpZWxkLmhvb2tzW25hbWVdKSB7XG4gICAgICBpZiAoIWNoYW5nZXMgfHwgY2hhbmdlcy5maWVsZCkge1xuICAgICAgICBjb25zdCByID0gdGhpcy5maWVsZC5ob29rc1tuYW1lXSh0aGlzLmZpZWxkKTtcbiAgICAgICAgaWYgKGlzT2JzZXJ2YWJsZShyKSAmJiBbJ29uSW5pdCcsICdhZnRlckNvbnRlbnRJbml0JywgJ2FmdGVyVmlld0luaXQnXS5pbmRleE9mKG5hbWUpICE9PSAtMSkge1xuICAgICAgICAgIGNvbnN0IHN1YiA9IHIuc3Vic2NyaWJlKCk7XG4gICAgICAgICAgdGhpcy5ob29rc09ic2VydmVycy5wdXNoKCgpID0+IHN1Yi51bnN1YnNjcmliZSgpKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cblxuICAgIGlmICh0aGlzLmZpZWxkICYmIHRoaXMuZmllbGQubGlmZWN5Y2xlICYmIHRoaXMuZmllbGQubGlmZWN5Y2xlW25hbWVdKSB7XG4gICAgICB0aGlzLmZpZWxkLmxpZmVjeWNsZVtuYW1lXShcbiAgICAgICAgdGhpcy5maWVsZC5mb3JtLFxuICAgICAgICB0aGlzLmZpZWxkLFxuICAgICAgICB0aGlzLmZpZWxkLm1vZGVsLFxuICAgICAgICB0aGlzLmZpZWxkLm9wdGlvbnMsXG4gICAgICApO1xuICAgIH1cblxuICAgIGlmIChuYW1lID09PSAnb25DaGFuZ2VzJyAmJiBjaGFuZ2VzLmZpZWxkKSB7XG4gICAgICB0aGlzLnJlbmRlckhvc3RCaW5kaW5nKCk7XG4gICAgICB0aGlzLnJlc2V0UmVmcyhjaGFuZ2VzLmZpZWxkLnByZXZpb3VzVmFsdWUpO1xuICAgICAgdGhpcy5yZW5kZXJGaWVsZCh0aGlzLmNvbnRhaW5lclJlZiwgdGhpcy5maWVsZCwgdGhpcy5maWVsZCA/IHRoaXMuZmllbGQud3JhcHBlcnMgOiBbXSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBhdHRhY2hDb21wb25lbnRSZWY8VCBleHRlbmRzIEZpZWxkVHlwZT4ocmVmOiBDb21wb25lbnRSZWY8VD4sIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5jb21wb25lbnRSZWZzLnB1c2gocmVmKTtcbiAgICBmaWVsZC5fY29tcG9uZW50UmVmcy5wdXNoKHJlZik7XG4gICAgT2JqZWN0LmFzc2lnbihyZWYuaW5zdGFuY2UsIHsgZmllbGQgfSk7XG4gIH1cblxuICBwcml2YXRlIHJlbmRlckhvc3RCaW5kaW5nKCkge1xuICAgIGlmICghdGhpcy5maWVsZCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIHRoaXMuaG9zdE9ic2VydmVycy5mb3JFYWNoKHVuc3Vic2NyaWJlID0+IHVuc3Vic2NyaWJlKCkpO1xuICAgIHRoaXMuaG9zdE9ic2VydmVycyA9IFtcbiAgICAgIHdyYXBQcm9wZXJ0eSh0aGlzLmZpZWxkLCAnaGlkZScsICh7IGZpcnN0Q2hhbmdlLCBjdXJyZW50VmFsdWUgfSkgPT4ge1xuICAgICAgICBpZiAoIWZpcnN0Q2hhbmdlIHx8IChmaXJzdENoYW5nZSAmJiBjdXJyZW50VmFsdWUpKSB7XG4gICAgICAgICAgdGhpcy5yZW5kZXJlci5zZXRTdHlsZSh0aGlzLmVsZW1lbnRSZWYubmF0aXZlRWxlbWVudCwgJ2Rpc3BsYXknLCBjdXJyZW50VmFsdWUgPyAnbm9uZScgOiAnJyk7XG4gICAgICAgIH1cbiAgICAgIH0pLFxuICAgICAgd3JhcFByb3BlcnR5KHRoaXMuZmllbGQsICdjbGFzc05hbWUnLCAoeyBmaXJzdENoYW5nZSwgY3VycmVudFZhbHVlIH0pID0+IHtcbiAgICAgICAgaWYgKCFmaXJzdENoYW5nZSB8fCAoZmlyc3RDaGFuZ2UgJiYgY3VycmVudFZhbHVlKSkge1xuICAgICAgICAgIHRoaXMucmVuZGVyZXIuc2V0QXR0cmlidXRlKHRoaXMuZWxlbWVudFJlZi5uYXRpdmVFbGVtZW50LCAnY2xhc3MnLCBjdXJyZW50VmFsdWUpO1xuICAgICAgICB9XG4gICAgICB9KSxcbiAgICBdO1xuICB9XG5cbiAgcHJpdmF0ZSByZXNldFJlZnMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoZmllbGQpIHtcbiAgICAgIGlmIChmaWVsZC5fY29tcG9uZW50UmVmcykge1xuICAgICAgICBmaWVsZC5fY29tcG9uZW50UmVmcyA9IGZpZWxkLl9jb21wb25lbnRSZWZzLmZpbHRlcihyZWYgPT4gdGhpcy5jb21wb25lbnRSZWZzLmluZGV4T2YocmVmKSA9PT0gLTEpO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX2NvbXBvbmVudFJlZnMnLCBbXSk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgdGhpcy5jb21wb25lbnRSZWZzID0gW107XG4gIH1cbn1cbiJdfQ==