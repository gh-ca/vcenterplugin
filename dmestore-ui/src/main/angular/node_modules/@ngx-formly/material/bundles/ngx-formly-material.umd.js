(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/core'), require('@ngx-formly/material/form-field'), require('@ngx-formly/material/input'), require('@ngx-formly/material/textarea'), require('@ngx-formly/material/radio'), require('@ngx-formly/material/checkbox'), require('@ngx-formly/material/multicheckbox'), require('@ngx-formly/material/select')) :
    typeof define === 'function' && define.amd ? define('@ngx-formly/material', ['exports', '@angular/core', '@ngx-formly/material/form-field', '@ngx-formly/material/input', '@ngx-formly/material/textarea', '@ngx-formly/material/radio', '@ngx-formly/material/checkbox', '@ngx-formly/material/multicheckbox', '@ngx-formly/material/select'], factory) :
    (factory((global['ngx-formly'] = global['ngx-formly'] || {}, global['ngx-formly'].material = {}),global.ng.core,global['ngx-formly'].material['form-field'],global['ngx-formly'].material.input,global['ngx-formly'].material.textarea,global['ngx-formly'].material.radio,global['ngx-formly'].material.checkbox,global['ngx-formly'].material.multicheckbox,global['ngx-formly'].material.select));
}(this, (function (exports,core,formField,input,textarea,radio,checkbox,multicheckbox,select) { 'use strict';

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */
    var FormlyMaterialModule = /** @class */ (function () {
        function FormlyMaterialModule() {
        }
        FormlyMaterialModule.decorators = [
            { type: core.NgModule, args: [{
                        imports: [
                            formField.FormlyMatFormFieldModule,
                            input.FormlyMatInputModule,
                            textarea.FormlyMatTextAreaModule,
                            radio.FormlyMatRadioModule,
                            checkbox.FormlyMatCheckboxModule,
                            multicheckbox.FormlyMatMultiCheckboxModule,
                            select.FormlyMatSelectModule,
                        ],
                    },] }
        ];
        return FormlyMaterialModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
     */

    exports.FieldType = formField.FieldType;
    exports.FormlyMaterialModule = FormlyMaterialModule;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=ngx-formly-material.umd.js.map