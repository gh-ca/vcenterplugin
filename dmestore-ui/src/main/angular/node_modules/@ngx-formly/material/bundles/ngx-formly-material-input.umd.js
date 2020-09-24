(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@ngx-formly/core'), require('@angular/forms'), require('@angular/core'), require('@angular/material/input'), require('@ngx-formly/material/form-field')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/input', ['exports', '@angular/common', '@ngx-formly/core', '@angular/forms', '@angular/core', '@angular/material/input', '@ngx-formly/material/form-field'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.input = {}),global.ng.common,global.core,global.ng.forms,global.ng.core,global.ng.material.input,global['ngx-formly'].material['form-field']));
}(this, (function (exports,common,core,forms,core$1,input,formField) { 'use strict';

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
    var FormlyFieldInput = /** @class */ (function (_super) {
        __extends(FormlyFieldInput, _super);
        function FormlyFieldInput() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        Object.defineProperty(FormlyFieldInput.prototype, "type", {
            get: /**
             * @return {?}
             */ function () {
                return this.to.type || 'text';
            },
            enumerable: true,
            configurable: true
        });
        FormlyFieldInput.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-input',
                        template: "\n    <input *ngIf=\"type !== 'number'; else numberTmp\"\n      matInput\n      [id]=\"id\"\n      [type]=\"type || 'text'\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [placeholder]=\"to.placeholder\">\n    <ng-template #numberTmp>\n      <input matInput\n             [id]=\"id\"\n             type=\"number\"\n             [readonly]=\"to.readonly\"\n             [required]=\"to.required\"\n             [errorStateMatcher]=\"errorStateMatcher\"\n             [formControl]=\"formControl\"\n             [formlyAttributes]=\"field\"\n             [tabindex]=\"to.tabindex\"\n             [placeholder]=\"to.placeholder\">\n    </ng-template>\n  "
                    }] }
        ];
        FormlyFieldInput.propDecorators = {
            formFieldControl: [{ type: core$1.ViewChild, args: [input.MatInput, ( /** @type {?} */({ static: false })),] }]
        };
        return FormlyFieldInput;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatInputModule = /** @class */ (function () {
        function FormlyMatInputModule() {
        }
        FormlyMatInputModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldInput],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            input.MatInputModule,
                            formField.FormlyMatFormFieldModule,
                            core.FormlyModule.forChild({
                                types: [
                                    {
                                        name: 'input',
                                        component: FormlyFieldInput,
                                        wrappers: ['form-field'],
                                    },
                                ],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatInputModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatInputModule = FormlyMatInputModule;
    exports.FormlyFieldInput = FormlyFieldInput;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-input.umd.js.map