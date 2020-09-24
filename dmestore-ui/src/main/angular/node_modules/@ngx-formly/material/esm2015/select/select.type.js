/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChild } from '@angular/core';
import { MatSelect } from '@angular/material/select';
import { FieldType } from '@ngx-formly/material/form-field';
export class FormlyFieldSelect extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                options: [],
                /**
                 * @param {?} o1
                 * @param {?} o2
                 * @return {?}
                 */
                compareWith(o1, o2) {
                    return o1 === o2;
                },
            },
        };
    }
    /**
     * @param {?} options
     * @return {?}
     */
    getSelectAllState(options) {
        if (this.empty || this.value.length === 0) {
            return '';
        }
        return this.value.length !== this.getSelectAllValue(options).length
            ? 'indeterminate'
            : 'checked';
    }
    /**
     * @param {?} options
     * @return {?}
     */
    toggleSelectAll(options) {
        /** @type {?} */
        const selectAllValue = this.getSelectAllValue(options);
        this.formControl.setValue(!this.value || this.value.length !== selectAllValue.length
            ? selectAllValue
            : []);
    }
    /**
     * @param {?} $event
     * @return {?}
     */
    change($event) {
        if (this.to.change) {
            this.to.change(this.field, $event);
        }
    }
    /**
     * @return {?}
     */
    _getAriaLabelledby() {
        if (this.to.attributes && this.to.attributes['aria-labelledby']) {
            return this.to.attributes['aria-labelledby'];
        }
        if (this.formField && this.formField._labelId) {
            return this.formField._labelId;
        }
        return null;
    }
    /**
     * @private
     * @param {?} options
     * @return {?}
     */
    getSelectAllValue(options) {
        if (!this.selectAllValue || options !== this.selectAllValue.options) {
            /** @type {?} */
            const flatOptions = [];
            options.forEach((/**
             * @param {?} o
             * @return {?}
             */
            o => o.group
                ? flatOptions.push(...o.group)
                : flatOptions.push(o)));
            this.selectAllValue = {
                options,
                value: flatOptions.map((/**
                 * @param {?} o
                 * @return {?}
                 */
                o => o.value)),
            };
        }
        return this.selectAllValue.value;
    }
}
FormlyFieldSelect.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-select',
                template: `
    <ng-template #selectAll let-selectOptions="selectOptions">
      <mat-option (click)="toggleSelectAll(selectOptions)">
        <mat-pseudo-checkbox class="mat-option-pseudo-checkbox"
          [state]="getSelectAllState(selectOptions)">
        </mat-pseudo-checkbox>
        {{ to.selectAllOption }}
      </mat-option>
    </ng-template>

    <mat-select [id]="id"
      [formControl]="formControl"
      [formlyAttributes]="field"
      [placeholder]="to.placeholder"
      [tabindex]="to.tabindex"
      [required]="to.required"
      [compareWith]="to.compareWith"
      [multiple]="to.multiple"
      (selectionChange)="change($event)"
      [errorStateMatcher]="errorStateMatcher"
      [aria-labelledby]="_getAriaLabelledby()"
      [disableOptionCentering]="to.disableOptionCentering"
      >
      <ng-container *ngIf="to.options | formlySelectOptions:field | async as selectOptions">
        <ng-container *ngIf="to.multiple && to.selectAllOption" [ngTemplateOutlet]="selectAll" [ngTemplateOutletContext]="{ selectOptions: selectOptions }">
        </ng-container>
        <ng-container *ngFor="let item of selectOptions">
          <mat-optgroup *ngIf="item.group" [label]="item.label">
            <mat-option *ngFor="let child of item.group" [value]="child.value" [disabled]="child.disabled">
              {{ child.label }}
            </mat-option>
          </mat-optgroup>
          <mat-option *ngIf="!item.group" [value]="item.value" [disabled]="item.disabled">{{ item.label }}</mat-option>
        </ng-container>
      </ng-container>
    </mat-select>
  `
            }] }
];
FormlyFieldSelect.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatSelect, (/** @type {?} */ ({ static: true })),] }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic2VsZWN0LnR5cGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9tYXRlcmlhbC9zZWxlY3QvIiwic291cmNlcyI6WyJzZWxlY3QudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDckQsT0FBTyxFQUFFLFNBQVMsRUFBbUIsTUFBTSwwQkFBMEIsQ0FBQztBQUN0RSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUEwQzVELE1BQU0sT0FBTyxpQkFBa0IsU0FBUSxTQUFTO0lBeENoRDs7UUEyQ0UsbUJBQWMsR0FBRztZQUNmLGVBQWUsRUFBRTtnQkFDZixPQUFPLEVBQUUsRUFBRTs7Ozs7O2dCQUNYLFdBQVcsQ0FBQyxFQUFPLEVBQUUsRUFBTztvQkFDMUIsT0FBTyxFQUFFLEtBQUssRUFBRSxDQUFDO2dCQUNuQixDQUFDO2FBQ0Y7U0FDRixDQUFDO0lBMkRKLENBQUM7Ozs7O0lBdkRDLGlCQUFpQixDQUFDLE9BQWM7UUFDOUIsSUFBSSxJQUFJLENBQUMsS0FBSyxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsTUFBTSxLQUFLLENBQUMsRUFBRTtZQUN6QyxPQUFPLEVBQUUsQ0FBQztTQUNYO1FBR0QsT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLE1BQU0sS0FBSyxJQUFJLENBQUMsaUJBQWlCLENBQUMsT0FBTyxDQUFDLENBQUMsTUFBTTtZQUNqRSxDQUFDLENBQUMsZUFBZTtZQUNqQixDQUFDLENBQUMsU0FBUyxDQUFDO0lBQ2hCLENBQUM7Ozs7O0lBRUQsZUFBZSxDQUFDLE9BQWM7O2NBQ3RCLGNBQWMsR0FBRyxJQUFJLENBQUMsaUJBQWlCLENBQUMsT0FBTyxDQUFDO1FBQ3RELElBQUksQ0FBQyxXQUFXLENBQUMsUUFBUSxDQUN2QixDQUFDLElBQUksQ0FBQyxLQUFLLElBQUksSUFBSSxDQUFDLEtBQUssQ0FBQyxNQUFNLEtBQUssY0FBYyxDQUFDLE1BQU07WUFDeEQsQ0FBQyxDQUFDLGNBQWM7WUFDaEIsQ0FBQyxDQUFDLEVBQUUsQ0FDUCxDQUFDO0lBQ0osQ0FBQzs7Ozs7SUFFRCxNQUFNLENBQUMsTUFBdUI7UUFDNUIsSUFBSSxJQUFJLENBQUMsRUFBRSxDQUFDLE1BQU0sRUFBRTtZQUNsQixJQUFJLENBQUMsRUFBRSxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsS0FBSyxFQUFFLE1BQU0sQ0FBQyxDQUFDO1NBQ3BDO0lBQ0gsQ0FBQzs7OztJQUVELGtCQUFrQjtRQUNoQixJQUFJLElBQUksQ0FBQyxFQUFFLENBQUMsVUFBVSxJQUFJLElBQUksQ0FBQyxFQUFFLENBQUMsVUFBVSxDQUFDLGlCQUFpQixDQUFDLEVBQUU7WUFDL0QsT0FBTyxJQUFJLENBQUMsRUFBRSxDQUFDLFVBQVUsQ0FBQyxpQkFBaUIsQ0FBQyxDQUFDO1NBQzlDO1FBRUQsSUFBSSxJQUFJLENBQUMsU0FBUyxJQUFJLElBQUksQ0FBQyxTQUFTLENBQUMsUUFBUSxFQUFFO1lBQzdDLE9BQU8sSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLENBQUM7U0FDaEM7UUFFRCxPQUFPLElBQUksQ0FBQztJQUNkLENBQUM7Ozs7OztJQUVPLGlCQUFpQixDQUFDLE9BQWM7UUFDdEMsSUFBSSxDQUFDLElBQUksQ0FBQyxjQUFjLElBQUksT0FBTyxLQUFLLElBQUksQ0FBQyxjQUFjLENBQUMsT0FBTyxFQUFFOztrQkFDN0QsV0FBVyxHQUFVLEVBQUU7WUFDN0IsT0FBTyxDQUFDLE9BQU87Ozs7WUFBQyxDQUFDLENBQUMsRUFBRSxDQUFDLENBQUMsQ0FBQyxLQUFLO2dCQUMxQixDQUFDLENBQUMsV0FBVyxDQUFDLElBQUksQ0FBQyxHQUFHLENBQUMsQ0FBQyxLQUFLLENBQUM7Z0JBQzlCLENBQUMsQ0FBQyxXQUFXLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxFQUN0QixDQUFDO1lBRUYsSUFBSSxDQUFDLGNBQWMsR0FBRztnQkFDcEIsT0FBTztnQkFDUCxLQUFLLEVBQUUsV0FBVyxDQUFDLEdBQUc7Ozs7Z0JBQUMsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxDQUFDLENBQUMsS0FBSyxFQUFDO2FBQ3JDLENBQUM7U0FDSDtRQUdELE9BQU8sSUFBSSxDQUFDLGNBQWMsQ0FBQyxLQUFLLENBQUM7SUFDbkMsQ0FBQzs7O1lBNUdGLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUseUJBQXlCO2dCQUNuQyxRQUFRLEVBQUU7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztHQW9DVDthQUNGOzs7K0JBRUUsU0FBUyxTQUFDLFNBQVMsRUFBRSxtQkFBTSxFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTs7OztJQUE1Qyw2Q0FBMkU7O0lBRTNFLDJDQU9FOzs7OztJQUVGLDJDQUF3RCIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgVmlld0NoaWxkIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBNYXRTZWxlY3QsIE1hdFNlbGVjdENoYW5nZSB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL3NlbGVjdCc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICdAbmd4LWZvcm1seS9tYXRlcmlhbC9mb3JtLWZpZWxkJztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnZm9ybWx5LWZpZWxkLW1hdC1zZWxlY3QnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxuZy10ZW1wbGF0ZSAjc2VsZWN0QWxsIGxldC1zZWxlY3RPcHRpb25zPVwic2VsZWN0T3B0aW9uc1wiPlxuICAgICAgPG1hdC1vcHRpb24gKGNsaWNrKT1cInRvZ2dsZVNlbGVjdEFsbChzZWxlY3RPcHRpb25zKVwiPlxuICAgICAgICA8bWF0LXBzZXVkby1jaGVja2JveCBjbGFzcz1cIm1hdC1vcHRpb24tcHNldWRvLWNoZWNrYm94XCJcbiAgICAgICAgICBbc3RhdGVdPVwiZ2V0U2VsZWN0QWxsU3RhdGUoc2VsZWN0T3B0aW9ucylcIj5cbiAgICAgICAgPC9tYXQtcHNldWRvLWNoZWNrYm94PlxuICAgICAgICB7eyB0by5zZWxlY3RBbGxPcHRpb24gfX1cbiAgICAgIDwvbWF0LW9wdGlvbj5cbiAgICA8L25nLXRlbXBsYXRlPlxuXG4gICAgPG1hdC1zZWxlY3QgW2lkXT1cImlkXCJcbiAgICAgIFtmb3JtQ29udHJvbF09XCJmb3JtQ29udHJvbFwiXG4gICAgICBbZm9ybWx5QXR0cmlidXRlc109XCJmaWVsZFwiXG4gICAgICBbcGxhY2Vob2xkZXJdPVwidG8ucGxhY2Vob2xkZXJcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtyZXF1aXJlZF09XCJ0by5yZXF1aXJlZFwiXG4gICAgICBbY29tcGFyZVdpdGhdPVwidG8uY29tcGFyZVdpdGhcIlxuICAgICAgW211bHRpcGxlXT1cInRvLm11bHRpcGxlXCJcbiAgICAgIChzZWxlY3Rpb25DaGFuZ2UpPVwiY2hhbmdlKCRldmVudClcIlxuICAgICAgW2Vycm9yU3RhdGVNYXRjaGVyXT1cImVycm9yU3RhdGVNYXRjaGVyXCJcbiAgICAgIFthcmlhLWxhYmVsbGVkYnldPVwiX2dldEFyaWFMYWJlbGxlZGJ5KClcIlxuICAgICAgW2Rpc2FibGVPcHRpb25DZW50ZXJpbmddPVwidG8uZGlzYWJsZU9wdGlvbkNlbnRlcmluZ1wiXG4gICAgICA+XG4gICAgICA8bmctY29udGFpbmVyICpuZ0lmPVwidG8ub3B0aW9ucyB8IGZvcm1seVNlbGVjdE9wdGlvbnM6ZmllbGQgfCBhc3luYyBhcyBzZWxlY3RPcHRpb25zXCI+XG4gICAgICAgIDxuZy1jb250YWluZXIgKm5nSWY9XCJ0by5tdWx0aXBsZSAmJiB0by5zZWxlY3RBbGxPcHRpb25cIiBbbmdUZW1wbGF0ZU91dGxldF09XCJzZWxlY3RBbGxcIiBbbmdUZW1wbGF0ZU91dGxldENvbnRleHRdPVwieyBzZWxlY3RPcHRpb25zOiBzZWxlY3RPcHRpb25zIH1cIj5cbiAgICAgICAgPC9uZy1jb250YWluZXI+XG4gICAgICAgIDxuZy1jb250YWluZXIgKm5nRm9yPVwibGV0IGl0ZW0gb2Ygc2VsZWN0T3B0aW9uc1wiPlxuICAgICAgICAgIDxtYXQtb3B0Z3JvdXAgKm5nSWY9XCJpdGVtLmdyb3VwXCIgW2xhYmVsXT1cIml0ZW0ubGFiZWxcIj5cbiAgICAgICAgICAgIDxtYXQtb3B0aW9uICpuZ0Zvcj1cImxldCBjaGlsZCBvZiBpdGVtLmdyb3VwXCIgW3ZhbHVlXT1cImNoaWxkLnZhbHVlXCIgW2Rpc2FibGVkXT1cImNoaWxkLmRpc2FibGVkXCI+XG4gICAgICAgICAgICAgIHt7IGNoaWxkLmxhYmVsIH19XG4gICAgICAgICAgICA8L21hdC1vcHRpb24+XG4gICAgICAgICAgPC9tYXQtb3B0Z3JvdXA+XG4gICAgICAgICAgPG1hdC1vcHRpb24gKm5nSWY9XCIhaXRlbS5ncm91cFwiIFt2YWx1ZV09XCJpdGVtLnZhbHVlXCIgW2Rpc2FibGVkXT1cIml0ZW0uZGlzYWJsZWRcIj57eyBpdGVtLmxhYmVsIH19PC9tYXQtb3B0aW9uPlxuICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgIDwvbmctY29udGFpbmVyPlxuICAgIDwvbWF0LXNlbGVjdD5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRTZWxlY3QgZXh0ZW5kcyBGaWVsZFR5cGUge1xuICBAVmlld0NoaWxkKE1hdFNlbGVjdCwgPGFueT4geyBzdGF0aWM6IHRydWUgfSkgZm9ybUZpZWxkQ29udHJvbCE6IE1hdFNlbGVjdDtcblxuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIG9wdGlvbnM6IFtdLFxuICAgICAgY29tcGFyZVdpdGgobzE6IGFueSwgbzI6IGFueSkge1xuICAgICAgICByZXR1cm4gbzEgPT09IG8yO1xuICAgICAgfSxcbiAgICB9LFxuICB9O1xuXG4gIHByaXZhdGUgc2VsZWN0QWxsVmFsdWUhOiB7IG9wdGlvbnM6IGFueSwgdmFsdWU6IGFueVtdIH07XG5cbiAgZ2V0U2VsZWN0QWxsU3RhdGUob3B0aW9uczogYW55W10pIHtcbiAgICBpZiAodGhpcy5lbXB0eSB8fCB0aGlzLnZhbHVlLmxlbmd0aCA9PT0gMCkge1xuICAgICAgcmV0dXJuICcnO1xuICAgIH1cblxuXG4gICAgcmV0dXJuIHRoaXMudmFsdWUubGVuZ3RoICE9PSB0aGlzLmdldFNlbGVjdEFsbFZhbHVlKG9wdGlvbnMpLmxlbmd0aFxuICAgICAgPyAnaW5kZXRlcm1pbmF0ZSdcbiAgICAgIDogJ2NoZWNrZWQnO1xuICB9XG5cbiAgdG9nZ2xlU2VsZWN0QWxsKG9wdGlvbnM6IGFueVtdKSB7XG4gICAgY29uc3Qgc2VsZWN0QWxsVmFsdWUgPSB0aGlzLmdldFNlbGVjdEFsbFZhbHVlKG9wdGlvbnMpO1xuICAgIHRoaXMuZm9ybUNvbnRyb2wuc2V0VmFsdWUoXG4gICAgICAhdGhpcy52YWx1ZSB8fCB0aGlzLnZhbHVlLmxlbmd0aCAhPT0gc2VsZWN0QWxsVmFsdWUubGVuZ3RoXG4gICAgICAgID8gc2VsZWN0QWxsVmFsdWVcbiAgICAgICAgOiBbXSxcbiAgICApO1xuICB9XG5cbiAgY2hhbmdlKCRldmVudDogTWF0U2VsZWN0Q2hhbmdlKSB7XG4gICAgaWYgKHRoaXMudG8uY2hhbmdlKSB7XG4gICAgICB0aGlzLnRvLmNoYW5nZSh0aGlzLmZpZWxkLCAkZXZlbnQpO1xuICAgIH1cbiAgfVxuXG4gIF9nZXRBcmlhTGFiZWxsZWRieSgpIHtcbiAgICBpZiAodGhpcy50by5hdHRyaWJ1dGVzICYmIHRoaXMudG8uYXR0cmlidXRlc1snYXJpYS1sYWJlbGxlZGJ5J10pIHtcbiAgICAgIHJldHVybiB0aGlzLnRvLmF0dHJpYnV0ZXNbJ2FyaWEtbGFiZWxsZWRieSddO1xuICAgIH1cblxuICAgIGlmICh0aGlzLmZvcm1GaWVsZCAmJiB0aGlzLmZvcm1GaWVsZC5fbGFiZWxJZCkge1xuICAgICAgcmV0dXJuIHRoaXMuZm9ybUZpZWxkLl9sYWJlbElkO1xuICAgIH1cblxuICAgIHJldHVybiBudWxsO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRTZWxlY3RBbGxWYWx1ZShvcHRpb25zOiBhbnlbXSkge1xuICAgIGlmICghdGhpcy5zZWxlY3RBbGxWYWx1ZSB8fCBvcHRpb25zICE9PSB0aGlzLnNlbGVjdEFsbFZhbHVlLm9wdGlvbnMpIHtcbiAgICAgIGNvbnN0IGZsYXRPcHRpb25zOiBhbnlbXSA9IFtdO1xuICAgICAgb3B0aW9ucy5mb3JFYWNoKG8gPT4gby5ncm91cFxuICAgICAgICA/IGZsYXRPcHRpb25zLnB1c2goLi4uby5ncm91cClcbiAgICAgICAgOiBmbGF0T3B0aW9ucy5wdXNoKG8pLFxuICAgICAgKTtcblxuICAgICAgdGhpcy5zZWxlY3RBbGxWYWx1ZSA9IHtcbiAgICAgICAgb3B0aW9ucyxcbiAgICAgICAgdmFsdWU6IGZsYXRPcHRpb25zLm1hcChvID0+IG8udmFsdWUpLFxuICAgICAgfTtcbiAgICB9XG5cblxuICAgIHJldHVybiB0aGlzLnNlbGVjdEFsbFZhbHVlLnZhbHVlO1xuICB9XG59XG4iXX0=