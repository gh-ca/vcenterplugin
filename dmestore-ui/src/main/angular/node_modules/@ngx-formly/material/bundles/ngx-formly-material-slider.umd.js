(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@ngx-formly/core'), require('@angular/forms'), require('@angular/core'), require('@ngx-formly/material/form-field'), require('@angular/material/slider')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/slider', ['exports', '@angular/common', '@ngx-formly/core', '@angular/forms', '@angular/core', '@ngx-formly/material/form-field', '@angular/material/slider'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.slider = {}),global.ng.common,global.core,global.ng.forms,global.ng.core,global['ngx-formly'].material['form-field'],global.ng.material.slider));
}(this, (function (exports,common,core,forms,core$1,formField,slider) { 'use strict';

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
    var FormlySliderTypeComponent = /** @class */ (function (_super) {
        __extends(FormlySliderTypeComponent, _super);
        function FormlySliderTypeComponent() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    hideFieldUnderline: true,
                    floatLabel: 'always',
                    thumbLabel: false,
                },
            };
            return _this;
        }
        /**
         * @param {?} event
         * @return {?}
         */
        FormlySliderTypeComponent.prototype.onContainerClick = /**
         * @param {?} event
         * @return {?}
         */
            function (event) {
                this.slider.focus();
                _super.prototype.onContainerClick.call(this, event);
            };
        FormlySliderTypeComponent.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-slider',
                        template: "\n    <mat-slider\n      [id]=\"id\"\n      [style.width]=\"'100%'\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [color]=\"to.color\"\n      [thumbLabel]=\"to.thumbLabel\"\n      [step]=\"to.step\"\n      [max]=\"to.max\"\n      [min]=\"to.min\">\n    </mat-slider>\n  "
                    }] }
        ];
        FormlySliderTypeComponent.propDecorators = {
            slider: [{ type: core$1.ViewChild, args: [slider.MatSlider,] }]
        };
        return FormlySliderTypeComponent;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatSliderModule = /** @class */ (function () {
        function FormlyMatSliderModule() {
        }
        FormlyMatSliderModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlySliderTypeComponent],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            slider.MatSliderModule,
                            formField.FormlyMatFormFieldModule,
                            core.FormlyModule.forChild({
                                types: [{
                                        name: 'slider',
                                        component: FormlySliderTypeComponent,
                                        wrappers: ['form-field'],
                                    }],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatSliderModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatSliderModule = FormlyMatSliderModule;
    exports.ɵa = FormlySliderTypeComponent;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-slider.umd.js.map