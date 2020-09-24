/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Directive, ElementRef, Input, Renderer2, Inject } from '@angular/core';
import { wrapProperty, defineHiddenProp, FORMLY_VALIDATORS } from '../utils';
import { DOCUMENT } from '@angular/common';
export class FormlyAttributes {
    /**
     * @param {?} renderer
     * @param {?} elementRef
     * @param {?} _document
     */
    constructor(renderer, elementRef, _document) {
        this.renderer = renderer;
        this.elementRef = elementRef;
        this.uiAttributesCache = {};
        this.uiAttributes = [
            ...FORMLY_VALIDATORS,
            'tabindex',
            'placeholder',
            'readonly',
            'disabled',
            'step',
        ];
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
    /**
     * @return {?}
     */
    get to() { return this.field.templateOptions || {}; }
    /**
     * @private
     * @return {?}
     */
    get fieldAttrElements() { return (this.field && this.field['_elementRefs']) || []; }
    /**
     * @param {?} changes
     * @return {?}
     */
    ngOnChanges(changes) {
        if (changes.field) {
            this.field.name && this.setAttribute('name', this.field.name);
            this.uiEvents.listeners.forEach((/**
             * @param {?} listener
             * @return {?}
             */
            listener => listener()));
            this.uiEvents.events.forEach((/**
             * @param {?} eventName
             * @return {?}
             */
            eventName => {
                if (this.to && this.to[eventName]) {
                    this.uiEvents.listeners.push(this.renderer.listen(this.elementRef.nativeElement, eventName, (/**
                     * @param {?} e
                     * @return {?}
                     */
                    (e) => this.to[eventName](this.field, e))));
                }
            }));
            if (this.to && this.to.attributes) {
                wrapProperty(this.to, 'attributes', (/**
                 * @param {?} __0
                 * @return {?}
                 */
                ({ currentValue, previousValue }) => {
                    if (previousValue) {
                        Object.keys(previousValue).forEach((/**
                         * @param {?} attr
                         * @return {?}
                         */
                        attr => this.removeAttribute(attr)));
                    }
                    if (currentValue) {
                        Object.keys(currentValue).forEach((/**
                         * @param {?} attr
                         * @return {?}
                         */
                        attr => this.setAttribute(attr, currentValue[attr])));
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
                ({ currentValue }) => {
                    this.toggleFocus(currentValue);
                }));
            }
        }
        if (changes.id) {
            this.setAttribute('id', this.id);
        }
    }
    /**
     * We need to re-evaluate all the attributes on every change detection cycle, because
     * by using a HostBinding we run into certain edge cases. This means that whatever logic
     * is in here has to be super lean or we risk seriously damaging or destroying the performance.
     *
     * Formly issue: https://github.com/ngx-formly/ngx-formly/issues/1317
     * Material issue: https://github.com/angular/components/issues/14024
     * @return {?}
     */
    ngDoCheck() {
        this.uiAttributes.forEach((/**
         * @param {?} attr
         * @return {?}
         */
        attr => {
            /** @type {?} */
            const value = this.to[attr];
            if (this.uiAttributesCache[attr] !== value) {
                this.uiAttributesCache[attr] = value;
                if (value || value === 0) {
                    this.setAttribute(attr, value === true ? attr : `${value}`);
                }
                else {
                    this.removeAttribute(attr);
                }
            }
        }));
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.uiEvents.listeners.forEach((/**
         * @param {?} listener
         * @return {?}
         */
        listener => listener()));
        this.detachElementRef(this.field);
    }
    /**
     * @param {?} value
     * @return {?}
     */
    toggleFocus(value) {
        /** @type {?} */
        const element = this.fieldAttrElements ? this.fieldAttrElements[0] : null;
        if (!element || !element.nativeElement.focus) {
            return;
        }
        /** @type {?} */
        const isFocused = !!this.document.activeElement
            && this.fieldAttrElements
                .some((/**
             * @param {?} __0
             * @return {?}
             */
            ({ nativeElement }) => this.document.activeElement === nativeElement || nativeElement.contains(this.document.activeElement)));
        if (value && !isFocused) {
            element.nativeElement.focus();
        }
        else if (!value && isFocused) {
            element.nativeElement.blur();
        }
    }
    /**
     * @param {?} $event
     * @return {?}
     */
    onFocus($event) {
        this.field['___$focus'] = true;
        if (this.to.focus) {
            this.to.focus(this.field, $event);
        }
    }
    /**
     * @param {?} $event
     * @return {?}
     */
    onBlur($event) {
        this.field['___$focus'] = false;
        if (this.to.blur) {
            this.to.blur(this.field, $event);
        }
    }
    /**
     * @param {?} $event
     * @return {?}
     */
    onChange($event) {
        if (this.to.change) {
            this.to.change(this.field, $event);
        }
        if (this.field.formControl) {
            this.field.formControl.markAsDirty();
        }
    }
    /**
     * @private
     * @param {?} f
     * @return {?}
     */
    attachElementRef(f) {
        if (!f) {
            return;
        }
        if (f['_elementRefs'] && f['_elementRefs'].indexOf(this.elementRef) === -1) {
            f['_elementRefs'].push(this.elementRef);
        }
        else {
            defineHiddenProp(f, '_elementRefs', [this.elementRef]);
        }
    }
    /**
     * @private
     * @param {?} f
     * @return {?}
     */
    detachElementRef(f) {
        /** @type {?} */
        const index = f && f['_elementRefs'] ? this.fieldAttrElements.indexOf(this.elementRef) : -1;
        if (index !== -1) {
            this.field['_elementRefs'].splice(index, 1);
        }
    }
    /**
     * @private
     * @param {?} attr
     * @param {?} value
     * @return {?}
     */
    setAttribute(attr, value) {
        this.renderer.setAttribute(this.elementRef.nativeElement, attr, value);
    }
    /**
     * @private
     * @param {?} attr
     * @return {?}
     */
    removeAttribute(attr) {
        this.renderer.removeAttribute(this.elementRef.nativeElement, attr);
    }
}
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
FormlyAttributes.ctorParameters = () => [
    { type: Renderer2 },
    { type: ElementRef },
    { type: undefined, decorators: [{ type: Inject, args: [DOCUMENT,] }] }
];
FormlyAttributes.propDecorators = {
    field: [{ type: Input, args: ['formlyAttributes',] }],
    id: [{ type: Input }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmF0dHJpYnV0ZXMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvbXBvbmVudHMvZm9ybWx5LmF0dHJpYnV0ZXMudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsVUFBVSxFQUFFLEtBQUssRUFBNEIsU0FBUyxFQUFXLE1BQU0sRUFBYSxNQUFNLGVBQWUsQ0FBQztBQUU5SCxPQUFPLEVBQUUsWUFBWSxFQUFFLGdCQUFnQixFQUFFLGlCQUFpQixFQUFFLE1BQU0sVUFBVSxDQUFDO0FBQzdFLE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSxpQkFBaUIsQ0FBQztBQVUzQyxNQUFNLE9BQU8sZ0JBQWdCOzs7Ozs7SUFrQzNCLFlBQ1UsUUFBbUIsRUFDbkIsVUFBc0IsRUFDWixTQUFjO1FBRnhCLGFBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsZUFBVSxHQUFWLFVBQVUsQ0FBWTtRQS9CeEIsc0JBQWlCLEdBQVEsRUFBRSxDQUFDO1FBQzVCLGlCQUFZLEdBQUc7WUFDckIsR0FBRyxpQkFBaUI7WUFDcEIsVUFBVTtZQUNWLGFBQWE7WUFDYixVQUFVO1lBQ1YsVUFBVTtZQUNWLE1BQU07U0FDUCxDQUFDOzs7Ozs7UUFPTSxhQUFRLEdBQUc7WUFDakIsU0FBUyxFQUFFLEVBQUU7WUFDYixNQUFNLEVBQUU7Z0JBQ04sT0FBTztnQkFDUCxPQUFPO2dCQUNQLFNBQVM7Z0JBQ1QsVUFBVTthQUNYO1NBQ0YsQ0FBQztRQVdBLElBQUksQ0FBQyxRQUFRLEdBQUcsU0FBUyxDQUFDO0lBQzVCLENBQUM7Ozs7SUFWRCxJQUFJLEVBQUUsS0FBNEIsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsSUFBSSxFQUFFLENBQUMsQ0FBQyxDQUFDOzs7OztJQUU1RSxJQUFZLGlCQUFpQixLQUFtQixPQUFPLENBQUMsSUFBSSxDQUFDLEtBQUssSUFBSSxJQUFJLENBQUMs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QyxhQUFhLEVBQUUsSUFBSSxDQUFDLENBQUM7SUFDckUsQ0FBQzs7O1lBMUxGLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUsb0JBQW9CO2dCQUM5QixJQUFJLEVBQUU7b0JBQ0osU0FBUyxFQUFFLGlCQUFpQjtvQkFDNUIsUUFBUSxFQUFFLGdCQUFnQjtvQkFDMUIsVUFBVSxFQUFFLGtCQUFrQjtpQkFDL0I7YUFDRjs7OztZQVpnRSxTQUFTO1lBQXRELFVBQVU7NENBa0R6QixNQUFNLFNBQUMsUUFBUTs7O29CQXBDakIsS0FBSyxTQUFDLGtCQUFrQjtpQkFDeEIsS0FBSzs7OztJQUROLGlDQUFvRDs7SUFDcEQsOEJBQW9COzs7OztJQUVwQixvQ0FBMkI7Ozs7O0lBQzNCLDZDQUFvQzs7Ozs7SUFDcEMsd0NBT0U7Ozs7Ozs7O0lBT0Ysb0NBUUU7Ozs7O0lBT0Esb0NBQTJCOzs7OztJQUMzQixzQ0FBOEIiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBEaXJlY3RpdmUsIEVsZW1lbnRSZWYsIElucHV0LCBPbkNoYW5nZXMsIFNpbXBsZUNoYW5nZXMsIFJlbmRlcmVyMiwgRG9DaGVjaywgSW5qZWN0LCBPbkRlc3Ryb3kgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnLCBGb3JtbHlUZW1wbGF0ZU9wdGlvbnMgfSBmcm9tICcuL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgd3JhcFByb3BlcnR5LCBkZWZpbmVIaWRkZW5Qcm9wLCBGT1JNTFlfVkFMSURBVE9SUyB9IGZyb20gJy4uL3V0aWxzJztcbmltcG9ydCB7IERPQ1VNRU5UIH0gZnJvbSAnQGFuZ3VsYXIvY29tbW9uJztcblxuQERpcmVjdGl2ZSh7XG4gIHNlbGVjdG9yOiAnW2Zvcm1seUF0dHJpYnV0ZXNdJyxcbiAgaG9zdDoge1xuICAgICcoZm9jdXMpJzogJ29uRm9jdXMoJGV2ZW50KScsXG4gICAgJyhibHVyKSc6ICdvbkJsdXIoJGV2ZW50KScsXG4gICAgJyhjaGFuZ2UpJzogJ29uQ2hhbmdlKCRldmVudCknLFxuICB9LFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlBdHRyaWJ1dGVzIGltcGxlbWVudHMgT25DaGFuZ2VzLCBEb0NoZWNrLCBPbkRlc3Ryb3kge1xuICBASW5wdXQoJ2Zvcm1seUF0dHJpYnV0ZXMnKSBmaWVsZDogRm9ybWx5RmllbGRDb25maWc7XG4gIEBJbnB1dCgpIGlkOiBzdHJpbmc7XG5cbiAgcHJpdmF0ZSBkb2N1bWVudDogRG9jdW1lbnQ7XG4gIHByaXZhdGUgdWlBdHRyaWJ1dGVzQ2FjaGU6IGFueSA9IHt9O1xuICBwcml2YXRlIHVpQXR0cmlidXRlcyA9IFtcbiAgICAuLi5GT1JNTFlfVkFMSURBVE9SUyxcbiAgICAndGFiaW5kZXgnLFxuICAgICdwbGFjZWhvbGRlcicsXG4gICAgJ3JlYWRvbmx5JyxcbiAgICAnZGlzYWJsZWQnLFxuICAgICdzdGVwJyxcbiAgXTtcblxuICAvKipcbiAgICogSG9zdEJpbmRpbmcgZG9lc24ndCByZWdpc3RlciBsaXN0ZW5lcnMgY29uZGl0aW9uYWxseSB3aGljaCBtYXkgcHJvZHVjZSBzb21lIHBlcmYgaXNzdWVzLlxuICAgKlxuICAgKiBGb3JtbHkgaXNzdWU6IGh0dHBzOi8vZ2l0aHViLmNvbS9uZ3gtZm9ybWx5L25neC1mb3JtbHkvaXNzdWVzLzE5OTFcbiAgICovXG4gIHByaXZhdGUgdWlFdmVudHMgPSB7XG4gICAgbGlzdGVuZXJzOiBbXSxcbiAgICBldmVudHM6IFtcbiAgICAgICdjbGljaycsXG4gICAgICAna2V5dXAnLFxuICAgICAgJ2tleWRvd24nLFxuICAgICAgJ2tleXByZXNzJyxcbiAgICBdLFxuICB9O1xuXG4gIGdldCB0bygpOiBGb3JtbHlUZW1wbGF0ZU9wdGlvbnMgeyByZXR1cm4gdGhpcy5maWVsZC50ZW1wbGF0ZU9wdGlvbnMgfHwge307IH1cblxuICBwcml2YXRlIGdldCBmaWVsZEF0dHJFbGVtZW50cygpOiBFbGVtZW50UmVmW10geyByZXR1cm4gKHRoaXMuZmllbGQgJiYgdGhpcy5maWVsZFsnX2VsZW1lbnRSZWZzJ10pIHx8IFtdOyB9XG5cbiAgY29uc3RydWN0b3IoXG4gICAgcHJpdmF0ZSByZW5kZXJlcjogUmVuZGVyZXIyLFxuICAgIHByaXZhdGUgZWxlbWVudFJlZjogRWxlbWVudFJlZixcbiAgICBASW5qZWN0KERPQ1VNRU5UKSBfZG9jdW1lbnQ6IGFueSxcbiAgKSB7XG4gICAgdGhpcy5kb2N1bWVudCA9IF9kb2N1bWVudDtcbiAgfVxuXG4gIG5nT25DaGFuZ2VzKGNoYW5nZXM6IFNpbXBsZUNoYW5nZXMpIHtcbiAgICBpZiAoY2hhbmdlcy5maWVsZCkge1xuICAgICAgdGhpcy5maWVsZC5uYW1lICYmIHRoaXMuc2V0QXR0cmlidXRlKCduYW1lJywgdGhpcy5maWVsZC5uYW1lKTtcbiAgICAgIHRoaXMudWlFdmVudHMubGlzdGVuZXJzLmZvckVhY2gobGlzdGVuZXIgPT4gbGlzdGVuZXIoKSk7XG4gICAgICB0aGlzLnVpRXZlbnRzLmV2ZW50cy5mb3JFYWNoKGV2ZW50TmFtZSA9PiB7XG4gICAgICAgIGlmICh0aGlzLnRvICYmIHRoaXMudG9bZXZlbnROYW1lXSkge1xuICAgICAgICAgIHRoaXMudWlFdmVudHMubGlzdGVuZXJzLnB1c2goXG4gICAgICAgICAgICB0aGlzLnJlbmRlcmVyLmxpc3RlbihcbiAgICAgICAgICAgICAgdGhpcy5lbGVtZW50UmVmLm5hdGl2ZUVsZW1lbnQsXG4gICAgICAgICAgICAgIGV2ZW50TmFtZSxcbiAgICAgICAgICAgICAgKGUpID0+IHRoaXMudG9bZXZlbnROYW1lXSh0aGlzLmZpZWxkLCBlKSxcbiAgICAgICAgICAgICksXG4gICAgICAgICAgKTtcbiAgICAgICAgfVxuICAgICAgfSk7XG5cbiAgICAgIGlmICh0aGlzLnRvICYmIHRoaXMudG8uYXR0cmlidXRlcykge1xuICAgICAgICB3cmFwUHJvcGVydHkodGhpcy50bywgJ2F0dHJpYnV0ZXMnLCAoeyBjdXJyZW50VmFsdWUsIHByZXZpb3VzVmFsdWUgfSkgPT4ge1xuICAgICAgICAgIGlmIChwcmV2aW91c1ZhbHVlKSB7XG4gICAgICAgICAgICBPYmplY3Qua2V5cyhwcmV2aW91c1ZhbHVlKS5mb3JFYWNoKGF0dHIgPT4gdGhpcy5yZW1vdmVBdHRyaWJ1dGUoYXR0cikpO1xuICAgICAgICAgIH1cblxuICAgICAgICAgIGlmIChjdXJyZW50VmFsdWUpIHtcbiAgICAgICAgICAgIE9iamVjdC5rZXlzKGN1cnJlbnRWYWx1ZSkuZm9yRWFjaChhdHRyID0+IHRoaXMuc2V0QXR0cmlidXRlKGF0dHIsIGN1cnJlbnRWYWx1ZVthdHRyXSkpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICB9XG5cbiAgICAgIHRoaXMuZGV0YWNoRWxlbWVudFJlZihjaGFuZ2VzLmZpZWxkLnByZXZpb3VzVmFsdWUpO1xuICAgICAgdGhpcy5hdHRhY2hFbGVtZW50UmVmKGNoYW5nZXMuZmllbGQuY3VycmVudFZhbHVlKTtcbiAgICAgIGlmICh0aGlzLmZpZWxkQXR0ckVsZW1lbnRzLmxlbmd0aCA9PT0gMSkge1xuICAgICAgICAhdGhpcy5pZCAmJiB0aGlzLmZpZWxkLmlkICYmIHRoaXMuc2V0QXR0cmlidXRlKCdpZCcsIHRoaXMuZmllbGQuaWQpO1xuICAgICAgICB3cmFwUHJvcGVydHkodGhpcy5maWVsZCwgJ2ZvY3VzJywgKHsgY3VycmVudFZhbHVlIH0pID0+IHtcbiAgICAgICAgICB0aGlzLnRvZ2dsZUZvY3VzKGN1cnJlbnRWYWx1ZSk7XG4gICAgICAgIH0pO1xuICAgICAgfVxuICAgIH1cblxuICAgIGlmIChjaGFuZ2VzLmlkKSB7XG4gICAgICB0aGlzLnNldEF0dHJpYnV0ZSgnaWQnLCB0aGlzLmlkKTtcbiAgICB9XG4gIH1cblxuICAvKipcbiAgICogV2UgbmVlZCB0byByZS1ldmFsdWF0ZSBhbGwgdGhlIGF0dHJpYnV0ZXMgb24gZXZlcnkgY2hhbmdlIGRldGVjdGlvbiBjeWNsZSwgYmVjYXVzZVxuICAgKiBieSB1c2luZyBhIEhvc3RCaW5kaW5nIHdlIHJ1biBpbnRvIGNlcnRhaW4gZWRnZSBjYXNlcy4gVGhpcyBtZWFucyB0aGF0IHdoYXRldmVyIGxvZ2ljXG4gICAqIGlzIGluIGhlcmUgaGFzIHRvIGJlIHN1cGVyIGxlYW4gb3Igd2UgcmlzayBzZXJpb3VzbHkgZGFtYWdpbmcgb3IgZGVzdHJveWluZyB0aGUgcGVyZm9ybWFuY2UuXG4gICAqXG4gICAqIEZvcm1seSBpc3N1ZTogaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMTMxN1xuICAgKiBNYXRlcmlhbCBpc3N1ZTogaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvY29tcG9uZW50cy9pc3N1ZXMvMTQwMjRcbiAgICovXG4gIG5nRG9DaGVjaygpIHtcbiAgICB0aGlzLnVpQXR0cmlidXRlcy5mb3JFYWNoKGF0dHIgPT4ge1xuICAgICAgY29uc3QgdmFsdWUgPSB0aGlzLnRvW2F0dHJdO1xuICAgICAgaWYgKHRoaXMudWlBdHRyaWJ1dGVzQ2FjaGVbYXR0cl0gIT09IHZhbHVlKSB7XG4gICAgICAgIHRoaXMudWlBdHRyaWJ1dGVzQ2FjaGVbYXR0cl0gPSB2YWx1ZTtcbiAgICAgICAgaWYgKHZhbHVlIHx8IHZhbHVlID09PSAwKSB7XG4gICAgICAgICAgdGhpcy5zZXRBdHRyaWJ1dGUoYXR0ciwgdmFsdWUgPT09IHRydWUgPyBhdHRyIDogYCR7dmFsdWV9YCk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgdGhpcy5yZW1vdmVBdHRyaWJ1dGUoYXR0cik7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9KTtcbiAgfVxuXG4gIG5nT25EZXN0cm95KCkge1xuICAgIHRoaXMudWlFdmVudHMubGlzdGVuZXJzLmZvckVhY2gobGlzdGVuZXIgPT4gbGlzdGVuZXIoKSk7XG4gICAgdGhpcy5kZXRhY2hFbGVtZW50UmVmKHRoaXMuZmllbGQpO1xuICB9XG5cbiAgdG9nZ2xlRm9jdXModmFsdWU6IGJvb2xlYW4pIHtcbiAgICBjb25zdCBlbGVtZW50ID0gdGhpcy5maWVsZEF0dHJFbGVtZW50cyA/IHRoaXMuZmllbGRBdHRyRWxlbWVudHNbMF0gOiBudWxsO1xuICAgIGlmICghZWxlbWVudCB8fCAhZWxlbWVudC5uYXRpdmVFbGVtZW50LmZvY3VzKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgY29uc3QgaXNGb2N1c2VkID0gISF0aGlzLmRvY3VtZW50LmFjdGl2ZUVsZW1lbnRcbiAgICAgICYmIHRoaXMuZmllbGRBdHRyRWxlbWVudHNcbiAgICAgICAgLnNvbWUoKHsgbmF0aXZlRWxlbWVudCB9KSA9PiB0aGlzLmRvY3VtZW50LmFjdGl2ZUVsZW1lbnQgPT09IG5hdGl2ZUVsZW1lbnQgfHwgbmF0aXZlRWxlbWVudC5jb250YWlucyh0aGlzLmRvY3VtZW50LmFjdGl2ZUVsZW1lbnQpKTtcblxuICAgIGlmICh2YWx1ZSAmJiAhaXNGb2N1c2VkKSB7XG4gICAgICBlbGVtZW50Lm5hdGl2ZUVsZW1lbnQuZm9jdXMoKTtcbiAgICB9IGVsc2UgaWYgKCF2YWx1ZSAmJiBpc0ZvY3VzZWQpIHtcbiAgICAgIGVsZW1lbnQubmF0aXZlRWxlbWVudC5ibHVyKCk7XG4gICAgfVxuICB9XG5cbiAgb25Gb2N1cygkZXZlbnQ6IGFueSkge1xuICAgIHRoaXMuZmllbGRbJ19fXyRmb2N1cyddID0gdHJ1ZTtcbiAgICBpZiAodGhpcy50by5mb2N1cykge1xuICAgICAgdGhpcy50by5mb2N1cyh0aGlzLmZpZWxkLCAkZXZlbnQpO1xuICAgIH1cbiAgfVxuXG4gIG9uQmx1cigkZXZlbnQ6IGFueSkge1xuICAgIHRoaXMuZmllbGRbJ19fXyRmb2N1cyddID0gZmFsc2U7XG4gICAgaWYgKHRoaXMudG8uYmx1cikge1xuICAgICAgdGhpcy50by5ibHVyKHRoaXMuZmllbGQsICRldmVudCk7XG4gICAgfVxuICB9XG5cbiAgb25DaGFuZ2UoJGV2ZW50OiBhbnkpIHtcbiAgICBpZiAodGhpcy50by5jaGFuZ2UpIHtcbiAgICAgIHRoaXMudG8uY2hhbmdlKHRoaXMuZmllbGQsICRldmVudCk7XG4gICAgfVxuXG4gICAgaWYgKHRoaXMuZmllbGQuZm9ybUNvbnRyb2wpIHtcbiAgICAgIHRoaXMuZmllbGQuZm9ybUNvbnRyb2wubWFya0FzRGlydHkoKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIGF0dGFjaEVsZW1lbnRSZWYoZjogRm9ybWx5RmllbGRDb25maWcpIHtcbiAgICBpZiAoIWYpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBpZiAoZlsnX2VsZW1lbnRSZWZzJ10gJiYgZlsnX2VsZW1lbnRSZWZzJ10uaW5kZXhPZih0aGlzLmVsZW1lbnRSZWYpID09PSAtMSkge1xuICAgICAgZlsnX2VsZW1lbnRSZWZzJ10ucHVzaCh0aGlzLmVsZW1lbnRSZWYpO1xuICAgIH0gZWxzZSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKGYsICdfZWxlbWVudFJlZnMnLCBbdGhpcy5lbGVtZW50UmVmXSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBkZXRhY2hFbGVtZW50UmVmKGY6IEZvcm1seUZpZWxkQ29uZmlnKSB7XG4gICAgY29uc3QgaW5kZXggPSBmICYmIGZbJ19lbGVtZW50UmVmcyddID8gdGhpcy5maWVsZEF0dHJFbGVtZW50cy5pbmRleE9mKHRoaXMuZWxlbWVudFJlZikgOiAtMTtcbiAgICBpZiAoaW5kZXggIT09IC0xKSB7XG4gICAgICB0aGlzLmZpZWxkWydfZWxlbWVudFJlZnMnXS5zcGxpY2UoaW5kZXgsIDEpO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgc2V0QXR0cmlidXRlKGF0dHI6IHN0cmluZywgdmFsdWU6IHN0cmluZykge1xuICAgIHRoaXMucmVuZGVyZXIuc2V0QXR0cmlidXRlKHRoaXMuZWxlbWVudFJlZi5uYXRpdmVFbGVtZW50LCBhdHRyLCB2YWx1ZSk7XG4gIH1cblxuICBwcml2YXRlIHJlbW92ZUF0dHJpYnV0ZShhdHRyOiBzdHJpbmcpIHtcbiAgICB0aGlzLnJlbmRlcmVyLnJlbW92ZUF0dHJpYnV0ZSh0aGlzLmVsZW1lbnRSZWYubmF0aXZlRWxlbWVudCwgYXR0cik7XG4gIH1cbn1cbiJdfQ==