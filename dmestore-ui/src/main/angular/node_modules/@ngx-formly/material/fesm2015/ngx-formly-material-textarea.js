import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatInput, MAT_INPUT_VALUE_ACCESSOR, MatInputModule } from '@angular/material/input';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldTextArea extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                cols: 1,
                rows: 1,
            },
        };
    }
}
FormlyFieldTextArea.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-textarea',
                template: `
    <textarea matInput
      [id]="id"
      [readonly]="to.readonly"
      [required]="to.required"
      [formControl]="formControl"
      [errorStateMatcher]="errorStateMatcher"
      [cols]="to.cols"
      [rows]="to.rows"
      [formlyAttributes]="field"
      [placeholder]="to.placeholder"
      [tabindex]="to.tabindex"
      [cdkTextareaAutosize]="to.autosize"
      [cdkAutosizeMinRows]="to.autosizeMinRows"
      [cdkAutosizeMaxRows]="to.autosizeMaxRows"
      [class.cdk-textarea-autosize]="to.autosize"
      >
    </textarea>
  `,
                providers: [
                    // fix for https://github.com/ngx-formly/ngx-formly/issues/1688
                    // rely on formControl value instead of elementRef which return empty value in Firefox.
                    { provide: MAT_INPUT_VALUE_ACCESSOR, useExisting: FormlyFieldTextArea },
                ]
            }] }
];
FormlyFieldTextArea.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatTextAreaModule {
}
FormlyMatTextAreaModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyFieldTextArea],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatInputModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
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

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatTextAreaModule, FormlyFieldTextArea };

//# sourceMappingURL=ngx-formly-material-textarea.js.map