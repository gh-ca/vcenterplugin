/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild, Renderer2 } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatCheckbox } from '@angular/material/checkbox';
var FormlyFieldCheckbox = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldCheckbox, _super);
    function FormlyFieldCheckbox(renderer) {
        var _this = _super.call(this) || this;
        _this.renderer = renderer;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                indeterminate: true,
                floatLabel: 'always',
                hideLabel: true,
                align: 'start',
                // start or end
                color: 'accent',
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyFieldCheckbox.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.checkbox.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    /**
     * @return {?}
     */
    FormlyFieldCheckbox.prototype.ngAfterViewChecked = /**
     * @return {?}
     */
    function () {
        if (this.required !== this._required && this.checkbox && this.checkbox._inputElement) {
            this._required = this.required;
            /** @type {?} */
            var inputElement = this.checkbox._inputElement.nativeElement;
            if (this.required) {
                this.renderer.setAttribute(inputElement, 'required', 'required');
            }
            else {
                this.renderer.removeAttribute(inputElement, 'required');
            }
        }
    };
    FormlyFieldCheckbox.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-checkbox',
                    template: "\n    <mat-checkbox\n      [formControl]=\"formControl\"\n      [id]=\"id\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [indeterminate]=\"to.indeterminate && formControl.value === null\"\n      [color]=\"to.color\"\n      [labelPosition]=\"to.align || to.labelPosition\">\n      {{ to.label }}\n      <span *ngIf=\"to.required && to.hideRequiredMarker !== true\" class=\"mat-form-field-required-marker\">*</span>\n    </mat-checkbox>\n  "
                }] }
    ];
    /** @nocollapse */
    FormlyFieldCheckbox.ctorParameters = function () { return [
        { type: Renderer2 }
    ]; };
    FormlyFieldCheckbox.propDecorators = {
        checkbox: [{ type: ViewChild, args: [MatCheckbox,] }]
    };
    return FormlyFieldCheckbox;
}(FieldType));
export { FormlyFieldCheckbox };
if (false) {
    /** @type {?} */
    FormlyFieldCheckbox.prototype.checkbox;
    /** @type {?} */
    FormlyFieldCheckbox.prototype.defaultOptions;
    /**
     * @type {?}
     * @private
     */
    FormlyFieldCheckbox.prototype._required;
    /**
     * @type {?}
     * @private
     */
    FormlyFieldCheckbox.prototype.renderer;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hlY2tib3gudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL2NoZWNrYm94LyIsInNvdXJjZXMiOlsiY2hlY2tib3gudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFFLFNBQVMsRUFBb0IsTUFBTSxlQUFlLENBQUM7QUFDbEYsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBQzVELE9BQU8sRUFBRSxXQUFXLEVBQUUsTUFBTSw0QkFBNEIsQ0FBQztBQUV6RDtJQWdCeUMsK0NBQVM7SUFjaEQsNkJBQW9CLFFBQW1CO1FBQXZDLFlBQ0UsaUJBQU8sU0FDUjtRQUZtQixjQUFRLEdBQVIsUUFBUSxDQUFXO1FBWnZDLG9CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2Ysa0JBQWtCLEVBQUUsSUFBSTtnQkFDeEIsYUFBYSxFQUFFLElBQUk7Z0JBQ25CLFVBQVUsRUFBRSxRQUFRO2dCQUNwQixTQUFTLEVBQUUsSUFBSTtnQkFDZixLQUFLLEVBQUUsT0FBTzs7Z0JBQ2QsS0FBSyxFQUFFLFFBQVE7YUFDaEI7U0FDRixDQUFDOztJQUtGLENBQUM7Ozs7O0lBRUQsOENBQWdCOzs7O0lBQWhCLFVBQWlCLEtBQWlCO1FBQ2hDLElBQUksQ0FBQyxRQUFRLENBQUMsS0FBSyxFQUFFLENBQUM7UUFDdEIsaUJBQU0sZ0JBQWdCLFlBQUMsS0FBSyxDQUFDLENBQUM7SUFDaEMsQ0FBQzs7OztJQUVELGdEQUFrQjs7O0lBQWxCO1FBQ0UsSUFBSSxJQUFJLENBQUMsUUFBUSxLQUFLLElBQUksQ0FBQyxTQUFTLElBQUksSUFBSSxDQUFDLFFBQVEsSUFBSSxJQUFJLENBQUMsUUFBUSxDQUFDLGFBQWEsRUFBRTtZQUNwRixJQUFJLENBQUMsU0FBUyxHQUFHLElBQUksQ0FBQyxRQUFRLENBQUM7O2dCQUN6QixZQUFZLEdBQUcsSUFBSSxDQUFDLFFBQVEsQ0FBQyxhQUFhLENBQUMsYUFBYTtZQUM5RCxJQUFJLElBQUksQ0FBQyxRQUFRLEVBQUU7Z0JBQ2pCLElBQUksQ0FBQyxRQUFRLENBQUMsWUFBWSxDQUFDLFlBQVksRUFBRSxVQUFVLEVBQUUsVUFBVSxDQUFDLENBQUM7YUFDbEU7aUJBQU07Z0JBQ0wsSUFBSSxDQUFDLFFBQVEsQ0FBQyxlQUFlLENBQUMsWUFBWSxFQUFFLFVBQVUsQ0FBQyxDQUFDO2FBQ3pEO1NBQ0Y7SUFDSCxDQUFDOztnQkFqREYsU0FBUyxTQUFDO29CQUNULFFBQVEsRUFBRSwyQkFBMkI7b0JBQ3JDLFFBQVEsRUFBRSx3ZEFZVDtpQkFDRjs7OztnQkFuQjhCLFNBQVM7OzsyQkFxQnJDLFNBQVMsU0FBQyxXQUFXOztJQWlDeEIsMEJBQUM7Q0FBQSxBQWxERCxDQWdCeUMsU0FBUyxHQWtDakQ7U0FsQ1ksbUJBQW1COzs7SUFDOUIsdUNBQStDOztJQUMvQyw2Q0FTRTs7Ozs7SUFFRix3Q0FBNEI7Ozs7O0lBQ2hCLHVDQUEyQiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgVmlld0NoaWxkLCBSZW5kZXJlcjIsIEFmdGVyVmlld0NoZWNrZWQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0Q2hlY2tib3ggfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9jaGVja2JveCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtY2hlY2tib3gnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxtYXQtY2hlY2tib3hcbiAgICAgIFtmb3JtQ29udHJvbF09XCJmb3JtQ29udHJvbFwiXG4gICAgICBbaWRdPVwiaWRcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtpbmRldGVybWluYXRlXT1cInRvLmluZGV0ZXJtaW5hdGUgJiYgZm9ybUNvbnRyb2wudmFsdWUgPT09IG51bGxcIlxuICAgICAgW2NvbG9yXT1cInRvLmNvbG9yXCJcbiAgICAgIFtsYWJlbFBvc2l0aW9uXT1cInRvLmFsaWduIHx8IHRvLmxhYmVsUG9zaXRpb25cIj5cbiAgICAgIHt7IHRvLmxhYmVsIH19XG4gICAgICA8c3BhbiAqbmdJZj1cInRvLnJlcXVpcmVkICYmIHRvLmhpZGVSZXF1aXJlZE1hcmtlciAhPT0gdHJ1ZVwiIGNsYXNzPVwibWF0LWZvcm0tZmllbGQtcmVxdWlyZWQtbWFya2VyXCI+Kjwvc3Bhbj5cbiAgICA8L21hdC1jaGVja2JveD5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRDaGVja2JveCBleHRlbmRzIEZpZWxkVHlwZSBpbXBsZW1lbnRzIEFmdGVyVmlld0NoZWNrZWQge1xuICBAVmlld0NoaWxkKE1hdENoZWNrYm94KSBjaGVja2JveCE6IE1hdENoZWNrYm94O1xuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIGhpZGVGaWVsZFVuZGVybGluZTogdHJ1ZSxcbiAgICAgIGluZGV0ZXJtaW5hdGU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIGhpZGVMYWJlbDogdHJ1ZSxcbiAgICAgIGFsaWduOiAnc3RhcnQnLCAvLyBzdGFydCBvciBlbmRcbiAgICAgIGNvbG9yOiAnYWNjZW50JywgLy8gd29ya2Fyb3VuZCBmb3IgaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvY29tcG9uZW50cy9pc3N1ZXMvMTg0NjVcbiAgICB9LFxuICB9O1xuXG4gIHByaXZhdGUgX3JlcXVpcmVkITogYm9vbGVhbjtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSByZW5kZXJlcjogUmVuZGVyZXIyKSB7XG4gICAgc3VwZXIoKTtcbiAgfVxuXG4gIG9uQ29udGFpbmVyQ2xpY2soZXZlbnQ6IE1vdXNlRXZlbnQpOiB2b2lkIHtcbiAgICB0aGlzLmNoZWNrYm94LmZvY3VzKCk7XG4gICAgc3VwZXIub25Db250YWluZXJDbGljayhldmVudCk7XG4gIH1cblxuICBuZ0FmdGVyVmlld0NoZWNrZWQoKSB7XG4gICAgaWYgKHRoaXMucmVxdWlyZWQgIT09IHRoaXMuX3JlcXVpcmVkICYmIHRoaXMuY2hlY2tib3ggJiYgdGhpcy5jaGVja2JveC5faW5wdXRFbGVtZW50KSB7XG4gICAgICB0aGlzLl9yZXF1aXJlZCA9IHRoaXMucmVxdWlyZWQ7XG4gICAgICBjb25zdCBpbnB1dEVsZW1lbnQgPSB0aGlzLmNoZWNrYm94Ll9pbnB1dEVsZW1lbnQubmF0aXZlRWxlbWVudDtcbiAgICAgIGlmICh0aGlzLnJlcXVpcmVkKSB7XG4gICAgICAgIHRoaXMucmVuZGVyZXIuc2V0QXR0cmlidXRlKGlucHV0RWxlbWVudCwgJ3JlcXVpcmVkJywgJ3JlcXVpcmVkJyk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICB0aGlzLnJlbmRlcmVyLnJlbW92ZUF0dHJpYnV0ZShpbnB1dEVsZW1lbnQsICdyZXF1aXJlZCcpO1xuICAgICAgfVxuICAgIH1cbiAgfVxufVxuIl19