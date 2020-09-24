/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChildren, QueryList } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatCheckbox } from '@angular/material/checkbox';
export class FormlyFieldMultiCheckbox extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                color: 'accent',
            },
        };
    }
    /**
     * @param {?} value
     * @param {?} checked
     * @return {?}
     */
    onChange(value, checked) {
        if (this.to.type === 'array') {
            this.formControl.patchValue(checked
                ? [...(this.formControl.value || []), value]
                : [...(this.formControl.value || [])].filter((/**
                 * @param {?} o
                 * @return {?}
                 */
                o => o !== value)));
        }
        else {
            this.formControl.patchValue(Object.assign({}, this.formControl.value, { [value]: checked }));
        }
        this.formControl.markAsTouched();
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        if (this.checkboxes.length) {
            this.checkboxes.first.focus();
        }
        super.onContainerClick(event);
    }
    /**
     * @param {?} option
     * @return {?}
     */
    isChecked(option) {
        /** @type {?} */
        const value = this.formControl.value;
        return value && (this.to.type === 'array'
            ? (value.indexOf(option.value) !== -1)
            : value[option.value]);
    }
}
FormlyFieldMultiCheckbox.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-multicheckbox',
                template: `
    <ng-container *ngFor="let option of to.options | formlySelectOptions:field | async; let i = index;">
      <mat-checkbox
        [id]="id + '_' + i"
        [formlyAttributes]="field"
        [tabindex]="to.tabindex"
        [color]="to.color"
        [labelPosition]="to.labelPosition"
        [checked]="isChecked(option)"
        [disabled]="formControl.disabled"
        (change)="onChange(option.value, $event.checked)">
          {{ option.label }}
      </mat-checkbox>
    </ng-container>
  `
            }] }
];
FormlyFieldMultiCheckbox.propDecorators = {
    checkboxes: [{ type: ViewChildren, args: [MatCheckbox,] }]
};
if (false) {
    /** @type {?} */
    FormlyFieldMultiCheckbox.prototype.checkboxes;
    /** @type {?} */
    FormlyFieldMultiCheckbox.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibXVsdGljaGVja2JveC50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvbXVsdGljaGVja2JveC8iLCJzb3VyY2VzIjpbIm11bHRpY2hlY2tib3gudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxZQUFZLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ25FLE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUM1RCxPQUFPLEVBQUUsV0FBVyxFQUFFLE1BQU0sNEJBQTRCLENBQUM7QUFvQnpELE1BQU0sT0FBTyx3QkFBeUIsU0FBUSxTQUFTO0lBbEJ2RDs7UUFxQkUsbUJBQWMsR0FBRztZQUNmLGVBQWUsRUFBRTtnQkFDZixrQkFBa0IsRUFBRSxJQUFJO2dCQUN4QixVQUFVLEVBQUUsUUFBUTtnQkFDcEIsT0FBTyxFQUFFLEVBQUU7Z0JBQ1gsS0FBSyxFQUFFLFFBQVE7YUFDaEI7U0FDRixDQUFDO0lBOEJKLENBQUM7Ozs7OztJQTVCQyxRQUFRLENBQUMsS0FBVSxFQUFFLE9BQWdCO1FBQ25DLElBQUksSUFBSSxDQUFDLEVBQUUsQ0FBQyxJQUFJLEtBQUssT0FBTyxFQUFFO1lBQzVCLElBQUksQ0FBQyxXQUFXLENBQUMsVUFBVSxDQUFDLE9BQU87Z0JBQ2pDLENBQUMsQ0FBQyxDQUFDLEdBQUcsQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssSUFBSSxFQUFFLENBQUMsRUFBRSxLQUFLLENBQUM7Z0JBQzVDLENBQUMsQ0FBQyxDQUFDLEdBQUcsQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssSUFBSSxFQUFFLENBQUMsQ0FBQyxDQUFDLE1BQU07Ozs7Z0JBQUMsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxDQUFDLEtBQUssS0FBSyxFQUFDLENBQy9ELENBQUM7U0FDSDthQUFNO1lBQ0wsSUFBSSxDQUFDLFdBQVcsQ0FBQyxVQUFVLG1CQUFNLElBQUksQ0FBQyxXQUFXLENBQUMsS0FBSyxJQUFFLENBQUMsS0FBSyxDQUFDLEVBQUUsT0FBTyxJQUFHLENBQUM7U0FDOUU7UUFDRCxJQUFJLENBQUMsV0FBVyxDQUFDLGFBQWEsRUFBRSxDQUFDO0lBQ25DLENBQUM7Ozs7O0lBRUQsZ0JBQWdCLENBQUMsS0FBaUI7UUFDaEMsSUFBSSxJQUFJLENBQUMsVUFBVSxDQUFDLE1BQU0sRUFBRTtZQUMxQixJQUFJLENBQUMsVUFBVSxDQUFDLEtBQUssQ0FBQyxLQUFLLEVBQUUsQ0FBQztTQUMvQjtRQUNELEtBQUssQ0FBQyxnQkFBZ0IsQ0FBQyxLQUFLLENBQUMsQ0FBQztJQUNoQyxDQUFDOzs7OztJQUVELFNBQVMsQ0FBQyxNQUFXOztjQUNiLEtBQUssR0FBRyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUs7UUFFcEMsT0FBTyxLQUFLLElBQUksQ0FDZCxJQUFJLENBQUMsRUFBRSxDQUFDLElBQUksS0FBSyxPQUFPO1lBQ3RCLENBQUMsQ0FBQyxDQUFDLEtBQUssQ0FBQyxPQUFPLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDO1lBQ3RDLENBQUMsQ0FBQyxLQUFLLENBQUMsTUFBTSxDQUFDLEtBQUssQ0FBQyxDQUN4QixDQUFDO0lBQ0osQ0FBQzs7O1lBekRGLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUsZ0NBQWdDO2dCQUMxQyxRQUFRLEVBQUU7Ozs7Ozs7Ozs7Ozs7O0dBY1Q7YUFDRjs7O3lCQUVFLFlBQVksU0FBQyxXQUFXOzs7O0lBQXpCLDhDQUErRDs7SUFFL0Qsa0RBT0UiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQsIFZpZXdDaGlsZHJlbiwgUXVlcnlMaXN0IH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICdAbmd4LWZvcm1seS9tYXRlcmlhbC9mb3JtLWZpZWxkJztcbmltcG9ydCB7IE1hdENoZWNrYm94IH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvY2hlY2tib3gnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LW11bHRpY2hlY2tib3gnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxuZy1jb250YWluZXIgKm5nRm9yPVwibGV0IG9wdGlvbiBvZiB0by5vcHRpb25zIHwgZm9ybWx5U2VsZWN0T3B0aW9uczpmaWVsZCB8IGFzeW5jOyBsZXQgaSA9IGluZGV4O1wiPlxuICAgICAgPG1hdC1jaGVja2JveFxuICAgICAgICBbaWRdPVwiaWQgKyAnXycgKyBpXCJcbiAgICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgICBbdGFiaW5kZXhdPVwidG8udGFiaW5kZXhcIlxuICAgICAgICBbY29sb3JdPVwidG8uY29sb3JcIlxuICAgICAgICBbbGFiZWxQb3NpdGlvbl09XCJ0by5sYWJlbFBvc2l0aW9uXCJcbiAgICAgICAgW2NoZWNrZWRdPVwiaXNDaGVja2VkKG9wdGlvbilcIlxuICAgICAgICBbZGlzYWJsZWRdPVwiZm9ybUNvbnRyb2wuZGlzYWJsZWRcIlxuICAgICAgICAoY2hhbmdlKT1cIm9uQ2hhbmdlKG9wdGlvbi52YWx1ZSwgJGV2ZW50LmNoZWNrZWQpXCI+XG4gICAgICAgICAge3sgb3B0aW9uLmxhYmVsIH19XG4gICAgICA8L21hdC1jaGVja2JveD5cbiAgICA8L25nLWNvbnRhaW5lcj5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRNdWx0aUNoZWNrYm94IGV4dGVuZHMgRmllbGRUeXBlIHtcbiAgQFZpZXdDaGlsZHJlbihNYXRDaGVja2JveCkgY2hlY2tib3hlcyE6IFF1ZXJ5TGlzdDxNYXRDaGVja2JveD47XG5cbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBoaWRlRmllbGRVbmRlcmxpbmU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIG9wdGlvbnM6IFtdLFxuICAgICAgY29sb3I6ICdhY2NlbnQnLCAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9jb21wb25lbnRzL2lzc3Vlcy8xODQ2NVxuICAgIH0sXG4gIH07XG5cbiAgb25DaGFuZ2UodmFsdWU6IGFueSwgY2hlY2tlZDogYm9vbGVhbikge1xuICAgIGlmICh0aGlzLnRvLnR5cGUgPT09ICdhcnJheScpIHtcbiAgICAgIHRoaXMuZm9ybUNvbnRyb2wucGF0Y2hWYWx1ZShjaGVja2VkXG4gICAgICAgID8gWy4uLih0aGlzLmZvcm1Db250cm9sLnZhbHVlIHx8IFtdKSwgdmFsdWVdXG4gICAgICAgIDogWy4uLih0aGlzLmZvcm1Db250cm9sLnZhbHVlIHx8IFtdKV0uZmlsdGVyKG8gPT4gbyAhPT0gdmFsdWUpLFxuICAgICAgKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdGhpcy5mb3JtQ29udHJvbC5wYXRjaFZhbHVlKHsgLi4udGhpcy5mb3JtQ29udHJvbC52YWx1ZSwgW3ZhbHVlXTogY2hlY2tlZCB9KTtcbiAgICB9XG4gICAgdGhpcy5mb3JtQ29udHJvbC5tYXJrQXNUb3VjaGVkKCk7XG4gIH1cblxuICBvbkNvbnRhaW5lckNsaWNrKGV2ZW50OiBNb3VzZUV2ZW50KTogdm9pZCB7XG4gICAgaWYgKHRoaXMuY2hlY2tib3hlcy5sZW5ndGgpIHtcbiAgICAgIHRoaXMuY2hlY2tib3hlcy5maXJzdC5mb2N1cygpO1xuICAgIH1cbiAgICBzdXBlci5vbkNvbnRhaW5lckNsaWNrKGV2ZW50KTtcbiAgfVxuXG4gIGlzQ2hlY2tlZChvcHRpb246IGFueSkge1xuICAgIGNvbnN0IHZhbHVlID0gdGhpcy5mb3JtQ29udHJvbC52YWx1ZTtcblxuICAgIHJldHVybiB2YWx1ZSAmJiAoXG4gICAgICB0aGlzLnRvLnR5cGUgPT09ICdhcnJheSdcbiAgICAgICAgPyAodmFsdWUuaW5kZXhPZihvcHRpb24udmFsdWUpICE9PSAtMSlcbiAgICAgICAgOiB2YWx1ZVtvcHRpb24udmFsdWVdXG4gICAgKTtcbiAgfVxufVxuIl19