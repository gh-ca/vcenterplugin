/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { MatInput } from '@angular/material/input';
import { FieldType } from '@ngx-formly/material/form-field';
var FormlyFieldInput = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldInput, _super);
    function FormlyFieldInput() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    Object.defineProperty(FormlyFieldInput.prototype, "type", {
        get: /**
         * @return {?}
         */
        function () {
            return this.to.type || 'text';
        },
        enumerable: true,
        configurable: true
    });
    FormlyFieldInput.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-input',
                    template: "\n    <input *ngIf=\"type !== 'number'; else numberTmp\"\n      matInput\n      [id]=\"id\"\n      [type]=\"type || 'text'\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [placeholder]=\"to.placeholder\">\n    <ng-template #numberTmp>\n      <input matInput\n             [id]=\"id\"\n             type=\"number\"\n             [readonly]=\"to.readonly\"\n             [required]=\"to.required\"\n             [errorStateMatcher]=\"errorStateMatcher\"\n             [formControl]=\"formControl\"\n             [formlyAttributes]=\"field\"\n             [tabindex]=\"to.tabindex\"\n             [placeholder]=\"to.placeholder\">\n    </ng-template>\n  "
                }] }
    ];
    FormlyFieldInput.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: false })),] }]
    };
    return FormlyFieldInput;
}(FieldType));
export { FormlyFieldInput };
if (false) {
    /** @type {?} */
    FormlyFieldInput.prototype.formFieldControl;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaW5wdXQudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL2lucHV0LyIsInNvdXJjZXMiOlsiaW5wdXQudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQVUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzdELE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSx5QkFBeUIsQ0FBQztBQUNuRCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFFNUQ7SUE0QnNDLDRDQUFTO0lBNUIvQzs7SUFrQ0EsQ0FBQztJQUhDLHNCQUFJLGtDQUFJOzs7O1FBQVI7WUFDRSxPQUFPLElBQUksQ0FBQyxFQUFFLENBQUMsSUFBSSxJQUFJLE1BQU0sQ0FBQztRQUNoQyxDQUFDOzs7T0FBQTs7Z0JBakNGLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsd0JBQXdCO29CQUNsQyxRQUFRLEVBQUUsczBCQXdCVDtpQkFDRjs7O21DQUVFLFNBQVMsU0FBQyxRQUFRLEVBQUUsbUJBQU0sRUFBRSxNQUFNLEVBQUUsS0FBSyxFQUFFLEVBQUE7O0lBSzlDLHVCQUFDO0NBQUEsQUFsQ0QsQ0E0QnNDLFNBQVMsR0FNOUM7U0FOWSxnQkFBZ0I7OztJQUMzQiw0Q0FBMEUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQsIE9uSW5pdCwgVmlld0NoaWxkIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBNYXRJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2lucHV0JztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LWlucHV0JyxcbiAgdGVtcGxhdGU6IGBcbiAgICA8aW5wdXQgKm5nSWY9XCJ0eXBlICE9PSAnbnVtYmVyJzsgZWxzZSBudW1iZXJUbXBcIlxuICAgICAgbWF0SW5wdXRcbiAgICAgIFtpZF09XCJpZFwiXG4gICAgICBbdHlwZV09XCJ0eXBlIHx8ICd0ZXh0J1wiXG4gICAgICBbcmVhZG9ubHldPVwidG8ucmVhZG9ubHlcIlxuICAgICAgW3JlcXVpcmVkXT1cInRvLnJlcXVpcmVkXCJcbiAgICAgIFtlcnJvclN0YXRlTWF0Y2hlcl09XCJlcnJvclN0YXRlTWF0Y2hlclwiXG4gICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtwbGFjZWhvbGRlcl09XCJ0by5wbGFjZWhvbGRlclwiPlxuICAgIDxuZy10ZW1wbGF0ZSAjbnVtYmVyVG1wPlxuICAgICAgPGlucHV0IG1hdElucHV0XG4gICAgICAgICAgICAgW2lkXT1cImlkXCJcbiAgICAgICAgICAgICB0eXBlPVwibnVtYmVyXCJcbiAgICAgICAgICAgICBbcmVhZG9ubHldPVwidG8ucmVhZG9ubHlcIlxuICAgICAgICAgICAgIFtyZXF1aXJlZF09XCJ0by5yZXF1aXJlZFwiXG4gICAgICAgICAgICAgW2Vycm9yU3RhdGVNYXRjaGVyXT1cImVycm9yU3RhdGVNYXRjaGVyXCJcbiAgICAgICAgICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgICAgICAgIFtmb3JtbHlBdHRyaWJ1dGVzXT1cImZpZWxkXCJcbiAgICAgICAgICAgICBbdGFiaW5kZXhdPVwidG8udGFiaW5kZXhcIlxuICAgICAgICAgICAgIFtwbGFjZWhvbGRlcl09XCJ0by5wbGFjZWhvbGRlclwiPlxuICAgIDwvbmctdGVtcGxhdGU+XG4gIGAsXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seUZpZWxkSW5wdXQgZXh0ZW5kcyBGaWVsZFR5cGUgaW1wbGVtZW50cyBPbkluaXQge1xuICBAVmlld0NoaWxkKE1hdElucHV0LCA8YW55PiB7IHN0YXRpYzogZmFsc2UgfSkgZm9ybUZpZWxkQ29udHJvbCE6IE1hdElucHV0O1xuXG4gIGdldCB0eXBlKCkge1xuICAgIHJldHVybiB0aGlzLnRvLnR5cGUgfHwgJ3RleHQnO1xuICB9XG59XG4iXX0=