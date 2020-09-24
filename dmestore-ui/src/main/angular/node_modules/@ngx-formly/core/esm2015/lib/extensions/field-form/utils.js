/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { FormArray, FormGroup, FormControl } from '@angular/forms';
import { getKeyPath, getFieldValue, isNullOrUndefined, defineHiddenProp, wrapProperty, isUndefined, assignFieldValue } from '../../utils';
/**
 * @param {?} field
 * @param {?=} emitEvent
 * @return {?}
 */
export function unregisterControl(field, emitEvent = false) {
    /** @type {?} */
    const form = (/** @type {?} */ (field.formControl.parent));
    if (!form) {
        return;
    }
    /** @type {?} */
    const control = field.formControl;
    /** @type {?} */
    const opts = { emitEvent };
    if (form instanceof FormArray) {
        /** @type {?} */
        const key = form.controls.findIndex((/**
         * @param {?} c
         * @return {?}
         */
        c => c === control));
        if (key !== -1) {
            updateControl(form, opts, (/**
             * @return {?}
             */
            () => form.removeAt(key)));
        }
    }
    else if (form instanceof FormGroup) {
        /** @type {?} */
        const paths = getKeyPath(field);
        /** @type {?} */
        const key = paths[paths.length - 1];
        if (form.get([key]) === control) {
            updateControl(form, opts, (/**
             * @return {?}
             */
            () => form.removeControl(key)));
        }
    }
    control.setParent(null);
    if (field['autoClear']) {
        if (field.parent.model) {
            delete field.parent.model[Array.isArray(field.key) ? field.key[0] : field.key];
        }
        control.reset({ value: undefined, disabled: control.disabled }, { emitEvent: field.fieldGroup ? false : emitEvent, onlySelf: true });
    }
}
/**
 * @param {?} field
 * @return {?}
 */
export function findControl(field) {
    if (field.formControl) {
        return field.formControl;
    }
    /** @type {?} */
    const form = (/** @type {?} */ (field.parent.formControl));
    return form ? form.get(getKeyPath(field)) : null;
}
/**
 * @param {?} field
 * @param {?=} control
 * @param {?=} emitEvent
 * @return {?}
 */
export function registerControl(field, control, emitEvent = false) {
    control = control || field.formControl;
    if (!control['_fields']) {
        defineHiddenProp(control, '_fields', []);
    }
    if (control['_fields'].indexOf(field) === -1) {
        control['_fields'].push(field);
    }
    if (!field.formControl && control) {
        defineHiddenProp(field, 'formControl', control);
        field.templateOptions.disabled = !!field.templateOptions.disabled;
        wrapProperty(field.templateOptions, 'disabled', (/**
         * @param {?} __0
         * @return {?}
         */
        ({ firstChange, currentValue }) => {
            if (!firstChange) {
                currentValue ? field.formControl.disable() : field.formControl.enable();
            }
        }));
        if (control.registerOnDisabledChange) {
            control.registerOnDisabledChange((/**
             * @param {?} value
             * @return {?}
             */
            (value) => field.templateOptions['___$disabled'] = value));
        }
    }
    /** @type {?} */
    let parent = (/** @type {?} */ (field.parent.formControl));
    if (!parent) {
        return;
    }
    /** @type {?} */
    const paths = getKeyPath(field);
    if (!parent['_formlyControls']) {
        defineHiddenProp(parent, '_formlyControls', {});
    }
    parent['_formlyControls'][paths.join('.')] = control;
    for (let i = 0; i < (paths.length - 1); i++) {
        /** @type {?} */
        const path = paths[i];
        if (!parent.get([path])) {
            registerControl({
                key: [path],
                formControl: new FormGroup({}),
                parent: { formControl: parent },
            });
        }
        parent = (/** @type {?} */ (parent.get([path])));
    }
    if (field['autoClear'] && field.parent && !isUndefined(field.defaultValue) && isUndefined(getFieldValue(field))) {
        assignFieldValue(field, field.defaultValue);
    }
    /** @type {?} */
    const value = getFieldValue(field);
    if (!(isNullOrUndefined(control.value) && isNullOrUndefined(value))
        && control.value !== value
        && control instanceof FormControl) {
        control.patchValue(value);
    }
    /** @type {?} */
    const key = paths[paths.length - 1];
    if (!field._hide && parent.get([key]) !== control) {
        updateControl(parent, { emitEvent }, (/**
         * @return {?}
         */
        () => parent.setControl(key, control)));
    }
}
/**
 * @param {?} c
 * @return {?}
 */
export function updateValidity(c) {
    /** @type {?} */
    const status = c.status;
    c.updateValueAndValidity({ emitEvent: false });
    if (status !== c.status) {
        ((/** @type {?} */ (c.statusChanges))).emit(c.status);
    }
}
/**
 * @param {?} form
 * @param {?} opts
 * @param {?} action
 * @return {?}
 */
function updateControl(form, opts, action) {
    /**
     *  workaround for https://github.com/angular/angular/issues/27679
     */
    if (form instanceof FormGroup && !form['__patchForEachChild']) {
        defineHiddenProp(form, '__patchForEachChild', true);
        ((/** @type {?} */ (form)))._forEachChild = (/**
         * @param {?} cb
         * @return {?}
         */
        (cb) => {
            Object
                .keys(form.controls)
                .forEach((/**
             * @param {?} k
             * @return {?}
             */
            k => form.controls[k] && cb(form.controls[k], k)));
        });
    }
    /**
     * workaround for https://github.com/angular/angular/issues/20439
     * @type {?}
     */
    const updateValueAndValidity = form.updateValueAndValidity.bind(form);
    if (opts.emitEvent === false) {
        form.updateValueAndValidity = (/**
         * @param {?} opts
         * @return {?}
         */
        (opts) => {
            updateValueAndValidity(Object.assign({}, (opts || {}), { emitEvent: false }));
        });
    }
    action();
    if (opts.emitEvent === false) {
        form.updateValueAndValidity = updateValueAndValidity;
    }
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2V4dGVuc2lvbnMvZmllbGQtZm9ybS91dGlscy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsV0FBVyxFQUFtQixNQUFNLGdCQUFnQixDQUFDO0FBRXBGLE9BQU8sRUFBRSxVQUFVLEVBQUUsYUFBYSxFQUFFLGlCQUFpQixFQUFFLGdCQUFnQixFQUFFLFlBQVksRUFBRSxXQUFXLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSxhQUFhLENBQUM7Ozs7OztBQUkxSSxNQUFNLFVBQVUsaUJBQWlCLENBQUMsS0FBd0IsRUFBRSxTQUFTLEdBQUcsS0FBSzs7VUFDckUsSUFBSSxHQUFHLG1CQUFBLEtBQUssQ0FBQyxXQUFXLENBQUMsTUFBTSxFQUF5QjtJQUM5RCxJQUFJLENBQUMsSUFBSSxFQUFFO1FBQ1QsT0FBTztLQUNSOztVQUVLLE9BQU8sR0FBRyxLQUFLLENBQUMsV0FBVzs7VUFDM0IsSUFBSSxHQUFHLEVBQUUsU0FBUyxFQUFFO0lBQzFCLElBQUksSUFBSSxZQUFZLFNBQVMsRUFBRTs7Y0FDdkIsR0FBRyxHQUFHLElBQUksQ0FBQyxRQUFRLENBQUMsU0FBUzs7OztRQUFDLENBQUMsQ0FBQyxFQUFFLENBQUMsQ0FBQyxLQUFLLE9BQU8sRUFBQztRQUN2RCxJQUFJLEdBQUcsS0FBSyxDQUFDLENBQUMsRUFBRTtZQUNkLGFBQWEsQ0FBQyxJQUFJLEVBQUUsSUFBSTs7O1lBQUUsR0FBRyxFQUFFLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxHQUFHLENBQUMsRUFBQyxDQUFDO1NBQ3JEO0tBQ0Y7U0FBTSxJQUFJLElBQUksWUFBWSxTQUFTLEVBQUU7O2NBQzlCLEtBQUssR0FBRyxVQUFVLENBQUMsS0FBSyxDQUFDOztjQUN6QixHQUFHLEdBQUcsS0FBSyxDQUFDLEtBQUssQ0FBQyxNQUFN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QUFDLElBQXlCLEVBQUUsSUFBNEIsRUFBRSxNQUFnQjtJQUM5Rjs7T0FFRztJQUNILElBQUksSUFBSSxZQUFZLFNBQVMsSUFBSSxDQUFDLElBQUksQ0FBQyxxQkFBcUIsQ0FBQyxFQUFFO1FBQzdELGdCQUFnQixDQUFDLElBQUksRUFBRSxxQkFBcUIsRUFBRSxJQUFJLENBQUMsQ0FBQztRQUNwRCxDQUFDLG1CQUFBLElBQUksRUFBTyxDQUFDLENBQUMsYUFBYTs7OztRQUFHLENBQUMsRUFBWSxFQUFFLEVBQUU7WUFDN0MsTUFBTTtpQkFDSCxJQUFJLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQztpQkFDbkIsT0FBTzs7OztZQUFDLENBQUMsQ0FBQyxFQUFFLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxDQUFDLENBQUMsSUFBSSxFQUFFLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxDQUFDLENBQUMsRUFBRSxDQUFDLENBQUMsRUFBQyxDQUFDO1FBQy9ELENBQUMsQ0FBQSxDQUFDO0tBQ0g7Ozs7O1VBS0ssc0JBQXNCLEdBQUcsSUFBSSxDQUFDLHNCQUFzQixDQUFDLElBQUksQ0FBQyxJQUFJLENBQUM7SUFDckUsSUFBSSxJQUFJLENBQUMsU0FBUyxLQUFLLEtBQUssRUFBRTtRQUM1QixJQUFJLENBQUMsc0JBQXNCOzs7O1FBQUcsQ0FBQyxJQUFJLEVBQUUsRUFBRTtZQUNyQyxzQkFBc0IsbUJBQU0sQ0FBQyxJQUFJLElBQUksRUFBRSxDQUFDLElBQUUsU0FBUyxFQUFFLEtBQUssSUFBRyxDQUFDO1FBQ2hFLENBQUMsQ0FBQSxDQUFDO0tBQ0g7SUFFRCxNQUFNLEVBQUUsQ0FBQztJQUVULElBQUksSUFBSSxDQUFDLFNBQVMsS0FBSyxLQUFLLEVBQUU7UUFDNUIsSUFBSSxDQUFDLHNCQUFzQixHQUFHLHNCQUFzQixDQUFDO0tBQ3REO0FBQ0gsQ0FBQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEZvcm1BcnJheSwgRm9ybUdyb3VwLCBGb3JtQ29udHJvbCwgQWJzdHJhY3RDb250cm9sIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICcuLi8uLi9jb3JlJztcbmltcG9ydCB7IGdldEtleVBhdGgsIGdldEZpZWxkVmFsdWUsIGlzTnVsbE9yVW5kZWZpbmVkLCBkZWZpbmVIaWRkZW5Qcm9wLCB3cmFwUHJvcGVydHksIGlzVW5kZWZpbmVkLCBhc3NpZ25GaWVsZFZhbHVlIH0gZnJvbSAnLi4vLi4vdXRpbHMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWdDYWNoZSB9IGZyb20gJy4uLy4uL2NvbXBvbmVudHMvZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBFdmVudEVtaXR0ZXIgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcblxuZXhwb3J0IGZ1bmN0aW9uIHVucmVnaXN0ZXJDb250cm9sKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZywgZW1pdEV2ZW50ID0gZmFsc2UpIHtcbiAgY29uc3QgZm9ybSA9IGZpZWxkLmZvcm1Db250cm9sLnBhcmVudCBhcyBGb3JtQXJyYXkgfCBGb3JtR3JvdXA7XG4gIGlmICghZm9ybSkge1xuICAgIHJldHVybjtcbiAgfVxuXG4gIGNvbnN0IGNvbnRyb2wgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgY29uc3Qgb3B0cyA9IHsgZW1pdEV2ZW50IH07XG4gIGlmIChmb3JtIGluc3RhbmNlb2YgRm9ybUFycmF5KSB7XG4gICAgY29uc3Qga2V5ID0gZm9ybS5jb250cm9scy5maW5kSW5kZXgoYyA9PiBjID09PSBjb250cm9sKTtcbiAgICBpZiAoa2V5ICE9PSAtMSkge1xuICAgICAgdXBkYXRlQ29udHJvbChmb3JtLCBvcHRzLCAoKSA9PiBmb3JtLnJlbW92ZUF0KGtleSkpO1xuICAgIH1cbiAgfSBlbHNlIGlmIChmb3JtIGluc3RhbmNlb2YgRm9ybUdyb3VwKSB7XG4gICAgY29uc3QgcGF0aHMgPSBnZXRLZXlQYXRoKGZpZWxkKTtcbiAgICBjb25zdCBrZXkgPSBwYXRoc1twYXRocy5sZW5ndGggLSAxXTtcbiAgICBpZiAoZm9ybS5nZXQoW2tleV0pID09PSBjb250cm9sKSB7XG4gICAgICB1cGRhdGVDb250cm9sKGZvcm0sIG9wdHMsICgpID0+IGZvcm0ucmVtb3ZlQ29udHJvbChrZXkpKTtcbiAgICB9XG4gIH1cblxuICBjb250cm9sLnNldFBhcmVudChudWxsKTtcbiAgaWYgKGZpZWxkWydhdXRvQ2xlYXInXSkge1xuICAgIGlmIChmaWVsZC5wYXJlbnQubW9kZWwpIHtcbiAgICAgIGRlbGV0ZSBmaWVsZC5wYXJlbnQubW9kZWxbQXJyYXkuaXNBcnJheShmaWVsZC5rZXkpID8gZmllbGQua2V5WzBdIDogZmllbGQua2V5XTtcbiAgICB9XG4gICAgY29udHJvbC5yZXNldChcbiAgICAgIHsgdmFsdWU6IHVuZGVmaW5lZCwgZGlzYWJsZWQ6IGNvbnRyb2wuZGlzYWJsZWQgfSxcbiAgICAgIHsgZW1pdEV2ZW50OiBmaWVsZC5maWVsZEdyb3VwID8gZmFsc2UgOiBlbWl0RXZlbnQsIG9ubHlTZWxmOiB0cnVlIH0sXG4gICAgKTtcbiAgfVxufVxuXG5leHBvcnQgZnVuY3Rpb24gZmluZENvbnRyb2woZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKTogQWJzdHJhY3RDb250cm9sIHtcbiAgaWYgKGZpZWxkLmZvcm1Db250cm9sKSB7XG4gICAgcmV0dXJuIGZpZWxkLmZvcm1Db250cm9sO1xuICB9XG5cbiAgY29uc3QgZm9ybSA9IGZpZWxkLnBhcmVudC5mb3JtQ29udHJvbCBhcyBGb3JtR3JvdXA7XG5cbiAgcmV0dXJuIGZvcm0gPyBmb3JtLmdldChnZXRLZXlQYXRoKGZpZWxkKSkgOiBudWxsO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gcmVnaXN0ZXJDb250cm9sKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlLCBjb250cm9sPzogYW55LCBlbWl0RXZlbnQgPSBmYWxzZSkge1xuICBjb250cm9sID0gY29udHJvbCB8fCBmaWVsZC5mb3JtQ29udHJvbDtcbiAgaWYgKCFjb250cm9sWydfZmllbGRzJ10pIHtcbiAgICBkZWZpbmVIaWRkZW5Qcm9wKGNvbnRyb2wsICdfZmllbGRzJywgW10pO1xuICB9XG4gIGlmIChjb250cm9sWydfZmllbGRzJ10uaW5kZXhPZihmaWVsZCkgPT09IC0xKSB7XG4gICAgY29udHJvbFsnX2ZpZWxkcyddLnB1c2goZmllbGQpO1xuICB9XG5cbiAgaWYgKCFmaWVsZC5mb3JtQ29udHJvbCAmJiBjb250cm9sKSB7XG4gICAgZGVmaW5lSGlkZGVuUHJvcChmaWVsZCwgJ2Zvcm1Db250cm9sJywgY29udHJvbCk7XG5cbiAgICBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQgPSAhIWZpZWxkLnRlbXBsYXRlT3B0aW9ucy5kaXNhYmxlZDtcbiAgICB3cmFwUHJvcGVydHkoZmllbGQudGVtcGxhdGVPcHRpb25zLCAnZGlzYWJsZWQnLCAoeyBmaXJzdENoYW5nZSwgY3VycmVudFZhbHVlIH0pID0+IHtcbiAgICAgIGlmICghZmlyc3RDaGFuZ2UpIHtcbiAgICAgICAgY3VycmVudFZhbHVlID8gZmllbGQuZm9ybUNvbnRyb2wuZGlzYWJsZSgpIDogZmllbGQuZm9ybUNvbnRyb2wuZW5hYmxlKCk7XG4gICAgICB9XG4gICAgfSk7XG4gICAgaWYgKGNvbnRyb2wucmVnaXN0ZXJPbkRpc2FibGVkQ2hhbmdlKSB7XG4gICAgICBjb250cm9sLnJlZ2lzdGVyT25EaXNhYmxlZENoYW5nZShcbiAgICAgICAgKHZhbHVlOiBib29sZWFuKSA9PiBmaWVsZC50ZW1wbGF0ZU9wdGlvbnNbJ19fXyRkaXNhYmxlZCddID0gdmFsdWUsXG4gICAgICApO1xuICAgIH1cbiAgfVxuXG4gIGxldCBwYXJlbnQgPSBmaWVsZC5wYXJlbnQuZm9ybUNvbnRyb2wgYXMgRm9ybUdyb3VwO1xuICBpZiAoIXBhcmVudCkge1xuICAgIHJldHVybjtcbiAgfVxuXG4gIGNvbnN0IHBhdGhzID0gZ2V0S2V5UGF0aChmaWVsZCk7XG4gIGlmICghcGFyZW50WydfZm9ybWx5Q29udHJvbHMnXSkge1xuICAgIGRlZmluZUhpZGRlblByb3AocGFyZW50LCAnX2Zvcm1seUNvbnRyb2xzJywge30pO1xuICB9XG4gIHBhcmVudFsnX2Zvcm1seUNvbnRyb2xzJ11bcGF0aHMuam9pbignLicpXSA9IGNvbnRyb2w7XG5cbiAgZm9yIChsZXQgaSA9IDA7IGkgPCAocGF0aHMubGVuZ3RoIC0gMSk7IGkrKykge1xuICAgIGNvbnN0IHBhdGggPSBwYXRoc1tpXTtcbiAgICBpZiAoIXBhcmVudC5nZXQoW3BhdGhdKSkge1xuICAgICAgcmVnaXN0ZXJDb250cm9sKHtcbiAgICAgICAga2V5OiBbcGF0aF0sXG4gICAgICAgIGZvcm1Db250cm9sOiBuZXcgRm9ybUdyb3VwKHt9KSxcbiAgICAgICAgcGFyZW50OiB7IGZvcm1Db250cm9sOiBwYXJlbnQgfSxcbiAgICAgIH0pO1xuICAgIH1cblxuICAgIHBhcmVudCA9IDxGb3JtR3JvdXA+IHBhcmVudC5nZXQoW3BhdGhdKTtcbiAgfVxuXG4gIGlmIChmaWVsZFsnYXV0b0NsZWFyJ10gJiYgZmllbGQucGFyZW50ICYmICFpc1VuZGVmaW5lZChmaWVsZC5kZWZhdWx0VmFsdWUpICYmIGlzVW5kZWZpbmVkKGdldEZpZWxkVmFsdWUoZmllbGQpKSkge1xuICAgIGFzc2lnbkZpZWxkVmFsdWUoZmllbGQsIGZpZWxkLmRlZmF1bHRWYWx1ZSk7XG4gIH1cblxuICBjb25zdCB2YWx1ZSA9IGdldEZpZWxkVmFsdWUoZmllbGQpO1xuICBpZiAoXG4gICAgIShpc051bGxPclVuZGVmaW5lZChjb250cm9sLnZhbHVlKSAmJiBpc051bGxPclVuZGVmaW5lZCh2YWx1ZSkpXG4gICAgJiYgY29udHJvbC52YWx1ZSAhPT0gdmFsdWVcbiAgICAmJiBjb250cm9sIGluc3RhbmNlb2YgRm9ybUNvbnRyb2xcbiAgKSB7XG4gICAgY29udHJvbC5wYXRjaFZhbHVlKHZhbHVlKTtcbiAgfVxuICBjb25zdCBrZXkgPSBwYXRoc1twYXRocy5sZW5ndGggLSAxXTtcbiAgaWYgKCFmaWVsZC5faGlkZSAmJiBwYXJlbnQuZ2V0KFtrZXldKSAhPT0gY29udHJvbCkge1xuICAgIHVwZGF0ZUNvbnRyb2woXG4gICAgICBwYXJlbnQsXG4gICAgICB7IGVtaXRFdmVudCB9LFxuICAgICAgKCkgPT4gcGFyZW50LnNldENvbnRyb2woa2V5LCBjb250cm9sKSxcbiAgICApO1xuICB9XG59XG5cbmV4cG9ydCBmdW5jdGlvbiB1cGRhdGVWYWxpZGl0eShjOiBBYnN0cmFjdENvbnRyb2wpIHtcbiAgY29uc3Qgc3RhdHVzID0gYy5zdGF0dXM7XG4gIGMudXBkYXRlVmFsdWVBbmRWYWxpZGl0eSh7IGVtaXRFdmVudDogZmFsc2UgfSk7XG4gIGlmIChzdGF0dXMgIT09IGMuc3RhdHVzKSB7XG4gICAgKGMuc3RhdHVzQ2hhbmdlcyBhcyBFdmVudEVtaXR0ZXI8c3RyaW5nPikuZW1pdChjLnN0YXR1cyk7XG4gIH1cbn1cblxuZnVuY3Rpb24gdXBkYXRlQ29udHJvbChmb3JtOiBGb3JtR3JvdXB8Rm9ybUFycmF5LCBvcHRzOiB7IGVtaXRFdmVudDogYm9vbGVhbiB9LCBhY3Rpb246IEZ1bmN0aW9uKSB7XG4gIC8qKlxuICAgKiAgd29ya2Fyb3VuZCBmb3IgaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvYW5ndWxhci9pc3N1ZXMvMjc2NzlcbiAgICovXG4gIGlmIChmb3JtIGluc3RhbmNlb2YgRm9ybUdyb3VwICYmICFmb3JtWydfX3BhdGNoRm9yRWFjaENoaWxkJ10pIHtcbiAgICBkZWZpbmVIaWRkZW5Qcm9wKGZvcm0sICdfX3BhdGNoRm9yRWFjaENoaWxkJywgdHJ1ZSk7XG4gICAgKGZvcm0gYXMgYW55KS5fZm9yRWFjaENoaWxkID0gKGNiOiBGdW5jdGlvbikgPT4ge1xuICAgICAgT2JqZWN0XG4gICAgICAgIC5rZXlzKGZvcm0uY29udHJvbHMpXG4gICAgICAgIC5mb3JFYWNoKGsgPT4gZm9ybS5jb250cm9sc1trXSAmJiBjYihmb3JtLmNvbnRyb2xzW2tdLCBrKSk7XG4gICAgfTtcbiAgfVxuXG4gIC8qKlxuICAgKiB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9hbmd1bGFyL2lzc3Vlcy8yMDQzOVxuICAgKi9cbiAgY29uc3QgdXBkYXRlVmFsdWVBbmRWYWxpZGl0eSA9IGZvcm0udXBkYXRlVmFsdWVBbmRWYWxpZGl0eS5iaW5kKGZvcm0pO1xuICBpZiAob3B0cy5lbWl0RXZlbnQgPT09IGZhbHNlKSB7XG4gICAgZm9ybS51cGRhdGVWYWx1ZUFuZFZhbGlkaXR5ID0gKG9wdHMpID0+IHtcbiAgICAgIHVwZGF0ZVZhbHVlQW5kVmFsaWRpdHkoeyAuLi4ob3B0cyB8fCB7fSksIGVtaXRFdmVudDogZmFsc2UgfSk7XG4gICAgfTtcbiAgfVxuXG4gIGFjdGlvbigpO1xuXG4gIGlmIChvcHRzLmVtaXRFdmVudCA9PT0gZmFsc2UpIHtcbiAgICBmb3JtLnVwZGF0ZVZhbHVlQW5kVmFsaWRpdHkgPSB1cGRhdGVWYWx1ZUFuZFZhbGlkaXR5O1xuICB9XG59XG4iXX0=