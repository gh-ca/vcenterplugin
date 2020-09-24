(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@ngx-formly/core'), require('@angular/forms'), require('@angular/core'), require('@ngx-formly/material/form-field'), require('@angular/material/checkbox')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/checkbox', ['exports', '@angular/common', '@ngx-formly/core', '@angular/forms', '@angular/core', '@ngx-formly/material/form-field', '@angular/material/checkbox'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.checkbox = {}),global.ng.common,global.core,global.ng.forms,global.ng.core,global['ngx-formly'].material['form-field'],global.ng.material.checkbox));
}(this, (function (exports,common,core,forms,core$1,formField,checkbox) { 'use strict';

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
    var FormlyFieldCheckbox = /** @class */ (function (_super) {
        __extends(FormlyFieldCheckbox, _super);
        function FormlyFieldCheckbox(renderer) {
            var _this = _super.call(this) || this;
            _this.renderer = renderer;
            _this.defaultOptions = {
                templateOptions: {
                    hideFieldUnderline: true,
                    indeterminate: true,
                    floatLabel: 'always',
                    hideLabel: true,
                    align: 'start',
                    // start or end
                    color: 'accent',
                },
            };
            return _this;
        }
        /**
         * @param {?} event
         * @return {?}
         */
        FormlyFieldCheckbox.prototype.onContainerClick = /**
         * @param {?} event
         * @return {?}
         */
            function (event) {
                this.checkbox.focus();
                _super.prototype.onContainerClick.call(this, event);
            };
        /**
         * @return {?}
         */
        FormlyFieldCheckbox.prototype.ngAfterViewChecked = /**
         * @return {?}
         */
            function () {
                if (this.required !== this._required && this.checkbox && this.checkbox._inputElement) {
                    this._required = this.required;
                    /** @type {?} */
                    var inputElement = this.checkbox._inputElement.nativeElement;
                    if (this.required) {
                        this.renderer.setAttribute(inputElement, 'required', 'required');
                    }
                    else {
                        this.renderer.removeAttribute(inputElement, 'required');
                    }
                }
            };
        FormlyFieldCheckbox.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-checkbox',
                        template: "\n    <mat-checkbox\n      [formControl]=\"formControl\"\n      [id]=\"id\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [indeterminate]=\"to.indeterminate && formControl.value === null\"\n      [color]=\"to.color\"\n      [labelPosition]=\"to.align || to.labelPosition\">\n      {{ to.label }}\n      <span *ngIf=\"to.required && to.hideRequiredMarker !== true\" class=\"mat-form-field-required-marker\">*</span>\n    </mat-checkbox>\n  "
                    }] }
        ];
        /** @nocollapse */
        FormlyFieldCheckbox.ctorParameters = function () {
            return [
                { type: core$1.Renderer2 }
            ];
        };
        FormlyFieldCheckbox.propDecorators = {
            checkbox: [{ type: core$1.ViewChild, args: [checkbox.MatCheckbox,] }]
        };
        return FormlyFieldCheckbox;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatCheckboxModule = /** @class */ (function () {
        function FormlyMatCheckboxModule() {
        }
        FormlyMatCheckboxModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldCheckbox],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            checkbox.MatCheckboxModule,
                            formField.FormlyMatFormFieldModule,
                            core.FormlyModule.forChild({
                                types: [
                                    {
                                        name: 'checkbox',
                                        component: FormlyFieldCheckbox,
                                        wrappers: ['form-field'],
                                    },
                                ],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatCheckboxModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatCheckboxModule = FormlyMatCheckboxModule;
    exports.FormlyFieldCheckbox = FormlyFieldCheckbox;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-checkbox.umd.js.map