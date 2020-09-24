(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@angular/forms'), require('@ngx-formly/core/select'), require('@angular/core'), require('@ngx-formly/material/form-field'), require('@angular/material/radio'), require('@ngx-formly/core')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/radio', ['exports', '@angular/common', '@angular/forms', '@ngx-formly/core/select', '@angular/core', '@ngx-formly/material/form-field', '@angular/material/radio', '@ngx-formly/core'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.radio = {}),global.ng.common,global.ng.forms,global.select,global.ng.core,global['ngx-formly'].material['form-field'],global.ng.material.radio,global.core$1));
}(this, (function (exports,common,forms,select,core,formField,radio,core$1) { 'use strict';

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
    var FormlyFieldRadio = /** @class */ (function (_super) {
        __extends(FormlyFieldRadio, _super);
        function FormlyFieldRadio() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    hideFieldUnderline: true,
                    floatLabel: 'always',
                    options: [],
                    tabindex: -1,
                },
            };
            return _this;
        }
        /**
         * @return {?}
         */
        FormlyFieldRadio.prototype.ngAfterViewInit = /**
         * @return {?}
         */
            function () {
                var _this = this;
                this.focusObserver = core$1.ɵwrapProperty(this.field, 'focus', ( /**
                 * @param {?} __0
                 * @return {?}
                 */function (_a) {
                    var currentValue = _a.currentValue;
                    if (_this.to.tabindex === -1
                        && currentValue
                        && _this.radioGroup._radios.length > 0) {
                        /** @type {?} */
                        var radio$$1 = _this.radioGroup.selected
                            ? _this.radioGroup.selected
                            : _this.radioGroup._radios.first;
                        radio$$1.focus();
                    }
                }));
            };
        /**
         * @return {?}
         */
        FormlyFieldRadio.prototype.ngOnDestroy = /**
         * @return {?}
         */
            function () {
                this.focusObserver && this.focusObserver();
            };
        FormlyFieldRadio.decorators = [
            { type: core.Component, args: [{
                        selector: 'formly-field-mat-radio',
                        template: "\n    <mat-radio-group\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [required]=\"to.required\"\n      [tabindex]=\"to.tabindex\">\n      <mat-radio-button *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\"\n        [id]=\"id + '_' + i\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [value]=\"option.value\">\n        {{ option.label }}\n      </mat-radio-button>\n    </mat-radio-group>\n  "
                    }] }
        ];
        FormlyFieldRadio.propDecorators = {
            radioGroup: [{ type: core.ViewChild, args: [radio.MatRadioGroup,] }]
        };
        return FormlyFieldRadio;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatRadioModule = /** @class */ (function () {
        function FormlyMatRadioModule() {
        }
        FormlyMatRadioModule.decorators = [
            { type: core.NgModule, args: [{
                        declarations: [FormlyFieldRadio],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            radio.MatRadioModule,
                            formField.FormlyMatFormFieldModule,
                            select.FormlySelectModule,
                            core$1.FormlyModule.forChild({
                                types: [{
                                        name: 'radio',
                                        component: FormlyFieldRadio,
                                        wrappers: ['form-field'],
                                    }],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatRadioModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatRadioModule = FormlyMatRadioModule;
    exports.FormlyFieldRadio = FormlyFieldRadio;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-radio.umd.js.map