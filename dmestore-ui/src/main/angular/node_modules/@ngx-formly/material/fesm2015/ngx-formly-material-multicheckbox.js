import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { Component, ViewChildren, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatCheckbox, MatCheckboxModule } from '@angular/material/checkbox';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldMultiCheckbox extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                color: 'accent',
            },
        };
    }
    /**
     * @param {?} value
     * @param {?} checked
     * @return {?}
     */
    onChange(value, checked) {
        if (this.to.type === 'array') {
            this.formControl.patchValue(checked
                ? [...(this.formControl.value || []), value]
                : [...(this.formControl.value || [])].filter((/**
                 * @param {?} o
                 * @return {?}
                 */
                o => o !== value)));
        }
        else {
            this.formControl.patchValue(Object.assign({}, this.formControl.value, { [value]: checked }));
        }
        this.formControl.markAsTouched();
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        if (this.checkboxes.length) {
            this.checkboxes.first.focus();
        }
        super.onContainerClick(event);
    }
    /**
     * @param {?} option
     * @return {?}
     */
    isChecked(option) {
        /** @type {?} */
        const value = this.formControl.value;
        return value && (this.to.type === 'array'
            ? (value.indexOf(option.value) !== -1)
            : value[option.value]);
    }
}
FormlyFieldMultiCheckbox.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-multicheckbox',
                template: `
    <ng-container *ngFor="let option of to.options | formlySelectOptions:field | async; let i = index;">
      <mat-checkbox
        [id]="id + '_' + i"
        [formlyAttributes]="field"
        [tabindex]="to.tabindex"
        [color]="to.color"
        [labelPosition]="to.labelPosition"
        [checked]="isChecked(option)"
        [disabled]="formControl.disabled"
        (change)="onChange(option.value, $event.checked)">
          {{ option.label }}
      </mat-checkbox>
    </ng-container>
  `
            }] }
];
FormlyFieldMultiCheckbox.propDecorators = {
    checkboxes: [{ type: ViewChildren, args: [MatCheckbox,] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatMultiCheckboxModule {
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