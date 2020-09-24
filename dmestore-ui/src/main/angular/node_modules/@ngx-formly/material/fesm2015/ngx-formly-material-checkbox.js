import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, Renderer2, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatCheckbox, MatCheckboxModule } from '@angular/material/checkbox';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldCheckbox extends FieldType {
    /**
     * @param {?} renderer
     */
    constructor(renderer) {
        super();
        this.renderer = renderer;
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                indeterminate: true,
                floatLabel: 'always',
                hideLabel: true,
                align: 'start',
                // start or end
                color: 'accent',
            },
        };
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        this.checkbox.focus();
        super.onContainerClick(event);
    }
    /**
     * @return {?}
     */
    ngAfterViewChecked() {
        if (this.required !== this._required && this.checkbox && this.checkbox._inputElement) {
            this._required = this.required;
            /** @type {?} */
            const inputElement = this.checkbox._inputElement.nativeElement;
            if (this.required) {
                this.renderer.setAttribute(inputElement, 'required', 'required');
            }
            else {
                this.renderer.removeAttribute(inputElement, 'required');
            }
        }
    }
}
FormlyFieldCheckbox.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-checkbox',
                template: `
    <mat-checkbox
      [formControl]="formControl"
      [id]="id"
      [formlyAttributes]="field"
      [tabindex]="to.tabindex"
      [indeterminate]="to.indeterminate && formControl.value === null"
      [color]="to.color"
      [labelPosition]="to.align || to.labelPosition">
      {{ to.label }}
      <span *ngIf="to.required && to.hideRequiredMarker !== true" class="mat-form-field-required-marker">*</span>
    </mat-checkbox>
  `
            }] }
];
/** @nocollapse */
FormlyFieldCheckbox.ctorParameters = () => [
    { type: Renderer2 }
];
FormlyFieldCheckbox.propDecorators = {
    checkbox: [{ type: ViewChild, args: [MatCheckbox,] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatCheckboxModule {
}
FormlyMatCheckboxModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyFieldCheckbox],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatCheckboxModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
                        types: [
                            {
                                name: 'checkbox',
                                component: FormlyFieldCheckbox,
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

export { FormlyMatCheckboxModule, FormlyFieldCheckbox };

//# sourceMappingURL=ngx-formly-material-checkbox.js.map