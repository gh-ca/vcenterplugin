import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatFormFieldControl, MatFormFieldModule } from '@angular/material/form-field';
import { FocusMonitor } from '@angular/cdk/a11y';
import { __extends } from 'tslib';
import { ViewChild, Type, Component, Renderer2, ElementRef, ViewContainerRef, NgModule } from '@angular/core';
import { FieldType, ɵdefineHiddenProp, FieldWrapper, FormlyConfig, FormlyModule } from '@ngx-formly/core';
import { Subject } from 'rxjs';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyErrorStateMatcher = /** @class */ (function () {
    function FormlyErrorStateMatcher(field) {
        this.field = field;
    }
    /**
     * @param {?} control
     * @param {?} form
     * @return {?}
     */
    FormlyErrorStateMatcher.prototype.isErrorState = /**
     * @param {?} control
     * @param {?} form
     * @return {?}
     */
    function (control, form) {
        return this.field && this.field.showError;
    };
    return FormlyErrorStateMatcher;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @abstract
 * @template F
 */
var FieldType$1 = /** @class */ (function (_super) {
    __extends(FieldType$$1, _super);
    function FieldType$$1() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.errorStateMatcher = new FormlyErrorStateMatcher(_this);
        _this.stateChanges = new Subject();
        _this._errorState = false;
        return _this;
    }
    Object.defineProperty(FieldType$$1.prototype, "formFieldControl", {
        get: /**
         * @return {?}
         */
        function () { return this._control || this; },
        set: /**
         * @param {?} control
         * @return {?}
         */
        function (control) {
            this._control = control;
            if (this.formField && control !== this.formField._control) {
                this.formField._control = control;
            }
        },
        enumerable: true,
        configurable: true
    });
    /**
     * @return {?}
     */
    FieldType$$1.prototype.ngOnInit = /**
     * @return {?}
     */
    function () {
        if (this.formField) {
            this.formField._control = this.formFieldControl;
        }
    };
    /**
     * @return {?}
     */
    FieldType$$1.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        if (this.matPrefix || this.matSuffix) {
            setTimeout((/**
             * @return {?}
             */
            function () {
                ɵdefineHiddenProp(_this.field, '_matprefix', _this.matPrefix);
                ɵdefineHiddenProp(_this.field, '_matsuffix', _this.matSuffix);
                ((/** @type {?} */ (_this.options)))._markForCheck(_this.field);
            }));
        }
    };
    /**
     * @return {?}
     */
    FieldType$$1.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        if (this.formField) {
            delete this.formField._control;
        }
        this.stateChanges.complete();
    };
    /**
     * @param {?} ids
     * @return {?}
     */
    FieldType$$1.prototype.setDescribedByIds = /**
     * @param {?} ids
     * @return {?}
     */
    function (ids) { };
    /**
     * @param {?} event
     * @return {?}
     */
    FieldType$$1.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.field.focus = true;
        this.stateChanges.next();
    };
    Object.defineProperty(FieldType$$1.prototype, "errorState", {
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
    Object.defineProperty(FieldType$$1.prototype, "controlType", {
        get: /**
         * @return {?}
         */
        function () {
            if (this.to.type) {
                return this.to.type;
            }
            if (((/** @type {?} */ (this.field.type))) instanceof Type) {
                return (/** @type {?} */ (this.field.type)).constructor.name;
            }
            return (/** @type {?} */ (this.field.type));
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "focused", {
        get: /**
         * @return {?}
         */
        function () { return !!this.field.focus && !this.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "disabled", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "required", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.required; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "placeholder", {
        get: /**
         * @return {?}
         */
        function () { return this.to.placeholder || ''; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "shouldPlaceholderFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.shouldLabelFloat; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "value", {
        get: /**
         * @return {?}
         */
        function () { return this.formControl.value; },
        set: /**
         * @param {?} value
         * @return {?}
         */
        function (value) { this.formControl.patchValue(value); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "ngControl", {
        get: /**
         * @return {?}
         */
        function () { return (/** @type {?} */ (this.formControl)); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "empty", {
        get: /**
         * @return {?}
         */
        function () { return this.value === undefined || this.value === null || this.value === ''; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "shouldLabelFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.focused || !this.empty; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType$$1.prototype, "formField", {
        get: /**
         * @return {?}
         */
        function () { return this.field ? ((/** @type {?} */ (this.field)))['__formField__'] : null; },
        enumerable: true,
        configurable: true
    });
    FieldType$$1.propDecorators = {
        matPrefix: [{ type: ViewChild, args: ['matPrefix',] }],
        matSuffix: [{ type: ViewChild, args: ['matSuffix',] }]
    };
    return FieldType$$1;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyWrapperFormField = /** @class */ (function (_super) {
    __extends(FormlyWrapperFormField, _super);
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
        ɵdefineHiddenProp(this.field, '__formField__', this.formField);
        /** @type {?} */
        var ref = this.config.resolveFieldTypeRef(this.formlyField);
        if (ref && !(ref.instance instanceof FieldType$1)) {
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

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatFormFieldModule = /** @class */ (function () {
    function FormlyMatFormFieldModule() {
    }
    FormlyMatFormFieldModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyWrapperFormField],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatFormFieldModule,
                        FormlyModule.forChild({
                            wrappers: [{
                                    name: 'form-field',
                                    component: FormlyWrapperFormField,
                                }],
                        }),
                    ],
                },] }
    ];
    return FormlyMatFormFieldModule;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatFormFieldModule, FieldType$1 as FieldType, FormlyWrapperFormField as ɵa };

//# sourceMappingURL=ngx-formly-material-form-field.js.map