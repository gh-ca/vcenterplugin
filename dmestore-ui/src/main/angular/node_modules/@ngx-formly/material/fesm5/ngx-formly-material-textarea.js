import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { MatInput, MAT_INPUT_VALUE_ACCESSOR, MatInputModule } from '@angular/material/input';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyFieldTextArea = /** @class */ (function (_super) {
    __extends(FormlyFieldTextArea, _super);
    function FormlyFieldTextArea() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                cols: 1,
                rows: 1,
            },
        };
        return _this;
    }
    FormlyFieldTextArea.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-textarea',
                    template: "\n    <textarea matInput\n      [id]=\"id\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [formControl]=\"formControl\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [cols]=\"to.cols\"\n      [rows]=\"to.rows\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [cdkTextareaAutosize]=\"to.autosize\"\n      [cdkAutosizeMinRows]=\"to.autosizeMinRows\"\n      [cdkAutosizeMaxRows]=\"to.autosizeMaxRows\"\n      [class.cdk-textarea-autosize]=\"to.autosize\"\n      >\n    </textarea>\n  ",
                    providers: [
                        // fix for https://github.com/ngx-formly/ngx-formly/issues/1688
                        // rely on formControl value instead of elementRef which return empty value in Firefox.
                        { provide: MAT_INPUT_VALUE_ACCESSOR, useExisting: FormlyFieldTextArea },
                    ]
                }] }
    ];
    FormlyFieldTextArea.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyFieldTextArea;
}(FieldType));

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatTextAreaModule = /** @class */ (function () {
    function FormlyMatTextAreaModule() {
    }
    FormlyMatTextAreaModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldTextArea],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatInputModule,
                        FormlyMatFormFieldModule,
                        FormlyModule.forChild({
                            types: [
                                {
                                    name: 'textarea',
                                    component: FormlyFieldTextArea,
                                    wrappers: ['form-field'],
                                },
                            ],
                        }),
                    ],
                },] }
    ];
    return FormlyMatTextAreaModule;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatTextAreaModule, FormlyFieldTextArea };

//# sourceMappingURL=ngx-formly-material-textarea.js.map