/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatInput } from '@angular/material/input';
var FormlyFieldNativeSelect = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldNativeSelect, _super);
    function FormlyFieldNativeSelect() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                options: [],
            },
        };
        return _this;
    }
    FormlyFieldNativeSelect.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-native-select',
                    template: "\n    <select matNativeControl\n      [id]=\"id\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\">\n      <option *ngIf=\"to.placeholder\" [ngValue]=\"null\">{{ to.placeholder }}</option>\n      <ng-container *ngIf=\"to.options | formlySelectOptions:field | async as opts\">\n        <ng-container *ngIf=\"to._flatOptions else grouplist\">\n          <ng-container *ngFor=\"let opt of opts\">\n            <option [ngValue]=\"opt.value\" [disabled]=\"opt.disabled\">{{ opt.label }}</option>\n          </ng-container>\n        </ng-container>\n\n        <ng-template #grouplist>\n          <ng-container *ngFor=\"let opt of opts\">\n            <option *ngIf=\"!opt.group else optgroup\" [ngValue]=\"opt.value\" [disabled]=\"opt.disabled\">{{ opt.label }}</option>\n            <ng-template #optgroup>\n              <optgroup [label]=\"opt.label\">\n                <option *ngFor=\"let child of opt.group\" [ngValue]=\"child.value\" [disabled]=\"child.disabled\">\n                  {{ child.label }}\n                </option>\n              </optgroup>\n            </ng-template>\n          </ng-container>\n        </ng-template>\n      </ng-container>\n    </select>\n  "
                }] }
    ];
    FormlyFieldNativeSelect.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyFieldNativeSelect;
}(FieldType));
export { FormlyFieldNativeSelect };
if (false) {
    /** @type {?} */
    FormlyFieldNativeSelect.prototype.formFieldControl;
    /** @type {?} */
    FormlyFieldNativeSelect.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibmF0aXZlLXNlbGVjdC50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvbmF0aXZlLXNlbGVjdC8iLCJzb3VyY2VzIjpbIm5hdGl2ZS1zZWxlY3QudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ3JELE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUM1RCxPQUFPLEVBQUUsUUFBUSxFQUFFLE1BQU0seUJBQXlCLENBQUM7QUFFbkQ7SUFrQzZDLG1EQUFTO0lBbEN0RDtRQUFBLHFFQXlDQztRQUxDLG9CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2YsT0FBTyxFQUFFLEVBQUU7YUFDWjtTQUNGLENBQUM7O0lBQ0osQ0FBQzs7Z0JBekNBLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsZ0NBQWdDO29CQUMxQyxRQUFRLEVBQUUseXlDQThCVDtpQkFDRjs7O21DQUVFLFNBQVMsU0FBQyxRQUFRLEVBQUUsbUJBQU0sRUFBRSxNQUFNLEVBQUUsSUFBSSxFQUFFLEVBQUE7O0lBTTdDLDhCQUFDO0NBQUEsQUF6Q0QsQ0FrQzZDLFNBQVMsR0FPckQ7U0FQWSx1QkFBdUI7OztJQUNsQyxtREFBeUU7O0lBQ3pFLGlEQUlFIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0SW5wdXQgfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9pbnB1dCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtbmF0aXZlLXNlbGVjdCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPHNlbGVjdCBtYXROYXRpdmVDb250cm9sXG4gICAgICBbaWRdPVwiaWRcIlxuICAgICAgW3JlYWRvbmx5XT1cInRvLnJlYWRvbmx5XCJcbiAgICAgIFtyZXF1aXJlZF09XCJ0by5yZXF1aXJlZFwiXG4gICAgICBbZXJyb3JTdGF0ZU1hdGNoZXJdPVwiZXJyb3JTdGF0ZU1hdGNoZXJcIlxuICAgICAgW2Zvcm1Db250cm9sXT1cImZvcm1Db250cm9sXCJcbiAgICAgIFtmb3JtbHlBdHRyaWJ1dGVzXT1cImZpZWxkXCI+XG4gICAgICA8b3B0aW9uICpuZ0lmPVwidG8ucGxhY2Vob2xkZXJcIiBbbmdWYWx1ZV09XCJudWxsXCI+e3sgdG8ucGxhY2Vob2xkZXIgfX08L29wdGlvbj5cbiAgICAgIDxuZy1jb250YWluZXIgKm5nSWY9XCJ0by5vcHRpb25zIHwgZm9ybWx5U2VsZWN0T3B0aW9uczpmaWVsZCB8IGFzeW5jIGFzIG9wdHNcIj5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdJZj1cInRvLl9mbGF0T3B0aW9ucyBlbHNlIGdyb3VwbGlzdFwiPlxuICAgICAgICAgIDxuZy1jb250YWluZXIgKm5nRm9yPVwibGV0IG9wdCBvZiBvcHRzXCI+XG4gICAgICAgICAgICA8b3B0aW9uIFtuZ1ZhbHVlXT1cIm9wdC52YWx1ZVwiIFtkaXNhYmxlZF09XCJvcHQuZGlzYWJsZWRcIj57eyBvcHQubGFiZWwgfX08L29wdGlvbj5cbiAgICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgICAgPC9uZy1jb250YWluZXI+XG5cbiAgICAgICAgPG5nLXRlbXBsYXRlICNncm91cGxpc3Q+XG4gICAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdGb3I9XCJsZXQgb3B0IG9mIG9wdHNcIj5cbiAgICAgICAgICAgIDxvcHRpb24gKm5nSWY9XCIhb3B0Lmdyb3VwIGVsc2Ugb3B0Z3JvdXBcIiBbbmdWYWx1ZV09XCJvcHQudmFsdWVcIiBbZGlzYWJsZWRdPVwib3B0LmRpc2FibGVkXCI+e3sgb3B0LmxhYmVsIH19PC9vcHRpb24+XG4gICAgICAgICAgICA8bmctdGVtcGxhdGUgI29wdGdyb3VwPlxuICAgICAgICAgICAgICA8b3B0Z3JvdXAgW2xhYmVsXT1cIm9wdC5sYWJlbFwiPlxuICAgICAgICAgICAgICAgIDxvcHRpb24gKm5nRm9yPVwibGV0IGNoaWxkIG9mIG9wdC5ncm91cFwiIFtuZ1ZhbHVlXT1cImNoaWxkLnZhbHVlXCIgW2Rpc2FibGVkXT1cImNoaWxkLmRpc2FibGVkXCI+XG4gICAgICAgICAgICAgICAgICB7eyBjaGlsZC5sYWJlbCB9fVxuICAgICAgICAgICAgICAgIDwvb3B0aW9uPlxuICAgICAgICAgICAgICA8L29wdGdyb3VwPlxuICAgICAgICAgICAgPC9uZy10ZW1wbGF0ZT5cbiAgICAgICAgICA8L25nLWNvbnRhaW5lcj5cbiAgICAgICAgPC9uZy10ZW1wbGF0ZT5cbiAgICAgIDwvbmctY29udGFpbmVyPlxuICAgIDwvc2VsZWN0PlxuICBgLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlGaWVsZE5hdGl2ZVNlbGVjdCBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIEBWaWV3Q2hpbGQoTWF0SW5wdXQsIDxhbnk+IHsgc3RhdGljOiB0cnVlIH0pIGZvcm1GaWVsZENvbnRyb2whOiBNYXRJbnB1dDtcbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBvcHRpb25zOiBbXSxcbiAgICB9LFxuICB9O1xufVxuIl19