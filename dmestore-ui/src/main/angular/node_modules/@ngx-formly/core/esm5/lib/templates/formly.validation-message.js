/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, Input, ChangeDetectionStrategy } from '@angular/core';
import { FormlyConfig } from '../services/formly.config';
import { isObject } from '../utils';
import { isObservable, of } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
var FormlyValidationMessage = /** @class */ (function () {
    function FormlyValidationMessage(formlyConfig) {
        this.formlyConfig = formlyConfig;
    }
    /**
     * @return {?}
     */
    FormlyValidationMessage.prototype.ngOnChanges = /**
     * @return {?}
     */
    function () {
        var _this = this;
        this.errorMessage$ = this.field.formControl.statusChanges.pipe(startWith(null), switchMap((/**
         * @return {?}
         */
        function () { return isObservable(_this.errorMessage)
            ? _this.errorMessage
            : of(_this.errorMessage); })));
    };
    Object.defineProperty(FormlyValidationMessage.prototype, "errorMessage", {
        get: /**
         * @return {?}
         */
        function () {
            /** @type {?} */
            var fieldForm = this.field.formControl;
            for (var error in fieldForm.errors) {
                if (fieldForm.errors.hasOwnProperty(error)) {
                    /** @type {?} */
                    var message = this.formlyConfig.getValidatorMessage(error);
                    if (isObject(fieldForm.errors[error])) {
                        if (fieldForm.errors[error].errorPath) {
                            return;
                        }
                        if (fieldForm.errors[error].message) {
                            message = fieldForm.errors[error].message;
                        }
                    }
                    if (this.field.validation && this.field.validation.messages && this.field.validation.messages[error]) {
                        message = this.field.validation.messages[error];
                    }
                    if (this.field.validators && this.field.validators[error] && this.field.validators[error].message) {
                        message = this.field.validators[error].message;
                    }
                    if (this.field.asyncValidators && this.field.asyncValidators[error] && this.field.asyncValidators[error].message) {
                        message = this.field.asyncValidators[error].message;
                    }
                    if (typeof message === 'function') {
                        return message(fieldForm.errors[error], this.field);
                    }
                    return message;
                }
            }
        },
        enumerable: true,
        configurable: true
    });
    FormlyValidationMessage.decorators = [
        { type: Component, args: [{
                    selector: 'formly-validation-message',
                    template: "{{ errorMessage$ | async }}",
                    changeDetection: ChangeDetectionStrategy.OnPush
                }] }
    ];
    /** @nocollapse */
    FormlyValidationMessage.ctorParameters = function () { return [
        { type: FormlyConfig }
    ]; };
    FormlyValidationMessage.propDecorators = {
        field: [{ type: Input }]
    };
    return FormlyValidationMessage;
}());
export { FormlyValidationMessage };
if (false) {
    /** @type {?} */
    FormlyValidationMessage.prototype.field;
    /** @type {?} */
    FormlyValidationMessage.prototype.errorMessage$;
    /**
     * @type {?}
     * @private
     */
    FormlyValidationMessage.prototype.formlyConfig;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LnZhbGlkYXRpb24tbWVzc2FnZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2Zvcm1seS52YWxpZGF0aW9uLW1lc3NhZ2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsS0FBSyxFQUFFLHVCQUF1QixFQUFhLE1BQU0sZUFBZSxDQUFDO0FBQ3JGLE9BQU8sRUFBRSxZQUFZLEVBQTJCLE1BQU0sMkJBQTJCLENBQUM7QUFFbEYsT0FBTyxFQUFFLFFBQVEsRUFBRSxNQUFNLFVBQVUsQ0FBQztBQUNwQyxPQUFPLEVBQWMsWUFBWSxFQUFFLEVBQUUsRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUNwRCxPQUFPLEVBQUUsU0FBUyxFQUFFLFNBQVMsRUFBRSxNQUFNLGdCQUFnQixDQUFDO0FBRXREO0lBU0UsaUNBQW9CLFlBQTBCO1FBQTFCLGlCQUFZLEdBQVosWUFBWSxDQUFjO0lBQUcsQ0FBQzs7OztJQUVsRCw2Q0FBVzs7O0lBQVg7UUFBQSxpQkFRQztRQVBDLElBQUksQ0FBQyxhQUFhLEdBQUcsSUFBSSxDQUFDLEtBQUssQ0FBQyxXQUFXLENBQUMsYUFBYSxDQUFDLElBQUksQ0FDNUQsU0FBUyxDQUFDLElBQUksQ0FBQyxFQUNmLFNBQVM7OztRQUFDLGNBQU0sT0FBQSxZQUFZLENBQUMsS0FBSSxDQUFDLFlBQVksQ0FBQztZQUM3QyxDQUFDLENBQUMsS0FBSSxDQUFDLFlBQVk7WUFDbkIsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxLQUFJLENBQUMsWUFBWSxDQUFDLEVBRlQsQ0FFUyxFQUN4QixDQUNGLENBQUM7SUFDSixDQUFDO0lBRUQsc0JBQUksaURBQVk7Ozs7UUFBaEI7O2dCQUNRLFNBQVMsR0FBRyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVc7WUFDeEMsS0FBSyxJQUFJLEtBQUssSUFBSSxTQUFTLENBQUMsTUFBTSxFQUFFO2dCQUNsQyxJQUFJLFNBQVMsQ0FBQyxNQUFNLENBQUMsY0FBYyxDQUFDLEtBQUssQ0FBQyxFQUFFOzt3QkFDdEMsT0FBTyxHQUF1QyxJQUFJLENBQUMsWUFBWSxDQUFDLG1CQUFtQixDQUFDLEtBQUssQ0FBQztvQkFFOUYsSUFBSSxRQUFRLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQyxFQUFFO3dCQUNyQyxJQUFJLFNBQVMsQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLENBQUMsU0FBUyxFQUFFOzRCQUNyQyxPQUFPO3lCQUNSO3dCQUVELElBQUksU0FBUyxDQUFDLE1BQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLEVBQUU7NEJBQ25DLE9BQU8sR0FBRyxTQUFTLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxDQUFDLE9BQU8sQ0FBQzt5QkFDM0M7cUJBQ0Y7b0JBRUQsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsQ0FBQyxRQUFRLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxVQUFVLENBQUMsUUFBUSxDQUFDLEtBQUssQ0FBQyxFQUFFO3dCQUNwRyxPQUFPLEdBQUcsSUFBSSxDQUFDLEtBQUssQ0FBQyxVQUFVLENBQUMsUUFBUSxDQUFDLEtBQUssQ0FBQyxDQUFDO3FCQUNqRDtvQkFFRCxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxDQUFDLEtBQUssQ0FBQyxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxDQUFDLEtBQUssQ0FBQyxDQUFDLE9BQU8sRUFBRTt3QkFDakcsT0FBTyxHQUFHLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxDQUFDLEtBQUssQ0FBQyxDQUFDLE9BQU8sQ0FBQztxQkFDaEQ7b0JBRUQsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsQ0FBQyxLQUFLLENBQUMsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLEVBQUU7d0JBQ2hILE9BQU8sR0FBRyxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLENBQUM7cUJBQ3JEO29CQUVELElBQUksT0FBTyxPQUFPLEtBQUssVUFBVSxFQUFFO3dCQUNqQyxPQUFPLE9BQU8sQ0FBQyxTQUFTLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxFQUFFLElBQUksQ0FBQyxLQUFLLENBQUMsQ0FBQztxQkFDckQ7b0JBRUQsT0FBTyxPQUFPLENBQUM7aUJBQ2hCO2FBQ0Y7UUFDSCxDQUFDOzs7T0FBQTs7Z0JBeERGLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsMkJBQTJCO29CQUNyQyxRQUFRLEVBQUUsNkJBQTZCO29CQUN2QyxlQUFlLEVBQUUsdUJBQXVCLENBQUMsTUFBTTtpQkFDaEQ7Ozs7Z0JBVlEsWUFBWTs7O3dCQVlsQixLQUFLOztJQW1EUiw4QkFBQztDQUFBLEFBekRELElBeURDO1NBcERZLHVCQUF1Qjs7O0lBQ2xDLHdDQUFrQzs7SUFDbEMsZ0RBQWtDOzs7OztJQUV0QiwrQ0FBa0MiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQsIElucHV0LCBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneSwgT25DaGFuZ2VzIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGb3JtbHlDb25maWcsIFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uIH0gZnJvbSAnLi4vc2VydmljZXMvZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZyB9IGZyb20gJy4uL2NvbXBvbmVudHMvZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBpc09iamVjdCB9IGZyb20gJy4uL3V0aWxzJztcbmltcG9ydCB7IE9ic2VydmFibGUsIGlzT2JzZXJ2YWJsZSwgb2YgfSBmcm9tICdyeGpzJztcbmltcG9ydCB7IHN0YXJ0V2l0aCwgc3dpdGNoTWFwIH0gZnJvbSAncnhqcy9vcGVyYXRvcnMnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktdmFsaWRhdGlvbi1tZXNzYWdlJyxcbiAgdGVtcGxhdGU6IGB7eyBlcnJvck1lc3NhZ2UkIHwgYXN5bmMgfX1gLFxuICBjaGFuZ2VEZXRlY3Rpb246IENoYW5nZURldGVjdGlvblN0cmF0ZWd5Lk9uUHVzaCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5VmFsaWRhdGlvbk1lc3NhZ2UgaW1wbGVtZW50cyBPbkNoYW5nZXMge1xuICBASW5wdXQoKSBmaWVsZDogRm9ybWx5RmllbGRDb25maWc7XG4gIGVycm9yTWVzc2FnZSQ6IE9ic2VydmFibGU8c3RyaW5nPjtcblxuICBjb25zdHJ1Y3Rvcihwcml2YXRlIGZvcm1seUNvbmZpZzogRm9ybWx5Q29uZmlnKSB7fVxuXG4gIG5nT25DaGFuZ2VzKCkge1xuICAgIHRoaXMuZXJyb3JNZXNzYWdlJCA9IHRoaXMuZmllbGQuZm9ybUNvbnRyb2wuc3RhdHVzQ2hhbmdlcy5waXBlKFxuICAgICAgc3RhcnRXaXRoKG51bGwpLFxuICAgICAgc3dpdGNoTWFwKCgpID0+IGlzT2JzZXJ2YWJsZSh0aGlzLmVycm9yTWVzc2FnZSlcbiAgICAgICAgPyB0aGlzLmVycm9yTWVzc2FnZVxuICAgICAgICA6IG9mKHRoaXMuZXJyb3JNZXNzYWdlKSxcbiAgICAgICksXG4gICAgKTtcbiAgfVxuXG4gIGdldCBlcnJvck1lc3NhZ2UoKSB7XG4gICAgY29uc3QgZmllbGRGb3JtID0gdGhpcy5maWVsZC5mb3JtQ29udHJvbDtcbiAgICBmb3IgKGxldCBlcnJvciBpbiBmaWVsZEZvcm0uZXJyb3JzKSB7XG4gICAgICBpZiAoZmllbGRGb3JtLmVycm9ycy5oYXNPd25Qcm9wZXJ0eShlcnJvcikpIHtcbiAgICAgICAgbGV0IG1lc3NhZ2U6IFZhbGlkYXRpb25NZXNzYWdlT3B0aW9uWydtZXNzYWdlJ10gPSB0aGlzLmZvcm1seUNvbmZpZy5nZXRWYWxpZGF0b3JNZXNzYWdlKGVycm9yKTtcblxuICAgICAgICBpZiAoaXNPYmplY3QoZmllbGRGb3JtLmVycm9yc1tlcnJvcl0pKSB7XG4gICAgICAgICAgaWYgKGZpZWxkRm9ybS5lcnJvcnNbZXJyb3JdLmVycm9yUGF0aCkge1xuICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgIH1cblxuICAgICAgICAgIGlmIChmaWVsZEZvcm0uZXJyb3JzW2Vycm9yXS5tZXNzYWdlKSB7XG4gICAgICAgICAgICBtZXNzYWdlID0gZmllbGRGb3JtLmVycm9yc1tlcnJvcl0ubWVzc2FnZTtcbiAgICAgICAgICB9XG4gICAgICAgIH1cblxuICAgICAgICBpZiAodGhpcy5maWVsZC52YWxpZGF0aW9uICYmIHRoaXMuZmllbGQudmFsaWRhdGlvbi5tZXNzYWdlcyAmJiB0aGlzLmZpZWxkLnZhbGlkYXRpb24ubWVzc2FnZXNbZXJyb3JdKSB7XG4gICAgICAgICAgbWVzc2FnZSA9IHRoaXMuZmllbGQudmFsaWRhdGlvbi5tZXNzYWdlc1tlcnJvcl07XG4gICAgICAgIH1cblxuICAgICAgICBpZiAodGhpcy5maWVsZC52YWxpZGF0b3JzICYmIHRoaXMuZmllbGQudmFsaWRhdG9yc1tlcnJvcl0gJiYgdGhpcy5maWVsZC52YWxpZGF0b3JzW2Vycm9yXS5tZXNzYWdlKSB7XG4gICAgICAgICAgbWVzc2FnZSA9IHRoaXMuZmllbGQudmFsaWRhdG9yc1tlcnJvcl0ubWVzc2FnZTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmICh0aGlzLmZpZWxkLmFzeW5jVmFsaWRhdG9ycyAmJiB0aGlzLmZpZWxkLmFzeW5jVmFsaWRhdG9yc1tlcnJvcl0gJiYgdGhpcy5maWVsZC5hc3luY1ZhbGlkYXRvcnNbZXJyb3JdLm1lc3NhZ2UpIHtcbiAgICAgICAgICBtZXNzYWdlID0gdGhpcy5maWVsZC5hc3luY1ZhbGlkYXRvcnNbZXJyb3JdLm1lc3NhZ2U7XG4gICAgICAgIH1cblxuICAgICAgICBpZiAodHlwZW9mIG1lc3NhZ2UgPT09ICdmdW5jdGlvbicpIHtcbiAgICAgICAgICByZXR1cm4gbWVzc2FnZShmaWVsZEZvcm0uZXJyb3JzW2Vycm9yXSwgdGhpcy5maWVsZCk7XG4gICAgICAgIH1cblxuICAgICAgICByZXR1cm4gbWVzc2FnZTtcbiAgICAgIH1cbiAgICB9XG4gIH1cbn1cbiJdfQ==