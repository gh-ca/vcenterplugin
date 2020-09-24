/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Injectable, InjectionToken } from '@angular/core';
import { reverseDeepMerge, defineHiddenProp } from './../utils';
import * as i0 from "@angular/core";
/** @type {?} */
export var FORMLY_CONFIG = new InjectionToken('FORMLY_CONFIG');
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
var FormlyConfig = /** @class */ (function () {
    function FormlyConfig() {
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
    FormlyConfig.prototype.addConfig = /**
     * @param {?} config
     * @return {?}
     */
    function (config) {
        var _this = this;
        if (config.types) {
            config.types.forEach((/**
             * @param {?} type
             * @return {?}
             */
            function (type) { return _this.setType(type); }));
        }
        if (config.validators) {
            config.validators.forEach((/**
             * @param {?} validator
             * @return {?}
             */
            function (validator) { return _this.setValidator(validator); }));
        }
        if (config.wrappers) {
            config.wrappers.forEach((/**
             * @param {?} wrapper
             * @return {?}
             */
            function (wrapper) { return _this.setWrapper(wrapper); }));
        }
        if (config.manipulators) {
            console.warn("NgxFormly: passing 'manipulators' config is deprecated, use custom extension instead.");
            config.manipulators.forEach((/**
             * @param {?} manipulator
             * @return {?}
             */
            function (manipulator) { return _this.setManipulator(manipulator); }));
        }
        if (config.validationMessages) {
            config.validationMessages.forEach((/**
             * @param {?} validation
             * @return {?}
             */
            function (validation) { return _this.addValidatorMessage(validation.name, validation.message); }));
        }
        if (config.extensions) {
            config.extensions.forEach((/**
             * @param {?} c
             * @return {?}
             */
            function (c) { return _this.extensions[c.name] = c.extension; }));
        }
        if (config.extras) {
            this.extras = tslib_1.__assign({}, this.extras, config.extras);
        }
    };
    /**
     * @param {?} options
     * @return {?}
     */
    FormlyConfig.prototype.setType = /**
     * @param {?} options
     * @return {?}
     */
    function (options) {
        var _this = this;
        if (Array.isArray(options)) {
            options.forEach((/**
             * @param {?} option
             * @return {?}
             */
            function (option) { return _this.setType(option); }));
        }
        else {
            if (!this.types[options.name]) {
                this.types[options.name] = (/** @type {?} */ ({ name: options.name }));
            }
            ['component', 'extends', 'defaultOptions'].forEach((/**
             * @param {?} prop
             * @return {?}
             */
            function (prop) {
                if (options.hasOwnProperty(prop)) {
                    _this.types[options.name][prop] = options[prop];
                }
            }));
            if (options.wrappers) {
                options.wrappers.forEach((/**
                 * @param {?} wrapper
                 * @return {?}
                 */
                function (wrapper) { return _this.setTypeWrapper(options.name, wrapper); }));
            }
        }
    };
    /**
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.getType = /**
     * @param {?} name
     * @return {?}
     */
    function (name) {
        if (!this.types[name]) {
            throw new Error("[Formly Error] The type \"" + name + "\" could not be found. Please make sure that is registered through the FormlyModule declaration.");
        }
        this.mergeExtendedType(name);
        return this.types[name];
    };
    /**
     * @param {?=} field
     * @return {?}
     */
    FormlyConfig.prototype.getMergedField = /**
     * @param {?=} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        if (field === void 0) { field = {}; }
        /** @type {?} */
        var type = this.getType(field.type);
        if (type.defaultOptions) {
            reverseDeepMerge(field, type.defaultOptions);
        }
        /** @type {?} */
        var extendDefaults = type.extends && this.getType(type.extends).defaultOptions;
        if (extendDefaults) {
            reverseDeepMerge(field, extendDefaults);
        }
        if (field && field.optionsTypes) {
            field.optionsTypes.forEach((/**
             * @param {?} option
             * @return {?}
             */
            function (option) {
                /** @type {?} */
                var defaultOptions = _this.getType(option).defaultOptions;
                if (defaultOptions) {
                    reverseDeepMerge(field, defaultOptions);
                }
            }));
        }
        /** @type {?} */
        var componentRef = this.resolveFieldTypeRef(field);
        if (componentRef && componentRef.instance && componentRef.instance.defaultOptions) {
            reverseDeepMerge(field, componentRef.instance.defaultOptions);
        }
        if (!field.wrappers && type.wrappers) {
            field.wrappers = tslib_1.__spread(type.wrappers);
        }
    };
    /** @internal */
    /**
     * \@internal
     * @param {?=} field
     * @return {?}
     */
    FormlyConfig.prototype.resolveFieldTypeRef = /**
     * \@internal
     * @param {?=} field
     * @return {?}
     */
    function (field) {
        if (field === void 0) { field = {}; }
        if (!field.type) {
            return null;
        }
        /** @type {?} */
        var type = this.getType(field.type);
        if (!type.component || type['_componentRef']) {
            return type['_componentRef'];
        }
        var _a = field.parent.options, _resolver = _a._resolver, _injector = _a._injector;
        defineHiddenProp(type, '_componentRef', _resolver.resolveComponentFactory(type.component).create(_injector));
        return type['_componentRef'];
    };
    /**
     * @param {?} options
     * @return {?}
     */
    FormlyConfig.prototype.setWrapper = /**
     * @param {?} options
     * @return {?}
     */
    function (options) {
        var _this = this;
        this.wrappers[options.name] = options;
        if (options.types) {
            options.types.forEach((/**
             * @param {?} type
             * @return {?}
             */
            function (type) {
                _this.setTypeWrapper(type, options.name);
            }));
        }
    };
    /**
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.getWrapper = /**
     * @param {?} name
     * @return {?}
     */
    function (name) {
        if (!this.wrappers[name]) {
            throw new Error("[Formly Error] The wrapper \"" + name + "\" could not be found. Please make sure that is registered through the FormlyModule declaration.");
        }
        return this.wrappers[name];
    };
    /**
     * @param {?} type
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.setTypeWrapper = /**
     * @param {?} type
     * @param {?} name
     * @return {?}
     */
    function (type, name) {
        if (!this.types[type]) {
            this.types[type] = (/** @type {?} */ ({}));
        }
        if (!this.types[type].wrappers) {
            this.types[type].wrappers = [];
        }
        if (this.types[type].wrappers.indexOf(name) === -1) {
            this.types[type].wrappers.push(name);
        }
    };
    /**
     * @param {?} options
     * @return {?}
     */
    FormlyConfig.prototype.setValidator = /**
     * @param {?} options
     * @return {?}
     */
    function (options) {
        this.validators[options.name] = options;
    };
    /**
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.getValidator = /**
     * @param {?} name
     * @return {?}
     */
    function (name) {
        if (!this.validators[name]) {
            throw new Error("[Formly Error] The validator \"" + name + "\" could not be found. Please make sure that is registered through the FormlyModule declaration.");
        }
        return this.validators[name];
    };
    /**
     * @param {?} name
     * @param {?} message
     * @return {?}
     */
    FormlyConfig.prototype.addValidatorMessage = /**
     * @param {?} name
     * @param {?} message
     * @return {?}
     */
    function (name, message) {
        this.messages[name] = message;
    };
    /**
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.getValidatorMessage = /**
     * @param {?} name
     * @return {?}
     */
    function (name) {
        return this.messages[name];
    };
    /**
     * @param {?} manipulator
     * @return {?}
     */
    FormlyConfig.prototype.setManipulator = /**
     * @param {?} manipulator
     * @return {?}
     */
    function (manipulator) {
        new manipulator.class()[manipulator.method](this);
    };
    /**
     * @private
     * @param {?} name
     * @return {?}
     */
    FormlyConfig.prototype.mergeExtendedType = /**
     * @private
     * @param {?} name
     * @return {?}
     */
    function (name) {
        if (!this.types[name].extends) {
            return;
        }
        /** @type {?} */
        var extendedType = this.getType(this.types[name].extends);
        if (!this.types[name].component) {
            this.types[name].component = extendedType.component;
        }
        if (!this.types[name].wrappers) {
            this.types[name].wrappers = extendedType.wrappers;
        }
    };
    FormlyConfig.decorators = [
        { type: Injectable, args: [{ providedIn: 'root' },] }
    ];
    /** @nocollapse */ FormlyConfig.ngInjectableDef = i0.defineInjectable({ factory: function FormlyConfig_Factory() { return new FormlyConfig(); }, token: FormlyConfig, providedIn: "root" });
    return FormlyConfig;
}());
export { FormlyConfig };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmNvbmZpZy5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvc2VydmljZXMvZm9ybWx5LmNvbmZpZy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxVQUFVLEVBQUUsY0FBYyxFQUFnQixNQUFNLGVBQWUsQ0FBQztBQUd6RSxPQUFPLEVBQUUsZ0JBQWdCLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSxZQUFZLENBQUM7OztBQUloRSxNQUFNLEtBQU8sYUFBYSxHQUFHLElBQUksY0FBYyxDQUFlLGVBQWUsQ0FBQzs7Ozs7QUFHOUUscUNBSUM7Ozs7OztJQUhDLDZEQUE2Qzs7Ozs7SUFDN0MsNERBQTRDOzs7OztJQUM1Qyw4REFBOEM7Ozs7O0FBTWhEO0lBQUE7UUFFRSxVQUFLLEdBQWlDLEVBQUUsQ0FBQztRQUN6QyxlQUFVLEdBQXdDLEVBQUUsQ0FBQztRQUNyRCxhQUFRLEdBQXNDLEVBQUUsQ0FBQztRQUNqRCxhQUFRLEdBQTJELEVBQUUsQ0FBQztRQUN0RSx5QkFBb0IsR0FHaEI7WUFDRixVQUFVLEVBQUUsRUFBRTtZQUNkLFdBQVcsRUFBRSxFQUFFO1NBQ2hCLENBQUM7UUFDRixXQUFNLEdBQTJCO1lBQy9CLGlCQUFpQixFQUFFLHNCQUFzQjtZQUN6QyxTQUFTOzs7O1lBQUUsVUFBUyxLQUFnQjtnQkFDbEMsT0FBTyxLQUFLLENBQUMsV0FBVyxJQUFJLEtBQUssQ0FBQyxXQUFXLENBQUMsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVcsQ0FBQyxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsT0FBTyxDQUFDLFVBQVUsSUFBSSxLQUFLLENBQUMsT0FBTyxDQUFDLFVBQVUsQ0FBQyxTQUFTLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxLQUFLLENBQUMsS0FBSyxDQUFDLFVBQVUsSUFBSSxLQUFLLENBQUMsS0FBSyxDQUFDLFVBQVUsQ0FBQyxJQUFJLENBQUMs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O0FBRUQsMENBR0M7OztJQUZDLDBDQUFrQzs7SUFDbEMsMkNBQW1DOzs7OztBQUdyQyxrQ0FzQkM7OztJQXJCQyw2QkFBcUI7O0lBQ3JCLGdDQUEyQjs7SUFDM0Isa0NBQStCOztJQUMvQixrQ0FBK0I7O0lBQy9CLDBDQUErQzs7Ozs7SUFHL0Msb0NBQW1DOztJQUNuQyw4QkFZRSIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEluamVjdGFibGUsIEluamVjdGlvblRva2VuLCBDb21wb25lbnRSZWYgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IFZhbGlkYXRpb25FcnJvcnMsIEFic3RyYWN0Q29udHJvbCB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJy4vLi4vdGVtcGxhdGVzL2ZpZWxkLnR5cGUnO1xuaW1wb3J0IHsgcmV2ZXJzZURlZXBNZXJnZSwgZGVmaW5lSGlkZGVuUHJvcCB9IGZyb20gJy4vLi4vdXRpbHMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgT2JzZXJ2YWJsZSB9IGZyb20gJ3J4anMnO1xuXG5leHBvcnQgY29uc3QgRk9STUxZX0NPTkZJRyA9IG5ldyBJbmplY3Rpb25Ub2tlbjxGb3JtbHlDb25maWc+KCdGT1JNTFlfQ09ORklHJyk7XG5cbi8qKiBAZXhwZXJpbWVudGFsICovXG5leHBvcnQgaW50ZXJmYWNlIEZvcm1seUV4dGVuc2lvbiB7XG4gIHByZVBvcHVsYXRlPyhmaWVsZDogRm9ybWx5RmllbGRDb25maWcpOiB2b2lkO1xuICBvblBvcHVsYXRlPyhmaWVsZDogRm9ybWx5RmllbGRDb25maWcpOiB2b2lkO1xuICBwb3N0UG9wdWxhdGU/KGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZyk6IHZvaWQ7XG59XG5cbi8qKlxuICogTWFpbnRhaW5zIGxpc3Qgb2YgZm9ybWx5IGZpZWxkIGRpcmVjdGl2ZSB0eXBlcy4gVGhpcyBjYW4gYmUgdXNlZCB0byByZWdpc3RlciBuZXcgZmllbGQgdGVtcGxhdGVzLlxuICovXG5ASW5qZWN0YWJsZSh7IHByb3ZpZGVkSW46ICdyb290JyB9KVxuZXhwb3J0IGNsYXNzIEZvcm1seUNvbmZpZyB7XG4gIHR5cGVzOiB7W25hbWU6IHN0cmluZ106IFR5cGVPcHRpb259ID0ge307XG4gIHZhbGlkYXRvcnM6IHsgW25hbWU6IHN0cmluZ106IFZhbGlkYXRvck9wdGlvbiB9ID0ge307XG4gIHdyYXBwZXJzOiB7IFtuYW1lOiBzdHJpbmddOiBXcmFwcGVyT3B0aW9uIH0gPSB7fTtcbiAgbWVzc2FnZXM6IHsgW25hbWU6IHN0cmluZ106IFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uWydtZXNzYWdlJ10gfSA9IHt9O1xuICB0ZW1wbGF0ZU1hbmlwdWxhdG9yczoge1xuICAgIHByZVdyYXBwZXI6IE1hbmlwdWxhdG9yV3JhcHBlcltdO1xuICAgIHBvc3RXcmFwcGVyOiBNYW5pcHVsYXRvcldyYXBwZXJbXTtcbiAgfSA9IHtcbiAgICBwcmVXcmFwcGVyOiBbXSxcbiAgICBwb3N0V3JhcHBlcjogW10sXG4gIH07XG4gIGV4dHJhczogQ29uZmlnT3B0aW9uWydleHRyYXMnXSA9IHtcbiAgICBjaGVja0V4cHJlc3Npb25PbjogJ2NoYW5nZURldGVjdGlvbkNoZWNrJyxcbiAgICBzaG93RXJyb3I6IGZ1bmN0aW9uKGZpZWxkOiBGaWVsZFR5cGUpIHtcbiAgICAgIHJldHVybiBmaWVsZC5mb3JtQ29udHJvbCAmJiBmaWVsZC5mb3JtQ29udHJvbC5pbnZhbGlkICYmIChmaWVsZC5mb3JtQ29udHJvbC50b3VjaGVkIHx8IChmaWVsZC5vcHRpb25zLnBhcmVudEZvcm0gJiYgZmllbGQub3B0aW9ucy5wYXJlbnRGb3JtLnN1Ym1pdHRlZCkgfHwgISEoZmllbGQuZmllbGQudmFsaWRhdGlvbiAmJiBmaWVsZC5maWVsZC52YWxpZGF0aW9uLnNob3cpKTtcbiAgICB9LFxuICB9O1xuICBleHRlbnNpb25zOiB7IFtuYW1lOiBzdHJpbmddOiBGb3JtbHlFeHRlbnNpb24gfSA9IHt9O1xuXG4gIGFkZENvbmZpZyhjb25maWc6IENvbmZpZ09wdGlvbikge1xuICAgIGlmIChjb25maWcudHlwZXMpIHtcbiAgICAgIGNvbmZpZy50eXBlcy5mb3JFYWNoKHR5cGUgPT4gdGhpcy5zZXRUeXBlKHR5cGUpKTtcbiAgICB9XG4gICAgaWYgKGNvbmZpZy52YWxpZGF0b3JzKSB7XG4gICAgICBjb25maWcudmFsaWRhdG9ycy5mb3JFYWNoKHZhbGlkYXRvciA9PiB0aGlzLnNldFZhbGlkYXRvcih2YWxpZGF0b3IpKTtcbiAgICB9XG4gICAgaWYgKGNvbmZpZy53cmFwcGVycykge1xuICAgICAgY29uZmlnLndyYXBwZXJzLmZvckVhY2god3JhcHBlciA9PiB0aGlzLnNldFdyYXBwZXIod3JhcHBlcikpO1xuICAgIH1cbiAgICBpZiAoY29uZmlnLm1hbmlwdWxhdG9ycykge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ21hbmlwdWxhdG9ycycgY29uZmlnIGlzIGRlcHJlY2F0ZWQsIHVzZSBjdXN0b20gZXh0ZW5zaW9uIGluc3RlYWQuYCk7XG4gICAgICBjb25maWcubWFuaXB1bGF0b3JzLmZvckVhY2gobWFuaXB1bGF0b3IgPT4gdGhpcy5zZXRNYW5pcHVsYXRvcihtYW5pcHVsYXRvcikpO1xuICAgIH1cbiAgICBpZiAoY29uZmlnLnZhbGlkYXRpb25NZXNzYWdlcykge1xuICAgICAgY29uZmlnLnZhbGlkYXRpb25NZXNzYWdlcy5mb3JFYWNoKHZhbGlkYXRpb24gPT4gdGhpcy5hZGRWYWxpZGF0b3JNZXNzYWdlKHZhbGlkYXRpb24ubmFtZSwgdmFsaWRhdGlvbi5tZXNzYWdlKSk7XG4gICAgfVxuICAgIGlmIChjb25maWcuZXh0ZW5zaW9ucykge1xuICAgICAgY29uZmlnLmV4dGVuc2lvbnMuZm9yRWFjaChjID0+IHRoaXMuZXh0ZW5zaW9uc1tjLm5hbWVdID0gYy5leHRlbnNpb24pO1xuICAgIH1cbiAgICBpZiAoY29uZmlnLmV4dHJhcykge1xuICAgICAgdGhpcy5leHRyYXMgPSB7IC4uLnRoaXMuZXh0cmFzLCAuLi5jb25maWcuZXh0cmFzIH07XG4gICAgfVxuICB9XG5cbiAgc2V0VHlwZShvcHRpb25zOiBUeXBlT3B0aW9uIHwgVHlwZU9wdGlvbltdKSB7XG4gICAgaWYgKEFycmF5LmlzQXJyYXkob3B0aW9ucykpIHtcbiAgICAgIG9wdGlvbnMuZm9yRWFjaCgob3B0aW9uKSA9PiB0aGlzLnNldFR5cGUob3B0aW9uKSk7XG4gICAgfSBlbHNlIHtcbiAgICAgIGlmICghdGhpcy50eXBlc1tvcHRpb25zLm5hbWVdKSB7XG4gICAgICAgIHRoaXMudHlwZXNbb3B0aW9ucy5uYW1lXSA9IDxUeXBlT3B0aW9uPnsgbmFtZTogb3B0aW9ucy5uYW1lIH07XG4gICAgICB9XG5cbiAgICAgIFsnY29tcG9uZW50JywgJ2V4dGVuZHMnLCAnZGVmYXVsdE9wdGlvbnMnXS5mb3JFYWNoKHByb3AgPT4ge1xuICAgICAgICBpZiAob3B0aW9ucy5oYXNPd25Qcm9wZXJ0eShwcm9wKSkge1xuICAgICAgICAgIHRoaXMudHlwZXNbb3B0aW9ucy5uYW1lXVtwcm9wXSA9IG9wdGlvbnNbcHJvcF07XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgICBpZiAob3B0aW9ucy53cmFwcGVycykge1xuICAgICAgICBvcHRpb25zLndyYXBwZXJzLmZvckVhY2goKHdyYXBwZXIpID0+IHRoaXMuc2V0VHlwZVdyYXBwZXIob3B0aW9ucy5uYW1lLCB3cmFwcGVyKSk7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgZ2V0VHlwZShuYW1lOiBzdHJpbmcpOiBUeXBlT3B0aW9uIHtcbiAgICBpZiAoIXRoaXMudHlwZXNbbmFtZV0pIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcihgW0Zvcm1seSBFcnJvcl0gVGhlIHR5cGUgXCIke25hbWV9XCIgY291bGQgbm90IGJlIGZvdW5kLiBQbGVhc2UgbWFrZSBzdXJlIHRoYXQgaXMgcmVnaXN0ZXJlZCB0aHJvdWdoIHRoZSBGb3JtbHlNb2R1bGUgZGVjbGFyYXRpb24uYCk7XG4gICAgfVxuXG4gICAgdGhpcy5tZXJnZUV4dGVuZGVkVHlwZShuYW1lKTtcblxuICAgIHJldHVybiB0aGlzLnR5cGVzW25hbWVdO1xuICB9XG5cbiAgZ2V0TWVyZ2VkRmllbGQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnID0ge30pOiBhbnkge1xuICAgIGNvbnN0IHR5cGUgPSB0aGlzLmdldFR5cGUoZmllbGQudHlwZSk7XG4gICAgaWYgKHR5cGUuZGVmYXVsdE9wdGlvbnMpIHtcbiAgICAgIHJldmVyc2VEZWVwTWVyZ2UoZmllbGQsIHR5cGUuZGVmYXVsdE9wdGlvbnMpO1xuICAgIH1cblxuICAgIGNvbnN0IGV4dGVuZERlZmF1bHRzID0gdHlwZS5leHRlbmRzICYmIHRoaXMuZ2V0VHlwZSh0eXBlLmV4dGVuZHMpLmRlZmF1bHRPcHRpb25zO1xuICAgIGlmIChleHRlbmREZWZhdWx0cykge1xuICAgICAgcmV2ZXJzZURlZXBNZXJnZShmaWVsZCwgZXh0ZW5kRGVmYXVsdHMpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZCAmJiBmaWVsZC5vcHRpb25zVHlwZXMpIHtcbiAgICAgIGZpZWxkLm9wdGlvbnNUeXBlcy5mb3JFYWNoKG9wdGlvbiA9PiB7XG4gICAgICAgIGNvbnN0IGRlZmF1bHRPcHRpb25zID0gdGhpcy5nZXRUeXBlKG9wdGlvbikuZGVmYXVsdE9wdGlvbnM7XG4gICAgICAgIGlmIChkZWZhdWx0T3B0aW9ucykge1xuICAgICAgICAgIHJldmVyc2VEZWVwTWVyZ2UoZmllbGQsIGRlZmF1bHRPcHRpb25zKTtcbiAgICAgICAgfVxuICAgICAgfSk7XG4gICAgfVxuXG4gICAgY29uc3QgY29tcG9uZW50UmVmID0gdGhpcy5yZXNvbHZlRmllbGRUeXBlUmVmKGZpZWxkKTtcbiAgICBpZiAoY29tcG9uZW50UmVmICYmIGNvbXBvbmVudFJlZi5pbnN0YW5jZSAmJiBjb21wb25lbnRSZWYuaW5zdGFuY2UuZGVmYXVsdE9wdGlvbnMpIHtcbiAgICAgIHJldmVyc2VEZWVwTWVyZ2UoZmllbGQsIGNvbXBvbmVudFJlZi5pbnN0YW5jZS5kZWZhdWx0T3B0aW9ucyk7XG4gICAgfVxuXG4gICAgaWYgKCFmaWVsZC53cmFwcGVycyAmJiB0eXBlLndyYXBwZXJzKSB7XG4gICAgICBmaWVsZC53cmFwcGVycyA9IFsuLi50eXBlLndyYXBwZXJzXTtcbiAgICB9XG4gIH1cblxuICAvKiogQGludGVybmFsICovXG4gIHJlc29sdmVGaWVsZFR5cGVSZWYoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgPSB7fSk6IENvbXBvbmVudFJlZjxGaWVsZFR5cGU+IHtcbiAgICBpZiAoIWZpZWxkLnR5cGUpIHtcbiAgICAgIHJldHVybiBudWxsO1xuICAgIH1cblxuICAgIGNvbnN0IHR5cGUgPSB0aGlzLmdldFR5cGUoZmllbGQudHlwZSk7XG4gICAgaWYgKCF0eXBlLmNvbXBvbmVudCB8fCB0eXBlWydfY29tcG9uZW50UmVmJ10pIHtcbiAgICAgIHJldHVybiB0eXBlWydfY29tcG9uZW50UmVmJ107XG4gICAgfVxuXG4gICAgY29uc3QgeyBfcmVzb2x2ZXIsIF9pbmplY3RvciB9ID0gZmllbGQucGFyZW50Lm9wdGlvbnM7XG4gICAgZGVmaW5lSGlkZGVuUHJvcChcbiAgICAgIHR5cGUsXG4gICAgICAnX2NvbXBvbmVudFJlZicsXG4gICAgICBfcmVzb2x2ZXIucmVzb2x2ZUNvbXBvbmVudEZhY3Rvcnk8RmllbGRUeXBlPih0eXBlLmNvbXBvbmVudCkuY3JlYXRlKF9pbmplY3RvciksXG4gICAgKTtcblxuICAgIHJldHVybiB0eXBlWydfY29tcG9uZW50UmVmJ107XG4gIH1cblxuICBzZXRXcmFwcGVyKG9wdGlvbnM6IFdyYXBwZXJPcHRpb24pIHtcbiAgICB0aGlzLndyYXBwZXJzW29wdGlvbnMubmFtZV0gPSBvcHRpb25zO1xuICAgIGlmIChvcHRpb25zLnR5cGVzKSB7XG4gICAgICBvcHRpb25zLnR5cGVzLmZvckVhY2goKHR5cGUpID0+IHtcbiAgICAgICAgdGhpcy5zZXRUeXBlV3JhcHBlcih0eXBlLCBvcHRpb25zLm5hbWUpO1xuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgZ2V0V3JhcHBlcihuYW1lOiBzdHJpbmcpOiBXcmFwcGVyT3B0aW9uIHtcbiAgICBpZiAoIXRoaXMud3JhcHBlcnNbbmFtZV0pIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcihgW0Zvcm1seSBFcnJvcl0gVGhlIHdyYXBwZXIgXCIke25hbWV9XCIgY291bGQgbm90IGJlIGZvdW5kLiBQbGVhc2UgbWFrZSBzdXJlIHRoYXQgaXMgcmVnaXN0ZXJlZCB0aHJvdWdoIHRoZSBGb3JtbHlNb2R1bGUgZGVjbGFyYXRpb24uYCk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMud3JhcHBlcnNbbmFtZV07XG4gIH1cblxuICBzZXRUeXBlV3JhcHBlcih0eXBlOiBzdHJpbmcsIG5hbWU6IHN0cmluZykge1xuICAgIGlmICghdGhpcy50eXBlc1t0eXBlXSkge1xuICAgICAgdGhpcy50eXBlc1t0eXBlXSA9IDxUeXBlT3B0aW9uPnt9O1xuICAgIH1cbiAgICBpZiAoIXRoaXMudHlwZXNbdHlwZV0ud3JhcHBlcnMpIHtcbiAgICAgIHRoaXMudHlwZXNbdHlwZV0ud3JhcHBlcnMgPSBbXTtcbiAgICB9XG4gICAgaWYgKHRoaXMudHlwZXNbdHlwZV0ud3JhcHBlcnMuaW5kZXhPZihuYW1lKSA9PT0gLTEpIHtcbiAgICAgIHRoaXMudHlwZXNbdHlwZV0ud3JhcHBlcnMucHVzaChuYW1lKTtcbiAgICB9XG4gIH1cblxuICBzZXRWYWxpZGF0b3Iob3B0aW9uczogVmFsaWRhdG9yT3B0aW9uKSB7XG4gICAgdGhpcy52YWxpZGF0b3JzW29wdGlvbnMubmFtZV0gPSBvcHRpb25zO1xuICB9XG5cbiAgZ2V0VmFsaWRhdG9yKG5hbWU6IHN0cmluZyk6IFZhbGlkYXRvck9wdGlvbiB7XG4gICAgaWYgKCF0aGlzLnZhbGlkYXRvcnNbbmFtZV0pIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcihgW0Zvcm1seSBFcnJvcl0gVGhlIHZhbGlkYXRvciBcIiR7bmFtZX1cIiBjb3VsZCBub3QgYmUgZm91bmQuIFBsZWFzZSBtYWtlIHN1cmUgdGhhdCBpcyByZWdpc3RlcmVkIHRocm91Z2ggdGhlIEZvcm1seU1vZHVsZSBkZWNsYXJhdGlvbi5gKTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy52YWxpZGF0b3JzW25hbWVdO1xuICB9XG5cbiAgYWRkVmFsaWRhdG9yTWVzc2FnZShuYW1lOiBzdHJpbmcsIG1lc3NhZ2U6IFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uWydtZXNzYWdlJ10pIHtcbiAgICB0aGlzLm1lc3NhZ2VzW25hbWVdID0gbWVzc2FnZTtcbiAgfVxuXG4gIGdldFZhbGlkYXRvck1lc3NhZ2UobmFtZTogc3RyaW5nKSB7XG4gICAgcmV0dXJuIHRoaXMubWVzc2FnZXNbbmFtZV07XG4gIH1cblxuICBzZXRNYW5pcHVsYXRvcihtYW5pcHVsYXRvcjogTWFuaXB1bGF0b3JPcHRpb24pIHtcbiAgICBuZXcgbWFuaXB1bGF0b3IuY2xhc3MoKVttYW5pcHVsYXRvci5tZXRob2RdKHRoaXMpO1xuICB9XG5cbiAgcHJpdmF0ZSBtZXJnZUV4dGVuZGVkVHlwZShuYW1lOiBzdHJpbmcpIHtcbiAgICBpZiAoIXRoaXMudHlwZXNbbmFtZV0uZXh0ZW5kcykge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGNvbnN0IGV4dGVuZGVkVHlwZSA9IHRoaXMuZ2V0VHlwZSh0aGlzLnR5cGVzW25hbWVdLmV4dGVuZHMpO1xuICAgIGlmICghdGhpcy50eXBlc1tuYW1lXS5jb21wb25lbnQpIHtcbiAgICAgIHRoaXMudHlwZXNbbmFtZV0uY29tcG9uZW50ID0gZXh0ZW5kZWRUeXBlLmNvbXBvbmVudDtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMudHlwZXNbbmFtZV0ud3JhcHBlcnMpIHtcbiAgICAgIHRoaXMudHlwZXNbbmFtZV0ud3JhcHBlcnMgPSBleHRlbmRlZFR5cGUud3JhcHBlcnM7XG4gICAgfVxuICB9XG59XG5leHBvcnQgaW50ZXJmYWNlIFR5cGVPcHRpb24ge1xuICBuYW1lOiBzdHJpbmc7XG4gIGNvbXBvbmVudD86IGFueTtcbiAgd3JhcHBlcnM/OiBzdHJpbmdbXTtcbiAgZXh0ZW5kcz86IHN0cmluZztcbiAgZGVmYXVsdE9wdGlvbnM/OiBGb3JtbHlGaWVsZENvbmZpZztcbn1cblxuZXhwb3J0IGludGVyZmFjZSBXcmFwcGVyT3B0aW9uIHtcbiAgbmFtZTogc3RyaW5nO1xuICBjb21wb25lbnQ6IGFueTtcbiAgdHlwZXM/OiBzdHJpbmdbXTtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBGaWVsZFZhbGlkYXRvckZuIHtcbiAgKGM6IEFic3RyYWN0Q29udHJvbCwgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnLCBvcHRpb25zPzogeyBbaWQ6IHN0cmluZ106IGFueTsgfSk6IFZhbGlkYXRpb25FcnJvcnMgfCBudWxsO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIFZhbGlkYXRvck9wdGlvbiB7XG4gIG5hbWU6IHN0cmluZztcbiAgdmFsaWRhdGlvbjogRmllbGRWYWxpZGF0b3JGbjtcbiAgb3B0aW9ucz86IHsgW2lkOiBzdHJpbmddOiBhbnkgfTtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBFeHRlbnNpb25PcHRpb24ge1xuICBuYW1lOiBzdHJpbmc7XG4gIGV4dGVuc2lvbjogRm9ybWx5RXh0ZW5zaW9uO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uIHtcbiAgbmFtZTogc3RyaW5nO1xuICBtZXNzYWdlOiBzdHJpbmcgfCAoKGVycm9yOiBhbnksIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykgPT4gc3RyaW5nIHwgT2JzZXJ2YWJsZTxzdHJpbmc+KTtcbn1cblxuZXhwb3J0IGludGVyZmFjZSBNYW5pcHVsYXRvck9wdGlvbiB7XG4gIGNsYXNzPzogeyBuZXcgKCk6IGFueSB9O1xuICBtZXRob2Q/OiBzdHJpbmc7XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgTWFuaXB1bGF0b3JXcmFwcGVyIHtcbiAgKGY6IEZvcm1seUZpZWxkQ29uZmlnKTogc3RyaW5nO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIFRlbXBsYXRlTWFuaXB1bGF0b3JzIHtcbiAgcHJlV3JhcHBlcj86IE1hbmlwdWxhdG9yV3JhcHBlcltdO1xuICBwb3N0V3JhcHBlcj86IE1hbmlwdWxhdG9yV3JhcHBlcltdO1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIENvbmZpZ09wdGlvbiB7XG4gIHR5cGVzPzogVHlwZU9wdGlvbltdO1xuICB3cmFwcGVycz86IFdyYXBwZXJPcHRpb25bXTtcbiAgdmFsaWRhdG9ycz86IFZhbGlkYXRvck9wdGlvbltdO1xuICBleHRlbnNpb25zPzogRXh0ZW5zaW9uT3B0aW9uW107XG4gIHZhbGlkYXRpb25NZXNzYWdlcz86IFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uW107XG5cbiAgLyoqIEBkZXByZWNhdGVkIHVzZSBgZXh0ZW5zaW9uc2AgaW5zdGVhZCAqL1xuICBtYW5pcHVsYXRvcnM/OiBNYW5pcHVsYXRvck9wdGlvbltdO1xuICBleHRyYXM/OiB7XG4gICAgLyoqIEBkZXByZWNhdGVkIHVzZSBgZXh0ZW5zaW9uc2AgaW5zdGVhZCAqL1xuICAgIGZpZWxkVHJhbnNmb3JtPzogYW55LFxuICAgIGltbXV0YWJsZT86IGJvb2xlYW4sXG4gICAgc2hvd0Vycm9yPzogKGZpZWxkOiBGaWVsZFR5cGUpID0+IGJvb2xlYW47XG5cbiAgICAvKipcbiAgICAgKiBEZWZpbmVzIHRoZSBvcHRpb24gd2hpY2ggZm9ybWx5IHJlbHkgb24gdG8gY2hlY2sgZmllbGQgZXhwcmVzc2lvbiBwcm9wZXJ0aWVzLlxuICAgICAqIC0gYG1vZGVsQ2hhbmdlYDogcGVyZm9ybSBhIGNoZWNrIHdoZW4gdGhlIHZhbHVlIG9mIHRoZSBmb3JtIGNvbnRyb2wgY2hhbmdlcy5cbiAgICAgKiAtIGBjaGFuZ2VEZXRlY3Rpb25DaGVja2A6IHRyaWdnZXJzIGFuIGltbWVkaWF0ZSBjaGVjayB3aGVuIGBuZ0RvQ2hlY2tgIGlzIGNhbGxlZC5cbiAgICAqL1xuICAgIGNoZWNrRXhwcmVzc2lvbk9uPzogJ21vZGVsQ2hhbmdlJyB8ICdjaGFuZ2VEZXRlY3Rpb25DaGVjaycsXG4gIH07XG59XG4iXX0=