/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, Input, ChangeDetectionStrategy } from '@angular/core';
import { FormlyConfig } from '../services/formly.config';
import { isObject } from '../utils';
import { isObservable, of } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
export class FormlyValidationMessage {
    /**
     * @param {?} formlyConfig
     */
    constructor(formlyConfig) {
        this.formlyConfig = formlyConfig;
    }
    /**
     * @return {?}
     */
    ngOnChanges() {
        this.errorMessage$ = this.field.formControl.statusChanges.pipe(startWith(null), switchMap((/**
         * @return {?}
         */
        () => isObservable(this.errorMessage)
            ? this.errorMessage
            : of(this.errorMessage))));
    }
    /**
     * @return {?}
     */
    get errorMessage() {
        /** @type {?} */
        const fieldForm = this.field.formControl;
        for (let error in fieldForm.errors) {
            if (fieldForm.errors.hasOwnProperty(error)) {
                /** @type {?} */
                let message = this.formlyConfig.getValidatorMessage(error);
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
    }
}
FormlyValidationMessage.decorators = [
    { type: Component, args: [{
                selector: 'formly-validation-message',
                template: `{{ errorMessage$ | async }}`,
                changeDetection: ChangeDetectionStrategy.OnPush
            }] }
];
/** @nocollapse */
FormlyValidationMessage.ctorParameters = () => [
    { type: FormlyConfig }
];
FormlyValidationMessage.propDecorators = {
    field: [{ type: Input }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LnZhbGlkYXRpb24tbWVzc2FnZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2Zvcm1seS52YWxpZGF0aW9uLW1lc3NhZ2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsS0FBSyxFQUFFLHVCQUF1QixFQUFhLE1BQU0sZUFBZSxDQUFDO0FBQ3JGLE9BQU8sRUFBRSxZQUFZLEVBQTJCLE1BQU0sMkJBQTJCLENBQUM7QUFFbEYsT0FBTyxFQUFFLFFBQVEsRUFBRSxNQUFNLFVBQVUsQ0FBQztBQUNwQyxPQUFPLEVBQWMsWUFBWSxFQUFFLEVBQUUsRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUNwRCxPQUFPLEVBQUUsU0FBUyxFQUFFLFNBQVMsRUFBRSxNQUFNLGdCQUFnQixDQUFDO0FBT3RELE1BQU0sT0FBTyx1QkFBdUI7Ozs7SUFJbEMsWUFBb0IsWUFBMEI7UUFBMUIsaUJBQVksR0FBWixZQUFZLENBQWM7SUFBRyxDQUFDOzs7O0lBRWxELFdBQVc7UUFDVCxJQUFJLENBQUMsYUFBYSxHQUFHLElBQUksQ0FBQyxLQUFLLENBQUMsV0FBVyxDQUFDLGFBQWEsQ0FBQyxJQUFJLENBQzVELFNBQVMsQ0FBQyxJQUFJLENBQUMsRUFDZixTQUFTOzs7UUFBQyxHQUFHLEVBQUUsQ0FBQyxZQUFZLENBQUMsSUFBSSxDQUFDLFlBQVksQ0FBQztZQUM3QyxDQUFDLENBQUMsSUFBSSxDQUFDLFlBQVk7WUFDbkIsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxJQUFJLENBQUMsWUFBWSxDQUFDLEVBQ3hCLENBQ0YsQ0FBQztJQUNKLENBQUM7Ozs7SUFFRCxJQUFJLFlBQVk7O2NBQ1IsU0FBUyxHQUFHLElBQUksQ0FBQyxLQUFLLENBQUMsV0FBVztRQUN4QyxLQUFLLElBQUksS0FBSyxJQUFJLFNBQVMsQ0FBQyxNQUFNLEVBQUU7WUFDbEMsSUFBSSxTQUFTLENBQUMsTUFBTSxDQUFDLGNBQWMsQ0FBQyxLQUFLLENBQUMsRUFBRTs7b0JBQ3RDLE9BQU8sR0FBdUMsSUFBSSxDQUFDLFlBQVksQ0FBQyxtQkFBbUIsQ0FBQyxLQUFLLENBQUM7Z0JBRTlGLElBQUksUUFBUSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLENBQUMsRUFBRTtvQkFDckMsSUFBSSxTQUFTLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxDQUFDLFNBQVMsRUFBRTt3QkFDckMsT0FBTztxQkFDUjtvQkFFRCxJQUFJLFNBQVMsQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLENBQUMsT0FBTyxFQUFFO3dCQUNuQyxPQUFPLEdBQUcsU0FBUyxDQUFDLE1BQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLENBQUM7cUJBQzNDO2lCQUNGO2dCQUVELElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxVQUFVLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxVQUFVLENBQUMsUUFBUSxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxDQUFDLFFBQVEsQ0FBQyxLQUFLLENBQUMsRUFBRTtvQkFDcEcsT0FBTyxHQUFHLElBQUksQ0FBQyxLQUFLLENBQUMsVUFBVSxDQUFDLFFBQVEsQ0FBQyxLQUFLLENBQUMsQ0FBQztpQkFDakQ7Z0JBRUQsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLEVBQUU7b0JBQ2pHLE9BQU8sR0FBRyxJQUFJLENBQUMsS0FBSyxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxPQUFPLENBQUM7aUJBQ2hEO2dCQUVELElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxlQUFlLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxlQUFlLENBQUMsS0FBSyxDQUFDLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxlQUFlLENBQUMsS0FBSyxDQUFDLENBQUMsT0FBTyxFQUFFO29CQUNoSCxPQUFPLEdBQUcsSUFBSSxDQUFDLEtBQUssQ0FBQyxlQUFlLENBQUMsS0FBSyxDQUFDLENBQUMsT0FBTyxDQUFDO2lCQUNyRDtnQkFFRCxJQUFJLE9BQU8sT0FBTyxLQUFLLFVBQVUsRUFBRTtvQkFDakMsT0FBTyxPQUFPLENBQUMsU0FBUyxDQUFDLE1BQU0sQ0FBQyxLQUFLLENBQUMsRUFBRSxJQUFJLENBQUMsS0FBSyxDQUFDLENBQUM7aUJBQ3JEO2dCQUVELE9BQU8sT0FBTyxDQUFDO2FBQ2hCO1NBQ0Y7SUFDSCxDQUFDOzs7WUF4REYsU0FBUyxTQUFDO2dCQUNULFFBQVEsRUFBRSwyQkFBMkI7Z0JBQ3JDLFFBQVEsRUFBRSw2QkFBNkI7Z0JBQ3ZDLGVBQWUsRUFBRSx1QkFBdUIsQ0FBQyxNQUFNO2FBQ2hEOzs7O1lBVlEsWUFBWTs7O29CQVlsQixLQUFLOzs7O0lBQU4sd0NBQWtDOztJQUNsQyxnREFBa0M7Ozs7O0lBRXRCLCtDQUFrQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgSW5wdXQsIENoYW5nZURldGVjdGlvblN0cmF0ZWd5LCBPbkNoYW5nZXMgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZywgVmFsaWRhdGlvbk1lc3NhZ2VPcHRpb24gfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnIH0gZnJvbSAnLi4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IGlzT2JqZWN0IH0gZnJvbSAnLi4vdXRpbHMnO1xuaW1wb3J0IHsgT2JzZXJ2YWJsZSwgaXNPYnNlcnZhYmxlLCBvZiB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgc3RhcnRXaXRoLCBzd2l0Y2hNYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS12YWxpZGF0aW9uLW1lc3NhZ2UnLFxuICB0ZW1wbGF0ZTogYHt7IGVycm9yTWVzc2FnZSQgfCBhc3luYyB9fWAsXG4gIGNoYW5nZURldGVjdGlvbjogQ2hhbmdlRGV0ZWN0aW9uU3RyYXRlZ3kuT25QdXNoLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlWYWxpZGF0aW9uTWVzc2FnZSBpbXBsZW1lbnRzIE9uQ2hhbmdlcyB7XG4gIEBJbnB1dCgpIGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZztcbiAgZXJyb3JNZXNzYWdlJDogT2JzZXJ2YWJsZTxzdHJpbmc+O1xuXG4gIGNvbnN0cnVjdG9yKHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcpIHt9XG5cbiAgbmdPbkNoYW5nZXMoKSB7XG4gICAgdGhpcy5lcnJvck1lc3NhZ2UkID0gdGhpcy5maWVsZC5mb3JtQ29udHJvbC5zdGF0dXNDaGFuZ2VzLnBpcGUoXG4gICAgICBzdGFydFdpdGgobnVsbCksXG4gICAgICBzd2l0Y2hNYXAoKCkgPT4gaXNPYnNlcnZhYmxlKHRoaXMuZXJyb3JNZXNzYWdlKVxuICAgICAgICA/IHRoaXMuZXJyb3JNZXNzYWdlXG4gICAgICAgIDogb2YodGhpcy5lcnJvck1lc3NhZ2UpLFxuICAgICAgKSxcbiAgICApO1xuICB9XG5cbiAgZ2V0IGVycm9yTWVzc2FnZSgpIHtcbiAgICBjb25zdCBmaWVsZEZvcm0gPSB0aGlzLmZpZWxkLmZvcm1Db250cm9sO1xuICAgIGZvciAobGV0IGVycm9yIGluIGZpZWxkRm9ybS5lcnJvcnMpIHtcbiAgICAgIGlmIChmaWVsZEZvcm0uZXJyb3JzLmhhc093blByb3BlcnR5KGVycm9yKSkge1xuICAgICAgICBsZXQgbWVzc2FnZTogVmFsaWRhdGlvbk1lc3NhZ2VPcHRpb25bJ21lc3NhZ2UnXSA9IHRoaXMuZm9ybWx5Q29uZmlnLmdldFZhbGlkYXRvck1lc3NhZ2UoZXJyb3IpO1xuXG4gICAgICAgIGlmIChpc09iamVjdChmaWVsZEZvcm0uZXJyb3JzW2Vycm9yXSkpIHtcbiAgICAgICAgICBpZiAoZmllbGRGb3JtLmVycm9yc1tlcnJvcl0uZXJyb3JQYXRoKSB7XG4gICAgICAgICAgICByZXR1cm47XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgaWYgKGZpZWxkRm9ybS5lcnJvcnNbZXJyb3JdLm1lc3NhZ2UpIHtcbiAgICAgICAgICAgIG1lc3NhZ2UgPSBmaWVsZEZvcm0uZXJyb3JzW2Vycm9yXS5tZXNzYWdlO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuXG4gICAgICAgIGlmICh0aGlzLmZpZWxkLnZhbGlkYXRpb24gJiYgdGhpcy5maWVsZC52YWxpZGF0aW9uLm1lc3NhZ2VzICYmIHRoaXMuZmllbGQudmFsaWRhdGlvbi5tZXNzYWdlc1tlcnJvcl0pIHtcbiAgICAgICAgICBtZXNzYWdlID0gdGhpcy5maWVsZC52YWxpZGF0aW9uLm1lc3NhZ2VzW2Vycm9yXTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmICh0aGlzLmZpZWxkLnZhbGlkYXRvcnMgJiYgdGhpcy5maWVsZC52YWxpZGF0b3JzW2Vycm9yXSAmJiB0aGlzLmZpZWxkLnZhbGlkYXRvcnNbZXJyb3JdLm1lc3NhZ2UpIHtcbiAgICAgICAgICBtZXNzYWdlID0gdGhpcy5maWVsZC52YWxpZGF0b3JzW2Vycm9yXS5tZXNzYWdlO1xuICAgICAgICB9XG5cbiAgICAgICAgaWYgKHRoaXMuZmllbGQuYXN5bmNWYWxpZGF0b3JzICYmIHRoaXMuZmllbGQuYXN5bmNWYWxpZGF0b3JzW2Vycm9yXSAmJiB0aGlzLmZpZWxkLmFzeW5jVmFsaWRhdG9yc1tlcnJvcl0ubWVzc2FnZSkge1xuICAgICAgICAgIG1lc3NhZ2UgPSB0aGlzLmZpZWxkLmFzeW5jVmFsaWRhdG9yc1tlcnJvcl0ubWVzc2FnZTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmICh0eXBlb2YgbWVzc2FnZSA9PT0gJ2Z1bmN0aW9uJykge1xuICAgICAgICAgIHJldHVybiBtZXNzYWdlKGZpZWxkRm9ybS5lcnJvcnNbZXJyb3JdLCB0aGlzLmZpZWxkKTtcbiAgICAgICAgfVxuXG4gICAgICAgIHJldHVybiBtZXNzYWdlO1xuICAgICAgfVxuICAgIH1cbiAgfVxufVxuIl19