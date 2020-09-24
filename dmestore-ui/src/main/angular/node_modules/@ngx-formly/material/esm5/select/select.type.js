/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { MatSelect } from '@angular/material/select';
import { FieldType } from '@ngx-formly/material/form-field';
var FormlyFieldSelect = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldSelect, _super);
    function FormlyFieldSelect() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                options: [],
                compareWith: /**
                 * @param {?} o1
                 * @param {?} o2
                 * @return {?}
                 */
                function (o1, o2) {
                    return o1 === o2;
                },
            },
        };
        return _this;
    }
    /**
     * @param {?} options
     * @return {?}
     */
    FormlyFieldSelect.prototype.getSelectAllState = /**
     * @param {?} options
     * @return {?}
     */
    function (options) {
        if (this.empty || this.value.length === 0) {
            return '';
        }
        return this.value.length !== this.getSelectAllValue(options).length
            ? 'indeterminate'
            : 'checked';
    };
    /**
     * @param {?} options
     * @return {?}
     */
    FormlyFieldSelect.prototype.toggleSelectAll = /**
     * @param {?} options
     * @return {?}
     */
    function (options) {
        /** @type {?} */
        var selectAllValue = this.getSelectAllValue(options);
        this.formControl.setValue(!this.value || this.value.length !== selectAllValue.length
            ? selectAllValue
            : []);
    };
    /**
     * @param {?} $event
     * @return {?}
     */
    FormlyFieldSelect.prototype.change = /**
     * @param {?} $event
     * @return {?}
     */
    function ($event) {
        if (this.to.change) {
            this.to.change(this.field, $event);
        }
    };
    /**
     * @return {?}
     */
    FormlyFieldSelect.prototype._getAriaLabelledby = /**
     * @return {?}
     */
    function () {
        if (this.to.attributes && this.to.attributes['aria-labelledby']) {
            return this.to.attributes['aria-labelledby'];
        }
        if (this.formField && this.formField._labelId) {
            return this.formField._labelId;
        }
        return null;
    };
    /**
     * @private
     * @param {?} options
     * @return {?}
     */
    FormlyFieldSelect.prototype.getSelectAllValue = /**
     * @private
     * @param {?} options
     * @return {?}
     */
    function (options) {
        if (!this.selectAllValue || options !== this.selectAllValue.options) {
            /** @type {?} */
            var flatOptions_1 = [];
            options.forEach((/**
             * @param {?} o
             * @return {?}
             */
            function (o) { return o.group
                ? flatOptions_1.push.apply(flatOptions_1, tslib_1.__spread(o.group)) : flatOptions_1.push(o); }));
            this.selectAllValue = {
                options: options,
                value: flatOptions_1.map((/**
                 * @param {?} o
                 * @return {?}
                 */
                function (o) { return o.value; })),
            };
        }
        return this.selectAllValue.value;
    };
    FormlyFieldSelect.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-select',
                    template: "\n    <ng-template #selectAll let-selectOptions=\"selectOptions\">\n      <mat-option (click)=\"toggleSelectAll(selectOptions)\">\n        <mat-pseudo-checkbox class=\"mat-option-pseudo-checkbox\"\n          [state]=\"getSelectAllState(selectOptions)\">\n        </mat-pseudo-checkbox>\n        {{ to.selectAllOption }}\n      </mat-option>\n    </ng-template>\n\n    <mat-select [id]=\"id\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [required]=\"to.required\"\n      [compareWith]=\"to.compareWith\"\n      [multiple]=\"to.multiple\"\n      (selectionChange)=\"change($event)\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [aria-labelledby]=\"_getAriaLabelledby()\"\n      [disableOptionCentering]=\"to.disableOptionCentering\"\n      >\n      <ng-container *ngIf=\"to.options | formlySelectOptions:field | async as selectOptions\">\n        <ng-container *ngIf=\"to.multiple && to.selectAllOption\" [ngTemplateOutlet]=\"selectAll\" [ngTemplateOutletContext]=\"{ selectOptions: selectOptions }\">\n        </ng-container>\n        <ng-container *ngFor=\"let item of selectOptions\">\n          <mat-optgroup *ngIf=\"item.group\" [label]=\"item.label\">\n            <mat-option *ngFor=\"let child of item.group\" [value]=\"child.value\" [disabled]=\"child.disabled\">\n              {{ child.label }}\n            </mat-option>\n          </mat-optgroup>\n          <mat-option *ngIf=\"!item.group\" [value]=\"item.value\" [disabled]=\"item.disabled\">{{ item.label }}</mat-option>\n        </ng-container>\n      </ng-container>\n    </mat-select>\n  "
                }] }
    ];
    FormlyFieldSelect.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatSelect, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyFieldSelect;
}(FieldType));
export { FormlyFieldSelect };
if (false) {
    /** @type {?} */
    FormlyFieldSelect.prototype.formFieldControl;
    /** @type {?} */
    FormlyFieldSelect.prototype.defaultOptions;
    /**
     * @type {?}
     * @private
     */
    FormlyFieldSelect.prototype.selectAllValue;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic2VsZWN0LnR5cGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9tYXRlcmlhbC9zZWxlY3QvIiwic291cmNlcyI6WyJzZWxlY3QudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ3JELE9BQU8sRUFBRSxTQUFTLEVBQW1CLE1BQU0sMEJBQTBCLENBQUM7QUFDdEUsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBRTVEO0lBd0N1Qyw2Q0FBUztJQXhDaEQ7UUFBQSxxRUE2R0M7UUFsRUMsb0JBQWMsR0FBRztZQUNmLGVBQWUsRUFBRTtnQkFDZixPQUFPLEVBQUUsRUFBRTtnQkFDWCxXQUFXOzs7OzswQkFBQyxFQUFPLEVBQUUsRUFBTztvQkFDMUIsT0FBTyxFQUFFLEtBQUssRUFBRSxDQUFDO2dCQUNuQixDQUFDO2FBQ0Y7U0FDRixDQUFDOztJQTJESixDQUFDOzs7OztJQXZEQyw2Q0FBaUI7Ozs7SUFBakIsVUFBa0IsT0FBYztRQUM5QixJQUFJLElBQUksQ0FBQyxLQUFLLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxNQUFNLEtBQUssQ0FBQyxFQUFFO1lBQ3pDLE9BQU8sRUFBRSxDQUFDO1NBQ1g7UUFHRCxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsTUFBTSxLQUFLLElBQUksQ0FBQyxpQkFBaUIsQ0FBQyxPQUFPLENBQUMsQ0FBQyxNQUFNO1lBQ2pFLENBQUMsQ0FBQyxlQUFlO1lBQ2pCLENBQUMsQ0FBQyxTQUFTLENBQUM7SUFDaEIsQ0FBQzs7Ozs7SUFFRCwyQ0FBZTs7OztJQUFmLFVBQWdCLE9BQWM7O1lBQ3RCLGNBQWMsR0FBRyxJQUFJLENBQUMsaUJBQWlCLENBQUMsT0FBTyxDQUFDO1FBQ3RELElBQUksQ0FBQyxXQUFXLENBQUMsUUFBUSxDQUN2QixDQUFDLElBQUksQ0FBQyxLQUFLLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxNQUFNLEtBQUssY0FBYyxDQUFDLE1BQU07WUFDeEQsQ0FBQyxDQUFDLGNBQWM7WUFDaEIsQ0FBQyxDQUFDLEVBQUUsQ0FDUCxDQUFDO0lBQ0osQ0FBQzs7Ozs7SUFFRCxrQ0FBTTs7OztJQUFOLFVBQU8sTUFBdUI7UUFDNUIsSUFBSSxJQUFJLENBQUMsRUFBRSxDQUFDLE1BQU0sRUFBRTtZQUNsQixJQUFJLENBQUMsRUFBRSxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsS0FBSyxFQUFFLE1BQU0sQ0FBQyxDQUFDO1NBQ3BDO0lBQ0gsQ0FBQzs7OztJQUVELDhDQUFrQjs7O0lBQWxCO1FBQ0UsSUFBSSxJQUFJLENBQUMsRUFBRSxDQUFDLFVBQVUsSUFBSSxJQUFJLENBQUMsRUFBRSxDQUFDLFVBQVUsQ0FBQyxpQkFBaUIsQ0FBQyxFQUFFO1lBQy9ELE9BQU8sSUFBSSxDQUFDLEVBQUUsQ0FBQyxVQUFVLENBQUMsaUJBQWlCLENBQUMsQ0FBQztTQUM5QztRQUVELElBQUksSUFBSSxDQUFDLFNBQVMsSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLFFBQVEsRUFBRTtZQUM3QyxPQUFPLElBQUksQ0FBQyxTQUFTLENBQUMsUUFBUSxDQUFDO1NBQ2hDO1FBRUQsT0FBTyxJQUFJLENBQUM7SUFDZCxDQUFDOzs7Ozs7SUFFTyw2Q0FBaUI7Ozs7O0lBQXpCLFVBQTBCLE9BQWM7UUFDdEMsSUFBSSxDQUFDLElBQUksQ0FBQyxjQUFjLElBQUksT0FBTyxLQUFLLElBQUksQ0FBQyxjQUFjLENBQUMsT0FBTyxFQUFFOztnQkFDN0QsYUFBVyxHQUFVLEVBQUU7WUFDN0IsT0FBTyxDQUFDLE9BQU87Ozs7WUFBQyxVQUFBLENBQUMsSUFBSSxPQUFBLENBQUMsQ0FBQyxLQUFLO2dCQUMxQixDQUFDLENBQUMsYUFBVyxDQUFDLElBQUksT0FBaEIsYUFBVyxtQkFBUyxDQUFDLENBQUMsS0FBSyxHQUM3QixDQUFDLENBQUMsYUFBVyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsRUFGRixDQUVFLEVBQ3RCLENBQUM7WUFFRixJQUFJLENBQUMsY0FBYyxHQUFHO2dCQUNwQixPQUFPLFNBQUE7Z0JBQ1AsS0FBSyxFQUFFLGFBQVcsQ0FBQyxHQUFHOzs7O2dCQUFDLFVBQUEsQ0FBQyxJQUFJLE9BQUEsQ0FBQyxDQUFDLEtBQUssRUFBUCxDQUFPLEVBQUM7YUFDckMsQ0FBQztTQUNIO1FBR0QsT0FBTyxJQUFJLENBQUMsY0FBYyxDQUFDLEtBQUssQ0FBQztJQUNuQyxDQUFDOztnQkE1R0YsU0FBUyxTQUFDO29CQUNULFFBQVEsRUFBRSx5QkFBeUI7b0JBQ25DLFFBQVEsRUFBRSw4b0RBb0NUO2lCQUNGOzs7bUNBRUUsU0FBUyxTQUFDLFNBQVMsRUFBRSxtQkFBTSxFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTs7SUFvRTlDLHdCQUFDO0NBQUEsQUE3R0QsQ0F3Q3VDLFNBQVMsR0FxRS9DO1NBckVZLGlCQUFpQjs7O0lBQzVCLDZDQUEyRTs7SUFFM0UsMkNBT0U7Ozs7O0lBRUYsMkNBQXdEIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IE1hdFNlbGVjdCwgTWF0U2VsZWN0Q2hhbmdlIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvc2VsZWN0JztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LXNlbGVjdCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPG5nLXRlbXBsYXRlICNzZWxlY3RBbGwgbGV0LXNlbGVjdE9wdGlvbnM9XCJzZWxlY3RPcHRpb25zXCI+XG4gICAgICA8bWF0LW9wdGlvbiAoY2xpY2spPVwidG9nZ2xlU2VsZWN0QWxsKHNlbGVjdE9wdGlvbnMpXCI+XG4gICAgICAgIDxtYXQtcHNldWRvLWNoZWNrYm94IGNsYXNzPVwibWF0LW9wdGlvbi1wc2V1ZG8tY2hlY2tib3hcIlxuICAgICAgICAgIFtzdGF0ZV09XCJnZXRTZWxlY3RBbGxTdGF0ZShzZWxlY3RPcHRpb25zKVwiPlxuICAgICAgICA8L21hdC1wc2V1ZG8tY2hlY2tib3g+XG4gICAgICAgIHt7IHRvLnNlbGVjdEFsbE9wdGlvbiB9fVxuICAgICAgPC9tYXQtb3B0aW9uPlxuICAgIDwvbmctdGVtcGxhdGU+XG5cbiAgICA8bWF0LXNlbGVjdCBbaWRdPVwiaWRcIlxuICAgICAgW2Zvcm1Db250cm9sXT1cImZvcm1Db250cm9sXCJcbiAgICAgIFtmb3JtbHlBdHRyaWJ1dGVzXT1cImZpZWxkXCJcbiAgICAgIFtwbGFjZWhvbGRlcl09XCJ0by5wbGFjZWhvbGRlclwiXG4gICAgICBbdGFiaW5kZXhdPVwidG8udGFiaW5kZXhcIlxuICAgICAgW3JlcXVpcmVkXT1cInRvLnJlcXVpcmVkXCJcbiAgICAgIFtjb21wYXJlV2l0aF09XCJ0by5jb21wYXJlV2l0aFwiXG4gICAgICBbbXVsdGlwbGVdPVwidG8ubXVsdGlwbGVcIlxuICAgICAgKHNlbGVjdGlvbkNoYW5nZSk9XCJjaGFuZ2UoJGV2ZW50KVwiXG4gICAgICBbZXJyb3JTdGF0ZU1hdGNoZXJdPVwiZXJyb3JTdGF0ZU1hdGNoZXJcIlxuICAgICAgW2FyaWEtbGFiZWxsZWRieV09XCJfZ2V0QXJpYUxhYmVsbGVkYnkoKVwiXG4gICAgICBbZGlzYWJsZU9wdGlvbkNlbnRlcmluZ109XCJ0by5kaXNhYmxlT3B0aW9uQ2VudGVyaW5nXCJcbiAgICAgID5cbiAgICAgIDxuZy1jb250YWluZXIgKm5nSWY9XCJ0by5vcHRpb25zIHwgZm9ybWx5U2VsZWN0T3B0aW9uczpmaWVsZCB8IGFzeW5jIGFzIHNlbGVjdE9wdGlvbnNcIj5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdJZj1cInRvLm11bHRpcGxlICYmIHRvLnNlbGVjdEFsbE9wdGlvblwiIFtuZ1RlbXBsYXRlT3V0bGV0XT1cInNlbGVjdEFsbFwiIFtuZ1RlbXBsYXRlT3V0bGV0Q29udGV4dF09XCJ7IHNlbGVjdE9wdGlvbnM6IHNlbGVjdE9wdGlvbnMgfVwiPlxuICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdGb3I9XCJsZXQgaXRlbSBvZiBzZWxlY3RPcHRpb25zXCI+XG4gICAgICAgICAgPG1hdC1vcHRncm91cCAqbmdJZj1cIml0ZW0uZ3JvdXBcIiBbbGFiZWxdPVwiaXRlbS5sYWJlbFwiPlxuICAgICAgICAgICAgPG1hdC1vcHRpb24gKm5nRm9yPVwibGV0IGNoaWxkIG9mIGl0ZW0uZ3JvdXBcIiBbdmFsdWVdPVwiY2hpbGQudmFsdWVcIiBbZGlzYWJsZWRdPVwiY2hpbGQuZGlzYWJsZWRcIj5cbiAgICAgICAgICAgICAge3sgY2hpbGQubGFiZWwgfX1cbiAgICAgICAgICAgIDwvbWF0LW9wdGlvbj5cbiAgICAgICAgICA8L21hdC1vcHRncm91cD5cbiAgICAgICAgICA8bWF0LW9wdGlvbiAqbmdJZj1cIiFpdGVtLmdyb3VwXCIgW3ZhbHVlXT1cIml0ZW0udmFsdWVcIiBbZGlzYWJsZWRdPVwiaXRlbS5kaXNhYmxlZFwiPnt7IGl0ZW0ubGFiZWwgfX08L21hdC1vcHRpb24+XG4gICAgICAgIDwvbmctY29udGFpbmVyPlxuICAgICAgPC9uZy1jb250YWluZXI+XG4gICAgPC9tYXQtc2VsZWN0PlxuICBgLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlGaWVsZFNlbGVjdCBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIEBWaWV3Q2hpbGQoTWF0U2VsZWN0LCA8YW55PiB7IHN0YXRpYzogdHJ1ZSB9KSBmb3JtRmllbGRDb250cm9sITogTWF0U2VsZWN0O1xuXG4gIGRlZmF1bHRPcHRpb25zID0ge1xuICAgIHRlbXBsYXRlT3B0aW9uczoge1xuICAgICAgb3B0aW9uczogW10sXG4gICAgICBjb21wYXJlV2l0aChvMTogYW55LCBvMjogYW55KSB7XG4gICAgICAgIHJldHVybiBvMSA9PT0gbzI7XG4gICAgICB9LFxuICAgIH0sXG4gIH07XG5cbiAgcHJpdmF0ZSBzZWxlY3RBbGxWYWx1ZSE6IHsgb3B0aW9uczogYW55LCB2YWx1ZTogYW55W10gfTtcblxuICBnZXRTZWxlY3RBbGxTdGF0ZShvcHRpb25zOiBhbnlbXSkge1xuICAgIGlmICh0aGlzLmVtcHR5IHx8IHRoaXMudmFsdWUubGVuZ3RoID09PSAwKSB7XG4gICAgICByZXR1cm4gJyc7XG4gICAgfVxuXG5cbiAgICByZXR1cm4gdGhpcy52YWx1ZS5sZW5ndGggIT09IHRoaXMuZ2V0U2VsZWN0QWxsVmFsdWUob3B0aW9ucykubGVuZ3RoXG4gICAgICA/ICdpbmRldGVybWluYXRlJ1xuICAgICAgOiAnY2hlY2tlZCc7XG4gIH1cblxuICB0b2dnbGVTZWxlY3RBbGwob3B0aW9uczogYW55W10pIHtcbiAgICBjb25zdCBzZWxlY3RBbGxWYWx1ZSA9IHRoaXMuZ2V0U2VsZWN0QWxsVmFsdWUob3B0aW9ucyk7XG4gICAgdGhpcy5mb3JtQ29udHJvbC5zZXRWYWx1ZShcbiAgICAgICF0aGlzLnZhbHVlIHx8IHRoaXMudmFsdWUubGVuZ3RoICE9PSBzZWxlY3RBbGxWYWx1ZS5sZW5ndGhcbiAgICAgICAgPyBzZWxlY3RBbGxWYWx1ZVxuICAgICAgICA6IFtdLFxuICAgICk7XG4gIH1cblxuICBjaGFuZ2UoJGV2ZW50OiBNYXRTZWxlY3RDaGFuZ2UpIHtcbiAgICBpZiAodGhpcy50by5jaGFuZ2UpIHtcbiAgICAgIHRoaXMudG8uY2hhbmdlKHRoaXMuZmllbGQsICRldmVudCk7XG4gICAgfVxuICB9XG5cbiAgX2dldEFyaWFMYWJlbGxlZGJ5KCkge1xuICAgIGlmICh0aGlzLnRvLmF0dHJpYnV0ZXMgJiYgdGhpcy50by5hdHRyaWJ1dGVzWydhcmlhLWxhYmVsbGVkYnknXSkge1xuICAgICAgcmV0dXJuIHRoaXMudG8uYXR0cmlidXRlc1snYXJpYS1sYWJlbGxlZGJ5J107XG4gICAgfVxuXG4gICAgaWYgKHRoaXMuZm9ybUZpZWxkICYmIHRoaXMuZm9ybUZpZWxkLl9sYWJlbElkKSB7XG4gICAgICByZXR1cm4gdGhpcy5mb3JtRmllbGQuX2xhYmVsSWQ7XG4gICAgfVxuXG4gICAgcmV0dXJuIG51bGw7XG4gIH1cblxuICBwcml2YXRlIGdldFNlbGVjdEFsbFZhbHVlKG9wdGlvbnM6IGFueVtdKSB7XG4gICAgaWYgKCF0aGlzLnNlbGVjdEFsbFZhbHVlIHx8IG9wdGlvbnMgIT09IHRoaXMuc2VsZWN0QWxsVmFsdWUub3B0aW9ucykge1xuICAgICAgY29uc3QgZmxhdE9wdGlvbnM6IGFueVtdID0gW107XG4gICAgICBvcHRpb25zLmZvckVhY2gobyA9PiBvLmdyb3VwXG4gICAgICAgID8gZmxhdE9wdGlvbnMucHVzaCguLi5vLmdyb3VwKVxuICAgICAgICA6IGZsYXRPcHRpb25zLnB1c2gobyksXG4gICAgICApO1xuXG4gICAgICB0aGlzLnNlbGVjdEFsbFZhbHVlID0ge1xuICAgICAgICBvcHRpb25zLFxuICAgICAgICB2YWx1ZTogZmxhdE9wdGlvbnMubWFwKG8gPT4gby52YWx1ZSksXG4gICAgICB9O1xuICAgIH1cblxuXG4gICAgcmV0dXJuIHRoaXMuc2VsZWN0QWxsVmFsdWUudmFsdWU7XG4gIH1cbn1cbiJdfQ==