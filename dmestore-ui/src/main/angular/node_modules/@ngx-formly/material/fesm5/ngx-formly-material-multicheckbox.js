import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { __extends, __spread, __assign } from 'tslib';
import { Component, ViewChildren, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatCheckbox, MatCheckboxModule } from '@angular/material/checkbox';

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
                ? __spread((this.formControl.value || []), [value]) : __spread((this.formControl.value || [])).filter((/**
             * @param {?} o
             * @return {?}
             */
            function (o) { return o !== value; })));
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
        { type: Component, args: [{
                    selector: 'formly-field-mat-multicheckbox',
                    template: "\n    <ng-container *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\">\n      <mat-checkbox\n        [id]=\"id + '_' + i\"\n        [formlyAttributes]=\"field\"\n        [tabindex]=\"to.tabindex\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [checked]=\"isChecked(option)\"\n        [disabled]=\"formControl.disabled\"\n        (change)=\"onChange(option.value, $event.checked)\">\n          {{ option.label }}\n      </mat-checkbox>\n    </ng-container>\n  "
                }] }
    ];
    FormlyFieldMultiCheckbox.propDecorators = {
        checkboxes: [{ type: ViewChildren, args: [MatCheckbox,] }]
    };
    return FormlyFieldMultiCheckbox;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatMultiCheckboxModule = /** @class */ (function () {
    function FormlyMatMultiCheckboxModule() {
    }
    FormlyMatMultiCheckboxModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldMultiCheckbox],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatCheckboxModule,
                        FormlyMatFormFieldModule,
                        FormlySelectModule,
                        FormlyModule.forChild({
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

export { FormlyMatMultiCheckboxModule, FormlyFieldMultiCheckbox };

//# sourceMappingURL=ngx-formly-material-multicheckbox.js.map