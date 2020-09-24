/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { getFieldValue, defineHiddenProp } from '../../utils';
import { registerControl, findControl, updateValidity as updateControlValidity } from './utils';
import { of } from 'rxjs';
/**
 * \@experimental
 */
var /**
 * \@experimental
 */
FieldFormExtension = /** @class */ (function () {
    function FieldFormExtension(config) {
        this.config = config;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    FieldFormExtension.prototype.onPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        if (field.key) {
            this.addFormControl(field);
        }
        if (field.parent && field.fieldGroup && !field.key) {
            defineHiddenProp(field, 'formControl', field.parent.formControl);
        }
    };
    /**
     * @param {?} field
     * @return {?}
     */
    FieldFormExtension.prototype.postPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        if (field.parent) {
            return;
        }
        /** @type {?} */
        var fieldsToUpdate = this.setValidators(field);
        if (fieldsToUpdate.length === 0) {
            return;
        }
        if (fieldsToUpdate.length === 1) {
            fieldsToUpdate[0].formControl.updateValueAndValidity();
        }
        else {
            ((/** @type {?} */ (field.formControl)))._updateTreeValidity();
        }
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    FieldFormExtension.prototype.addFormControl = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        /** @type {?} */
        var control = findControl(field);
        if (!control) {
            /** @type {?} */
            var controlOptions = { updateOn: field.modelOptions.updateOn };
            /** @type {?} */
            var value = getFieldValue(field);
            /** @type {?} */
            var ref = this.config ? this.config.resolveFieldTypeRef(field) : null;
            if (ref && ref.componentType && ref.componentType['createControl']) {
                /** @type {?} */
                var component = ref.componentType;
                console.warn("NgxFormly: '" + component.name + "::createControl' is deprecated since v5.0, use 'prePopulate' hook instead.");
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
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    FieldFormExtension.prototype.setValidators = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        /** @type {?} */
        var updateValidity = false;
        if (field.key || !field.parent) {
            var c_1 = field.formControl;
            /** @type {?} */
            var disabled = field.templateOptions ? field.templateOptions.disabled : false;
            if (disabled && c_1.enabled) {
                c_1.disable({ emitEvent: false, onlySelf: true });
                updateValidity = true;
            }
            if (null === c_1.validator || null === c_1.asyncValidator) {
                c_1.setValidators((/**
                 * @return {?}
                 */
                function () {
                    /** @type {?} */
                    var v = Validators.compose(_this.mergeValidators(field, '_validators'));
                    return v ? v(c_1) : null;
                }));
                c_1.setAsyncValidators((/**
                 * @return {?}
                 */
                function () {
                    /** @type {?} */
                    var v = Validators.composeAsync(_this.mergeValidators(field, '_asyncValidators'));
                    return v ? v(c_1) : of(null);
                }));
                if (!c_1.parent) {
                    updateControlValidity(c_1);
                }
                else {
                    updateValidity = true;
                }
            }
        }
        /** @type {?} */
        var fieldsToUpdate = updateValidity ? [field] : [];
        (field.fieldGroup || []).forEach((/**
         * @param {?} f
         * @return {?}
         */
        function (f) {
            /** @type {?} */
            var childrenToUpdate = _this.setValidators(f);
            if (!updateValidity) {
                fieldsToUpdate.push.apply(fieldsToUpdate, tslib_1.__spread(childrenToUpdate));
            }
        }));
        return fieldsToUpdate;
    };
    /**
     * @private
     * @template T
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    FieldFormExtension.prototype.mergeValidators = /**
     * @private
     * @template T
     * @param {?} field
     * @param {?} type
     * @return {?}
     */
    function (field, type) {
        var _this = this;
        /** @type {?} */
        var validators = [];
        /** @type {?} */
        var c = field.formControl;
        if (c && c['_fields'] && c['_fields'].length > 1) {
            c['_fields']
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return !f._hide; }))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return validators.push.apply(validators, tslib_1.__spread(f[type])); }));
        }
        else {
            validators.push.apply(validators, tslib_1.__spread(field[type]));
        }
        if (field.fieldGroup) {
            field.fieldGroup
                .filter((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return !f.key && f.fieldGroup; }))
                .forEach((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return validators.push.apply(validators, tslib_1.__spread(_this.mergeValidators(f, type))); }));
        }
        return validators;
    };
    return FieldFormExtension;
}());
/**
 * \@experimental
 */
export { FieldFormExtension };
if (false) {
    /**
     * @type {?}
     * @private
     */
    FieldFormExtension.prototype.config;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtZm9ybS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9maWVsZC1mb3JtL2ZpZWxkLWZvcm0udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFFQSxPQUFPLEVBQUUsU0FBUyxFQUFFLFdBQVcsRUFBMEIsVUFBVSxFQUFpQyxNQUFNLGdCQUFnQixDQUFDO0FBQzNILE9BQU8sRUFBRSxhQUFhLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSxhQUFhLENBQUM7QUFDOUQsT0FBTyxFQUFFLGVBQWUsRUFBRSxXQUFXLEVBQUUsY0FBYyxJQUFJLHFCQUFxQixFQUFFLE1BQU0sU0FBUyxDQUFDO0FBQ2hHLE9BQU8sRUFBRSxFQUFFLEVBQUUsTUFBTSxNQUFNLENBQUM7Ozs7QUFHMUI7Ozs7SUFDRSw0QkFBb0IsTUFBb0I7UUFBcEIsV0FBTSxHQUFOLE1BQU0sQ0FBYztJQUFJLENBQUM7Ozs7O0lBRTdDLHVDQUFVOzs7O0lBQVYsVUFBVyxLQUE2QjtRQUN0QyxJQUFJLEtBQUssQ0FBQyxHQUFHLEVBQUU7WUFDYixJQUFJLENBQUMsY0FBYyxDQUFDLEtBQUssQ0FBQyxDQUFDO1NBQzVCO1FBRUQsSUFBSSxLQUFLLENBQUMsTUFBTSxJQUFJLEtBQUssQ0FBQyxVQUFVLElBQUksQ0FBQyxLQUFLLENBQUMsR0FBRyxFQUFFO1lBQ2xELGdCQUFnQixDQUFDLEtBQUssRUFBRSxhQUFhLEVBQUUsS0FBSyxDQUFDLE1BQU0sQ0FBQyxXQUFXLENBQUMsQ0FBQztTQUNsRTtJQUNILENBQUM7Ozs7O0lBRUQseUNBQVk7Ozs7SUFBWixVQUFhLEtBQTZCO1FBQ3hDLElBQUksS0FBSyxDQUFDLE1BQU0sRUFBRTtZQUNoQixPQUFPO1NBQ1I7O1lBRUssY0FBYyxHQUFHLElBQUksQ0FBQyxhQUFhLENBQUMsS0FBSyxDQUFDO1FBQ2hELElBQUksY0FBYyxDQUFDLE1BQU0sS0FBSyxDQUFDLEVBQUU7WUFD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VUFBVSxDQUFDLElBQUksT0FBZixVQUFVLG1CQUFTLENBQUMsQ0FBQyxJQUFJLENBQUMsSUFBMUIsQ0FBMkIsRUFBQyxDQUFDO1NBQ3hFO2FBQU07WUFDTCxVQUFVLENBQUMsSUFBSSxPQUFmLFVBQVUsbUJBQVMsS0FBSyxDQUFDLElBQUksQ0FBQyxHQUFFO1NBQ2pDO1FBRUQsSUFBSSxLQUFLLENBQUMsVUFBVSxFQUFFO1lBQ3BCLEtBQUssQ0FBQyxVQUFVO2lCQUNiLE1BQU07Ozs7WUFBQyxVQUFBLENBQUMsSUFBSSxPQUFBLENBQUMsQ0FBQyxDQUFDLEdBQUcsSUFBSSxDQUFDLENBQUMsVUFBVSxFQUF0QixDQUFzQixFQUFDO2lCQUNuQyxPQUFPOzs7O1lBQUMsVUFBQSxDQUFDLElBQUksT0FBQSxVQUFVLENBQUMsSUFBSSxPQUFmLFVBQVUsbUJBQVMsS0FBSSxDQUFDLGVBQWUsQ0FBQyxDQUFDLEVBQUUsSUFBSSxDQUFDLElBQWhELENBQWlELEVBQUMsQ0FBQztTQUNwRTtRQUVELE9BQU8sVUFBVSxDQUFDO0lBQ3BCLENBQUM7SUFDSCx5QkFBQztBQUFELENBQUMsQUFoSEQsSUFnSEM7Ozs7Ozs7Ozs7SUEvR2Esb0NBQTRCIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRm9ybWx5RXh0ZW5zaW9uLCBGb3JtbHlDb25maWcgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi8uLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgRm9ybUdyb3VwLCBGb3JtQ29udHJvbCwgQWJzdHJhY3RDb250cm9sT3B0aW9ucywgVmFsaWRhdG9ycywgVmFsaWRhdG9yRm4sIEFzeW5jVmFsaWRhdG9yRm4gfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBnZXRGaWVsZFZhbHVlLCBkZWZpbmVIaWRkZW5Qcm9wIH0gZnJvbSAnLi4vLi4vdXRpbHMnO1xuaW1wb3J0IHsgcmVnaXN0ZXJDb250cm9sLCBmaW5kQ29udHJvbCwgdXBkYXRlVmFsaWRpdHkgYXMgdXBkYXRlQ29udHJvbFZhbGlkaXR5IH0gZnJvbSAnLi91dGlscyc7XG5pbXBvcnQgeyBvZiB9IGZyb20gJ3J4anMnO1xuXG4vKiogQGV4cGVyaW1lbnRhbCAqL1xuZXhwb3J0IGNsYXNzIEZpZWxkRm9ybUV4dGVuc2lvbiBpbXBsZW1lbnRzIEZvcm1seUV4dGVuc2lvbiB7XG4gIGNvbnN0cnVjdG9yKHByaXZhdGUgY29uZmlnOiBGb3JtbHlDb25maWcpIHsgfVxuXG4gIG9uUG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoZmllbGQua2V5KSB7XG4gICAgICB0aGlzLmFkZEZvcm1Db250cm9sKGZpZWxkKTtcbiAgICB9XG5cbiAgICBpZiAoZmllbGQucGFyZW50ICYmIGZpZWxkLmZpZWxkR3JvdXAgJiYgIWZpZWxkLmtleSkge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcChmaWVsZCwgJ2Zvcm1Db250cm9sJywgZmllbGQucGFyZW50LmZvcm1Db250cm9sKTtcbiAgICB9XG4gIH1cblxuICBwb3N0UG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoZmllbGQucGFyZW50KSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgY29uc3QgZmllbGRzVG9VcGRhdGUgPSB0aGlzLnNldFZhbGlkYXRvcnMoZmllbGQpO1xuICAgIGlmIChmaWVsZHNUb1VwZGF0ZS5sZW5ndGggPT09IDApIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBpZiAoZmllbGRzVG9VcGRhdGUubGVuZ3RoID09PSAxKSB7XG4gICAgICBmaWVsZHNUb1VwZGF0ZVswXS5mb3JtQ29udHJvbC51cGRhdGVWYWx1ZUFuZFZhbGlkaXR5KCk7XG4gICAgfSBlbHNlIHtcbiAgICAgIChmaWVsZC5mb3JtQ29udHJvbCBhcyBhbnkpLl91cGRhdGVUcmVlVmFsaWRpdHkoKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIGFkZEZvcm1Db250cm9sKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgbGV0IGNvbnRyb2wgPSBmaW5kQ29udHJvbChmaWVsZCk7XG4gICAgaWYgKCFjb250cm9sKSB7XG4gICAgICBjb25zdCBjb250cm9sT3B0aW9uczogQWJzdHJhY3RDb250cm9sT3B0aW9ucyA9IHsgdXBkYXRlT246IGZpZWxkLm1vZGVsT3B0aW9ucy51cGRhdGVPbiB9O1xuICAgICAgY29uc3QgdmFsdWUgPSBnZXRGaWVsZFZhbHVlKGZpZWxkKTtcblxuICAgICAgY29uc3QgcmVmID0gdGhpcy5jb25maWcgPyB0aGlzLmNvbmZpZy5yZXNvbHZlRmllbGRUeXBlUmVmKGZpZWxkKSA6IG51bGw7XG4gICAgICBpZiAocmVmICYmIHJlZi5jb21wb25lbnRUeXBlICYmIHJlZi5jb21wb25lbnRUeXBlWydjcmVhdGVDb250cm9sJ10pIHtcbiAgICAgICAgY29uc3QgY29tcG9uZW50ID0gcmVmLmNvbXBvbmVudFR5cGU7XG4gICAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiAnJHtjb21wb25lbnQubmFtZX06OmNyZWF0ZUNvbnRyb2wnIGlzIGRlcHJlY2F0ZWQgc2luY2UgdjUuMCwgdXNlICdwcmVQb3B1bGF0ZScgaG9vayBpbnN0ZWFkLmApO1xuICAgICAgICBjb250cm9sID0gY29tcG9uZW50WydjcmVhdGVDb250cm9sJ10odmFsdWUsIGZpZWxkKTtcbiAgICAgIH0gZWxzZSBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgICAvLyBUT0RPOiBtb3ZlIHRvIHBvc3RQb3B1bGF0ZVxuICAgICAgICBjb250cm9sID0gbmV3IEZvcm1Hcm91cCh7fSwgY29udHJvbE9wdGlvbnMpO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgY29udHJvbCA9IG5ldyBGb3JtQ29udHJvbCh2YWx1ZSwgY29udHJvbE9wdGlvbnMpO1xuICAgICAgfVxuICAgIH1cblxuICAgIHJlZ2lzdGVyQ29udHJvbChmaWVsZCwgY29udHJvbCk7XG4gIH1cblxuICBwcml2YXRlIHNldFZhbGlkYXRvcnMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBsZXQgdXBkYXRlVmFsaWRpdHkgPSBmYWxzZTtcbiAgICBpZiAoZmllbGQua2V5IHx8ICFmaWVsZC5wYXJlbnQpIHtcbiAgICAgIGNvbnN0IHsgZm9ybUNvbnRyb2w6IGMgfSA9IGZpZWxkO1xuICAgICAgY29uc3QgZGlzYWJsZWQgPSBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMgPyBmaWVsZC50ZW1wbGF0ZU9wdGlvbnMuZGlzYWJsZWQgOiBmYWxzZTtcbiAgICAgIGlmIChkaXNhYmxlZCAmJiBjLmVuYWJsZWQpIHtcbiAgICAgICAgYy5kaXNhYmxlKHsgZW1pdEV2ZW50OiBmYWxzZSwgb25seVNlbGY6IHRydWUgfSk7XG4gICAgICAgIHVwZGF0ZVZhbGlkaXR5ID0gdHJ1ZTtcbiAgICAgIH1cblxuICAgICAgaWYgKG51bGwgPT09IGMudmFsaWRhdG9yIHx8IG51bGwgPT09IGMuYXN5bmNWYWxpZGF0b3IpIHtcbiAgICAgICAgYy5zZXRWYWxpZGF0b3JzKCgpID0+IHtcbiAgICAgICAgICBjb25zdCB2ID0gVmFsaWRhdG9ycy5jb21wb3NlKHRoaXMubWVyZ2VWYWxpZGF0b3JzPFZhbGlkYXRvckZuPihmaWVsZCwgJ192YWxpZGF0b3JzJykpO1xuXG4gICAgICAgICAgcmV0dXJuIHYgPyB2KGMpIDogbnVsbDtcbiAgICAgICAgfSk7XG4gICAgICAgIGMuc2V0QXN5bmNWYWxpZGF0b3JzKCgpID0+IHtcbiAgICAgICAgICBjb25zdCB2ID0gVmFsaWRhdG9ycy5jb21wb3NlQXN5bmModGhpcy5tZXJnZVZhbGlkYXRvcnM8QXN5bmNWYWxpZGF0b3JGbj4oZmllbGQsICdfYXN5bmNWYWxpZGF0b3JzJykpO1xuXG4gICAgICAgICAgcmV0dXJuIHYgPyB2KGMpIDogb2YobnVsbCk7XG4gICAgICAgIH0pO1xuXG4gICAgICAgIGlmICghYy5wYXJlbnQpIHtcbiAgICAgICAgICB1cGRhdGVDb250cm9sVmFsaWRpdHkoYyk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgdXBkYXRlVmFsaWRpdHkgPSB0cnVlO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuXG4gICAgY29uc3QgZmllbGRzVG9VcGRhdGUgPSB1cGRhdGVWYWxpZGl0eSA/IFtmaWVsZF0gOiBbXTtcbiAgICAoZmllbGQuZmllbGRHcm91cCB8fCBbXSkuZm9yRWFjaChmID0+IHtcbiAgICAgIGNvbnN0IGNoaWxkcmVuVG9VcGRhdGUgPSB0aGlzLnNldFZhbGlkYXRvcnMoZik7XG4gICAgICBpZiAoIXVwZGF0ZVZhbGlkaXR5KSB7XG4gICAgICAgIGZpZWxkc1RvVXBkYXRlLnB1c2goLi4uY2hpbGRyZW5Ub1VwZGF0ZSk7XG4gICAgICB9XG4gICAgfSk7XG5cbiAgICByZXR1cm4gZmllbGRzVG9VcGRhdGU7XG4gIH1cblxuICBwcml2YXRlIG1lcmdlVmFsaWRhdG9yczxUPihmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSwgdHlwZTogJ192YWxpZGF0b3JzJyB8ICdfYXN5bmNWYWxpZGF0b3JzJyk6IFRbXSB7XG4gICAgY29uc3QgdmFsaWRhdG9yczogYW55ID0gW107XG4gICAgY29uc3QgYyA9IGZpZWxkLmZvcm1Db250cm9sO1xuICAgIGlmIChjICYmIGNbJ19maWVsZHMnXSAmJiBjWydfZmllbGRzJ10ubGVuZ3RoID4gMSkge1xuICAgICAgY1snX2ZpZWxkcyddXG4gICAgICAgIC5maWx0ZXIoKGY6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpID0+ICFmLl9oaWRlKVxuICAgICAgICAuZm9yRWFjaCgoZjogRm9ybWx5RmllbGRDb25maWdDYWNoZSkgPT4gdmFsaWRhdG9ycy5wdXNoKC4uLmZbdHlwZV0pKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdmFsaWRhdG9ycy5wdXNoKC4uLmZpZWxkW3R5cGVdKTtcbiAgICB9XG5cbiAgICBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgZmllbGQuZmllbGRHcm91cFxuICAgICAgICAuZmlsdGVyKGYgPT4gIWYua2V5ICYmIGYuZmllbGRHcm91cClcbiAgICAgICAgLmZvckVhY2goZiA9PiB2YWxpZGF0b3JzLnB1c2goLi4udGhpcy5tZXJnZVZhbGlkYXRvcnMoZiwgdHlwZSkpKTtcbiAgICB9XG5cbiAgICByZXR1cm4gdmFsaWRhdG9ycztcbiAgfVxufVxuIl19