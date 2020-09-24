import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { ɵdefineHiddenProp, FormlyModule } from '@ngx-formly/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatDatepickerInput, MatDatepickerModule } from '@angular/material/datepicker';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyDatepickerTypeComponent extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                datepickerOptions: {
                    startView: 'month',
                    datepickerTogglePosition: 'suffix',
                    dateInput: (/**
                     * @return {?}
                     */
                    () => { }),
                    dateChange: (/**
                     * @return {?}
                     */
                    () => { }),
                    monthSelected: (/**
                     * @return {?}
                     */
                    () => { }),
                    yearSelected: (/**
                     * @return {?}
                     */
                    () => { }),
                },
            },
        };
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        super.ngAfterViewInit();
        // temporary fix for https://github.com/angular/material2/issues/6728
        ((/** @type {?} */ (this.datepickerInput)))._formField = this.formField;
        setTimeout((/**
         * @return {?}
         */
        () => {
            ɵdefineHiddenProp(this.field, '_mat' + this.to.datepickerOptions.datepickerTogglePosition, this.datepickerToggle);
            ((/** @type {?} */ (this.options)))._markForCheck(this.field);
        }));
    }
}
FormlyDatepickerTypeComponent.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-datepicker',
                template: `
    <input matInput
      [id]="id"
      [errorStateMatcher]="errorStateMatcher"
      [formControl]="formControl"
      [matDatepicker]="picker"
      [matDatepickerFilter]="to.datepickerOptions.filter"
      [max]="to.datepickerOptions.max"
      [min]="to.datepickerOptions.min"
      [formlyAttributes]="field"
      [placeholder]="to.placeholder"
      [tabindex]="to.tabindex"
      [readonly]="to.readonly"
      [required]="to.required"
      (dateInput)="to.datepickerOptions.dateInput(field, $event)"
      (dateChange)="to.datepickerOptions.dateChange(field, $event)">
    <ng-template #datepickerToggle>
      <mat-datepicker-toggle [for]="picker"></mat-datepicker-toggle>
    </ng-template>
    <mat-datepicker #picker
      [color]="to.color"
      [dateClass]="to.datepickerOptions.dateClass"
      [disabled]="to.datepickerOptions.disabled"
      [opened]="to.datepickerOptions.opened"
      [panelClass]="to.datepickerOptions.panelClass"
      [startAt]="to.datepickerOptions.startAt"
      [startView]="to.datepickerOptions.startView"
      [touchUi]="to.datepickerOptions.touchUi"
      (monthSelected)="to.datepickerOptions.monthSelected(field, $event, picker)"
      (yearSelected)="to.datepickerOptions.yearSelected(field, $event, picker)"
    >
    </mat-datepicker>
  `
            }] }
];
FormlyDatepickerTypeComponent.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }],
    datepickerInput: [{ type: ViewChild, args: [MatDatepickerInput,] }],
    datepickerToggle: [{ type: ViewChild, args: ['datepickerToggle',] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatDatepickerModule {
}
FormlyMatDatepickerModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyDatepickerTypeComponent],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatInputModule,
                    MatDatepickerModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
                        types: [{
                                name: 'datepicker',
                                component: FormlyDatepickerTypeComponent,
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

export { FormlyMatDatepickerModule, FormlyDatepickerTypeComponent as ɵa };

//# sourceMappingURL=ngx-formly-material-datepicker.js.map