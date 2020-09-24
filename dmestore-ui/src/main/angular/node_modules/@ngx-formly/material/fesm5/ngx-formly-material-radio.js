import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlySelectModule } from '@ngx-formly/core/select';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatRadioGroup, MatRadioModule } from '@angular/material/radio';
import { ɵwrapProperty, FormlyModule } from '@ngx-formly/core';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyFieldRadio = /** @class */ (function (_super) {
    __extends(FormlyFieldRadio, _super);
    function FormlyFieldRadio() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                tabindex: -1,
            },
        };
        return _this;
    }
    /**
     * @return {?}
     */
    FormlyFieldRadio.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        this.focusObserver = ɵwrapProperty(this.field, 'focus', (/**
         * @param {?} __0
         * @return {?}
         */
        function (_a) {
            var currentValue = _a.currentValue;
            if (_this.to.tabindex === -1
                && currentValue
                && _this.radioGroup._radios.length > 0) {
                /** @type {?} */
                var radio = _this.radioGroup.selected
                    ? _this.radioGroup.selected
                    : _this.radioGroup._radios.first;
                radio.focus();
            }
        }));
    };
    /**
     * @return {?}
     */
    FormlyFieldRadio.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.focusObserver && this.focusObserver();
    };
    FormlyFieldRadio.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-radio',
                    template: "\n    <mat-radio-group\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [required]=\"to.required\"\n      [tabindex]=\"to.tabindex\">\n      <mat-radio-button *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\"\n        [id]=\"id + '_' + i\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [value]=\"option.value\">\n        {{ option.label }}\n      </mat-radio-button>\n    </mat-radio-group>\n  "
                }] }
    ];
    FormlyFieldRadio.propDecorators = {
        radioGroup: [{ type: ViewChild, args: [MatRadioGroup,] }]
    };
    return FormlyFieldRadio;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatRadioModule = /** @class */ (function () {
    function FormlyMatRadioModule() {
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
    return FormlyMatRadioModule;
}());

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