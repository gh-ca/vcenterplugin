/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Directive, ElementRef, Input, Renderer2, Inject } from '@angular/core';
import { wrapProperty, defineHiddenProp, FORMLY_VALIDATORS } from '../utils';
import { DOCUMENT } from '@angular/common';
var FormlyAttributes = /** @class */ (function () {
    function FormlyAttributes(renderer, elementRef, _document) {
        this.renderer = renderer;
        this.elementRef = elementRef;
        this.uiAttributesCache = {};
        this.uiAttributes = tslib_1.__spread(FORMLY_VALIDATORS, [
            'tabindex',
            'placeholder',
            'readonly',
            'disabled',
            'step',
        ]);
        /**
         * HostBinding doesn't register listeners conditionally which may produce some perf issues.
         *
         * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1991
         */
        this.uiEvents = {
            listeners: [],
            events: [
                'click',
                'keyup',
                'keydown',
                'keypress',
            ],
        };
        this.document = _document;
    }
    Object.defineProperty(FormlyAttributes.prototype, "to", {
        get: /**
         * @return {?}
         */
        function () { return this.field.templateOptions || {}; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyAttributes.prototype, "fieldAttrElements", {
        get: /**
         * @private
         * @return {?}
         */
        function () { return (this.field && this.field['_elementRefs']) || []; },
        enumerable: true,
        configurable: true
    });
    /**
     * @param {?} changes
     * @return {?}
     */
    FormlyAttributes.prototype.ngOnChanges = /**
     * @param {?} changes
     * @return {?}
     */
    function (changes) {
        var _this = this;
        if (changes.field) {
            this.field.name && this.setAttribute('name', this.field.name);
            this.uiEvents.listeners.forEach((/**
             * @param {?} listener
             * @return {?}
             */
            function (listener) { return listener(); }));
            this.uiEvents.events.forEach((/**
             * @param {?} eventName
             * @return {?}
             */
            function (eventName) {
                if (_this.to && _this.to[eventName]) {
                    _this.uiEvents.listeners.push(_this.renderer.listen(_this.elementRef.nativeElement, eventName, (/**
                     * @param {?} e
                     * @return {?}
                     */
                    function (e) { return _this.to[eventName](_this.field, e); })));
                }
            }));
            if (this.to && this.to.attributes) {
                wrapProperty(this.to, 'attributes', (/**
                 * @param {?} __0
                 * @return {?}
                 */
                function (_a) {
                    var currentValue = _a.currentValue, previousValue = _a.previousValue;
                    if (previousValue) {
                        Object.keys(previousValue).forEach((/**
                         * @param {?} attr
                         * @return {?}
                         */
                        function (attr) { return _this.removeAttribute(attr); }));
                    }
                    if (currentValue) {
                        Object.keys(currentValue).forEach((/**
                         * @param {?} attr
                         * @return {?}
                         */
                        function (attr) { return _this.setAttribute(attr, currentValue[attr]); }));
                    }
                }));
            }
            this.detachElementRef(changes.field.previousValue);
            this.attachElementRef(changes.field.currentValue);
            if (this.fieldAttrElements.length === 1) {
                !this.id && this.field.id && this.setAttribute('id', this.field.id);
                wrapProperty(this.field, 'focus', (/**
                 * @param {?} __0
                 * @return {?}
                 */
                function (_a) {
                    var currentValue = _a.currentValue;
                    _this.toggleFocus(currentValue);
                }));
            }
        }
        if (changes.id) {
            this.setAttribute('id', this.id);
        }
    };
    /**
     * We need to re-evaluate all the attributes on every change detection cycle, because
     * by using a HostBinding we run into certain edge cases. This means that whatever logic
     * is in here has to be super lean or we risk seriously damaging or destroying the performance.
     *
     * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1317
     * Material issue: https://github.com/angular/components/issues/14024
     */
    /**
     * We need to re-evaluate all the attributes on every change detection cycle, because
     * by using a HostBinding we run into certain edge cases. This means that whatever logic
     * is in here has to be super lean or we risk seriously damaging or destroying the performance.
     *
     * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1317
     * Material issue: https://github.com/angular/components/issues/14024
     * @return {?}
     */
    FormlyAttributes.prototype.ngDoCheck = /**
     * We need to re-evaluate all the attributes on every change detection cycle, because
     * by using a HostBinding we run into certain edge cases. This means that whatever logic
     * is in here has to be super lean or we risk seriously damaging or destroying the performance.
     *
     * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1317
     * Material issue: https://github.com/angular/components/issues/14024
     * @return {?}
     */
    function () {
        var _this = this;
        this.uiAttributes.forEach((/**
         * @param {?} attr
         * @return {?}
         */
        function (attr) {
            /** @type {?} */
            var value = _this.to[attr];
            if (_this.uiAttributesCache[attr] !== value) {
                _this.uiAttributesCache[attr] = value;
                if (value || value === 0) {
                    _this.setAttribute(attr, value === true ? attr : "" + value);
                }
                else {
                    _this.removeAttribute(attr);
                }
            }
        }));
    };
    /**
     * @return {?}
     */
    FormlyAttributes.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.uiEvents.listeners.forEach((/**
         * @param {?} listener
         * @return {?}
         */
        function (listener) { return listener(); }));
        this.detachElementRef(this.field);
    };
    /**
     * @param {?} value
     * @return {?}
     */
    FormlyAttributes.prototype.toggleFocus = /**
     * @param {?} value
     * @return {?}
     */
    function (value) {
        var _this = this;
        /** @type {?} */
        var element = this.fieldAttrElements ? this.fieldAttrElements[0] : null;
        if (!element || !element.nativeElement.focus) {
            return;
        }
        /** @type {?} */
        var isFocused = !!this.document.activeElement
            && this.fieldAttrElements
                .some((/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var nativeElement = _a.nativeElement;
                return _this.document.activeElement === nativeElement || nativeElement.contains(_this.document.activeElement);
            }));
        if (value && !isFocused) {
            element.nativeElement.focus();
        }
        else if (!value && isFocused) {
            element.nativeElement.blur();
        }
    };
    /**
     * @param {?} $event
     * @return {?}
     */
    FormlyAttributes.prototype.onFocus = /**
     * @param {?} $event
     * @return {?}
     */
    function ($event) {
        this.field['___$focus'] = true;
        if (this.to.focus) {
            this.to.focus(this.field, $event);
        }
    };
    /**
     * @param {?} $event
     * @return {?}
     */
    FormlyAttributes.prototype.onBlur = /**
     * @param {?} $event
     * @return {?}
     */
    function ($event) {
        this.field['___$focus'] = false;
        if (this.to.blur) {
            this.to.blur(this.field, $event);
        }
    };
    /**
     * @param {?} $event
     * @return {?}
     */
    FormlyAttributes.prototype.onChange = /**
     * @param {?} $event
     * @return {?}
     */
    function ($event) {
        if (this.to.change) {
            this.to.change(this.field, $event);
        }
        if (this.field.formControl) {
            this.field.formControl.markAsDirty();
        }
    };
    /**
     * @private
     * @param {?} f
     * @return {?}
     */
    FormlyAttributes.prototype.attachElementRef = /**
     * @private
     * @param {?} f
     * @return {?}
     */
    function (f) {
        if (!f) {
            return;
        }
        if (f['_elementRefs'] && f['_elementRefs'].indexOf(this.elementRef) === -1) {
            f['_elementRefs'].push(this.elementRef);
        }
        else {
            defineHiddenProp(f, '_elementRefs', [this.elementRef]);
        }
    };
    /**
     * @private
     * @param {?} f
     * @return {?}
     */
    FormlyAttributes.prototype.detachElementRef = /**
     * @private
     * @param {?} f
     * @return {?}
     */
    function (f) {
        /** @type {?} */
        var index = f && f['_elementRefs'] ? this.fieldAttrElements.indexOf(this.elementRef) : -1;
        if (index !== -1) {
            this.field['_elementRefs'].splice(index, 1);
        }
    };
    /**
     * @private
     * @param {?} attr
     * @param {?} value
     * @return {?}
     */
    FormlyAttributes.prototype.setAttribute = /**
     * @private
     * @param {?} attr
     * @param {?} value
     * @return {?}
     */
    function (attr, value) {
        this.renderer.setAttribute(this.elementRef.nativeElement, attr, value);
    };
    /**
     * @private
     * @param {?} attr
     * @return {?}
     */
    FormlyAttributes.prototype.removeAttribute = /**
     * @private
     * @param {?} attr
     * @return {?}
     */
    function (attr) {
        this.renderer.removeAttribute(this.elementRef.nativeElement, attr);
    };
    FormlyAttributes.decorators = [
        { type: Directive, args: [{
                    selector: '[formlyAttributes]',
                    host: {
                        '(focus)': 'onFocus($event)',
                        '(blur)': 'onBlur($event)',
                        '(change)': 'onChange($event)',
                    },
                },] }
    ];
    /** @nocollapse */
    FormlyAttributes.ctorParameters = function () { return [
        { type: Renderer2 },
        { type: ElementRef },
        { type: undefined, decorators: [{ type: Inject, args: [DOCUMENT,] }] }
    ]; };
    FormlyAttributes.propDecorators = {
        field: [{ type: Input, args: ['formlyAttributes',] }],
        id: [{ type: Input }]
    };
    return FormlyAttributes;
}());
export { FormlyAttributes };
if (false) {
    /** @type {?} */
    FormlyAttributes.prototype.field;
    /** @type {?} */
    FormlyAttributes.prototype.id;
    /**
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.document;
    /**
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.uiAttributesCache;
    /**
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.uiAttributes;
    /**
     * HostBinding doesn't register listeners conditionally which may produce some perf issues.
     *
     * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1991
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.uiEvents;
    /**
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.renderer;
    /**
     * @type {?}
     * @private
     */
    FormlyAttributes.prototype.elementRef;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmF0dHJpYnV0ZXMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvbXBvbmVudHMvZm9ybWx5LmF0dHJpYnV0ZXMudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFFLFVBQVUsRUFBRSxLQUFLLEVBQTRCLFNBQVMsRUFBVyxNQUFNLEVBQWEsTUFBTSxlQUFlLENBQUM7QUFFOUgsT0FBTyxFQUFFLFlBQVksRUFBRSxnQkFBZ0IsRUFBRSxpQkFBaUIsRUFBRSxNQUFNLFVBQVUsQ0FBQztBQUM3RSxPQUFPLEVBQUUsUUFBUSxFQUFFLE1BQU0saUJBQWlCLENBQUM7QUFFM0M7SUEwQ0UsMEJBQ1UsUUFBbUIsRUFDbkIsVUFBc0IsRUFDWixTQUFjO1FBRnhCLGFBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsZUFBVSxHQUFWLFVBQVUsQ0FBWTtRQS9CeEIsc0JBQWlCLEdBQVEsRUFBRSxDQUFDO1FBQzVCLGlCQUFZLG9CQUNmLGlCQUFpQjtZQUNwQixVQUFVO1lBQ1YsYUFBYTtZQUNiLFVBQVU7WUFDVixVQUFVO1lBQ1YsTUFBTTtXQUNOOzs7Ozs7UUFPTSxhQUFRLEdBQUc7WUFDakIsU0FBUyxFQUFFLEVBQUU7WUFDYixNQUFNLEVBQUU7Z0JBQ04sT0FBTztnQkFDUCxPQUFPO2dCQUNQLFNBQVM7Z0JBQ1QsVUFBVTthQUNYO1NBQ0YsQ0FBQztRQVdBLElBQUksQ0FBQyxRQUFRLEdBQUcsU0FBUyxDQUFDO0lBQzVCLENBQUM7SUFWRCxzQkFBSSxnQ0FBRTs7OztRQUFOLGNBQWtDLE9BQU8sSUFBSSxDQUFDLEtBQUssQ0FBQyxlQUFlLElBQUksRUFBRSxDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFFNUUsc0JBQVksK0NBQWlCOzs7OztRQUE3QixjQUFnRCxPQUFPLENBQUMsSUFBSSxDQUFDLEtBQUssSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLGNBQWMs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QUM5QyxJQUFJLENBQUMsUUFBUSxDQUFDLFlBQVksQ0FBQyxJQUFJLENBQUMsVUFBVSxDQUFDLGFBQWEsRUFBRSxJQUFJLEVBQUUsS0FBSyxDQUFDLENBQUM7SUFDekUsQ0FBQzs7Ozs7O0lBRU8sMENBQWU7Ozs7O0lBQXZCLFVBQXdCLElBQVk7UUFDbEMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxlQUFlLENBQUMsSUFBSSxDQUFDLFVBQVUsQ0FBQyxhQUFhLEVBQUUsSUFBSSxDQUFDLENBQUM7SUFDckUsQ0FBQzs7Z0JBMUxGLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsb0JBQW9CO29CQUM5QixJQUFJLEVBQUU7d0JBQ0osU0FBUyxFQUFFLGlCQUFpQjt3QkFDNUIsUUFBUSxFQUFFLGdCQUFnQjt3QkFDMUIsVUFBVSxFQUFFLGtCQUFrQjtxQkFDL0I7aUJBQ0Y7Ozs7Z0JBWmdFLFNBQVM7Z0JBQXRELFVBQVU7Z0RBa0R6QixNQUFNLFNBQUMsUUFBUTs7O3dCQXBDakIsS0FBSyxTQUFDLGtCQUFrQjtxQkFDeEIsS0FBSzs7SUFpTFIsdUJBQUM7Q0FBQSxBQTNMRCxJQTJMQztTQW5MWSxnQkFBZ0I7OztJQUMzQixpQ0FBb0Q7O0lBQ3BELDhCQUFvQjs7Ozs7SUFFcEIsb0NBQTJCOzs7OztJQUMzQiw2Q0FBb0M7Ozs7O0lBQ3BDLHdDQU9FOzs7Ozs7OztJQU9GLG9DQVFFOzs7OztJQU9BLG9DQUEyQjs7Ozs7SUFDM0Isc0NBQThCIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRGlyZWN0aXZlLCBFbGVtZW50UmVmLCBJbnB1dCwgT25DaGFuZ2VzLCBTaW1wbGVDaGFuZ2VzLCBSZW5kZXJlcjIsIERvQ2hlY2ssIEluamVjdCwgT25EZXN0cm95IH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZywgRm9ybWx5VGVtcGxhdGVPcHRpb25zIH0gZnJvbSAnLi9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IHdyYXBQcm9wZXJ0eSwgZGVmaW5lSGlkZGVuUHJvcCwgRk9STUxZX1ZBTElEQVRPUlMgfSBmcm9tICcuLi91dGlscyc7XG5pbXBvcnQgeyBET0NVTUVOVCB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbic7XG5cbkBEaXJlY3RpdmUoe1xuICBzZWxlY3RvcjogJ1tmb3JtbHlBdHRyaWJ1dGVzXScsXG4gIGhvc3Q6IHtcbiAgICAnKGZvY3VzKSc6ICdvbkZvY3VzKCRldmVudCknLFxuICAgICcoYmx1ciknOiAnb25CbHVyKCRldmVudCknLFxuICAgICcoY2hhbmdlKSc6ICdvbkNoYW5nZSgkZXZlbnQpJyxcbiAgfSxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5QXR0cmlidXRlcyBpbXBsZW1lbnRzIE9uQ2hhbmdlcywgRG9DaGVjaywgT25EZXN0cm95IHtcbiAgQElucHV0KCdmb3JtbHlBdHRyaWJ1dGVzJykgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnO1xuICBASW5wdXQoKSBpZDogc3RyaW5nO1xuXG4gIHByaXZhdGUgZG9jdW1lbnQ6IERvY3VtZW50O1xuICBwcml2YXRlIHVpQXR0cmlidXRlc0NhY2hlOiBhbnkgPSB7fTtcbiAgcHJpdmF0ZSB1aUF0dHJpYnV0ZXMgPSBbXG4gICAgLi4uRk9STUxZX1ZBTElEQVRPUlMsXG4gICAgJ3RhYmluZGV4JyxcbiAgICAncGxhY2Vob2xkZXInLFxuICAgICdyZWFkb25seScsXG4gICAgJ2Rpc2FibGVkJyxcbiAgICAnc3RlcCcsXG4gIF07XG5cbiAgLyoqXG4gICAqIEhvc3RCaW5kaW5nIGRvZXNuJ3QgcmVnaXN0ZXIgbGlzdGVuZXJzIGNvbmRpdGlvbmFsbHkgd2hpY2ggbWF5IHByb2R1Y2Ugc29tZSBwZXJmIGlzc3Vlcy5cbiAgICpcbiAgICogRm9ybWx5IGlzc3VlOiBodHRwczovL2dpdGh1Yi5jb20vbmd4LWZvcm1seS9uZ3gtZm9ybWx5L2lzc3Vlcy8xOTkxXG4gICAqL1xuICBwcml2YXRlIHVpRXZlbnRzID0ge1xuICAgIGxpc3RlbmVyczogW10sXG4gICAgZXZlbnRzOiBbXG4gICAgICAnY2xpY2snLFxuICAgICAgJ2tleXVwJyxcbiAgICAgICdrZXlkb3duJyxcbiAgICAgICdrZXlwcmVzcycsXG4gICAgXSxcbiAgfTtcblxuICBnZXQgdG8oKTogRm9ybWx5VGVtcGxhdGVPcHRpb25zIHsgcmV0dXJuIHRoaXMuZmllbGQudGVtcGxhdGVPcHRpb25zIHx8IHt9OyB9XG5cbiAgcHJpdmF0ZSBnZXQgZmllbGRBdHRyRWxlbWVudHMoKTogRWxlbWVudFJlZltdIHsgcmV0dXJuICh0aGlzLmZpZWxkICYmIHRoaXMuZmllbGRbJ19lbGVtZW50UmVmcyddKSB8fCBbXTsgfVxuXG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgcmVuZGVyZXI6IFJlbmRlcmVyMixcbiAgICBwcml2YXRlIGVsZW1lbnRSZWY6IEVsZW1lbnRSZWYsXG4gICAgQEluamVjdChET0NVTUVOVCkgX2RvY3VtZW50OiBhbnksXG4gICkge1xuICAgIHRoaXMuZG9jdW1lbnQgPSBfZG9jdW1lbnQ7XG4gIH1cblxuICBuZ09uQ2hhbmdlcyhjaGFuZ2VzOiBTaW1wbGVDaGFuZ2VzKSB7XG4gICAgaWYgKGNoYW5nZXMuZmllbGQpIHtcbiAgICAgIHRoaXMuZmllbGQubmFtZSAmJiB0aGlzLnNldEF0dHJpYnV0ZSgnbmFtZScsIHRoaXMuZmllbGQubmFtZSk7XG4gICAgICB0aGlzLnVpRXZlbnRzLmxpc3RlbmVycy5mb3JFYWNoKGxpc3RlbmVyID0+IGxpc3RlbmVyKCkpO1xuICAgICAgdGhpcy51aUV2ZW50cy5ldmVudHMuZm9yRWFjaChldmVudE5hbWUgPT4ge1xuICAgICAgICBpZiAodGhpcy50byAmJiB0aGlzLnRvW2V2ZW50TmFtZV0pIHtcbiAgICAgICAgICB0aGlzLnVpRXZlbnRzLmxpc3RlbmVycy5wdXNoKFxuICAgICAgICAgICAgdGhpcy5yZW5kZXJlci5saXN0ZW4oXG4gICAgICAgICAgICAgIHRoaXMuZWxlbWVudFJlZi5uYXRpdmVFbGVtZW50LFxuICAgICAgICAgICAgICBldmVudE5hbWUsXG4gICAgICAgICAgICAgIChlKSA9PiB0aGlzLnRvW2V2ZW50TmFtZV0odGhpcy5maWVsZCwgZSksXG4gICAgICAgICAgICApLFxuICAgICAgICAgICk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgICBpZiAodGhpcy50byAmJiB0aGlzLnRvLmF0dHJpYnV0ZXMpIHtcbiAgICAgICAgd3JhcFByb3BlcnR5KHRoaXMudG8sICdhdHRyaWJ1dGVzJywgKHsgY3VycmVudFZhbHVlLCBwcmV2aW91c1ZhbHVlIH0pID0+IHtcbiAgICAgICAgICBpZiAocHJldmlvdXNWYWx1ZSkge1xuICAgICAgICAgICAgT2JqZWN0LmtleXMocHJldmlvdXNWYWx1ZSkuZm9yRWFjaChhdHRyID0+IHRoaXMucmVtb3ZlQXR0cmlidXRlKGF0dHIpKTtcbiAgICAgICAgICB9XG5cbiAgICAgICAgICBpZiAoY3VycmVudFZhbHVlKSB7XG4gICAgICAgICAgICBPYmplY3Qua2V5cyhjdXJyZW50VmFsdWUpLmZvckVhY2goYXR0ciA9PiB0aGlzLnNldEF0dHJpYnV0ZShhdHRyLCBjdXJyZW50VmFsdWVbYXR0cl0pKTtcbiAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgfVxuXG4gICAgICB0aGlzLmRldGFjaEVsZW1lbnRSZWYoY2hhbmdlcy5maWVsZC5wcmV2aW91c1ZhbHVlKTtcbiAgICAgIHRoaXMuYXR0YWNoRWxlbWVudFJlZihjaGFuZ2VzLmZpZWxkLmN1cnJlbnRWYWx1ZSk7XG4gICAgICBpZiAodGhpcy5maWVsZEF0dHJFbGVtZW50cy5sZW5ndGggPT09IDEpIHtcbiAgICAgICAgIXRoaXMuaWQgJiYgdGhpcy5maWVsZC5pZCAmJiB0aGlzLnNldEF0dHJpYnV0ZSgnaWQnLCB0aGlzLmZpZWxkLmlkKTtcbiAgICAgICAgd3JhcFByb3BlcnR5KHRoaXMuZmllbGQsICdmb2N1cycsICh7IGN1cnJlbnRWYWx1ZSB9KSA9PiB7XG4gICAgICAgICAgdGhpcy50b2dnbGVGb2N1cyhjdXJyZW50VmFsdWUpO1xuICAgICAgICB9KTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICBpZiAoY2hhbmdlcy5pZCkge1xuICAgICAgdGhpcy5zZXRBdHRyaWJ1dGUoJ2lkJywgdGhpcy5pZCk7XG4gICAgfVxuICB9XG5cbiAgLyoqXG4gICAqIFdlIG5lZWQgdG8gcmUtZXZhbHVhdGUgYWxsIHRoZSBhdHRyaWJ1dGVzIG9uIGV2ZXJ5IGNoYW5nZSBkZXRlY3Rpb24gY3ljbGUsIGJlY2F1c2VcbiAgICogYnkgdXNpbmcgYSBIb3N0QmluZGluZyB3ZSBydW4gaW50byBjZXJ0YWluIGVkZ2UgY2FzZXMuIFRoaXMgbWVhbnMgdGhhdCB3aGF0ZXZlciBsb2dpY1xuICAgKiBpcyBpbiBoZXJlIGhhcyB0byBiZSBzdXBlciBsZWFuIG9yIHdlIHJpc2sgc2VyaW91c2x5IGRhbWFnaW5nIG9yIGRlc3Ryb3lpbmcgdGhlIHBlcmZvcm1hbmNlLlxuICAgKlxuICAgKiBGb3JtbHkgaXNzdWU6IGh0dHBzOi8vZ2l0aHViLmNvbS9uZ3gtZm9ybWx5L25neC1mb3JtbHkvaXNzdWVzLzEzMTdcbiAgICogTWF0ZXJpYWwgaXNzdWU6IGh0dHBzOi8vZ2l0aHViLmNvbS9hbmd1bGFyL2NvbXBvbmVudHMvaXNzdWVzLzE0MDI0XG4gICAqL1xuICBuZ0RvQ2hlY2soKSB7XG4gICAgdGhpcy51aUF0dHJpYnV0ZXMuZm9yRWFjaChhdHRyID0+IHtcbiAgICAgIGNvbnN0IHZhbHVlID0gdGhpcy50b1thdHRyXTtcbiAgICAgIGlmICh0aGlzLnVpQXR0cmlidXRlc0NhY2hlW2F0dHJdICE9PSB2YWx1ZSkge1xuICAgICAgICB0aGlzLnVpQXR0cmlidXRlc0NhY2hlW2F0dHJdID0gdmFsdWU7XG4gICAgICAgIGlmICh2YWx1ZSB8fCB2YWx1ZSA9PT0gMCkge1xuICAgICAgICAgIHRoaXMuc2V0QXR0cmlidXRlKGF0dHIsIHZhbHVlID09PSB0cnVlID8gYXR0ciA6IGAke3ZhbHVlfWApO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHRoaXMucmVtb3ZlQXR0cmlidXRlKGF0dHIpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICBuZ09uRGVzdHJveSgpIHtcbiAgICB0aGlzLnVpRXZlbnRzLmxpc3RlbmVycy5mb3JFYWNoKGxpc3RlbmVyID0+IGxpc3RlbmVyKCkpO1xuICAgIHRoaXMuZGV0YWNoRWxlbWVudFJlZih0aGlzLmZpZWxkKTtcbiAgfVxuXG4gIHRvZ2dsZUZvY3VzKHZhbHVlOiBib29sZWFuKSB7XG4gICAgY29uc3QgZWxlbWVudCA9IHRoaXMuZmllbGRBdHRyRWxlbWVudHMgPyB0aGlzLmZpZWxkQXR0ckVsZW1lbnRzWzBdIDogbnVsbDtcbiAgICBpZiAoIWVsZW1lbnQgfHwgIWVsZW1lbnQubmF0aXZlRWxlbWVudC5mb2N1cykge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGNvbnN0IGlzRm9jdXNlZCA9ICEhdGhpcy5kb2N1bWVudC5hY3RpdmVFbGVtZW50XG4gICAgICAmJiB0aGlzLmZpZWxkQXR0ckVsZW1lbnRzXG4gICAgICAgIC5zb21lKCh7IG5hdGl2ZUVsZW1lbnQgfSkgPT4gdGhpcy5kb2N1bWVudC5hY3RpdmVFbGVtZW50ID09PSBuYXRpdmVFbGVtZW50IHx8IG5hdGl2ZUVsZW1lbnQuY29udGFpbnModGhpcy5kb2N1bWVudC5hY3RpdmVFbGVtZW50KSk7XG5cbiAgICBpZiAodmFsdWUgJiYgIWlzRm9jdXNlZCkge1xuICAgICAgZWxlbWVudC5uYXRpdmVFbGVtZW50LmZvY3VzKCk7XG4gICAgfSBlbHNlIGlmICghdmFsdWUgJiYgaXNGb2N1c2VkKSB7XG4gICAgICBlbGVtZW50Lm5hdGl2ZUVsZW1lbnQuYmx1cigpO1xuICAgIH1cbiAgfVxuXG4gIG9uRm9jdXMoJGV2ZW50OiBhbnkpIHtcbiAgICB0aGlzLmZpZWxkWydfX18kZm9jdXMnXSA9IHRydWU7XG4gICAgaWYgKHRoaXMudG8uZm9jdXMpIHtcbiAgICAgIHRoaXMudG8uZm9jdXModGhpcy5maWVsZCwgJGV2ZW50KTtcbiAgICB9XG4gIH1cblxuICBvbkJsdXIoJGV2ZW50OiBhbnkpIHtcbiAgICB0aGlzLmZpZWxkWydfX18kZm9jdXMnXSA9IGZhbHNlO1xuICAgIGlmICh0aGlzLnRvLmJsdXIpIHtcbiAgICAgIHRoaXMudG8uYmx1cih0aGlzLmZpZWxkLCAkZXZlbnQpO1xuICAgIH1cbiAgfVxuXG4gIG9uQ2hhbmdlKCRldmVudDogYW55KSB7XG4gICAgaWYgKHRoaXMudG8uY2hhbmdlKSB7XG4gICAgICB0aGlzLnRvLmNoYW5nZSh0aGlzLmZpZWxkLCAkZXZlbnQpO1xuICAgIH1cblxuICAgIGlmICh0aGlzLmZpZWxkLmZvcm1Db250cm9sKSB7XG4gICAgICB0aGlzLmZpZWxkLmZvcm1Db250cm9sLm1hcmtBc0RpcnR5KCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBhdHRhY2hFbGVtZW50UmVmKGY6IEZvcm1seUZpZWxkQ29uZmlnKSB7XG4gICAgaWYgKCFmKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgaWYgKGZbJ19lbGVtZW50UmVmcyddICYmIGZbJ19lbGVtZW50UmVmcyddLmluZGV4T2YodGhpcy5lbGVtZW50UmVmKSA9PT0gLTEpIHtcbiAgICAgIGZbJ19lbGVtZW50UmVmcyddLnB1c2godGhpcy5lbGVtZW50UmVmKTtcbiAgICB9IGVsc2Uge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcChmLCAnX2VsZW1lbnRSZWZzJywgW3RoaXMuZWxlbWVudFJlZl0pO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgZGV0YWNoRWxlbWVudFJlZihmOiBGb3JtbHlGaWVsZENvbmZpZykge1xuICAgIGNvbnN0IGluZGV4ID0gZiAmJiBmWydfZWxlbWVudFJlZnMnXSA/IHRoaXMuZmllbGRBdHRyRWxlbWVudHMuaW5kZXhPZih0aGlzLmVsZW1lbnRSZWYpIDogLTE7XG4gICAgaWYgKGluZGV4ICE9PSAtMSkge1xuICAgICAgdGhpcy5maWVsZFsnX2VsZW1lbnRSZWZzJ10uc3BsaWNlKGluZGV4LCAxKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHNldEF0dHJpYnV0ZShhdHRyOiBzdHJpbmcsIHZhbHVlOiBzdHJpbmcpIHtcbiAgICB0aGlzLnJlbmRlcmVyLnNldEF0dHJpYnV0ZSh0aGlzLmVsZW1lbnRSZWYubmF0aXZlRWxlbWVudCwgYXR0ciwgdmFsdWUpO1xuICB9XG5cbiAgcHJpdmF0ZSByZW1vdmVBdHRyaWJ1dGUoYXR0cjogc3RyaW5nKSB7XG4gICAgdGhpcy5yZW5kZXJlci5yZW1vdmVBdHRyaWJ1dGUodGhpcy5lbGVtZW50UmVmLm5hdGl2ZUVsZW1lbnQsIGF0dHIpO1xuICB9XG59XG4iXX0=