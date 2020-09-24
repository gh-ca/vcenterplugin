(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@ngx-formly/core'), require('@angular/forms'), require('@angular/core'), require('@angular/material/input'), require('@ngx-formly/material/form-field')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/textarea', ['exports', '@angular/common', '@ngx-formly/core', '@angular/forms', '@angular/core', '@angular/material/input', '@ngx-formly/material/form-field'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.textarea = {}),global.ng.common,global.core,global.ng.forms,global.ng.core,global.ng.material.input,global['ngx-formly'].material['form-field']));
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
    var FormlyFieldTextArea = /** @class */ (function (_super) {
        __extends(FormlyFieldTextArea, _super);
        function FormlyFieldTextArea() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    cols: 1,
                    rows: 1,
                },
            };
            return _this;
        }
        FormlyFieldTextArea.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-textarea',
                        template: "\n    <textarea matInput\n      [id]=\"id\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [formControl]=\"formControl\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [cols]=\"to.cols\"\n      [rows]=\"to.rows\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [cdkTextareaAutosize]=\"to.autosize\"\n      [cdkAutosizeMinRows]=\"to.autosizeMinRows\"\n      [cdkAutosizeMaxRows]=\"to.autosizeMaxRows\"\n      [class.cdk-textarea-autosize]=\"to.autosize\"\n      >\n    </textarea>\n  ",
                        providers: [
                            // fix for https://github.com/ngx-formly/ngx-formly/issues/1688
                            // rely on formControl value instead of elementRef which return empty value in Firefox.
                            { provide: input.MAT_INPUT_VALUE_ACCESSOR, useExisting: FormlyFieldTextArea },
                        ]
                    }] }
        ];
        FormlyFieldTextArea.propDecorators = {
            formFieldControl: [{ type: core$1.ViewChild, args: [input.MatInput, ( /** @type {?} */({ static: true })),] }]
        };
        return FormlyFieldTextArea;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatTextAreaModule = /** @class */ (function () {
        function FormlyMatTextAreaModule() {
        }
        FormlyMatTextAreaModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldTextArea],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            input.MatInputModule,
                            formField.FormlyMatFormFieldModule,
                            core.FormlyModule.forChild({
                                types: [
                                    {
                                        name: 'textarea',
                                        component: FormlyFieldTextArea,
                                        wrappers: ['form-field'],
                                    },
                                ],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatTextAreaModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatTextAreaModule = FormlyMatTextAreaModule;
    exports.FormlyFieldTextArea = FormlyFieldTextArea;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-textarea.umd.js.map