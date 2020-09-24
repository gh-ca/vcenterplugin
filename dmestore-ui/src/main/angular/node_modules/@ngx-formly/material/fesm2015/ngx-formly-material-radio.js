import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatRadioGroup, MatRadioModule } from '@angular/material/radio';
import { ɵwrapProperty, FormlyModule } from '@ngx-formly/core';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldRadio extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                tabindex: -1,
            },
        };
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        this.focusObserver = ɵwrapProperty(this.field, 'focus', (/**
         * @param {?} __0
         * @return {?}
         */
        ({ currentValue }) => {
            if (this.to.tabindex === -1
                && currentValue
                && this.radioGroup._radios.length > 0) {
                /** @type {?} */
                const radio = this.radioGroup.selected
                    ? this.radioGroup.selected
                    : this.radioGroup._radios.first;
                radio.focus();
            }
        }));
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.focusObserver && this.focusObserver();
    }
}
FormlyFieldRadio.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-radio',
                template: `
    <mat-radio-group
      [formControl]="formControl"
      [formlyAttributes]="field"
      [required]="to.required"
      [tabindex]="to.tabindex">
      <mat-radio-button *ngFor="let option of to.options | formlySelectOptions:field | async; let i = index;"
        [id]="id + '_' + i"
        [color]="to.color"
        [labelPosition]="to.labelPosition"
        [value]="option.value">
        {{ option.label }}
      </mat-radio-button>
    </mat-radio-group>
  `
            }] }
];
FormlyFieldRadio.propDecorators = {
    radioGroup: [{ type: ViewChild, args: [MatRadioGroup,] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatRadioModule {
}
FormlyMatRadioModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyFieldRadio],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatRadioModule,
                    FormlyMatFormFieldModule,
                    FormlySelectModule,
                    FormlyModule.forChild({
                        types: [{
                                name: 'radio',
                                component: FormlyFieldRadio,
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

export { FormlyMatRadioModule, FormlyFieldRadio };

//# sourceMappingURL=ngx-formly-material-radio.js.map