import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatSlider, MatSliderModule } from '@angular/material/slider';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlySliderTypeComponent extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                thumbLabel: false,
            },
        };
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        this.slider.focus();
        super.onContainerClick(event);
    }
}
FormlySliderTypeComponent.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-slider',
                template: `
    <mat-slider
      [id]="id"
      [style.width]="'100%'"
      [formControl]="formControl"
      [formlyAttributes]="field"
      [tabindex]="to.tabindex"
      [color]="to.color"
      [thumbLabel]="to.thumbLabel"
      [step]="to.step"
      [max]="to.max"
      [min]="to.min">
    </mat-slider>
  `
            }] }
];
FormlySliderTypeComponent.propDecorators = {
    slider: [{ type: ViewChild, args: [MatSlider,] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatSliderModule {
}
FormlyMatSliderModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlySliderTypeComponent],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatSliderModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
                        types: [{
                                name: 'slider',
                                component: FormlySliderTypeComponent,
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

export { FormlyMatSliderModule, FormlySliderTypeComponent as ɵa };

//# sourceMappingURL=ngx-formly-material-slider.js.map