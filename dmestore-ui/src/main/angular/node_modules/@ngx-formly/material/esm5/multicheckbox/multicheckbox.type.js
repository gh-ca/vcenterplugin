/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChildren, QueryList } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatCheckbox } from '@angular/material/checkbox';
var FormlyFieldMultiCheckbox = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldMultiCheckbox, _super);
    function FormlyFieldMultiCheckbox() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                color: 'accent',
            },
        };
        return _this;
    }
    /**
     * @param {?} value
     * @param {?} checked
     * @return {?}
     */
    FormlyFieldMultiCheckbox.prototype.onChange = /**
     * @param {?} value
     * @param {?} checked
     * @return {?}
     */
    function (value, checked) {
        var _a;
        if (this.to.type === 'array') {
            this.formControl.patchValue(checked
                ? tslib_1.__spread((this.formControl.value || []), [value]) : tslib_1.__spread((this.formControl.value || [])).filter((/**
             * @param {?} o
             * @return {?}
             */
            function (o) { return o !== value; })));
        }
        else {
            this.formControl.patchValue(tslib_1.__assign({}, this.formControl.value, (_a = {}, _a[value] = checked, _a)));
        }
        this.formControl.markAsTouched();
    };
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyFieldMultiCheckbox.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        if (this.checkboxes.length) {
            this.checkboxes.first.focus();
        }
        _super.prototype.onContainerClick.call(this, event);
    };
    /**
     * @param {?} option
     * @return {?}
     */
    FormlyFieldMultiCheckbox.prototype.isChecked = /**
     * @param {?} option
     * @return {?}
     */
    function (option) {
        /** @type {?} */
        var value = this.formControl.value;
        return value && (this.to.type === 'array'
            ? (value.indexOf(option.value) !== -1)
            : value[option.value]);
    };
    FormlyFieldMultiCheckbox.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-multicheckbox',
                    template: "\n    <ng-container *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\">\n      <mat-checkbox\n        [id]=\"id + '_' + i\"\n        [formlyAttributes]=\"field\"\n        [tabindex]=\"to.tabindex\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [checked]=\"isChecked(option)\"\n        [disabled]=\"formControl.disabled\"\n        (change)=\"onChange(option.value, $event.checked)\">\n          {{ option.label }}\n      </mat-checkbox>\n    </ng-container>\n  "
                }] }
    ];
    FormlyFieldMultiCheckbox.propDecorators = {
        checkboxes: [{ type: ViewChildren, args: [MatCheckbox,] }]
    };
    return FormlyFieldMultiCheckbox;
}(FieldType));
export { FormlyFieldMultiCheckbox };
if (false) {
    /** @type {?} */
    FormlyFieldMultiCheckbox.prototype.checkboxes;
    /** @type {?} */
    FormlyFieldMultiCheckbox.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibXVsdGljaGVja2JveC50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvbXVsdGljaGVja2JveC8iLCJzb3VyY2VzIjpbIm11bHRpY2hlY2tib3gudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsWUFBWSxFQUFFLFNBQVMsRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUNuRSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDNUQsT0FBTyxFQUFFLFdBQVcsRUFBRSxNQUFNLDRCQUE0QixDQUFDO0FBRXpEO0lBa0I4QyxvREFBUztJQWxCdkQ7UUFBQSxxRUEwREM7UUFyQ0Msb0JBQWMsR0FBRztZQUNmLGVBQWUsRUFBRTtnQkFDZixrQkFBa0IsRUFBRSxJQUFJO2dCQUN4QixVQUFVLEVBQUUsUUFBUTtnQkFDcEIsT0FBTyxFQUFFLEVBQUU7Z0JBQ1gsS0FBSyxFQUFFLFFBQVE7YUFDaEI7U0FDRixDQUFDOztJQThCSixDQUFDOzs7Ozs7SUE1QkMsMkNBQVE7Ozs7O0lBQVIsVUFBUyxLQUFVLEVBQUUsT0FBZ0I7O1FBQ25DLElBQUksSUFBSSxDQUFDLEVBQUUsQ0FBQyxJQUFJLEtBQUssT0FBTyxFQUFFO1lBQzVCLElBQUksQ0FBQyxXQUFXLENBQUMsVUFBVSxDQUFDLE9BQU87Z0JBQ2pDLENBQUMsa0JBQUssQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssSUFBSSxFQUFFLENBQUMsR0FBRSxLQUFLLEdBQzNDLENBQUMsQ0FBQyxpQkFBSSxDQUFDLElBQUksQ0FBQyxXQUFXLENBQUMsS0FBSyxJQUFJLEVBQUUsQ0FBQyxFQUFFLE1BQU07Ozs7WUFBQyxVQUFBLENBQUMsSUFBSSxPQUFBLENBQUMsS0FBSyxLQUFLLEVBQVgsQ0FBVyxFQUFDLENBQy9ELENBQUM7U0FDSDthQUFNO1lBQ0wsSUFBSSxDQUFDLFdBQVcsQ0FBQyxVQUFVLHNCQUFNLElBQUksQ0FBQyxXQUFXLENBQUMsS0FBSyxlQUFHLEtBQUssSUFBRyxPQUFPLE9BQUcsQ0FBQztTQUM5RTtRQUNELElBQUksQ0FBQyxXQUFXLENBQUMsYUFBYSxFQUFFLENBQUM7SUFDbkMsQ0FBQzs7Ozs7SUFFRCxtREFBZ0I7Ozs7SUFBaEIsVUFBaUIsS0FBaUI7UUFDaEMsSUFBSSxJQUFJLENBQUMsVUFBVSxDQUFDLE1BQU0sRUFBRTtZQUMxQixJQUFJLENBQUMsVUFBVSxDQUFDLEtBQUssQ0FBQyxLQUFLLEVBQUUsQ0FBQztTQUMvQjtRQUNELGlCQUFNLGdCQUFnQixZQUFDLEtBQUssQ0FBQyxDQUFDO0lBQ2hDLENBQUM7Ozs7O0lBRUQsNENBQVM7Ozs7SUFBVCxVQUFVLE1BQVc7O1lBQ2IsS0FBSyxHQUFHLElBQUksQ0FBQyxXQUFXLENBQUMsS0FBSztRQUVwQyxPQUFPLEtBQUssSUFBSSxDQUNkLElBQUksQ0FBQyxFQUFFLENBQUMsSUFBSSxLQUFLLE9BQU87WUFDdEIsQ0FBQyxDQUFDLENBQUMsS0FBSyxDQUFDLE9BQU8sQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUM7WUFDdEMsQ0FBQyxDQUFDLEtBQUssQ0FBQyxNQUFNLENBQUMsS0FBSyxDQUFDLENBQ3hCLENBQUM7SUFDSixDQUFDOztnQkF6REYsU0FBUyxTQUFDO29CQUNULFFBQVEsRUFBRSxnQ0FBZ0M7b0JBQzFDLFFBQVEsRUFBRSwwaEJBY1Q7aUJBQ0Y7Ozs2QkFFRSxZQUFZLFNBQUMsV0FBVzs7SUF1QzNCLCtCQUFDO0NBQUEsQUExREQsQ0FrQjhDLFNBQVMsR0F3Q3REO1NBeENZLHdCQUF3Qjs7O0lBQ25DLDhDQUErRDs7SUFFL0Qsa0RBT0UiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQsIFZpZXdDaGlsZHJlbiwgUXVlcnlMaXN0IH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICdAbmd4LWZvcm1seS9tYXRlcmlhbC9mb3JtLWZpZWxkJztcbmltcG9ydCB7IE1hdENoZWNrYm94IH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvY2hlY2tib3gnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LW11bHRpY2hlY2tib3gnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxuZy1jb250YWluZXIgKm5nRm9yPVwibGV0IG9wdGlvbiBvZiB0by5vcHRpb25zIHwgZm9ybWx5U2VsZWN0T3B0aW9uczpmaWVsZCB8IGFzeW5jOyBsZXQgaSA9IGluZGV4O1wiPlxuICAgICAgPG1hdC1jaGVja2JveFxuICAgICAgICBbaWRdPVwiaWQgKyAnXycgKyBpXCJcbiAgICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgICBbdGFiaW5kZXhdPVwidG8udGFiaW5kZXhcIlxuICAgICAgICBbY29sb3JdPVwidG8uY29sb3JcIlxuICAgICAgICBbbGFiZWxQb3NpdGlvbl09XCJ0by5sYWJlbFBvc2l0aW9uXCJcbiAgICAgICAgW2NoZWNrZWRdPVwiaXNDaGVja2VkKG9wdGlvbilcIlxuICAgICAgICBbZGlzYWJsZWRdPVwiZm9ybUNvbnRyb2wuZGlzYWJsZWRcIlxuICAgICAgICAoY2hhbmdlKT1cIm9uQ2hhbmdlKG9wdGlvbi52YWx1ZSwgJGV2ZW50LmNoZWNrZWQpXCI+XG4gICAgICAgICAge3sgb3B0aW9uLmxhYmVsIH19XG4gICAgICA8L21hdC1jaGVja2JveD5cbiAgICA8L25nLWNvbnRhaW5lcj5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRNdWx0aUNoZWNrYm94IGV4dGVuZHMgRmllbGRUeXBlIHtcbiAgQFZpZXdDaGlsZHJlbihNYXRDaGVja2JveCkgY2hlY2tib3hlcyE6IFF1ZXJ5TGlzdDxNYXRDaGVja2JveD47XG5cbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBoaWRlRmllbGRVbmRlcmxpbmU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIG9wdGlvbnM6IFtdLFxuICAgICAgY29sb3I6ICdhY2NlbnQnLCAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9jb21wb25lbnRzL2lzc3Vlcy8xODQ2NVxuICAgIH0sXG4gIH07XG5cbiAgb25DaGFuZ2UodmFsdWU6IGFueSwgY2hlY2tlZDogYm9vbGVhbikge1xuICAgIGlmICh0aGlzLnRvLnR5cGUgPT09ICdhcnJheScpIHtcbiAgICAgIHRoaXMuZm9ybUNvbnRyb2wucGF0Y2hWYWx1ZShjaGVja2VkXG4gICAgICAgID8gWy4uLih0aGlzLmZvcm1Db250cm9sLnZhbHVlIHx8IFtdKSwgdmFsdWVdXG4gICAgICAgIDogWy4uLih0aGlzLmZvcm1Db250cm9sLnZhbHVlIHx8IFtdKV0uZmlsdGVyKG8gPT4gbyAhPT0gdmFsdWUpLFxuICAgICAgKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdGhpcy5mb3JtQ29udHJvbC5wYXRjaFZhbHVlKHsgLi4udGhpcy5mb3JtQ29udHJvbC52YWx1ZSwgW3ZhbHVlXTogY2hlY2tlZCB9KTtcbiAgICB9XG4gICAgdGhpcy5mb3JtQ29udHJvbC5tYXJrQXNUb3VjaGVkKCk7XG4gIH1cblxuICBvbkNvbnRhaW5lckNsaWNrKGV2ZW50OiBNb3VzZUV2ZW50KTogdm9pZCB7XG4gICAgaWYgKHRoaXMuY2hlY2tib3hlcy5sZW5ndGgpIHtcbiAgICAgIHRoaXMuY2hlY2tib3hlcy5maXJzdC5mb2N1cygpO1xuICAgIH1cbiAgICBzdXBlci5vbkNvbnRhaW5lckNsaWNrKGV2ZW50KTtcbiAgfVxuXG4gIGlzQ2hlY2tlZChvcHRpb246IGFueSkge1xuICAgIGNvbnN0IHZhbHVlID0gdGhpcy5mb3JtQ29udHJvbC52YWx1ZTtcblxuICAgIHJldHVybiB2YWx1ZSAmJiAoXG4gICAgICB0aGlzLnRvLnR5cGUgPT09ICdhcnJheSdcbiAgICAgICAgPyAodmFsdWUuaW5kZXhPZihvcHRpb24udmFsdWUpICE9PSAtMSlcbiAgICAgICAgOiB2YWx1ZVtvcHRpb24udmFsdWVdXG4gICAgKTtcbiAgfVxufVxuIl19