/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChild, Renderer2 } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatCheckbox } from '@angular/material/checkbox';
export class FormlyFieldCheckbox extends FieldType {
    /**
     * @param {?} renderer
     */
    constructor(renderer) {
        super();
        this.renderer = renderer;
        this.defaultOptions = {
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
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        this.checkbox.focus();
        super.onContainerClick(event);
    }
    /**
     * @return {?}
     */
    ngAfterViewChecked() {
        if (this.required !== this._required && this.checkbox && this.checkbox._inputElement) {
            this._required = this.required;
            /** @type {?} */
            const inputElement = this.checkbox._inputElement.nativeElement;
            if (this.required) {
                this.renderer.setAttribute(inputElement, 'required', 'required');
            }
            else {
                this.renderer.removeAttribute(inputElement, 'required');
            }
        }
    }
}
FormlyFieldCheckbox.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-checkbox',
                template: `
    <mat-checkbox
      [formControl]="formControl"
      [id]="id"
      [formlyAttributes]="field"
      [tabindex]="to.tabindex"
      [indeterminate]="to.indeterminate && formControl.value === null"
      [color]="to.color"
      [labelPosition]="to.align || to.labelPosition">
      {{ to.label }}
      <span *ngIf="to.required && to.hideRequiredMarker !== true" class="mat-form-field-required-marker">*</span>
    </mat-checkbox>
  `
            }] }
];
/** @nocollapse */
FormlyFieldCheckbox.ctorParameters = () => [
    { type: Renderer2 }
];
FormlyFieldCheckbox.propDecorators = {
    checkbox: [{ type: ViewChild, args: [MatCheckbox,] }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hlY2tib3gudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL2NoZWNrYm94LyIsInNvdXJjZXMiOlsiY2hlY2tib3gudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFvQixNQUFNLGVBQWUsQ0FBQztBQUNsRixPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDNUQsT0FBTyxFQUFFLFdBQVcsRUFBRSxNQUFNLDRCQUE0QixDQUFDO0FBa0J6RCxNQUFNLE9BQU8sbUJBQW9CLFNBQVEsU0FBUzs7OztJQWNoRCxZQUFvQixRQUFtQjtRQUNyQyxLQUFLLEVBQUUsQ0FBQztRQURVLGFBQVEsR0FBUixRQUFRLENBQVc7UUFadkMsbUJBQWMsR0FBRztZQUNmLGVBQWUsRUFBRTtnQkFDZixrQkFBa0IsRUFBRSxJQUFJO2dCQUN4QixhQUFhLEVBQUUsSUFBSTtnQkFDbkIsVUFBVSxFQUFFLFFBQVE7Z0JBQ3BCLFNBQVMsRUFBRSxJQUFJO2dCQUNmLEtBQUssRUFBRSxPQUFPOztnQkFDZCxLQUFLLEVBQUUsUUFBUTthQUNoQjtTQUNGLENBQUM7SUFLRixDQUFDOzs7OztJQUVELGdCQUFnQixDQUFDLEtBQWlCO1FBQ2hDLElBQUksQ0FBQyxRQUFRLENBQUMsS0FBSyxFQUFFLENBQUM7UUFDdEIsS0FBSyxDQUFDLGdCQUFnQixDQUFDLEtBQUssQ0FBQyxDQUFDO0lBQ2hDLENBQUM7Ozs7SUFFRCxrQkFBa0I7UUFDaEIsSUFBSSxJQUFJLENBQUMsUUFBUSxLQUFLLElBQUksQ0FBQyxTQUFTLElBQUksSUFBSSxDQUFDLFFBQVEsSUFBSSxJQUFJLENBQUMsUUFBUSxDQUFDLGFBQWEsRUFBRTtZQUNwRixJQUFJLENBQUMsU0FBUyxHQUFHLElBQUksQ0FBQyxRQUFRLENBQUM7O2tCQUN6QixZQUFZLEdBQUcsSUFBSSxDQUFDLFFBQVEsQ0FBQyxhQUFhLENBQUMsYUFBYTtZQUM5RCxJQUFJLElBQUksQ0FBQyxRQUFRLEVBQUU7Z0JBQ2pCLElBQUksQ0FBQyxRQUFRLENBQUMsWUFBWSxDQUFDLFlBQVksRUFBRSxVQUFVLEVBQUUsVUFBVSxDQUFDLENBQUM7YUFDbEU7aUJBQU07Z0JBQ0wsSUFBSSxDQUFDLFFBQVEsQ0FBQyxlQUFlLENBQUMsWUFBWSxFQUFFLFVBQVUsQ0FBQyxDQUFDO2FBQ3pEO1NBQ0Y7SUFDSCxDQUFDOzs7WUFqREYsU0FBUyxTQUFDO2dCQUNULFFBQVEsRUFBRSwyQkFBMkI7Z0JBQ3JDLFFBQVEsRUFBRTs7Ozs7Ozs7Ozs7O0dBWVQ7YUFDRjs7OztZQW5COEIsU0FBUzs7O3VCQXFCckMsU0FBUyxTQUFDLFdBQVc7Ozs7SUFBdEIsdUNBQStDOztJQUMvQyw2Q0FTRTs7Ozs7SUFFRix3Q0FBNEI7Ozs7O0lBQ2hCLHVDQUEyQiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgVmlld0NoaWxkLCBSZW5kZXJlcjIsIEFmdGVyVmlld0NoZWNrZWQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0Q2hlY2tib3ggfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9jaGVja2JveCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtY2hlY2tib3gnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxtYXQtY2hlY2tib3hcbiAgICAgIFtmb3JtQ29udHJvbF09XCJmb3JtQ29udHJvbFwiXG4gICAgICBbaWRdPVwiaWRcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtpbmRldGVybWluYXRlXT1cInRvLmluZGV0ZXJtaW5hdGUgJiYgZm9ybUNvbnRyb2wudmFsdWUgPT09IG51bGxcIlxuICAgICAgW2NvbG9yXT1cInRvLmNvbG9yXCJcbiAgICAgIFtsYWJlbFBvc2l0aW9uXT1cInRvLmFsaWduIHx8IHRvLmxhYmVsUG9zaXRpb25cIj5cbiAgICAgIHt7IHRvLmxhYmVsIH19XG4gICAgICA8c3BhbiAqbmdJZj1cInRvLnJlcXVpcmVkICYmIHRvLmhpZGVSZXF1aXJlZE1hcmtlciAhPT0gdHJ1ZVwiIGNsYXNzPVwibWF0LWZvcm0tZmllbGQtcmVxdWlyZWQtbWFya2VyXCI+Kjwvc3Bhbj5cbiAgICA8L21hdC1jaGVja2JveD5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRDaGVja2JveCBleHRlbmRzIEZpZWxkVHlwZSBpbXBsZW1lbnRzIEFmdGVyVmlld0NoZWNrZWQge1xuICBAVmlld0NoaWxkKE1hdENoZWNrYm94KSBjaGVja2JveCE6IE1hdENoZWNrYm94O1xuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIGhpZGVGaWVsZFVuZGVybGluZTogdHJ1ZSxcbiAgICAgIGluZGV0ZXJtaW5hdGU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIGhpZGVMYWJlbDogdHJ1ZSxcbiAgICAgIGFsaWduOiAnc3RhcnQnLCAvLyBzdGFydCBvciBlbmRcbiAgICAgIGNvbG9yOiAnYWNjZW50JywgLy8gd29ya2Fyb3VuZCBmb3IgaHR0cHM6Ly9naXRodWIuY29tL2FuZ3VsYXIvY29tcG9uZW50cy9pc3N1ZXMvMTg0NjVcbiAgICB9LFxuICB9O1xuXG4gIHByaXZhdGUgX3JlcXVpcmVkITogYm9vbGVhbjtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSByZW5kZXJlcjogUmVuZGVyZXIyKSB7XG4gICAgc3VwZXIoKTtcbiAgfVxuXG4gIG9uQ29udGFpbmVyQ2xpY2soZXZlbnQ6IE1vdXNlRXZlbnQpOiB2b2lkIHtcbiAgICB0aGlzLmNoZWNrYm94LmZvY3VzKCk7XG4gICAgc3VwZXIub25Db250YWluZXJDbGljayhldmVudCk7XG4gIH1cblxuICBuZ0FmdGVyVmlld0NoZWNrZWQoKSB7XG4gICAgaWYgKHRoaXMucmVxdWlyZWQgIT09IHRoaXMuX3JlcXVpcmVkICYmIHRoaXMuY2hlY2tib3ggJiYgdGhpcy5jaGVja2JveC5faW5wdXRFbGVtZW50KSB7XG4gICAgICB0aGlzLl9yZXF1aXJlZCA9IHRoaXMucmVxdWlyZWQ7XG4gICAgICBjb25zdCBpbnB1dEVsZW1lbnQgPSB0aGlzLmNoZWNrYm94Ll9pbnB1dEVsZW1lbnQubmF0aXZlRWxlbWVudDtcbiAgICAgIGlmICh0aGlzLnJlcXVpcmVkKSB7XG4gICAgICAgIHRoaXMucmVuZGVyZXIuc2V0QXR0cmlidXRlKGlucHV0RWxlbWVudCwgJ3JlcXVpcmVkJywgJ3JlcXVpcmVkJyk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICB0aGlzLnJlbmRlcmVyLnJlbW92ZUF0dHJpYnV0ZShpbnB1dEVsZW1lbnQsICdyZXF1aXJlZCcpO1xuICAgICAgfVxuICAgIH1cbiAgfVxufVxuIl19