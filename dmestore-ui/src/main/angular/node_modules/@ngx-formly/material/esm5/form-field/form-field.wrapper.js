/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild, Renderer2, ElementRef, ViewContainerRef } from '@angular/core';
import { FieldWrapper, ɵdefineHiddenProp as defineHiddenProp, FormlyConfig } from '@ngx-formly/core';
import { MatFormField } from '@angular/material/form-field';
import { MatFormFieldControl } from '@angular/material/form-field';
import { Subject } from 'rxjs';
import { FocusMonitor } from '@angular/cdk/a11y';
import { FieldType } from './field.type';
/**
 * @record
 */
function MatFormlyFieldConfig() { }
if (false) {
    /** @type {?} */
    MatFormlyFieldConfig.prototype._matprefix;
    /** @type {?} */
    MatFormlyFieldConfig.prototype._matsuffix;
    /** @type {?} */
    MatFormlyFieldConfig.prototype.__formField__;
}
var FormlyWrapperFormField = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyWrapperFormField, _super);
    function FormlyWrapperFormField(config, renderer, elementRef, focusMonitor) {
        var _this = _super.call(this) || this;
        _this.config = config;
        _this.renderer = renderer;
        _this.elementRef = elementRef;
        _this.focusMonitor = focusMonitor;
        _this.stateChanges = new Subject();
        _this._errorState = false;
        _this.initialGapCalculated = false;
        return _this;
    }
    /**
     * @return {?}
     */
    FormlyWrapperFormField.prototype.ngOnInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        this.formField._control = this;
        defineHiddenProp(this.field, '__formField__', this.formField);
        /** @type {?} */
        var ref = this.config.resolveFieldTypeRef(this.formlyField);
        if (ref && !(ref.instance instanceof FieldType)) {
            console.warn("Component '" + ref.componentType.name + "' must extend 'FieldType' from '@ngx-formly/material/form-field'.");
        }
        // fix for https://github.com/angular/material2/issues/11437
        if (this.formlyField.hide && (/** @type {?} */ (this.formlyField.templateOptions)).appearance === 'outline') {
            this.initialGapCalculated = true;
        }
        this.focusMonitor.monitor(this.elementRef, true).subscribe((/**
         * @param {?} origin
         * @return {?}
         */
        function (origin) {
            if (!origin && _this.field.focus) {
                _this.field.focus = false;
            }
            _this.stateChanges.next();
        }));
    };
    /**
     * @return {?}
     */
    FormlyWrapperFormField.prototype.ngAfterContentChecked = /**
     * @return {?}
     */
    function () {
        if (!this.initialGapCalculated || this.formlyField.hide) {
            return;
        }
        this.formField.updateOutlineGap();
        this.initialGapCalculated = true;
    };
    /**
     * @return {?}
     */
    FormlyWrapperFormField.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        // temporary fix for https://github.com/angular/material2/issues/7891
        if (this.formField.appearance !== 'outline' && this.to.hideFieldUnderline === true) {
            /** @type {?} */
            var underlineElement = this.formField._elementRef.nativeElement.querySelector('.mat-form-field-underline');
            underlineElement && this.renderer.removeChild(underlineElement.parentNode, underlineElement);
        }
    };
    /**
     * @return {?}
     */
    FormlyWrapperFormField.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        delete this.formlyField.__formField__;
        this.stateChanges.complete();
        this.focusMonitor.stopMonitoring(this.elementRef);
    };
    /**
     * @param {?} ids
     * @return {?}
     */
    FormlyWrapperFormField.prototype.setDescribedByIds = /**
     * @param {?} ids
     * @return {?}
     */
    function (ids) { };
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyWrapperFormField.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.formlyField.focus = true;
        this.stateChanges.next();
    };
    Object.defineProperty(FormlyWrapperFormField.prototype, "errorState", {
        get: /**
         * @return {?}
         */
        function () {
            /** @type {?} */
            var showError = (/** @type {?} */ ((/** @type {?} */ (this.options)).showError))(this);
            if (showError !== this._errorState) {
                this._errorState = showError;
                this.stateChanges.next();
            }
            return showError;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "controlType", {
        get: /**
         * @return {?}
         */
        function () { return this.to.type; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "focused", {
        get: /**
         * @return {?}
         */
        function () { return !!this.formlyField.focus && !this.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "disabled", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "required", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.required; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "placeholder", {
        get: /**
         * @return {?}
         */
        function () { return this.to.placeholder || ''; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "shouldPlaceholderFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.shouldLabelFloat; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "value", {
        get: /**
         * @return {?}
         */
        function () { return this.formControl.value; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "ngControl", {
        get: /**
         * @return {?}
         */
        function () { return (/** @type {?} */ (this.formControl)); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "empty", {
        get: /**
         * @return {?}
         */
        function () { return !this.formControl.value; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "shouldLabelFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.focused || !this.empty; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyWrapperFormField.prototype, "formlyField", {
        get: /**
         * @return {?}
         */
        function () { return (/** @type {?} */ (this.field)); },
        enumerable: true,
        configurable: true
    });
    FormlyWrapperFormField.decorators = [
        { type: Component, args: [{
                    selector: 'formly-wrapper-mat-form-field',
                    template: "\n    <!-- fix https://github.com/angular/material2/pull/7083 by setting width to 100% -->\n    <mat-form-field\n      [hideRequiredMarker]=\"true\"\n      [floatLabel]=\"to.floatLabel\"\n      [appearance]=\"to.appearance\"\n      [color]=\"to.color\"\n      [style.width]=\"'100%'\">\n      <ng-container #fieldComponent></ng-container>\n      <mat-label *ngIf=\"to.label && to.hideLabel !== true\">\n        {{ to.label }}\n        <span *ngIf=\"to.required && to.hideRequiredMarker !== true\" class=\"mat-form-field-required-marker\">*</span>\n      </mat-label>\n\n      <ng-container matPrefix>\n        <ng-container *ngTemplateOutlet=\"to.prefix ? to.prefix : formlyField._matprefix\"></ng-container>\n      </ng-container>\n\n      <ng-container matSuffix>\n        <ng-container *ngTemplateOutlet=\"to.suffix ? to.suffix : formlyField._matsuffix\"></ng-container>\n      </ng-container>\n\n      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->\n      <mat-error [id]=\"null\">\n        <formly-validation-message [field]=\"field\"></formly-validation-message>\n      </mat-error>\n      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->\n      <mat-hint *ngIf=\"to.description\" [id]=\"null\">{{ to.description }}</mat-hint>\n    </mat-form-field>\n  ",
                    providers: [{ provide: MatFormFieldControl, useExisting: FormlyWrapperFormField }]
                }] }
    ];
    /** @nocollapse */
    FormlyWrapperFormField.ctorParameters = function () { return [
        { type: FormlyConfig },
        { type: Renderer2 },
        { type: ElementRef },
        { type: FocusMonitor }
    ]; };
    FormlyWrapperFormField.propDecorators = {
        fieldComponent: [{ type: ViewChild, args: ['fieldComponent', (/** @type {?} */ ({ read: ViewContainerRef, static: true })),] }],
        formField: [{ type: ViewChild, args: [MatFormField, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyWrapperFormField;
}(FieldWrapper));
export { FormlyWrapperFormField };
if (false) {
    /** @type {?} */
    FormlyWrapperFormField.prototype.fieldComponent;
    /** @type {?} */
    FormlyWrapperFormField.prototype.formField;
    /** @type {?} */
    FormlyWrapperFormField.prototype.stateChanges;
    /** @type {?} */
    FormlyWrapperFormField.prototype._errorState;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.initialGapCalculated;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.config;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.renderer;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.elementRef;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.focusMonitor;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybS1maWVsZC53cmFwcGVyLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZC8iLCJzb3VyY2VzIjpbImZvcm0tZmllbGQud3JhcHBlci50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFxQixTQUFTLEVBQW1ELFVBQVUsRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUNsSyxPQUFPLEVBQUUsWUFBWSxFQUFFLGlCQUFpQixJQUFJLGdCQUFnQixFQUFxQixZQUFZLEVBQUUsTUFBTSxrQkFBa0IsQ0FBQztBQUN4SCxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0sOEJBQThCLENBQUM7QUFDNUQsT0FBTyxFQUFFLG1CQUFtQixFQUFFLE1BQU0sOEJBQThCLENBQUM7QUFDbkUsT0FBTyxFQUFFLE9BQU8sRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUMvQixPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0sbUJBQW1CLENBQUM7QUFDakQsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGNBQWMsQ0FBQzs7OztBQUV6QyxtQ0FJQzs7O0lBSEMsMENBQTZCOztJQUM3QiwwQ0FBNkI7O0lBQzdCLDZDQUFzQzs7QUFHeEM7SUFrQzRDLGtEQUFrQztJQVc1RSxnQ0FDVSxNQUFvQixFQUNwQixRQUFtQixFQUNuQixVQUFzQixFQUN0QixZQUEwQjtRQUpwQyxZQU1FLGlCQUFPLFNBQ1I7UUFOUyxZQUFNLEdBQU4sTUFBTSxDQUFjO1FBQ3BCLGNBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsZ0JBQVUsR0FBVixVQUFVLENBQVk7UUFDdEIsa0JBQVksR0FBWixZQUFZLENBQWM7UUFScEMsa0JBQVksR0FBRyxJQUFJLE9BQU8sRUFBUSxDQUFDO1FBQ25DLGlCQUFXLEdBQUcsS0FBSyxDQUFDO1FBQ1osMEJBQW9CLEdBQUcsS0FBSyxDQUFDOztJQVNyQyxDQUFDOzs7O0lBRUQseUNBQVE7OztJQUFSO1FBQUEsaUJBcUJDO1FBcEJDLElBQUksQ0FBQyxTQUFTLENBQUMsUUFBUSxHQUFHLElBQUksQ0FBQztRQUMvQixnQkFBZ0IsQ0FBQyxJQUFJLENBQUMsS0FBSyxFQUFFLGVBQWUsRUFBRSxJQUFJLENBQUMsU0FBUyxDQUFDLENBQUM7O1lBRXhELEdBQUcsR0FBRyxJQUFJLENBQUMsTUFBTSxDQUFDLG1CQUFtQixDQUFDLElBQUksQ0FBQyxXQUFXLENBQUM7UUFDN0QsSUFBSSxHQUFHLElBQUksQ0FBQyxDQUFDLEdBQUcsQ0FBQyxRQUFRLFlBQVksU0FBUyxDQUFDLEVBQUU7WUFDL0MsT0FBTyxDQUFDLElBQUksQ0FBQyxnQkFBYyxHQUFHLENBQUMsYUFBYSxDQUFDLElBQUksc0VBQW1FLENBQUMsQ0FBQztTQUN2SDtRQUVELDREQUE0RDtRQUM1RCxJQUFJLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSxJQUFJLG1CQUFBLElBQUksQ0FBQyxXQUFXLENBQUMsZUFBZSxFQUFDLENBQUMsVUFBVSxLQUFLLFNBQVMsRUFBRTtZQUN2RixJQUFJLENBQUMsb0JBQW9CLEdBQUcsSUFBSSxDQUFDO1NBQ2xDO1FBRUQsSUFBSSxDQUFDLFlBQVksQ0FBQyxPQUFPLENBQUMsSUFBSSxDQUFDLFVBQVUsRUFBRSxJQUFJLENBQUMsQ0FBQyxTQUFTOzs7O1FBQUMsVUFBQSxNQUFNO1lBQy9ELElBQUksQ0FBQyxNQUFNLElBQUksS0FBSSxDQUFDLEtBQUssQ0FBQyxLQUFLLEVBQUU7Z0JBQy9CLEtBQUksQ0FBQyxLQUFLLENBQUMsS0FBSyxHQUFHLEtBQUssQ0FBQzthQUMxQjtZQUVELEtBQUksQ0FBQyxZQUFZLENBQUMsSUFBSSxFQUFFLENBQUM7UUFDM0IsQ0FBQyxFQUFDLENBQUM7SUFDTCxDQUFDOzs7O0lBRUQsc0RBQXFCOzs7SUFBckI7UUFDRSxJQUFJLENBQUMsSUFBSSxDQUFDLG9CQUFvQixJQUFJLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSxFQUFFO1lBQ3ZELE9BQU87U0FDUjtRQUVELElBQUksQ0FBQyxTQUFTLENBQUMsZ0JBQWdCLEVBQUUsQ0FBQztRQUNsQyxJQUFJLENBQUMsb0JBQW9CLEdBQUcsSUFBSSxDQUFDO0lBQ25DLENBQUM7Ozs7SUFFRCxnREFBZTs7O0lBQWY7UUFDRSxxRUFBcUU7UUFDckUsSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLFVBQVUsS0FBSyxTQUFTLElBQUksSUFBSSxDQUFDLEVBQUUsQ0FBQyxrQkFBa0IsS0FBSyxJQUFJLEVBQUU7O2dCQUM1RSxnQkFBZ0IsR0FBRyxJQUFJLENBQUMsU0FBUyxDQUFDLFdBQVcsQ0FBQyxhQUFhLENBQUMsYUFBYSxDQUFDLDJCQUEyQixDQUFDO1lBQzVHLGdCQUFnQixJQUFJLElBQUksQ0FBQyxRQUFRLENBQUMsV0FBVyxDQUFDLGdCQUFnQixDQUFDLFVBQVUsRUFBRSxnQkFBZ0IsQ0FBQyxDQUFDO1NBQzlGO0lBQ0gsQ0FBQzs7OztJQUVELDRDQUFXOzs7SUFBWDtRQUNFLE9BQU8sSUFBSSxDQUFDLFdBQVcsQ0FBQyxhQUFhLENBQUM7UUFDdEMsSUFBSSxDQUFDLFlBQVksQ0FBQyxRQUFRLEVBQUUsQ0FBQztRQUM3QixJQUFJLENBQUMsWUFBWSxDQUFDLGNBQWMsQ0FBQyxJQUFJLENBQUMsVUFBVSxDQUFDLENBQUM7SUFDcEQsQ0FBQzs7Ozs7SUFFRCxrREFBaUI7Ozs7SUFBakIsVUFBa0IsR0FBYSxJQUFVLENBQUM7Ozs7O0lBQzFDLGlEQUFnQjs7OztJQUFoQixVQUFpQixLQUFpQjtRQUNoQyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssR0FBRyxJQUFJLENBQUM7UUFDOUIsSUFBSSxDQUFDLFlBQVksQ0FBQyxJQUFJLEVBQUUsQ0FBQztJQUMzQixDQUFDO0lBRUQsc0JBQUksOENBQVU7Ozs7UUFBZDs7Z0JBQ1EsU0FBUyxHQUFHLG1CQUFBLG1CQUFBLElBQUksQ0FBQyxPQUFPLEVBQUMsQ0FBQyxTQUFTLEVBQUMsQ0FBQyxJQUFJLENBQUM7WUFDaEQsSUFBSSxTQUFTLEtBQUssSUFBSSxDQUFDLFdBQVcsRUFBRTtnQkFDbEMsSUFBSSxDQUFDLFdBQVcsR0FBRyxTQUFTLENBQUM7Z0JBQzdCLElBQUksQ0FBQyxZQUFZLENBQUMsSUFBSSxFQUFFLENBQUM7YUFDMUI7WUFFRCxPQUFPLFNBQVMsQ0FBQztRQUNuQixDQUFDOzs7T0FBQTtJQUNELHNCQUFJLCtDQUFXOzs7O1FBQWYsY0FBb0IsT0FBTyxJQUFJLENBQUMsRUFBRSxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBQzFDLHNCQUFJLDJDQUFPOzs7O1FBQVgsY0FBZ0IsT0FBTyxDQUFDLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxLQUFLLElBQUksQ0FBQyxJQUFJLENBQUMsUUFBUSxDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFDcEUsc0JBQUksNENBQVE7Ozs7UUFBWixjQUFpQixPQUFPLENBQUMsQ0FBQyxJQUFJLENBQUMsRUFBRSxDQUFDLFFBQVEsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBQzdDLHNCQUFJLDRDQUFROzs7O1FBQVosY0FBaUIsT0FBTyxDQUFDLENBQUMsSUFBSSxDQUFDLEVBQUUsQ0FBQyxRQUFRLENBQUMsQ0FBQyxDQUFDOzs7T0FBQTtJQUM3QyxzQkFBSSwrQ0FBVzs7OztRQUFmLGNBQW9CLE9BQU8sSUFBSSxDQUFDLEVBQUUsQ0FBQyxXQUFXLElBQUksRUFBRSxDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFDdkQsc0JBQUksMERBQXNCOzs7O1FBQTFCLGNBQStCLE9BQU8sSUFBSSxDQUFDLGdCQUFnQixDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFDOUQsc0JBQUkseUNBQUs7Ozs7UUFBVCxjQUFjLE9BQU8sSUFBSSxDQUFDLFdBQVcsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDOzs7T0FBQTtJQUM5QyxzQkFBSSw2Q0FBUzs7OztRQUFiLGNBQWtCLE9BQU8sbUJBQUEsSUFBSSxDQUFDLFdBQVcsRUFBTyxDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFDbkQsc0JBQUkseUNBQUs7Ozs7UUFBVCxjQUFjLE9BQU8sQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBQy9DLHNCQUFJLG9EQUFnQjs7OztRQUFwQixjQUF5QixPQUFPLElBQUksQ0FBQyxPQUFPLElBQUksQ0FBQyxJQUFJLENBQUMsS0FBSyxDQUFDLENBQUMsQ0FBQzs7O09BQUE7SUFFOUQsc0JBQUksK0NBQVc7Ozs7UUFBZixjQUFvQixPQUFPLG1CQUFBLElBQUksQ0FBQyxLQUFLLEVBQXdCLENBQUMsQ0FBQyxDQUFDOzs7T0FBQTs7Z0JBOUhqRSxTQUFTLFNBQUM7b0JBQ1QsUUFBUSxFQUFFLCtCQUErQjtvQkFDekMsUUFBUSxFQUFFLHV6Q0E2QlQ7b0JBQ0QsU0FBUyxFQUFFLENBQUMsRUFBRSxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsV0FBVyxFQUFFLHNCQUFzQixFQUFFLENBQUM7aUJBQ25GOzs7O2dCQTlDZ0YsWUFBWTtnQkFEM0MsU0FBUztnQkFBbUQsVUFBVTtnQkFLL0csWUFBWTs7O2lDQTZDbEIsU0FBUyxTQUFDLGdCQUFnQixFQUFFLG1CQUFLLEVBQUUsSUFBSSxFQUFFLGdCQUFnQixFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTs0QkFHekUsU0FBUyxTQUFDLFlBQVksRUFBRSxtQkFBTSxFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTs7SUF3RmpELDZCQUFDO0NBQUEsQUEvSEQsQ0FrQzRDLFlBQVksR0E2RnZEO1NBN0ZZLHNCQUFzQjs7O0lBRWpDLGdEQUE4Rzs7SUFHOUcsMkNBQTBFOztJQUUxRSw4Q0FBbUM7O0lBQ25DLDZDQUFvQjs7Ozs7SUFDcEIsc0RBQXFDOzs7OztJQUduQyx3Q0FBNEI7Ozs7O0lBQzVCLDBDQUEyQjs7Ozs7SUFDM0IsNENBQThCOzs7OztJQUM5Qiw4Q0FBa0MiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQsIFZpZXdDaGlsZCwgT25Jbml0LCBPbkRlc3Ryb3ksIFJlbmRlcmVyMiwgQWZ0ZXJWaWV3SW5pdCwgQWZ0ZXJDb250ZW50Q2hlY2tlZCwgVGVtcGxhdGVSZWYsIEVsZW1lbnRSZWYsIFZpZXdDb250YWluZXJSZWYgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkV3JhcHBlciwgybVkZWZpbmVIaWRkZW5Qcm9wIGFzIGRlZmluZUhpZGRlblByb3AsIEZvcm1seUZpZWxkQ29uZmlnLCBGb3JtbHlDb25maWcgfSBmcm9tICdAbmd4LWZvcm1seS9jb3JlJztcbmltcG9ydCB7IE1hdEZvcm1GaWVsZCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0Rm9ybUZpZWxkQ29udHJvbCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgU3ViamVjdCB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgRm9jdXNNb25pdG9yIH0gZnJvbSAnQGFuZ3VsYXIvY2RrL2ExMXknO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnLi9maWVsZC50eXBlJztcblxuaW50ZXJmYWNlIE1hdEZvcm1seUZpZWxkQ29uZmlnIGV4dGVuZHMgRm9ybWx5RmllbGRDb25maWcge1xuICBfbWF0cHJlZml4OiBUZW1wbGF0ZVJlZjxhbnk+O1xuICBfbWF0c3VmZml4OiBUZW1wbGF0ZVJlZjxhbnk+O1xuICBfX2Zvcm1GaWVsZF9fOiBGb3JtbHlXcmFwcGVyRm9ybUZpZWxkO1xufVxuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktd3JhcHBlci1tYXQtZm9ybS1maWVsZCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPCEtLSBmaXggaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvbWF0ZXJpYWwyL3B1bGwvNzA4MyBieSBzZXR0aW5nIHdpZHRoIHRvIDEwMCUgLS0+XG4gICAgPG1hdC1mb3JtLWZpZWxkXG4gICAgICBbaGlkZVJlcXVpcmVkTWFya2VyXT1cInRydWVcIlxuICAgICAgW2Zsb2F0TGFiZWxdPVwidG8uZmxvYXRMYWJlbFwiXG4gICAgICBbYXBwZWFyYW5jZV09XCJ0by5hcHBlYXJhbmNlXCJcbiAgICAgIFtjb2xvcl09XCJ0by5jb2xvclwiXG4gICAgICBbc3R5bGUud2lkdGhdPVwiJzEwMCUnXCI+XG4gICAgICA8bmctY29udGFpbmVyICNmaWVsZENvbXBvbmVudD48L25nLWNvbnRhaW5lcj5cbiAgICAgIDxtYXQtbGFiZWwgKm5nSWY9XCJ0by5sYWJlbCAmJiB0by5oaWRlTGFiZWwgIT09IHRydWVcIj5cbiAgICAgICAge3sgdG8ubGFiZWwgfX1cbiAgICAgICAgPHNwYW4gKm5nSWY9XCJ0by5yZXF1aXJlZCAmJiB0by5oaWRlUmVxdWlyZWRNYXJrZXIgIT09IHRydWVcIiBjbGFzcz1cIm1hdC1mb3JtLWZpZWxkLXJlcXVpcmVkLW1hcmtlclwiPio8L3NwYW4+XG4gICAgICA8L21hdC1sYWJlbD5cblxuICAgICAgPG5nLWNvbnRhaW5lciBtYXRQcmVmaXg+XG4gICAgICAgIDxuZy1jb250YWluZXIgKm5nVGVtcGxhdGVPdXRsZXQ9XCJ0by5wcmVmaXggPyB0by5wcmVmaXggOiBmb3JtbHlGaWVsZC5fbWF0cHJlZml4XCI+PC9uZy1jb250YWluZXI+XG4gICAgICA8L25nLWNvbnRhaW5lcj5cblxuICAgICAgPG5nLWNvbnRhaW5lciBtYXRTdWZmaXg+XG4gICAgICAgIDxuZy1jb250YWluZXIgKm5nVGVtcGxhdGVPdXRsZXQ9XCJ0by5zdWZmaXggPyB0by5zdWZmaXggOiBmb3JtbHlGaWVsZC5fbWF0c3VmZml4XCI+PC9uZy1jb250YWluZXI+XG4gICAgICA8L25nLWNvbnRhaW5lcj5cblxuICAgICAgPCEtLSBmaXggaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvbWF0ZXJpYWwyL2lzc3Vlcy83NzM3IGJ5IHNldHRpbmcgaWQgdG8gbnVsbCAgLS0+XG4gICAgICA8bWF0LWVycm9yIFtpZF09XCJudWxsXCI+XG4gICAgICAgIDxmb3JtbHktdmFsaWRhdGlvbi1tZXNzYWdlIFtmaWVsZF09XCJmaWVsZFwiPjwvZm9ybWx5LXZhbGlkYXRpb24tbWVzc2FnZT5cbiAgICAgIDwvbWF0LWVycm9yPlxuICAgICAgPCEtLSBmaXggaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvbWF0ZXJpYWwyL2lzc3Vlcy83NzM3IGJ5IHNldHRpbmcgaWQgdG8gbnVsbCAgLS0+XG4gICAgICA8bWF0LWhpbnQgKm5nSWY9XCJ0by5kZXNjcmlwdGlvblwiIFtpZF09XCJudWxsXCI+e3sgdG8uZGVzY3JpcHRpb24gfX08L21hdC1oaW50PlxuICAgIDwvbWF0LWZvcm0tZmllbGQ+XG4gIGAsXG4gIHByb3ZpZGVyczogW3sgcHJvdmlkZTogTWF0Rm9ybUZpZWxkQ29udHJvbCwgdXNlRXhpc3Rpbmc6IEZvcm1seVdyYXBwZXJGb3JtRmllbGQgfV0sXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seVdyYXBwZXJGb3JtRmllbGQgZXh0ZW5kcyBGaWVsZFdyYXBwZXI8TWF0Rm9ybWx5RmllbGRDb25maWc+IGltcGxlbWVudHMgT25Jbml0LCBPbkRlc3Ryb3ksIE1hdEZvcm1GaWVsZENvbnRyb2w8YW55PiwgQWZ0ZXJWaWV3SW5pdCwgQWZ0ZXJDb250ZW50Q2hlY2tlZCB7XG4gIC8vIFRPRE86IHJlbW92ZSBgYW55YCwgb25jZSBkcm9wcGluZyBhbmd1bGFyIGBWN2Agc3VwcG9ydC5cbiAgQFZpZXdDaGlsZCgnZmllbGRDb21wb25lbnQnLCA8YW55PnsgcmVhZDogVmlld0NvbnRhaW5lclJlZiwgc3RhdGljOiB0cnVlIH0pIGZpZWxkQ29tcG9uZW50ITogVmlld0NvbnRhaW5lclJlZjtcblxuICAvLyBUT0RPOiByZW1vdmUgYGFueWAsIG9uY2UgZHJvcHBpbmcgYW5ndWxhciBgVjdgIHN1cHBvcnQuXG4gIEBWaWV3Q2hpbGQoTWF0Rm9ybUZpZWxkLCA8YW55PiB7IHN0YXRpYzogdHJ1ZSB9KSBmb3JtRmllbGQhOiBNYXRGb3JtRmllbGQ7XG5cbiAgc3RhdGVDaGFuZ2VzID0gbmV3IFN1YmplY3Q8dm9pZD4oKTtcbiAgX2Vycm9yU3RhdGUgPSBmYWxzZTtcbiAgcHJpdmF0ZSBpbml0aWFsR2FwQ2FsY3VsYXRlZCA9IGZhbHNlO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgY29uZmlnOiBGb3JtbHlDb25maWcsXG4gICAgcHJpdmF0ZSByZW5kZXJlcjogUmVuZGVyZXIyLFxuICAgIHByaXZhdGUgZWxlbWVudFJlZjogRWxlbWVudFJlZixcbiAgICBwcml2YXRlIGZvY3VzTW9uaXRvcjogRm9jdXNNb25pdG9yLFxuICApIHtcbiAgICBzdXBlcigpO1xuICB9XG5cbiAgbmdPbkluaXQoKSB7XG4gICAgdGhpcy5mb3JtRmllbGQuX2NvbnRyb2wgPSB0aGlzO1xuICAgIGRlZmluZUhpZGRlblByb3AodGhpcy5maWVsZCwgJ19fZm9ybUZpZWxkX18nLCB0aGlzLmZvcm1GaWVsZCk7XG5cbiAgICBjb25zdCByZWYgPSB0aGlzLmNvbmZpZy5yZXNvbHZlRmllbGRUeXBlUmVmKHRoaXMuZm9ybWx5RmllbGQpO1xuICAgIGlmIChyZWYgJiYgIShyZWYuaW5zdGFuY2UgaW5zdGFuY2VvZiBGaWVsZFR5cGUpKSB7XG4gICAgICBjb25zb2xlLndhcm4oYENvbXBvbmVudCAnJHtyZWYuY29tcG9uZW50VHlwZS5uYW1lfScgbXVzdCBleHRlbmQgJ0ZpZWxkVHlwZScgZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCcuYCk7XG4gICAgfVxuXG4gICAgLy8gZml4IGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzExNDM3XG4gICAgaWYgKHRoaXMuZm9ybWx5RmllbGQuaGlkZSAmJiB0aGlzLmZvcm1seUZpZWxkLnRlbXBsYXRlT3B0aW9ucyEuYXBwZWFyYW5jZSA9PT0gJ291dGxpbmUnKSB7XG4gICAgICB0aGlzLmluaXRpYWxHYXBDYWxjdWxhdGVkID0gdHJ1ZTtcbiAgICB9XG5cbiAgICB0aGlzLmZvY3VzTW9uaXRvci5tb25pdG9yKHRoaXMuZWxlbWVudFJlZiwgdHJ1ZSkuc3Vic2NyaWJlKG9yaWdpbiA9PiB7XG4gICAgICBpZiAoIW9yaWdpbiAmJiB0aGlzLmZpZWxkLmZvY3VzKSB7XG4gICAgICAgIHRoaXMuZmllbGQuZm9jdXMgPSBmYWxzZTtcbiAgICAgIH1cblxuICAgICAgdGhpcy5zdGF0ZUNoYW5nZXMubmV4dCgpO1xuICAgIH0pO1xuICB9XG5cbiAgbmdBZnRlckNvbnRlbnRDaGVja2VkKCkge1xuICAgIGlmICghdGhpcy5pbml0aWFsR2FwQ2FsY3VsYXRlZCB8fCB0aGlzLmZvcm1seUZpZWxkLmhpZGUpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICB0aGlzLmZvcm1GaWVsZC51cGRhdGVPdXRsaW5lR2FwKCk7XG4gICAgdGhpcy5pbml0aWFsR2FwQ2FsY3VsYXRlZCA9IHRydWU7XG4gIH1cblxuICBuZ0FmdGVyVmlld0luaXQoKSB7XG4gICAgLy8gdGVtcG9yYXJ5IGZpeCBmb3IgaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvbWF0ZXJpYWwyL2lzc3Vlcy83ODkxXG4gICAgaWYgKHRoaXMuZm9ybUZpZWxkLmFwcGVhcmFuY2UgIT09ICdvdXRsaW5lJyAmJiB0aGlzLnRvLmhpZGVGaWVsZFVuZGVybGluZSA9PT0gdHJ1ZSkge1xuICAgICAgY29uc3QgdW5kZXJsaW5lRWxlbWVudCA9IHRoaXMuZm9ybUZpZWxkLl9lbGVtZW50UmVmLm5hdGl2ZUVsZW1lbnQucXVlcnlTZWxlY3RvcignLm1hdC1mb3JtLWZpZWxkLXVuZGVybGluZScpO1xuICAgICAgdW5kZXJsaW5lRWxlbWVudCAmJiB0aGlzLnJlbmRlcmVyLnJlbW92ZUNoaWxkKHVuZGVybGluZUVsZW1lbnQucGFyZW50Tm9kZSwgdW5kZXJsaW5lRWxlbWVudCk7XG4gICAgfVxuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgZGVsZXRlIHRoaXMuZm9ybWx5RmllbGQuX19mb3JtRmllbGRfXztcbiAgICB0aGlzLnN0YXRlQ2hhbmdlcy5jb21wbGV0ZSgpO1xuICAgIHRoaXMuZm9jdXNNb25pdG9yLnN0b3BNb25pdG9yaW5nKHRoaXMuZWxlbWVudFJlZik7XG4gIH1cblxuICBzZXREZXNjcmliZWRCeUlkcyhpZHM6IHN0cmluZ1tdKTogdm9pZCB7IH1cbiAgb25Db250YWluZXJDbGljayhldmVudDogTW91c2VFdmVudCk6IHZvaWQge1xuICAgIHRoaXMuZm9ybWx5RmllbGQuZm9jdXMgPSB0cnVlO1xuICAgIHRoaXMuc3RhdGVDaGFuZ2VzLm5leHQoKTtcbiAgfVxuXG4gIGdldCBlcnJvclN0YXRlKCkge1xuICAgIGNvbnN0IHNob3dFcnJvciA9IHRoaXMub3B0aW9ucyEuc2hvd0Vycm9yISh0aGlzKTtcbiAgICBpZiAoc2hvd0Vycm9yICE9PSB0aGlzLl9lcnJvclN0YXRlKSB7XG4gICAgICB0aGlzLl9lcnJvclN0YXRlID0gc2hvd0Vycm9yO1xuICAgICAgdGhpcy5zdGF0ZUNoYW5nZXMubmV4dCgpO1xuICAgIH1cblxuICAgIHJldHVybiBzaG93RXJyb3I7XG4gIH1cbiAgZ2V0IGNvbnRyb2xUeXBlKCkgeyByZXR1cm4gdGhpcy50by50eXBlOyB9XG4gIGdldCBmb2N1c2VkKCkgeyByZXR1cm4gISF0aGlzLmZvcm1seUZpZWxkLmZvY3VzICYmICF0aGlzLmRpc2FibGVkOyB9XG4gIGdldCBkaXNhYmxlZCgpIHsgcmV0dXJuICEhdGhpcy50by5kaXNhYmxlZDsgfVxuICBnZXQgcmVxdWlyZWQoKSB7IHJldHVybiAhIXRoaXMudG8ucmVxdWlyZWQ7IH1cbiAgZ2V0IHBsYWNlaG9sZGVyKCkgeyByZXR1cm4gdGhpcy50by5wbGFjZWhvbGRlciB8fCAnJzsgfVxuICBnZXQgc2hvdWxkUGxhY2Vob2xkZXJGbG9hdCgpIHsgcmV0dXJuIHRoaXMuc2hvdWxkTGFiZWxGbG9hdDsgfVxuICBnZXQgdmFsdWUoKSB7IHJldHVybiB0aGlzLmZvcm1Db250cm9sLnZhbHVlOyB9XG4gIGdldCBuZ0NvbnRyb2woKSB7IHJldHVybiB0aGlzLmZvcm1Db250cm9sIGFzIGFueTsgfVxuICBnZXQgZW1wdHkoKSB7IHJldHVybiAhdGhpcy5mb3JtQ29udHJvbC52YWx1ZTsgfVxuICBnZXQgc2hvdWxkTGFiZWxGbG9hdCgpIHsgcmV0dXJuIHRoaXMuZm9jdXNlZCB8fCAhdGhpcy5lbXB0eTsgfVxuXG4gIGdldCBmb3JtbHlGaWVsZCgpIHsgcmV0dXJuIHRoaXMuZmllbGQgYXMgTWF0Rm9ybWx5RmllbGRDb25maWc7IH1cbn1cbiJdfQ==