/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChild } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatInput } from '@angular/material/input';
export class FormlyFieldNativeSelect extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                options: [],
            },
        };
    }
}
FormlyFieldNativeSelect.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-native-select',
                template: `
    <select matNativeControl
      [id]="id"
      [readonly]="to.readonly"
      [required]="to.required"
      [errorStateMatcher]="errorStateMatcher"
      [formControl]="formControl"
      [formlyAttributes]="field">
      <option *ngIf="to.placeholder" [ngValue]="null">{{ to.placeholder }}</option>
      <ng-container *ngIf="to.options | formlySelectOptions:field | async as opts">
        <ng-container *ngIf="to._flatOptions else grouplist">
          <ng-container *ngFor="let opt of opts">
            <option [ngValue]="opt.value" [disabled]="opt.disabled">{{ opt.label }}</option>
          </ng-container>
        </ng-container>

        <ng-template #grouplist>
          <ng-container *ngFor="let opt of opts">
            <option *ngIf="!opt.group else optgroup" [ngValue]="opt.value" [disabled]="opt.disabled">{{ opt.label }}</option>
            <ng-template #optgroup>
              <optgroup [label]="opt.label">
                <option *ngFor="let child of opt.group" [ngValue]="child.value" [disabled]="child.disabled">
                  {{ child.label }}
                </option>
              </optgroup>
            </ng-template>
          </ng-container>
        </ng-template>
      </ng-container>
    </select>
  `
            }] }
];
FormlyFieldNativeSelect.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
};
if (false) {
    /** @type {?} */
    FormlyFieldNativeSelect.prototype.formFieldControl;
    /** @type {?} */
    FormlyFieldNativeSelect.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibmF0aXZlLXNlbGVjdC50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvbmF0aXZlLXNlbGVjdC8iLCJzb3VyY2VzIjpbIm5hdGl2ZS1zZWxlY3QudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDckQsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBQzVELE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSx5QkFBeUIsQ0FBQztBQW9DbkQsTUFBTSxPQUFPLHVCQUF3QixTQUFRLFNBQVM7SUFsQ3REOztRQW9DRSxtQkFBYyxHQUFHO1lBQ2YsZUFBZSxFQUFFO2dCQUNmLE9BQU8sRUFBRSxFQUFFO2FBQ1o7U0FDRixDQUFDO0lBQ0osQ0FBQzs7O1lBekNBLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUsZ0NBQWdDO2dCQUMxQyxRQUFRLEVBQUU7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztHQThCVDthQUNGOzs7K0JBRUUsU0FBUyxTQUFDLFFBQVEsRUFBRSxtQkFBTSxFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTs7OztJQUEzQyxtREFBeUU7O0lBQ3pFLGlEQUlFIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0SW5wdXQgfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9pbnB1dCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtbmF0aXZlLXNlbGVjdCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPHNlbGVjdCBtYXROYXRpdmVDb250cm9sXG4gICAgICBbaWRdPVwiaWRcIlxuICAgICAgW3JlYWRvbmx5XT1cInRvLnJlYWRvbmx5XCJcbiAgICAgIFtyZXF1aXJlZF09XCJ0by5yZXF1aXJlZFwiXG4gICAgICBbZXJyb3JTdGF0ZU1hdGNoZXJdPVwiZXJyb3JTdGF0ZU1hdGNoZXJcIlxuICAgICAgW2Zvcm1Db250cm9sXT1cImZvcm1Db250cm9sXCJcbiAgICAgIFtmb3JtbHlBdHRyaWJ1dGVzXT1cImZpZWxkXCI+XG4gICAgICA8b3B0aW9uICpuZ0lmPVwidG8ucGxhY2Vob2xkZXJcIiBbbmdWYWx1ZV09XCJudWxsXCI+e3sgdG8ucGxhY2Vob2xkZXIgfX08L29wdGlvbj5cbiAgICAgIDxuZy1jb250YWluZXIgKm5nSWY9XCJ0by5vcHRpb25zIHwgZm9ybWx5U2VsZWN0T3B0aW9uczpmaWVsZCB8IGFzeW5jIGFzIG9wdHNcIj5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdJZj1cInRvLl9mbGF0T3B0aW9ucyBlbHNlIGdyb3VwbGlzdFwiPlxuICAgICAgICAgIDxuZy1jb250YWluZXIgKm5nRm9yPVwibGV0IG9wdCBvZiBvcHRzXCI+XG4gICAgICAgICAgICA8b3B0aW9uIFtuZ1ZhbHVlXT1cIm9wdC52YWx1ZVwiIFtkaXNhYmxlZF09XCJvcHQuZGlzYWJsZWRcIj57eyBvcHQubGFiZWwgfX08L29wdGlvbj5cbiAgICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgICAgPC9uZy1jb250YWluZXI+XG5cbiAgICAgICAgPG5nLXRlbXBsYXRlICNncm91cGxpc3Q+XG4gICAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdGb3I9XCJsZXQgb3B0IG9mIG9wdHNcIj5cbiAgICAgICAgICAgIDxvcHRpb24gKm5nSWY9XCIhb3B0Lmdyb3VwIGVsc2Ugb3B0Z3JvdXBcIiBbbmdWYWx1ZV09XCJvcHQudmFsdWVcIiBbZGlzYWJsZWRdPVwib3B0LmRpc2FibGVkXCI+e3sgb3B0LmxhYmVsIH19PC9vcHRpb24+XG4gICAgICAgICAgICA8bmctdGVtcGxhdGUgI29wdGdyb3VwPlxuICAgICAgICAgICAgICA8b3B0Z3JvdXAgW2xhYmVsXT1cIm9wdC5sYWJlbFwiPlxuICAgICAgICAgICAgICAgIDxvcHRpb24gKm5nRm9yPVwibGV0IGNoaWxkIG9mIG9wdC5ncm91cFwiIFtuZ1ZhbHVlXT1cImNoaWxkLnZhbHVlXCIgW2Rpc2FibGVkXT1cImNoaWxkLmRpc2FibGVkXCI+XG4gICAgICAgICAgICAgICAgICB7eyBjaGlsZC5sYWJlbCB9fVxuICAgICAgICAgICAgICAgIDwvb3B0aW9uPlxuICAgICAgICAgICAgICA8L29wdGdyb3VwPlxuICAgICAgICAgICAgPC9uZy10ZW1wbGF0ZT5cbiAgICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgICAgPC9uZy10ZW1wbGF0ZT5cbiAgICAgIDwvbmctY29udGFpbmVyPlxuICAgIDwvc2VsZWN0PlxuICBgLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlGaWVsZE5hdGl2ZVNlbGVjdCBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIEBWaWV3Q2hpbGQoTWF0SW5wdXQsIDxhbnk+IHsgc3RhdGljOiB0cnVlIH0pIGZvcm1GaWVsZENvbnRyb2whOiBNYXRJbnB1dDtcbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBvcHRpb25zOiBbXSxcbiAgICB9LFxuICB9O1xufVxuIl19