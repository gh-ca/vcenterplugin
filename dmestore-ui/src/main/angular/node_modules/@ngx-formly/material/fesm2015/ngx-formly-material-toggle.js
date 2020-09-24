import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatSlideToggle, MatSlideToggleModule } from '@angular/material/slide-toggle';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyToggleTypeComponent extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                hideLabel: true,
            },
        };
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        this.slideToggle.focus();
        super.onContainerClick(event);
    }
}
FormlyToggleTypeComponent.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-toggle',
                template: `
    <mat-slide-toggle
      [id]="id"
      [formControl]="formControl"
      [formlyAttributes]="field"
      [color]="to.color"
      [tabindex]="to.tabindex"
      [required]="to.required">
      {{ to.label }}
    </mat-slide-toggle>
  `
            }] }
];
FormlyToggleTypeComponent.propDecorators = {
    slideToggle: [{ type: ViewChild, args: [MatSlideToggle,] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatToggleModule {
}
FormlyMatToggleModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyToggleTypeComponent],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatSlideToggleModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
                        types: [{
                                name: 'toggle',
                                component: FormlyToggleTypeComponent,
                                wrappers: ['form-field'],
                            }],
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

export { FormlyMatToggleModule, FormlyToggleTypeComponent as ɵa };

//# sourceMappingURL=ngx-formly-material-toggle.js.map