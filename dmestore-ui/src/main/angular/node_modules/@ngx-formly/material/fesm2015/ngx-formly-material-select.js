import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatPseudoCheckboxModule } from '@angular/material/core';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyFieldSelect extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                options: [],
                /**
                 * @param {?} o1
                 * @param {?} o2
                 * @return {?}
                 */
                compareWith(o1, o2) {
                    return o1 === o2;
                },
            },
        };
    }
    /**
     * @param {?} options
     * @return {?}
     */
    getSelectAllState(options) {
        if (this.empty || this.value.length === 0) {
            return '';
        }
        return this.value.length !== this.getSelectAllValue(options).length
            ? 'indeterminate'
            : 'checked';
    }
    /**
     * @param {?} options
     * @return {?}
     */
    toggleSelectAll(options) {
        /** @type {?} */
        const selectAllValue = this.getSelectAllValue(options);
        this.formControl.setValue(!this.value || this.value.length !== selectAllValue.length
            ? selectAllValue
            : []);
    }
    /**
     * @param {?} $event
     * @return {?}
     */
    change($event) {
        if (this.to.change) {
            this.to.change(this.field, $event);
        }
    }
    /**
     * @return {?}
     */
    _getAriaLabelledby() {
        if (this.to.attributes && this.to.attributes['aria-labelledby']) {
            return this.to.attributes['aria-labelledby'];
        }
        if (this.formField && this.formField._labelId) {
            return this.formField._labelId;
        }
        return null;
    }
    /**
     * @private
     * @param {?} options
     * @return {?}
     */
    getSelectAllValue(options) {
        if (!this.selectAllValue || options !== this.selectAllValue.options) {
            /** @type {?} */
            const flatOptions = [];
            options.forEach((/**
             * @param {?} o
             * @return {?}
             */
            o => o.group
                ? flatOptions.push(...o.group)
                : flatOptions.push(o)));
            this.selectAllValue = {
                options,
                value: flatOptions.map((/**
                 * @param {?} o
                 * @return {?}
                 */
                o => o.value)),
            };
        }
        return this.selectAllValue.value;
    }
}
FormlyFieldSelect.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-select',
                template: `
    <ng-template #selectAll let-selectOptions="selectOptions">
      <mat-option (click)="toggleSelectAll(selectOptions)">
        <mat-pseudo-checkbox class="mat-option-pseudo-checkbox"
          [state]="getSelectAllState(selectOptions)">
        </mat-pseudo-checkbox>
        {{ to.selectAllOption }}
      </mat-option>
    </ng-template>

    <mat-select [id]="id"
      [formControl]="formControl"
      [formlyAttributes]="field"
      [placeholder]="to.placeholder"
      [tabindex]="to.tabindex"
      [required]="to.required"
      [compareWith]="to.compareWith"
      [multiple]="to.multiple"
      (selectionChange)="change($event)"
      [errorStateMatcher]="errorStateMatcher"
      [aria-labelledby]="_getAriaLabelledby()"
      [disableOptionCentering]="to.disableOptionCentering"
      >
      <ng-container *ngIf="to.options | formlySelectOptions:field | async as selectOptions">
        <ng-container *ngIf="to.multiple && to.selectAllOption" [ngTemplateOutlet]="selectAll" [ngTemplateOutletContext]="{ selectOptions: selectOptions }">
        </ng-container>
        <ng-container *ngFor="let item of selectOptions">
          <mat-optgroup *ngIf="item.group" [label]="item.label">
            <mat-option *ngFor="let child of item.group" [value]="child.value" [disabled]="child.disabled">
              {{ child.label }}
            </mat-option>
          </mat-optgroup>
          <mat-option *ngIf="!item.group" [value]="item.value" [disabled]="item.disabled">{{ item.label }}</mat-option>
        </ng-container>
      </ng-container>
    </mat-select>
  `
            }] }
];
FormlyFieldSelect.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatSelect, (/** @type {?} */ ({ static: true })),] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatSelectModule {
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