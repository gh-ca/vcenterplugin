/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatRadioGroup } from '@angular/material/radio';
import { ɵwrapProperty as wrapProperty } from '@ngx-formly/core';
var FormlyFieldRadio = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldRadio, _super);
    function FormlyFieldRadio() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                options: [],
                tabindex: -1,
            },
        };
        return _this;
    }
    /**
     * @return {?}
     */
    FormlyFieldRadio.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        this.focusObserver = wrapProperty(this.field, 'focus', (/**
         * @param {?} __0
         * @return {?}
         */
        function (_a) {
            var currentValue = _a.currentValue;
            if (_this.to.tabindex === -1
                && currentValue
                && _this.radioGroup._radios.length > 0) {
                /** @type {?} */
                var radio = _this.radioGroup.selected
                    ? _this.radioGroup.selected
                    : _this.radioGroup._radios.first;
                radio.focus();
            }
        }));
    };
    /**
     * @return {?}
     */
    FormlyFieldRadio.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.focusObserver && this.focusObserver();
    };
    FormlyFieldRadio.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-radio',
                    template: "\n    <mat-radio-group\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [required]=\"to.required\"\n      [tabindex]=\"to.tabindex\">\n      <mat-radio-button *ngFor=\"let option of to.options | formlySelectOptions:field | async; let i = index;\"\n        [id]=\"id + '_' + i\"\n        [color]=\"to.color\"\n        [labelPosition]=\"to.labelPosition\"\n        [value]=\"option.value\">\n        {{ option.label }}\n      </mat-radio-button>\n    </mat-radio-group>\n  "
                }] }
    ];
    FormlyFieldRadio.propDecorators = {
        radioGroup: [{ type: ViewChild, args: [MatRadioGroup,] }]
    };
    return FormlyFieldRadio;
}(FieldType));
export { FormlyFieldRadio };
if (false) {
    /** @type {?} */
    FormlyFieldRadio.prototype.radioGroup;
    /** @type {?} */
    FormlyFieldRadio.prototype.defaultOptions;
    /**
     * @type {?}
     * @private
     */
    FormlyFieldRadio.prototype.focusObserver;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmFkaW8udHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL3JhZGlvLyIsInNvdXJjZXMiOlsicmFkaW8udHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUE0QixNQUFNLGVBQWUsQ0FBQztBQUMvRSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDNUQsT0FBTyxFQUFFLGFBQWEsRUFBRSxNQUFNLHlCQUF5QixDQUFDO0FBQ3hELE9BQU8sRUFBRSxhQUFhLElBQUksWUFBWSxFQUFFLE1BQU0sa0JBQWtCLENBQUM7QUFFakU7SUFrQnNDLDRDQUFTO0lBbEIvQztRQUFBLHFFQWlEQztRQTdCQyxvQkFBYyxHQUFHO1lBQ2YsZUFBZSxFQUFFO2dCQUNmLGtCQUFrQixFQUFFLElBQUk7Z0JBQ3hCLFVBQVUsRUFBRSxRQUFRO2dCQUNwQixPQUFPLEVBQUUsRUFBRTtnQkFDWCxRQUFRLEVBQUUsQ0FBQyxDQUFDO2FBQ2I7U0FDRixDQUFDOztJQXNCSixDQUFDOzs7O0lBbkJDLDBDQUFlOzs7SUFBZjtRQUFBLGlCQWNDO1FBYkMsSUFBSSxDQUFDLGFBQWEsR0FBRyxZQUFZLENBQUMsSUFBSSxDQUFDLEtBQUssRUFBRSxPQUFPOzs7O1FBQUUsVUFBQyxFQUFnQjtnQkFBZCw4QkFBWTtZQUNwRSxJQUNFLEtBQUksQ0FBQyxFQUFFLENBQUMsUUFBUSxLQUFLLENBQUMsQ0FBQzttQkFDcEIsWUFBWTttQkFDWixLQUFJLENBQUMsVUFBVSxDQUFDLE9BQU8sQ0FBQyxNQUFNLEdBQUcsQ0FBQyxFQUNyQzs7b0JBQ00sS0FBSyxHQUFHLEtBQUksQ0FBQyxVQUFVLENBQUMsUUFBUTtvQkFDcEMsQ0FBQyxDQUFDLEtBQUksQ0FBQyxVQUFVLENBQUMsUUFBUTtvQkFDMUIsQ0FBQyxDQUFDLEtBQUksQ0FBQyxVQUFVLENBQUMsT0FBTyxDQUFDLEtBQUs7Z0JBRWpDLEtBQUssQ0FBQyxLQUFLLEVBQUUsQ0FBQzthQUNmO1FBQ0gsQ0FBQyxFQUFDLENBQUM7SUFDTCxDQUFDOzs7O0lBRUQsc0NBQVc7OztJQUFYO1FBQ0UsSUFBSSxDQUFDLGFBQWEsSUFBSSxJQUFJLENBQUMsYUFBYSxFQUFFLENBQUM7SUFDN0MsQ0FBQzs7Z0JBaERGLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsd0JBQXdCO29CQUNsQyxRQUFRLEVBQUUsd2ZBY1Q7aUJBQ0Y7Ozs2QkFFRSxTQUFTLFNBQUMsYUFBYTs7SUE4QjFCLHVCQUFDO0NBQUEsQUFqREQsQ0FrQnNDLFNBQVMsR0ErQjlDO1NBL0JZLGdCQUFnQjs7O0lBQzNCLHNDQUFxRDs7SUFDckQsMENBT0U7Ozs7O0lBRUYseUNBQWlDIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQsIEFmdGVyVmlld0luaXQsIE9uRGVzdHJveSB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNYXRSYWRpb0dyb3VwIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvcmFkaW8nO1xuaW1wb3J0IHsgybV3cmFwUHJvcGVydHkgYXMgd3JhcFByb3BlcnR5IH0gZnJvbSAnQG5neC1mb3JtbHkvY29yZSc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtcmFkaW8nLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxtYXQtcmFkaW8tZ3JvdXBcbiAgICAgIFtmb3JtQ29udHJvbF09XCJmb3JtQ29udHJvbFwiXG4gICAgICBbZm9ybWx5QXR0cmlidXRlc109XCJmaWVsZFwiXG4gICAgICBbcmVxdWlyZWRdPVwidG8ucmVxdWlyZWRcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCI+XG4gICAgICA8bWF0LXJhZGlvLWJ1dHRvbiAqbmdGb3I9XCJsZXQgb3B0aW9uIG9mIHRvLm9wdGlvbnMgfCBmb3JtbHlTZWxlY3RPcHRpb25zOmZpZWxkIHwgYXN5bmM7IGxldCBpID0gaW5kZXg7XCJcbiAgICAgICAgW2lkXT1cImlkICsgJ18nICsgaVwiXG4gICAgICAgIFtjb2xvcl09XCJ0by5jb2xvclwiXG4gICAgICAgIFtsYWJlbFBvc2l0aW9uXT1cInRvLmxhYmVsUG9zaXRpb25cIlxuICAgICAgICBbdmFsdWVdPVwib3B0aW9uLnZhbHVlXCI+XG4gICAgICAgIHt7IG9wdGlvbi5sYWJlbCB9fVxuICAgICAgPC9tYXQtcmFkaW8tYnV0dG9uPlxuICAgIDwvbWF0LXJhZGlvLWdyb3VwPlxuICBgLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlGaWVsZFJhZGlvIGV4dGVuZHMgRmllbGRUeXBlIGltcGxlbWVudHMgQWZ0ZXJWaWV3SW5pdCwgT25EZXN0cm95IHtcbiAgQFZpZXdDaGlsZChNYXRSYWRpb0dyb3VwKSByYWRpb0dyb3VwITogTWF0UmFkaW9Hcm91cDtcbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBoaWRlRmllbGRVbmRlcmxpbmU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIG9wdGlvbnM6IFtdLFxuICAgICAgdGFiaW5kZXg6IC0xLFxuICAgIH0sXG4gIH07XG5cbiAgcHJpdmF0ZSBmb2N1c09ic2VydmVyITogRnVuY3Rpb247XG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICB0aGlzLmZvY3VzT2JzZXJ2ZXIgPSB3cmFwUHJvcGVydHkodGhpcy5maWVsZCwgJ2ZvY3VzJywgKHsgY3VycmVudFZhbHVlIH0pID0+IHtcbiAgICAgIGlmIChcbiAgICAgICAgdGhpcy50by50YWJpbmRleCA9PT0gLTFcbiAgICAgICAgJiYgY3VycmVudFZhbHVlXG4gICAgICAgICYmIHRoaXMucmFkaW9Hcm91cC5fcmFkaW9zLmxlbmd0aCA+IDBcbiAgICAgICkge1xuICAgICAgICBjb25zdCByYWRpbyA9IHRoaXMucmFkaW9Hcm91cC5zZWxlY3RlZFxuICAgICAgICAgID8gdGhpcy5yYWRpb0dyb3VwLnNlbGVjdGVkXG4gICAgICAgICAgOiB0aGlzLnJhZGlvR3JvdXAuX3JhZGlvcy5maXJzdDtcblxuICAgICAgICByYWRpby5mb2N1cygpO1xuICAgICAgfVxuICAgIH0pO1xuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgdGhpcy5mb2N1c09ic2VydmVyICYmIHRoaXMuZm9jdXNPYnNlcnZlcigpO1xuICB9XG59XG4iXX0=