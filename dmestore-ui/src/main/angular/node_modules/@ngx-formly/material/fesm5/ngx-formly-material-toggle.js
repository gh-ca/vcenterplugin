import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatSlideToggle, MatSlideToggleModule } from '@angular/material/slide-toggle';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyToggleTypeComponent = /** @class */ (function (_super) {
    __extends(FormlyToggleTypeComponent, _super);
    function FormlyToggleTypeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                hideLabel: true,
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyToggleTypeComponent.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.slideToggle.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    FormlyToggleTypeComponent.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-toggle',
                    template: "\n    <mat-slide-toggle\n      [id]=\"id\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [color]=\"to.color\"\n      [tabindex]=\"to.tabindex\"\n      [required]=\"to.required\">\n      {{ to.label }}\n    </mat-slide-toggle>\n  "
                }] }
    ];
    FormlyToggleTypeComponent.propDecorators = {
        slideToggle: [{ type: ViewChild, args: [MatSlideToggle,] }]
    };
    return FormlyToggleTypeComponent;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatToggleModule = /** @class */ (function () {
    function FormlyMatToggleModule() {
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
    return FormlyMatToggleModule;
}());

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