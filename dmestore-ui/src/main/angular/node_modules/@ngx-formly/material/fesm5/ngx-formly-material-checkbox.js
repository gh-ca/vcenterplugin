import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, Renderer2, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatCheckbox, MatCheckboxModule } from '@angular/material/checkbox';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyFieldCheckbox = /** @class */ (function (_super) {
    __extends(FormlyFieldCheckbox, _super);
    function FormlyFieldCheckbox(renderer) {
        var _this = _super.call(this) || this;
        _this.renderer = renderer;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                indeterminate: true,
                floatLabel: 'always',
                hideLabel: true,
                align: 'start',
                // start or end
                color: 'accent',
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyFieldCheckbox.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.checkbox.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    /**
     * @return {?}
     */
    FormlyFieldCheckbox.prototype.ngAfterViewChecked = /**
     * @return {?}
     */
    function () {
        if (this.required !== this._required && this.checkbox && this.checkbox._inputElement) {
            this._required = this.required;
            /** @type {?} */
            var inputElement = this.checkbox._inputElement.nativeElement;
            if (this.required) {
                this.renderer.setAttribute(inputElement, 'required', 'required');
            }
            else {
                this.renderer.removeAttribute(inputElement, 'required');
            }
        }
    };
    FormlyFieldCheckbox.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-checkbox',
                    template: "\n    <mat-checkbox\n      [formControl]=\"formControl\"\n      [id]=\"id\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [indeterminate]=\"to.indeterminate && formControl.value === null\"\n      [color]=\"to.color\"\n      [labelPosition]=\"to.align || to.labelPosition\">\n      {{ to.label }}\n      <span *ngIf=\"to.required && to.hideRequiredMarker !== true\" class=\"mat-form-field-required-marker\">*</span>\n    </mat-checkbox>\n  "
                }] }
    ];
    /** @nocollapse */
    FormlyFieldCheckbox.ctorParameters = function () { return [
        { type: Renderer2 }
    ]; };
    FormlyFieldCheckbox.propDecorators = {
        checkbox: [{ type: ViewChild, args: [MatCheckbox,] }]
    };
    return FormlyFieldCheckbox;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatCheckboxModule = /** @class */ (function () {
    function FormlyMatCheckboxModule() {
    }
    FormlyMatCheckboxModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldCheckbox],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatCheckboxModule,
                        FormlyMatFormFieldModule,
                        FormlyModule.forChild({
                            types: [
                                {
                                    name: 'checkbox',
                                    component: FormlyFieldCheckbox,
                                    wrappers: ['form-field'],
                                },
                            ],
                        }),
                    ],
                },] }
    ];
    return FormlyMatCheckboxModule;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatCheckboxModule, FormlyFieldCheckbox };

//# sourceMappingURL=ngx-formly-material-checkbox.js.map