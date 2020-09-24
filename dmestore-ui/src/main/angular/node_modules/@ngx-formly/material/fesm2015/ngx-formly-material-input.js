import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatInput, MatInputModule } from '@angular/material/input';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldInput extends FieldType {
    /**
     * @return {?}
     */
    get type() {
        return this.to.type || 'text';
    }
}
FormlyFieldInput.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-input',
                template: `
    <input *ngIf="type !== 'number'; else numberTmp"
      matInput
      [id]="id"
      [type]="type || 'text'"
      [readonly]="to.readonly"
      [required]="to.required"
      [errorStateMatcher]="errorStateMatcher"
      [formControl]="formControl"
      [formlyAttributes]="field"
      [tabindex]="to.tabindex"
      [placeholder]="to.placeholder">
    <ng-template #numberTmp>
      <input matInput
             [id]="id"
             type="number"
             [readonly]="to.readonly"
             [required]="to.required"
             [errorStateMatcher]="errorStateMatcher"
             [formControl]="formControl"
             [formlyAttributes]="field"
             [tabindex]="to.tabindex"
             [placeholder]="to.placeholder">
    </ng-template>
  `
            }] }
];
FormlyFieldInput.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: false })),] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatInputModule {
}
FormlyMatInputModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyFieldInput],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatInputModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
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

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatInputModule, FormlyFieldInput };

//# sourceMappingURL=ngx-formly-material-input.js.map