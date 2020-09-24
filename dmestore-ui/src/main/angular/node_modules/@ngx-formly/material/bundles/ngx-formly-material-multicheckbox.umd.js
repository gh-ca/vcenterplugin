(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@angular/forms'), require('@ngx-formly/core'), require('@ngx-formly/core/select'), require('@angular/core'), require('@ngx-formly/material/form-field'), require('@angular/material/checkbox')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/multicheckbox', ['exports', '@angular/common', '@angular/forms', '@ngx-formly/core', '@ngx-formly/core/select', '@angular/core', '@ngx-formly/material/form-field', '@angular/material/checkbox'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.multicheckbox = {}),global.ng.common,global.ng.forms,global.core,global.select,global.ng.core,global['ngx-formly'].material['form-field'],global.ng.material.checkbox));
}(this, (function (exports,common,forms,core,select,core$1,formField,checkbox) { 'use strict';

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
    var __assign = function () {
        __assign = Object.assign || function __assign(t) {
            for (var s, i = 1, n = arguments.length; i < n; i++) {
                s = arguments[i];
                for (var p in s)
                    if (Object.prototype.hasOwnProperty.call(s, p))
                        t[p] = s[p];
            }
            return t;
        };
        return __assign.apply(this, arguments);
    };
    function __read(o, n) {
        var m = typeof Symbol === "function" && o[Symbol.iterator];
        if (!m)
            return o;
        var i = m.call(o), r, ar = [], e;
        try {
            while ((n === void 0 || n-- > 0) && !(r = i.next()).done)
                ar.push(r.value);
        }
        catch (error) {
            e = { error: error };
        }
        finally {
            try {
                if (r && !r.done && (m = i["return"]))
                    m.call(i);
            }
            finally {
                if (e)
                    throw e.error;
            }
        }
        return ar;
    }
    function __spread() {
        for (var ar = [], i = 0; i < arguments.length; i++)
            ar = ar.concat(__read(arguments[i]));
        return ar;
    }

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyFieldMultiCheckbox = /** @class */ (function (_super) {
        __extends(FormlyFieldMultiCheckbox, _super);
        function FormlyFieldMultiCheckbox() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    hideFieldUnderline: true,
                    floatLabel: 'always',
                    options: [],
                    color: 'accent',
                },
            };
            return _this;
        }
        /**
         * @param {?} value
         * @param {?} checked
         * @return {?}
         */
        FormlyFieldMultiCheckbox.prototype.onChange = /**
         * @param {?} value
         * @param {?} checked
         * @return {?}
         */
            function (value, checked) {
                var _a;
                if (this.to.type === 'array') {
                    this.formControl.patchValue(checked
                        ? __spread((this.formControl.value || []), [value]) : __spread((this.formControl.value || [])).filter(( /**
                 * @param {?} o
                 * @return {?}
                 */function (o) { return o !== value; })));
                }
                else {
                    this.formControl.patchValue(__assign({}, this.formControl.value, (_a = {}, _a[value] = checked, _a)));
                }
                this.formControl.markAsTouched();
            };
        /**
         * @param {?} event
         * @return {?}
         */
        FormlyFieldMultiCheckbox.prototype.onContainerClick = /**
         * @param {?} event
         * @return {?}
         */
            function (event) {
                if (this.checkboxes.length) {
                    this.checkboxes.first.focus();
                }
                _super.prototype.onContainerClick.call(this, event);
            };
        /**
         * @param {?} option
         * @return {?}
         */
        FormlyFieldMultiCheckbox.prototype.isChecked = /**
         * @param {?} option
         * @return {?}
         */
            function (option) {
                /** @type {?} */
                var value = this.formControl.value;
                return value && (this.to.type === 'array'
                    ? (value.indexOf(option.value) !== -1)
                    : value[option.value]);
            };
        FormlyFieldMultiCheckbox.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-multicheckbox',
                        template: "\n    <ng-container *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\">\n      <mat-checkbox\n        [id]=\"id + '_' + i\"\n        [formlyAttributes]=\"field\"\n        [tabindex]=\"to.tabindex\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [checked]=\"isChecked(option)\"\n        [disabled]=\"formControl.disabled\"\n        (change)=\"onChange(option.value, $event.checked)\">\n          {{ option.label }}\n      </mat-checkbox>\n    </ng-container>\n  "
                    }] }
        ];
        FormlyFieldMultiCheckbox.propDecorators = {
            checkboxes: [{ type: core$1.ViewChildren, args: [checkbox.MatCheckbox,] }]
        };
        return FormlyFieldMultiCheckbox;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatMultiCheckboxModule = /** @class */ (function () {
        function FormlyMatMultiCheckboxModule() {
        }
        FormlyMatMultiCheckboxModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldMultiCheckbox],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            checkbox.MatCheckboxModule,
                            formField.FormlyMatFormFieldModule,
                            select.FormlySelectModule,
                            core.FormlyModule.forChild({
                                types: [
                                    {
                                        name: 'multicheckbox',
                                        component: FormlyFieldMultiCheckbox,
                                        wrappers: ['form-field'],
                                    },
                                ],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatMultiCheckboxModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatMultiCheckboxModule = FormlyMatMultiCheckboxModule;
    exports.FormlyFieldMultiCheckbox = FormlyFieldMultiCheckbox;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-multicheckbox.umd.js.map