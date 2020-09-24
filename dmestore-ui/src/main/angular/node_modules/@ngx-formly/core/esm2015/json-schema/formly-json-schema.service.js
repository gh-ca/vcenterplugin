/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ɵreverseDeepMerge as reverseDeepMerge, ɵgetFieldInitialValue as getFieldInitialValue, } from '@ngx-formly/core';
import * as i0 from "@angular/core";
/**
 * @record
 */
export function FormlyJsonschemaOptions() { }
if (false) {
    /**
     * allows to intercept the mapping, taking the already mapped
     * formly field and the original JSONSchema source from which it
     * was mapped.
     * @type {?|undefined}
     */
    FormlyJsonschemaOptions.prototype.map;
}
/**
 * @param {?} v
 * @return {?}
 */
function isEmpty(v) {
    return v === '' || v === undefined || v === null;
}
/**
 * @param {?} schema
 * @return {?}
 */
function isConst(schema) {
    return schema.hasOwnProperty('const') || (schema.enum && schema.enum.length === 1);
}
/**
 * @param {?} field
 * @return {?}
 */
function totalMatchedFields(field) {
    if (field.key && !field.fieldGroup) {
        return getFieldInitialValue(field) !== undefined ? 1 : 0;
    }
    return field.fieldGroup.reduce((/**
     * @param {?} s
     * @param {?} f
     * @return {?}
     */
    (s, f) => totalMatchedFields(f) + s), 0);
}
/**
 * @record
 */
function IOptions() { }
if (false) {
    /** @type {?} */
    IOptions.prototype.schema;
    /** @type {?|undefined} */
    IOptions.prototype.autoClear;
}
export class FormlyJsonschema {
    /**
     * @param {?} schema
     * @param {?=} options
     * @return {?}
     */
    toFieldConfig(schema, options) {
        return this._toFieldConfig(schema, Object.assign({ schema }, (options || {})));
    }
    /**
     * @private
     * @param {?} schema
     * @param {?} options
     * @return {?}
     */
    _toFieldConfig(schema, options) {
        schema = this.resolveSchema(schema, options);
        /** @type {?} */
        let field = {
            type: this.guessType(schema),
            defaultValue: schema.default,
            templateOptions: {
                label: schema.title,
                readonly: schema.readOnly,
                description: schema.description,
            },
        };
        if (options.autoClear) {
            field['autoClear'] = true;
        }
        switch (field.type) {
            case 'null': {
                this.addValidator(field, 'null', (/**
                 * @param {?} __0
                 * @return {?}
                 */
                ({ value }) => value === null));
                break;
            }
            case 'number':
            case 'integer': {
                field.parsers = [(/**
                     * @param {?} v
                     * @return {?}
                     */
                    v => isEmpty(v) ? null : Number(v))];
                if (schema.hasOwnProperty('minimum')) {
                    field.templateOptions.min = schema.minimum;
                }
                if (schema.hasOwnProperty('maximum')) {
                    field.templateOptions.max = schema.maximum;
                }
                if (schema.hasOwnProperty('exclusiveMinimum')) {
                    field.templateOptions.exclusiveMinimum = schema.exclusiveMinimum;
                    this.addValidator(field, 'exclusiveMinimum', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => isEmpty(value) || (value > schema.exclusiveMinimum)));
                }
                if (schema.hasOwnProperty('exclusiveMaximum')) {
                    field.templateOptions.exclusiveMaximum = schema.exclusiveMaximum;
                    this.addValidator(field, 'exclusiveMaximum', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => isEmpty(value) || (value < schema.exclusiveMaximum)));
                }
                if (schema.hasOwnProperty('multipleOf')) {
                    field.templateOptions.step = schema.multipleOf;
                    this.addValidator(field, 'multipleOf', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => isEmpty(value) || (value % schema.multipleOf === 0)));
                }
                break;
            }
            case 'string': {
                /** @type {?} */
                const schemaType = (/** @type {?} */ (schema.type));
                if (Array.isArray(schemaType) && (schemaType.indexOf('null') !== -1)) {
                    field.parsers = [(/**
                         * @param {?} v
                         * @return {?}
                         */
                        v => isEmpty(v) ? null : v)];
                }
                ['minLength', 'maxLength', 'pattern'].forEach((/**
                 * @param {?} prop
                 * @return {?}
                 */
                prop => {
                    if (schema.hasOwnProperty(prop)) {
                        field.templateOptions[prop] = schema[prop];
                    }
                }));
                break;
            }
            case 'object': {
                field.fieldGroup = [];
                const [propDeps, schemaDeps] = this.resolveDependencies(schema);
                Object.keys(schema.properties || {}).forEach((/**
                 * @param {?} key
                 * @return {?}
                 */
                key => {
                    /** @type {?} */
                    const f = this._toFieldConfig((/** @type {?} */ (schema.properties[key])), options);
                    field.fieldGroup.push(f);
                    f.key = key;
                    if (Array.isArray(schema.required) && schema.required.indexOf(key) !== -1) {
                        f.templateOptions.required = true;
                    }
                    if (f.templateOptions && !f.templateOptions.required && propDeps[key]) {
                        f.expressionProperties = {
                            'templateOptions.required': (/**
                             * @param {?} m
                             * @return {?}
                             */
                            m => m && propDeps[key].some((/**
                             * @param {?} k
                             * @return {?}
                             */
                            k => !isEmpty(m[k])))),
                        };
                    }
                    if (schemaDeps[key]) {
                        /** @type {?} */
                        const getConstValue = (/**
                         * @param {?} s
                         * @return {?}
                         */
                        (s) => {
                            return s.hasOwnProperty('const') ? s.const : s.enum[0];
                        });
                        /** @type {?} */
                        const oneOfSchema = schemaDeps[key].oneOf;
                        if (oneOfSchema
                            && oneOfSchema.every((/**
                             * @param {?} o
                             * @return {?}
                             */
                            o => o.properties && o.properties[key] && isConst(o.properties[key])))) {
                            oneOfSchema.forEach((/**
                             * @param {?} oneOfSchemaItem
                             * @return {?}
                             */
                            oneOfSchemaItem => {
                                const _a = oneOfSchemaItem.properties, _b = key, constSchema = _a[_b], properties = tslib_1.__rest(_a, [typeof _b === "symbol" ? _b : _b + ""]);
                                field.fieldGroup.push(Object.assign({}, this._toFieldConfig(Object.assign({}, oneOfSchemaItem, { properties }), Object.assign({}, options, { autoClear: true })), { hideExpression: (/**
                                     * @param {?} m
                                     * @return {?}
                                     */
                                    m => !m || getConstValue(constSchema) !== m[key]) }));
                            }));
                        }
                        else {
                            field.fieldGroup.push(Object.assign({}, this._toFieldConfig(schemaDeps[key], options), { hideExpression: (/**
                                 * @param {?} m
                                 * @return {?}
                                 */
                                m => !m || isEmpty(m[key])) }));
                        }
                    }
                }));
                if (schema.oneOf) {
                    field.fieldGroup.push(this.resolveMultiSchema('oneOf', (/** @type {?} */ (schema.oneOf)), options));
                }
                if (schema.anyOf) {
                    field.fieldGroup.push(this.resolveMultiSchema('anyOf', (/** @type {?} */ (schema.anyOf)), options));
                }
                break;
            }
            case 'array': {
                if (schema.hasOwnProperty('minItems')) {
                    field.templateOptions.minItems = schema.minItems;
                    this.addValidator(field, 'minItems', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => isEmpty(value) || (value.length >= schema.minItems)));
                }
                if (schema.hasOwnProperty('maxItems')) {
                    field.templateOptions.maxItems = schema.maxItems;
                    this.addValidator(field, 'maxItems', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => isEmpty(value) || (value.length <= schema.maxItems)));
                }
                if (schema.hasOwnProperty('uniqueItems')) {
                    field.templateOptions.uniqueItems = schema.uniqueItems;
                    this.addValidator(field, 'uniqueItems', (/**
                     * @param {?} __0
                     * @return {?}
                     */
                    ({ value }) => {
                        if (isEmpty(value) || !schema.uniqueItems) {
                            return true;
                        }
                        /** @type {?} */
                        const uniqueItems = Array.from(new Set(value.map((/**
                         * @param {?} v
                         * @return {?}
                         */
                        (v) => JSON.stringify(v)))));
                        return uniqueItems.length === value.length;
                    }));
                }
                // resolve items schema needed for isEnum check
                if (schema.items && !Array.isArray(schema.items)) {
                    schema.items = this.resolveSchema((/** @type {?} */ (schema.items)), options);
                }
                // TODO: remove isEnum check once adding an option to skip extension
                if (!this.isEnum(schema)) {
                    /** @type {?} */
                    const _this = this;
                    Object.defineProperty(field, 'fieldArray', {
                        get: (/**
                         * @return {?}
                         */
                        function () {
                            if (!Array.isArray(schema.items)) {
                                // When items is a single schema, the additionalItems keyword is meaningless, and it should not be used.
                                return _this._toFieldConfig((/** @type {?} */ (schema.items)), options);
                            }
                            /** @type {?} */
                            const length = this.fieldGroup ? this.fieldGroup.length : 0;
                            /** @type {?} */
                            const itemSchema = schema.items[length]
                                ? schema.items[length]
                                : schema.additionalItems;
                            return itemSchema
                                ? _this._toFieldConfig((/** @type {?} */ (itemSchema)), options)
                                : {};
                        }),
                        enumerable: true,
                        configurable: true,
                    });
                }
                break;
            }
        }
        if (schema.hasOwnProperty('const')) {
            field.templateOptions.const = schema.const;
            this.addValidator(field, 'const', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ value }) => value === schema.const));
            if (!field.type) {
                field.defaultValue = schema.const;
            }
        }
        if (this.isEnum(schema)) {
            field.templateOptions.multiple = field.type === 'array';
            field.type = 'enum';
            field.templateOptions.options = this.toEnumOptions(schema);
        }
        // map in possible formlyConfig options from the widget property
        if (schema['widget'] && schema['widget'].formlyConfig) {
            field = reverseDeepMerge(schema['widget'].formlyConfig, field);
        }
        // if there is a map function passed in, use it to allow the user to
        // further customize how fields are being mapped
        return options.map ? options.map(field, schema) : field;
    }
    /**
     * @private
     * @param {?} schema
     * @param {?} options
     * @return {?}
     */
    resolveSchema(schema, options) {
        if (schema.$ref) {
            schema = this.resolveDefinition(schema, options);
        }
        if (schema.allOf) {
            schema = this.resolveAllOf(schema, options);
        }
        return schema;
    }
    /**
     * @private
     * @param {?} __0
     * @param {?} options
     * @return {?}
     */
    resolveAllOf(_a, options) {
        var { allOf } = _a, baseSchema = tslib_1.__rest(_a, ["allOf"]);
        if (!allOf.length) {
            throw Error(`allOf array can not be empty ${allOf}.`);
        }
        return allOf.reduce((/**
         * @param {?} base
         * @param {?} schema
         * @return {?}
         */
        (base, schema) => {
            schema = this.resolveSchema(schema, options);
            if (base.required && schema.required) {
                base.required = [...base.required, ...schema.required];
            }
            if (schema.uniqueItems) {
                base.uniqueItems = schema.uniqueItems;
            }
            // resolve to min value
            ['maxLength', 'maximum', 'exclusiveMaximum', 'maxItems', 'maxProperties']
                .forEach((/**
             * @param {?} prop
             * @return {?}
             */
            prop => {
                if (!isEmpty(base[prop]) && !isEmpty(schema[prop])) {
                    base[prop] = base[prop] < schema[prop] ? base[prop] : schema[prop];
                }
            }));
            // resolve to max value
            ['minLength', 'minimum', 'exclusiveMinimum', 'minItems', 'minProperties']
                .forEach((/**
             * @param {?} prop
             * @return {?}
             */
            prop => {
                if (!isEmpty(base[prop]) && !isEmpty(schema[prop])) {
                    base[prop] = base[prop] > schema[prop] ? base[prop] : schema[prop];
                }
            }));
            return reverseDeepMerge(base, schema);
        }), baseSchema);
    }
    /**
     * @private
     * @param {?} mode
     * @param {?} schemas
     * @param {?} options
     * @return {?}
     */
    resolveMultiSchema(mode, schemas, options) {
        return {
            type: 'multischema',
            fieldGroup: [
                {
                    type: 'enum',
                    templateOptions: {
                        multiple: mode === 'anyOf',
                        options: schemas
                            .map((/**
                         * @param {?} s
                         * @param {?} i
                         * @return {?}
                         */
                        (s, i) => ({ label: s.title, value: i }))),
                    },
                },
                {
                    fieldGroup: schemas.map((/**
                     * @param {?} s
                     * @param {?} i
                     * @return {?}
                     */
                    (s, i) => (Object.assign({}, this._toFieldConfig(s, Object.assign({}, options, { autoClear: true })), { hideExpression: (/**
                         * @param {?} m
                         * @param {?} fs
                         * @param {?} f
                         * @return {?}
                         */
                        (m, fs, f) => {
                            /** @type {?} */
                            const selectField = f.parent.parent.fieldGroup[0];
                            if (!selectField.formControl) {
                                /** @type {?} */
                                const value = f.parent.fieldGroup
                                    .map((/**
                                 * @param {?} f
                                 * @param {?} i
                                 * @return {?}
                                 */
                                (f, i) => (/** @type {?} */ ([f, i]))))
                                    .filter((/**
                                 * @param {?} __0
                                 * @return {?}
                                 */
                                ([f, i]) => this.isFieldValid(f, schemas[i], options)))
                                    .sort((/**
                                 * @param {?} __0
                                 * @param {?} __1
                                 * @return {?}
                                 */
                                ([f1], [f2]) => {
                                    /** @type {?} */
                                    const matchedFields1 = totalMatchedFields(f1);
                                    /** @type {?} */
                                    const matchedFields2 = totalMatchedFields(f2);
                                    if (matchedFields1 === matchedFields2) {
                                        return 0;
                                    }
                                    return matchedFields2 > matchedFields1 ? 1 : -1;
                                }))
                                    .map((/**
                                 * @param {?} __0
                                 * @return {?}
                                 */
                                ([, i]) => i));
                                /** @type {?} */
                                const normalizedValue = [value.length === 0 ? 0 : value[0]];
                                /** @type {?} */
                                const formattedValue = mode === 'anyOf' ? normalizedValue : normalizedValue[0];
                                selectField.formControl = new FormControl(formattedValue);
                            }
                            /** @type {?} */
                            const control = selectField.formControl;
                            return Array.isArray(control.value)
                                ? control.value.indexOf(i) === -1
                                : control.value !== i;
                        }) })))),
                },
            ],
        };
    }
    /**
     * @private
     * @param {?} schema
     * @param {?} options
     * @return {?}
     */
    resolveDefinition(schema, options) {
        const [uri, pointer] = schema.$ref.split('#/');
        if (uri) {
            throw Error(`Remote schemas for ${schema.$ref} not supported yet.`);
        }
        /** @type {?} */
        const definition = !pointer ? null : pointer.split('/').reduce((/**
         * @param {?} def
         * @param {?} path
         * @return {?}
         */
        (def, path) => def && def.hasOwnProperty(path) ? def[path] : null), options.schema);
        if (!definition) {
            throw Error(`Cannot find a definition for ${schema.$ref}.`);
        }
        if (definition.$ref) {
            return this.resolveDefinition(definition, options);
        }
        return Object.assign({}, definition, ['title', 'description', 'default'].reduce((/**
         * @param {?} annotation
         * @param {?} p
         * @return {?}
         */
        (annotation, p) => {
            if (schema.hasOwnProperty(p)) {
                annotation[p] = schema[p];
            }
            return annotation;
        }), {}));
    }
    /**
     * @private
     * @param {?} schema
     * @return {?}
     */
    resolveDependencies(schema) {
        /** @type {?} */
        const deps = {};
        /** @type {?} */
        const schemaDeps = {};
        Object.keys(schema.dependencies || {}).forEach((/**
         * @param {?} prop
         * @return {?}
         */
        prop => {
            /** @type {?} */
            const dependency = (/** @type {?} */ (schema.dependencies[prop]));
            if (Array.isArray(dependency)) {
                // Property dependencies
                dependency.forEach((/**
                 * @param {?} dep
                 * @return {?}
                 */
                dep => {
                    if (!deps[dep]) {
                        deps[dep] = [prop];
                    }
                    else {
                        deps[dep].push(prop);
                    }
                }));
            }
            else {
                // schema dependencies
                schemaDeps[prop] = dependency;
            }
        }));
        return [deps, schemaDeps];
    }
    /**
     * @private
     * @param {?} schema
     * @return {?}
     */
    guessType(schema) {
        /** @type {?} */
        const type = (/** @type {?} */ (schema.type));
        if (!type && schema.properties) {
            return 'object';
        }
        if (Array.isArray(type)) {
            if (type.length === 1) {
                return type[0];
            }
            if (type.length === 2 && type.indexOf('null') !== -1) {
                return type[type[0] === 'null' ? 1 : 0];
            }
        }
        return type;
    }
    /**
     * @private
     * @param {?} field
     * @param {?} name
     * @param {?} validator
     * @return {?}
     */
    addValidator(field, name, validator) {
        field.validators = field.validators || {};
        field.validators[name] = validator;
    }
    /**
     * @private
     * @param {?} schema
     * @return {?}
     */
    isEnum(schema) {
        return schema.enum
            || (schema.anyOf && schema.anyOf.every(isConst))
            || (schema.oneOf && schema.oneOf.every(isConst))
            || schema.uniqueItems && schema.items && !Array.isArray(schema.items) && this.isEnum((/** @type {?} */ (schema.items)));
    }
    /**
     * @private
     * @param {?} schema
     * @return {?}
     */
    toEnumOptions(schema) {
        if (schema.enum) {
            return schema.enum.map((/**
             * @param {?} value
             * @return {?}
             */
            value => ({ value, label: value })));
        }
        /** @type {?} */
        const toEnum = (/**
         * @param {?} s
         * @return {?}
         */
        (s) => {
            /** @type {?} */
            const value = s.hasOwnProperty('const') ? s.const : s.enum[0];
            return { value: value, label: s.title || value };
        });
        if (schema.anyOf) {
            return schema.anyOf.map(toEnum);
        }
        if (schema.oneOf) {
            return schema.oneOf.map(toEnum);
        }
        return this.toEnumOptions((/** @type {?} */ (schema.items)));
    }
    /**
     * @private
     * @param {?} field
     * @param {?} schema
     * @param {?} options
     * @return {?}
     */
    isFieldValid(field, schema, options) {
        const { form } = ((/** @type {?} */ (field.options)))._buildField({
            form: new FormGroup({}),
            fieldGroup: [this._toFieldConfig(schema, options)],
            model: field.model,
        });
        return form.valid;
    }
}
FormlyJsonschema.decorators = [
    { type: Injectable, args: [{ providedIn: 'root' },] }
];
/** @nocollapse */ FormlyJsonschema.ngInjectableDef = i0.defineInjectable({ factory: function FormlyJsonschema_Factory() { return new FormlyJsonschema(); }, token: FormlyJsonschema, providedIn: "root" });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LWpzb24tc2NoZW1hLnNlcnZpY2UuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlL2pzb24tc2NoZW1hLyIsInNvdXJjZXMiOlsiZm9ybWx5LWpzb24tc2NoZW1hLnNlcnZpY2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsVUFBVSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBRzNDLE9BQU8sRUFBbUIsV0FBVyxFQUFFLFNBQVMsRUFBRSxNQUFNLGdCQUFnQixDQUFDO0FBQ3pFLE9BQU8sRUFDTCxpQkFBaUIsSUFBSSxnQkFBZ0IsRUFDckMscUJBQXFCLElBQUksb0JBQW9CLEdBQzlDLE1BQU0sa0JBQWtCLENBQUM7Ozs7O0FBRTFCLDZDQU9DOzs7Ozs7OztJQURDLHNDQUFvRjs7Ozs7O0FBR3RGLFNBQVMsT0FBTyxDQUFDLENBQU07SUFDckIsT0FBTyxDQUFDLEtBQUssRUFBRSxJQUFJLENBQUMsS0FBSyxTQUFTLElBQUksQ0FBQyxLQUFLLElBQUksQ0FBQztBQUNuRCxDQUFDOzs7OztBQUVELFNBQVMsT0FBTyxDQUFDLE1BQW1CO0lBQ2xDLE9BQU8sTUFBTSxDQUFDLGNBQWMsQ0FBQyxPQUFPLENBQUMsSUFBSSxDQUFDLE1BQU0sQ0FBQyxJQUFJLElBQUksTUFBTSxDQUFDLElBQUksQ0FBQyxNQUFNLEtBQUssQ0FBQyxDQUFDLENBQUM7QUFDckYsQ0FBQzs7Ozs7QUFFRCxTQUFTLGtCQUFrQixDQUFDLEtBQXdCO0lBQ2xELElBQUksS0FBSyxDQUFDLEdBQUcsSUFBSSxDQUFDLEtBQUssQ0FBQyxVQUFVLEVBQUU7UUFDbEMsT0FBTyxvQkFBb0IsQ0FBQyxLQUFLLENBQUMsS0FBSyxTQUFTLENBQUMsQ0FBQyxDQUFDLENBQUMsQ0FBQyxDQUFDLENBQUMsQ0FBQyxDQUFDO0tBQzFEO0lBRUQsT0FBTyxLQUFLLENBQUMsVUFBVSxDQUFDLE1BQU07Ozs7O0lBQUMsQ0FBQyxDQUFDLEVBQUUsQ0FB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Q2hCLE9BQU8sTUFBTSxDQUFDLEtBQUssQ0FBQyxHQUFHLENBQUMsTUFBTSxDQUFDLENBQUM7U0FDakM7UUFFRCxPQUFPLElBQUksQ0FBQyxhQUFhLENBQUMsbUJBQWMsTUFBTSxDQUFDLEtBQUssRUFBQSxDQUFDLENBQUM7SUFDeEQsQ0FBQzs7Ozs7Ozs7SUFFTyxZQUFZLENBQUMsS0FBd0IsRUFBRSxNQUFtQixFQUFFLE9BQWlCO2NBQzdFLEVBQUUsSUFBSSxFQUFFLEdBQUcsQ0FBQyxtQkFBQSxLQUFLLENBQUMsT0FBTyxFQUFPLENBQUMsQ0FBQyxXQUFXLENBQUM7WUFDbEQsSUFBSSxFQUFFLElBQUksU0FBUyxDQUFDLEVBQUUsQ0FBQztZQUN2QixVQUFVLEVBQUUsQ0FBQyxJQUFJLENBQUMsY0FBYyxDQUFDLE1BQU0sRUFBRSxPQUFPLENBQUMsQ0FBQztZQUNsRCxLQUFLLEVBQUUsS0FBSyxDQUFDLEtBQUs7U0FDbkIsQ0FBQztRQUVGLE9BQU8sSUFBSSxDQUFDLEtBQUssQ0FBQztJQUNwQixDQUFDOzs7WUEzYUYsVUFBVSxTQUFDLEVBQUUsVUFBVSxFQUFFLE1BQU0sRUFBRSIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEluamVjdGFibGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnIH0gZnJvbSAnQG5neC1mb3JtbHkvY29yZSc7XG5pbXBvcnQgeyBKU09OU2NoZW1hNywgSlNPTlNjaGVtYTdUeXBlTmFtZSB9IGZyb20gJ2pzb24tc2NoZW1hJztcbmltcG9ydCB7IEFic3RyYWN0Q29udHJvbCwgRm9ybUNvbnRyb2wsIEZvcm1Hcm91cCB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7XG4gIMm1cmV2ZXJzZURlZXBNZXJnZSBhcyByZXZlcnNlRGVlcE1lcmdlLFxuICDJtWdldEZpZWxkSW5pdGlhbFZhbHVlIGFzIGdldEZpZWxkSW5pdGlhbFZhbHVlLFxufSBmcm9tICdAbmd4LWZvcm1seS9jb3JlJztcblxuZXhwb3J0IGludGVyZmFjZSBGb3JtbHlKc29uc2NoZW1hT3B0aW9ucyB7XG4gIC8qKlxuICAgKiBhbGxvd3MgdG8gaW50ZXJjZXB0IHRoZSBtYXBwaW5nLCB0YWtpbmcgdGhlIGFscmVhZHkgbWFwcGVkXG4gICAqIGZvcm1seSBmaWVsZCBhbmQgdGhlIG9yaWdpbmFsIEpTT05TY2hlbWEgc291cmNlIGZyb20gd2hpY2ggaXRcbiAgICogd2FzIG1hcHBlZC5cbiAgICovXG4gIG1hcD86IChtYXBwZWRGaWVsZDogRm9ybWx5RmllbGRDb25maWcsIG1hcFNvdXJjZTogSlNPTlNjaGVtYTcpID0+IEZvcm1seUZpZWxkQ29uZmlnO1xufVxuXG5mdW5jdGlvbiBpc0VtcHR5KHY6IGFueSkge1xuICByZXR1cm4gdiA9PT0gJycgfHwgdiA9PT0gdW5kZWZpbmVkIHx8IHYgPT09IG51bGw7XG59XG5cbmZ1bmN0aW9uIGlzQ29uc3Qoc2NoZW1hOiBKU09OU2NoZW1hNykge1xuICByZXR1cm4gc2NoZW1hLmhhc093blByb3BlcnR5KCdjb25zdCcpIHx8IChzY2hlbWEuZW51bSAmJiBzY2hlbWEuZW51bS5sZW5ndGggPT09IDEpO1xufVxuXG5mdW5jdGlvbiB0b3RhbE1hdGNoZWRGaWVsZHMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKTogbnVtYmVyIHtcbiAgaWYgKGZpZWxkLmtleSAmJiAhZmllbGQuZmllbGRHcm91cCkge1xuICAgIHJldHVybiBnZXRGaWVsZEluaXRpYWxWYWx1ZShmaWVsZCkgIT09IHVuZGVmaW5lZCA/IDEgOiAwO1xuICB9XG5cbiAgcmV0dXJuIGZpZWxkLmZpZWxkR3JvdXAucmVkdWNlKChzLCBmKSA9PiB0b3RhbE1hdGNoZWRGaWVsZHMoZikgKyBzLCAwKTtcbn1cblxuaW50ZXJmYWNlIElPcHRpb25zIGV4dGVuZHMgRm9ybWx5SnNvbnNjaGVtYU9wdGlvbnMge1xuICBzY2hlbWE6IEpTT05TY2hlbWE3O1xuICBhdXRvQ2xlYXI/OiBib29sZWFuO1xufVxuXG5ASW5qZWN0YWJsZSh7IHByb3ZpZGVkSW46ICdyb290JyB9KVxuZXhwb3J0IGNsYXNzIEZvcm1seUpzb25zY2hlbWEge1xuICB0b0ZpZWxkQ29uZmlnKHNjaGVtYTogSlNPTlNjaGVtYTcsIG9wdGlvbnM/OiBGb3JtbHlKc29uc2NoZW1hT3B0aW9ucyk6IEZvcm1seUZpZWxkQ29uZmlnIHtcbiAgICByZXR1cm4gdGhpcy5fdG9GaWVsZENvbmZpZyhzY2hlbWEsIHsgc2NoZW1hLCAuLi4ob3B0aW9ucyB8fCB7fSkgfSk7XG4gIH1cblxuICBwcml2YXRlIF90b0ZpZWxkQ29uZmlnKHNjaGVtYTogSlNPTlNjaGVtYTcsIG9wdGlvbnM6IElPcHRpb25zKTogRm9ybWx5RmllbGRDb25maWcge1xuICAgIHNjaGVtYSA9IHRoaXMucmVzb2x2ZVNjaGVtYShzY2hlbWEsIG9wdGlvbnMpO1xuXG4gICAgbGV0IGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZyA9IHtcbiAgICAgIHR5cGU6IHRoaXMuZ3Vlc3NUeXBlKHNjaGVtYSksXG4gICAgICBkZWZhdWx0VmFsdWU6IHNjaGVtYS5kZWZhdWx0LFxuICAgICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICAgIGxhYmVsOiBzY2hlbWEudGl0bGUsXG4gICAgICAgIHJlYWRvbmx5OiBzY2hlbWEucmVhZE9ubHksXG4gICAgICAgIGRlc2NyaXB0aW9uOiBzY2hlbWEuZGVzY3JpcHRpb24sXG4gICAgICB9LFxuICAgIH07XG5cbiAgICBpZiAob3B0aW9ucy5hdXRvQ2xlYXIpIHtcbiAgICAgIGZpZWxkWydhdXRvQ2xlYXInXSA9IHRydWU7XG4gICAgfVxuXG4gICAgc3dpdGNoIChmaWVsZC50eXBlKSB7XG4gICAgICBjYXNlICdudWxsJzoge1xuICAgICAgICB0aGlzLmFkZFZhbGlkYXRvcihmaWVsZCwgJ251bGwnLCAoeyB2YWx1ZSB9KSA9PiB2YWx1ZSA9PT0gbnVsbCk7XG4gICAgICAgIGJyZWFrO1xuICAgICAgfVxuICAgICAgY2FzZSAnbnVtYmVyJzpcbiAgICAgIGNhc2UgJ2ludGVnZXInOiB7XG4gICAgICAgIGZpZWxkLnBhcnNlcnMgPSBbdiA9PiBpc0VtcHR5KHYpID8gbnVsbCA6IE51bWJlcih2KV07XG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ21pbmltdW0nKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5taW4gPSBzY2hlbWEubWluaW11bTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ21heGltdW0nKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5tYXggPSBzY2hlbWEubWF4aW11bTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ2V4Y2x1c2l2ZU1pbmltdW0nKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5leGNsdXNpdmVNaW5pbXVtID0gc2NoZW1hLmV4Y2x1c2l2ZU1pbmltdW07XG4gICAgICAgICAgdGhpcy5hZGRWYWxpZGF0b3IoZmllbGQsICdleGNsdXNpdmVNaW5pbXVtJywgKHsgdmFsdWUgfSkgPT4gaXNFbXB0eSh2YWx1ZSkgfHwgKHZhbHVlID4gc2NoZW1hLmV4Y2x1c2l2ZU1pbmltdW0pKTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ2V4Y2x1c2l2ZU1heGltdW0nKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5leGNsdXNpdmVNYXhpbXVtID0gc2NoZW1hLmV4Y2x1c2l2ZU1heGltdW07XG4gICAgICAgICAgdGhpcy5hZGRWYWxpZGF0b3IoZmllbGQsICdleGNsdXNpdmVNYXhpbXVtJywgKHsgdmFsdWUgfSkgPT4gaXNFbXB0eSh2YWx1ZSkgfHwgKHZhbHVlIDwgc2NoZW1hLmV4Y2x1c2l2ZU1heGltdW0pKTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ211bHRpcGxlT2YnKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5zdGVwID0gc2NoZW1hLm11bHRpcGxlT2Y7XG4gICAgICAgICAgdGhpcy5hZGRWYWxpZGF0b3IoZmllbGQsICdtdWx0aXBsZU9mJywgKHsgdmFsdWUgfSkgPT4gaXNFbXB0eSh2YWx1ZSkgfHwgKHZhbHVlICUgc2NoZW1hLm11bHRpcGxlT2YgPT09IDApKTtcbiAgICAgICAgfVxuICAgICAgICBicmVhaztcbiAgICAgIH1cbiAgICAgIGNhc2UgJ3N0cmluZyc6IHtcbiAgICAgICAgY29uc3Qgc2NoZW1hVHlwZSA9IHNjaGVtYS50eXBlIGFzIEpTT05TY2hlbWE3VHlwZU5hbWU7XG4gICAgICAgIGlmIChBcnJheS5pc0FycmF5KHNjaGVtYVR5cGUpICYmIChzY2hlbWFUeXBlLmluZGV4T2YoJ251bGwnKSAhPT0gLTEpKSB7XG4gICAgICAgICAgZmllbGQucGFyc2VycyA9IFt2ID0+IGlzRW1wdHkodikgPyBudWxsIDogdl07XG4gICAgICAgIH1cblxuICAgICAgICBbJ21pbkxlbmd0aCcsICdtYXhMZW5ndGgnLCAncGF0dGVybiddLmZvckVhY2gocHJvcCA9PiB7XG4gICAgICAgICAgaWYgKHNjaGVtYS5oYXNPd25Qcm9wZXJ0eShwcm9wKSkge1xuICAgICAgICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zW3Byb3BdID0gc2NoZW1hW3Byb3BdO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICAgIGJyZWFrO1xuICAgICAgfVxuICAgICAgY2FzZSAnb2JqZWN0Jzoge1xuICAgICAgICBmaWVsZC5maWVsZEdyb3VwID0gW107XG5cbiAgICAgICAgY29uc3QgW3Byb3BEZXBzLCBzY2hlbWFEZXBzXSA9IHRoaXMucmVzb2x2ZURlcGVuZGVuY2llcyhzY2hlbWEpO1xuICAgICAgICBPYmplY3Qua2V5cyhzY2hlbWEucHJvcGVydGllcyB8fCB7fSkuZm9yRWFjaChrZXkgPT4ge1xuICAgICAgICAgIGNvbnN0IGYgPSB0aGlzLl90b0ZpZWxkQ29uZmlnKDxKU09OU2NoZW1hNz4gc2NoZW1hLnByb3BlcnRpZXNba2V5XSwgb3B0aW9ucyk7XG4gICAgICAgICAgZmllbGQuZmllbGRHcm91cC5wdXNoKGYpO1xuICAgICAgICAgIGYua2V5ID0ga2V5O1xuICAgICAgICAgIGlmIChBcnJheS5pc0FycmF5KHNjaGVtYS5yZXF1aXJlZCkgJiYgc2NoZW1hLnJlcXVpcmVkLmluZGV4T2Yoa2V5KSAhPT0gLTEpIHtcbiAgICAgICAgICAgIGYudGVtcGxhdGVPcHRpb25zLnJlcXVpcmVkID0gdHJ1ZTtcbiAgICAgICAgICB9XG4gICAgICAgICAgaWYgKGYudGVtcGxhdGVPcHRpb25zICYmICFmLnRlbXBsYXRlT3B0aW9ucy5yZXF1aXJlZCAmJiBwcm9wRGVwc1trZXldKSB7XG4gICAgICAgICAgICBmLmV4cHJlc3Npb25Qcm9wZXJ0aWVzID0ge1xuICAgICAgICAgICAgICAndGVtcGxhdGVPcHRpb25zLnJlcXVpcmVkJzogbSA9PiBtICYmIHByb3BEZXBzW2tleV0uc29tZShrID0+ICFpc0VtcHR5KG1ba10pKSxcbiAgICAgICAgICAgIH07XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgaWYgKHNjaGVtYURlcHNba2V5XSkge1xuICAgICAgICAgICAgY29uc3QgZ2V0Q29uc3RWYWx1ZSA9IChzOiBKU09OU2NoZW1hNykgPT4ge1xuICAgICAgICAgICAgICByZXR1cm4gcy5oYXNPd25Qcm9wZXJ0eSgnY29uc3QnKSA/IHMuY29uc3QgOiBzLmVudW1bMF07XG4gICAgICAgICAgICB9O1xuXG4gICAgICAgICAgICBjb25zdCBvbmVPZlNjaGVtYSA9IHNjaGVtYURlcHNba2V5XS5vbmVPZjtcbiAgICAgICAgICAgIGlmIChcbiAgICAgICAgICAgICAgb25lT2ZTY2hlbWFcbiAgICAgICAgICAgICAgJiYgb25lT2ZTY2hlbWEuZXZlcnkobyA9PiBvLnByb3BlcnRpZXMgJiYgby5wcm9wZXJ0aWVzW2tleV0gJiYgaXNDb25zdChvLnByb3BlcnRpZXNba2V5XSkpXG4gICAgICAgICAgICApIHtcbiAgICAgICAgICAgICAgb25lT2ZTY2hlbWEuZm9yRWFjaChvbmVPZlNjaGVtYUl0ZW0gPT4ge1xuICAgICAgICAgICAgICAgIGNvbnN0IHsgW2tleV06IGNvbnN0U2NoZW1hLCAuLi5wcm9wZXJ0aWVzIH0gPSBvbmVPZlNjaGVtYUl0ZW0ucHJvcGVydGllcztcbiAgICAgICAgICAgICAgICBmaWVsZC5maWVsZEdyb3VwLnB1c2goe1xuICAgICAgICAgICAgICAgICAgLi4udGhpcy5fdG9GaWVsZENvbmZpZyh7IC4uLm9uZU9mU2NoZW1hSXRlbSwgcHJvcGVydGllcyB9LCB7IC4uLm9wdGlvbnMsIGF1dG9DbGVhcjogdHJ1ZSB9KSxcbiAgICAgICAgICAgICAgICAgIGhpZGVFeHByZXNzaW9uOiBtID0+ICFtIHx8IGdldENvbnN0VmFsdWUoY29uc3RTY2hlbWEpICE9PSBtW2tleV0sXG4gICAgICAgICAgICAgICAgfSk7XG4gICAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgICAgZmllbGQuZmllbGRHcm91cC5wdXNoKHtcbiAgICAgICAgICAgICAgICAuLi50aGlzLl90b0ZpZWxkQ29uZmlnKHNjaGVtYURlcHNba2V5XSwgb3B0aW9ucyksXG4gICAgICAgICAgICAgICAgaGlkZUV4cHJlc3Npb246IG0gPT4gIW0gfHwgaXNFbXB0eShtW2tleV0pLFxuICAgICAgICAgICAgICB9KTtcbiAgICAgICAgICAgIH1cblxuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG5cbiAgICAgICAgaWYgKHNjaGVtYS5vbmVPZikge1xuICAgICAgICAgIGZpZWxkLmZpZWxkR3JvdXAucHVzaCh0aGlzLnJlc29sdmVNdWx0aVNjaGVtYShcbiAgICAgICAgICAgICdvbmVPZicsXG4gICAgICAgICAgICA8SlNPTlNjaGVtYTdbXT4gc2NoZW1hLm9uZU9mLFxuICAgICAgICAgICAgb3B0aW9ucyxcbiAgICAgICAgICApKTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChzY2hlbWEuYW55T2YpIHtcbiAgICAgICAgICBmaWVsZC5maWVsZEdyb3VwLnB1c2godGhpcy5yZXNvbHZlTXVsdGlTY2hlbWEoXG4gICAgICAgICAgICAnYW55T2YnLFxuICAgICAgICAgICAgPEpTT05TY2hlbWE3W10+IHNjaGVtYS5hbnlPZixcbiAgICAgICAgICAgIG9wdGlvbnMsXG4gICAgICAgICAgKSk7XG4gICAgICAgIH1cbiAgICAgICAgYnJlYWs7XG4gICAgICB9XG4gICAgICBjYXNlICdhcnJheSc6IHtcbiAgICAgICAgaWYgKHNjaGVtYS5oYXNPd25Qcm9wZXJ0eSgnbWluSXRlbXMnKSkge1xuICAgICAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5taW5JdGVtcyA9IHNjaGVtYS5taW5JdGVtcztcbiAgICAgICAgICB0aGlzLmFkZFZhbGlkYXRvcihmaWVsZCwgJ21pbkl0ZW1zJywgKHsgdmFsdWUgfSkgPT4gaXNFbXB0eSh2YWx1ZSkgfHwgKHZhbHVlLmxlbmd0aCA+PSBzY2hlbWEubWluSXRlbXMpKTtcbiAgICAgICAgfVxuICAgICAgICBpZiAoc2NoZW1hLmhhc093blByb3BlcnR5KCdtYXhJdGVtcycpKSB7XG4gICAgICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zLm1heEl0ZW1zID0gc2NoZW1hLm1heEl0ZW1zO1xuICAgICAgICAgIHRoaXMuYWRkVmFsaWRhdG9yKGZpZWxkLCAnbWF4SXRlbXMnLCAoeyB2YWx1ZSB9KSA9PiBpc0VtcHR5KHZhbHVlKSB8fCAodmFsdWUubGVuZ3RoIDw9IHNjaGVtYS5tYXhJdGVtcykpO1xuICAgICAgICB9XG4gICAgICAgIGlmIChzY2hlbWEuaGFzT3duUHJvcGVydHkoJ3VuaXF1ZUl0ZW1zJykpIHtcbiAgICAgICAgICBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMudW5pcXVlSXRlbXMgPSBzY2hlbWEudW5pcXVlSXRlbXM7XG4gICAgICAgICAgdGhpcy5hZGRWYWxpZGF0b3IoZmllbGQsICd1bmlxdWVJdGVtcycsICh7IHZhbHVlIH0pID0+IHtcbiAgICAgICAgICAgIGlmIChpc0VtcHR5KHZhbHVlKSB8fCAhc2NoZW1hLnVuaXF1ZUl0ZW1zKSB7XG4gICAgICAgICAgICAgIHJldHVybiB0cnVlO1xuICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICBjb25zdCB1bmlxdWVJdGVtcyA9IEFycmF5LmZyb20oXG4gICAgICAgICAgICAgIG5ldyBTZXQodmFsdWUubWFwKCh2OiBhbnkpID0+IEpTT04uc3RyaW5naWZ5KHYpKSksXG4gICAgICAgICAgICApO1xuXG4gICAgICAgICAgICByZXR1cm4gdW5pcXVlSXRlbXMubGVuZ3RoID09PSB2YWx1ZS5sZW5ndGg7XG4gICAgICAgICAgfSk7XG4gICAgICAgIH1cblxuICAgICAgICAvLyByZXNvbHZlIGl0ZW1zIHNjaGVtYSBuZWVkZWQgZm9yIGlzRW51bSBjaGVja1xuICAgICAgICBpZiAoc2NoZW1hLml0ZW1zICYmICFBcnJheS5pc0FycmF5KHNjaGVtYS5pdGVtcykpIHtcbiAgICAgICAgICBzY2hlbWEuaXRlbXMgPSB0aGlzLnJlc29sdmVTY2hlbWEoPEpTT05TY2hlbWE3PiBzY2hlbWEuaXRlbXMsIG9wdGlvbnMpO1xuICAgICAgICB9XG5cbiAgICAgICAgLy8gVE9ETzogcmVtb3ZlIGlzRW51bSBjaGVjayBvbmNlIGFkZGluZyBhbiBvcHRpb24gdG8gc2tpcCBleHRlbnNpb25cbiAgICAgICAgaWYgKCF0aGlzLmlzRW51bShzY2hlbWEpKSB7XG4gICAgICAgICAgY29uc3QgX3RoaXMgPSB0aGlzO1xuICAgICAgICAgIE9iamVjdC5kZWZpbmVQcm9wZXJ0eShmaWVsZCwgJ2ZpZWxkQXJyYXknLCB7XG4gICAgICAgICAgICBnZXQ6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgICBpZiAoIUFycmF5LmlzQXJyYXkoc2NoZW1hLml0ZW1zKSkge1xuICAgICAgICAgICAgICAgIC8vIFdoZW4gaXRlbXMgaXMgYSBzaW5nbGUgc2NoZW1hLCB0aGUgYWRkaXRpb25hbEl0ZW1zIGtleXdvcmQgaXMgbWVhbmluZ2xlc3MsIGFuZCBpdCBzaG91bGQgbm90IGJlIHVzZWQuXG4gICAgICAgICAgICAgICAgcmV0dXJuIF90aGlzLl90b0ZpZWxkQ29uZmlnKDxKU09OU2NoZW1hNz4gc2NoZW1hLml0ZW1zLCBvcHRpb25zKTtcbiAgICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICAgIGNvbnN0IGxlbmd0aCA9IHRoaXMuZmllbGRHcm91cCA/IHRoaXMuZmllbGRHcm91cC5sZW5ndGggOiAwO1xuICAgICAgICAgICAgICBjb25zdCBpdGVtU2NoZW1hID0gc2NoZW1hLml0ZW1zW2xlbmd0aF1cbiAgICAgICAgICAgICAgICA/IHNjaGVtYS5pdGVtc1tsZW5ndGhdXG4gICAgICAgICAgICAgICAgOiBzY2hlbWEuYWRkaXRpb25hbEl0ZW1zO1xuXG4gICAgICAgICAgICAgIHJldHVybiBpdGVtU2NoZW1hXG4gICAgICAgICAgICAgICAgPyBfdGhpcy5fdG9GaWVsZENvbmZpZyg8SlNPTlNjaGVtYTc+IGl0ZW1TY2hlbWEsIG9wdGlvbnMpXG4gICAgICAgICAgICAgICAgOiB7fTtcbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBlbnVtZXJhYmxlOiB0cnVlLFxuICAgICAgICAgICAgY29uZmlndXJhYmxlOiB0cnVlLFxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG5cbiAgICAgICAgYnJlYWs7XG4gICAgICB9XG4gICAgfVxuXG4gICAgaWYgKHNjaGVtYS5oYXNPd25Qcm9wZXJ0eSgnY29uc3QnKSkge1xuICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zLmNvbnN0ID0gc2NoZW1hLmNvbnN0O1xuICAgICAgdGhpcy5hZGRWYWxpZGF0b3IoZmllbGQsICdjb25zdCcsICh7IHZhbHVlIH0pID0+IHZhbHVlID09PSBzY2hlbWEuY29uc3QpO1xuICAgICAgaWYgKCFmaWVsZC50eXBlKSB7XG4gICAgICAgIGZpZWxkLmRlZmF1bHRWYWx1ZSA9IHNjaGVtYS5jb25zdDtcbiAgICAgIH1cbiAgICB9XG5cbiAgICBpZiAodGhpcy5pc0VudW0oc2NoZW1hKSkge1xuICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zLm11bHRpcGxlID0gZmllbGQudHlwZSA9PT0gJ2FycmF5JztcbiAgICAgIGZpZWxkLnR5cGUgPSAnZW51bSc7XG4gICAgICBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMub3B0aW9ucyA9IHRoaXMudG9FbnVtT3B0aW9ucyhzY2hlbWEpO1xuICAgIH1cblxuICAgIC8vIG1hcCBpbiBwb3NzaWJsZSBmb3JtbHlDb25maWcgb3B0aW9ucyBmcm9tIHRoZSB3aWRnZXQgcHJvcGVydHlcbiAgICBpZiAoc2NoZW1hWyd3aWRnZXQnXSAmJiBzY2hlbWFbJ3dpZGdldCddLmZvcm1seUNvbmZpZykge1xuICAgICAgZmllbGQgPSByZXZlcnNlRGVlcE1lcmdlKHNjaGVtYVsnd2lkZ2V0J10uZm9ybWx5Q29uZmlnLCBmaWVsZCk7XG4gICAgfVxuXG4gICAgLy8gaWYgdGhlcmUgaXMgYSBtYXAgZnVuY3Rpb24gcGFzc2VkIGluLCB1c2UgaXQgdG8gYWxsb3cgdGhlIHVzZXIgdG9cbiAgICAvLyBmdXJ0aGVyIGN1c3RvbWl6ZSBob3cgZmllbGRzIGFyZSBiZWluZyBtYXBwZWRcbiAgICByZXR1cm4gb3B0aW9ucy5tYXAgPyBvcHRpb25zLm1hcChmaWVsZCwgc2NoZW1hKSA6IGZpZWxkO1xuICB9XG5cbiAgcHJpdmF0ZSByZXNvbHZlU2NoZW1hKHNjaGVtYTogSlNPTlNjaGVtYTcsIG9wdGlvbnM6IElPcHRpb25zKSB7XG4gICAgaWYgKHNjaGVtYS4kcmVmKSB7XG4gICAgICBzY2hlbWEgPSB0aGlzLnJlc29sdmVEZWZpbml0aW9uKHNjaGVtYSwgb3B0aW9ucyk7XG4gICAgfVxuXG4gICAgaWYgKHNjaGVtYS5hbGxPZikge1xuICAgICAgc2NoZW1hID0gdGhpcy5yZXNvbHZlQWxsT2Yoc2NoZW1hLCBvcHRpb25zKTtcbiAgICB9XG5cbiAgICByZXR1cm4gc2NoZW1hO1xuICB9XG5cbiAgcHJpdmF0ZSByZXNvbHZlQWxsT2YoeyBhbGxPZiwgLi4uYmFzZVNjaGVtYSB9OiBKU09OU2NoZW1hNywgb3B0aW9uczogSU9wdGlvbnMpIHtcbiAgICBpZiAoIWFsbE9mLmxlbmd0aCkge1xuICAgICAgdGhyb3cgRXJyb3IoYGFsbE9mIGFycmF5IGNhbiBub3QgYmUgZW1wdHkgJHthbGxPZn0uYCk7XG4gICAgfVxuXG4gICAgcmV0dXJuIGFsbE9mLnJlZHVjZSgoYmFzZTogSlNPTlNjaGVtYTcsIHNjaGVtYTogSlNPTlNjaGVtYTcpID0+IHtcbiAgICAgIHNjaGVtYSA9IHRoaXMucmVzb2x2ZVNjaGVtYShzY2hlbWEsIG9wdGlvbnMpO1xuICAgICAgaWYgKGJhc2UucmVxdWlyZWQgJiYgc2NoZW1hLnJlcXVpcmVkKSB7XG4gICAgICAgIGJhc2UucmVxdWlyZWQgPSBbLi4uYmFzZS5yZXF1aXJlZCwgLi4uc2NoZW1hLnJlcXVpcmVkXTtcbiAgICAgIH1cblxuICAgICAgaWYgKHNjaGVtYS51bmlxdWVJdGVtcykge1xuICAgICAgICBiYXNlLnVuaXF1ZUl0ZW1zID0gc2NoZW1hLnVuaXF1ZUl0ZW1zO1xuICAgICAgfVxuXG4gICAgICAvLyByZXNvbHZlIHRvIG1pbiB2YWx1ZVxuICAgICAgWydtYXhMZW5ndGgnLCAnbWF4aW11bScsICdleGNsdXNpdmVNYXhpbXVtJywgJ21heEl0ZW1zJywgJ21heFByb3BlcnRpZXMnXVxuICAgICAgICAuZm9yRWFjaChwcm9wID0+IHtcbiAgICAgICAgICBpZiAoIWlzRW1wdHkoYmFzZVtwcm9wXSkgJiYgIWlzRW1wdHkoc2NoZW1hW3Byb3BdKSkge1xuICAgICAgICAgICAgYmFzZVtwcm9wXSA9IGJhc2VbcHJvcF0gPCBzY2hlbWFbcHJvcF0gPyBiYXNlW3Byb3BdIDogc2NoZW1hW3Byb3BdO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG5cbiAgICAgIC8vIHJlc29sdmUgdG8gbWF4IHZhbHVlXG4gICAgICBbJ21pbkxlbmd0aCcsICdtaW5pbXVtJywgJ2V4Y2x1c2l2ZU1pbmltdW0nLCAnbWluSXRlbXMnLCAnbWluUHJvcGVydGllcyddXG4gICAgICAgIC5mb3JFYWNoKHByb3AgPT4ge1xuICAgICAgICAgIGlmICghaXNFbXB0eShiYXNlW3Byb3BdKSAmJiAhaXNFbXB0eShzY2hlbWFbcHJvcF0pKSB7XG4gICAgICAgICAgICBiYXNlW3Byb3BdID0gYmFzZVtwcm9wXSA+IHNjaGVtYVtwcm9wXSA/IGJhc2VbcHJvcF0gOiBzY2hlbWFbcHJvcF07XG4gICAgICAgICAgfVxuICAgICAgICB9KTtcblxuICAgICAgcmV0dXJuIHJldmVyc2VEZWVwTWVyZ2UoYmFzZSwgc2NoZW1hKTtcbiAgICB9LCBiYXNlU2NoZW1hKTtcbiAgfVxuXG4gIHByaXZhdGUgcmVzb2x2ZU11bHRpU2NoZW1hKFxuICAgIG1vZGU6ICdvbmVPZicgfCAnYW55T2YnLFxuICAgIHNjaGVtYXM6IEpTT05TY2hlbWE3W10sXG4gICAgb3B0aW9uczogSU9wdGlvbnMsXG4gICk6IEZvcm1seUZpZWxkQ29uZmlnIHtcbiAgICByZXR1cm4ge1xuICAgICAgdHlwZTogJ211bHRpc2NoZW1hJyxcbiAgICAgIGZpZWxkR3JvdXA6IFtcbiAgICAgICAge1xuICAgICAgICAgIHR5cGU6ICdlbnVtJyxcbiAgICAgICAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgICAgICAgIG11bHRpcGxlOiBtb2RlID09PSAnYW55T2YnLFxuICAgICAgICAgICAgb3B0aW9uczogc2NoZW1hc1xuICAgICAgICAgICAgICAubWFwKChzLCBpKSA9PiAoeyBsYWJlbDogcy50aXRsZSwgdmFsdWU6IGkgfSkpLFxuICAgICAgICAgIH0sXG4gICAgICAgIH0sXG4gICAgICAgIHtcbiAgICAgICAgICBmaWVsZEdyb3VwOiBzY2hlbWFzLm1hcCgocywgaSkgPT4gKHtcbiAgICAgICAgICAgIC4uLnRoaXMuX3RvRmllbGRDb25maWcocywgeyAuLi5vcHRpb25zLCBhdXRvQ2xlYXI6IHRydWUgfSksXG4gICAgICAgICAgICBoaWRlRXhwcmVzc2lvbjogKG0sIGZzLCBmKSA9PiB7XG4gICAgICAgICAgICAgIGNvbnN0IHNlbGVjdEZpZWxkID0gZi5wYXJlbnQucGFyZW50LmZpZWxkR3JvdXBbMF07XG4gICAgICAgICAgICAgIGlmICghc2VsZWN0RmllbGQuZm9ybUNvbnRyb2wpIHtcbiAgICAgICAgICAgICAgICBjb25zdCB2YWx1ZSA9IGYucGFyZW50LmZpZWxkR3JvdXBcbiAgICAgICAgICAgICAgICAgIC5tYXAoKGYsIGkpID0+IFtmLCBpXSBhcyBbRm9ybWx5RmllbGRDb25maWcsIG51bWJlcl0pXG4gICAgICAgICAgICAgICAgICAuZmlsdGVyKChbZiwgaV0pID0+IHRoaXMuaXNGaWVsZFZhbGlkKGYsIHNjaGVtYXNbaV0sIG9wdGlvbnMpKVxuICAgICAgICAgICAgICAgICAgLnNvcnQoKFtmMV0sIFtmMl0pID0+IHtcbiAgICAgICAgICAgICAgICAgICAgY29uc3QgbWF0Y2hlZEZpZWxkczEgPSB0b3RhbE1hdGNoZWRGaWVsZHMoZjEpO1xuICAgICAgICAgICAgICAgICAgICBjb25zdCBtYXRjaGVkRmllbGRzMiA9IHRvdGFsTWF0Y2hlZEZpZWxkcyhmMik7XG4gICAgICAgICAgICAgICAgICAgIGlmIChtYXRjaGVkRmllbGRzMSA9PT0gbWF0Y2hlZEZpZWxkczIpIHtcbiAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gMDtcbiAgICAgICAgICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBtYXRjaGVkRmllbGRzMiA+IG1hdGNoZWRGaWVsZHMxID8gMSA6IC0xO1xuICAgICAgICAgICAgICAgICAgfSlcbiAgICAgICAgICAgICAgICAgIC5tYXAoKFssIGldKSA9PiBpKVxuICAgICAgICAgICAgICAgIDtcblxuICAgICAgICAgICAgICAgIGNvbnN0IG5vcm1hbGl6ZWRWYWx1ZSA9IFt2YWx1ZS5sZW5ndGggPT09IDAgPyAwIDogdmFsdWVbMF1dO1xuICAgICAgICAgICAgICAgIGNvbnN0IGZvcm1hdHRlZFZhbHVlID0gbW9kZSA9PT0gJ2FueU9mJyA/IG5vcm1hbGl6ZWRWYWx1ZSA6IG5vcm1hbGl6ZWRWYWx1ZVswXTtcbiAgICAgICAgICAgICAgICBzZWxlY3RGaWVsZC5mb3JtQ29udHJvbCA9IG5ldyBGb3JtQ29udHJvbChmb3JtYXR0ZWRWYWx1ZSk7XG4gICAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgICBjb25zdCBjb250cm9sID0gc2VsZWN0RmllbGQuZm9ybUNvbnRyb2w7XG5cbiAgICAgICAgICAgICAgcmV0dXJuIEFycmF5LmlzQXJyYXkoY29udHJvbC52YWx1ZSlcbiAgICAgICAgICAgICAgICA/IGNvbnRyb2wudmFsdWUuaW5kZXhPZihpKSA9PT0gLTFcbiAgICAgICAgICAgICAgICA6IGNvbnRyb2wudmFsdWUgIT09IGk7XG4gICAgICAgICAgICB9LFxuICAgICAgICAgIH0pKSxcbiAgICAgICAgfSxcbiAgICAgIF0sXG4gICAgfTtcbiAgfVxuXG4gIHByaXZhdGUgcmVzb2x2ZURlZmluaXRpb24oc2NoZW1hOiBKU09OU2NoZW1hNywgb3B0aW9uczogSU9wdGlvbnMpOiBKU09OU2NoZW1hNyB7XG4gICAgY29uc3QgW3VyaSwgcG9pbnRlcl0gPSBzY2hlbWEuJHJlZi5zcGxpdCgnIy8nKTtcbiAgICBpZiAodXJpKSB7XG4gICAgICB0aHJvdyBFcnJvcihgUmVtb3RlIHNjaGVtYXMgZm9yICR7c2NoZW1hLiRyZWZ9IG5vdCBzdXBwb3J0ZWQgeWV0LmApO1xuICAgIH1cblxuICAgIGNvbnN0IGRlZmluaXRpb24gPSAhcG9pbnRlciA/IG51bGwgOiBwb2ludGVyLnNwbGl0KCcvJykucmVkdWNlKFxuICAgICAgKGRlZiwgcGF0aCkgPT4gZGVmICYmIGRlZi5oYXNPd25Qcm9wZXJ0eShwYXRoKSA/IGRlZltwYXRoXSA6IG51bGwsXG4gICAgICBvcHRpb25zLnNjaGVtYSxcbiAgICApO1xuXG4gICAgaWYgKCFkZWZpbml0aW9uKSB7XG4gICAgICB0aHJvdyBFcnJvcihgQ2Fubm90IGZpbmQgYSBkZWZpbml0aW9uIGZvciAke3NjaGVtYS4kcmVmfS5gKTtcbiAgICB9XG5cbiAgICBpZiAoZGVmaW5pdGlvbi4kcmVmKSB7XG4gICAgICByZXR1cm4gdGhpcy5yZXNvbHZlRGVmaW5pdGlvbihkZWZpbml0aW9uLCBvcHRpb25zKTtcbiAgICB9XG5cbiAgICByZXR1cm4ge1xuICAgICAgLi4uZGVmaW5pdGlvbixcbiAgICAgIC4uLlsndGl0bGUnLCAnZGVzY3JpcHRpb24nLCAnZGVmYXVsdCddLnJlZHVjZSgoYW5ub3RhdGlvbiwgcCkgPT4ge1xuICAgICAgICBpZiAoc2NoZW1hLmhhc093blByb3BlcnR5KHApKSB7XG4gICAgICAgICAgYW5ub3RhdGlvbltwXSA9IHNjaGVtYVtwXTtcbiAgICAgICAgfVxuXG4gICAgICAgIHJldHVybiBhbm5vdGF0aW9uO1xuICAgICAgfSwge30pLFxuICAgIH07XG4gIH1cblxuICBwcml2YXRlIHJlc29sdmVEZXBlbmRlbmNpZXMoc2NoZW1hOiBKU09OU2NoZW1hNykge1xuICAgIGNvbnN0IGRlcHMgPSB7fTtcbiAgICBjb25zdCBzY2hlbWFEZXBzID0ge307XG5cbiAgICBPYmplY3Qua2V5cyhzY2hlbWEuZGVwZW5kZW5jaWVzIHx8IHt9KS5mb3JFYWNoKHByb3AgPT4ge1xuICAgICAgY29uc3QgZGVwZW5kZW5jeSA9IHNjaGVtYS5kZXBlbmRlbmNpZXNbcHJvcF0gYXMgSlNPTlNjaGVtYTc7XG4gICAgICBpZiAoQXJyYXkuaXNBcnJheShkZXBlbmRlbmN5KSkge1xuICAgICAgICAvLyBQcm9wZXJ0eSBkZXBlbmRlbmNpZXNcbiAgICAgICAgZGVwZW5kZW5jeS5mb3JFYWNoKGRlcCA9PiB7XG4gICAgICAgICAgaWYgKCFkZXBzW2RlcF0pIHtcbiAgICAgICAgICAgIGRlcHNbZGVwXSA9IFtwcm9wXTtcbiAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgZGVwc1tkZXBdLnB1c2gocHJvcCk7XG4gICAgICAgICAgfVxuICAgICAgICB9KTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIC8vIHNjaGVtYSBkZXBlbmRlbmNpZXNcbiAgICAgICAgc2NoZW1hRGVwc1twcm9wXSA9IGRlcGVuZGVuY3k7XG4gICAgICB9XG4gICAgfSk7XG5cbiAgICByZXR1cm4gW2RlcHMsIHNjaGVtYURlcHNdO1xuICB9XG5cbiAgcHJpdmF0ZSBndWVzc1R5cGUoc2NoZW1hOiBKU09OU2NoZW1hNykge1xuICAgIGNvbnN0IHR5cGUgPSBzY2hlbWEudHlwZSBhcyBKU09OU2NoZW1hN1R5cGVOYW1lO1xuICAgIGlmICghdHlwZSAmJiBzY2hlbWEucHJvcGVydGllcykge1xuICAgICAgcmV0dXJuICdvYmplY3QnO1xuICAgIH1cblxuICAgIGlmIChBcnJheS5pc0FycmF5KHR5cGUpKSB7XG4gICAgICBpZiAodHlwZS5sZW5ndGggPT09IDEpIHtcbiAgICAgICAgcmV0dXJuIHR5cGVbMF07XG4gICAgICB9XG5cbiAgICAgIGlmICh0eXBlLmxlbmd0aCA9PT0gMiAmJiB0eXBlLmluZGV4T2YoJ251bGwnKSAhPT0gLTEpIHtcbiAgICAgICAgcmV0dXJuIHR5cGVbdHlwZVswXSA9PT0gJ251bGwnID8gMSA6IDBdO1xuICAgICAgfVxuICAgIH1cblxuICAgIHJldHVybiB0eXBlO1xuICB9XG5cbiAgcHJpdmF0ZSBhZGRWYWxpZGF0b3IoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnLCBuYW1lOiBzdHJpbmcsIHZhbGlkYXRvcjogKGNvbnRyb2w6IEFic3RyYWN0Q29udHJvbCkgPT4gYm9vbGVhbikge1xuICAgIGZpZWxkLnZhbGlkYXRvcnMgPSBmaWVsZC52YWxpZGF0b3JzIHx8IHt9O1xuICAgIGZpZWxkLnZhbGlkYXRvcnNbbmFtZV0gPSB2YWxpZGF0b3I7XG4gIH1cblxuICBwcml2YXRlIGlzRW51bShzY2hlbWE6IEpTT05TY2hlbWE3KSB7XG4gICAgcmV0dXJuIHNjaGVtYS5lbnVtXG4gICAgICB8fCAoc2NoZW1hLmFueU9mICYmIHNjaGVtYS5hbnlPZi5ldmVyeShpc0NvbnN0KSlcbiAgICAgIHx8IChzY2hlbWEub25lT2YgJiYgc2NoZW1hLm9uZU9mLmV2ZXJ5KGlzQ29uc3QpKVxuICAgICAgfHwgc2NoZW1hLnVuaXF1ZUl0ZW1zICYmIHNjaGVtYS5pdGVtcyAmJiAhQXJyYXkuaXNBcnJheShzY2hlbWEuaXRlbXMpICYmIHRoaXMuaXNFbnVtKDxKU09OU2NoZW1hNz4gc2NoZW1hLml0ZW1zKTtcbiAgfVxuXG4gIHByaXZhdGUgdG9FbnVtT3B0aW9ucyhzY2hlbWE6IEpTT05TY2hlbWE3KSB7XG4gICAgaWYgKHNjaGVtYS5lbnVtKSB7XG4gICAgICByZXR1cm4gc2NoZW1hLmVudW0ubWFwKHZhbHVlID0+ICh7IHZhbHVlLCBsYWJlbDogdmFsdWUgfSkpO1xuICAgIH1cblxuICAgIGNvbnN0IHRvRW51bSA9IChzOiBKU09OU2NoZW1hNykgPT4ge1xuICAgICAgY29uc3QgdmFsdWUgPSBzLmhhc093blByb3BlcnR5KCdjb25zdCcpID8gcy5jb25zdCA6IHMuZW51bVswXTtcblxuICAgICAgcmV0dXJuIHsgdmFsdWU6IHZhbHVlLCBsYWJlbDogcy50aXRsZSB8fCB2YWx1ZSB9O1xuICAgIH07XG5cbiAgICBpZiAoc2NoZW1hLmFueU9mKSB7XG4gICAgICByZXR1cm4gc2NoZW1hLmFueU9mLm1hcCh0b0VudW0pO1xuICAgIH1cblxuICAgIGlmIChzY2hlbWEub25lT2YpIHtcbiAgICAgIHJldHVybiBzY2hlbWEub25lT2YubWFwKHRvRW51bSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMudG9FbnVtT3B0aW9ucyg8SlNPTlNjaGVtYTc+IHNjaGVtYS5pdGVtcyk7XG4gIH1cblxuICBwcml2YXRlIGlzRmllbGRWYWxpZChmaWVsZDogRm9ybWx5RmllbGRDb25maWcsIHNjaGVtYTogSlNPTlNjaGVtYTcsIG9wdGlvbnM6IElPcHRpb25zKTogYm9vbGVhbiB7XG4gICAgY29uc3QgeyBmb3JtIH0gPSAoZmllbGQub3B0aW9ucyBhcyBhbnkpLl9idWlsZEZpZWxkKHtcbiAgICAgIGZvcm06IG5ldyBGb3JtR3JvdXAoe30pLFxuICAgICAgZmllbGRHcm91cDogW3RoaXMuX3RvRmllbGRDb25maWcoc2NoZW1hLCBvcHRpb25zKV0sXG4gICAgICBtb2RlbDogZmllbGQubW9kZWwsXG4gICAgfSk7XG5cbiAgICByZXR1cm4gZm9ybS52YWxpZDtcbiAgfVxufVxuIl19