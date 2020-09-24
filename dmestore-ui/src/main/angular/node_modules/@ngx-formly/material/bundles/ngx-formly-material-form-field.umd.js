(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@angular/forms'), require('@angular/material/form-field'), require('@angular/cdk/a11y'), require('@angular/core'), require('@ngx-formly/core'), require('rxjs')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/form-field', ['exports', '@angular/common', '@angular/forms', '@angular/material/form-field', '@angular/cdk/a11y', '@angular/core', '@ngx-formly/core', 'rxjs'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material['form-field'] = {}),global.ng.common,global.ng.forms,global.ng.material['form-field'],global.ng.cdk.a11y,global.ng.core,global.core$1,global.rxjs));
}(this, (function (exports,common,forms,formField,a11y,core,core$1,rxjs) { 'use strict';

    /*! *****************************************************************************
    Copyright (c) Microsoft Corporation. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License"); you may not use
    this file except in compliance with the License. You may obtain a copy of the
    License at http://www.apache.org/licenses/LICENSE-2.0

    THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED
    WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
    MERCHANTABLITY OR NON-INFRINGEMENT.

    See the Apache Version 2.0 License for specific language governing permissions
    and limitations under the License.
    ***************************************************************************** */
    /* global Reflect, Promise */
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b)
                if (b.hasOwnProperty(p))
                    d[p] = b[p]; };
        return extendStatics(d, b);
    };
    function __extends(d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    }

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
    var FieldType = /** @class */ (function (_super) {
        __extends(FieldType, _super);
        function FieldType() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.errorStateMatcher = new FormlyErrorStateMatcher(_this);
            _this.stateChanges = new rxjs.Subject();
            _this._errorState = false;
            return _this;
        }
        Object.defineProperty(FieldType.prototype, "formFieldControl", {
            get: /**
             * @return {?}
             */ function () { return this._control || this; },
            set: /**
             * @param {?} control
             * @return {?}
             */ function (control) {
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
        FieldType.prototype.ngOnInit = /**
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
        FieldType.prototype.ngAfterViewInit = /**
         * @return {?}
         */
            function () {
                var _this = this;
                if (this.matPrefix || this.matSuffix) {
                    setTimeout(( /**
                     * @return {?}
                     */function () {
                        core$1.ɵdefineHiddenProp(_this.field, '_matprefix', _this.matPrefix);
                        core$1.ɵdefineHiddenProp(_this.field, '_matsuffix', _this.matSuffix);
                        (( /** @type {?} */(_this.options)))._markForCheck(_this.field);
                    }));
                }
            };
        /**
         * @return {?}
         */
        FieldType.prototype.ngOnDestroy = /**
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
        FieldType.prototype.setDescribedByIds = /**
         * @param {?} ids
         * @return {?}
         */
            function (ids) { };
        /**
         * @param {?} event
         * @return {?}
         */
        FieldType.prototype.onContainerClick = /**
         * @param {?} event
         * @return {?}
         */
            function (event) {
                this.field.focus = true;
                this.stateChanges.next();
            };
        Object.defineProperty(FieldType.prototype, "errorState", {
            get: /**
             * @return {?}
             */ function () {
                /** @type {?} */
                var showError = ( /** @type {?} */(( /** @type {?} */(this.options)).showError))(this);
                if (showError !== this._errorState) {
                    this._errorState = showError;
                    this.stateChanges.next();
                }
                return showError;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "controlType", {
            get: /**
             * @return {?}
             */ function () {
                if (this.to.type) {
                    return this.to.type;
                }
                if ((( /** @type {?} */(this.field.type))) instanceof core.Type) {
                    return ( /** @type {?} */(this.field.type)).constructor.name;
                }
                return ( /** @type {?} */(this.field.type));
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "focused", {
            get: /**
             * @return {?}
             */ function () { return !!this.field.focus && !this.disabled; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "disabled", {
            get: /**
             * @return {?}
             */ function () { return !!this.to.disabled; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "required", {
            get: /**
             * @return {?}
             */ function () { return !!this.to.required; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "placeholder", {
            get: /**
             * @return {?}
             */ function () { return this.to.placeholder || ''; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "shouldPlaceholderFloat", {
            get: /**
             * @return {?}
             */ function () { return this.shouldLabelFloat; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "value", {
            get: /**
             * @return {?}
             */ function () { return this.formControl.value; },
            set: /**
             * @param {?} value
             * @return {?}
             */ function (value) { this.formControl.patchValue(value); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "ngControl", {
            get: /**
             * @return {?}
             */ function () { return ( /** @type {?} */(this.formControl)); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "empty", {
            get: /**
             * @return {?}
             */ function () { return this.value === undefined || this.value === null || this.value === ''; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "shouldLabelFloat", {
            get: /**
             * @return {?}
             */ function () { return this.focused || !this.empty; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FieldType.prototype, "formField", {
            get: /**
             * @return {?}
             */ function () { return this.field ? (( /** @type {?} */(this.field)))['__formField__'] : null; },
            enumerable: true,
            configurable: true
        });
        FieldType.propDecorators = {
            matPrefix: [{ type: core.ViewChild, args: ['matPrefix',] }],
            matSuffix: [{ type: core.ViewChild, args: ['matSuffix',] }]
        };
        return FieldType;
    }(core$1.FieldType));

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
            _this.stateChanges = new rxjs.Subject();
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
                core$1.ɵdefineHiddenProp(this.field, '__formField__', this.formField);
                /** @type {?} */
                var ref = this.config.resolveFieldTypeRef(this.formlyField);
                if (ref && !(ref.instance instanceof FieldType)) {
                    console.warn("Component '" + ref.componentType.name + "' must extend 'FieldType' from '@ngx-formly/material/form-field'.");
                }
                // fix for https://github.com/angular/material2/issues/11437
                if (this.formlyField.hide && ( /** @type {?} */(this.formlyField.templateOptions)).appearance === 'outline') {
                    this.initialGapCalculated = true;
                }
                this.focusMonitor.monitor(this.elementRef, true).subscribe(( /**
                 * @param {?} origin
                 * @return {?}
                 */function (origin) {
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
             */ function () {
                /** @type {?} */
                var showError = ( /** @type {?} */(( /** @type {?} */(this.options)).showError))(this);
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
             */ function () { return this.to.type; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "focused", {
            get: /**
             * @return {?}
             */ function () { return !!this.formlyField.focus && !this.disabled; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "disabled", {
            get: /**
             * @return {?}
             */ function () { return !!this.to.disabled; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "required", {
            get: /**
             * @return {?}
             */ function () { return !!this.to.required; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "placeholder", {
            get: /**
             * @return {?}
             */ function () { return this.to.placeholder || ''; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "shouldPlaceholderFloat", {
            get: /**
             * @return {?}
             */ function () { return this.shouldLabelFloat; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "value", {
            get: /**
             * @return {?}
             */ function () { return this.formControl.value; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "ngControl", {
            get: /**
             * @return {?}
             */ function () { return ( /** @type {?} */(this.formControl)); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "empty", {
            get: /**
             * @return {?}
             */ function () { return !this.formControl.value; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "shouldLabelFloat", {
            get: /**
             * @return {?}
             */ function () { return this.focused || !this.empty; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(FormlyWrapperFormField.prototype, "formlyField", {
            get: /**
             * @return {?}
             */ function () { return ( /** @type {?} */(this.field)); },
            enumerable: true,
            configurable: true
        });
        FormlyWrapperFormField.decorators = [
            { type: core.Component, args: [{
                        selector: 'formly-wrapper-mat-form-field',
                        template: "\n    <!-- fix https://github.com/angular/material2/pull/7083 by setting width to 100% -->\n    <mat-form-field\n      [hideRequiredMarker]=\"true\"\n      [floatLabel]=\"to.floatLabel\"\n      [appearance]=\"to.appearance\"\n      [color]=\"to.color\"\n      [style.width]=\"'100%'\">\n      <ng-container #fieldComponent></ng-container>\n      <mat-label *ngIf=\"to.label && to.hideLabel !== true\">\n        {{ to.label }}\n        <span *ngIf=\"to.required && to.hideRequiredMarker !== true\" class=\"mat-form-field-required-marker\">*</span>\n      </mat-label>\n\n      <ng-container matPrefix>\n        <ng-container *ngTemplateOutlet=\"to.prefix ? to.prefix : formlyField._matprefix\"></ng-container>\n      </ng-container>\n\n      <ng-container matSuffix>\n        <ng-container *ngTemplateOutlet=\"to.suffix ? to.suffix : formlyField._matsuffix\"></ng-container>\n      </ng-container>\n\n      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->\n      <mat-error [id]=\"null\">\n        <formly-validation-message [field]=\"field\"></formly-validation-message>\n      </mat-error>\n      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->\n      <mat-hint *ngIf=\"to.description\" [id]=\"null\">{{ to.description }}</mat-hint>\n    </mat-form-field>\n  ",
                        providers: [{ provide: formField.MatFormFieldControl, useExisting: FormlyWrapperFormField }]
                    }] }
        ];
        /** @nocollapse */
        FormlyWrapperFormField.ctorParameters = function () {
            return [
                { type: core$1.FormlyConfig },
                { type: core.Renderer2 },
                { type: core.ElementRef },
                { type: a11y.FocusMonitor }
            ];
        };
        FormlyWrapperFormField.propDecorators = {
            fieldComponent: [{ type: core.ViewChild, args: ['fieldComponent', ( /** @type {?} */({ read: core.ViewContainerRef, static: true })),] }],
            formField: [{ type: core.ViewChild, args: [formField.MatFormField, ( /** @type {?} */({ static: true })),] }]
        };
        return FormlyWrapperFormField;
    }(core$1.FieldWrapper));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatFormFieldModule = /** @class */ (function () {
        function FormlyMatFormFieldModule() {
        }
        FormlyMatFormFieldModule.decorators = [
            { type: core.NgModule, args: [{
                        declarations: [FormlyWrapperFormField],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            formField.MatFormFieldModule,
                            core$1.FormlyModule.forChild({
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

    exports.FormlyMatFormFieldModule = FormlyMatFormFieldModule;
    exports.FieldType = FieldType;
    exports.ɵa = FormlyWrapperFormField;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-form-field.umd.js.map