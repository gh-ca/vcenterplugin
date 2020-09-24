/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Input } from '@angular/core';
/**
 * @abstract
 * @template F
 */
var FieldType = /** @class */ (function () {
    function FieldType() {
    }
    Object.defineProperty(FieldType.prototype, "model", {
        get: /**
         * @return {?}
         */
        function () { return this.field.model; },
        set: /**
         * @param {?} m
         * @return {?}
         */
        function (m) { console.warn("NgxFormly: passing 'model' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!"); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "form", {
        get: /**
         * @return {?}
         */
        function () { return (/** @type {?} */ (this.field.parent.formControl)); },
        set: /**
         * @param {?} form
         * @return {?}
         */
        function (form) { console.warn("NgxFormly: passing 'form' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!"); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "options", {
        get: /**
         * @return {?}
         */
        function () { return this.field.options; },
        set: /**
         * @param {?} options
         * @return {?}
         */
        function (options) { console.warn("NgxFormly: passing 'options' input to '" + this.constructor.name + "' component is not required anymore, you may remove it!"); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "key", {
        get: /**
         * @return {?}
         */
        function () { return this.field.key; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "formControl", {
        get: /**
         * @return {?}
         */
        function () { return this.field.formControl; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "to", {
        get: /**
         * @return {?}
         */
        function () { return this.field.templateOptions || {}; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "showError", {
        get: /**
         * @return {?}
         */
        function () { return this.options.showError(this); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "id", {
        get: /**
         * @return {?}
         */
        function () { return this.field.id; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "formState", {
        get: /**
         * @return {?}
         */
        function () { return this.options.formState || {}; },
        enumerable: true,
        configurable: true
    });
    FieldType.propDecorators = {
        field: [{ type: Input }],
        model: [{ type: Input }],
        form: [{ type: Input }],
        options: [{ type: Input }]
    };
    return FieldType;
}());
export { FieldType };
if (false) {
    /** @type {?} */
    FieldType.prototype.field;
    /** @type {?} */
    FieldType.prototype.defaultOptions;
}
/**
 * @deprecated use `FieldType` instead
 * @abstract
 */
var /**
 * @deprecated use `FieldType` instead
 * @abstract
 */
Field = /** @class */ (function (_super) {
    tslib_1.__extends(Field, _super);
    function Field() {
        var _this = _super.call(this) || this;
        console.warn("NgxFormly: 'Field' has been renamed to 'FieldType', extend 'FieldType' instead.");
        return _this;
    }
    return Field;
}(FieldType));
/**
 * @deprecated use `FieldType` instead
 * @abstract
 */
export { Field };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsS0FBSyxFQUFFLE1BQU0sZUFBZSxDQUFDOzs7OztBQUl0QztJQUFBO0lBMkJBLENBQUM7SUF2QkMsc0JBQ0ksNEJBQUs7Ozs7UUFEVCxjQUNjLE9BQU8sSUFBSSxDQUFDLEtBQUssQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDOzs7OztRQUN4QyxVQUFVLENBQU0sSUFBSSxPQUFPLENBQUMsSUFBSSxDQUFDLDBDQUF3QyxJQUFJLENBQUMsV0FBVyxDQUFDLElBQUksNERBQXlELENBQUMsQ0FBQyxDQUFDLENBQUM7OztPQURuSDtJQUd4QyxzQkFDSSwyQkFBSTs7OztRQURSLGNBQ2EsT0FBTyxtQkFBWSxJQUFJLENBQUMsS0FBSyxDQUFDLE1BQU0sQ0FBQyxXQUFXLEVBQUEsQ0FBQyxDQUFDLENBQUM7Ozs7O1FBQ2hFLFVBQVMsSUFBSSxJQUFJLE9BQU8sQ0FBQyxJQUFJLENBQUMseUNBQXVDLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSw0REFBeUQsQ0FBQyxDQUFDLENBQUMsQ0FBQzs7O09BRHZGO0lBR2hFLHNCQUNJLDhCQUFPOzs7O1FBRFgsY0FDZ0IsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLE9BQU8sQ0FBQyxDQUFDLENBQUM7Ozs7O1FBQzVDLFVBQVksT0FBcUIsSUFBSSxPQUFPLENBQUMsSUFBSSxDQUFDLDRDQUEwQyxJQUFJLENBQUMsV0FBVyxDQUFDLElBQUksNERBQXlELENBQUMsQ0FBQyxDQUFDLENBQUM7OztPQURsSTtJQUc1QyxzQkFBSSwwQkFBRzs7OztRQUFQLGNBQVksT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLEdBQUcsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBRXBDLHNCQUFJLGtDQUFXOzs7O1FBQWYsY0FBb0IsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVcsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBRXBELHNCQUFJLHlCQUFFOzs7O1FBQU4sY0FBVyxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsZUFBZSxJQUFJLEVBQUUsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBRXJELHNCQUFJLGdDQUFTOzs7O1FBQWIsY0FBMkIsT0FBTyxJQUFJLENBQUMsT0FBTyxDQUFDLFNBQVMsQ0FBQyxJQUFJLENBQUMsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBRWpFLHNCQUFJLHlCQUFFOzs7O1FBQU4sY0FBbUIsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLEVBQUUsQ0FBQyxDQUFDLENBQUM7OztPQUFBO0lBRTFDLHNCQUFJLGdDQUFTOzs7O1FBQWIsY0FBa0IsT0FBTyxJQUFJLENBQUMsT0FBTyxDQUFDLFNBQVMsSUFBSSxFQUFFLENBQUMsQ0FBQyxDQUFDOzs7T0FBQTs7d0JBekJ2RCxLQUFLO3dCQUdMLEtBQUs7dUJBSUwsS0FBSzswQkFJTCxLQUFLOztJQWVSLGdCQUFDO0NBQUEsQUEzQkQsSUEyQkM7U0EzQnFCLFNBQVM7OztJQUM3QiwwQkFBa0I7O0lBQ2xCLG1DQUFtQjs7Ozs7O0FBOEJyQjs7Ozs7SUFBb0MsaUNBQVM7SUFDM0M7UUFBQSxZQUNFLGlCQUFPLFNBRVI7UUFEQyxPQUFPLENBQUMsSUFBSSxDQUFDLGlGQUFpRixDQUFDLENBQUM7O0lBQ2xHLENBQUM7SUFDSCxZQUFDO0FBQUQsQ0FBQyxBQUxELENBQW9DLFNBQVMsR0FLNUMiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRm9ybUdyb3VwIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICcuLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGRUeXBlPEYgZXh0ZW5kcyBGb3JtbHlGaWVsZENvbmZpZyA9IEZvcm1seUZpZWxkQ29uZmlnPiB7XG4gIEBJbnB1dCgpIGZpZWxkOiBGO1xuICBkZWZhdWx0T3B0aW9ucz86IEY7XG5cbiAgQElucHV0KClcbiAgZ2V0IG1vZGVsKCkgeyByZXR1cm4gdGhpcy5maWVsZC5tb2RlbDsgfVxuICBzZXQgbW9kZWwobTogYW55KSB7IGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBwYXNzaW5nICdtb2RlbCcgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIEBJbnB1dCgpXG4gIGdldCBmb3JtKCkgeyByZXR1cm4gPEZvcm1Hcm91cD4gdGhpcy5maWVsZC5wYXJlbnQuZm9ybUNvbnRyb2w7IH1cbiAgc2V0IGZvcm0oZm9ybSkgeyBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnZm9ybScgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIEBJbnB1dCgpXG4gIGdldCBvcHRpb25zKCkgeyByZXR1cm4gdGhpcy5maWVsZC5vcHRpb25zOyB9XG4gIHNldCBvcHRpb25zKG9wdGlvbnM6IEZbJ29wdGlvbnMnXSkgeyBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnb3B0aW9ucycgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIGdldCBrZXkoKSB7IHJldHVybiB0aGlzLmZpZWxkLmtleTsgfVxuXG4gIGdldCBmb3JtQ29udHJvbCgpIHsgcmV0dXJuIHRoaXMuZmllbGQuZm9ybUNvbnRyb2w7IH1cblxuICBnZXQgdG8oKSB7IHJldHVybiB0aGlzLmZpZWxkLnRlbXBsYXRlT3B0aW9ucyB8fCB7fTsgfVxuXG4gIGdldCBzaG93RXJyb3IoKTogYm9vbGVhbiB7IHJldHVybiB0aGlzLm9wdGlvbnMuc2hvd0Vycm9yKHRoaXMpOyB9XG5cbiAgZ2V0IGlkKCk6IHN0cmluZyB7IHJldHVybiB0aGlzLmZpZWxkLmlkOyB9XG5cbiAgZ2V0IGZvcm1TdGF0ZSgpIHsgcmV0dXJuIHRoaXMub3B0aW9ucy5mb3JtU3RhdGUgfHwge307IH1cbn1cblxuLyoqXG4gKiBAZGVwcmVjYXRlZCB1c2UgYEZpZWxkVHlwZWAgaW5zdGVhZFxuICovXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGQgZXh0ZW5kcyBGaWVsZFR5cGUge1xuICBjb25zdHJ1Y3RvcigpIHtcbiAgICBzdXBlcigpO1xuICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiAnRmllbGQnIGhhcyBiZWVuIHJlbmFtZWQgdG8gJ0ZpZWxkVHlwZScsIGV4dGVuZCAnRmllbGRUeXBlJyBpbnN0ZWFkLmApO1xuICB9XG59XG4iXX0=