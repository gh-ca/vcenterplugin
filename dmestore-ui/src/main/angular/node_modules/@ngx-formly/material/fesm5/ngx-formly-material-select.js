import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { __extends, __spread } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatPseudoCheckboxModule } from '@angular/material/core';

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
                 */
                function (o1, o2) {
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
            options.forEach((/**
             * @param {?} o
             * @return {?}
             */
            function (o) { return o.group
                ? flatOptions_1.push.apply(flatOptions_1, __spread(o.group)) : flatOptions_1.push(o); }));
            this.selectAllValue = {
                options: options,
                value: flatOptions_1.map((/**
                 * @param {?} o
                 * @return {?}
                 */
                function (o) { return o.value; })),
            };
        }
        return this.selectAllValue.value;
    };
    FormlyFieldSelect.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-select',
                    template: "\n    <ng-template #selectAll let-selectOptions=\"selectOptions\">\n      <mat-option (click)=\"toggleSelectAll(selectOptions)\">\n        <mat-pseudo-checkbox class=\"mat-option-pseudo-checkbox\"\n          [state]=\"getSelectAllState(selectOptions)\">\n        </mat-pseudo-checkbox>\n        {{ to.selectAllOption }}\n      </mat-option>\n    </ng-template>\n\n    <mat-select [id]=\"id\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [required]=\"to.required\"\n      [compareWith]=\"to.compareWith\"\n      [multiple]=\"to.multiple\"\n      (selectionChange)=\"change($event)\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [aria-labelledby]=\"_getAriaLabelledby()\"\n      [disableOptionCentering]=\"to.disableOptionCentering\"\n      >\n      <ng-container *ngIf=\"to.options | formlySelectOptions:field | async as selectOptions\">\n        <ng-container *ngIf=\"to.multiple && to.selectAllOption\" [ngTemplateOutlet]=\"selectAll\" [ngTemplateOutletContext]=\"{ selectOptions: selectOptions }\">\n        </ng-container>\n        <ng-container *ngFor=\"let item of selectOptions\">\n          <mat-optgroup *ngIf=\"item.group\" [label]=\"item.label\">\n            <mat-option *ngFor=\"let child of item.group\" [value]=\"child.value\" [disabled]=\"child.disabled\">\n              {{ child.label }}\n            </mat-option>\n          </mat-optgroup>\n          <mat-option *ngIf=\"!item.group\" [value]=\"item.value\" [disabled]=\"item.disabled\">{{ item.label }}</mat-option>\n        </ng-container>\n      </ng-container>\n    </mat-select>\n  "
                }] }
    ];
    FormlyFieldSelect.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatSelect, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyFieldSelect;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatSelectModule = /** @class */ (function () {
    function FormlyMatSelectModule() {
    }
    FormlyMatSelectModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldSelect],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatSelectModule,
                        MatPseudoCheckboxModule,
                        FormlyMatFormFieldModule,
                        FormlySelectModule,
                        FormlyModule.forChild({
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

export { FormlyMatSelectModule, FormlyFieldSelect };

//# sourceMappingURL=ngx-formly-material-select.js.map