/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Inject, Optional } from '@angular/core';
import { FormArray } from '@angular/forms';
import { FieldType } from './field.type';
import { clone, isNullOrUndefined, assignFieldValue } from '../utils';
import { FormlyFormBuilder } from '../services/formly.form.builder';
import { FORMLY_CONFIG } from '../services/formly.config';
import { registerControl, unregisterControl } from '../extensions/field-form/utils';
/**
 * @abstract
 * @template F
 */
var FieldArrayType = /** @class */ (function (_super) {
    tslib_1.__extends(FieldArrayType, _super);
    function FieldArrayType(builder) {
        var _this = _super.call(this) || this;
        _this.defaultOptions = {
            defaultValue: [],
        };
        if (builder instanceof FormlyFormBuilder) {
            console.warn("NgxFormly: passing 'FormlyFormBuilder' to '" + _this.constructor.name + "' type is not required anymore, you may remove it!");
        }
        return _this;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    FieldArrayType.prototype.onPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        if (!field.formControl && field.key) {
            registerControl(field, new FormArray([], { updateOn: field.modelOptions.updateOn }));
        }
        field.fieldGroup = field.fieldGroup || [];
        /** @type {?} */
        var length = field.model ? field.model.length : 0;
        if (field.fieldGroup.length > length) {
            for (var i = field.fieldGroup.length - 1; i >= length; --i) {
                unregisterControl(field.fieldGroup[i]);
                field.fieldGroup.splice(i, 1);
            }
        }
        for (var i = field.fieldGroup.length; i < length; i++) {
            /** @type {?} */
            var f = tslib_1.__assign({}, clone(field.fieldArray), { key: "" + i });
            field.fieldGroup.push(f);
        }
    };
    /**
     * @param {?=} i
     * @param {?=} initialModel
     * @param {?=} __2
     * @return {?}
     */
    FieldArrayType.prototype.add = /**
     * @param {?=} i
     * @param {?=} initialModel
     * @param {?=} __2
     * @return {?}
     */
    function (i, initialModel, _a) {
        var markAsDirty = (_a === void 0 ? { markAsDirty: true } : _a).markAsDirty;
        i = isNullOrUndefined(i) ? this.field.fieldGroup.length : i;
        if (!this.model) {
            assignFieldValue(this.field, []);
        }
        this.model.splice(i, 0, initialModel ? clone(initialModel) : undefined);
        ((/** @type {?} */ (this.options)))._buildForm(true);
        markAsDirty && this.formControl.markAsDirty();
    };
    /**
     * @param {?} i
     * @param {?=} __1
     * @return {?}
     */
    FieldArrayType.prototype.remove = /**
     * @param {?} i
     * @param {?=} __1
     * @return {?}
     */
    function (i, _a) {
        var markAsDirty = (_a === void 0 ? { markAsDirty: true } : _a).markAsDirty;
        this.model.splice(i, 1);
        unregisterControl(this.field.fieldGroup[i], true);
        this.field.fieldGroup.splice(i, 1);
        this.field.fieldGroup.forEach((/**
         * @param {?} f
         * @param {?} key
         * @return {?}
         */
        function (f, key) { return f.key = "" + key; }));
        ((/** @type {?} */ (this.options)))._buildForm(true);
        markAsDirty && this.formControl.markAsDirty();
    };
    /** @nocollapse */
    FieldArrayType.ctorParameters = function () { return [
        { type: FormlyFormBuilder, decorators: [{ type: Inject, args: [FORMLY_CONFIG,] }, { type: Optional }] }
    ]; };
    return FieldArrayType;
}(FieldType));
export { FieldArrayType };
if (false) {
    /** @type {?} */
    FieldArrayType.prototype.formControl;
    /** @type {?} */
    FieldArrayType.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtYXJyYXkudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLWFycmF5LnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsTUFBTSxFQUFFLFFBQVEsRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUNqRCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFDM0MsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGNBQWMsQ0FBQztBQUN6QyxPQUFPLEVBQUUsS0FBSyxFQUFFLGlCQUFpQixFQUFFLGdCQUFnQixFQUFFLE1BQU0sVUFBVSxDQUFDO0FBQ3RFLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBRXBFLE9BQU8sRUFBRSxhQUFhLEVBQW1CLE1BQU0sMkJBQTJCLENBQUM7QUFDM0UsT0FBTyxFQUFFLGVBQWUsRUFBRSxpQkFBaUIsRUFBRSxNQUFNLGdDQUFnQyxDQUFDOzs7OztBQUVwRjtJQUE4RiwwQ0FBWTtJQU14Ryx3QkFBK0MsT0FBMkI7UUFBMUUsWUFDRSxpQkFBTyxTQUtSO1FBVkQsb0JBQWMsR0FBUTtZQUNwQixZQUFZLEVBQUUsRUFBRTtTQUNqQixDQUFDO1FBS0EsSUFBSSxPQUFPLFlBQVksaUJBQWlCLEVBQUU7WUFDeEMsT0FBTyxDQUFDLElBQUksQ0FBQyxnREFBOEMsS0FBSSxDQUFDLFdBQVcsQ0FBQyxJQUFJLHVEQUFvRCxDQUFDLENBQUM7U0FDdkk7O0lBQ0gsQ0FBQzs7Ozs7SUFFRCxtQ0FBVTs7OztJQUFWLFVBQVcsS0FBd0I7UUFDakMsSUFBSSxDQUFDLEtBQUssQ0FBQyxXQUFXLElBQUksS0FBSyxDQUFDLEdBQUcsRUFBRTtZQUNuQyxlQUFlLENBQUMsS0FBSyxFQUFFLElBQUksU0FBUyxDQUFDLEVBQUUsRUFBRSxFQUFFLFFBQVEsRUFBRSxLQUFLLENBQUMs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LE1BQU0sU0FBQyxhQUFhLGNBQUcsUUFBUTs7SUFrRDlDLHFCQUFDO0NBQUEsQUF4REQsQ0FBOEYsU0FBUyxHQXdEdEc7U0F4RHFCLGNBQWM7OztJQUNsQyxxQ0FBdUI7O0lBQ3ZCLHdDQUVFIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgSW5qZWN0LCBPcHRpb25hbCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRm9ybUFycmF5IH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnLi9maWVsZC50eXBlJztcbmltcG9ydCB7IGNsb25lLCBpc051bGxPclVuZGVmaW5lZCwgYXNzaWduRmllbGRWYWx1ZSB9IGZyb20gJy4uL3V0aWxzJztcbmltcG9ydCB7IEZvcm1seUZvcm1CdWlsZGVyIH0gZnJvbSAnLi4vc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlcic7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZyB9IGZyb20gJy4uL2NvbXBvbmVudHMvZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBGT1JNTFlfQ09ORklHLCBGb3JtbHlFeHRlbnNpb24gfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IHJlZ2lzdGVyQ29udHJvbCwgdW5yZWdpc3RlckNvbnRyb2wgfSBmcm9tICcuLi9leHRlbnNpb25zL2ZpZWxkLWZvcm0vdXRpbHMnO1xuXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGRBcnJheVR5cGU8RiBleHRlbmRzIEZvcm1seUZpZWxkQ29uZmlnID0gRm9ybWx5RmllbGRDb25maWc+IGV4dGVuZHMgRmllbGRUeXBlPEY+IGltcGxlbWVudHMgRm9ybWx5RXh0ZW5zaW9uIHtcbiAgZm9ybUNvbnRyb2w6IEZvcm1BcnJheTtcbiAgZGVmYXVsdE9wdGlvbnM6IGFueSA9IHtcbiAgICBkZWZhdWx0VmFsdWU6IFtdLFxuICB9O1xuXG4gIGNvbnN0cnVjdG9yKEBJbmplY3QoRk9STUxZX0NPTkZJRykgQE9wdGlvbmFsKCkgYnVpbGRlcj86IEZvcm1seUZvcm1CdWlsZGVyKSB7XG4gICAgc3VwZXIoKTtcblxuICAgIGlmIChidWlsZGVyIGluc3RhbmNlb2YgRm9ybWx5Rm9ybUJ1aWxkZXIpIHtcbiAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBwYXNzaW5nICdGb3JtbHlGb3JtQnVpbGRlcicgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgdHlwZSBpcyBub3QgcmVxdWlyZWQgYW55bW9yZSwgeW91IG1heSByZW1vdmUgaXQhYCk7XG4gICAgfVxuICB9XG5cbiAgb25Qb3B1bGF0ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWcpIHtcbiAgICBpZiAoIWZpZWxkLmZvcm1Db250cm9sICYmIGZpZWxkLmtleSkge1xuICAgICAgcmVnaXN0ZXJDb250cm9sKGZpZWxkLCBuZXcgRm9ybUFycmF5KFtdLCB7IHVwZGF0ZU9uOiBmaWVsZC5tb2RlbE9wdGlvbnMudXBkYXRlT24gfSkpO1xuICAgIH1cblxuICAgIGZpZWxkLmZpZWxkR3JvdXAgPSBmaWVsZC5maWVsZEdyb3VwIHx8IFtdO1xuXG4gICAgY29uc3QgbGVuZ3RoID0gZmllbGQubW9kZWwgPyBmaWVsZC5tb2RlbC5sZW5ndGggOiAwO1xuICAgIGlmIChmaWVsZC5maWVsZEdyb3VwLmxlbmd0aCA+IGxlbmd0aCkge1xuICAgICAgZm9yIChsZXQgaSA9IGZpZWxkLmZpZWxkR3JvdXAubGVuZ3RoIC0gMTsgaSA+PSBsZW5ndGg7IC0taSkge1xuICAgICAgICB1bnJlZ2lzdGVyQ29udHJvbChmaWVsZC5maWVsZEdyb3VwW2ldKTtcbiAgICAgICAgZmllbGQuZmllbGRHcm91cC5zcGxpY2UoaSwgMSk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgZm9yIChsZXQgaSA9IGZpZWxkLmZpZWxkR3JvdXAubGVuZ3RoOyBpIDwgbGVuZ3RoOyBpKyspIHtcbiAgICAgIGNvbnN0IGYgPSB7IC4uLmNsb25lKGZpZWxkLmZpZWxkQXJyYXkpLCBrZXk6IGAke2l9YCB9O1xuICAgICAgZmllbGQuZmllbGRHcm91cC5wdXNoKGYpO1xuICAgIH1cbiAgfVxuXG4gIGFkZChpPzogbnVtYmVyLCBpbml0aWFsTW9kZWw/OiBhbnksIHsgbWFya0FzRGlydHkgfSA9IHsgbWFya0FzRGlydHk6IHRydWUgfSkge1xuICAgIGkgPSBpc051bGxPclVuZGVmaW5lZChpKSA/IHRoaXMuZmllbGQuZmllbGRHcm91cC5sZW5ndGggOiBpO1xuICAgIGlmICghdGhpcy5tb2RlbCkge1xuICAgICAgYXNzaWduRmllbGRWYWx1ZSh0aGlzLmZpZWxkLCBbXSk7XG4gICAgfVxuXG4gICAgdGhpcy5tb2RlbC5zcGxpY2UoaSwgMCwgaW5pdGlhbE1vZGVsID8gY2xvbmUoaW5pdGlhbE1vZGVsKSA6IHVuZGVmaW5lZCk7XG5cbiAgICAoPGFueT4gdGhpcy5vcHRpb25zKS5fYnVpbGRGb3JtKHRydWUpO1xuICAgIG1hcmtBc0RpcnR5ICYmIHRoaXMuZm9ybUNvbnRyb2wubWFya0FzRGlydHkoKTtcbiAgfVxuXG4gIHJlbW92ZShpOiBudW1iZXIsIHsgbWFya0FzRGlydHkgfSA9IHsgbWFya0FzRGlydHk6IHRydWUgfSkge1xuICAgIHRoaXMubW9kZWwuc3BsaWNlKGksIDEpO1xuICAgIHVucmVnaXN0ZXJDb250cm9sKHRoaXMuZmllbGQuZmllbGRHcm91cFtpXSwgdHJ1ZSk7XG4gICAgdGhpcy5maWVsZC5maWVsZEdyb3VwLnNwbGljZShpLCAxKTtcbiAgICB0aGlzLmZpZWxkLmZpZWxkR3JvdXAuZm9yRWFjaCgoZiwga2V5KSA9PiBmLmtleSA9IGAke2tleX1gKTtcblxuICAgICg8YW55PiB0aGlzLm9wdGlvbnMpLl9idWlsZEZvcm0odHJ1ZSk7XG4gICAgbWFya0FzRGlydHkgJiYgdGhpcy5mb3JtQ29udHJvbC5tYXJrQXNEaXJ0eSgpO1xuICB9XG59XG4iXX0=