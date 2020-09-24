/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { isObject, isNullOrUndefined, isFunction, defineHiddenProp, wrapProperty, reduceFormUpdateValidityCalls } from '../../utils';
import { evalExpression, evalStringExpression } from './utils';
import { Observable } from 'rxjs';
import { unregisterControl, registerControl, updateValidity } from '../field-form/utils';
/**
 * \@experimental
 */
var /**
 * \@experimental
 */
FieldExpressionExtension = /** @class */ (function () {
    function FieldExpressionExtension() {
    }
    /**
     * @param {?} field
     * @return {?}
     */
    FieldExpressionExtension.prototype.prePopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        if (field.parent || field.options._checkField) {
            return;
        }
        /** @type {?} */
        var checkLocked = false;
        field.options._checkField = (/**
         * @param {?} f
         * @param {?} ignoreCache
         * @return {?}
         */
        function (f, ignoreCache) {
            if (!checkLocked) {
                checkLocked = true;
                reduceFormUpdateValidityCalls(f.formControl, (/**
                 * @return {?}
                 */
                function () { return _this.checkField(f, ignoreCache); }));
                checkLocked = false;
            }
        });
    };
    /**
     * @param {?} field
     * @return {?}
     */
    FieldExpressionExtension.prototype.onPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        if (!field.parent || field._expressionProperties) {
            return;
        }
        // cache built expression
        defineHiddenProp(field, '_expressionProperties', {});
        if (field.expressionProperties) {
            var _loop_1 = function (key) {
                /** @type {?} */
                var expressionProperty = field.expressionProperties[key];
                if (typeof expressionProperty === 'string' || isFunction(expressionProperty)) {
                    field._expressionProperties[key] = {
                        expression: this_1._evalExpression(expressionProperty, key === 'templateOptions.disabled' && field.parent.expressionProperties && field.parent.expressionProperties.hasOwnProperty('templateOptions.disabled')
                            ? (/**
                             * @return {?}
                             */
                            function () { return field.parent.templateOptions.disabled; })
                            : undefined),
                    };
                    if (key === 'templateOptions.disabled') {
                        Object.defineProperty(field._expressionProperties[key], 'expressionValue', {
                            get: (/**
                             * @return {?}
                             */
                            function () { return field.templateOptions.disabled; }),
                            set: (/**
                             * @return {?}
                             */
                            function () { }),
                            enumerable: true,
                            configurable: true,
                        });
                    }
                }
                else if (expressionProperty instanceof Observable) {
                    /** @type {?} */
                    var subscribe_1 = (/**
                     * @return {?}
                     */
                    function () { return ((/** @type {?} */ (expressionProperty)))
                        .subscribe((/**
                     * @param {?} v
                     * @return {?}
                     */
                    function (v) {
                        _this.setExprValue(field, key, v);
                        if (field.options && field.options._markForCheck) {
                            field.options._markForCheck(field);
                        }
                    })); });
                    /** @type {?} */
                    var subscription_1 = subscribe_1();
                    /** @type {?} */
                    var onInit_1 = field.hooks.onInit;
                    field.hooks.onInit = (/**
                     * @return {?}
                     */
                    function () {
                        if (subscription_1 === null) {
                            subscription_1 = subscribe_1();
                        }
                        return onInit_1 && onInit_1(field);
                    });
                    /** @type {?} */
                    var onDestroy_1 = field.hooks.onDestroy;
                    field.hooks.onDestroy = (/**
                     * @return {?}
                     */
                    function () {
                        onDestroy_1 && onDestroy_1(field);
                        subscription_1.unsubscribe();
                        subscription_1 = null;
                    });
                }
            };
            var this_1 = this;
            for (var key in field.expressionProperties) {
                _loop_1(key);
            }
        }
        if (field.hideExpression) {
            // delete hide value in order to force re-evaluate it in FormlyFormExpression.
            delete field.hide;
            /** @type {?} */
            var parent_1 = field.parent;
            while (parent_1 && !parent_1.hideExpression) {
                parent_1 = parent_1.parent;
            }
            field.hideExpression = this._evalExpression(field.hideExpression, parent_1 && parent_1.hideExpression ? (/**
             * @return {?}
             */
            function () { return parent_1.hide; }) : undefined);
        }
        else {
            wrapProperty(field, 'hide', (/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var currentValue = _a.currentValue, firstChange = _a.firstChange;
                field._hide = currentValue;
                if (!firstChange || (firstChange && currentValue === true)) {
                    field.options._hiddenFieldsForCheck.push(field);
                }
            }));
        }
    };
    /**
     * @private
     * @param {?} expression
     * @param {?=} parentExpression
     * @return {?}
     */
    FieldExpressionExtension.prototype._evalExpression = /**
     * @private
     * @param {?} expression
     * @param {?=} parentExpression
     * @return {?}
     */
    function (expression, parentExpression) {
        expression = expression || ((/**
         * @return {?}
         */
        function () { return false; }));
        if (typeof expression === 'string') {
            expression = evalStringExpression(expression, ['model', 'formState', 'field']);
        }
        return parentExpression
            ? (/**
             * @param {?} model
             * @param {?} formState
             * @param {?} field
             * @return {?}
             */
            function (model, formState, field) { return parentExpression() || expression(model, formState, field); })
            : expression;
    };
    /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    FieldExpressionExtension.prototype.checkField = /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    function (field, ignoreCache) {
        var _this = this;
        if (ignoreCache === void 0) { ignoreCache = false; }
        this._checkField(field, ignoreCache);
        field.options._hiddenFieldsForCheck
            .sort((/**
         * @param {?} f
         * @return {?}
         */
        function (f) { return f.hide ? -1 : 1; }))
            .forEach((/**
         * @param {?} f
         * @return {?}
         */
        function (f) { return _this.toggleFormControl(f, f.hide); }));
        field.options._hiddenFieldsForCheck = [];
    };
    /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    FieldExpressionExtension.prototype._checkField = /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    function (field, ignoreCache) {
        var _this = this;
        if (ignoreCache === void 0) { ignoreCache = false; }
        /** @type {?} */
        var markForCheck = false;
        field.fieldGroup.forEach((/**
         * @param {?} f
         * @return {?}
         */
        function (f) {
            _this.checkFieldExpressionChange(f, ignoreCache) && (markForCheck = true);
            if (_this.checkFieldVisibilityChange(f, ignoreCache)) {
                field.options._hiddenFieldsForCheck.push(f);
                markForCheck = true;
            }
            if (f.fieldGroup && f.fieldGroup.length > 0) {
                _this._checkField(f, ignoreCache);
            }
        }));
        if (markForCheck && field.options && field.options._markForCheck) {
            field.options._markForCheck(field);
        }
    };
    /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    FieldExpressionExtension.prototype.checkFieldExpressionChange = /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    function (field, ignoreCache) {
        if (!field || !field._expressionProperties) {
            return false;
        }
        /** @type {?} */
        var markForCheck = false;
        /** @type {?} */
        var expressionProperties = field._expressionProperties;
        for (var key in expressionProperties) {
            /** @type {?} */
            var expressionValue = evalExpression(expressionProperties[key].expression, { field: field }, [field.model, field.options.formState, field]);
            if (key === 'templateOptions.disabled') {
                expressionValue = !!expressionValue;
            }
            if (ignoreCache || (expressionProperties[key].expressionValue !== expressionValue
                && (!isObject(expressionValue) || JSON.stringify(expressionValue) !== JSON.stringify(expressionProperties[key].expressionValue)))) {
                markForCheck = true;
                expressionProperties[key].expressionValue = expressionValue;
                this.setExprValue(field, key, expressionValue);
            }
        }
        return markForCheck;
    };
    /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    FieldExpressionExtension.prototype.checkFieldVisibilityChange = /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    function (field, ignoreCache) {
        if (!field || isNullOrUndefined(field.hideExpression)) {
            return false;
        }
        /** @type {?} */
        var hideExpressionResult = !!evalExpression(field.hideExpression, { field: field }, [field.model, field.options.formState, field]);
        /** @type {?} */
        var markForCheck = false;
        if (hideExpressionResult !== field.hide || ignoreCache) {
            markForCheck = true;
            // toggle hide
            field.hide = hideExpressionResult;
            field.templateOptions.hidden = hideExpressionResult;
        }
        return markForCheck;
    };
    /**
     * @private
     * @param {?} field
     * @param {?} value
     * @return {?}
     */
    FieldExpressionExtension.prototype.setDisabledState = /**
     * @private
     * @param {?} field
     * @param {?} value
     * @return {?}
     */
    function (field, value) {
        var _this = this;
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return !f.expressionProperties || !f.expressionProperties.hasOwnProperty('templateOptions.disabled'); }))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return _this.setDisabledState(f, value); }));
        }
        if (field.key && field.templateOptions.disabled !== value) {
            field.templateOptions.disabled = value;
        }
    };
    /**
     * @private
     * @param {?} field
     * @param {?} hide
     * @return {?}
     */
    FieldExpressionExtension.prototype.toggleFormControl = /**
     * @private
     * @param {?} field
     * @param {?} hide
     * @return {?}
     */
    function (field, hide) {
        var _this = this;
        if (field.formControl && field.key) {
            defineHiddenProp(field, '_hide', !!(hide || field.hide));
            /** @type {?} */
            var c = field.formControl;
            if (c['_fields'].length > 1) {
                updateValidity(c);
            }
            hide === true && c['_fields'].every((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return !!f._hide; }))
                ? unregisterControl(field)
                : registerControl(field);
        }
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return !f.hideExpression; }))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return _this.toggleFormControl(f, hide); }));
        }
        if (field.options.fieldChanges) {
            field.options.fieldChanges.next((/** @type {?} */ ({ field: field, type: 'hidden', value: hide })));
        }
    };
    /**
     * @private
     * @param {?} field
     * @param {?} prop
     * @param {?} value
     * @return {?}
     */
    FieldExpressionExtension.prototype.setExprValue = /**
     * @private
     * @param {?} field
     * @param {?} prop
     * @param {?} value
     * @return {?}
     */
    function (field, prop, value) {
        try {
            /** @type {?} */
            var target = field;
            /** @type {?} */
            var paths = prop.split('.');
            /** @type {?} */
            var lastIndex = paths.length - 1;
            for (var i = 0; i < lastIndex; i++) {
                target = target[paths[i]];
            }
            target[paths[lastIndex]] = value;
        }
        catch (error) {
            error.message = "[Formly Error] [Expression \"" + prop + "\"] " + error.message;
            throw error;
        }
        if (prop === 'templateOptions.disabled' && field.key) {
            this.setDisabledState(field, value);
        }
        if (prop.indexOf('model.') === 0) {
            /** @type {?} */
            var path = prop.replace(/^model\./, '');
            /** @type {?} */
            var control = field.key && prop === path ? field.formControl : field.parent.formControl.get(path);
            if (control
                && !(isNullOrUndefined(control.value) && isNullOrUndefined(value))
                && control.value !== value) {
                control.patchValue(value, { emitEvent: false });
            }
        }
        this.emitExpressionChanges(field, prop, value);
    };
    /**
     * @private
     * @param {?} field
     * @param {?} property
     * @param {?} value
     * @return {?}
     */
    FieldExpressionExtension.prototype.emitExpressionChanges = /**
     * @private
     * @param {?} field
     * @param {?} property
     * @param {?} value
     * @return {?}
     */
    function (field, property, value) {
        if (!field.options.fieldChanges) {
            return;
        }
        field.options.fieldChanges.next({
            field: field,
            type: 'expressionChanges',
            property: property,
            value: value,
        });
    };
    return FieldExpressionExtension;
}());
/**
 * \@experimental
 */
export { FieldExpressionExtension };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtZXhwcmVzc2lvbi5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC1leHByZXNzaW9uL2ZpZWxkLWV4cHJlc3Npb24udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUNBLE9BQU8sRUFBRSxRQUFRLEVBQUUsaUJBQWlCLEVBQUUsVUFBVSxFQUFFLGdCQUFnQixFQUFFLFlBQVksRUFBRSw2QkFBNkIsRUFBRSxNQUFNLGFBQWEsQ0FBQztBQUNySSxPQUFPLEVBQUUsY0FBYyxFQUFFLG9CQUFvQixFQUFFLE1BQU0sU0FBUyxDQUFDO0FBQy9ELE9BQU8sRUFBRSxVQUFVLEVBQWdCLE1BQU0sTUFBTSxDQUFDO0FBRWhELE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxlQUFlLEVBQUUsY0FBYyxFQUFFLE1BQU0scUJBQXFCLENBQUM7Ozs7QUFHekY7Ozs7SUFBQTtJQWdSQSxDQUFDOzs7OztJQS9RQyw4Q0FBVzs7OztJQUFYLFVBQVksS0FBNkI7UUFBekMsaUJBZ0JDO1FBZkMsSUFBSSxLQUFLLENBQUMsTUFBTSxJQUFJLEtBQUssQ0FBQyxPQUFPLENBQUMsV0FBVyxFQUFFO1lBQzdDLE9BQU87U0FDUjs7WUFFRyxXQUFXLEdBQUcsS0FBSztRQUN2QixLQUFLLENBQUMsT0FBTyxDQUFDLFdBQVc7Ozs7O1FBQUcsVUFBQyxDQUFDLEVBQUUsV0FBVztZQUN6QyxJQUFJLENBQUMsV0FBVyxFQUFFO2dCQUNoQixXQUFXLEdBQUcsSUFBSSxDQUFDO2dCQUNuQiw2QkFBNkIsQ0FDM0IsQ0FBQyxDQUFDLFdBQVc7OztnQkFDYixjQUFNLE9BQUEsS0FBSSxDQUFDLFVBQVUsQ0FBQyxDQUFDLEVBQUUsV0FBVyxDQUFDLEVBQS9CLENBQStCLEVBQ3RDLENBQUM7Z0JBQ0YsV0FBVyxHQUFHLEtBQUssQ0FBQzthQUNyQjtRQUNILENBQUMsQ0FBQSxDQUFDO0lBQ0osQ0FBQzs7Ozs7SUFFRCw2Q0FBVTs7OztJQUFWLFVBQVcsS0FBNkI7UUFBeEMsaUJBOEVDO1FBN0VDLElBQUksQ0FBQyxLQUFLLENBQUMsTUFBTSxJQUFJLEtBQUssQ0FBQyxxQkFBcUIsRUFBRTtZQUNoRCxPQUFPO1NBQ1I7UUFFRCx5QkFBeUI7UUFDekIsZ0JBQWdCLENBQUMsS0FBSyxFQUFFLHVCQUF1QixFQUFFLEVBQUUsQ0FBQyxDQUFDO1FBRXJELElBQUksS0FBSyxDQUFDLG9CQUFvQixFQUFFO29DQUNuQixHQUFHOztvQkFDTixrQkFBa0IsR0FBRyxLQUFLLENBQUMsb0JBQW9CLENBQUMsR0FBRyxDQUFDO2dCQUUxRCxJQUFJLE9BQU8sa0JBQWtCLEtBQUssUUFBUSxJQUFJLFVBQVUsQ0FBQyxrQkFBa0IsQ0FBQyxFQUFFO29CQUM1RSxLQUFLLENBQUMscUJBQXFCLENBQUMsR0FBRyxDQUFDLEdBQUc7d0JBQ2pDLFVBQVUsRUFBRSxPQUFLLGVBQWUsQ0FDOUIsa0JBQWtCLEVBQ2xCLEdBQUcsS0FBSywwQkFBMEIsSUFBSSxLQUFLLENBQUMsTUFBTSxDQUFDLG9CQUFvQixJQUFJLEtBQUssQ0FBQyxNQUFNLENBQUMsb0JBQW9CLENBQUMsY0FBYyxDQUFDLDBCQUEwQixDQUFDOzRCQUNySixDQUFDOzs7NEJBQUMsY0FBTSxPQUFBLEtBQUssQ0FBQyxNQUFNLENBQUMsZUFBZSxDQUFDLFFBQVEsRUFBckMsQ0FBcUM7NEJBQzdDLENBQUMsQ0FBQyxTQUFTLENBQ2Q7cUJBQ0YsQ0FBQztvQkFDRixJQUFJLEdBQUcsS0FBSywwQkFBMEIsRUFBRTt3QkFDdEMsTUFBTSxDQUFDLGNBQWMsQ0FBQyxLQUFLLENBQUMscUJBQXFCLENBQUMsR0FBRyxDQUFDLEVBQUUsaUJBQWlCLEVBQUU7NEJBQ3pFLEdBQUc7Ozs0QkFBRSxjQUFNLE9BQUEsS0FBSyxDQUFDLGVBQWUsQ0FBQyxRQUFRLEVBQTlCLENBQThCLENBQUE7NEJBQ3pDLEdBQUc7Ozs0QkFBRSxjQUFRLENBQUMsQ0FBQTs0QkFDZCxVQUFVLEVBQUUsSUFBSTs0QkFDaEIsWUFBWSxFQUFFLElBQUk7eUJBQ25CLENBQUMsQ0FBQztxQkFDSjtpQkFDRjtxQkFBTSxJQUFJLGtCQUFrQixZQUFZLFVBQVUsRUFBRTs7d0JBQzdDLFdBQVM7OztvQkFBRyxjQUFNLE9BQUEsQ0FBQyxtQkFBQSxrQkFBa0IsRUFBbUIsQ0FBQzt5QkFDNUQsU0FBUzs7OztvQkFBQyxVQUFBLENBQUM7d0JBQ1YsS0FBSSxDQUFDLFlBQVksQ0FBQyxLQUFLLEVBQUUsR0FBRyxFQUFFLENBQUMsQ0FBQyxDQUFDO3dCQUNqQyxJQUFJLEtBQUssQ0FBQyxPQUFPLElBQUksS0FBSyxDQUFDLE9BQU8sQ0FBQyxhQUFhLEVBQUU7NEJBQ2hELEtBQUssQ0FBQyxPQUFPLENBQUMsYUFBYSxDQUFDLEtBQUssQ0FBQyxDQUFDO3lCQUNwQztvQkFDSCxDQUFDLEVBQUMsRUFOb0IsQ0FNcEIsQ0FBQTs7d0JBRUEsY0FBWSxHQUFpQixXQUFTLEVBQUU7O3dCQUN0QyxRQUFNLEdBQUcsS0FBSyxDQUFDLEtBQUssQ0FBQyxNQUFNO29CQUNqQyxLQUFLLENBQUMsS0FBSyxDQUFDLE1BQU07OztvQkFBRzt3QkFDbkIsSUFBSSxjQUFZLEtBQUssSUFBSSxFQUFFOzRCQUN6QixjQUFZLEdBQUcsV0FBUyxFQUFFLENBQUM7eUJBQzVCO3dCQUNELE9BQU8sUUFBTSxJQUFJLFFBQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQztvQkFDakMsQ0FBQyxDQUFBLENBQUM7O3dCQUVJLFdBQVMsR0FBRyxLQUFLLENBQUMsS0FBSyxDQUFDLFNBQVM7b0JBQ3ZDLEtBQUssQ0FBQyxLQUFLLENBQUMsU0FBUzs7O29CQUFHO3dCQUN0QixXQUFTLElBQUksV0FBUyxDQUFDLEtBQUssQ0FBQyxDQUFDO3dCQUM5QixjQUFZLENBQUMsV0FBVyxFQUFFLENBQUM7d0JBQzNCLGNBQVksR0FBRyxJQUFJLENBQUM7b0JBQ3RCLENBQUMsQ0FBQSxDQUFDO2lCQUNIOzs7WUE1Q0gsS0FBSyxJQUFNLEdBQUcsSUFBSSxLQUFLLENBQUMsb0JBQW9CO3dCQUFqQyxHQUFHO2FBNkNiO1NBQ0Y7UUFFRCxJQUFJLEtBQUssQ0FBQyxjQUFjLEVBQUU7WUFDeEIsOEVBQThFO1lBQzlFLE9BQU8sS0FBSyxDQUFDLElBQUksQ0FBQzs7Z0JBRWQsUUFBTSxHQUFHLEtBQUssQ0FBQyxNQUFNO1lBQ3pCLE9BQU8sUUFBTSxJQUFJLENBQUMsUUFBTSxDQUFDLGNBQWMsRUFBRTtnQkFDdkMsUUFBTSxHQUFHLFFBQU0sQ0FBQyxNQUFNLENBQUM7YUFDeEI7WUFFRCxLQUFLLENBQUMsY0FBYyxHQUFHLElBQUksQ0FBQyxlQUFlLENBQ3pDLEtBQUssQ0FBQyxjQUFjLEVBQ3BCLFFBQU0sSUFBSSxRQUFNLENBQUMsY0FBYyxDQUFDLENBQUM7OztZQUFDLGNBQU0sT0FBQSxRQUFNLENBQUMsSUFBSSxFQUFYLENBQVcsRUFBQyxDQUFDLENBQUMsU0FBUyxDQUNoRSxDQUFDO1NBQ0g7YUFBTTtZQUNMLFlBQVksQ0FBQyxLQUFLLEVBQUUsTUFBTTs7OztZQUFFLFVBQUMsRUFBNkI7b0JBQTNCLDhCQUFZLEVBQUUsNEJBQVc7Z0JBQ3RELEtBQUssQ0FBQyxLQUFLLEdBQUcsWUFBWSxDQUFDO2dCQUMzQixJQUFJLENBQUMsV0FBVyxJQUFJLENBQUMsV0FBVyxJQUFJLFlBQVksS0FBSyxJQUFJLENBQUMsRUFBRTtvQkFDMUQsS0FBSyxDQUFDLE9BQU8sQ0FBQyxxQkFBcUIsQ0FBQyxJQUFJLENBQUMsS0FBSyxDQUFDLENBQUM7aUJBQ2pEO1lBQ0gsQ0FBQyxFQUFDLENBQUM7U0FDSjtJQUNILENBQUM7Ozs7Ozs7SUFFTyxrREFBZTs7Ozs7O0lBQXZCLFVBQXdCLFVBQVUsRUFBRSxnQkFBaUI7UUFDbkQsVUFBVSxHQUFHLFVBQVUsSUFBSTs7O1FBQUMsY0FBTSxPQUFBLEtBQUssRUFBTCxDQUFLLEVBQUMsQ0FBQztRQUN6QyxJQUFJLE9BQU8sVUFBVSxLQUFLLFFBQVEsRUFBRTtZQUNsQyxVQUFVLEdBQUcsb0JBQW9CLENBQUMsVUFBVSxFQUFFLENBQUMsT0FBTyxFQUFFLFdBQVcsRUFBRSxPQUFPLENBQUMsQ0FBQyxDQUFDO1NBQ2hGO1FBRUQsT0FBTyxnQkFBZ0I7WUFDckIsQ0FBQzs7Ozs7O1lBQUMsVUFBQyxLQUFVLEVBQUUsU0FBYyxFQUFFLEtBQXdCLElBQUssT0FBQSxnQkFBZ0IsRUFBRSxJQUFJLFVBQVUsQ0FBQyxLQUFLLEVBQUUsU0FBUyxFQUFFLEtBQUssQ0FBQyxFQUF6RCxDQUF5RDtZQUNySCxDQUFDLENBQUMsVUFBVSxDQUFDO0lBQ2pCLENBQUM7Ozs7Ozs7SUFFTyw2Q0FBVTs7Ozs7O0lBQWxCLFVBQW1CLEtBQTZCLEVBQUUsV0FBbUI7UUFBckUsaUJBUUM7UUFSaUQsNEJBQUEsRUFBQSxtQkFBbUI7UUFDbkUsSUFBSSxDQUFDLFdBQVcsQ0FBQyxLQUFLLEVBQUUsV0FBVyxDQUFDLENBQUM7UUFFckMsS0FBSyxDQUFDLE9BQU8sQ0FBQyxxQkFBcUI7YUFDaEMsSUFBSTs7OztRQUFDLFVBQUEsQ0FBQyxJQUFJLE9BQUEsQ0FBQyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsQ0FBQyxDQUFDLENBQUMsQ0FBQyxDQUFDLENBQUMsRUFBZixDQUFlLEVBQUM7YUFDMUIsT0FBTzs7OztRQUFDLFVBQUEsQ0FBQyxJQUFJLE9BQUEsS0FBSSxDQUFDLGlCQUFpQixDQUFDLENBQUMsRUFBRSxDQUFDLENBQUMsSUFBSSxDQUFDLEVBQWpDLENBQWlDLEVBQUMsQ0FBQztRQUVuRCxLQUFLLENBQUMsT0FBTyxDQUFDLHFCQUFxQixHQUFHLEVBQUUsQ0FBQztJQUMzQyxDQUFDOzs7Ozs7O0lBRU8sOENBQVc7Ozs7OztJQUFuQixVQUFvQixLQUE2QixFQUFFLFdBQW1CO1FBQXRFLGlCQWlCQztRQWpCa0QsNEJBQUEsRUFBQSxtQkFBbUI7O1lBQ2hFLFlBQVksR0FBRyxLQUFLO1FBQ3hCLEtBQUssQ0FBQyxVQUFVLENBQUMsT0FBTzs7OztRQUFDLFVBQUEsQ0FBQztZQUN4QixLQUFJLENBQUMsMEJBQTBCLENBQUMsQ0FBQyxFQUFFLFdBQVcsQ0FBQyxJQUFJLENBQUMsWUFBWSxHQUFHLElBQUksQ0FBQyxDQUFDO1lBQ3pFLElBQUksS0FBSSxDQUFDLDBCQUEwQixDQUFDLENBQUMsRUFBRSxXQUFXLENBQUMsRUFBRTtnQkFDbkQsS0FBSyxDQUFDLE9BQU8sQ0FBQyxxQkFBcUIsQ0FBQyxJQUFJLENBQUMsQ0FBQyxDQUFDLENBQUM7Z0JBQzVDLFlBQVksR0FBRyxJQUFJLENBQUM7YUFDckI7WUFFRCxJQUFJLENBQUMsQ0FBQyxVQUFVLElBQUksQ0FBQyxDQUFDLFVBQVUsQ0FBQyxNQUFNLEdBQUcsQ0FBQyxFQUFFO2dCQUMzQyxLQUFJLENBQUMsV0FBVyxDQUFDLENBQUMsRUFBRSxXQUFXLENBQUMsQ0FBQzthQUNsQztRQUNILENBQUMsRUFBQyxDQUFDO1FBRUgsSUFBSSxZQUFZLElBQUksS0FBSyxDQUFDLE9BQU8sSUFBSSxLQUFLLENBQUMsT0FBTyxDQUFDLGFBQWEsRUFBRTtZQUNoRSxLQUFLLENBQUMsT0FBTyxDQUFDLGFBQWEsQ0FBQyxLQUFLLENBQUMsQ0FBQztTQUNwQztJQUNILENBQUM7Ozs7Ozs7SUFFTyw2REFBMEI7Ozs7OztJQUFsQyxVQUFtQyxLQUE2QixFQUFFLFdBQVc7UUFDM0UsSUFBSSxDQUFDLEtBQUssSUFBSSxDQUFDLEtBQUssQ0FBQyxxQkFBcUIsRUFBRTtZQUMxQyxPQUFPLEtBQUssQ0FBQztTQUNkOztZQUVHLFlBQVksR0FBRyxLQUFLOztZQUNsQixvQkFBb0IsR0FBRyxLQUFLLENBQUMscUJBQXFCO1FBRXhELEtBQUssSUFBTSxHQUFHLElBQUksb0JBQW9CLEVBQUU7O2dCQUNsQyxlQUFlLEdBQUcsY0FBYyxDQUFDLG9CQUFvQixDQUFDLEdBQUcsQ0FBQyxDQUFDLFVBQVUsRUFBRSxFQUFFLEtBQUssT0FBQSxFQUFFLEVBQUUsQ0FBQyxLQUFLLENBQUMsS0FBSyxFQUFFLEtBQUssQ0FBQyxPQUFPLENBQUMsU0FBUyxFQUFFLEtBQUssQ0FBQyxDQUFDO1lBQ3BJLElBQUksR0FBRyxLQUFLLDBCQUEwQixFQUFFO2dCQUN0QyxlQUFlLEdBQUcsQ0FBQyxDQUFDLGVBQWUsQ0FBQzthQUNyQztZQUVELElBQ0UsV0FBVyxJQUFJLENBQ2Isb0JBQW9CLENBQUMsR0FBRyxDQUFDLENBQUMsZUFBZSxLQUFLLGVBQWU7bUJBQzFELENBQUMsQ0FBQyxRQUFRLENBQUMsZUFBZSxDQUFDLElBQUksSUFBSSxDQUFDLFNBQVMsQ0FBQyxlQUFlLENBQUMsS0FBSyxJQUFJLENBQUMsU0FBUyxDQUFDLG9CQUFvQixDQUFDLEdBQUcsQ0FBQyxDQUFDLGVBQWUsQ0FBQyxDQUFDLENBQ2pJLEVBQ0Q7Z0JBQ0EsWUFBWSxHQUFHLElBQUksQ0FBQztnQkFDcEIsb0JBQW9CLENBQUMsR0FBRyxDQUFDLENBQUMsZUFBZSxHQUFHLGVBQWUsQ0FBQztnQkFDNUQsSUFBSSxDQUFDLFlBQVksQ0FBQyxLQUFLLEVBQUUsR0FBRyxFQUFFLGVBQWUsQ0FBQyxDQUFDO2FBQ2hEO1NBQ0Y7UUFFRCxPQUFPLFlBQVksQ0FBQztJQUN0QixDQUFDOzs7Ozs7O0lBRU8sNkRBQTBCOzs7Ozs7SUFBbEMsVUFBbUMsS0FBNkIsRUFBRSxXQUFXO1FBQzNFLElBQUksQ0FBQyxLQUFLLElBQUksaUJBQWlCLENBQUMsS0FBSyxDQUFDLGNBQWMsQ0FBQyxFQUFFO1lBQ3JELE9BQU8sS0FBSyxDQUFDO1NBQ2Q7O1lBRUssb0JBQW9CLEdBQVksQ0FBQyxDQUFDLGNBQWMsQ0FDcEQsS0FBSyxDQUFDLGNBQWMsRUFDcEIsRUFBRSxLQUFLLE9BQUEsRUFBRSxFQUNULENBQUMsS0FBSyxDQUFDLEtBQUssRUFBRSxLQUFLLENBQUMsT0FBTyxDQUFDLFNBQVMsRUFBRSxLQUFLLENBQUMsQ0FDOUM7O1lBQ0csWUFBWSxHQUFHLEtBQUs7UUFDeEIsSUFBSSxvQkFBb0IsS0FBSyxLQUFLLENBQUMsSUFBSSxJQUFJLFdBQVcsRUFBRTtZQUN0RCxZQUFZLEdBQUcsSUFBSSxDQUFDO1lBQ3BCLGNBQWM7WUFDZCxLQUFLLENBQUMsSUFBSSxHQUFHLG9CQUFvQixDQUFDO1lBQ2xDLEtBQUssQ0FBQyxlQUFlLENBQUMsTUFBTSxHQUFHLG9CQUFvQixDQUFDO1NBQ3JEO1FBRUQsT0FBTyxZQUFZLENBQUM7SUFDdEIsQ0FBQzs7Ozs7OztJQUVPLG1EQUFnQjs7Ozs7O0lBQXhCLFVBQXlCLEtBQXdCLEVBQUUsS0FBYztRQUFqRSxpQkFVQztRQVRDLElBQUksS0FBSyxDQUFDLFVBQVUsRUFBRTtZQUNwQixLQUFLLENBQUMsVUFBVTtpQkFDYixNQUFNOzs7O1lBQUMsVUFBQSxDQUFDLElBQUksT0FBQSxDQUFDLENBQUMsQ0FBQyxvQkFBb0IsSUFBSSxDQUFDLENBQUMsQ0FBQyxvQkFBb0IsQ0FBQyxjQUFjLENBQUMsMEJBQTBCLENBQUMsRUFBN0YsQ0FBNkYsRUFBQztpQkFDMUcsT0FBTzs7OztZQUFDLFVBQUEsQ0FBQyxJQUFJLE9BQUEsS0FBSSxDQUFDLGdCQUFnQixDQUFDLENBQUMsRUFBRSxLQUFLLENBQUMsRUFBL0IsQ0FBK0IsRUFBQyxDQUFDO1NBQ2xEO1FBRUQsSUFBSSxLQUFLLENBQUMsR0FBRyxJQUFJLEtBQUssQ0FBQyxlQUFlLENBQUMsUUFBUSxLQUFLLEtBQUssRUFBRTtZQUN6RCxLQUFLLENBQUMsZUFBZSxDQUFDLFFBQVEsR0FBRyxLQUFLLENBQUM7U0FDeEM7SUFDSCxDQUFDOzs7Ozs7O0lBRU8sb0RBQWlCOzs7Ozs7SUFBekIsVUFBMEIsS0FBNkIsRUFBRSxJQUFhO1FBQXRFLGlCQXNCQztRQXJCQyxJQUFJLEtBQUssQ0FBQyxXQUFXLElBQUksS0FBSyxDQUFDLEdBQUcsRUFBRTtZQUNsQyxnQkFBZ0IsQ0FBQyxLQUFLLEVBQUUsT0FBTyxFQUFFLENBQUMsQ0FBQyxDQUFDLElBQUksSUFBSSxLQUFLLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQzs7Z0JBQ25ELENBQUMsR0FBRyxLQUFLLENBQUMsV0FBVztZQUMzQixJQUFJLENBQUMsQ0FBQyxTQUFTLENBQUMsQ0FBQyxNQUFNLEdBQUcsQ0FBQyxFQUFFO2dCQUMzQixjQUFjLENBQUMsQ0FBQyxDQUFDLENBQUM7YUFDbkI7WUFFRCxJQUFJLEtBQUssSUFBSSxJQUFJLENBQUMsQ0FBQyxTQUFTLENBQUMsQ0FBQyxLQUFLOzs7O1lBQUMsVUFBQSxDQUFDLElBQUksT0FBQSxDQUFDLENBQUMsQ0FBQyxDQUFDLEtBQUssRUFBVCxDQUFTLEVBQUM7Z0JBQ2pELENBQUMsQ0FBQyxpQkFBaUIsQ0FBQyxLQUFLLENBQUM7Z0JBQzFCLENBQUMsQ0FBQyxlQUFlLENBQUMsS0FBSyxDQUFDLENBQUM7U0FDNUI7UUFFRCxJQUFJLEtBQUssQ0FBQyxVQUFVLEVBQUU7WUFDcEIsS0FBSyxDQUFDLFVBQVU7aUJBQ2IsTUFBTTs7OztZQUFDLFVBQUEsQ0FBQyxJQUFJLE9BQUEsQ0FBQyxDQUFDLENBQUMsY0FBYyxFQUFqQixDQUFpQixFQUFDO2lCQUM5QixPQUFPOzs7O1lBQUMsVUFBQSxDQUFDLElBQUksT0FBQSxLQUFJLENBQUMsaUJBQWlCLENBQUMsQ0FBQyxFQUFFLElBQUksQ0FBQyxFQUEvQixDQUErQixFQUFDLENBQUM7U0FDbEQ7UUFFRCxJQUFJLEtBQUssQ0FBQyxPQUFPLENBQUMsWUFBWSxFQUFFO1lBQzlCLEtBQUssQ0FBQyxPQUFPLENBQUMsWUFBWSxDQUFDLElBQUksQ0FBQyxtQkFBeUIsRUFBRSxLQUFLLE9BQUEsRUFBRSxJQUFJLEVBQUUsUUFBUSxFQUFFLEtBQUssRUFBRSxJQUFJLEVBQUUsRUFBQSxDQUFDLENBQUM7U0FDbEc7SUFDSCxDQUFDOzs7Ozs7OztJQUVPLCtDQUFZOzs7Ozs7O0lBQXBCLFVBQXFCLEtBQTZCLEVBQUUsSUFBWSxFQUFFLEtBQVU7UUFDMUUsSUFBSTs7Z0JBQ0UsTUFBTSxHQUFHLEtBQUs7O2dCQUNaLEtBQUssR0FBRyxJQUFJLENBQUMsS0FBSyxDQUFDLEdBQUcsQ0FBQzs7Z0JBQ3ZCLFNBQVMsR0FBRyxLQUFLLENBQUMsTUFBTSxHQUFHLENBQUM7WUFDbEMsS0FBSyxJQUFJLENBQUMsR0FBRyxDQUFDLEVBQUUsQ0FBQyxHQUFHLFNBQVMsRUFBRSxDQUFDLEVBQUUsRUFBRTtnQkFDbEMsTUFBTSxHQUFHLE1BQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUMsQ0FBQzthQUMzQjtZQUVELE1BQU0sQ0FBQyxLQUFLLENBQUMsU0FBUyxDQUFDLENBQUMsR0FBRyxLQUFLLENBQUM7U0FDbEM7UUFBQyxPQUFPLEtBQUssRUFBRTtZQUNkLEtBQUssQ0FBQyxPQUFPLEdBQUcsa0NBQStCLElBQUksWUFBTSxLQUFLLENBQUMsT0FBUyxDQUFDO1lBQ3pFLE1BQU0sS0FBSyxDQUFDO1NBQ2I7UUFFRCxJQUFJLElBQUksS0FBSywwQkFBMEIsSUFBSSxLQUFLLENBQUMsR0FBRyxFQUFFO1lBQ3BELElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxLQUFLLEVBQUUsS0FBSyxDQUFDLENBQUM7U0FDckM7UUFFRCxJQUFJLElBQUksQ0FBQyxPQUFPLENBQUMsUUFBUSxDQUFDLEtBQUssQ0FBQyxFQUFFOztnQkFDMUIsSUFBSSxHQUFHLElBQUksQ0FBQyxPQUFPLENBQUMsVUFBVSxFQUFFLEVBQUUsQ0FBQzs7Z0JBQ3ZDLE9BQU8sR0FBRyxLQUFLLENBQUMsR0FBRyxJQUFJLElBQUksS0FBSyxJQUFJLENBQUMsQ0FBQyxDQUFDLEtBQUssQ0FBQyxXQUFXLENBQUMsQ0FBQyxDQUFDLEtBQUssQ0FBQyxNQUFNLENBQUMsV0FBVyxDQUFDLEdBQUcsQ0FBQyxJQUFJLENBQUM7WUFFL0YsSUFDRSxPQUFPO21CQUNKLENBQUMsQ0FBQyxpQkFBaUIsQ0FBQyxPQUFPLENBQUMsS0FBSyxDQUFDLElBQUksaUJBQWlCLENBQUMsS0FBSyxDQUFDLENBQUM7bUJBQy9ELE9BQU8sQ0FBQyxLQUFLLEtBQUssS0FBSyxFQUMxQjtnQkFDQSxPQUFPLENBQUMsVUFBVSxDQUFDLEtBQUssRUFBRSxFQUFFLFNBQVMsRUFBRSxLQUFLLEVBQUUsQ0FBQyxDQUFDO2FBQ2pEO1NBQ0Y7UUFFRCxJQUFJLENBQUMscUJBQXFCLENBQUMsS0FBSyxFQUFFLElBQUksRUFBRSxLQUFLLENBQUMsQ0FBQztJQUNqRCxDQUFDOzs7Ozs7OztJQUVPLHdEQUFxQjs7Ozs7OztJQUE3QixVQUE4QixLQUE2QixFQUFFLFFBQWdCLEVBQUUsS0FBVTtRQUN2RixJQUFJLENBQUMsS0FBSyxDQUFDLE9BQU8sQ0FBQyxZQUFZLEVBQUU7WUFDL0IsT0FBTztTQUNSO1FBRUQsS0FBSyxDQUFDLE9BQU8sQ0FBQyxZQUFZLENBQUMsSUFBSSxDQUFDO1lBQzlCLEtBQUssRUFBRSxLQUFLO1lBQ1osSUFBSSxFQUFFLG1CQUFtQjtZQUN6QixRQUFRLFVBQUE7WUFDUixLQUFLLE9BQUE7U0FDTixDQUFDLENBQUM7SUFDTCxDQUFDO0lBQ0gsK0JBQUM7QUFBRCxDQUFDLEFBaFJELElBZ1JDIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seVZhbHVlQ2hhbmdlRXZlbnQsIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi8uLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgaXNPYmplY3QsIGlzTnVsbE9yVW5kZWZpbmVkLCBpc0Z1bmN0aW9uLCBkZWZpbmVIaWRkZW5Qcm9wLCB3cmFwUHJvcGVydHksIHJlZHVjZUZvcm1VcGRhdGVWYWxpZGl0eUNhbGxzIH0gZnJvbSAnLi4vLi4vdXRpbHMnO1xuaW1wb3J0IHsgZXZhbEV4cHJlc3Npb24sIGV2YWxTdHJpbmdFeHByZXNzaW9uIH0gZnJvbSAnLi91dGlscyc7XG5pbXBvcnQgeyBPYnNlcnZhYmxlLCBTdWJzY3JpcHRpb24gfSBmcm9tICdyeGpzJztcbmltcG9ydCB7IEZvcm1seUV4dGVuc2lvbiB9IGZyb20gJy4uLy4uL3NlcnZpY2VzL2Zvcm1seS5jb25maWcnO1xuaW1wb3J0IHsgdW5yZWdpc3RlckNvbnRyb2wsIHJlZ2lzdGVyQ29udHJvbCwgdXBkYXRlVmFsaWRpdHkgfSBmcm9tICcuLi9maWVsZC1mb3JtL3V0aWxzJztcblxuLyoqIEBleHBlcmltZW50YWwgKi9cbmV4cG9ydCBjbGFzcyBGaWVsZEV4cHJlc3Npb25FeHRlbnNpb24gaW1wbGVtZW50cyBGb3JtbHlFeHRlbnNpb24ge1xuICBwcmVQb3B1bGF0ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIGlmIChmaWVsZC5wYXJlbnQgfHwgZmllbGQub3B0aW9ucy5fY2hlY2tGaWVsZCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGxldCBjaGVja0xvY2tlZCA9IGZhbHNlO1xuICAgIGZpZWxkLm9wdGlvbnMuX2NoZWNrRmllbGQgPSAoZiwgaWdub3JlQ2FjaGUpID0+IHtcbiAgICAgIGlmICghY2hlY2tMb2NrZWQpIHtcbiAgICAgICAgY2hlY2tMb2NrZWQgPSB0cnVlO1xuICAgICAgICByZWR1Y2VGb3JtVXBkYXRlVmFsaWRpdHlDYWxscyhcbiAgICAgICAgICBmLmZvcm1Db250cm9sLFxuICAgICAgICAgICgpID0+IHRoaXMuY2hlY2tGaWVsZChmLCBpZ25vcmVDYWNoZSksXG4gICAgICAgICk7XG4gICAgICAgIGNoZWNrTG9ja2VkID0gZmFsc2U7XG4gICAgICB9XG4gICAgfTtcbiAgfVxuXG4gIG9uUG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoIWZpZWxkLnBhcmVudCB8fCBmaWVsZC5fZXhwcmVzc2lvblByb3BlcnRpZXMpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICAvLyBjYWNoZSBidWlsdCBleHByZXNzaW9uXG4gICAgZGVmaW5lSGlkZGVuUHJvcChmaWVsZCwgJ19leHByZXNzaW9uUHJvcGVydGllcycsIHt9KTtcblxuICAgIGlmIChmaWVsZC5leHByZXNzaW9uUHJvcGVydGllcykge1xuICAgICAgZm9yIChjb25zdCBrZXkgaW4gZmllbGQuZXhwcmVzc2lvblByb3BlcnRpZXMpIHtcbiAgICAgICAgY29uc3QgZXhwcmVzc2lvblByb3BlcnR5ID0gZmllbGQuZXhwcmVzc2lvblByb3BlcnRpZXNba2V5XTtcblxuICAgICAgICBpZiAodHlwZW9mIGV4cHJlc3Npb25Qcm9wZXJ0eSA9PT0gJ3N0cmluZycgfHwgaXNGdW5jdGlvbihleHByZXNzaW9uUHJvcGVydHkpKSB7XG4gICAgICAgICAgZmllbGQuX2V4cHJlc3Npb25Qcm9wZXJ0aWVzW2tleV0gPSB7XG4gICAgICAgICAgICBleHByZXNzaW9uOiB0aGlzLl9ldmFsRXhwcmVzc2lvbihcbiAgICAgICAgICAgICAgZXhwcmVzc2lvblByb3BlcnR5LFxuICAgICAgICAgICAgICBrZXkgPT09ICd0ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQnICYmIGZpZWxkLnBhcmVudC5leHByZXNzaW9uUHJvcGVydGllcyAmJiBmaWVsZC5wYXJlbnQuZXhwcmVzc2lvblByb3BlcnRpZXMuaGFzT3duUHJvcGVydHkoJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcpXG4gICAgICAgICAgICAgICAgPyAoKSA9PiBmaWVsZC5wYXJlbnQudGVtcGxhdGVPcHRpb25zLmRpc2FibGVkXG4gICAgICAgICAgICAgICAgOiB1bmRlZmluZWQsXG4gICAgICAgICAgICApLFxuICAgICAgICAgIH07XG4gICAgICAgICAgaWYgKGtleSA9PT0gJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcpIHtcbiAgICAgICAgICAgIE9iamVjdC5kZWZpbmVQcm9wZXJ0eShmaWVsZC5fZXhwcmVzc2lvblByb3BlcnRpZXNba2V5XSwgJ2V4cHJlc3Npb25WYWx1ZScsIHtcbiAgICAgICAgICAgICAgZ2V0OiAoKSA9PiBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQsXG4gICAgICAgICAgICAgIHNldDogKCkgPT4geyB9LFxuICAgICAgICAgICAgICBlbnVtZXJhYmxlOiB0cnVlLFxuICAgICAgICAgICAgICBjb25maWd1cmFibGU6IHRydWUsXG4gICAgICAgICAgICB9KTtcbiAgICAgICAgICB9XG4gICAgICAgIH0gZWxzZSBpZiAoZXhwcmVzc2lvblByb3BlcnR5IGluc3RhbmNlb2YgT2JzZXJ2YWJsZSkge1xuICAgICAgICAgIGNvbnN0IHN1YnNjcmliZSA9ICgpID0+IChleHByZXNzaW9uUHJvcGVydHkgYXMgT2JzZXJ2YWJsZTxhbnk+KVxuICAgICAgICAgICAgLnN1YnNjcmliZSh2ID0+IHtcbiAgICAgICAgICAgICAgdGhpcy5zZXRFeHByVmFsdWUoZmllbGQsIGtleSwgdik7XG4gICAgICAgICAgICAgIGlmIChmaWVsZC5vcHRpb25zICYmIGZpZWxkLm9wdGlvbnMuX21hcmtGb3JDaGVjaykge1xuICAgICAgICAgICAgICAgIGZpZWxkLm9wdGlvbnMuX21hcmtGb3JDaGVjayhmaWVsZCk7XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0pO1xuXG4gICAgICAgICAgbGV0IHN1YnNjcmlwdGlvbjogU3Vic2NyaXB0aW9uID0gc3Vic2NyaWJlKCk7XG4gICAgICAgICAgY29uc3Qgb25Jbml0ID0gZmllbGQuaG9va3Mub25Jbml0O1xuICAgICAgICAgIGZpZWxkLmhvb2tzLm9uSW5pdCA9ICgpID0+IHtcbiAgICAgICAgICAgIGlmIChzdWJzY3JpcHRpb24gPT09IG51bGwpIHtcbiAgICAgICAgICAgICAgc3Vic2NyaXB0aW9uID0gc3Vic2NyaWJlKCk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgICByZXR1cm4gb25Jbml0ICYmIG9uSW5pdChmaWVsZCk7XG4gICAgICAgICAgfTtcblxuICAgICAgICAgIGNvbnN0IG9uRGVzdHJveSA9IGZpZWxkLmhvb2tzLm9uRGVzdHJveTtcbiAgICAgICAgICBmaWVsZC5ob29rcy5vbkRlc3Ryb3kgPSAoKSA9PiB7XG4gICAgICAgICAgICBvbkRlc3Ryb3kgJiYgb25EZXN0cm95KGZpZWxkKTtcbiAgICAgICAgICAgIHN1YnNjcmlwdGlvbi51bnN1YnNjcmliZSgpO1xuICAgICAgICAgICAgc3Vic2NyaXB0aW9uID0gbnVsbDtcbiAgICAgICAgICB9O1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLmhpZGVFeHByZXNzaW9uKSB7XG4gICAgICAvLyBkZWxldGUgaGlkZSB2YWx1ZSBpbiBvcmRlciB0byBmb3JjZSByZS1ldmFsdWF0ZSBpdCBpbiBGb3JtbHlGb3JtRXhwcmVzc2lvbi5cbiAgICAgIGRlbGV0ZSBmaWVsZC5oaWRlO1xuXG4gICAgICBsZXQgcGFyZW50ID0gZmllbGQucGFyZW50O1xuICAgICAgd2hpbGUgKHBhcmVudCAmJiAhcGFyZW50LmhpZGVFeHByZXNzaW9uKSB7XG4gICAgICAgIHBhcmVudCA9IHBhcmVudC5wYXJlbnQ7XG4gICAgICB9XG5cbiAgICAgIGZpZWxkLmhpZGVFeHByZXNzaW9uID0gdGhpcy5fZXZhbEV4cHJlc3Npb24oXG4gICAgICAgIGZpZWxkLmhpZGVFeHByZXNzaW9uLFxuICAgICAgICBwYXJlbnQgJiYgcGFyZW50LmhpZGVFeHByZXNzaW9uID8gKCkgPT4gcGFyZW50LmhpZGUgOiB1bmRlZmluZWQsXG4gICAgICApO1xuICAgIH0gZWxzZSB7XG4gICAgICB3cmFwUHJvcGVydHkoZmllbGQsICdoaWRlJywgKHsgY3VycmVudFZhbHVlLCBmaXJzdENoYW5nZSB9KSA9PiB7XG4gICAgICAgIGZpZWxkLl9oaWRlID0gY3VycmVudFZhbHVlO1xuICAgICAgICBpZiAoIWZpcnN0Q2hhbmdlIHx8IChmaXJzdENoYW5nZSAmJiBjdXJyZW50VmFsdWUgPT09IHRydWUpKSB7XG4gICAgICAgICAgZmllbGQub3B0aW9ucy5faGlkZGVuRmllbGRzRm9yQ2hlY2sucHVzaChmaWVsZCk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgX2V2YWxFeHByZXNzaW9uKGV4cHJlc3Npb24sIHBhcmVudEV4cHJlc3Npb24/KSB7XG4gICAgZXhwcmVzc2lvbiA9IGV4cHJlc3Npb24gfHwgKCgpID0+IGZhbHNlKTtcbiAgICBpZiAodHlwZW9mIGV4cHJlc3Npb24gPT09ICdzdHJpbmcnKSB7XG4gICAgICBleHByZXNzaW9uID0gZXZhbFN0cmluZ0V4cHJlc3Npb24oZXhwcmVzc2lvbiwgWydtb2RlbCcsICdmb3JtU3RhdGUnLCAnZmllbGQnXSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHBhcmVudEV4cHJlc3Npb25cbiAgICAgID8gKG1vZGVsOiBhbnksIGZvcm1TdGF0ZTogYW55LCBmaWVsZDogRm9ybWx5RmllbGRDb25maWcpID0+IHBhcmVudEV4cHJlc3Npb24oKSB8fCBleHByZXNzaW9uKG1vZGVsLCBmb3JtU3RhdGUsIGZpZWxkKVxuICAgICAgOiBleHByZXNzaW9uO1xuICB9XG5cbiAgcHJpdmF0ZSBjaGVja0ZpZWxkKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBpZ25vcmVDYWNoZSA9IGZhbHNlKSB7XG4gICAgdGhpcy5fY2hlY2tGaWVsZChmaWVsZCwgaWdub3JlQ2FjaGUpO1xuXG4gICAgZmllbGQub3B0aW9ucy5faGlkZGVuRmllbGRzRm9yQ2hlY2tcbiAgICAgIC5zb3J0KGYgPT4gZi5oaWRlID8gLTEgOiAxKVxuICAgICAgLmZvckVhY2goZiA9PiB0aGlzLnRvZ2dsZUZvcm1Db250cm9sKGYsIGYuaGlkZSkpO1xuXG4gICAgZmllbGQub3B0aW9ucy5faGlkZGVuRmllbGRzRm9yQ2hlY2sgPSBbXTtcbiAgfVxuXG4gIHByaXZhdGUgX2NoZWNrRmllbGQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGlnbm9yZUNhY2hlID0gZmFsc2UpIHtcbiAgICBsZXQgbWFya0ZvckNoZWNrID0gZmFsc2U7XG4gICAgZmllbGQuZmllbGRHcm91cC5mb3JFYWNoKGYgPT4ge1xuICAgICAgdGhpcy5jaGVja0ZpZWxkRXhwcmVzc2lvbkNoYW5nZShmLCBpZ25vcmVDYWNoZSkgJiYgKG1hcmtGb3JDaGVjayA9IHRydWUpO1xuICAgICAgaWYgKHRoaXMuY2hlY2tGaWVsZFZpc2liaWxpdHlDaGFuZ2UoZiwgaWdub3JlQ2FjaGUpKSB7XG4gICAgICAgIGZpZWxkLm9wdGlvbnMuX2hpZGRlbkZpZWxkc0ZvckNoZWNrLnB1c2goZik7XG4gICAgICAgIG1hcmtGb3JDaGVjayA9IHRydWU7XG4gICAgICB9XG5cbiAgICAgIGlmIChmLmZpZWxkR3JvdXAgJiYgZi5maWVsZEdyb3VwLmxlbmd0aCA+IDApIHtcbiAgICAgICAgdGhpcy5fY2hlY2tGaWVsZChmLCBpZ25vcmVDYWNoZSk7XG4gICAgICB9XG4gICAgfSk7XG5cbiAgICBpZiAobWFya0ZvckNoZWNrICYmIGZpZWxkLm9wdGlvbnMgJiYgZmllbGQub3B0aW9ucy5fbWFya0ZvckNoZWNrKSB7XG4gICAgICBmaWVsZC5vcHRpb25zLl9tYXJrRm9yQ2hlY2soZmllbGQpO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgY2hlY2tGaWVsZEV4cHJlc3Npb25DaGFuZ2UoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGlnbm9yZUNhY2hlKTogYm9vbGVhbiB7XG4gICAgaWYgKCFmaWVsZCB8fCAhZmllbGQuX2V4cHJlc3Npb25Qcm9wZXJ0aWVzKSB7XG4gICAgICByZXR1cm4gZmFsc2U7XG4gICAgfVxuXG4gICAgbGV0IG1hcmtGb3JDaGVjayA9IGZhbHNlO1xuICAgIGNvbnN0IGV4cHJlc3Npb25Qcm9wZXJ0aWVzID0gZmllbGQuX2V4cHJlc3Npb25Qcm9wZXJ0aWVzO1xuXG4gICAgZm9yIChjb25zdCBrZXkgaW4gZXhwcmVzc2lvblByb3BlcnRpZXMpIHtcbiAgICAgIGxldCBleHByZXNzaW9uVmFsdWUgPSBldmFsRXhwcmVzc2lvbihleHByZXNzaW9uUHJvcGVydGllc1trZXldLmV4cHJlc3Npb24sIHsgZmllbGQgfSwgW2ZpZWxkLm1vZGVsLCBmaWVsZC5vcHRpb25zLmZvcm1TdGF0ZSwgZmllbGRdKTtcbiAgICAgIGlmIChrZXkgPT09ICd0ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQnKSB7XG4gICAgICAgIGV4cHJlc3Npb25WYWx1ZSA9ICEhZXhwcmVzc2lvblZhbHVlO1xuICAgICAgfVxuXG4gICAgICBpZiAoXG4gICAgICAgIGlnbm9yZUNhY2hlIHx8IChcbiAgICAgICAgICBleHByZXNzaW9uUHJvcGVydGllc1trZXldLmV4cHJlc3Npb25WYWx1ZSAhPT0gZXhwcmVzc2lvblZhbHVlXG4gICAgICAgICAgJiYgKCFpc09iamVjdChleHByZXNzaW9uVmFsdWUpIHx8IEpTT04uc3RyaW5naWZ5KGV4cHJlc3Npb25WYWx1ZSkgIT09IEpTT04uc3RyaW5naWZ5KGV4cHJlc3Npb25Qcm9wZXJ0aWVzW2tleV0uZXhwcmVzc2lvblZhbHVlKSlcbiAgICAgICAgKVxuICAgICAgKSB7XG4gICAgICAgIG1hcmtGb3JDaGVjayA9IHRydWU7XG4gICAgICAgIGV4cHJlc3Npb25Qcm9wZXJ0aWVzW2tleV0uZXhwcmVzc2lvblZhbHVlID0gZXhwcmVzc2lvblZhbHVlO1xuICAgICAgICB0aGlzLnNldEV4cHJWYWx1ZShmaWVsZCwga2V5LCBleHByZXNzaW9uVmFsdWUpO1xuICAgICAgfVxuICAgIH1cblxuICAgIHJldHVybiBtYXJrRm9yQ2hlY2s7XG4gIH1cblxuICBwcml2YXRlIGNoZWNrRmllbGRWaXNpYmlsaXR5Q2hhbmdlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBpZ25vcmVDYWNoZSk6IGJvb2xlYW4ge1xuICAgIGlmICghZmllbGQgfHwgaXNOdWxsT3JVbmRlZmluZWQoZmllbGQuaGlkZUV4cHJlc3Npb24pKSB7XG4gICAgICByZXR1cm4gZmFsc2U7XG4gICAgfVxuXG4gICAgY29uc3QgaGlkZUV4cHJlc3Npb25SZXN1bHQ6IGJvb2xlYW4gPSAhIWV2YWxFeHByZXNzaW9uKFxuICAgICAgZmllbGQuaGlkZUV4cHJlc3Npb24sXG4gICAgICB7IGZpZWxkIH0sXG4gICAgICBbZmllbGQubW9kZWwsIGZpZWxkLm9wdGlvbnMuZm9ybVN0YXRlLCBmaWVsZF0sXG4gICAgKTtcbiAgICBsZXQgbWFya0ZvckNoZWNrID0gZmFsc2U7XG4gICAgaWYgKGhpZGVFeHByZXNzaW9uUmVzdWx0ICE9PSBmaWVsZC5oaWRlIHx8IGlnbm9yZUNhY2hlKSB7XG4gICAgICBtYXJrRm9yQ2hlY2sgPSB0cnVlO1xuICAgICAgLy8gdG9nZ2xlIGhpZGVcbiAgICAgIGZpZWxkLmhpZGUgPSBoaWRlRXhwcmVzc2lvblJlc3VsdDtcbiAgICAgIGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5oaWRkZW4gPSBoaWRlRXhwcmVzc2lvblJlc3VsdDtcbiAgICB9XG5cbiAgICByZXR1cm4gbWFya0ZvckNoZWNrO1xuICB9XG5cbiAgcHJpdmF0ZSBzZXREaXNhYmxlZFN0YXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZywgdmFsdWU6IGJvb2xlYW4pIHtcbiAgICBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgZmllbGQuZmllbGRHcm91cFxuICAgICAgICAuZmlsdGVyKGYgPT4gIWYuZXhwcmVzc2lvblByb3BlcnRpZXMgfHwgIWYuZXhwcmVzc2lvblByb3BlcnRpZXMuaGFzT3duUHJvcGVydHkoJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcpKVxuICAgICAgICAuZm9yRWFjaChmID0+IHRoaXMuc2V0RGlzYWJsZWRTdGF0ZShmLCB2YWx1ZSkpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZC5rZXkgJiYgZmllbGQudGVtcGxhdGVPcHRpb25zLmRpc2FibGVkICE9PSB2YWx1ZSkge1xuICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zLmRpc2FibGVkID0gdmFsdWU7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSB0b2dnbGVGb3JtQ29udHJvbChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgaGlkZTogYm9vbGVhbikge1xuICAgIGlmIChmaWVsZC5mb3JtQ29udHJvbCAmJiBmaWVsZC5rZXkpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3AoZmllbGQsICdfaGlkZScsICEhKGhpZGUgfHwgZmllbGQuaGlkZSkpO1xuICAgICAgY29uc3QgYyA9IGZpZWxkLmZvcm1Db250cm9sO1xuICAgICAgaWYgKGNbJ19maWVsZHMnXS5sZW5ndGggPiAxKSB7XG4gICAgICAgIHVwZGF0ZVZhbGlkaXR5KGMpO1xuICAgICAgfVxuXG4gICAgICBoaWRlID09PSB0cnVlICYmIGNbJ19maWVsZHMnXS5ldmVyeShmID0+ICEhZi5faGlkZSlcbiAgICAgICAgPyB1bnJlZ2lzdGVyQ29udHJvbChmaWVsZClcbiAgICAgICAgOiByZWdpc3RlckNvbnRyb2woZmllbGQpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICBmaWVsZC5maWVsZEdyb3VwXG4gICAgICAgIC5maWx0ZXIoZiA9PiAhZi5oaWRlRXhwcmVzc2lvbilcbiAgICAgICAgLmZvckVhY2goZiA9PiB0aGlzLnRvZ2dsZUZvcm1Db250cm9sKGYsIGhpZGUpKTtcbiAgICB9XG5cbiAgICBpZiAoZmllbGQub3B0aW9ucy5maWVsZENoYW5nZXMpIHtcbiAgICAgIGZpZWxkLm9wdGlvbnMuZmllbGRDaGFuZ2VzLm5leHQoPEZvcm1seVZhbHVlQ2hhbmdlRXZlbnQ+IHsgZmllbGQsIHR5cGU6ICdoaWRkZW4nLCB2YWx1ZTogaGlkZSB9KTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHNldEV4cHJWYWx1ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgcHJvcDogc3RyaW5nLCB2YWx1ZTogYW55KSB7XG4gICAgdHJ5IHtcbiAgICAgIGxldCB0YXJnZXQgPSBmaWVsZDtcbiAgICAgIGNvbnN0IHBhdGhzID0gcHJvcC5zcGxpdCgnLicpO1xuICAgICAgY29uc3QgbGFzdEluZGV4ID0gcGF0aHMubGVuZ3RoIC0gMTtcbiAgICAgIGZvciAobGV0IGkgPSAwOyBpIDwgbGFzdEluZGV4OyBpKyspIHtcbiAgICAgICAgdGFyZ2V0ID0gdGFyZ2V0W3BhdGhzW2ldXTtcbiAgICAgIH1cblxuICAgICAgdGFyZ2V0W3BhdGhzW2xhc3RJbmRleF1dID0gdmFsdWU7XG4gICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgIGVycm9yLm1lc3NhZ2UgPSBgW0Zvcm1seSBFcnJvcl0gW0V4cHJlc3Npb24gXCIke3Byb3B9XCJdICR7ZXJyb3IubWVzc2FnZX1gO1xuICAgICAgdGhyb3cgZXJyb3I7XG4gICAgfVxuXG4gICAgaWYgKHByb3AgPT09ICd0ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQnICYmIGZpZWxkLmtleSkge1xuICAgICAgdGhpcy5zZXREaXNhYmxlZFN0YXRlKGZpZWxkLCB2YWx1ZSk7XG4gICAgfVxuXG4gICAgaWYgKHByb3AuaW5kZXhPZignbW9kZWwuJykgPT09IDApIHtcbiAgICAgIGNvbnN0IHBhdGggPSBwcm9wLnJlcGxhY2UoL15tb2RlbFxcLi8sICcnKSxcbiAgICAgICAgY29udHJvbCA9IGZpZWxkLmtleSAmJiBwcm9wID09PSBwYXRoID8gZmllbGQuZm9ybUNvbnRyb2wgOiBmaWVsZC5wYXJlbnQuZm9ybUNvbnRyb2wuZ2V0KHBhdGgpO1xuXG4gICAgICBpZiAoXG4gICAgICAgIGNvbnRyb2xcbiAgICAgICAgJiYgIShpc051bGxPclVuZGVmaW5lZChjb250cm9sLnZhbHVlKSAmJiBpc051bGxPclVuZGVmaW5lZCh2YWx1ZSkpXG4gICAgICAgICYmIGNvbnRyb2wudmFsdWUgIT09IHZhbHVlXG4gICAgICApIHtcbiAgICAgICAgY29udHJvbC5wYXRjaFZhbHVlKHZhbHVlLCB7IGVtaXRFdmVudDogZmFsc2UgfSk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgdGhpcy5lbWl0RXhwcmVzc2lvbkNoYW5nZXMoZmllbGQsIHByb3AsIHZhbHVlKTtcbiAgfVxuXG4gIHByaXZhdGUgZW1pdEV4cHJlc3Npb25DaGFuZ2VzKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBwcm9wZXJ0eTogc3RyaW5nLCB2YWx1ZTogYW55KSB7XG4gICAgaWYgKCFmaWVsZC5vcHRpb25zLmZpZWxkQ2hhbmdlcykge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGZpZWxkLm9wdGlvbnMuZmllbGRDaGFuZ2VzLm5leHQoe1xuICAgICAgZmllbGQ6IGZpZWxkLFxuICAgICAgdHlwZTogJ2V4cHJlc3Npb25DaGFuZ2VzJyxcbiAgICAgIHByb3BlcnR5LFxuICAgICAgdmFsdWUsXG4gICAgfSk7XG4gIH1cbn1cbiJdfQ==