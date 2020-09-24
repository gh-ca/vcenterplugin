(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/common'), require('@angular/forms'), require('@ngx-formly/core'), require('@ngx-formly/core/select'), require('@angular/core'), require('@angular/material/select'), require('@ngx-formly/material/form-field'), require('@angular/material/core')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material/select', ['exports', '@angular/common', '@angular/forms', '@ngx-formly/core', '@ngx-formly/core/select', '@angular/core', '@angular/material/select', '@ngx-formly/material/form-field', '@angular/material/core'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = global['ngx-formly'].material || {}, global['ngx-formly'].material.select = {}),global.ng.common,global.ng.forms,global.core,global.select,global.ng.core,global.ng.material.select,global['ngx-formly'].material['form-field'],global.ng.material.core));
}(this, (function (exports,common,forms,core,select,core$1,select$1,formField,core$2) { 'use strict';

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
    var FormlyFieldSelect = /** @class */ (function (_super) {
        __extends(FormlyFieldSelect, _super);
        function FormlyFieldSelect() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.defaultOptions = {
                templateOptions: {
                    options: [],
                    compareWith: /**
                     * @param {?} o1
                     * @param {?} o2
                     * @return {?}
                     */ function (o1, o2) {
                        return o1 === o2;
                    },
                },
            };
            return _this;
        }
        /**
         * @param {?} options
         * @return {?}
         */
        FormlyFieldSelect.prototype.getSelectAllState = /**
         * @param {?} options
         * @return {?}
         */
            function (options) {
                if (this.empty || this.value.length === 0) {
                    return '';
                }
                return this.value.length !== this.getSelectAllValue(options).length
                    ? 'indeterminate'
                    : 'checked';
            };
        /**
         * @param {?} options
         * @return {?}
         */
        FormlyFieldSelect.prototype.toggleSelectAll = /**
         * @param {?} options
         * @return {?}
         */
            function (options) {
                /** @type {?} */
                var selectAllValue = this.getSelectAllValue(options);
                this.formControl.setValue(!this.value || this.value.length !== selectAllValue.length
                    ? selectAllValue
                    : []);
            };
        /**
         * @param {?} $event
         * @return {?}
         */
        FormlyFieldSelect.prototype.change = /**
         * @param {?} $event
         * @return {?}
         */
            function ($event) {
                if (this.to.change) {
                    this.to.change(this.field, $event);
                }
            };
        /**
         * @return {?}
         */
        FormlyFieldSelect.prototype._getAriaLabelledby = /**
         * @return {?}
         */
            function () {
                if (this.to.attributes && this.to.attributes['aria-labelledby']) {
                    return this.to.attributes['aria-labelledby'];
                }
                if (this.formField && this.formField._labelId) {
                    return this.formField._labelId;
                }
                return null;
            };
        /**
         * @private
         * @param {?} options
         * @return {?}
         */
        FormlyFieldSelect.prototype.getSelectAllValue = /**
         * @private
         * @param {?} options
         * @return {?}
         */
            function (options) {
                if (!this.selectAllValue || options !== this.selectAllValue.options) {
                    /** @type {?} */
                    var flatOptions_1 = [];
                    options.forEach(( /**
                     * @param {?} o
                     * @return {?}
                     */function (o) {
                        return o.group
                            ? flatOptions_1.push.apply(flatOptions_1, __spread(o.group)) : flatOptions_1.push(o);
                    }));
                    this.selectAllValue = {
                        options: options,
                        value: flatOptions_1.map(( /**
                         * @param {?} o
                         * @return {?}
                         */function (o) { return o.value; })),
                    };
                }
                return this.selectAllValue.value;
            };
        FormlyFieldSelect.decorators = [
            { type: core$1.Component, args: [{
                        selector: 'formly-field-mat-select',
                        template: "\n    <ng-template #selectAll let-selectOptions=\"selectOptions\">\n      <mat-option (click)=\"toggleSelectAll(selectOptions)\">\n        <mat-pseudo-checkbox class=\"mat-option-pseudo-checkbox\"\n          [state]=\"getSelectAllState(selectOptions)\">\n        </mat-pseudo-checkbox>\n        {{ to.selectAllOption }}\n      </mat-option>\n    </ng-template>\n\n    <mat-select [id]=\"id\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [required]=\"to.required\"\n      [compareWith]=\"to.compareWith\"\n      [multiple]=\"to.multiple\"\n      (selectionChange)=\"change($event)\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [aria-labelledby]=\"_getAriaLabelledby()\"\n      [disableOptionCentering]=\"to.disableOptionCentering\"\n      >\n      <ng-container *ngIf=\"to.options | formlySelectOptions:field | async as selectOptions\">\n        <ng-container *ngIf=\"to.multiple && to.selectAllOption\" [ngTemplateOutlet]=\"selectAll\" [ngTemplateOutletContext]=\"{ selectOptions: selectOptions }\">\n        </ng-container>\n        <ng-container *ngFor=\"let item of selectOptions\">\n          <mat-optgroup *ngIf=\"item.group\" [label]=\"item.label\">\n            <mat-option *ngFor=\"let child of item.group\" [value]=\"child.value\" [disabled]=\"child.disabled\">\n              {{ child.label }}\n            </mat-option>\n          </mat-optgroup>\n          <mat-option *ngIf=\"!item.group\" [value]=\"item.value\" [disabled]=\"item.disabled\">{{ item.label }}</mat-option>\n        </ng-container>\n      </ng-container>\n    </mat-select>\n  "
                    }] }
        ];
        FormlyFieldSelect.propDecorators = {
            formFieldControl: [{ type: core$1.ViewChild, args: [select$1.MatSelect, ( /** @type {?} */({ static: true })),] }]
        };
        return FormlyFieldSelect;
    }(formField.FieldType));

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMatSelectModule = /** @class */ (function () {
        function FormlyMatSelectModule() {
        }
        FormlyMatSelectModule.decorators = [
            { type: core$1.NgModule, args: [{
                        declarations: [FormlyFieldSelect],
                        imports: [
                            common.CommonModule,
                            forms.ReactiveFormsModule,
                            select$1.MatSelectModule,
                            core$2.MatPseudoCheckboxModule,
                            formField.FormlyMatFormFieldModule,
                            select.FormlySelectModule,
                            core.FormlyModule.forChild({
                                types: [{
                                        name: 'select',
                                        component: FormlyFieldSelect,
                                        wrappers: ['form-field'],
                                    }],
                            }),
                        ],
                    },] }
        ];
        return FormlyMatSelectModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FormlyMatSelectModule = FormlyMatSelectModule;
    exports.FormlyFieldSelect = FormlyFieldSelect;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material-select.umd.js.map