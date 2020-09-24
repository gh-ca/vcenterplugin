import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatInput, MatInputModule } from '@angular/material/input';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyFieldInput = /** @class */ (function (_super) {
    __extends(FormlyFieldInput, _super);
    function FormlyFieldInput() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    Object.defineProperty(FormlyFieldInput.prototype, "type", {
        get: /**
         * @return {?}
         */
        function () {
            return this.to.type || 'text';
        },
        enumerable: true,
        configurable: true
    });
    FormlyFieldInput.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-input',
                    template: "\n    <input *ngIf=\"type !== 'number'; else numberTmp\"\n      matInput\n      [id]=\"id\"\n      [type]=\"type || 'text'\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [placeholder]=\"to.placeholder\">\n    <ng-template #numberTmp>\n      <input matInput\n             [id]=\"id\"\n             type=\"number\"\n             [readonly]=\"to.readonly\"\n             [required]=\"to.required\"\n             [errorStateMatcher]=\"errorStateMatcher\"\n             [formControl]=\"formControl\"\n             [formlyAttributes]=\"field\"\n             [tabindex]=\"to.tabindex\"\n             [placeholder]=\"to.placeholder\">\n    </ng-template>\n  "
                }] }
    ];
    FormlyFieldInput.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: false })),] }]
    };
    return FormlyFieldInput;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatInputModule = /** @class */ (function () {
    function FormlyMatInputModule() {
    }
    FormlyMatInputModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldInput],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatInputModule,
                        FormlyMatFormFieldModule,
                        FormlyModule.forChild({
                            types: [
                                {
                                    name: 'input',
                                    component: FormlyFieldInput,
                                    wrappers: ['form-field'],
                                },
                            ],
                        }),
                    ],
                },] }
    ];
    return FormlyMatInputModule;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatInputModule, FormlyFieldInput };

//# sourceMappingURL=ngx-formly-material-input.js.map