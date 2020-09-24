/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @record
 */
export function FormlyFieldConfig() { }
if (false) {
    /**
     * The model that stores all the data, where the model[key] is the value of the field
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.model;
    /**
     * The parent field.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.parent;
    /** @type {?|undefined} */
    FormlyFieldConfig.prototype.options;
    /** @type {?|undefined} */
    FormlyFieldConfig.prototype.form;
    /**
     * The key that relates to the model. This will link the field value to the model
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.key;
    /**
     * This allows you to specify the `id` of your field. Note, the `id` is generated if not set.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.id;
    /**
     * If you wish, you can specify a specific `name` for your field. This is useful if you're posting the form to a server using techniques of yester-year.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.name;
    /**
     * This is reserved for the templates. Any template-specific options go in here. Look at your specific template implementation to know the options required for this.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.templateOptions;
    /** @type {?|undefined} */
    FormlyFieldConfig.prototype.optionsTypes;
    /**
     * An object with a few useful properties
     * - `validation.messages`: A map of message names that will be displayed when the field has errors.
     * - `validation.show`: A boolean you as the developer can set to force displaying errors whatever the state of field. This is useful when you're trying to call the user's attention to some fields for some reason.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.validation;
    /**
     * Used to set validation rules for a particular field.
     * Should be an object of key - value pairs. The value can either be an expression to evaluate or a function to run.
     * Each should return a boolean value, returning true when the field is valid. See Validation for more information.
     *
     * {
     *   validation?: (string | ValidatorFn)[];
     *   [key: string]: ((control: AbstractControl, field: FormlyFieldConfig) => boolean) | ({ expression: (control: AbstractControl, field: FormlyFieldConfig) => boolean, message: ValidationMessageOption['message'] });
     * }
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.validators;
    /**
     * Use this one for anything that needs to validate asynchronously.
     * Pretty much exactly the same as the validators api, except it must be a function that returns a promise.
     *
     * {
     *   validation?: (string | AsyncValidatorFn)[];
     *   [key: string]: ((control: AbstractControl, field: FormlyFieldConfig) => Promise<boolean>) | ({ expression: (control: AbstractControl, field: FormlyFieldConfig) => Promise<boolean>, message: string });
     * }
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.asyncValidators;
    /**
     * Can be set instead of `type` to render custom html content.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.template;
    /**
     *  It is expected to be the name of the wrappers.
     *  The formly field template will be wrapped by the first wrapper, then the second, then the third, etc.
     *  You can also specify these as part of a type (which is the recommended approach).
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.wrappers;
    /**
     * Whether to hide the field. Defaults to false. If you wish this to be conditional use `hideExpression`
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.hide;
    /**
     * Conditionally hiding Field based on values from other Fields
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.hideExpression;
    /**
     * An object where the key is a property to be set on the main field config and the value is an expression used to assign that property.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.expressionProperties;
    /**
     * This is the [FormControl](https://angular.io/api/forms/FormControl) for the field.
     * It provides you more control like running validators, calculating status, and resetting state.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.formControl;
    /**
     * You can specify your own class that will be applied to the `formly-field` component.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.className;
    /**
     * Specify your own class that will be applied to the `formly-group` component.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.fieldGroupClassName;
    /**
     * A field group is a way to group fields together, making advanced layout very simple.
     * It can also be used to group fields that are associated with the same model (useful if it's different than the model for the rest of the fields).
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.fieldGroup;
    /** @type {?|undefined} */
    FormlyFieldConfig.prototype.fieldArray;
    /**
     * This should be a formly-field type added either by you or a plugin. More information over at Creating Formly Fields.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.type;
    /**
     * Whether to focus or blur the element field. Defaults to false. If you wish this to be conditional use `expressionProperties`
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.focus;
    /**
     * An object with a few useful properties to control the model changes
     * - `debounce`: integer value which contains the debounce model update value in milliseconds. A value of 0 triggers an immediate update.
     * - `updateOn`: string event value that instructs when the control should be updated
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.modelOptions;
    /** @type {?|undefined} */
    FormlyFieldConfig.prototype.hooks;
    /**
     * @deprecated use `hooks` instead
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.lifecycle;
    /**
     * Use `defaultValue` to initialize it the model. If this is provided and the value of the model at compile-time is undefined, then the value of the model will be assigned to `defaultValue`.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.defaultValue;
    /**
     * Array of functions to execute, as a pipeline, whenever the model updates, usually via user input.
     * @type {?|undefined}
     */
    FormlyFieldConfig.prototype.parsers;
}
/**
 * @record
 */
export function ExpressionPropertyCache() { }
if (false) {
    /** @type {?} */
    ExpressionPropertyCache.prototype.expression;
    /** @type {?|undefined} */
    ExpressionPropertyCache.prototype.expressionValue;
}
/**
 * @record
 */
export function FormlyFieldConfigCache() { }
if (false) {
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype.parent;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype.options;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._expressionProperties;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._hide;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._validators;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._asyncValidators;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._componentRefs;
    /** @type {?|undefined} */
    FormlyFieldConfigCache.prototype._keyPath;
}
/**
 * @record
 */
export function FormlyTemplateOptions() { }
if (false) {
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.type;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.label;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.placeholder;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.disabled;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.options;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.rows;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.cols;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.description;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.hidden;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.max;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.min;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.minLength;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.maxLength;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.pattern;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.required;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.tabindex;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.readonly;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.attributes;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.step;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.focus;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.blur;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.keyup;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.keydown;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.click;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.change;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.keypress;
    /** @type {?|undefined} */
    FormlyTemplateOptions.prototype.templateManipulators;
    /* Skipping unhandled member: [additionalProperties: string]: any;*/
}
/**
 * @record
 */
export function FormlyLifeCycleFn() { }
/**
 * @record
 */
export function FormlyHookFn() { }
/**
 * @record
 * @template T
 */
export function FormlyLifeCycleOptions() { }
if (false) {
    /** @type {?|undefined} */
    FormlyLifeCycleOptions.prototype.onInit;
    /** @type {?|undefined} */
    FormlyLifeCycleOptions.prototype.onChanges;
    /** @type {?|undefined} */
    FormlyLifeCycleOptions.prototype.afterContentInit;
    /** @type {?|undefined} */
    FormlyLifeCycleOptions.prototype.afterViewInit;
    /** @type {?|undefined} */
    FormlyLifeCycleOptions.prototype.onDestroy;
    /**
     * @deprecated
     * @type {?|undefined}
     */
    FormlyLifeCycleOptions.prototype.doCheck;
    /**
     * @deprecated
     * @type {?|undefined}
     */
    FormlyLifeCycleOptions.prototype.afterContentChecked;
    /**
     * @deprecated
     * @type {?|undefined}
     */
    FormlyLifeCycleOptions.prototype.afterViewChecked;
    /* Skipping unhandled member: [additionalProperties: string]: any;*/
}
/**
 * @record
 */
export function FormlyFormOptionsCache() { }
if (false) {
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._checkField;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._markForCheck;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._buildForm;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._buildField;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._resolver;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._injector;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._hiddenFieldsForCheck;
    /** @type {?|undefined} */
    FormlyFormOptionsCache.prototype._initialModel;
}
/**
 * @record
 */
export function FormlyFormOptions() { }
if (false) {
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.updateInitialValue;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.resetModel;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.formState;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.fieldChanges;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.fieldTransform;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.showError;
    /** @type {?|undefined} */
    FormlyFormOptions.prototype.parentForm;
}
/**
 * @record
 */
export function FormlyValueChangeEvent() { }
if (false) {
    /** @type {?} */
    FormlyValueChangeEvent.prototype.field;
    /** @type {?} */
    FormlyValueChangeEvent.prototype.type;
    /** @type {?} */
    FormlyValueChangeEvent.prototype.value;
    /* Skipping unhandled member: [meta: string]: any;*/
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZpZWxkLmNvbmZpZy5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7QUFNQSx1Q0FxS0M7Ozs7OztJQWpLQyxrQ0FBcUI7Ozs7O0lBS3JCLG1DQUFvQzs7SUFHcEMsb0NBQXFDOztJQUNyQyxpQ0FBMEI7Ozs7O0lBSzFCLGdDQUFpQzs7Ozs7SUFLakMsK0JBQVk7Ozs7O0lBS1osaUNBQWM7Ozs7O0lBS2QsNENBQXdDOztJQUV4Qyx5Q0FBd0I7Ozs7Ozs7SUFPeEIsdUNBTUU7Ozs7Ozs7Ozs7OztJQVlGLHVDQUFpQjs7Ozs7Ozs7Ozs7SUFXakIsNENBQXNCOzs7OztJQUt0QixxQ0FBa0I7Ozs7Ozs7SUFPbEIscUNBQW9COzs7OztJQUtwQixpQ0FBZTs7Ozs7SUFLZiwyQ0FBeUc7Ozs7O0lBS3pHLGlEQUEySTs7Ozs7O0lBTTNJLHdDQUE4Qjs7Ozs7SUFLOUIsc0NBQW1COzs7OztJQUtuQixnREFBNkI7Ozs7OztJQU03Qix1Q0FBaUM7O0lBRWpDLHVDQUErQjs7Ozs7SUFLL0IsaUNBQWM7Ozs7O0lBS2Qsa0NBQWdCOzs7Ozs7O0lBT2hCLHlDQVFFOztJQUVGLGtDQUE2Qzs7Ozs7SUFLN0Msc0NBQW1DOzs7OztJQUtuQyx5Q0FBbUI7Ozs7O0lBS25CLG9DQUFpQzs7Ozs7QUFHbkMsNkNBR0M7OztJQUZDLDZDQUFtRjs7SUFDbkYsa0RBQXNCOzs7OztBQUd4Qiw0Q0FZQzs7O0lBWEMsd0NBQWdDOztJQUNoQyx5Q0FBaUM7O0lBQ2pDLHVEQUF3RTs7SUFDeEUsdUNBQWdCOztJQUNoQiw2Q0FBNEI7O0lBQzVCLGtEQUFzQzs7SUFDdEMsZ0RBQTJDOztJQUMzQywwQ0FHRTs7Ozs7QUFLSiwyQ0E2QkM7OztJQTVCQyxxQ0FBYzs7SUFDZCxzQ0FBZTs7SUFDZiw0Q0FBcUI7O0lBQ3JCLHlDQUFtQjs7SUFDbkIsd0NBQW9DOztJQUNwQyxxQ0FBYzs7SUFDZCxxQ0FBYzs7SUFDZCw0Q0FBcUI7O0lBQ3JCLHVDQUFpQjs7SUFDakIsb0NBQWE7O0lBQ2Isb0NBQWE7O0lBQ2IsMENBQW1COztJQUNuQiwwQ0FBbUI7O0lBQ25CLHdDQUF3Qjs7SUFDeEIseUNBQW1COztJQUNuQix5Q0FBa0I7O0lBQ2xCLHlDQUFtQjs7SUFDbkIsMkNBQThDOztJQUM5QyxxQ0FBYzs7SUFDZCxzQ0FBNkI7O0lBQzdCLHFDQUE0Qjs7SUFDNUIsc0NBQTZCOztJQUM3Qix3Q0FBK0I7O0lBQy9CLHNDQUE2Qjs7SUFDN0IsdUNBQThCOztJQUM5Qix5Q0FBZ0M7O0lBQ2hDLHFEQUE0Qzs7Ozs7O0FBSTlDLHVDQUVDOzs7O0FBRUQsa0NBRUM7Ozs7O0FBRUQsNENBZ0JDOzs7SUFmQyx3Q0FBVzs7SUFDWCwyQ0FBYzs7SUFDZCxrREFBcUI7O0lBQ3JCLCtDQUFrQjs7SUFDbEIsMkNBQWM7Ozs7O0lBSWQseUNBQVk7Ozs7O0lBR1oscURBQXdCOzs7OztJQUd4QixrREFBcUI7Ozs7OztBQUd2Qiw0Q0FTQzs7O0lBUkMsNkNBQTZFOztJQUM3RSwrQ0FBd0Q7O0lBQ3hELDRDQUF3Qjs7SUFDeEIsNkNBQXdFOztJQUN4RSwyQ0FBcUM7O0lBQ3JDLDJDQUFxQjs7SUFDckIsdURBQWlEOztJQUNqRCwrQ0FBb0I7Ozs7O0FBRXRCLHVDQVFDOzs7SUFQQywrQ0FBZ0M7O0lBQ2hDLHVDQUFtQzs7SUFDbkMsc0NBQWdCOztJQUNoQix5Q0FBK0M7O0lBQy9DLDJDQUEySTs7SUFDM0ksc0NBQTBDOztJQUMxQyx1Q0FBdUM7Ozs7O0FBR3pDLDRDQUtDOzs7SUFKQyx1Q0FBeUI7O0lBQ3pCLHNDQUFhOztJQUNiLHVDQUFXIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRm9ybUdyb3VwLCBBYnN0cmFjdENvbnRyb2wsIEZvcm1Hcm91cERpcmVjdGl2ZSwgRm9ybUFycmF5LCBBc3luY1ZhbGlkYXRvckZuLCBWYWxpZGF0b3JGbiB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IFN1YmplY3QsIE9ic2VydmFibGUgfSBmcm9tICdyeGpzJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJy4uL3RlbXBsYXRlcy9maWVsZC50eXBlJztcbmltcG9ydCB7IFRlbXBsYXRlTWFuaXB1bGF0b3JzLCBWYWxpZGF0aW9uTWVzc2FnZU9wdGlvbiB9IGZyb20gJy4uL3NlcnZpY2VzL2Zvcm1seS5jb25maWcnO1xuaW1wb3J0IHsgQ29tcG9uZW50RmFjdG9yeVJlc29sdmVyLCBDb21wb25lbnRSZWYsIEluamVjdG9yIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5cbmV4cG9ydCBpbnRlcmZhY2UgRm9ybWx5RmllbGRDb25maWcge1xuICAvKipcbiAgICogVGhlIG1vZGVsIHRoYXQgc3RvcmVzIGFsbCB0aGUgZGF0YSwgd2hlcmUgdGhlIG1vZGVsW2tleV0gaXMgdGhlIHZhbHVlIG9mIHRoZSBmaWVsZFxuICAgKi9cbiAgcmVhZG9ubHkgbW9kZWw/OiBhbnk7XG5cbiAgLyoqXG4gICAqIFRoZSBwYXJlbnQgZmllbGQuXG4gICAqL1xuICByZWFkb25seSBwYXJlbnQ/OiBGb3JtbHlGaWVsZENvbmZpZztcblxuXG4gIHJlYWRvbmx5IG9wdGlvbnM/OiBGb3JtbHlGb3JtT3B0aW9ucztcbiAgcmVhZG9ubHkgZm9ybT86IEZvcm1Hcm91cDtcblxuICAvKipcbiAgICogVGhlIGtleSB0aGF0IHJlbGF0ZXMgdG8gdGhlIG1vZGVsLiBUaGlzIHdpbGwgbGluayB0aGUgZmllbGQgdmFsdWUgdG8gdGhlIG1vZGVsXG4gICAqL1xuICBrZXk/OiBzdHJpbmcgfCBudW1iZXIgfCBzdHJpbmdbXTtcblxuICAvKipcbiAgICogVGhpcyBhbGxvd3MgeW91IHRvIHNwZWNpZnkgdGhlIGBpZGAgb2YgeW91ciBmaWVsZC4gTm90ZSwgdGhlIGBpZGAgaXMgZ2VuZXJhdGVkIGlmIG5vdCBzZXQuXG4gICAqL1xuICBpZD86IHN0cmluZztcblxuICAvKipcbiAgICogSWYgeW91IHdpc2gsIHlvdSBjYW4gc3BlY2lmeSBhIHNwZWNpZmljIGBuYW1lYCBmb3IgeW91ciBmaWVsZC4gVGhpcyBpcyB1c2VmdWwgaWYgeW91J3JlIHBvc3RpbmcgdGhlIGZvcm0gdG8gYSBzZXJ2ZXIgdXNpbmcgdGVjaG5pcXVlcyBvZiB5ZXN0ZXIteWVhci5cbiAgICovXG4gIG5hbWU/OiBzdHJpbmc7XG5cbiAgLyoqXG4gICAqIFRoaXMgaXMgcmVzZXJ2ZWQgZm9yIHRoZSB0ZW1wbGF0ZXMuIEFueSB0ZW1wbGF0ZS1zcGVjaWZpYyBvcHRpb25zIGdvIGluIGhlcmUuIExvb2sgYXQgeW91ciBzcGVjaWZpYyB0ZW1wbGF0ZSBpbXBsZW1lbnRhdGlvbiB0byBrbm93IHRoZSBvcHRpb25zIHJlcXVpcmVkIGZvciB0aGlzLlxuICAgKi9cbiAgdGVtcGxhdGVPcHRpb25zPzogRm9ybWx5VGVtcGxhdGVPcHRpb25zO1xuXG4gIG9wdGlvbnNUeXBlcz86IHN0cmluZ1tdO1xuXG4gIC8qKlxuICAgKiBBbiBvYmplY3Qgd2l0aCBhIGZldyB1c2VmdWwgcHJvcGVydGllc1xuICAgKiAtIGB2YWxpZGF0aW9uLm1lc3NhZ2VzYDogQSBtYXAgb2YgbWVzc2FnZSBuYW1lcyB0aGF0IHdpbGwgYmUgZGlzcGxheWVkIHdoZW4gdGhlIGZpZWxkIGhhcyBlcnJvcnMuXG4gICAqIC0gYHZhbGlkYXRpb24uc2hvd2A6IEEgYm9vbGVhbiB5b3UgYXMgdGhlIGRldmVsb3BlciBjYW4gc2V0IHRvIGZvcmNlIGRpc3BsYXlpbmcgZXJyb3JzIHdoYXRldmVyIHRoZSBzdGF0ZSBvZiBmaWVsZC4gVGhpcyBpcyB1c2VmdWwgd2hlbiB5b3UncmUgdHJ5aW5nIHRvIGNhbGwgdGhlIHVzZXIncyBhdHRlbnRpb24gdG8gc29tZSBmaWVsZHMgZm9yIHNvbWUgcmVhc29uLlxuICAgKi9cbiAgdmFsaWRhdGlvbj86IHtcbiAgICBtZXNzYWdlcz86IHtcbiAgICAgIFttZXNzYWdlUHJvcGVydGllczogc3RyaW5nXTogVmFsaWRhdGlvbk1lc3NhZ2VPcHRpb25bJ21lc3NhZ2UnXTtcbiAgICB9O1xuICAgIHNob3c/OiBib29sZWFuO1xuICAgIFthZGRpdGlvbmFsUHJvcGVydGllczogc3RyaW5nXTogYW55O1xuICB9O1xuXG4gIC8qKlxuICAgKiBVc2VkIHRvIHNldCB2YWxpZGF0aW9uIHJ1bGVzIGZvciBhIHBhcnRpY3VsYXIgZmllbGQuXG4gICAqIFNob3VsZCBiZSBhbiBvYmplY3Qgb2Yga2V5IC0gdmFsdWUgcGFpcnMuIFRoZSB2YWx1ZSBjYW4gZWl0aGVyIGJlIGFuIGV4cHJlc3Npb24gdG8gZXZhbHVhdGUgb3IgYSBmdW5jdGlvbiB0byBydW4uXG4gICAqIEVhY2ggc2hvdWxkIHJldHVybiBhIGJvb2xlYW4gdmFsdWUsIHJldHVybmluZyB0cnVlIHdoZW4gdGhlIGZpZWxkIGlzIHZhbGlkLiBTZWUgVmFsaWRhdGlvbiBmb3IgbW9yZSBpbmZvcm1hdGlvbi5cbiAgICpcbiAgICoge1xuICAgKiAgIHZhbGlkYXRpb24/OiAoc3RyaW5nIHwgVmFsaWRhdG9yRm4pW107XG4gICAqICAgW2tleTogc3RyaW5nXTogKChjb250cm9sOiBBYnN0cmFjdENvbnRyb2wsIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykgPT4gYm9vbGVhbikgfCAoeyBleHByZXNzaW9uOiAoY29udHJvbDogQWJzdHJhY3RDb250cm9sLCBmaWVsZDogRm9ybWx5RmllbGRDb25maWcpID0+IGJvb2xlYW4sIG1lc3NhZ2U6IFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uWydtZXNzYWdlJ10gfSk7XG4gICAqIH1cbiAgICovXG4gIHZhbGlkYXRvcnM/OiBhbnk7XG5cbiAgLyoqXG4gICAqIFVzZSB0aGlzIG9uZSBmb3IgYW55dGhpbmcgdGhhdCBuZWVkcyB0byB2YWxpZGF0ZSBhc3luY2hyb25vdXNseS5cbiAgICogUHJldHR5IG11Y2ggZXhhY3RseSB0aGUgc2FtZSBhcyB0aGUgdmFsaWRhdG9ycyBhcGksIGV4Y2VwdCBpdCBtdXN0IGJlIGEgZnVuY3Rpb24gdGhhdCByZXR1cm5zIGEgcHJvbWlzZS5cbiAgICpcbiAgICoge1xuICAgKiAgIHZhbGlkYXRpb24/OiAoc3RyaW5nIHwgQXN5bmNWYWxpZGF0b3JGbilbXTtcbiAgICogICBba2V5OiBzdHJpbmddOiAoKGNvbnRyb2w6IEFic3RyYWN0Q29udHJvbCwgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKSA9PiBQcm9taXNlPGJvb2xlYW4+KSB8ICh7IGV4cHJlc3Npb246IChjb250cm9sOiBBYnN0cmFjdENvbnRyb2wsIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykgPT4gUHJvbWlzZTxib29sZWFuPiwgbWVzc2FnZTogc3RyaW5nIH0pO1xuICAgKiB9XG4gICAqL1xuICBhc3luY1ZhbGlkYXRvcnM/OiBhbnk7XG5cbiAgLyoqXG4gICAqIENhbiBiZSBzZXQgaW5zdGVhZCBvZiBgdHlwZWAgdG8gcmVuZGVyIGN1c3RvbSBodG1sIGNvbnRlbnQuXG4gICAqL1xuICB0ZW1wbGF0ZT86IHN0cmluZztcblxuICAvKipcbiAgICogIEl0IGlzIGV4cGVjdGVkIHRvIGJlIHRoZSBuYW1lIG9mIHRoZSB3cmFwcGVycy5cbiAgICogIFRoZSBmb3JtbHkgZmllbGQgdGVtcGxhdGUgd2lsbCBiZSB3cmFwcGVkIGJ5IHRoZSBmaXJzdCB3cmFwcGVyLCB0aGVuIHRoZSBzZWNvbmQsIHRoZW4gdGhlIHRoaXJkLCBldGMuXG4gICAqICBZb3UgY2FuIGFsc28gc3BlY2lmeSB0aGVzZSBhcyBwYXJ0IG9mIGEgdHlwZSAod2hpY2ggaXMgdGhlIHJlY29tbWVuZGVkIGFwcHJvYWNoKS5cbiAgICovXG4gIHdyYXBwZXJzPzogc3RyaW5nW107XG5cbiAgLyoqXG4gICAqIFdoZXRoZXIgdG8gaGlkZSB0aGUgZmllbGQuIERlZmF1bHRzIHRvIGZhbHNlLiBJZiB5b3Ugd2lzaCB0aGlzIHRvIGJlIGNvbmRpdGlvbmFsIHVzZSBgaGlkZUV4cHJlc3Npb25gXG4gICAqL1xuICBoaWRlPzogYm9vbGVhbjtcblxuICAvKipcbiAgICogQ29uZGl0aW9uYWxseSBoaWRpbmcgRmllbGQgYmFzZWQgb24gdmFsdWVzIGZyb20gb3RoZXIgRmllbGRzXG4gICAqL1xuICBoaWRlRXhwcmVzc2lvbj86IGJvb2xlYW4gfCBzdHJpbmcgfCAoKG1vZGVsOiBhbnksIGZvcm1TdGF0ZTogYW55LCBmaWVsZD86IEZvcm1seUZpZWxkQ29uZmlnKSA9PiBib29sZWFuKTtcblxuICAvKipcbiAgICogQW4gb2JqZWN0IHdoZXJlIHRoZSBrZXkgaXMgYSBwcm9wZXJ0eSB0byBiZSBzZXQgb24gdGhlIG1haW4gZmllbGQgY29uZmlnIGFuZCB0aGUgdmFsdWUgaXMgYW4gZXhwcmVzc2lvbiB1c2VkIHRvIGFzc2lnbiB0aGF0IHByb3BlcnR5LlxuICAgKi9cbiAgZXhwcmVzc2lvblByb3BlcnRpZXM/OiB7IFtwcm9wZXJ0eTogc3RyaW5nXTogc3RyaW5nIHwgKChtb2RlbDogYW55LCBmb3JtU3RhdGU6IGFueSwgZmllbGQ/OiBGb3JtbHlGaWVsZENvbmZpZykgPT4gYW55KSB8IE9ic2VydmFibGU8YW55PiB9O1xuXG4gIC8qKlxuICAgKiBUaGlzIGlzIHRoZSBbRm9ybUNvbnRyb2xdKGh0dHBzOi8vYW5ndWxhci5pby9hcGkvZm9ybXMvRm9ybUNvbnRyb2wpIGZvciB0aGUgZmllbGQuXG4gICAqIEl0IHByb3ZpZGVzIHlvdSBtb3JlIGNvbnRyb2wgbGlrZSBydW5uaW5nIHZhbGlkYXRvcnMsIGNhbGN1bGF0aW5nIHN0YXR1cywgYW5kIHJlc2V0dGluZyBzdGF0ZS5cbiAgICovXG4gIGZvcm1Db250cm9sPzogQWJzdHJhY3RDb250cm9sO1xuXG4gIC8qKlxuICAgKiBZb3UgY2FuIHNwZWNpZnkgeW91ciBvd24gY2xhc3MgdGhhdCB3aWxsIGJlIGFwcGxpZWQgdG8gdGhlIGBmb3JtbHktZmllbGRgIGNvbXBvbmVudC5cbiAgICovXG4gIGNsYXNzTmFtZT86IHN0cmluZztcblxuICAvKipcbiAgICogU3BlY2lmeSB5b3VyIG93biBjbGFzcyB0aGF0IHdpbGwgYmUgYXBwbGllZCB0byB0aGUgYGZvcm1seS1ncm91cGAgY29tcG9uZW50LlxuICAgKi9cbiAgZmllbGRHcm91cENsYXNzTmFtZT86IHN0cmluZztcblxuICAvKipcbiAgICogQSBmaWVsZCBncm91cCBpcyBhIHdheSB0byBncm91cCBmaWVsZHMgdG9nZXRoZXIsIG1ha2luZyBhZHZhbmNlZCBsYXlvdXQgdmVyeSBzaW1wbGUuXG4gICAqIEl0IGNhbiBhbHNvIGJlIHVzZWQgdG8gZ3JvdXAgZmllbGRzIHRoYXQgYXJlIGFzc29jaWF0ZWQgd2l0aCB0aGUgc2FtZSBtb2RlbCAodXNlZnVsIGlmIGl0J3MgZGlmZmVyZW50IHRoYW4gdGhlIG1vZGVsIGZvciB0aGUgcmVzdCBvZiB0aGUgZmllbGRzKS5cbiAgICovXG4gIGZpZWxkR3JvdXA/OiBGb3JtbHlGaWVsZENvbmZpZ1tdO1xuXG4gIGZpZWxkQXJyYXk/OiBGb3JtbHlGaWVsZENvbmZpZztcblxuICAvKipcbiAgICogVGhpcyBzaG91bGQgYmUgYSBmb3JtbHktZmllbGQgdHlwZSBhZGRlZCBlaXRoZXIgYnkgeW91IG9yIGEgcGx1Z2luLiBNb3JlIGluZm9ybWF0aW9uIG92ZXIgYXQgQ3JlYXRpbmcgRm9ybWx5IEZpZWxkcy5cbiAgICovXG4gIHR5cGU/OiBzdHJpbmc7XG5cbiAgLyoqXG4gICAqIFdoZXRoZXIgdG8gZm9jdXMgb3IgYmx1ciB0aGUgZWxlbWVudCBmaWVsZC4gRGVmYXVsdHMgdG8gZmFsc2UuIElmIHlvdSB3aXNoIHRoaXMgdG8gYmUgY29uZGl0aW9uYWwgdXNlIGBleHByZXNzaW9uUHJvcGVydGllc2BcbiAgICovXG4gIGZvY3VzPzogYm9vbGVhbjtcblxuICAvKipcbiAgICogQW4gb2JqZWN0IHdpdGggYSBmZXcgdXNlZnVsIHByb3BlcnRpZXMgdG8gY29udHJvbCB0aGUgbW9kZWwgY2hhbmdlc1xuICAgKiAtIGBkZWJvdW5jZWA6IGludGVnZXIgdmFsdWUgd2hpY2ggY29udGFpbnMgdGhlIGRlYm91bmNlIG1vZGVsIHVwZGF0ZSB2YWx1ZSBpbiBtaWxsaXNlY29uZHMuIEEgdmFsdWUgb2YgMCB0cmlnZ2VycyBhbiBpbW1lZGlhdGUgdXBkYXRlLlxuICAgKiAtIGB1cGRhdGVPbmA6IHN0cmluZyBldmVudCB2YWx1ZSB0aGF0IGluc3RydWN0cyB3aGVuIHRoZSBjb250cm9sIHNob3VsZCBiZSB1cGRhdGVkXG4gICAqL1xuICBtb2RlbE9wdGlvbnM/OiB7XG4gICAgZGVib3VuY2U/OiB7XG4gICAgICBkZWZhdWx0OiBudW1iZXI7XG4gICAgfTtcbiAgICAvKipcbiAgICAgKiBAc2VlIGh0dHBzOi8vYW5ndWxhci5pby9hcGkvZm9ybXMvQWJzdHJhY3RDb250cm9sI3VwZGF0ZU9uXG4gICAgICovXG4gICAgdXBkYXRlT24/OiAnY2hhbmdlJyB8ICdibHVyJyB8ICdzdWJtaXQnO1xuICB9O1xuXG4gIGhvb2tzPzogRm9ybWx5TGlmZUN5Y2xlT3B0aW9uczxGb3JtbHlIb29rRm4+O1xuXG4gIC8qKlxuICAgKiBAZGVwcmVjYXRlZCB1c2UgYGhvb2tzYCBpbnN0ZWFkXG4gICAqL1xuICBsaWZlY3ljbGU/OiBGb3JtbHlMaWZlQ3ljbGVPcHRpb25zO1xuXG4gIC8qKlxuICAgKiBVc2UgYGRlZmF1bHRWYWx1ZWAgdG8gaW5pdGlhbGl6ZSBpdCB0aGUgbW9kZWwuIElmIHRoaXMgaXMgcHJvdmlkZWQgYW5kIHRoZSB2YWx1ZSBvZiB0aGUgbW9kZWwgYXQgY29tcGlsZS10aW1lIGlzIHVuZGVmaW5lZCwgdGhlbiB0aGUgdmFsdWUgb2YgdGhlIG1vZGVsIHdpbGwgYmUgYXNzaWduZWQgdG8gYGRlZmF1bHRWYWx1ZWAuXG4gICAqL1xuICBkZWZhdWx0VmFsdWU/OiBhbnk7XG5cbiAgLyoqXG4gICAqIEFycmF5IG9mIGZ1bmN0aW9ucyB0byBleGVjdXRlLCBhcyBhIHBpcGVsaW5lLCB3aGVuZXZlciB0aGUgbW9kZWwgdXBkYXRlcywgdXN1YWxseSB2aWEgdXNlciBpbnB1dC5cbiAgICovXG4gIHBhcnNlcnM/OiAoKHZhbHVlOiBhbnkpID0+IHt9KVtdO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIEV4cHJlc3Npb25Qcm9wZXJ0eUNhY2hlIHtcbiAgZXhwcmVzc2lvbjogKG1vZGVsOiBhbnksIGZvcm1TdGF0ZTogYW55LCBmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkgPT4gYm9vbGVhbjtcbiAgZXhwcmVzc2lvblZhbHVlPzogYW55O1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgZXh0ZW5kcyBGb3JtbHlGaWVsZENvbmZpZyB7XG4gIHBhcmVudD86IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGU7XG4gIG9wdGlvbnM/OiBGb3JtbHlGb3JtT3B0aW9uc0NhY2hlO1xuICBfZXhwcmVzc2lvblByb3BlcnRpZXM/OiB7IFtwcm9wZXJ0eTogc3RyaW5nXTogRXhwcmVzc2lvblByb3BlcnR5Q2FjaGUgfTtcbiAgX2hpZGU/OiBib29sZWFuO1xuICBfdmFsaWRhdG9ycz86IFZhbGlkYXRvckZuW107XG4gIF9hc3luY1ZhbGlkYXRvcnM/OiBBc3luY1ZhbGlkYXRvckZuW107XG4gIF9jb21wb25lbnRSZWZzPzogQ29tcG9uZW50UmVmPEZpZWxkVHlwZT5bXTtcbiAgX2tleVBhdGg/OiB7XG4gICAga2V5OiBGb3JtbHlGaWVsZENvbmZpZ1sna2V5J107XG4gICAgcGF0aDogc3RyaW5nW107XG4gIH07XG59XG5cbmV4cG9ydCB0eXBlIEZvcm1seUF0dHJpYnV0ZUV2ZW50ID0gKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZywgZXZlbnQ/OiBhbnkpID0+IHZvaWQ7XG5cbmV4cG9ydCBpbnRlcmZhY2UgRm9ybWx5VGVtcGxhdGVPcHRpb25zIHtcbiAgdHlwZT86IHN0cmluZztcbiAgbGFiZWw/OiBzdHJpbmc7XG4gIHBsYWNlaG9sZGVyPzogc3RyaW5nO1xuICBkaXNhYmxlZD86IGJvb2xlYW47XG4gIG9wdGlvbnM/OiBhbnlbXSB8IE9ic2VydmFibGU8YW55W10+O1xuICByb3dzPzogbnVtYmVyO1xuICBjb2xzPzogbnVtYmVyO1xuICBkZXNjcmlwdGlvbj86IHN0cmluZztcbiAgaGlkZGVuPzogYm9vbGVhbjtcbiAgbWF4PzogbnVtYmVyO1xuICBtaW4/OiBudW1iZXI7XG4gIG1pbkxlbmd0aD86IG51bWJlcjtcbiAgbWF4TGVuZ3RoPzogbnVtYmVyO1xuICBwYXR0ZXJuPzogc3RyaW5nfFJlZ0V4cDtcbiAgcmVxdWlyZWQ/OiBib29sZWFuO1xuICB0YWJpbmRleD86IG51bWJlcjtcbiAgcmVhZG9ubHk/OiBib29sZWFuO1xuICBhdHRyaWJ1dGVzPzogeyBba2V5OiBzdHJpbmddOiBzdHJpbmd8bnVtYmVyIH07XG4gIHN0ZXA/OiBudW1iZXI7XG4gIGZvY3VzPzogRm9ybWx5QXR0cmlidXRlRXZlbnQ7XG4gIGJsdXI/OiBGb3JtbHlBdHRyaWJ1dGVFdmVudDtcbiAga2V5dXA/OiBGb3JtbHlBdHRyaWJ1dGVFdmVudDtcbiAga2V5ZG93bj86IEZvcm1seUF0dHJpYnV0ZUV2ZW50O1xuICBjbGljaz86IEZvcm1seUF0dHJpYnV0ZUV2ZW50O1xuICBjaGFuZ2U/OiBGb3JtbHlBdHRyaWJ1dGVFdmVudDtcbiAga2V5cHJlc3M/OiBGb3JtbHlBdHRyaWJ1dGVFdmVudDtcbiAgdGVtcGxhdGVNYW5pcHVsYXRvcnM/OiBUZW1wbGF0ZU1hbmlwdWxhdG9ycztcbiAgW2FkZGl0aW9uYWxQcm9wZXJ0aWVzOiBzdHJpbmddOiBhbnk7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRm9ybWx5TGlmZUN5Y2xlRm4ge1xuICAoZm9ybT86IEZvcm1Hcm91cCwgZmllbGQ/OiBGb3JtbHlGaWVsZENvbmZpZywgbW9kZWw/OiBhbnksIG9wdGlvbnM/OiBGb3JtbHlGb3JtT3B0aW9ucyk6IHZvaWQ7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRm9ybWx5SG9va0ZuIHtcbiAgKGZpZWxkPzogRm9ybWx5RmllbGRDb25maWcpOiB2b2lkO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIEZvcm1seUxpZmVDeWNsZU9wdGlvbnM8VCA9IEZvcm1seUxpZmVDeWNsZUZuPiB7XG4gIG9uSW5pdD86IFQ7XG4gIG9uQ2hhbmdlcz86IFQ7XG4gIGFmdGVyQ29udGVudEluaXQ/OiBUO1xuICBhZnRlclZpZXdJbml0PzogVDtcbiAgb25EZXN0cm95PzogVDtcbiAgW2FkZGl0aW9uYWxQcm9wZXJ0aWVzOiBzdHJpbmddOiBhbnk7XG5cbiAgLyoqIEBkZXByZWNhdGVkICovXG4gIGRvQ2hlY2s/OiBUO1xuXG4gIC8qKiBAZGVwcmVjYXRlZCAqL1xuICBhZnRlckNvbnRlbnRDaGVja2VkPzogVDtcblxuICAvKiogQGRlcHJlY2F0ZWQgKi9cbiAgYWZ0ZXJWaWV3Q2hlY2tlZD86IFQ7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRm9ybWx5Rm9ybU9wdGlvbnNDYWNoZSBleHRlbmRzIEZvcm1seUZvcm1PcHRpb25zIHtcbiAgX2NoZWNrRmllbGQ/OiAoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGlnbm9yZUNhY2hlPzogYm9vbGVhbikgPT4gdm9pZDtcbiAgX21hcmtGb3JDaGVjaz86IChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkgPT4gdm9pZDtcbiAgX2J1aWxkRm9ybT86ICgpID0+IHZvaWQ7XG4gIF9idWlsZEZpZWxkPzogKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSA9PiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlO1xuICBfcmVzb2x2ZXI/OiBDb21wb25lbnRGYWN0b3J5UmVzb2x2ZXI7XG4gIF9pbmplY3Rvcj86IEluamVjdG9yO1xuICBfaGlkZGVuRmllbGRzRm9yQ2hlY2s/OiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlW107XG4gIF9pbml0aWFsTW9kZWw/OiBhbnk7XG59XG5leHBvcnQgaW50ZXJmYWNlIEZvcm1seUZvcm1PcHRpb25zIHtcbiAgdXBkYXRlSW5pdGlhbFZhbHVlPzogKCkgPT4gdm9pZDtcbiAgcmVzZXRNb2RlbD86IChtb2RlbD86IGFueSkgPT4gdm9pZDtcbiAgZm9ybVN0YXRlPzogYW55O1xuICBmaWVsZENoYW5nZXM/OiBTdWJqZWN0PEZvcm1seVZhbHVlQ2hhbmdlRXZlbnQ+O1xuICBmaWVsZFRyYW5zZm9ybT86IChmaWVsZHM6IEZvcm1seUZpZWxkQ29uZmlnW10sIG1vZGVsOiBhbnksIGZvcm06IEZvcm1Hcm91cCB8IEZvcm1BcnJheSwgb3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnMpID0+IEZvcm1seUZpZWxkQ29uZmlnW107XG4gIHNob3dFcnJvcj86IChmaWVsZDogRmllbGRUeXBlKSA9PiBib29sZWFuO1xuICBwYXJlbnRGb3JtPzogRm9ybUdyb3VwRGlyZWN0aXZlIHwgbnVsbDtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBGb3JtbHlWYWx1ZUNoYW5nZUV2ZW50IHtcbiAgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnO1xuICB0eXBlOiBzdHJpbmc7XG4gIHZhbHVlOiBhbnk7XG4gIFttZXRhOiBzdHJpbmddOiBhbnk7XG59XG5cbiJdfQ==