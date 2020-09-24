(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@angular/forms'), require('@angular/core'), require('@ngx-formly/core'), require('@ngx-formly/material/form-field'), require('@angular/material/input'), require('@angular/material/datepicker')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/datepicker', ['exports', '@angular/common', '@angular/forms', '@angular/core', '@ngx-formly/core', '@ngx-formly/material/form-field', '@angular/material/input', '@angular/material/datepicker'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.datepicker = {}),global.ng.common,global.ng.forms,global.ng.core,global.core$1,global['ngx-formly'].material['form-field'],global.ng.material.input,global.ng.material.datepicker));
}(this, (function (exports,common,forms,core,core$1,formField,input,datepicker) { 'use strict';

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
    var FormlyDatepickerTypeComponent = /** @class */ (function (_super) {
        __extends(FormlyDatepickerTypeComponent, _super);
        function FormlyDatepickerTypeComponent() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    datepickerOptions: {
                        startView: 'month',
                        datepickerTogglePosition: 'suffix',
                        dateInput: ( /**
                         * @return {?}
                         */function () { }),
                        dateChange: ( /**
                         * @return {?}
                         */function () { }),
                        monthSelected: ( /**
                         * @return {?}
                         */function () { }),
                        yearSelected: ( /**
                         * @return {?}
                         */function () { }),
                    },
                },
            };
            return _this;
        }
        /**
         * @return {?}
         */
        FormlyDatepickerTypeComponent.prototype.ngAfterViewInit = /**
         * @return {?}
         */
            function () {
                var _this = this;
                _super.prototype.ngAfterViewInit.call(this);
                // temporary fix for https://github.com/angular/material2/issues/6728
                (( /** @type {?} */(this.datepickerInput)))._formField = this.formField;
                setTimeout(( /**
                 * @return {?}
                 */function () {
                    core$1.ɵdefineHiddenProp(_this.field, '_mat' + _this.to.datepickerOptions.datepickerTogglePosition, _this.datepickerToggle);
                    (( /** @type {?} */(_this.options)))._markForCheck(_this.field);
                }));
            };
        FormlyDatepickerTypeComponent.decorators = [
            { type: core.Component, args: [{
                        selector: 'formly-field-mat-datepicker',
                        template: "\n    <input matInput\n      [id]=\"id\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [matDatepicker]=\"picker\"\n      [matDatepickerFilter]=\"to.datepickerOptions.filter\"\n      [max]=\"to.datepickerOptions.max\"\n      [min]=\"to.datepickerOptions.min\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      (dateInput)=\"to.datepickerOptions.dateInput(field, $event)\"\n      (dateChange)=\"to.datepickerOptions.dateChange(field, $event)\">\n    <ng-template #datepickerToggle>\n      <mat-datepicker-toggle [for]=\"picker\"></mat-datepicker-toggle>\n    </ng-template>\n    <mat-datepicker #picker\n      [color]=\"to.color\"\n      [dateClass]=\"to.datepickerOptions.dateClass\"\n      [disabled]=\"to.datepickerOptions.disabled\"\n      [opened]=\"to.datepickerOptions.opened\"\n      [panelClass]=\"to.datepickerOptions.panelClass\"\n      [startAt]=\"to.datepickerOptions.startAt\"\n      [startView]=\"to.datepickerOptions.startView\"\n      [touchUi]=\"to.datepickerOptions.touchUi\"\n      (monthSelected)=\"to.datepickerOptions.monthSelected(field, $event, picker)\"\n      (yearSelected)=\"to.datepickerOptions.yearSelected(field, $event, picker)\"\n    >\n    </mat-datepicker>\n  "
                    }] }
        ];
        FormlyDatepickerTypeComponent.propDecorators = {
            formFieldControl: [{ type: core.ViewChild, args: [input.MatInput, ( /** @type {?} */({ static: true })),] }],
            datepickerInput: [{ type: core.ViewChild, args: [datepicker.MatDatepickerInput,] }],
            datepickerToggle: [{ type: core.ViewChild, args: ['datepickerToggle',] }]
        };
        return FormlyDatepickerTypeComponent;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatDatepickerModule = /** @class */ (function () {
        function FormlyMatDatepickerModule() {
        }
        FormlyMatDatepickerModule.decorators = [
            { type: core.NgModule, args: [{
                        declarations: [FormlyDatepickerTypeComponent],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            input.MatInputModule,
                            datepicker.MatDatepickerModule,
                            formField.FormlyMatFormFieldModule,
                            core$1.FormlyModule.forChild({
                                types: [{
                                        name: 'datepicker',
                                        component: FormlyDatepickerTypeComponent,
                                        wrappers: ['form-field'],
                                    }],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatDatepickerModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatDatepickerModule = FormlyMatDatepickerModule;
    exports.ɵa = FormlyDatepickerTypeComponent;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-datepicker.umd.js.map