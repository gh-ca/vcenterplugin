import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatSlider, MatSliderModule } from '@angular/material/slider';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlySliderTypeComponent = /** @class */ (function (_super) {
    __extends(FormlySliderTypeComponent, _super);
    function FormlySliderTypeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                thumbLabel: false,
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlySliderTypeComponent.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.slider.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    FormlySliderTypeComponent.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-slider',
                    template: "\n    <mat-slider\n      [id]=\"id\"\n      [style.width]=\"'100%'\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [color]=\"to.color\"\n      [thumbLabel]=\"to.thumbLabel\"\n      [step]=\"to.step\"\n      [max]=\"to.max\"\n      [min]=\"to.min\">\n    </mat-slider>\n  "
                }] }
    ];
    FormlySliderTypeComponent.propDecorators = {
        slider: [{ type: ViewChild, args: [MatSlider,] }]
    };
    return FormlySliderTypeComponent;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatSliderModule = /** @class */ (function () {
    function FormlyMatSliderModule() {
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
    return FormlyMatSliderModule;
}());

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