import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldNativeSelect extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                options: [],
            },
        };
    }
}
FormlyFieldNativeSelect.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-native-select',
                template: `
    <select matNativeControl
      [id]="id"
      [readonly]="to.readonly"
      [required]="to.required"
      [errorStateMatcher]="errorStateMatcher"
      [formControl]="formControl"
      [formlyAttributes]="field">
      <option *ngIf="to.placeholder" [ngValue]="null">{{ to.placeholder }}</option>
      <ng-container *ngIf="to.options | formlySelectOptions:field | async as opts">
        <ng-container *ngIf="to._flatOptions else grouplist">
          <ng-container *ngFor="let opt of opts">
            <option [ngValue]="opt.value" [disabled]="opt.disabled">{{ opt.label }}</option>
          </ng-container>
        </ng-container>

        <ng-template #grouplist>
          <ng-container *ngFor="let opt of opts">
            <option *ngIf="!opt.group else optgroup" [ngValue]="opt.value" [disabled]="opt.disabled">{{ opt.label }}</option>
            <ng-template #optgroup>
              <optgroup [label]="opt.label">
                <option *ngFor="let child of opt.group" [ngValue]="child.value" [disabled]="child.disabled">
                  {{ child.label }}
                </option>
              </optgroup>
            </ng-template>
          </ng-container>
        </ng-template>
      </ng-container>
    </select>
  `
            }] }
];
FormlyFieldNativeSelect.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatNativeSelectModule {
}
FormlyMatNativeSelectModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyFieldNativeSelect],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatInputModule,
                    FormlyMatFormFieldModule,
                    FormlySelectModule,
                    FormlyModule.forChild({
                        types: [
                            {
                                name: 'native-select',
                                component: FormlyFieldNativeSelect,
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

export { FormlyMatNativeSelectModule, FormlyFieldNativeSelect };

//# sourceMappingURL=ngx-formly-material-native-select.js.map