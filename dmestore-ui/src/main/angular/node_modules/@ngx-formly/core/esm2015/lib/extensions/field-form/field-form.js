/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { getFieldValue, defineHiddenProp } from '../../utils';
import { registerControl, findControl, updateValidity as updateControlValidity } from './utils';
import { of } from 'rxjs';
/**
 * \@experimental
 */
export class FieldFormExtension {
    /**
     * @param {?} config
     */
    constructor(config) {
        this.config = config;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    onPopulate(field) {
        if (field.key) {
            this.addFormControl(field);
        }
        if (field.parent && field.fieldGroup && !field.key) {
            defineHiddenProp(field, 'formControl', field.parent.formControl);
        }
    }
    /**
     * @param {?} field
     * @return {?}
     */
    postPopulate(field) {
        if (field.parent) {
            return;
        }
        /** @type {?} */
        const fieldsToUpdate = this.setValidators(field);
        if (fieldsToUpdate.length === 0) {
            return;
        }
        if (fieldsToUpdate.length === 1) {
            fieldsToUpdate[0].formControl.updateValueAndValidity();
        }
        else {
            ((/** @type {?} */ (field.formControl)))._updateTreeValidity();
        }
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    addFormControl(field) {
        /** @type {?} */
        let control = findControl(field);
        if (!control) {
            /** @type {?} */
            const controlOptions = { updateOn: field.modelOptions.updateOn };
            /** @type {?} */
            const value = getFieldValue(field);
            /** @type {?} */
            const ref = this.config ? this.config.resolveFieldTypeRef(field) : null;
            if (ref && ref.componentType && ref.componentType['createControl']) {
                /** @type {?} */
                const component = ref.componentType;
                console.warn(`NgxFormly: '${component.name}::createControl' is deprecated since v5.0, use 'prePopulate' hook instead.`);
                control = component['createControl'](value, field);
            }
            else if (field.fieldGroup) {
                // TODO: move to postPopulate
                control = new FormGroup({}, controlOptions);
            }
            else {
                control = new FormControl(value, controlOptions);
            }
        }
        registerControl(field, control);
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    setValidators(field) {
        /** @type {?} */
        let updateValidity = false;
        if (field.key || !field.parent) {
            const { formControl: c } = field;
            /** @type {?} */
            const disabled = field.templateOptions ? field.templateOptions.disabled : false;
            if (disabled && c.enabled) {
                c.disable({ emitEvent: false, onlySelf: true });
                updateValidity = true;
            }
            if (null === c.validator || null === c.asyncValidator) {
                c.setValidators((/**
                 * @return {?}
                 */
                () => {
                    /** @type {?} */
                    const v = Validators.compose(this.mergeValidators(field, '_validators'));
                    return v ? v(c) : null;
                }));
                c.setAsyncValidators((/**
                 * @return {?}
                 */
                () => {
                    /** @type {?} */
                    const v = Validators.composeAsync(this.mergeValidators(field, '_asyncValidators'));
                    return v ? v(c) : of(null);
                }));
                if (!c.parent) {
                    updateControlValidity(c);
                }
                else {
                    updateValidity = true;
                }
            }
        }
        /** @type {?} */
        const fieldsToUpdate = updateValidity ? [field] : [];
        (field.fieldGroup || []).forEach((/**
         * @param {?} f
         * @return {?}
         */
        f => {
            /** @type {?} */
            const childrenToUpdate = this.setValidators(f);
            if (!updateValidity) {
                fieldsToUpdate.push(...childrenToUpdate);
            }
        }));
        return fieldsToUpdate;
    }
    /**
     * @private
     * @template T
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    mergeValidators(field, type) {
        /** @type {?} */
        const validators = [];
        /** @type {?} */
        const c = field.formControl;
        if (c && c['_fields'] && c['_fields'].length > 1) {
            c['_fields']
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            (f) => !f._hide))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            (f) => validators.push(...f[type])));
        }
        else {
            validators.push(...field[type]);
        }
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            f => !f.key && f.fieldGroup))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            f => validators.push(...this.mergeValidators(f, type))));
        }
        return validators;
    }
}
if (false) {
    /**
     * @type {?}
     * @private
     */
    FieldFormExtension.prototype.config;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtZm9ybS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC1mb3JtL2ZpZWxkLWZvcm0udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUVBLE9BQU8sRUFBRSxTQUFTLEVBQUUsV0FBVyxFQUEwQixVQUFVLEVBQWlDLE1BQU0sZ0JBQWdCLENBQUM7QUFDM0gsT0FBTyxFQUFFLGFBQWEsRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLGFBQWEsQ0FBQztBQUM5RCxPQUFPLEVBQUUsZUFBZSxFQUFFLFdBQVcsRUFBRSxjQUFjLElBQUkscUJBQXFCLEVBQUUsTUFBTSxTQUFTLENBQUM7QUFDaEcsT0FBTyxFQUFFLEVBQUUsRUFBRSxNQUFNLE1BQU0sQ0FBQzs7OztBQUcxQixNQUFNLE9BQU8sa0JBQWtCOzs7O0lBQzdCLFlBQW9CLE1BQW9CO1FBQXBCLFdBQU0sR0FBTixNQUFNLENBQWM7SUFBSSxDQUFDOzs7OztJQUU3QyxVQUFVLENBQUMsS0FBNkI7UUFDdEMsSUFBSSxLQUFLLENBQUMsR0FBRyxFQUFFO1lBQ2IsSUFBSSxDQUFDLGNBQWMsQ0FBQyxLQUFLLENBQUMsQ0FBQztTQUM1QjtRQUVELElBQUksS0FBSyxDQUFDLE1BQU0sSUFBSSxLQUFLLENBQUMsVUFBVSxJQUFJLENBQUMsS0FBSyxDQUFDLEdBQUcsRUFBRTtZQUNsRCxnQkFBZ0IsQ0FBQyxLQUFLLEVBQUUsYUFBYSxFQUFFLEtBQUssQ0FBQyxNQUFNLENBQUMsV0FBVyxDQUFDLENBQUM7U0FDbEU7SUFDSCxDQUFDOzs7OztJQUVELFlBQVksQ0FBQyxLQUE2QjtRQUN4QyxJQUFJLEtBQUssQ0FBQyxNQUFNLEVBQUU7WUFDaEIsT0FBTztTQUNSOztjQUVLLGNBQWMsR0FBRyxJQUFJLENBQUMsYUFBYSxDQUFDLEtBQUssQ0FBQztRQUNoRCxJQUFJLGNBQWMsQ0FBQyxNQUFNLEtBQUssQ0FBQyxFQUFFO1lBQy9C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QyxHQUFHLENBQUMsQ0FBQyxJQUFJLENBQUMsQ0FBQyxFQUFDLENBQUM7U0FDeEU7YUFBTTtZQUNMLFVBQVUsQ0FBQyxJQUFJLENBQUMsR0FBRyxLQUFLLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQztTQUNqQztRQUVELElBQUksS0FBSyxDQUFDLFVBQVUsRUFBRTtZQUNwQixLQUFLLENBQUMsVUFBVTtpQkFDYixNQUFNOzs7O1lBQUMsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxDQUFDLENBQUMsQ0FBQyxHQUFHLElBQUksQ0FBQyxDQUFDLFVBQVUsRUFBQztpQkFDbkMsT0FBTzs7OztZQUFDLENBQUMsQ0FBQyxFQUFFLENBQUMsVUFBVSxDQUFDLElBQUksQ0FBQyxHQUFHLElBQUksQ0FBQyxlQUFlLENBQUMsQ0FBQyxFQUFFLElBQUksQ0FBQyxDQUFDLEVBQUMsQ0FBQztTQUNwRTtRQUVELE9BQU8sVUFBVSxDQUFDO0lBQ3BCLENBQUM7Q0FDRjs7Ozs7O0lBL0dhLG9DQUE0QiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEZvcm1seUV4dGVuc2lvbiwgRm9ybWx5Q29uZmlnIH0gZnJvbSAnLi4vLi4vc2VydmljZXMvZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlIH0gZnJvbSAnLi4vLi4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IEZvcm1Hcm91cCwgRm9ybUNvbnRyb2wsIEFic3RyYWN0Q29udHJvbE9wdGlvbnMsIFZhbGlkYXRvcnMsIFZhbGlkYXRvckZuLCBBc3luY1ZhbGlkYXRvckZuIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgZ2V0RmllbGRWYWx1ZSwgZGVmaW5lSGlkZGVuUHJvcCB9IGZyb20gJy4uLy4uL3V0aWxzJztcbmltcG9ydCB7IHJlZ2lzdGVyQ29udHJvbCwgZmluZENvbnRyb2wsIHVwZGF0ZVZhbGlkaXR5IGFzIHVwZGF0ZUNvbnRyb2xWYWxpZGl0eSB9IGZyb20gJy4vdXRpbHMnO1xuaW1wb3J0IHsgb2YgfSBmcm9tICdyeGpzJztcblxuLyoqIEBleHBlcmltZW50YWwgKi9cbmV4cG9ydCBjbGFzcyBGaWVsZEZvcm1FeHRlbnNpb24gaW1wbGVtZW50cyBGb3JtbHlFeHRlbnNpb24ge1xuICBjb25zdHJ1Y3Rvcihwcml2YXRlIGNvbmZpZzogRm9ybWx5Q29uZmlnKSB7IH1cblxuICBvblBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgaWYgKGZpZWxkLmtleSkge1xuICAgICAgdGhpcy5hZGRGb3JtQ29udHJvbChmaWVsZCk7XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLnBhcmVudCAmJiBmaWVsZC5maWVsZEdyb3VwICYmICFmaWVsZC5rZXkpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3AoZmllbGQsICdmb3JtQ29udHJvbCcsIGZpZWxkLnBhcmVudC5mb3JtQ29udHJvbCk7XG4gICAgfVxuICB9XG5cbiAgcG9zdFBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgaWYgKGZpZWxkLnBhcmVudCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGNvbnN0IGZpZWxkc1RvVXBkYXRlID0gdGhpcy5zZXRWYWxpZGF0b3JzKGZpZWxkKTtcbiAgICBpZiAoZmllbGRzVG9VcGRhdGUubGVuZ3RoID09PSAwKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkc1RvVXBkYXRlLmxlbmd0aCA9PT0gMSkge1xuICAgICAgZmllbGRzVG9VcGRhdGVbMF0uZm9ybUNvbnRyb2wudXBkYXRlVmFsdWVBbmRWYWxpZGl0eSgpO1xuICAgIH0gZWxzZSB7XG4gICAgICAoZmllbGQuZm9ybUNvbnRyb2wgYXMgYW55KS5fdXBkYXRlVHJlZVZhbGlkaXR5KCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBhZGRGb3JtQ29udHJvbChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIGxldCBjb250cm9sID0gZmluZENvbnRyb2woZmllbGQpO1xuICAgIGlmICghY29udHJvbCkge1xuICAgICAgY29uc3QgY29udHJvbE9wdGlvbnM6IEFic3RyYWN0Q29udHJvbE9wdGlvbnMgPSB7IHVwZGF0ZU9uOiBmaWVsZC5tb2RlbE9wdGlvbnMudXBkYXRlT24gfTtcbiAgICAgIGNvbnN0IHZhbHVlID0gZ2V0RmllbGRWYWx1ZShmaWVsZCk7XG5cbiAgICAgIGNvbnN0IHJlZiA9IHRoaXMuY29uZmlnID8gdGhpcy5jb25maWcucmVzb2x2ZUZpZWxkVHlwZVJlZihmaWVsZCkgOiBudWxsO1xuICAgICAgaWYgKHJlZiAmJiByZWYuY29tcG9uZW50VHlwZSAmJiByZWYuY29tcG9uZW50VHlwZVsnY3JlYXRlQ29udHJvbCddKSB7XG4gICAgICAgIGNvbnN0IGNvbXBvbmVudCA9IHJlZi5jb21wb25lbnRUeXBlO1xuICAgICAgICBjb25zb2xlLndhcm4oYE5neEZvcm1seTogJyR7Y29tcG9uZW50Lm5hbWV9OjpjcmVhdGVDb250cm9sJyBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjAsIHVzZSAncHJlUG9wdWxhdGUnIGhvb2sgaW5zdGVhZC5gKTtcbiAgICAgICAgY29udHJvbCA9IGNvbXBvbmVudFsnY3JlYXRlQ29udHJvbCddKHZhbHVlLCBmaWVsZCk7XG4gICAgICB9IGVsc2UgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgICAgLy8gVE9ETzogbW92ZSB0byBwb3N0UG9wdWxhdGVcbiAgICAgICAgY29udHJvbCA9IG5ldyBGb3JtR3JvdXAoe30sIGNvbnRyb2xPcHRpb25zKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGNvbnRyb2wgPSBuZXcgRm9ybUNvbnRyb2wodmFsdWUsIGNvbnRyb2xPcHRpb25zKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICByZWdpc3RlckNvbnRyb2woZmllbGQsIGNvbnRyb2wpO1xuICB9XG5cbiAgcHJpdmF0ZSBzZXRWYWxpZGF0b3JzKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgbGV0IHVwZGF0ZVZhbGlkaXR5ID0gZmFsc2U7XG4gICAgaWYgKGZpZWxkLmtleSB8fCAhZmllbGQucGFyZW50KSB7XG4gICAgICBjb25zdCB7IGZvcm1Db250cm9sOiBjIH0gPSBmaWVsZDtcbiAgICAgIGNvbnN0IGRpc2FibGVkID0gZmllbGQudGVtcGxhdGVPcHRpb25zID8gZmllbGQudGVtcGxhdGVPcHRpb25zLmRpc2FibGVkIDogZmFsc2U7XG4gICAgICBpZiAoZGlzYWJsZWQgJiYgYy5lbmFibGVkKSB7XG4gICAgICAgIGMuZGlzYWJsZSh7IGVtaXRFdmVudDogZmFsc2UsIG9ubHlTZWxmOiB0cnVlIH0pO1xuICAgICAgICB1cGRhdGVWYWxpZGl0eSA9IHRydWU7XG4gICAgICB9XG5cbiAgICAgIGlmIChudWxsID09PSBjLnZhbGlkYXRvciB8fCBudWxsID09PSBjLmFzeW5jVmFsaWRhdG9yKSB7XG4gICAgICAgIGMuc2V0VmFsaWRhdG9ycygoKSA9PiB7XG4gICAgICAgICAgY29uc3QgdiA9IFZhbGlkYXRvcnMuY29tcG9zZSh0aGlzLm1lcmdlVmFsaWRhdG9yczxWYWxpZGF0b3JGbj4oZmllbGQsICdfdmFsaWRhdG9ycycpKTtcblxuICAgICAgICAgIHJldHVybiB2ID8gdihjKSA6IG51bGw7XG4gICAgICAgIH0pO1xuICAgICAgICBjLnNldEFzeW5jVmFsaWRhdG9ycygoKSA9PiB7XG4gICAgICAgICAgY29uc3QgdiA9IFZhbGlkYXRvcnMuY29tcG9zZUFzeW5jKHRoaXMubWVyZ2VWYWxpZGF0b3JzPEFzeW5jVmFsaWRhdG9yRm4+KGZpZWxkLCAnX2FzeW5jVmFsaWRhdG9ycycpKTtcblxuICAgICAgICAgIHJldHVybiB2ID8gdihjKSA6IG9mKG51bGwpO1xuICAgICAgICB9KTtcblxuICAgICAgICBpZiAoIWMucGFyZW50KSB7XG4gICAgICAgICAgdXBkYXRlQ29udHJvbFZhbGlkaXR5KGMpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHVwZGF0ZVZhbGlkaXR5ID0gdHJ1ZTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cblxuICAgIGNvbnN0IGZpZWxkc1RvVXBkYXRlID0gdXBkYXRlVmFsaWRpdHkgPyBbZmllbGRdIDogW107XG4gICAgKGZpZWxkLmZpZWxkR3JvdXAgfHwgW10pLmZvckVhY2goZiA9PiB7XG4gICAgICBjb25zdCBjaGlsZHJlblRvVXBkYXRlID0gdGhpcy5zZXRWYWxpZGF0b3JzKGYpO1xuICAgICAgaWYgKCF1cGRhdGVWYWxpZGl0eSkge1xuICAgICAgICBmaWVsZHNUb1VwZGF0ZS5wdXNoKC4uLmNoaWxkcmVuVG9VcGRhdGUpO1xuICAgICAgfVxuICAgIH0pO1xuXG4gICAgcmV0dXJuIGZpZWxkc1RvVXBkYXRlO1xuICB9XG5cbiAgcHJpdmF0ZSBtZXJnZVZhbGlkYXRvcnM8VD4oZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHR5cGU6ICdfdmFsaWRhdG9ycycgfCAnX2FzeW5jVmFsaWRhdG9ycycpOiBUW10ge1xuICAgIGNvbnN0IHZhbGlkYXRvcnM6IGFueSA9IFtdO1xuICAgIGNvbnN0IGMgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgICBpZiAoYyAmJiBjWydfZmllbGRzJ10gJiYgY1snX2ZpZWxkcyddLmxlbmd0aCA+IDEpIHtcbiAgICAgIGNbJ19maWVsZHMnXVxuICAgICAgICAuZmlsdGVyKChmOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSA9PiAhZi5faGlkZSlcbiAgICAgICAgLmZvckVhY2goKGY6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpID0+IHZhbGlkYXRvcnMucHVzaCguLi5mW3R5cGVdKSk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHZhbGlkYXRvcnMucHVzaCguLi5maWVsZFt0eXBlXSk7XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLmZpZWxkR3JvdXBcbiAgICAgICAgLmZpbHRlcihmID0+ICFmLmtleSAmJiBmLmZpZWxkR3JvdXApXG4gICAgICAgIC5mb3JFYWNoKGYgPT4gdmFsaWRhdG9ycy5wdXNoKC4uLnRoaXMubWVyZ2VWYWxpZGF0b3JzKGYsIHR5cGUpKSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHZhbGlkYXRvcnM7XG4gIH1cbn1cbiJdfQ==