/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Validators } from '@angular/forms';
import { FORMLY_VALIDATORS, defineHiddenProp, isPromise, wrapProperty, clone } from '../../utils';
import { updateValidity } from '../field-form/utils';
import { isObservable } from 'rxjs';
import { map } from 'rxjs/operators';
/**
 * \@experimental
 */
var /**
 * \@experimental
 */
FieldValidationExtension = /** @class */ (function () {
    function FieldValidationExtension(formlyConfig) {
        this.formlyConfig = formlyConfig;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    FieldValidationExtension.prototype.onPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        this.initFieldValidation(field, 'validators');
        this.initFieldValidation(field, 'asyncValidators');
    };
    /**
     * @private
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    FieldValidationExtension.prototype.initFieldValidation = /**
     * @private
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    function (field, type) {
        var _this = this;
        /** @type {?} */
        var validators = [];
        if (type === 'validators' && !(field.hasOwnProperty('fieldGroup') && !field.key)) {
            validators.push(this.getPredefinedFieldValidation(field));
        }
        if (field[type]) {
            for (var validatorName in field[type]) {
                if (validatorName === 'validation' && !Array.isArray(field[type].validation)) {
                    field[type].validation = [field[type].validation];
                    console.warn("NgxFormly(" + field.key + "): passing a non array value to the 'validation' is deprecated, pass an array instead");
                }
                validatorName === 'validation'
                    ? validators.push.apply(validators, tslib_1.__spread(field[type].validation.map((/**
                     * @param {?} v
                     * @return {?}
                     */
                    function (v) { return _this.wrapNgValidatorFn(field, v); })))) : validators.push(this.wrapNgValidatorFn(field, field[type][validatorName], validatorName));
            }
        }
        defineHiddenProp(field, '_' + type, validators);
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    FieldValidationExtension.prototype.getPredefinedFieldValidation = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        /** @type {?} */
        var VALIDATORS = [];
        FORMLY_VALIDATORS.forEach((/**
         * @param {?} opt
         * @return {?}
         */
        function (opt) { return wrapProperty(field.templateOptions, opt, (/**
         * @param {?} __0
         * @return {?}
         */
        function (_a) {
            var currentValue = _a.currentValue, firstChange = _a.firstChange;
            VALIDATORS = VALIDATORS.filter((/**
             * @param {?} o
             * @return {?}
             */
            function (o) { return o !== opt; }));
            if (currentValue != null && currentValue !== false) {
                VALIDATORS.push(opt);
            }
            if (!firstChange && field.formControl) {
                updateValidity(field.formControl);
            }
        })); }));
        return (/**
         * @param {?} control
         * @return {?}
         */
        function (control) {
            if (VALIDATORS.length === 0) {
                return null;
            }
            return Validators.compose(VALIDATORS.map((/**
             * @param {?} opt
             * @return {?}
             */
            function (opt) { return (/**
             * @return {?}
             */
            function () {
                /** @type {?} */
                var value = field.templateOptions[opt];
                switch (opt) {
                    case 'required':
                        return Validators.required(control);
                    case 'pattern':
                        return Validators.pattern(value)(control);
                    case 'minLength':
                        return Validators.minLength(value)(control);
                    case 'maxLength':
                        return Validators.maxLength(value)(control);
                    case 'min':
                        return Validators.min(value)(control);
                    case 'max':
                        return Validators.max(value)(control);
                }
            }); })))(control);
        });
    };
    /**
     * @private
     * @param {?} field
     * @param {?} validator
     * @param {?=} validatorName
     * @return {?}
     */
    FieldValidationExtension.prototype.wrapNgValidatorFn = /**
     * @private
     * @param {?} field
     * @param {?} validator
     * @param {?=} validatorName
     * @return {?}
     */
    function (field, validator, validatorName) {
        var _this = this;
        /** @type {?} */
        var validatorOption = null;
        if (typeof validator === 'string') {
            validatorOption = clone(this.formlyConfig.getValidator(validator));
        }
        if (typeof validator === 'object' && validator.name) {
            validatorOption = clone(this.formlyConfig.getValidator(validator.name));
            if (validator.options) {
                validatorOption.options = validator.options;
            }
        }
        if (typeof validator === 'object' && validator.expression) {
            var expression = validator.expression, options = tslib_1.__rest(validator, ["expression"]);
            validatorOption = {
                name: validatorName,
                validation: expression,
                options: Object.keys(options).length > 0 ? options : null,
            };
        }
        if (typeof validator === 'function') {
            validatorOption = {
                name: validatorName,
                validation: validator,
            };
        }
        return (/**
         * @param {?} control
         * @return {?}
         */
        function (control) {
            /** @type {?} */
            var errors = validatorOption.validation(control, field, validatorOption.options);
            if (isPromise(errors)) {
                return errors.then((/**
                 * @param {?} v
                 * @return {?}
                 */
                function (v) { return _this.handleAsyncResult(field, validatorName ? !!v : v, validatorOption); }));
            }
            if (isObservable(errors) && !validatorName) {
                return errors.pipe(map((/**
                 * @param {?} v
                 * @return {?}
                 */
                function (v) { return _this.handleAsyncResult(field, v, validatorOption); })));
            }
            return _this.handleResult(field, validatorName ? !!errors : errors, validatorOption);
        });
    };
    /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} options
     * @return {?}
     */
    FieldValidationExtension.prototype.handleAsyncResult = /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} options
     * @return {?}
     */
    function (field, errors, options) {
        // workaround for https://github.com/angular/angular/issues/13200
        if (field.options && field.options._markForCheck) {
            field.options._markForCheck(field);
        }
        return this.handleResult(field, errors, options);
    };
    /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} __2
     * @return {?}
     */
    FieldValidationExtension.prototype.handleResult = /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} __2
     * @return {?}
     */
    function (field, errors, _a) {
        var name = _a.name, options = _a.options;
        var _b, _c;
        if (typeof errors === 'boolean') {
            errors = errors ? null : (_b = {}, _b[name] = options ? options : true, _b);
        }
        /** @type {?} */
        var ctrl = field.formControl;
        ctrl['_childrenErrors'] && ctrl['_childrenErrors'][name] && ctrl['_childrenErrors'][name]();
        if (errors && errors[name]) {
            /** @type {?} */
            var errorPath = errors[name].errorPath
                ? errors[name].errorPath
                : (options || {}).errorPath;
            /** @type {?} */
            var childCtrl_1 = errorPath ? field.formControl.get(errorPath) : null;
            if (childCtrl_1) {
                var _d = errors[name], errorPath_1 = _d.errorPath, opts = tslib_1.__rest(_d, ["errorPath"]);
                childCtrl_1.setErrors(tslib_1.__assign({}, (childCtrl_1.errors || {}), (_c = {}, _c[name] = opts, _c)));
                !ctrl['_childrenErrors'] && defineHiddenProp(ctrl, '_childrenErrors', {});
                ctrl['_childrenErrors'][name] = (/**
                 * @return {?}
                 */
                function () {
                    var _a = childCtrl_1.errors || {}, _b = name, toDelete = _a[_b], childErrors = tslib_1.__rest(_a, [typeof _b === "symbol" ? _b : _b + ""]);
                    childCtrl_1.setErrors(Object.keys(childErrors).length === 0 ? null : childErrors);
                });
            }
        }
        return errors;
    };
    return FieldValidationExtension;
}());
/**
 * \@experimental
 */
export { FieldValidationExtension };
if (false) {
    /**
     * @type {?}
     * @private
     */
    FieldValidationExtension.prototype.formlyConfig;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtdmFsaWRhdGlvbi5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC12YWxpZGF0aW9uL2ZpZWxkLXZhbGlkYXRpb24udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFFQSxPQUFPLEVBQW1CLFVBQVUsRUFBZSxNQUFNLGdCQUFnQixDQUFDO0FBQzFFLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxnQkFBZ0IsRUFBRSxTQUFTLEVBQUUsWUFBWSxFQUFFLEtBQUssRUFBRSxNQUFNLGFBQWEsQ0FBQztBQUNsRyxPQUFPLEVBQUUsY0FBYyxFQUFFLE1BQU0scUJBQXFCLENBQUM7QUFDckQsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUNwQyxPQUFPLEVBQUUsR0FBRyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7Ozs7QUFHckM7Ozs7SUFDRSxrQ0FBb0IsWUFBMEI7UUFBMUIsaUJBQVksR0FBWixZQUFZLENBQWM7SUFBRyxDQUFDOzs7OztJQUVsRCw2Q0FBVTs7OztJQUFWLFVBQVcsS0FBNkI7UUFDdEMsSUFBSSxDQUFDLG1CQUFtQixDQUFDLEtBQUssRUFBRSxZQUFZLENBQUMsQ0FBQztRQUM5QyxJQUFJLENBQUMsbUJBQW1CLENBQUMsS0FBSyxFQUFFLGlCQUFpQixDQUFDLENBQUM7SUFDckQsQ0FBQzs7Ozs7OztJQUVPLHNEQUFtQjs7Ozs7O0lBQTNCLFVBQTRCLEtBQTZCLEVBQUUsSUFBc0M7UUFBakcsaUJBcUJDOztZQXBCTyxVQUFVLEdBQWtCLEVBQUU7UUFDcEMsSUFBSSxJQUFJLEtBQUssWUFBWSxJQUFJLENBQUMsQ0FBQyxLQUFLLENBQUMsY0FBYyxDQUFDLFlBQVksQ0FBQyxJQUFJLENBQUMsS0FBSyxDQUFDLEdBQUcsQ0FBQyxFQUFFO1lBQ2hGLFVBQVUsQ0FBQyxJQUFJLENBQUMsSUFBSSxDQUFDLDRCQUE0QixDQUFDLEtBQUssQ0FB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O1lBQ3JFLElBQUksV0FBUyxFQUFFO29CQUNQLGlCQUFxQyxFQUFuQywwQkFBUyxFQUFFLHdDQUFPO2dCQUMxQixXQUFTLENBQUMsU0FBUyxzQkFBTSxDQUFDLFdBQVMsQ0FBQyxNQUFNLElBQUksRUFBRSxDQUFDLGVBQUcsSUFBSSxJQUFHLElBQUksT0FBRyxDQUFDO2dCQUVuRSxDQUFDLElBQUksQ0FBQyxpQkFBaUIsQ0FBQyxJQUFJLGdCQUFnQixDQUFDLElBQUksRUFBRSxpQkFBaUIsRUFBRSxFQUFFLENBQUMsQ0FBQztnQkFDMUUsSUFBSSxDQUFDLGlCQUFpQixDQUFDLENBQUMsSUFBSSxDQUFDOzs7Z0JBQUc7d0JBQ3hCLDZCQUE2RCxFQUEzRCxTQUFNLEVBQU4saUJBQWdCLEVBQUUseUVBQWM7b0JBQ3hDLFdBQVMsQ0FBQyxTQUFTLENBQUMsTUFBTSxDQUFDLElBQUksQ0FBQyxXQUFXLENBQUMsQ0FBQyxNQUFNLEtBQUssQ0FBQyxDQUFDLENBQUMsQ0FBQyxJQUFJLENBQUMsQ0FBQyxDQUFDLFdBQVcsQ0FBQyxDQUFDO2dCQUNsRixDQUFDLENBQUEsQ0FBQzthQUNIO1NBQ0Y7UUFFRCxPQUFPLE1BQU0sQ0FBQztJQUNoQixDQUFDO0lBQ0gsK0JBQUM7QUFBRCxDQUFDLEFBcEpELElBb0pDOzs7Ozs7Ozs7O0lBbkphLGdEQUFrQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEZvcm1seUV4dGVuc2lvbiwgRm9ybWx5Q29uZmlnLCBWYWxpZGF0b3JPcHRpb24gfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi8uLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgQWJzdHJhY3RDb250cm9sLCBWYWxpZGF0b3JzLCBWYWxpZGF0b3JGbiB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZPUk1MWV9WQUxJREFUT1JTLCBkZWZpbmVIaWRkZW5Qcm9wLCBpc1Byb21pc2UsIHdyYXBQcm9wZXJ0eSwgY2xvbmUgfSBmcm9tICcuLi8uLi91dGlscyc7XG5pbXBvcnQgeyB1cGRhdGVWYWxpZGl0eSB9IGZyb20gJy4uL2ZpZWxkLWZvcm0vdXRpbHMnO1xuaW1wb3J0IHsgaXNPYnNlcnZhYmxlIH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBtYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbi8qKiBAZXhwZXJpbWVudGFsICovXG5leHBvcnQgY2xhc3MgRmllbGRWYWxpZGF0aW9uRXh0ZW5zaW9uIGltcGxlbWVudHMgRm9ybWx5RXh0ZW5zaW9uIHtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSBmb3JtbHlDb25maWc6IEZvcm1seUNvbmZpZykge31cblxuICBvblBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5pbml0RmllbGRWYWxpZGF0aW9uKGZpZWxkLCAndmFsaWRhdG9ycycpO1xuICAgIHRoaXMuaW5pdEZpZWxkVmFsaWRhdGlvbihmaWVsZCwgJ2FzeW5jVmFsaWRhdG9ycycpO1xuICB9XG5cbiAgcHJpdmF0ZSBpbml0RmllbGRWYWxpZGF0aW9uKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCB0eXBlOiAndmFsaWRhdG9ycycgfCAnYXN5bmNWYWxpZGF0b3JzJykge1xuICAgIGNvbnN0IHZhbGlkYXRvcnM6IFZhbGlkYXRvckZuW10gPSBbXTtcbiAgICBpZiAodHlwZSA9PT0gJ3ZhbGlkYXRvcnMnICYmICEoZmllbGQuaGFzT3duUHJvcGVydHkoJ2ZpZWxkR3JvdXAnKSAmJiAhZmllbGQua2V5KSkge1xuICAgICAgdmFsaWRhdG9ycy5wdXNoKHRoaXMuZ2V0UHJlZGVmaW5lZEZpZWxkVmFsaWRhdGlvbihmaWVsZCkpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZFt0eXBlXSkge1xuICAgICAgZm9yIChjb25zdCB2YWxpZGF0b3JOYW1lIGluIGZpZWxkW3R5cGVdKSB7XG4gICAgICAgIGlmICh2YWxpZGF0b3JOYW1lID09PSAndmFsaWRhdGlvbicgJiYgIUFycmF5LmlzQXJyYXkoZmllbGRbdHlwZV0udmFsaWRhdGlvbikpIHtcbiAgICAgICAgICBmaWVsZFt0eXBlXS52YWxpZGF0aW9uID0gW2ZpZWxkW3R5cGVdLnZhbGlkYXRpb25dO1xuICAgICAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5KCR7ZmllbGQua2V5fSk6IHBhc3NpbmcgYSBub24gYXJyYXkgdmFsdWUgdG8gdGhlICd2YWxpZGF0aW9uJyBpcyBkZXByZWNhdGVkLCBwYXNzIGFuIGFycmF5IGluc3RlYWRgKTtcbiAgICAgICAgfVxuXG4gICAgICAgIHZhbGlkYXRvck5hbWUgPT09ICd2YWxpZGF0aW9uJ1xuICAgICAgICAgID8gdmFsaWRhdG9ycy5wdXNoKC4uLmZpZWxkW3R5cGVdLnZhbGlkYXRpb24ubWFwKHYgPT4gdGhpcy53cmFwTmdWYWxpZGF0b3JGbihmaWVsZCwgdikpKVxuICAgICAgICAgIDogdmFsaWRhdG9ycy5wdXNoKHRoaXMud3JhcE5nVmFsaWRhdG9yRm4oZmllbGQsIGZpZWxkW3R5cGVdW3ZhbGlkYXRvck5hbWVdLCB2YWxpZGF0b3JOYW1lKSlcbiAgICAgICAgO1xuICAgICAgfVxuICAgIH1cblxuICAgIGRlZmluZUhpZGRlblByb3AoZmllbGQsICdfJyArIHR5cGUsIHZhbGlkYXRvcnMpO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRQcmVkZWZpbmVkRmllbGRWYWxpZGF0aW9uKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKTogVmFsaWRhdG9yRm4ge1xuICAgIGxldCBWQUxJREFUT1JTID0gW107XG4gICAgRk9STUxZX1ZBTElEQVRPUlMuZm9yRWFjaChvcHQgPT4gd3JhcFByb3BlcnR5KGZpZWxkLnRlbXBsYXRlT3B0aW9ucywgb3B0LCAoeyBjdXJyZW50VmFsdWUsIGZpcnN0Q2hhbmdlIH0pID0+IHtcbiAgICAgIFZBTElEQVRPUlMgPSBWQUxJREFUT1JTLmZpbHRlcihvID0+IG8gIT09IG9wdCk7XG4gICAgICBpZiAoY3VycmVudFZhbHVlICE9IG51bGwgJiYgY3VycmVudFZhbHVlICE9PSBmYWxzZSkge1xuICAgICAgICBWQUxJREFUT1JTLnB1c2gob3B0KTtcbiAgICAgIH1cbiAgICAgIGlmICghZmlyc3RDaGFuZ2UgJiYgZmllbGQuZm9ybUNvbnRyb2wpIHtcbiAgICAgICAgdXBkYXRlVmFsaWRpdHkoZmllbGQuZm9ybUNvbnRyb2wpO1xuICAgICAgfVxuICAgIH0pKTtcblxuICAgIHJldHVybiAoY29udHJvbDogQWJzdHJhY3RDb250cm9sKSA9PiB7XG4gICAgICBpZiAoVkFMSURBVE9SUy5sZW5ndGggPT09IDApIHtcbiAgICAgICAgcmV0dXJuIG51bGw7XG4gICAgICB9XG5cbiAgICAgIHJldHVybiBWYWxpZGF0b3JzLmNvbXBvc2UoVkFMSURBVE9SUy5tYXAob3B0ID0+ICgpID0+IHtcbiAgICAgICAgY29uc3QgdmFsdWUgPSBmaWVsZC50ZW1wbGF0ZU9wdGlvbnNbb3B0XTtcbiAgICAgICAgc3dpdGNoIChvcHQpIHtcbiAgICAgICAgICBjYXNlICdyZXF1aXJlZCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5yZXF1aXJlZChjb250cm9sKTtcbiAgICAgICAgICBjYXNlICdwYXR0ZXJuJzpcbiAgICAgICAgICAgIHJldHVybiBWYWxpZGF0b3JzLnBhdHRlcm4odmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21pbkxlbmd0aCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5taW5MZW5ndGgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21heExlbmd0aCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5tYXhMZW5ndGgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21pbic6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5taW4odmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21heCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5tYXgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICB9XG4gICAgICB9KSkoY29udHJvbCk7XG4gICAgfTtcbiAgfVxuXG4gIHByaXZhdGUgd3JhcE5nVmFsaWRhdG9yRm4oZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHZhbGlkYXRvcjogYW55LCB2YWxpZGF0b3JOYW1lPzogc3RyaW5nKSB7XG4gICAgbGV0IHZhbGlkYXRvck9wdGlvbjogVmFsaWRhdG9yT3B0aW9uID0gbnVsbDtcbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ3N0cmluZycpIHtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IGNsb25lKHRoaXMuZm9ybWx5Q29uZmlnLmdldFZhbGlkYXRvcih2YWxpZGF0b3IpKTtcbiAgICB9XG5cbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ29iamVjdCcgJiYgdmFsaWRhdG9yLm5hbWUpIHtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IGNsb25lKHRoaXMuZm9ybWx5Q29uZmlnLmdldFZhbGlkYXRvcih2YWxpZGF0b3IubmFtZSkpO1xuICAgICAgaWYgKHZhbGlkYXRvci5vcHRpb25zKSB7XG4gICAgICAgIHZhbGlkYXRvck9wdGlvbi5vcHRpb25zID0gdmFsaWRhdG9yLm9wdGlvbnM7XG4gICAgICB9XG4gICAgfVxuXG4gICAgaWYgKHR5cGVvZiB2YWxpZGF0b3IgPT09ICdvYmplY3QnICYmIHZhbGlkYXRvci5leHByZXNzaW9uKSB7XG4gICAgICBjb25zdCB7IGV4cHJlc3Npb24sIC4uLm9wdGlvbnMgfSA9IHZhbGlkYXRvcjtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IHtcbiAgICAgICAgbmFtZTogdmFsaWRhdG9yTmFtZSxcbiAgICAgICAgdmFsaWRhdGlvbjogZXhwcmVzc2lvbixcbiAgICAgICAgb3B0aW9uczogT2JqZWN0LmtleXMob3B0aW9ucykubGVuZ3RoID4gMCA/IG9wdGlvbnMgOiBudWxsLFxuICAgICAgfTtcbiAgICB9XG5cbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ2Z1bmN0aW9uJykge1xuICAgICAgdmFsaWRhdG9yT3B0aW9uID0ge1xuICAgICAgICBuYW1lOiB2YWxpZGF0b3JOYW1lLFxuICAgICAgICB2YWxpZGF0aW9uOiB2YWxpZGF0b3IsXG4gICAgICB9O1xuICAgIH1cblxuICAgIHJldHVybiAoY29udHJvbDogQWJzdHJhY3RDb250cm9sKSA9PiB7XG4gICAgICBjb25zdCBlcnJvcnM6IGFueSA9IHZhbGlkYXRvck9wdGlvbi52YWxpZGF0aW9uKGNvbnRyb2wsIGZpZWxkLCB2YWxpZGF0b3JPcHRpb24ub3B0aW9ucyk7XG4gICAgICBpZiAoaXNQcm9taXNlKGVycm9ycykpIHtcbiAgICAgICAgcmV0dXJuIGVycm9ycy50aGVuKHYgPT4gdGhpcy5oYW5kbGVBc3luY1Jlc3VsdChmaWVsZCwgdmFsaWRhdG9yTmFtZSA/ICEhdiA6IHYsIHZhbGlkYXRvck9wdGlvbikpO1xuICAgICAgfVxuXG4gICAgICBpZiAoaXNPYnNlcnZhYmxlKGVycm9ycykgJiYgIXZhbGlkYXRvck5hbWUpIHtcbiAgICAgICAgcmV0dXJuIGVycm9ycy5waXBlKG1hcCh2ID0+IHRoaXMuaGFuZGxlQXN5bmNSZXN1bHQoZmllbGQsIHYsIHZhbGlkYXRvck9wdGlvbikpKTtcbiAgICAgIH1cblxuICAgICAgcmV0dXJuIHRoaXMuaGFuZGxlUmVzdWx0KGZpZWxkLCB2YWxpZGF0b3JOYW1lID8gISFlcnJvcnMgOiBlcnJvcnMsIHZhbGlkYXRvck9wdGlvbik7XG4gICAgfTtcbiAgfVxuXG4gIHByaXZhdGUgaGFuZGxlQXN5bmNSZXN1bHQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGVycm9yczogYW55LCBvcHRpb25zOiBWYWxpZGF0b3JPcHRpb24pIHtcbiAgICAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9hbmd1bGFyL2lzc3Vlcy8xMzIwMFxuICAgIGlmIChmaWVsZC5vcHRpb25zICYmIGZpZWxkLm9wdGlvbnMuX21hcmtGb3JDaGVjaykge1xuICAgICAgZmllbGQub3B0aW9ucy5fbWFya0ZvckNoZWNrKGZpZWxkKTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy5oYW5kbGVSZXN1bHQoZmllbGQsIGVycm9ycywgb3B0aW9ucyk7XG4gIH1cblxuICBwcml2YXRlIGhhbmRsZVJlc3VsdChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgZXJyb3JzOiBhbnksIHsgbmFtZSwgb3B0aW9ucyB9OiBWYWxpZGF0b3JPcHRpb24pIHtcbiAgICBpZiAodHlwZW9mIGVycm9ycyA9PT0gJ2Jvb2xlYW4nKSB7XG4gICAgICBlcnJvcnMgPSBlcnJvcnMgPyBudWxsIDogeyBbbmFtZV06IG9wdGlvbnMgPyBvcHRpb25zIDogdHJ1ZSB9O1xuICAgIH1cblxuICAgIGNvbnN0IGN0cmwgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgICBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXSAmJiBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXVtuYW1lXSAmJiBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXVtuYW1lXSgpO1xuXG4gICAgaWYgKGVycm9ycyAmJiBlcnJvcnNbbmFtZV0pIHtcbiAgICAgIGNvbnN0IGVycm9yUGF0aCA9IGVycm9yc1tuYW1lXS5lcnJvclBhdGhcbiAgICAgICAgPyBlcnJvcnNbbmFtZV0uZXJyb3JQYXRoXG4gICAgICAgIDogKG9wdGlvbnMgfHwge30pLmVycm9yUGF0aDtcblxuICAgICAgY29uc3QgY2hpbGRDdHJsID0gZXJyb3JQYXRoID8gZmllbGQuZm9ybUNvbnRyb2wuZ2V0KGVycm9yUGF0aCkgOiBudWxsO1xuICAgICAgaWYgKGNoaWxkQ3RybCkge1xuICAgICAgICBjb25zdCB7IGVycm9yUGF0aCwgLi4ub3B0cyB9ID0gZXJyb3JzW25hbWVdO1xuICAgICAgICBjaGlsZEN0cmwuc2V0RXJyb3JzKHsgLi4uKGNoaWxkQ3RybC5lcnJvcnMgfHwge30pLCBbbmFtZV06IG9wdHMgfSk7XG5cbiAgICAgICAgIWN0cmxbJ19jaGlsZHJlbkVycm9ycyddICYmIGRlZmluZUhpZGRlblByb3AoY3RybCwgJ19jaGlsZHJlbkVycm9ycycsIHt9KTtcbiAgICAgICAgY3RybFsnX2NoaWxkcmVuRXJyb3JzJ11bbmFtZV0gPSAoKSA9PiB7XG4gICAgICAgICAgY29uc3QgeyBbbmFtZV06IHRvRGVsZXRlLCAuLi5jaGlsZEVycm9ycyB9ID0gY2hpbGRDdHJsLmVycm9ycyB8fCB7fTtcbiAgICAgICAgICBjaGlsZEN0cmwuc2V0RXJyb3JzKE9iamVjdC5rZXlzKGNoaWxkRXJyb3JzKS5sZW5ndGggPT09IDAgPyBudWxsIDogY2hpbGRFcnJvcnMpO1xuICAgICAgICB9O1xuICAgICAgfVxuICAgIH1cblxuICAgIHJldHVybiBlcnJvcnM7XG4gIH1cbn1cbiJdfQ==