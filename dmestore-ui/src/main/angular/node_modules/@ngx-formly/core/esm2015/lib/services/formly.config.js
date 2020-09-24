/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Injectable, InjectionToken } from '@angular/core';
import { reverseDeepMerge, defineHiddenProp } from './../utils';
import * as i0 from "@angular/core";
/** @type {?} */
export const FORMLY_CONFIG = new InjectionToken('FORMLY_CONFIG');
/**
 * \@experimental
 * @record
 */
export function FormlyExtension() { }
if (false) {
    /**
     * @param {?} field
     * @return {?}
     */
    FormlyExtension.prototype.prePopulate = function (field) { };
    /**
     * @param {?} field
     * @return {?}
     */
    FormlyExtension.prototype.onPopulate = function (field) { };
    /**
     * @param {?} field
     * @return {?}
     */
    FormlyExtension.prototype.postPopulate = function (field) { };
}
/**
 * Maintains list of formly field directive types. This can be used to register new field templates.
 */
export class FormlyConfig {
    constructor() {
        this.types = {};
        this.validators = {};
        this.wrappers = {};
        this.messages = {};
        this.templateManipulators = {
            preWrapper: [],
            postWrapper: [],
        };
        this.extras = {
            checkExpressionOn: 'changeDetectionCheck',
            showError: (/**
             * @param {?} field
             * @return {?}
             */
            function (field) {
                return field.formControl && field.formControl.invalid && (field.formControl.touched || (field.options.parentForm && field.options.parentForm.submitted) || !!(field.field.validation && field.field.validation.show));
            }),
        };
        this.extensions = {};
    }
    /**
     * @param {?} config
     * @return {?}
     */
    addConfig(config) {
        if (config.types) {
            config.types.forEach((/**
             * @param {?} type
             * @return {?}
             */
            type => this.setType(type)));
        }
        if (config.validators) {
            config.validators.forEach((/**
             * @param {?} validator
             * @return {?}
             */
            validator => this.setValidator(validator)));
        }
        if (config.wrappers) {
            config.wrappers.forEach((/**
             * @param {?} wrapper
             * @return {?}
             */
            wrapper => this.setWrapper(wrapper)));
        }
        if (config.manipulators) {
            console.warn(`NgxFormly: passing 'manipulators' config is deprecated, use custom extension instead.`);
            config.manipulators.forEach((/**
             * @param {?} manipulator
             * @return {?}
             */
            manipulator => this.setManipulator(manipulator)));
        }
        if (config.validationMessages) {
            config.validationMessages.forEach((/**
             * @param {?} validation
             * @return {?}
             */
            validation => this.addValidatorMessage(validation.name, validation.message)));
        }
        if (config.extensions) {
            config.extensions.forEach((/**
             * @param {?} c
             * @return {?}
             */
            c => this.extensions[c.name] = c.extension));
        }
        if (config.extras) {
            this.extras = Object.assign({}, this.extras, config.extras);
        }
    }
    /**
     * @param {?} options
     * @return {?}
     */
    setType(options) {
        if (Array.isArray(options)) {
            options.forEach((/**
             * @param {?} option
             * @return {?}
             */
            (option) => this.setType(option)));
        }
        else {
            if (!this.types[options.name]) {
                this.types[options.name] = (/** @type {?} */ ({ name: options.name }));
            }
            ['component', 'extends', 'defaultOptions'].forEach((/**
             * @param {?} prop
             * @return {?}
             */
            prop => {
                if (options.hasOwnProperty(prop)) {
                    this.types[options.name][prop] = options[prop];
                }
            }));
            if (options.wrappers) {
                options.wrappers.forEach((/**
                 * @param {?} wrapper
                 * @return {?}
                 */
                (wrapper) => this.setTypeWrapper(options.name, wrapper)));
            }
        }
    }
    /**
     * @param {?} name
     * @return {?}
     */
    getType(name) {
        if (!this.types[name]) {
            throw new Error(`[Formly Error] The type "${name}" could not be found. Please make sure that is registered through the FormlyModule declaration.`);
        }
        this.mergeExtendedType(name);
        return this.types[name];
    }
    /**
     * @param {?=} field
     * @return {?}
     */
    getMergedField(field = {}) {
        /** @type {?} */
        const type = this.getType(field.type);
        if (type.defaultOptions) {
            reverseDeepMerge(field, type.defaultOptions);
        }
        /** @type {?} */
        const extendDefaults = type.extends && this.getType(type.extends).defaultOptions;
        if (extendDefaults) {
            reverseDeepMerge(field, extendDefaults);
        }
        if (field && field.optionsTypes) {
            field.optionsTypes.forEach((/**
             * @param {?} option
             * @return {?}
             */
            option => {
                /** @type {?} */
                const defaultOptions = this.getType(option).defaultOptions;
                if (defaultOptions) {
                    reverseDeepMerge(field, defaultOptions);
                }
            }));
        }
        /** @type {?} */
        const componentRef = this.resolveFieldTypeRef(field);
        if (componentRef && componentRef.instance && componentRef.instance.defaultOptions) {
            reverseDeepMerge(field, componentRef.instance.defaultOptions);
        }
        if (!field.wrappers && type.wrappers) {
            field.wrappers = [...type.wrappers];
        }
    }
    /**
     * \@internal
     * @param {?=} field
     * @return {?}
     */
    resolveFieldTypeRef(field = {}) {
        if (!field.type) {
            return null;
        }
        /** @type {?} */
        const type = this.getType(field.type);
        if (!type.component || type['_componentRef']) {
            return type['_componentRef'];
        }
        const { _resolver, _injector } = field.parent.options;
        defineHiddenProp(type, '_componentRef', _resolver.resolveComponentFactory(type.component).create(_injector));
        return type['_componentRef'];
    }
    /**
     * @param {?} options
     * @return {?}
     */
    setWrapper(options) {
        this.wrappers[options.name] = options;
        if (options.types) {
            options.types.forEach((/**
             * @param {?} type
             * @return {?}
             */
            (type) => {
                this.setTypeWrapper(type, options.name);
            }));
        }
    }
    /**
     * @param {?} name
     * @return {?}
     */
    getWrapper(name) {
        if (!this.wrappers[name]) {
            throw new Error(`[Formly Error] The wrapper "${name}" could not be found. Please make sure that is registered through the FormlyModule declaration.`);
        }
        return this.wrappers[name];
    }
    /**
     * @param {?} type
     * @param {?} name
     * @return {?}
     */
    setTypeWrapper(type, name) {
        if (!this.types[type]) {
            this.types[type] = (/** @type {?} */ ({}));
        }
        if (!this.types[type].wrappers) {
            this.types[type].wrappers = [];
        }
        if (this.types[type].wrappers.indexOf(name) === -1) {
            this.types[type].wrappers.push(name);
        }
    }
    /**
     * @param {?} options
     * @return {?}
     */
    setValidator(options) {
        this.validators[options.name] = options;
    }
    /**
     * @param {?} name
     * @return {?}
     */
    getValidator(name) {
        if (!this.validators[name]) {
            throw new Error(`[Formly Error] The validator "${name}" could not be found. Please make sure that is registered through the FormlyModule declaration.`);
        }
        return this.validators[name];
    }
    /**
     * @param {?} name
     * @param {?} message
     * @return {?}
     */
    addValidatorMessage(name, message) {
        this.messages[name] = message;
    }
    /**
     * @param {?} name
     * @return {?}
     */
    getValidatorMessage(name) {
        return this.messages[name];
    }
    /**
     * @param {?} manipulator
     * @return {?}
     */
    setManipulator(manipulator) {
        new manipulator.class()[manipulator.method](this);
    }
    /**
     * @private
     * @param {?} name
     * @return {?}
     */
    mergeExtendedType(name) {
        if (!this.types[name].extends) {
            return;
        }
        /** @type {?} */
        const extendedType = this.getType(this.types[name].extends);
        if (!this.types[name].component) {
            this.types[name].component = extendedType.component;
        }
        if (!this.types[name].wrappers) {
            this.types[name].wrappers = extendedType.wrappers;
        }
    }
}
FormlyConfig.decorators = [
    { type: Injectable, args: [{ providedIn: 'root' },] }
];
/** @nocollapse */ FormlyConfig.ngInjectableDef = i0.defineInjectable({ factory: function FormlyConfig_Factory() { return new FormlyConfig(); }, token: FormlyConfig, providedIn: "root" });
if (false) {
    /** @type {?} */
    FormlyConfig.prototype.types;
    /** @type {?} */
    FormlyConfig.prototype.validators;
    /** @type {?} */
    FormlyConfig.prototype.wrappers;
    /** @type {?} */
    FormlyConfig.prototype.messages;
    /** @type {?} */
    FormlyConfig.prototype.templateManipulators;
    /** @type {?} */
    FormlyConfig.prototype.extras;
    /** @type {?} */
    FormlyConfig.prototype.extensions;
}
/**
 * @record
 */
export function TypeOption() { }
if (false) {
    /** @type {?} */
    TypeOption.prototype.name;
    /** @type {?|undefined} */
    TypeOption.prototype.component;
    /** @type {?|undefined} */
    TypeOption.prototype.wrappers;
    /** @type {?|undefined} */
    TypeOption.prototype.extends;
    /** @type {?|undefined} */
    TypeOption.prototype.defaultOptions;
}
/**
 * @record
 */
export function WrapperOption() { }
if (false) {
    /** @type {?} */
    WrapperOption.prototype.name;
    /** @type {?} */
    WrapperOption.prototype.component;
    /** @type {?|undefined} */
    WrapperOption.prototype.types;
}
/**
 * @record
 */
export function FieldValidatorFn() { }
/**
 * @record
 */
export function ValidatorOption() { }
if (false) {
    /** @type {?} */
    ValidatorOption.prototype.name;
    /** @type {?} */
    ValidatorOption.prototype.validation;
    /** @type {?|undefined} */
    ValidatorOption.prototype.options;
}
/**
 * @record
 */
export function ExtensionOption() { }
if (false) {
    /** @type {?} */
    ExtensionOption.prototype.name;
    /** @type {?} */
    ExtensionOption.prototype.extension;
}
/**
 * @record
 */
export function ValidationMessageOption() { }
if (false) {
    /** @type {?} */
    ValidationMessageOption.prototype.name;
    /** @type {?} */
    ValidationMessageOption.prototype.message;
}
/**
 * @record
 */
export function ManipulatorOption() { }
if (false) {
    /** @type {?|undefined} */
    ManipulatorOption.prototype.class;
    /** @type {?|undefined} */
    ManipulatorOption.prototype.method;
}
/**
 * @record
 */
export function ManipulatorWrapper() { }
/**
 * @record
 */
export function TemplateManipulators() { }
if (false) {
    /** @type {?|undefined} */
    TemplateManipulators.prototype.preWrapper;
    /** @type {?|undefined} */
    TemplateManipulators.prototype.postWrapper;
}
/**
 * @record
 */
export function ConfigOption() { }
if (false) {
    /** @type {?|undefined} */
    ConfigOption.prototype.types;
    /** @type {?|undefined} */
    ConfigOption.prototype.wrappers;
    /** @type {?|undefined} */
    ConfigOption.prototype.validators;
    /** @type {?|undefined} */
    ConfigOption.prototype.extensions;
    /** @type {?|undefined} */
    ConfigOption.prototype.validationMessages;
    /**
     * @deprecated use `extensions` instead
     * @type {?|undefined}
     */
    ConfigOption.prototype.manipulators;
    /** @type {?|undefined} */
    ConfigOption.prototype.extras;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmNvbmZpZy5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvc2VydmljZXMvZm9ybWx5LmNvbmZpZy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFVBQVUsRUFBRSxjQUFjLEVBQWdCLE1BQU0sZUFBZSxDQUFDO0FBR3pFLE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLFlBQVksQ0FBQzs7O0FBSWhFLE1BQU0sT0FBTyxhQUFhLEdBQUcsSUFBSSxjQUFjLENBQWUsZUFBZSxDQUFDOzs7OztBQUc5RSxxQ0FJQzs7Ozs7O0lBSEMsNkRBQTZDOzs7OztJQUM3Qyw0REFBNEM7Ozs7O0lBQzVDLDhEQUE4Qzs7Ozs7QUFPaEQsTUFBTSxPQUFPLFlBQVk7SUFEekI7UUFFRSxVQUFLLEdBQWlDLEVBQUUsQ0FBQztRQUN6QyxlQUFVLEdBQXdDLEVBQUUsQ0FBQztRQUNyRCxhQUFRLEdBQXNDLEVBQUUsQ0FBQztRQUNqRCxhQUFRLEdBQTJELEVBQUUsQ0FBQztRQUN0RSx5QkFBb0IsR0FHaEI7WUFDRixVQUFVLEVBQUUsRUFBRTtZQUNkLFdBQVcsRUFBRSxFQUFFO1NBQ2hCLENBQUM7UUFDRixXQUFNLEdBQTJCO1lBQy9CLGlCQUFpQixFQUFFLHNCQUFzQjtZQUN6QyxTQUFTOzs7O1lBQUUsVUFBUyxLQUFnQjtnQkFDbEMsT0FBTyxLQUFLLENBQUMsV0FBVyxJQUFJLEtBQUssQ0FBQyxXQUFXLENBQUMsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVcsQ0FBQyxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsT0FBTyxDQUFDLFVBQVUsSUFBSSxLQUFLLENBQUMsT0FBTyxDQUFDLFVBQVUsQ0FBQyxTQUFTLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxLQUFLLENBQUMsS0FBSyxDQUFDLFVBQVUsSUFBSSxLQUFLLENBQUMsS0FBSyxDQUFDLFVBQVUs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SUFBSSxDQUFDLENBQUMsUUFBUSxFQUFFO1lBQzlCLElBQUksQ0FBQyxLQUFLLENBQUMsSUFBSSxDQUFDLENBQUMsUUFBUSxHQUFHLFlBQVksQ0FBQyxRQUFRLENBQUM7U0FDbkQ7SUFDSCxDQUFDOzs7WUFqTUYsVUFBVSxTQUFDLEVBQUUsVUFBVSxFQUFFLE1BQU0sRUFBRTs7Ozs7SUFFaEMsNkJBQXlDOztJQUN6QyxrQ0FBcUQ7O0lBQ3JELGdDQUFpRDs7SUFDakQsZ0NBQXNFOztJQUN0RSw0Q0FNRTs7SUFDRiw4QkFLRTs7SUFDRixrQ0FBcUQ7Ozs7O0FBZ0x2RCxnQ0FNQzs7O0lBTEMsMEJBQWE7O0lBQ2IsK0JBQWdCOztJQUNoQiw4QkFBb0I7O0lBQ3BCLDZCQUFpQjs7SUFDakIsb0NBQW1DOzs7OztBQUdyQyxtQ0FJQzs7O0lBSEMsNkJBQWE7O0lBQ2Isa0NBQWU7O0lBQ2YsOEJBQWlCOzs7OztBQUduQixzQ0FFQzs7OztBQUVELHFDQUlDOzs7SUFIQywrQkFBYTs7SUFDYixxQ0FBNkI7O0lBQzdCLGtDQUFnQzs7Ozs7QUFHbEMscUNBR0M7OztJQUZDLCtCQUFhOztJQUNiLG9DQUEyQjs7Ozs7QUFHN0IsNkNBR0M7OztJQUZDLHVDQUFhOztJQUNiLDBDQUEwRjs7Ozs7QUFHNUYsdUNBR0M7OztJQUZDLGtDQUF3Qjs7SUFDeEIsbUNBQWdCOzs7OztBQUdsQix3Q0FFQzs7OztBQUVELDBDQUdDOzs7SUFGQywwQ0FBa0M7O0lBQ2xDLDJDQUFtQzs7Ozs7QUFHckMsa0NBc0JDOzs7SUFyQkMsNkJBQXFCOztJQUNyQixnQ0FBMkI7O0lBQzNCLGtDQUErQjs7SUFDL0Isa0NBQStCOztJQUMvQiwwQ0FBK0M7Ozs7O0lBRy9DLG9DQUFtQzs7SUFDbkMsOEJBWUUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbmplY3RhYmxlLCBJbmplY3Rpb25Ub2tlbiwgQ29tcG9uZW50UmVmIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBWYWxpZGF0aW9uRXJyb3JzLCBBYnN0cmFjdENvbnRyb2wgfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICcuLy4uL3RlbXBsYXRlcy9maWVsZC50eXBlJztcbmltcG9ydCB7IHJldmVyc2VEZWVwTWVyZ2UsIGRlZmluZUhpZGRlblByb3AgfSBmcm9tICcuLy4uL3V0aWxzJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnLCBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlIH0gZnJvbSAnLi4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IE9ic2VydmFibGUgfSBmcm9tICdyeGpzJztcblxuZXhwb3J0IGNvbnN0IEZPUk1MWV9DT05GSUcgPSBuZXcgSW5qZWN0aW9uVG9rZW48Rm9ybWx5Q29uZmlnPignRk9STUxZX0NPTkZJRycpO1xuXG4vKiogQGV4cGVyaW1lbnRhbCAqL1xuZXhwb3J0IGludGVyZmFjZSBGb3JtbHlFeHRlbnNpb24ge1xuICBwcmVQb3B1bGF0ZT8oZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKTogdm9pZDtcbiAgb25Qb3B1bGF0ZT8oZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKTogdm9pZDtcbiAgcG9zdFBvcHVsYXRlPyhmaWVsZDogRm9ybWx5RmllbGRDb25maWcpOiB2b2lkO1xufVxuXG4vKipcbiAqIE1haW50YWlucyBsaXN0IG9mIGZvcm1seSBmaWVsZCBkaXJlY3RpdmUgdHlwZXMuIFRoaXMgY2FuIGJlIHVzZWQgdG8gcmVnaXN0ZXIgbmV3IGZpZWxkIHRlbXBsYXRlcy5cbiAqL1xuQEluamVjdGFibGUoeyBwcm92aWRlZEluOiAncm9vdCcgfSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlDb25maWcge1xuICB0eXBlczoge1tuYW1lOiBzdHJpbmddOiBUeXBlT3B0aW9ufSA9IHt9O1xuICB2YWxpZGF0b3JzOiB7IFtuYW1lOiBzdHJpbmddOiBWYWxpZGF0b3JPcHRpb24gfSA9IHt9O1xuICB3cmFwcGVyczogeyBbbmFtZTogc3RyaW5nXTogV3JhcHBlck9wdGlvbiB9ID0ge307XG4gIG1lc3NhZ2VzOiB7IFtuYW1lOiBzdHJpbmddOiBWYWxpZGF0aW9uTWVzc2FnZU9wdGlvblsnbWVzc2FnZSddIH0gPSB7fTtcbiAgdGVtcGxhdGVNYW5pcHVsYXRvcnM6IHtcbiAgICBwcmVXcmFwcGVyOiBNYW5pcHVsYXRvcldyYXBwZXJbXTtcbiAgICBwb3N0V3JhcHBlcjogTWFuaXB1bGF0b3JXcmFwcGVyW107XG4gIH0gPSB7XG4gICAgcHJlV3JhcHBlcjogW10sXG4gICAgcG9zdFdyYXBwZXI6IFtdLFxuICB9O1xuICBleHRyYXM6IENvbmZpZ09wdGlvblsnZXh0cmFzJ10gPSB7XG4gICAgY2hlY2tFeHByZXNzaW9uT246ICdjaGFuZ2VEZXRlY3Rpb25DaGVjaycsXG4gICAgc2hvd0Vycm9yOiBmdW5jdGlvbihmaWVsZDogRmllbGRUeXBlKSB7XG4gICAgICByZXR1cm4gZmllbGQuZm9ybUNvbnRyb2wgJiYgZmllbGQuZm9ybUNvbnRyb2wuaW52YWxpZCAmJiAoZmllbGQuZm9ybUNvbnRyb2wudG91Y2hlZCB8fCAoZmllbGQub3B0aW9ucy5wYXJlbnRGb3JtICYmIGZpZWxkLm9wdGlvbnMucGFyZW50Rm9ybS5zdWJtaXR0ZWQpIHx8ICEhKGZpZWxkLmZpZWxkLnZhbGlkYXRpb24gJiYgZmllbGQuZmllbGQudmFsaWRhdGlvbi5zaG93KSk7XG4gICAgfSxcbiAgfTtcbiAgZXh0ZW5zaW9uczogeyBbbmFtZTogc3RyaW5nXTogRm9ybWx5RXh0ZW5zaW9uIH0gPSB7fTtcblxuICBhZGRDb25maWcoY29uZmlnOiBDb25maWdPcHRpb24pIHtcbiAgICBpZiAoY29uZmlnLnR5cGVzKSB7XG4gICAgICBjb25maWcudHlwZXMuZm9yRWFjaCh0eXBlID0+IHRoaXMuc2V0VHlwZSh0eXBlKSk7XG4gICAgfVxuICAgIGlmIChjb25maWcudmFsaWRhdG9ycykge1xuICAgICAgY29uZmlnLnZhbGlkYXRvcnMuZm9yRWFjaCh2YWxpZGF0b3IgPT4gdGhpcy5zZXRWYWxpZGF0b3IodmFsaWRhdG9yKSk7XG4gICAgfVxuICAgIGlmIChjb25maWcud3JhcHBlcnMpIHtcbiAgICAgIGNvbmZpZy53cmFwcGVycy5mb3JFYWNoKHdyYXBwZXIgPT4gdGhpcy5zZXRXcmFwcGVyKHdyYXBwZXIpKTtcbiAgICB9XG4gICAgaWYgKGNvbmZpZy5tYW5pcHVsYXRvcnMpIHtcbiAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBwYXNzaW5nICdtYW5pcHVsYXRvcnMnIGNvbmZpZyBpcyBkZXByZWNhdGVkLCB1c2UgY3VzdG9tIGV4dGVuc2lvbiBpbnN0ZWFkLmApO1xuICAgICAgY29uZmlnLm1hbmlwdWxhdG9ycy5mb3JFYWNoKG1hbmlwdWxhdG9yID0+IHRoaXMuc2V0TWFuaXB1bGF0b3IobWFuaXB1bGF0b3IpKTtcbiAgICB9XG4gICAgaWYgKGNvbmZpZy52YWxpZGF0aW9uTWVzc2FnZXMpIHtcbiAgICAgIGNvbmZpZy52YWxpZGF0aW9uTWVzc2FnZXMuZm9yRWFjaCh2YWxpZGF0aW9uID0+IHRoaXMuYWRkVmFsaWRhdG9yTWVzc2FnZSh2YWxpZGF0aW9uLm5hbWUsIHZhbGlkYXRpb24ubWVzc2FnZSkpO1xuICAgIH1cbiAgICBpZiAoY29uZmlnLmV4dGVuc2lvbnMpIHtcbiAgICAgIGNvbmZpZy5leHRlbnNpb25zLmZvckVhY2goYyA9PiB0aGlzLmV4dGVuc2lvbnNbYy5uYW1lXSA9IGMuZXh0ZW5zaW9uKTtcbiAgICB9XG4gICAgaWYgKGNvbmZpZy5leHRyYXMpIHtcbiAgICAgIHRoaXMuZXh0cmFzID0geyAuLi50aGlzLmV4dHJhcywgLi4uY29uZmlnLmV4dHJhcyB9O1xuICAgIH1cbiAgfVxuXG4gIHNldFR5cGUob3B0aW9uczogVHlwZU9wdGlvbiB8IFR5cGVPcHRpb25bXSkge1xuICAgIGlmIChBcnJheS5pc0FycmF5KG9wdGlvbnMpKSB7XG4gICAgICBvcHRpb25zLmZvckVhY2goKG9wdGlvbikgPT4gdGhpcy5zZXRUeXBlKG9wdGlvbikpO1xuICAgIH0gZWxzZSB7XG4gICAgICBpZiAoIXRoaXMudHlwZXNbb3B0aW9ucy5uYW1lXSkge1xuICAgICAgICB0aGlzLnR5cGVzW29wdGlvbnMubmFtZV0gPSA8VHlwZU9wdGlvbj57IG5hbWU6IG9wdGlvbnMubmFtZSB9O1xuICAgICAgfVxuXG4gICAgICBbJ2NvbXBvbmVudCcsICdleHRlbmRzJywgJ2RlZmF1bHRPcHRpb25zJ10uZm9yRWFjaChwcm9wID0+IHtcbiAgICAgICAgaWYgKG9wdGlvbnMuaGFzT3duUHJvcGVydHkocHJvcCkpIHtcbiAgICAgICAgICB0aGlzLnR5cGVzW29wdGlvbnMubmFtZV1bcHJvcF0gPSBvcHRpb25zW3Byb3BdO1xuICAgICAgICB9XG4gICAgICB9KTtcblxuICAgICAgaWYgKG9wdGlvbnMud3JhcHBlcnMpIHtcbiAgICAgICAgb3B0aW9ucy53cmFwcGVycy5mb3JFYWNoKCh3cmFwcGVyKSA9PiB0aGlzLnNldFR5cGVXcmFwcGVyKG9wdGlvbnMubmFtZSwgd3JhcHBlcikpO1xuICAgICAgfVxuICAgIH1cbiAgfVxuXG4gIGdldFR5cGUobmFtZTogc3RyaW5nKTogVHlwZU9wdGlvbiB7XG4gICAgaWYgKCF0aGlzLnR5cGVzW25hbWVdKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYFtGb3JtbHkgRXJyb3JdIFRoZSB0eXBlIFwiJHtuYW1lfVwiIGNvdWxkIG5vdCBiZSBmb3VuZC4gUGxlYXNlIG1ha2Ugc3VyZSB0aGF0IGlzIHJlZ2lzdGVyZWQgdGhyb3VnaCB0aGUgRm9ybWx5TW9kdWxlIGRlY2xhcmF0aW9uLmApO1xuICAgIH1cblxuICAgIHRoaXMubWVyZ2VFeHRlbmRlZFR5cGUobmFtZSk7XG5cbiAgICByZXR1cm4gdGhpcy50eXBlc1tuYW1lXTtcbiAgfVxuXG4gIGdldE1lcmdlZEZpZWxkKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZyA9IHt9KTogYW55IHtcbiAgICBjb25zdCB0eXBlID0gdGhpcy5nZXRUeXBlKGZpZWxkLnR5cGUpO1xuICAgIGlmICh0eXBlLmRlZmF1bHRPcHRpb25zKSB7XG4gICAgICByZXZlcnNlRGVlcE1lcmdlKGZpZWxkLCB0eXBlLmRlZmF1bHRPcHRpb25zKTtcbiAgICB9XG5cbiAgICBjb25zdCBleHRlbmREZWZhdWx0cyA9IHR5cGUuZXh0ZW5kcyAmJiB0aGlzLmdldFR5cGUodHlwZS5leHRlbmRzKS5kZWZhdWx0T3B0aW9ucztcbiAgICBpZiAoZXh0ZW5kRGVmYXVsdHMpIHtcbiAgICAgIHJldmVyc2VEZWVwTWVyZ2UoZmllbGQsIGV4dGVuZERlZmF1bHRzKTtcbiAgICB9XG5cbiAgICBpZiAoZmllbGQgJiYgZmllbGQub3B0aW9uc1R5cGVzKSB7XG4gICAgICBmaWVsZC5vcHRpb25zVHlwZXMuZm9yRWFjaChvcHRpb24gPT4ge1xuICAgICAgICBjb25zdCBkZWZhdWx0T3B0aW9ucyA9IHRoaXMuZ2V0VHlwZShvcHRpb24pLmRlZmF1bHRPcHRpb25zO1xuICAgICAgICBpZiAoZGVmYXVsdE9wdGlvbnMpIHtcbiAgICAgICAgICByZXZlcnNlRGVlcE1lcmdlKGZpZWxkLCBkZWZhdWx0T3B0aW9ucyk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH1cblxuICAgIGNvbnN0IGNvbXBvbmVudFJlZiA9IHRoaXMucmVzb2x2ZUZpZWxkVHlwZVJlZihmaWVsZCk7XG4gICAgaWYgKGNvbXBvbmVudFJlZiAmJiBjb21wb25lbnRSZWYuaW5zdGFuY2UgJiYgY29tcG9uZW50UmVmLmluc3RhbmNlLmRlZmF1bHRPcHRpb25zKSB7XG4gICAgICByZXZlcnNlRGVlcE1lcmdlKGZpZWxkLCBjb21wb25lbnRSZWYuaW5zdGFuY2UuZGVmYXVsdE9wdGlvbnMpO1xuICAgIH1cblxuICAgIGlmICghZmllbGQud3JhcHBlcnMgJiYgdHlwZS53cmFwcGVycykge1xuICAgICAgZmllbGQud3JhcHBlcnMgPSBbLi4udHlwZS53cmFwcGVyc107XG4gICAgfVxuICB9XG5cbiAgLyoqIEBpbnRlcm5hbCAqL1xuICByZXNvbHZlRmllbGRUeXBlUmVmKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlID0ge30pOiBDb21wb25lbnRSZWY8RmllbGRUeXBlPiB7XG4gICAgaWYgKCFmaWVsZC50eXBlKSB7XG4gICAgICByZXR1cm4gbnVsbDtcbiAgICB9XG5cbiAgICBjb25zdCB0eXBlID0gdGhpcy5nZXRUeXBlKGZpZWxkLnR5cGUpO1xuICAgIGlmICghdHlwZS5jb21wb25lbnQgfHwgdHlwZVsnX2NvbXBvbmVudFJlZiddKSB7XG4gICAgICByZXR1cm4gdHlwZVsnX2NvbXBvbmVudFJlZiddO1xuICAgIH1cblxuICAgIGNvbnN0IHsgX3Jlc29sdmVyLCBfaW5qZWN0b3IgfSA9IGZpZWxkLnBhcmVudC5vcHRpb25zO1xuICAgIGRlZmluZUhpZGRlblByb3AoXG4gICAgICB0eXBlLFxuICAgICAgJ19jb21wb25lbnRSZWYnLFxuICAgICAgX3Jlc29sdmVyLnJlc29sdmVDb21wb25lbnRGYWN0b3J5PEZpZWxkVHlwZT4odHlwZS5jb21wb25lbnQpLmNyZWF0ZShfaW5qZWN0b3IpLFxuICAgICk7XG5cbiAgICByZXR1cm4gdHlwZVsnX2NvbXBvbmVudFJlZiddO1xuICB9XG5cbiAgc2V0V3JhcHBlcihvcHRpb25zOiBXcmFwcGVyT3B0aW9uKSB7XG4gICAgdGhpcy53cmFwcGVyc1tvcHRpb25zLm5hbWVdID0gb3B0aW9ucztcbiAgICBpZiAob3B0aW9ucy50eXBlcykge1xuICAgICAgb3B0aW9ucy50eXBlcy5mb3JFYWNoKCh0eXBlKSA9PiB7XG4gICAgICAgIHRoaXMuc2V0VHlwZVdyYXBwZXIodHlwZSwgb3B0aW9ucy5uYW1lKTtcbiAgICAgIH0pO1xuICAgIH1cbiAgfVxuXG4gIGdldFdyYXBwZXIobmFtZTogc3RyaW5nKTogV3JhcHBlck9wdGlvbiB7XG4gICAgaWYgKCF0aGlzLndyYXBwZXJzW25hbWVdKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYFtGb3JtbHkgRXJyb3JdIFRoZSB3cmFwcGVyIFwiJHtuYW1lfVwiIGNvdWxkIG5vdCBiZSBmb3VuZC4gUGxlYXNlIG1ha2Ugc3VyZSB0aGF0IGlzIHJlZ2lzdGVyZWQgdGhyb3VnaCB0aGUgRm9ybWx5TW9kdWxlIGRlY2xhcmF0aW9uLmApO1xuICAgIH1cblxuICAgIHJldHVybiB0aGlzLndyYXBwZXJzW25hbWVdO1xuICB9XG5cbiAgc2V0VHlwZVdyYXBwZXIodHlwZTogc3RyaW5nLCBuYW1lOiBzdHJpbmcpIHtcbiAgICBpZiAoIXRoaXMudHlwZXNbdHlwZV0pIHtcbiAgICAgIHRoaXMudHlwZXNbdHlwZV0gPSA8VHlwZU9wdGlvbj57fTtcbiAgICB9XG4gICAgaWYgKCF0aGlzLnR5cGVzW3R5cGVdLndyYXBwZXJzKSB7XG4gICAgICB0aGlzLnR5cGVzW3R5cGVdLndyYXBwZXJzID0gW107XG4gICAgfVxuICAgIGlmICh0aGlzLnR5cGVzW3R5cGVdLndyYXBwZXJzLmluZGV4T2YobmFtZSkgPT09IC0xKSB7XG4gICAgICB0aGlzLnR5cGVzW3R5cGVdLndyYXBwZXJzLnB1c2gobmFtZSk7XG4gICAgfVxuICB9XG5cbiAgc2V0VmFsaWRhdG9yKG9wdGlvbnM6IFZhbGlkYXRvck9wdGlvbikge1xuICAgIHRoaXMudmFsaWRhdG9yc1tvcHRpb25zLm5hbWVdID0gb3B0aW9ucztcbiAgfVxuXG4gIGdldFZhbGlkYXRvcihuYW1lOiBzdHJpbmcpOiBWYWxpZGF0b3JPcHRpb24ge1xuICAgIGlmICghdGhpcy52YWxpZGF0b3JzW25hbWVdKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYFtGb3JtbHkgRXJyb3JdIFRoZSB2YWxpZGF0b3IgXCIke25hbWV9XCIgY291bGQgbm90IGJlIGZvdW5kLiBQbGVhc2UgbWFrZSBzdXJlIHRoYXQgaXMgcmVnaXN0ZXJlZCB0aHJvdWdoIHRoZSBGb3JtbHlNb2R1bGUgZGVjbGFyYXRpb24uYCk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMudmFsaWRhdG9yc1tuYW1lXTtcbiAgfVxuXG4gIGFkZFZhbGlkYXRvck1lc3NhZ2UobmFtZTogc3RyaW5nLCBtZXNzYWdlOiBWYWxpZGF0aW9uTWVzc2FnZU9wdGlvblsnbWVzc2FnZSddKSB7XG4gICAgdGhpcy5tZXNzYWdlc1tuYW1lXSA9IG1lc3NhZ2U7XG4gIH1cblxuICBnZXRWYWxpZGF0b3JNZXNzYWdlKG5hbWU6IHN0cmluZykge1xuICAgIHJldHVybiB0aGlzLm1lc3NhZ2VzW25hbWVdO1xuICB9XG5cbiAgc2V0TWFuaXB1bGF0b3IobWFuaXB1bGF0b3I6IE1hbmlwdWxhdG9yT3B0aW9uKSB7XG4gICAgbmV3IG1hbmlwdWxhdG9yLmNsYXNzKClbbWFuaXB1bGF0b3IubWV0aG9kXSh0aGlzKTtcbiAgfVxuXG4gIHByaXZhdGUgbWVyZ2VFeHRlbmRlZFR5cGUobmFtZTogc3RyaW5nKSB7XG4gICAgaWYgKCF0aGlzLnR5cGVzW25hbWVdLmV4dGVuZHMpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBjb25zdCBleHRlbmRlZFR5cGUgPSB0aGlzLmdldFR5cGUodGhpcy50eXBlc1tuYW1lXS5leHRlbmRzKTtcbiAgICBpZiAoIXRoaXMudHlwZXNbbmFtZV0uY29tcG9uZW50KSB7XG4gICAgICB0aGlzLnR5cGVzW25hbWVdLmNvbXBvbmVudCA9IGV4dGVuZGVkVHlwZS5jb21wb25lbnQ7XG4gICAgfVxuXG4gICAgaWYgKCF0aGlzLnR5cGVzW25hbWVdLndyYXBwZXJzKSB7XG4gICAgICB0aGlzLnR5cGVzW25hbWVdLndyYXBwZXJzID0gZXh0ZW5kZWRUeXBlLndyYXBwZXJzO1xuICAgIH1cbiAgfVxufVxuZXhwb3J0IGludGVyZmFjZSBUeXBlT3B0aW9uIHtcbiAgbmFtZTogc3RyaW5nO1xuICBjb21wb25lbnQ/OiBhbnk7XG4gIHdyYXBwZXJzPzogc3RyaW5nW107XG4gIGV4dGVuZHM/OiBzdHJpbmc7XG4gIGRlZmF1bHRPcHRpb25zPzogRm9ybWx5RmllbGRDb25maWc7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgV3JhcHBlck9wdGlvbiB7XG4gIG5hbWU6IHN0cmluZztcbiAgY29tcG9uZW50OiBhbnk7XG4gIHR5cGVzPzogc3RyaW5nW107XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRmllbGRWYWxpZGF0b3JGbiB7XG4gIChjOiBBYnN0cmFjdENvbnRyb2wsIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZywgb3B0aW9ucz86IHsgW2lkOiBzdHJpbmddOiBhbnk7IH0pOiBWYWxpZGF0aW9uRXJyb3JzIHwgbnVsbDtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBWYWxpZGF0b3JPcHRpb24ge1xuICBuYW1lOiBzdHJpbmc7XG4gIHZhbGlkYXRpb246IEZpZWxkVmFsaWRhdG9yRm47XG4gIG9wdGlvbnM/OiB7IFtpZDogc3RyaW5nXTogYW55IH07XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRXh0ZW5zaW9uT3B0aW9uIHtcbiAgbmFtZTogc3RyaW5nO1xuICBleHRlbnNpb246IEZvcm1seUV4dGVuc2lvbjtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBWYWxpZGF0aW9uTWVzc2FnZU9wdGlvbiB7XG4gIG5hbWU6IHN0cmluZztcbiAgbWVzc2FnZTogc3RyaW5nIHwgKChlcnJvcjogYW55LCBmaWVsZDogRm9ybWx5RmllbGRDb25maWcpID0+IHN0cmluZyB8IE9ic2VydmFibGU8c3RyaW5nPik7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgTWFuaXB1bGF0b3JPcHRpb24ge1xuICBjbGFzcz86IHsgbmV3ICgpOiBhbnkgfTtcbiAgbWV0aG9kPzogc3RyaW5nO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIE1hbmlwdWxhdG9yV3JhcHBlciB7XG4gIChmOiBGb3JtbHlGaWVsZENvbmZpZyk6IHN0cmluZztcbn1cblxuZXhwb3J0IGludGVyZmFjZSBUZW1wbGF0ZU1hbmlwdWxhdG9ycyB7XG4gIHByZVdyYXBwZXI/OiBNYW5pcHVsYXRvcldyYXBwZXJbXTtcbiAgcG9zdFdyYXBwZXI/OiBNYW5pcHVsYXRvcldyYXBwZXJbXTtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBDb25maWdPcHRpb24ge1xuICB0eXBlcz86IFR5cGVPcHRpb25bXTtcbiAgd3JhcHBlcnM/OiBXcmFwcGVyT3B0aW9uW107XG4gIHZhbGlkYXRvcnM/OiBWYWxpZGF0b3JPcHRpb25bXTtcbiAgZXh0ZW5zaW9ucz86IEV4dGVuc2lvbk9wdGlvbltdO1xuICB2YWxpZGF0aW9uTWVzc2FnZXM/OiBWYWxpZGF0aW9uTWVzc2FnZU9wdGlvbltdO1xuXG4gIC8qKiBAZGVwcmVjYXRlZCB1c2UgYGV4dGVuc2lvbnNgIGluc3RlYWQgKi9cbiAgbWFuaXB1bGF0b3JzPzogTWFuaXB1bGF0b3JPcHRpb25bXTtcbiAgZXh0cmFzPzoge1xuICAgIC8qKiBAZGVwcmVjYXRlZCB1c2UgYGV4dGVuc2lvbnNgIGluc3RlYWQgKi9cbiAgICBmaWVsZFRyYW5zZm9ybT86IGFueSxcbiAgICBpbW11dGFibGU/OiBib29sZWFuLFxuICAgIHNob3dFcnJvcj86IChmaWVsZDogRmllbGRUeXBlKSA9PiBib29sZWFuO1xuXG4gICAgLyoqXG4gICAgICogRGVmaW5lcyB0aGUgb3B0aW9uIHdoaWNoIGZvcm1seSByZWx5IG9uIHRvIGNoZWNrIGZpZWxkIGV4cHJlc3Npb24gcHJvcGVydGllcy5cbiAgICAgKiAtIGBtb2RlbENoYW5nZWA6IHBlcmZvcm0gYSBjaGVjayB3aGVuIHRoZSB2YWx1ZSBvZiB0aGUgZm9ybSBjb250cm9sIGNoYW5nZXMuXG4gICAgICogLSBgY2hhbmdlRGV0ZWN0aW9uQ2hlY2tgOiB0cmlnZ2VycyBhbiBpbW1lZGlhdGUgY2hlY2sgd2hlbiBgbmdEb0NoZWNrYCBpcyBjYWxsZWQuXG4gICAgKi9cbiAgICBjaGVja0V4cHJlc3Npb25Pbj86ICdtb2RlbENoYW5nZScgfCAnY2hhbmdlRGV0ZWN0aW9uQ2hlY2snLFxuICB9O1xufVxuIl19