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
export class FieldValidationExtension {
    /**
     * @param {?} formlyConfig
     */
    constructor(formlyConfig) {
        this.formlyConfig = formlyConfig;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    onPopulate(field) {
        this.initFieldValidation(field, 'validators');
        this.initFieldValidation(field, 'asyncValidators');
    }
    /**
     * @private
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    initFieldValidation(field, type) {
        /** @type {?} */
        const validators = [];
        if (type === 'validators' && !(field.hasOwnProperty('fieldGroup') && !field.key)) {
            validators.push(this.getPredefinedFieldValidation(field));
        }
        if (field[type]) {
            for (const validatorName in field[type]) {
                if (validatorName === 'validation' && !Array.isArray(field[type].validation)) {
                    field[type].validation = [field[type].validation];
                    console.warn(`NgxFormly(${field.key}): passing a non array value to the 'validation' is deprecated, pass an array instead`);
                }
                validatorName === 'validation'
                    ? validators.push(...field[type].validation.map((/**
                     * @param {?} v
                     * @return {?}
                     */
                    v => this.wrapNgValidatorFn(field, v))))
                    : validators.push(this.wrapNgValidatorFn(field, field[type][validatorName], validatorName));
            }
        }
        defineHiddenProp(field, '_' + type, validators);
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    getPredefinedFieldValidation(field) {
        /** @type {?} */
        let VALIDATORS = [];
        FORMLY_VALIDATORS.forEach((/**
         * @param {?} opt
         * @return {?}
         */
        opt => wrapProperty(field.templateOptions, opt, (/**
         * @param {?} __0
         * @return {?}
         */
        ({ currentValue, firstChange }) => {
            VALIDATORS = VALIDATORS.filter((/**
             * @param {?} o
             * @return {?}
             */
            o => o !== opt));
            if (currentValue != null && currentValue !== false) {
                VALIDATORS.push(opt);
            }
            if (!firstChange && field.formControl) {
                updateValidity(field.formControl);
            }
        }))));
        return (/**
         * @param {?} control
         * @return {?}
         */
        (control) => {
            if (VALIDATORS.length === 0) {
                return null;
            }
            return Validators.compose(VALIDATORS.map((/**
             * @param {?} opt
             * @return {?}
             */
            opt => (/**
             * @return {?}
             */
            () => {
                /** @type {?} */
                const value = field.templateOptions[opt];
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
            }))))(control);
        });
    }
    /**
     * @private
     * @param {?} field
     * @param {?} validator
     * @param {?=} validatorName
     * @return {?}
     */
    wrapNgValidatorFn(field, validator, validatorName) {
        /** @type {?} */
        let validatorOption = null;
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
            const { expression } = validator, options = tslib_1.__rest(validator, ["expression"]);
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
        (control) => {
            /** @type {?} */
            const errors = validatorOption.validation(control, field, validatorOption.options);
            if (isPromise(errors)) {
                return errors.then((/**
                 * @param {?} v
                 * @return {?}
                 */
                v => this.handleAsyncResult(field, validatorName ? !!v : v, validatorOption)));
            }
            if (isObservable(errors) && !validatorName) {
                return errors.pipe(map((/**
                 * @param {?} v
                 * @return {?}
                 */
                v => this.handleAsyncResult(field, v, validatorOption))));
            }
            return this.handleResult(field, validatorName ? !!errors : errors, validatorOption);
        });
    }
    /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} options
     * @return {?}
     */
    handleAsyncResult(field, errors, options) {
        // workaround for https://github.com/angular/angular/issues/13200
        if (field.options && field.options._markForCheck) {
            field.options._markForCheck(field);
        }
        return this.handleResult(field, errors, options);
    }
    /**
     * @private
     * @param {?} field
     * @param {?} errors
     * @param {?} __2
     * @return {?}
     */
    handleResult(field, errors, { name, options }) {
        if (typeof errors === 'boolean') {
            errors = errors ? null : { [name]: options ? options : true };
        }
        /** @type {?} */
        const ctrl = field.formControl;
        ctrl['_childrenErrors'] && ctrl['_childrenErrors'][name] && ctrl['_childrenErrors'][name]();
        if (errors && errors[name]) {
            /** @type {?} */
            const errorPath = errors[name].errorPath
                ? errors[name].errorPath
                : (options || {}).errorPath;
            /** @type {?} */
            const childCtrl = errorPath ? field.formControl.get(errorPath) : null;
            if (childCtrl) {
                const _a = errors[name], { errorPath } = _a, opts = tslib_1.__rest(_a, ["errorPath"]);
                childCtrl.setErrors(Object.assign({}, (childCtrl.errors || {}), { [name]: opts }));
                !ctrl['_childrenErrors'] && defineHiddenProp(ctrl, '_childrenErrors', {});
                ctrl['_childrenErrors'][name] = (/**
                 * @return {?}
                 */
                () => {
                    const _a = childCtrl.errors || {}, _b = name, toDelete = _a[_b], childErrors = tslib_1.__rest(_a, [typeof _b === "symbol" ? _b : _b + ""]);
                    childCtrl.setErrors(Object.keys(childErrors).length === 0 ? null : childErrors);
                });
            }
        }
        return errors;
    }
}
if (false) {
    /**
     * @type {?}
     * @private
     */
    FieldValidationExtension.prototype.formlyConfig;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtdmFsaWRhdGlvbi5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC12YWxpZGF0aW9uL2ZpZWxkLXZhbGlkYXRpb24udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFFQSxPQUFPLEVBQW1CLFVBQVUsRUFBZSxNQUFNLGdCQUFnQixDQUFDO0FBQzFFLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxnQkFBZ0IsRUFBRSxTQUFTLEVBQUUsWUFBWSxFQUFFLEtBQUssRUFBRSxNQUFNLGFBQWEsQ0FBQztBQUNsRyxPQUFPLEVBQUUsY0FBYyxFQUFFLE1BQU0scUJBQXFCLENBQUM7QUFDckQsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUNwQyxPQUFPLEVBQUUsR0FBRyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7Ozs7QUFHckMsTUFBTSxPQUFPLHdCQUF3Qjs7OztJQUNuQyxZQUFvQixZQUEwQjtRQUExQixpQkFBWSxHQUFaLFlBQVksQ0FBYztJQUFHLENBQUM7Ozs7O0lBRWxELFVBQVUsQ0FBQyxLQUE2QjtRQUN0QyxJQUFJLENBQUMsbUJBQW1CLENBQUMsS0FBSyxFQUFFLFlBQVksQ0FBQyxDQUFDO1FBQzlDLElBQUksQ0FBQyxtQkFBbUIsQ0FBQyxLQUFLLEVBQUUsaUJBQWlCLENBQUMsQ0FBQztJQUNyRCxDQUFDOzs7Ozs7O0lBRU8sbUJBQW1CLENBQUMsS0FBNkIsRUFBRSxJQUFzQzs7Y0FDekYsVUFBVSxHQUFrQixFQUFFO1FBQ3BDLElBQUksSUFBSSxLQUFLLFlBQVksSUFBSSxDQUFDLENBQUMsS0FBSyxDQUFDLGNBQWMsQ0FBQyxZQUFZLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxHQUFHLENBQUMsRUFBRTtZQUNoRixVQUFVLENBQUMsSUFBSSxDQUFDLElBQUksQ0FBQyw0QkFBNEIsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDO1NBQzNE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LENBQUMsU0FBUyxtQkFBTSxDQUFDLFNBQVMsQ0FBQyxNQUFNLElBQUksRUFBRSxDQUFDLElBQUUsQ0FBQyxJQUFJLENBQUMsRUFBRSxJQUFJLElBQUcsQ0FBQztnQkFFbkUsQ0FBQyxJQUFJLENBQUMsaUJBQWlCLENBQUMsSUFBSSxnQkFBZ0IsQ0FBQyxJQUFJLEVBQUUsaUJBQWlCLEVBQUUsRUFBRSxDQUFDLENBQUM7Z0JBQzFFLElBQUksQ0FBQyxpQkFBaUIsQ0FBQyxDQUFDLElBQUksQ0FBQzs7O2dCQUFHLEdBQUcsRUFBRTswQkFDN0IsMkJBQTZELEVBQTNELFNBQU0sRUFBTixpQkFBZ0IsRUFBRSx5RUFBYztvQkFDeEMsU0FBUyxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxDQUFDLE1BQU0sS0FBSyxDQUFDLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsV0FBVyxDQUFDLENBQUM7Z0JBQ2xGLENBQUMsQ0FBQSxDQUFDO2FBQ0g7U0FDRjtRQUVELE9BQU8sTUFBTSxDQUFDO0lBQ2hCLENBQUM7Q0FDRjs7Ozs7O0lBbkphLGdEQUFrQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEZvcm1seUV4dGVuc2lvbiwgRm9ybWx5Q29uZmlnLCBWYWxpZGF0b3JPcHRpb24gfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi8uLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgQWJzdHJhY3RDb250cm9sLCBWYWxpZGF0b3JzLCBWYWxpZGF0b3JGbiB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZPUk1MWV9WQUxJREFUT1JTLCBkZWZpbmVIaWRkZW5Qcm9wLCBpc1Byb21pc2UsIHdyYXBQcm9wZXJ0eSwgY2xvbmUgfSBmcm9tICcuLi8uLi91dGlscyc7XG5pbXBvcnQgeyB1cGRhdGVWYWxpZGl0eSB9IGZyb20gJy4uL2ZpZWxkLWZvcm0vdXRpbHMnO1xuaW1wb3J0IHsgaXNPYnNlcnZhYmxlIH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBtYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbi8qKiBAZXhwZXJpbWVudGFsICovXG5leHBvcnQgY2xhc3MgRmllbGRWYWxpZGF0aW9uRXh0ZW5zaW9uIGltcGxlbWVudHMgRm9ybWx5RXh0ZW5zaW9uIHtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSBmb3JtbHlDb25maWc6IEZvcm1seUNvbmZpZykge31cblxuICBvblBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5pbml0RmllbGRWYWxpZGF0aW9uKGZpZWxkLCAndmFsaWRhdG9ycycpO1xuICAgIHRoaXMuaW5pdEZpZWxkVmFsaWRhdGlvbihmaWVsZCwgJ2FzeW5jVmFsaWRhdG9ycycpO1xuICB9XG5cbiAgcHJpdmF0ZSBpbml0RmllbGRWYWxpZGF0aW9uKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCB0eXBlOiAndmFsaWRhdG9ycycgfCAnYXN5bmNWYWxpZGF0b3JzJykge1xuICAgIGNvbnN0IHZhbGlkYXRvcnM6IFZhbGlkYXRvckZuW10gPSBbXTtcbiAgICBpZiAodHlwZSA9PT0gJ3ZhbGlkYXRvcnMnICYmICEoZmllbGQuaGFzT3duUHJvcGVydHkoJ2ZpZWxkR3JvdXAnKSAmJiAhZmllbGQua2V5KSkge1xuICAgICAgdmFsaWRhdG9ycy5wdXNoKHRoaXMuZ2V0UHJlZGVmaW5lZEZpZWxkVmFsaWRhdGlvbihmaWVsZCkpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZFt0eXBlXSkge1xuICAgICAgZm9yIChjb25zdCB2YWxpZGF0b3JOYW1lIGluIGZpZWxkW3R5cGVdKSB7XG4gICAgICAgIGlmICh2YWxpZGF0b3JOYW1lID09PSAndmFsaWRhdGlvbicgJiYgIUFycmF5LmlzQXJyYXkoZmllbGRbdHlwZV0udmFsaWRhdGlvbikpIHtcbiAgICAgICAgICBmaWVsZFt0eXBlXS52YWxpZGF0aW9uID0gW2ZpZWxkW3R5cGVdLnZhbGlkYXRpb25dO1xuICAgICAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5KCR7ZmllbGQua2V5fSk6IHBhc3NpbmcgYSBub24gYXJyYXkgdmFsdWUgdG8gdGhlICd2YWxpZGF0aW9uJyBpcyBkZXByZWNhdGVkLCBwYXNzIGFuIGFycmF5IGluc3RlYWRgKTtcbiAgICAgICAgfVxuXG4gICAgICAgIHZhbGlkYXRvck5hbWUgPT09ICd2YWxpZGF0aW9uJ1xuICAgICAgICAgID8gdmFsaWRhdG9ycy5wdXNoKC4uLmZpZWxkW3R5cGVdLnZhbGlkYXRpb24ubWFwKHYgPT4gdGhpcy53cmFwTmdWYWxpZGF0b3JGbihmaWVsZCwgdikpKVxuICAgICAgICAgIDogdmFsaWRhdG9ycy5wdXNoKHRoaXMud3JhcE5nVmFsaWRhdG9yRm4oZmllbGQsIGZpZWxkW3R5cGVdW3ZhbGlkYXRvck5hbWVdLCB2YWxpZGF0b3JOYW1lKSlcbiAgICAgICAgO1xuICAgICAgfVxuICAgIH1cblxuICAgIGRlZmluZUhpZGRlblByb3AoZmllbGQsICdfJyArIHR5cGUsIHZhbGlkYXRvcnMpO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRQcmVkZWZpbmVkRmllbGRWYWxpZGF0aW9uKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKTogVmFsaWRhdG9yRm4ge1xuICAgIGxldCBWQUxJREFUT1JTID0gW107XG4gICAgRk9STUxZX1ZBTElEQVRPUlMuZm9yRWFjaChvcHQgPT4gd3JhcFByb3BlcnR5KGZpZWxkLnRlbXBsYXRlT3B0aW9ucywgb3B0LCAoeyBjdXJyZW50VmFsdWUsIGZpcnN0Q2hhbmdlIH0pID0+IHtcbiAgICAgIFZBTElEQVRPUlMgPSBWQUxJREFUT1JTLmZpbHRlcihvID0+IG8gIT09IG9wdCk7XG4gICAgICBpZiAoY3VycmVudFZhbHVlICE9IG51bGwgJiYgY3VycmVudFZhbHVlICE9PSBmYWxzZSkge1xuICAgICAgICBWQUxJREFUT1JTLnB1c2gob3B0KTtcbiAgICAgIH1cbiAgICAgIGlmICghZmlyc3RDaGFuZ2UgJiYgZmllbGQuZm9ybUNvbnRyb2wpIHtcbiAgICAgICAgdXBkYXRlVmFsaWRpdHkoZmllbGQuZm9ybUNvbnRyb2wpO1xuICAgICAgfVxuICAgIH0pKTtcblxuICAgIHJldHVybiAoY29udHJvbDogQWJzdHJhY3RDb250cm9sKSA9PiB7XG4gICAgICBpZiAoVkFMSURBVE9SUy5sZW5ndGggPT09IDApIHtcbiAgICAgICAgcmV0dXJuIG51bGw7XG4gICAgICB9XG5cbiAgICAgIHJldHVybiBWYWxpZGF0b3JzLmNvbXBvc2UoVkFMSURBVE9SUy5tYXAob3B0ID0+ICgpID0+IHtcbiAgICAgICAgY29uc3QgdmFsdWUgPSBmaWVsZC50ZW1wbGF0ZU9wdGlvbnNbb3B0XTtcbiAgICAgICAgc3dpdGNoIChvcHQpIHtcbiAgICAgICAgICBjYXNlICdyZXF1aXJlZCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5yZXF1aXJlZChjb250cm9sKTtcbiAgICAgICAgICBjYXNlICdwYXR0ZXJuJzpcbiAgICAgICAgICAgIHJldHVybiBWYWxpZGF0b3JzLnBhdHRlcm4odmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21pbkxlbmd0aCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5taW5MZW5ndGgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21heExlbmd0aCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5tYXhMZW5ndGgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21pbic6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5taW4odmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICAgIGNhc2UgJ21heCc6XG4gICAgICAgICAgICByZXR1cm4gVmFsaWRhdG9ycy5tYXgodmFsdWUpKGNvbnRyb2wpO1xuICAgICAgICB9XG4gICAgICB9KSkoY29udHJvbCk7XG4gICAgfTtcbiAgfVxuXG4gIHByaXZhdGUgd3JhcE5nVmFsaWRhdG9yRm4oZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHZhbGlkYXRvcjogYW55LCB2YWxpZGF0b3JOYW1lPzogc3RyaW5nKSB7XG4gICAgbGV0IHZhbGlkYXRvck9wdGlvbjogVmFsaWRhdG9yT3B0aW9uID0gbnVsbDtcbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ3N0cmluZycpIHtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IGNsb25lKHRoaXMuZm9ybWx5Q29uZmlnLmdldFZhbGlkYXRvcih2YWxpZGF0b3IpKTtcbiAgICB9XG5cbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ29iamVjdCcgJiYgdmFsaWRhdG9yLm5hbWUpIHtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IGNsb25lKHRoaXMuZm9ybWx5Q29uZmlnLmdldFZhbGlkYXRvcih2YWxpZGF0b3IubmFtZSkpO1xuICAgICAgaWYgKHZhbGlkYXRvci5vcHRpb25zKSB7XG4gICAgICAgIHZhbGlkYXRvck9wdGlvbi5vcHRpb25zID0gdmFsaWRhdG9yLm9wdGlvbnM7XG4gICAgICB9XG4gICAgfVxuXG4gICAgaWYgKHR5cGVvZiB2YWxpZGF0b3IgPT09ICdvYmplY3QnICYmIHZhbGlkYXRvci5leHByZXNzaW9uKSB7XG4gICAgICBjb25zdCB7IGV4cHJlc3Npb24sIC4uLm9wdGlvbnMgfSA9IHZhbGlkYXRvcjtcbiAgICAgIHZhbGlkYXRvck9wdGlvbiA9IHtcbiAgICAgICAgbmFtZTogdmFsaWRhdG9yTmFtZSxcbiAgICAgICAgdmFsaWRhdGlvbjogZXhwcmVzc2lvbixcbiAgICAgICAgb3B0aW9uczogT2JqZWN0LmtleXMob3B0aW9ucykubGVuZ3RoID4gMCA/IG9wdGlvbnMgOiBudWxsLFxuICAgICAgfTtcbiAgICB9XG5cbiAgICBpZiAodHlwZW9mIHZhbGlkYXRvciA9PT0gJ2Z1bmN0aW9uJykge1xuICAgICAgdmFsaWRhdG9yT3B0aW9uID0ge1xuICAgICAgICBuYW1lOiB2YWxpZGF0b3JOYW1lLFxuICAgICAgICB2YWxpZGF0aW9uOiB2YWxpZGF0b3IsXG4gICAgICB9O1xuICAgIH1cblxuICAgIHJldHVybiAoY29udHJvbDogQWJzdHJhY3RDb250cm9sKSA9PiB7XG4gICAgICBjb25zdCBlcnJvcnM6IGFueSA9IHZhbGlkYXRvck9wdGlvbi52YWxpZGF0aW9uKGNvbnRyb2wsIGZpZWxkLCB2YWxpZGF0b3JPcHRpb24ub3B0aW9ucyk7XG4gICAgICBpZiAoaXNQcm9taXNlKGVycm9ycykpIHtcbiAgICAgICAgcmV0dXJuIGVycm9ycy50aGVuKHYgPT4gdGhpcy5oYW5kbGVBc3luY1Jlc3VsdChmaWVsZCwgdmFsaWRhdG9yTmFtZSA/ICEhdiA6IHYsIHZhbGlkYXRvck9wdGlvbikpO1xuICAgICAgfVxuXG4gICAgICBpZiAoaXNPYnNlcnZhYmxlKGVycm9ycykgJiYgIXZhbGlkYXRvck5hbWUpIHtcbiAgICAgICAgcmV0dXJuIGVycm9ycy5waXBlKG1hcCh2ID0+IHRoaXMuaGFuZGxlQXN5bmNSZXN1bHQoZmllbGQsIHYsIHZhbGlkYXRvck9wdGlvbikpKTtcbiAgICAgIH1cblxuICAgICAgcmV0dXJuIHRoaXMuaGFuZGxlUmVzdWx0KGZpZWxkLCB2YWxpZGF0b3JOYW1lID8gISFlcnJvcnMgOiBlcnJvcnMsIHZhbGlkYXRvck9wdGlvbik7XG4gICAgfTtcbiAgfVxuXG4gIHByaXZhdGUgaGFuZGxlQXN5bmNSZXN1bHQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGVycm9yczogYW55LCBvcHRpb25zOiBWYWxpZGF0b3JPcHRpb24pIHtcbiAgICAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9hbmd1bGFyL2lzc3Vlcy8xMzIwMFxuICAgIGlmIChmaWVsZC5vcHRpb25zICYmIGZpZWxkLm9wdGlvbnMuX21hcmtGb3JDaGVjaykge1xuICAgICAgZmllbGQub3B0aW9ucy5fbWFya0ZvckNoZWNrKGZpZWxkKTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy5oYW5kbGVSZXN1bHQoZmllbGQsIGVycm9ycywgb3B0aW9ucyk7XG4gIH1cblxuICBwcml2YXRlIGhhbmRsZVJlc3VsdChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgZXJyb3JzOiBhbnksIHsgbmFtZSwgb3B0aW9ucyB9OiBWYWxpZGF0b3JPcHRpb24pIHtcbiAgICBpZiAodHlwZW9mIGVycm9ycyA9PT0gJ2Jvb2xlYW4nKSB7XG4gICAgICBlcnJvcnMgPSBlcnJvcnMgPyBudWxsIDogeyBbbmFtZV06IG9wdGlvbnMgPyBvcHRpb25zIDogdHJ1ZSB9O1xuICAgIH1cblxuICAgIGNvbnN0IGN0cmwgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgICBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXSAmJiBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXVtuYW1lXSAmJiBjdHJsWydfY2hpbGRyZW5FcnJvcnMnXVtuYW1lXSgpO1xuXG4gICAgaWYgKGVycm9ycyAmJiBlcnJvcnNbbmFtZV0pIHtcbiAgICAgIGNvbnN0IGVycm9yUGF0aCA9IGVycm9yc1tuYW1lXS5lcnJvclBhdGhcbiAgICAgICAgPyBlcnJvcnNbbmFtZV0uZXJyb3JQYXRoXG4gICAgICAgIDogKG9wdGlvbnMgfHwge30pLmVycm9yUGF0aDtcblxuICAgICAgY29uc3QgY2hpbGRDdHJsID0gZXJyb3JQYXRoID8gZmllbGQuZm9ybUNvbnRyb2wuZ2V0KGVycm9yUGF0aCkgOiBudWxsO1xuICAgICAgaWYgKGNoaWxkQ3RybCkge1xuICAgICAgICBjb25zdCB7IGVycm9yUGF0aCwgLi4ub3B0cyB9ID0gZXJyb3JzW25hbWVdO1xuICAgICAgICBjaGlsZEN0cmwuc2V0RXJyb3JzKHsgLi4uKGNoaWxkQ3RybC5lcnJvcnMgfHwge30pLCBbbmFtZV06IG9wdHMgfSk7XG5cbiAgICAgICAgIWN0cmxbJ19jaGlsZHJlbkVycm9ycyddICYmIGRlZmluZUhpZGRlblByb3AoY3RybCwgJ19jaGlsZHJlbkVycm9ycycsIHt9KTtcbiAgICAgICAgY3RybFsnX2NoaWxkcmVuRXJyb3JzJ11bbmFtZV0gPSAoKSA9PiB7XG4gICAgICAgICAgY29uc3QgeyBbbmFtZV06IHRvRGVsZXRlLCAuLi5jaGlsZEVycm9ycyB9ID0gY2hpbGRDdHJsLmVycm9ycyB8fCB7fTtcbiAgICAgICAgICBjaGlsZEN0cmwuc2V0RXJyb3JzKE9iamVjdC5rZXlzKGNoaWxkRXJyb3JzKS5sZW5ndGggPT09IDAgPyBudWxsIDogY2hpbGRFcnJvcnMpO1xuICAgICAgICB9O1xuICAgICAgfVxuICAgIH1cblxuICAgIHJldHVybiBlcnJvcnM7XG4gIH1cbn1cbiJdfQ==