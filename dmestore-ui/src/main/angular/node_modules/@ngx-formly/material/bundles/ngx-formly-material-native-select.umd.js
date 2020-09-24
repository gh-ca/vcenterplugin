(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@ngx-formly/core'), require('@ngx-formly/core/select'), require('@angular/forms'), require('@angular/core'), require('@ngx-formly/material/form-field'), require('@angular/material/input')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/native-select', ['exports', '@angular/common', '@ngx-formly/core', '@ngx-formly/core/select', '@angular/forms', '@angular/core', '@ngx-formly/material/form-field', '@angular/material/input'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material['native-select'] = {}),global.ng.common,global.core,global.select,global.ng.forms,global.ng.core,global['ngx-formly'].material['form-field'],global.ng.material.input));
}(this, (function (exports,common,core,select,forms,core$1,formField,input) { 'use strict';

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
    var FormlyFieldNativeSelect = /** @class */ (function (_super) {
        __extends(FormlyFieldNativeSelect, _super);
        function FormlyFieldNativeSelect() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    options: [],
                },
            };
            return _this;
        }
        FormlyFieldNativeSelect.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-native-select',
                        template: "\n    <select matNativeControl\n      [id]=\"id\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\">\n      <option *ngIf=\"to.placeholder\" [ngValue]=\"null\">{{ to.placeholder }}</option>\n      <ng-container *ngIf=\"to.options | formlySelectOptions:field | async as opts\">\n        <ng-container *ngIf=\"to._flatOptions else grouplist\">\n          <ng-container *ngFor=\"let opt of opts\">\n            <option [ngValue]=\"opt.value\" [disabled]=\"opt.disabled\">{{ opt.label }}</option>\n          </ng-container>\n        </ng-container>\n\n        <ng-template #grouplist>\n          <ng-container *ngFor=\"let opt of opts\">\n            <option *ngIf=\"!opt.group else optgroup\" [ngValue]=\"opt.value\" [disabled]=\"opt.disabled\">{{ opt.label }}</option>\n            <ng-template #optgroup>\n              <optgroup [label]=\"opt.label\">\n                <option *ngFor=\"let child of opt.group\" [ngValue]=\"child.value\" [disabled]=\"child.disabled\">\n                  {{ child.label }}\n                </option>\n              </optgroup>\n            </ng-template>\n          </ng-container>\n        </ng-template>\n      </ng-container>\n    </select>\n  "
                    }] }
        ];
        FormlyFieldNativeSelect.propDecorators = {
            formFieldControl: [{ type: core$1.ViewChild, args: [input.MatInput, ( /** @type {?} */({ static: true })),] }]
        };
        return FormlyFieldNativeSelect;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatNativeSelectModule = /** @class */ (function () {
        function FormlyMatNativeSelectModule() {
        }
        FormlyMatNativeSelectModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldNativeSelect],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            input.MatInputModule,
                            formField.FormlyMatFormFieldModule,
                            select.FormlySelectModule,
                            core.FormlyModule.forChild({
                                types: [
                                    {
                                        name: 'native-select',
                                        component: FormlyFieldNativeSelect,
                                        wrappers: ['form-field'],
                                    },
                                ],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatNativeSelectModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatNativeSelectModule = FormlyMatNativeSelectModule;
    exports.FormlyFieldNativeSelect = FormlyFieldNativeSelect;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-native-select.umd.js.map