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
export class FieldExpressionExtension {
    /**
     * @param {?} field
     * @return {?}
     */
    prePopulate(field) {
        if (field.parent || field.options._checkField) {
            return;
        }
        /** @type {?} */
        let checkLocked = false;
        field.options._checkField = (/**
         * @param {?} f
         * @param {?} ignoreCache
         * @return {?}
         */
        (f, ignoreCache) => {
            if (!checkLocked) {
                checkLocked = true;
                reduceFormUpdateValidityCalls(f.formControl, (/**
                 * @return {?}
                 */
                () => this.checkField(f, ignoreCache)));
                checkLocked = false;
            }
        });
    }
    /**
     * @param {?} field
     * @return {?}
     */
    onPopulate(field) {
        if (!field.parent || field._expressionProperties) {
            return;
        }
        // cache built expression
        defineHiddenProp(field, '_expressionProperties', {});
        if (field.expressionProperties) {
            for (const key in field.expressionProperties) {
                /** @type {?} */
                const expressionProperty = field.expressionProperties[key];
                if (typeof expressionProperty === 'string' || isFunction(expressionProperty)) {
                    field._expressionProperties[key] = {
                        expression: this._evalExpression(expressionProperty, key === 'templateOptions.disabled' && field.parent.expressionProperties && field.parent.expressionProperties.hasOwnProperty('templateOptions.disabled')
                            ? (/**
                             * @return {?}
                             */
                            () => field.parent.templateOptions.disabled)
                            : undefined),
                    };
                    if (key === 'templateOptions.disabled') {
                        Object.defineProperty(field._expressionProperties[key], 'expressionValue', {
                            get: (/**
                             * @return {?}
                             */
                            () => field.templateOptions.disabled),
                            set: (/**
                             * @return {?}
                             */
                            () => { }),
                            enumerable: true,
                            configurable: true,
                        });
                    }
                }
                else if (expressionProperty instanceof Observable) {
                    /** @type {?} */
                    const subscribe = (/**
                     * @return {?}
                     */
                    () => ((/** @type {?} */ (expressionProperty)))
                        .subscribe((/**
                     * @param {?} v
                     * @return {?}
                     */
                    v => {
                        this.setExprValue(field, key, v);
                        if (field.options && field.options._markForCheck) {
                            field.options._markForCheck(field);
                        }
                    })));
                    /** @type {?} */
                    let subscription = subscribe();
                    /** @type {?} */
                    const onInit = field.hooks.onInit;
                    field.hooks.onInit = (/**
                     * @return {?}
                     */
                    () => {
                        if (subscription === null) {
                            subscription = subscribe();
                        }
                        return onInit && onInit(field);
                    });
                    /** @type {?} */
                    const onDestroy = field.hooks.onDestroy;
                    field.hooks.onDestroy = (/**
                     * @return {?}
                     */
                    () => {
                        onDestroy && onDestroy(field);
                        subscription.unsubscribe();
                        subscription = null;
                    });
                }
            }
        }
        if (field.hideExpression) {
            // delete hide value in order to force re-evaluate it in FormlyFormExpression.
            delete field.hide;
            /** @type {?} */
            let parent = field.parent;
            while (parent && !parent.hideExpression) {
                parent = parent.parent;
            }
            field.hideExpression = this._evalExpression(field.hideExpression, parent && parent.hideExpression ? (/**
             * @return {?}
             */
            () => parent.hide) : undefined);
        }
        else {
            wrapProperty(field, 'hide', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ currentValue, firstChange }) => {
                field._hide = currentValue;
                if (!firstChange || (firstChange && currentValue === true)) {
                    field.options._hiddenFieldsForCheck.push(field);
                }
            }));
        }
    }
    /**
     * @private
     * @param {?} expression
     * @param {?=} parentExpression
     * @return {?}
     */
    _evalExpression(expression, parentExpression) {
        expression = expression || ((/**
         * @return {?}
         */
        () => false));
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
            (model, formState, field) => parentExpression() || expression(model, formState, field))
            : expression;
    }
    /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    checkField(field, ignoreCache = false) {
        this._checkField(field, ignoreCache);
        field.options._hiddenFieldsForCheck
            .sort((/**
         * @param {?} f
         * @return {?}
         */
        f => f.hide ? -1 : 1))
            .forEach((/**
         * @param {?} f
         * @return {?}
         */
        f => this.toggleFormControl(f, f.hide)));
        field.options._hiddenFieldsForCheck = [];
    }
    /**
     * @private
     * @param {?} field
     * @param {?=} ignoreCache
     * @return {?}
     */
    _checkField(field, ignoreCache = false) {
        /** @type {?} */
        let markForCheck = false;
        field.fieldGroup.forEach((/**
         * @param {?} f
         * @return {?}
         */
        f => {
            this.checkFieldExpressionChange(f, ignoreCache) && (markForCheck = true);
            if (this.checkFieldVisibilityChange(f, ignoreCache)) {
                field.options._hiddenFieldsForCheck.push(f);
                markForCheck = true;
            }
            if (f.fieldGroup && f.fieldGroup.length > 0) {
                this._checkField(f, ignoreCache);
            }
        }));
        if (markForCheck && field.options && field.options._markForCheck) {
            field.options._markForCheck(field);
        }
    }
    /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    checkFieldExpressionChange(field, ignoreCache) {
        if (!field || !field._expressionProperties) {
            return false;
        }
        /** @type {?} */
        let markForCheck = false;
        /** @type {?} */
        const expressionProperties = field._expressionProperties;
        for (const key in expressionProperties) {
            /** @type {?} */
            let expressionValue = evalExpression(expressionProperties[key].expression, { field }, [field.model, field.options.formState, field]);
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
    }
    /**
     * @private
     * @param {?} field
     * @param {?} ignoreCache
     * @return {?}
     */
    checkFieldVisibilityChange(field, ignoreCache) {
        if (!field || isNullOrUndefined(field.hideExpression)) {
            return false;
        }
        /** @type {?} */
        const hideExpressionResult = !!evalExpression(field.hideExpression, { field }, [field.model, field.options.formState, field]);
        /** @type {?} */
        let markForCheck = false;
        if (hideExpressionResult !== field.hide || ignoreCache) {
            markForCheck = true;
            // toggle hide
            field.hide = hideExpressionResult;
            field.templateOptions.hidden = hideExpressionResult;
        }
        return markForCheck;
    }
    /**
     * @private
     * @param {?} field
     * @param {?} value
     * @return {?}
     */
    setDisabledState(field, value) {
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            f => !f.expressionProperties || !f.expressionProperties.hasOwnProperty('templateOptions.disabled')))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            f => this.setDisabledState(f, value)));
        }
        if (field.key && field.templateOptions.disabled !== value) {
            field.templateOptions.disabled = value;
        }
    }
    /**
     * @private
     * @param {?} field
     * @param {?} hide
     * @return {?}
     */
    toggleFormControl(field, hide) {
        if (field.formControl && field.key) {
            defineHiddenProp(field, '_hide', !!(hide || field.hide));
            /** @type {?} */
            const c = field.formControl;
            if (c['_fields'].length > 1) {
                updateValidity(c);
            }
            hide === true && c['_fields'].every((/**
             * @param {?} f
             * @return {?}
             */
            f => !!f._hide))
                ? unregisterControl(field)
                : registerControl(field);
        }
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            f => !f.hideExpression))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            f => this.toggleFormControl(f, hide)));
        }
        if (field.options.fieldChanges) {
            field.options.fieldChanges.next((/** @type {?} */ ({ field, type: 'hidden', value: hide })));
        }
    }
    /**
     * @private
     * @param {?} field
     * @param {?} prop
     * @param {?} value
     * @return {?}
     */
    setExprValue(field, prop, value) {
        try {
            /** @type {?} */
            let target = field;
            /** @type {?} */
            const paths = prop.split('.');
            /** @type {?} */
            const lastIndex = paths.length - 1;
            for (let i = 0; i < lastIndex; i++) {
                target = target[paths[i]];
            }
            target[paths[lastIndex]] = value;
        }
        catch (error) {
            error.message = `[Formly Error] [Expression "${prop}"] ${error.message}`;
            throw error;
        }
        if (prop === 'templateOptions.disabled' && field.key) {
            this.setDisabledState(field, value);
        }
        if (prop.indexOf('model.') === 0) {
            /** @type {?} */
            const path = prop.replace(/^model\./, '');
            /** @type {?} */
            const control = field.key && prop === path ? field.formControl : field.parent.formControl.get(path);
            if (control
                && !(isNullOrUndefined(control.value) && isNullOrUndefined(value))
                && control.value !== value) {
                control.patchValue(value, { emitEvent: false });
            }
        }
        this.emitExpressionChanges(field, prop, value);
    }
    /**
     * @private
     * @param {?} field
     * @param {?} property
     * @param {?} value
     * @return {?}
     */
    emitExpressionChanges(field, property, value) {
        if (!field.options.fieldChanges) {
            return;
        }
        field.options.fieldChanges.next({
            field: field,
            type: 'expressionChanges',
            property,
            value,
        });
    }
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtZXhwcmVzc2lvbi5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC1leHByZXNzaW9uL2ZpZWxkLWV4cHJlc3Npb24udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUNBLE9BQU8sRUFBRSxRQUFRLEVBQUUsaUJBQWlCLEVBQUUsVUFBVSxFQUFFLGdCQUFnQixFQUFFLFlBQVksRUFBRSw2QkFBNkIsRUFBRSxNQUFNLGFBQWEsQ0FBQztBQUNySSxPQUFPLEVBQUUsY0FBYyxFQUFFLG9CQUFvQixFQUFFLE1BQU0sU0FBUyxDQUFDO0FBQy9ELE9BQU8sRUFBRSxVQUFVLEVBQWdCLE1BQU0sTUFBTSxDQUFDO0FBRWhELE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxlQUFlLEVBQUUsY0FBYyxFQUFFLE1BQU0scUJBQXFCLENBQUM7Ozs7QUFHekYsTUFBTSxPQUFPLHdCQUF3Qjs7Ozs7SUFDbkMsV0FBVyxDQUFDLEtBQTZCO1FBQ3ZDLElBQUksS0FBSyxDQUFDLE1BQU0sSUFBSSxLQUFLLENBQUMsT0FBTyxDQUFDLFdBQVcsRUFBRTtZQUM3QyxPQUFPO1NBQ1I7O1lBRUcsV0FBVyxHQUFHLEtBQUs7UUFDdkIsS0FBSyxDQUFDLE9BQU8sQ0FBQyxXQUFXOzs7OztRQUFHLENBQUMsQ0FBQyxFQUFFLFdBQVcsRUFBRSxFQUFFO1lBQzdDLElBQUksQ0FBQyxXQUFXLEVBQUU7Z0JBQ2hCLFdBQVcsR0FBRyxJQUFJLENBQUM7Z0JBQ25CLDZCQUE2QixDQUMzQixDQUFDLENBQUMsV0FBVzs7O2dCQUNiLEdBQUcsRUFBRSxDQUFDLElBQUksQ0FBQyxVQUFVLENBQUMsQ0FBQyxFQUFFLFdBQVcsQ0FBQyxFQUN0QyxDQUFDO2dCQUNGLFdBQVcsR0FBRyxLQUFLLENBQUM7YUFDckI7UUFDSCxDQUFDLENBQUEsQ0FBQztJQUNKLENBQUM7Ozs7O0lBRUQsVUFBVSxDQUFDLEtBQTZCO1FB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LENBQUMsS0FBSyxDQUFDLEVBQUU7O2tCQUMxQixJQUFJLEdBQUcsSUFBSSxDQUFDLE9BQU8sQ0FBQyxVQUFVLEVBQUUsRUFBRSxDQUFDOztrQkFDdkMsT0FBTyxHQUFHLEtBQUssQ0FBQyxHQUFHLElBQUksSUFBSSxLQUFLLElBQUksQ0FBQyxDQUFDLENBQUMsS0FBSyxDQUFDLFdBQVcsQ0FBQyxDQUFDLENBQUMsS0FBSyxDQUFDLE1BQU0sQ0FBQyxXQUFXLENBQUMsR0FBRyxDQUFDLElBQUksQ0FBQztZQUUvRixJQUNFLE9BQU87bUJBQ0osQ0FBQyxDQUFDLGlCQUFpQixDQUFDLE9BQU8sQ0FBQyxLQUFLLENBQUMsSUFBSSxpQkFBaUIsQ0FBQyxLQUFLLENBQUMsQ0FBQzttQkFDL0QsT0FBTyxDQUFDLEtBQUssS0FBSyxLQUFLLEVBQzFCO2dCQUNBLE9BQU8sQ0FBQyxVQUFVLENBQUMsS0FBSyxFQUFFLEVBQUUsU0FBUyxFQUFFLEtBQUssRUFBRSxDQUFDLENBQUM7YUFDakQ7U0FDRjtRQUVELElBQUksQ0FBQyxxQkFBcUIsQ0FBQyxLQUFLLEVBQUUsSUFBSSxFQUFFLEtBQUssQ0FBQyxDQUFDO0lBQ2pELENBQUM7Ozs7Ozs7O0lBRU8scUJBQXFCLENBQUMsS0FBNkIsRUFBRSxRQUFnQixFQUFFLEtBQVU7UUFDdkYsSUFBSSxDQUFDLEtBQUssQ0FBQyxPQUFPLENBQUMsWUFBWSxFQUFFO1lBQy9CLE9BQU87U0FDUjtRQUVELEtBQUssQ0FBQyxPQUFPLENBQUMsWUFBWSxDQUFDLElBQUksQ0FBQztZQUM5QixLQUFLLEVBQUUsS0FBSztZQUNaLElBQUksRUFBRSxtQkFBbUI7WUFDekIsUUFBUTtZQUNSLEtBQUs7U0FDTixDQUFDLENBQUM7SUFDTCxDQUFDO0NBQ0YiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZywgRm9ybWx5VmFsdWVDaGFuZ2VFdmVudCwgRm9ybWx5RmllbGRDb25maWdDYWNoZSB9IGZyb20gJy4uLy4uL2NvbXBvbmVudHMvZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBpc09iamVjdCwgaXNOdWxsT3JVbmRlZmluZWQsIGlzRnVuY3Rpb24sIGRlZmluZUhpZGRlblByb3AsIHdyYXBQcm9wZXJ0eSwgcmVkdWNlRm9ybVVwZGF0ZVZhbGlkaXR5Q2FsbHMgfSBmcm9tICcuLi8uLi91dGlscyc7XG5pbXBvcnQgeyBldmFsRXhwcmVzc2lvbiwgZXZhbFN0cmluZ0V4cHJlc3Npb24gfSBmcm9tICcuL3V0aWxzJztcbmltcG9ydCB7IE9ic2VydmFibGUsIFN1YnNjcmlwdGlvbiB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgRm9ybWx5RXh0ZW5zaW9uIH0gZnJvbSAnLi4vLi4vc2VydmljZXMvZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyB1bnJlZ2lzdGVyQ29udHJvbCwgcmVnaXN0ZXJDb250cm9sLCB1cGRhdGVWYWxpZGl0eSB9IGZyb20gJy4uL2ZpZWxkLWZvcm0vdXRpbHMnO1xuXG4vKiogQGV4cGVyaW1lbnRhbCAqL1xuZXhwb3J0IGNsYXNzIEZpZWxkRXhwcmVzc2lvbkV4dGVuc2lvbiBpbXBsZW1lbnRzIEZvcm1seUV4dGVuc2lvbiB7XG4gIHByZVBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgaWYgKGZpZWxkLnBhcmVudCB8fCBmaWVsZC5vcHRpb25zLl9jaGVja0ZpZWxkKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgbGV0IGNoZWNrTG9ja2VkID0gZmFsc2U7XG4gICAgZmllbGQub3B0aW9ucy5fY2hlY2tGaWVsZCA9IChmLCBpZ25vcmVDYWNoZSkgPT4ge1xuICAgICAgaWYgKCFjaGVja0xvY2tlZCkge1xuICAgICAgICBjaGVja0xvY2tlZCA9IHRydWU7XG4gICAgICAgIHJlZHVjZUZvcm1VcGRhdGVWYWxpZGl0eUNhbGxzKFxuICAgICAgICAgIGYuZm9ybUNvbnRyb2wsXG4gICAgICAgICAgKCkgPT4gdGhpcy5jaGVja0ZpZWxkKGYsIGlnbm9yZUNhY2hlKSxcbiAgICAgICAgKTtcbiAgICAgICAgY2hlY2tMb2NrZWQgPSBmYWxzZTtcbiAgICAgIH1cbiAgICB9O1xuICB9XG5cbiAgb25Qb3B1bGF0ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIGlmICghZmllbGQucGFyZW50IHx8IGZpZWxkLl9leHByZXNzaW9uUHJvcGVydGllcykge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIC8vIGNhY2hlIGJ1aWx0IGV4cHJlc3Npb25cbiAgICBkZWZpbmVIaWRkZW5Qcm9wKGZpZWxkLCAnX2V4cHJlc3Npb25Qcm9wZXJ0aWVzJywge30pO1xuXG4gICAgaWYgKGZpZWxkLmV4cHJlc3Npb25Qcm9wZXJ0aWVzKSB7XG4gICAgICBmb3IgKGNvbnN0IGtleSBpbiBmaWVsZC5leHByZXNzaW9uUHJvcGVydGllcykge1xuICAgICAgICBjb25zdCBleHByZXNzaW9uUHJvcGVydHkgPSBmaWVsZC5leHByZXNzaW9uUHJvcGVydGllc1trZXldO1xuXG4gICAgICAgIGlmICh0eXBlb2YgZXhwcmVzc2lvblByb3BlcnR5ID09PSAnc3RyaW5nJyB8fCBpc0Z1bmN0aW9uKGV4cHJlc3Npb25Qcm9wZXJ0eSkpIHtcbiAgICAgICAgICBmaWVsZC5fZXhwcmVzc2lvblByb3BlcnRpZXNba2V5XSA9IHtcbiAgICAgICAgICAgIGV4cHJlc3Npb246IHRoaXMuX2V2YWxFeHByZXNzaW9uKFxuICAgICAgICAgICAgICBleHByZXNzaW9uUHJvcGVydHksXG4gICAgICAgICAgICAgIGtleSA9PT0gJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcgJiYgZmllbGQucGFyZW50LmV4cHJlc3Npb25Qcm9wZXJ0aWVzICYmIGZpZWxkLnBhcmVudC5leHByZXNzaW9uUHJvcGVydGllcy5oYXNPd25Qcm9wZXJ0eSgndGVtcGxhdGVPcHRpb25zLmRpc2FibGVkJylcbiAgICAgICAgICAgICAgICA/ICgpID0+IGZpZWxkLnBhcmVudC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWRcbiAgICAgICAgICAgICAgICA6IHVuZGVmaW5lZCxcbiAgICAgICAgICAgICksXG4gICAgICAgICAgfTtcbiAgICAgICAgICBpZiAoa2V5ID09PSAndGVtcGxhdGVPcHRpb25zLmRpc2FibGVkJykge1xuICAgICAgICAgICAgT2JqZWN0LmRlZmluZVByb3BlcnR5KGZpZWxkLl9leHByZXNzaW9uUHJvcGVydGllc1trZXldLCAnZXhwcmVzc2lvblZhbHVlJywge1xuICAgICAgICAgICAgICBnZXQ6ICgpID0+IGZpZWxkLnRlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCxcbiAgICAgICAgICAgICAgc2V0OiAoKSA9PiB7IH0sXG4gICAgICAgICAgICAgIGVudW1lcmFibGU6IHRydWUsXG4gICAgICAgICAgICAgIGNvbmZpZ3VyYWJsZTogdHJ1ZSxcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH1cbiAgICAgICAgfSBlbHNlIGlmIChleHByZXNzaW9uUHJvcGVydHkgaW5zdGFuY2VvZiBPYnNlcnZhYmxlKSB7XG4gICAgICAgICAgY29uc3Qgc3Vic2NyaWJlID0gKCkgPT4gKGV4cHJlc3Npb25Qcm9wZXJ0eSBhcyBPYnNlcnZhYmxlPGFueT4pXG4gICAgICAgICAgICAuc3Vic2NyaWJlKHYgPT4ge1xuICAgICAgICAgICAgICB0aGlzLnNldEV4cHJWYWx1ZShmaWVsZCwga2V5LCB2KTtcbiAgICAgICAgICAgICAgaWYgKGZpZWxkLm9wdGlvbnMgJiYgZmllbGQub3B0aW9ucy5fbWFya0ZvckNoZWNrKSB7XG4gICAgICAgICAgICAgICAgZmllbGQub3B0aW9ucy5fbWFya0ZvckNoZWNrKGZpZWxkKTtcbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfSk7XG5cbiAgICAgICAgICBsZXQgc3Vic2NyaXB0aW9uOiBTdWJzY3JpcHRpb24gPSBzdWJzY3JpYmUoKTtcbiAgICAgICAgICBjb25zdCBvbkluaXQgPSBmaWVsZC5ob29rcy5vbkluaXQ7XG4gICAgICAgICAgZmllbGQuaG9va3Mub25Jbml0ID0gKCkgPT4ge1xuICAgICAgICAgICAgaWYgKHN1YnNjcmlwdGlvbiA9PT0gbnVsbCkge1xuICAgICAgICAgICAgICBzdWJzY3JpcHRpb24gPSBzdWJzY3JpYmUoKTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICAgIHJldHVybiBvbkluaXQgJiYgb25Jbml0KGZpZWxkKTtcbiAgICAgICAgICB9O1xuXG4gICAgICAgICAgY29uc3Qgb25EZXN0cm95ID0gZmllbGQuaG9va3Mub25EZXN0cm95O1xuICAgICAgICAgIGZpZWxkLmhvb2tzLm9uRGVzdHJveSA9ICgpID0+IHtcbiAgICAgICAgICAgIG9uRGVzdHJveSAmJiBvbkRlc3Ryb3koZmllbGQpO1xuICAgICAgICAgICAgc3Vic2NyaXB0aW9uLnVuc3Vic2NyaWJlKCk7XG4gICAgICAgICAgICBzdWJzY3JpcHRpb24gPSBudWxsO1xuICAgICAgICAgIH07XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9XG5cbiAgICBpZiAoZmllbGQuaGlkZUV4cHJlc3Npb24pIHtcbiAgICAgIC8vIGRlbGV0ZSBoaWRlIHZhbHVlIGluIG9yZGVyIHRvIGZvcmNlIHJlLWV2YWx1YXRlIGl0IGluIEZvcm1seUZvcm1FeHByZXNzaW9uLlxuICAgICAgZGVsZXRlIGZpZWxkLmhpZGU7XG5cbiAgICAgIGxldCBwYXJlbnQgPSBmaWVsZC5wYXJlbnQ7XG4gICAgICB3aGlsZSAocGFyZW50ICYmICFwYXJlbnQuaGlkZUV4cHJlc3Npb24pIHtcbiAgICAgICAgcGFyZW50ID0gcGFyZW50LnBhcmVudDtcbiAgICAgIH1cblxuICAgICAgZmllbGQuaGlkZUV4cHJlc3Npb24gPSB0aGlzLl9ldmFsRXhwcmVzc2lvbihcbiAgICAgICAgZmllbGQuaGlkZUV4cHJlc3Npb24sXG4gICAgICAgIHBhcmVudCAmJiBwYXJlbnQuaGlkZUV4cHJlc3Npb24gPyAoKSA9PiBwYXJlbnQuaGlkZSA6IHVuZGVmaW5lZCxcbiAgICAgICk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHdyYXBQcm9wZXJ0eShmaWVsZCwgJ2hpZGUnLCAoeyBjdXJyZW50VmFsdWUsIGZpcnN0Q2hhbmdlIH0pID0+IHtcbiAgICAgICAgZmllbGQuX2hpZGUgPSBjdXJyZW50VmFsdWU7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UgfHwgKGZpcnN0Q2hhbmdlICYmIGN1cnJlbnRWYWx1ZSA9PT0gdHJ1ZSkpIHtcbiAgICAgICAgICBmaWVsZC5vcHRpb25zLl9oaWRkZW5GaWVsZHNGb3JDaGVjay5wdXNoKGZpZWxkKTtcbiAgICAgICAgfVxuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBfZXZhbEV4cHJlc3Npb24oZXhwcmVzc2lvbiwgcGFyZW50RXhwcmVzc2lvbj8pIHtcbiAgICBleHByZXNzaW9uID0gZXhwcmVzc2lvbiB8fCAoKCkgPT4gZmFsc2UpO1xuICAgIGlmICh0eXBlb2YgZXhwcmVzc2lvbiA9PT0gJ3N0cmluZycpIHtcbiAgICAgIGV4cHJlc3Npb24gPSBldmFsU3RyaW5nRXhwcmVzc2lvbihleHByZXNzaW9uLCBbJ21vZGVsJywgJ2Zvcm1TdGF0ZScsICdmaWVsZCddKTtcbiAgICB9XG5cbiAgICByZXR1cm4gcGFyZW50RXhwcmVzc2lvblxuICAgICAgPyAobW9kZWw6IGFueSwgZm9ybVN0YXRlOiBhbnksIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykgPT4gcGFyZW50RXhwcmVzc2lvbigpIHx8IGV4cHJlc3Npb24obW9kZWwsIGZvcm1TdGF0ZSwgZmllbGQpXG4gICAgICA6IGV4cHJlc3Npb247XG4gIH1cblxuICBwcml2YXRlIGNoZWNrRmllbGQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGlnbm9yZUNhY2hlID0gZmFsc2UpIHtcbiAgICB0aGlzLl9jaGVja0ZpZWxkKGZpZWxkLCBpZ25vcmVDYWNoZSk7XG5cbiAgICBmaWVsZC5vcHRpb25zLl9oaWRkZW5GaWVsZHNGb3JDaGVja1xuICAgICAgLnNvcnQoZiA9PiBmLmhpZGUgPyAtMSA6IDEpXG4gICAgICAuZm9yRWFjaChmID0+IHRoaXMudG9nZ2xlRm9ybUNvbnRyb2woZiwgZi5oaWRlKSk7XG5cbiAgICBmaWVsZC5vcHRpb25zLl9oaWRkZW5GaWVsZHNGb3JDaGVjayA9IFtdO1xuICB9XG5cbiAgcHJpdmF0ZSBfY2hlY2tGaWVsZChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgaWdub3JlQ2FjaGUgPSBmYWxzZSkge1xuICAgIGxldCBtYXJrRm9yQ2hlY2sgPSBmYWxzZTtcbiAgICBmaWVsZC5maWVsZEdyb3VwLmZvckVhY2goZiA9PiB7XG4gICAgICB0aGlzLmNoZWNrRmllbGRFeHByZXNzaW9uQ2hhbmdlKGYsIGlnbm9yZUNhY2hlKSAmJiAobWFya0ZvckNoZWNrID0gdHJ1ZSk7XG4gICAgICBpZiAodGhpcy5jaGVja0ZpZWxkVmlzaWJpbGl0eUNoYW5nZShmLCBpZ25vcmVDYWNoZSkpIHtcbiAgICAgICAgZmllbGQub3B0aW9ucy5faGlkZGVuRmllbGRzRm9yQ2hlY2sucHVzaChmKTtcbiAgICAgICAgbWFya0ZvckNoZWNrID0gdHJ1ZTtcbiAgICAgIH1cblxuICAgICAgaWYgKGYuZmllbGRHcm91cCAmJiBmLmZpZWxkR3JvdXAubGVuZ3RoID4gMCkge1xuICAgICAgICB0aGlzLl9jaGVja0ZpZWxkKGYsIGlnbm9yZUNhY2hlKTtcbiAgICAgIH1cbiAgICB9KTtcblxuICAgIGlmIChtYXJrRm9yQ2hlY2sgJiYgZmllbGQub3B0aW9ucyAmJiBmaWVsZC5vcHRpb25zLl9tYXJrRm9yQ2hlY2spIHtcbiAgICAgIGZpZWxkLm9wdGlvbnMuX21hcmtGb3JDaGVjayhmaWVsZCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBjaGVja0ZpZWxkRXhwcmVzc2lvbkNoYW5nZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgaWdub3JlQ2FjaGUpOiBib29sZWFuIHtcbiAgICBpZiAoIWZpZWxkIHx8ICFmaWVsZC5fZXhwcmVzc2lvblByb3BlcnRpZXMpIHtcbiAgICAgIHJldHVybiBmYWxzZTtcbiAgICB9XG5cbiAgICBsZXQgbWFya0ZvckNoZWNrID0gZmFsc2U7XG4gICAgY29uc3QgZXhwcmVzc2lvblByb3BlcnRpZXMgPSBmaWVsZC5fZXhwcmVzc2lvblByb3BlcnRpZXM7XG5cbiAgICBmb3IgKGNvbnN0IGtleSBpbiBleHByZXNzaW9uUHJvcGVydGllcykge1xuICAgICAgbGV0IGV4cHJlc3Npb25WYWx1ZSA9IGV2YWxFeHByZXNzaW9uKGV4cHJlc3Npb25Qcm9wZXJ0aWVzW2tleV0uZXhwcmVzc2lvbiwgeyBmaWVsZCB9LCBbZmllbGQubW9kZWwsIGZpZWxkLm9wdGlvbnMuZm9ybVN0YXRlLCBmaWVsZF0pO1xuICAgICAgaWYgKGtleSA9PT0gJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcpIHtcbiAgICAgICAgZXhwcmVzc2lvblZhbHVlID0gISFleHByZXNzaW9uVmFsdWU7XG4gICAgICB9XG5cbiAgICAgIGlmIChcbiAgICAgICAgaWdub3JlQ2FjaGUgfHwgKFxuICAgICAgICAgIGV4cHJlc3Npb25Qcm9wZXJ0aWVzW2tleV0uZXhwcmVzc2lvblZhbHVlICE9PSBleHByZXNzaW9uVmFsdWVcbiAgICAgICAgICAmJiAoIWlzT2JqZWN0KGV4cHJlc3Npb25WYWx1ZSkgfHwgSlNPTi5zdHJpbmdpZnkoZXhwcmVzc2lvblZhbHVlKSAhPT0gSlNPTi5zdHJpbmdpZnkoZXhwcmVzc2lvblByb3BlcnRpZXNba2V5XS5leHByZXNzaW9uVmFsdWUpKVxuICAgICAgICApXG4gICAgICApIHtcbiAgICAgICAgbWFya0ZvckNoZWNrID0gdHJ1ZTtcbiAgICAgICAgZXhwcmVzc2lvblByb3BlcnRpZXNba2V5XS5leHByZXNzaW9uVmFsdWUgPSBleHByZXNzaW9uVmFsdWU7XG4gICAgICAgIHRoaXMuc2V0RXhwclZhbHVlKGZpZWxkLCBrZXksIGV4cHJlc3Npb25WYWx1ZSk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgcmV0dXJuIG1hcmtGb3JDaGVjaztcbiAgfVxuXG4gIHByaXZhdGUgY2hlY2tGaWVsZFZpc2liaWxpdHlDaGFuZ2UoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIGlnbm9yZUNhY2hlKTogYm9vbGVhbiB7XG4gICAgaWYgKCFmaWVsZCB8fCBpc051bGxPclVuZGVmaW5lZChmaWVsZC5oaWRlRXhwcmVzc2lvbikpIHtcbiAgICAgIHJldHVybiBmYWxzZTtcbiAgICB9XG5cbiAgICBjb25zdCBoaWRlRXhwcmVzc2lvblJlc3VsdDogYm9vbGVhbiA9ICEhZXZhbEV4cHJlc3Npb24oXG4gICAgICBmaWVsZC5oaWRlRXhwcmVzc2lvbixcbiAgICAgIHsgZmllbGQgfSxcbiAgICAgIFtmaWVsZC5tb2RlbCwgZmllbGQub3B0aW9ucy5mb3JtU3RhdGUsIGZpZWxkXSxcbiAgICApO1xuICAgIGxldCBtYXJrRm9yQ2hlY2sgPSBmYWxzZTtcbiAgICBpZiAoaGlkZUV4cHJlc3Npb25SZXN1bHQgIT09IGZpZWxkLmhpZGUgfHwgaWdub3JlQ2FjaGUpIHtcbiAgICAgIG1hcmtGb3JDaGVjayA9IHRydWU7XG4gICAgICAvLyB0b2dnbGUgaGlkZVxuICAgICAgZmllbGQuaGlkZSA9IGhpZGVFeHByZXNzaW9uUmVzdWx0O1xuICAgICAgZmllbGQudGVtcGxhdGVPcHRpb25zLmhpZGRlbiA9IGhpZGVFeHByZXNzaW9uUmVzdWx0O1xuICAgIH1cblxuICAgIHJldHVybiBtYXJrRm9yQ2hlY2s7XG4gIH1cblxuICBwcml2YXRlIHNldERpc2FibGVkU3RhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnLCB2YWx1ZTogYm9vbGVhbikge1xuICAgIGlmIChmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICBmaWVsZC5maWVsZEdyb3VwXG4gICAgICAgIC5maWx0ZXIoZiA9PiAhZi5leHByZXNzaW9uUHJvcGVydGllcyB8fCAhZi5leHByZXNzaW9uUHJvcGVydGllcy5oYXNPd25Qcm9wZXJ0eSgndGVtcGxhdGVPcHRpb25zLmRpc2FibGVkJykpXG4gICAgICAgIC5mb3JFYWNoKGYgPT4gdGhpcy5zZXREaXNhYmxlZFN0YXRlKGYsIHZhbHVlKSk7XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLmtleSAmJiBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQgIT09IHZhbHVlKSB7XG4gICAgICBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQgPSB2YWx1ZTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHRvZ2dsZUZvcm1Db250cm9sKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBoaWRlOiBib29sZWFuKSB7XG4gICAgaWYgKGZpZWxkLmZvcm1Db250cm9sICYmIGZpZWxkLmtleSkge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcChmaWVsZCwgJ19oaWRlJywgISEoaGlkZSB8fCBmaWVsZC5oaWRlKSk7XG4gICAgICBjb25zdCBjID0gZmllbGQuZm9ybUNvbnRyb2w7XG4gICAgICBpZiAoY1snX2ZpZWxkcyddLmxlbmd0aCA+IDEpIHtcbiAgICAgICAgdXBkYXRlVmFsaWRpdHkoYyk7XG4gICAgICB9XG5cbiAgICAgIGhpZGUgPT09IHRydWUgJiYgY1snX2ZpZWxkcyddLmV2ZXJ5KGYgPT4gISFmLl9oaWRlKVxuICAgICAgICA/IHVucmVnaXN0ZXJDb250cm9sKGZpZWxkKVxuICAgICAgICA6IHJlZ2lzdGVyQ29udHJvbChmaWVsZCk7XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLmZpZWxkR3JvdXBcbiAgICAgICAgLmZpbHRlcihmID0+ICFmLmhpZGVFeHByZXNzaW9uKVxuICAgICAgICAuZm9yRWFjaChmID0+IHRoaXMudG9nZ2xlRm9ybUNvbnRyb2woZiwgaGlkZSkpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZC5vcHRpb25zLmZpZWxkQ2hhbmdlcykge1xuICAgICAgZmllbGQub3B0aW9ucy5maWVsZENoYW5nZXMubmV4dCg8Rm9ybWx5VmFsdWVDaGFuZ2VFdmVudD4geyBmaWVsZCwgdHlwZTogJ2hpZGRlbicsIHZhbHVlOiBoaWRlIH0pO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgc2V0RXhwclZhbHVlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBwcm9wOiBzdHJpbmcsIHZhbHVlOiBhbnkpIHtcbiAgICB0cnkge1xuICAgICAgbGV0IHRhcmdldCA9IGZpZWxkO1xuICAgICAgY29uc3QgcGF0aHMgPSBwcm9wLnNwbGl0KCcuJyk7XG4gICAgICBjb25zdCBsYXN0SW5kZXggPSBwYXRocy5sZW5ndGggLSAxO1xuICAgICAgZm9yIChsZXQgaSA9IDA7IGkgPCBsYXN0SW5kZXg7IGkrKykge1xuICAgICAgICB0YXJnZXQgPSB0YXJnZXRbcGF0aHNbaV1dO1xuICAgICAgfVxuXG4gICAgICB0YXJnZXRbcGF0aHNbbGFzdEluZGV4XV0gPSB2YWx1ZTtcbiAgICB9IGNhdGNoIChlcnJvcikge1xuICAgICAgZXJyb3IubWVzc2FnZSA9IGBbRm9ybWx5IEVycm9yXSBbRXhwcmVzc2lvbiBcIiR7cHJvcH1cIl0gJHtlcnJvci5tZXNzYWdlfWA7XG4gICAgICB0aHJvdyBlcnJvcjtcbiAgICB9XG5cbiAgICBpZiAocHJvcCA9PT0gJ3RlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZCcgJiYgZmllbGQua2V5KSB7XG4gICAgICB0aGlzLnNldERpc2FibGVkU3RhdGUoZmllbGQsIHZhbHVlKTtcbiAgICB9XG5cbiAgICBpZiAocHJvcC5pbmRleE9mKCdtb2RlbC4nKSA9PT0gMCkge1xuICAgICAgY29uc3QgcGF0aCA9IHByb3AucmVwbGFjZSgvXm1vZGVsXFwuLywgJycpLFxuICAgICAgICBjb250cm9sID0gZmllbGQua2V5ICYmIHByb3AgPT09IHBhdGggPyBmaWVsZC5mb3JtQ29udHJvbCA6IGZpZWxkLnBhcmVudC5mb3JtQ29udHJvbC5nZXQocGF0aCk7XG5cbiAgICAgIGlmIChcbiAgICAgICAgY29udHJvbFxuICAgICAgICAmJiAhKGlzTnVsbE9yVW5kZWZpbmVkKGNvbnRyb2wudmFsdWUpICYmIGlzTnVsbE9yVW5kZWZpbmVkKHZhbHVlKSlcbiAgICAgICAgJiYgY29udHJvbC52YWx1ZSAhPT0gdmFsdWVcbiAgICAgICkge1xuICAgICAgICBjb250cm9sLnBhdGNoVmFsdWUodmFsdWUsIHsgZW1pdEV2ZW50OiBmYWxzZSB9KTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICB0aGlzLmVtaXRFeHByZXNzaW9uQ2hhbmdlcyhmaWVsZCwgcHJvcCwgdmFsdWUpO1xuICB9XG5cbiAgcHJpdmF0ZSBlbWl0RXhwcmVzc2lvbkNoYW5nZXMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHByb3BlcnR5OiBzdHJpbmcsIHZhbHVlOiBhbnkpIHtcbiAgICBpZiAoIWZpZWxkLm9wdGlvbnMuZmllbGRDaGFuZ2VzKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgZmllbGQub3B0aW9ucy5maWVsZENoYW5nZXMubmV4dCh7XG4gICAgICBmaWVsZDogZmllbGQsXG4gICAgICB0eXBlOiAnZXhwcmVzc2lvbkNoYW5nZXMnLFxuICAgICAgcHJvcGVydHksXG4gICAgICB2YWx1ZSxcbiAgICB9KTtcbiAgfVxufVxuIl19