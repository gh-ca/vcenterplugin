/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatSlider } from '@angular/material/slider';
var FormlySliderTypeComponent = /** @class */ (function (_super) {
    tslib_1.__extends(FormlySliderTypeComponent, _super);
    function FormlySliderTypeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                thumbLabel: false,
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlySliderTypeComponent.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.slider.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    FormlySliderTypeComponent.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-slider',
                    template: "\n    <mat-slider\n      [id]=\"id\"\n      [style.width]=\"'100%'\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [tabindex]=\"to.tabindex\"\n      [color]=\"to.color\"\n      [thumbLabel]=\"to.thumbLabel\"\n      [step]=\"to.step\"\n      [max]=\"to.max\"\n      [min]=\"to.min\">\n    </mat-slider>\n  "
                }] }
    ];
    FormlySliderTypeComponent.propDecorators = {
        slider: [{ type: ViewChild, args: [MatSlider,] }]
    };
    return FormlySliderTypeComponent;
}(FieldType));
export { FormlySliderTypeComponent };
if (false) {
    /** @type {?} */
    FormlySliderTypeComponent.prototype.slider;
    /** @type {?} */
    FormlySliderTypeComponent.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic2xpZGVyLnR5cGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9tYXRlcmlhbC9zbGlkZXIvIiwic291cmNlcyI6WyJzbGlkZXIudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ3JELE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUM1RCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sMEJBQTBCLENBQUM7QUFFckQ7SUFpQitDLHFEQUFTO0lBakJ4RDtRQUFBLHFFQStCQztRQVpDLG9CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2Ysa0JBQWtCLEVBQUUsSUFBSTtnQkFDeEIsVUFBVSxFQUFFLFFBQVE7Z0JBQ3BCLFVBQVUsRUFBRSxLQUFLO2FBQ2xCO1NBQ0YsQ0FBQzs7SUFNSixDQUFDOzs7OztJQUpDLG9EQUFnQjs7OztJQUFoQixVQUFpQixLQUFpQjtRQUNoQyxJQUFJLENBQUMsTUFBTSxDQUFDLEtBQUssRUFBRSxDQUFDO1FBQ3BCLGlCQUFNLGdCQUFnQixZQUFDLEtBQUssQ0FBQyxDQUFDO0lBQ2hDLENBQUM7O2dCQTlCRixTQUFTLFNBQUM7b0JBQ1QsUUFBUSxFQUFFLHlCQUF5QjtvQkFDbkMsUUFBUSxFQUFFLHFWQWFUO2lCQUNGOzs7eUJBRUUsU0FBUyxTQUFDLFNBQVM7O0lBYXRCLGdDQUFDO0NBQUEsQUEvQkQsQ0FpQitDLFNBQVMsR0FjdkQ7U0FkWSx5QkFBeUI7OztJQUNwQywyQ0FBeUM7O0lBQ3pDLG1EQU1FIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0U2xpZGVyIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvc2xpZGVyJztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnZm9ybWx5LWZpZWxkLW1hdC1zbGlkZXInLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxtYXQtc2xpZGVyXG4gICAgICBbaWRdPVwiaWRcIlxuICAgICAgW3N0eWxlLndpZHRoXT1cIicxMDAlJ1wiXG4gICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtjb2xvcl09XCJ0by5jb2xvclwiXG4gICAgICBbdGh1bWJMYWJlbF09XCJ0by50aHVtYkxhYmVsXCJcbiAgICAgIFtzdGVwXT1cInRvLnN0ZXBcIlxuICAgICAgW21heF09XCJ0by5tYXhcIlxuICAgICAgW21pbl09XCJ0by5taW5cIj5cbiAgICA8L21hdC1zbGlkZXI+XG4gIGAsXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seVNsaWRlclR5cGVDb21wb25lbnQgZXh0ZW5kcyBGaWVsZFR5cGUge1xuICBAVmlld0NoaWxkKE1hdFNsaWRlcikgc2xpZGVyITogTWF0U2xpZGVyO1xuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIGhpZGVGaWVsZFVuZGVybGluZTogdHJ1ZSxcbiAgICAgIGZsb2F0TGFiZWw6ICdhbHdheXMnLFxuICAgICAgdGh1bWJMYWJlbDogZmFsc2UsXG4gICAgfSxcbiAgfTtcblxuICBvbkNvbnRhaW5lckNsaWNrKGV2ZW50OiBNb3VzZUV2ZW50KTogdm9pZCB7XG4gICAgdGhpcy5zbGlkZXIuZm9jdXMoKTtcbiAgICBzdXBlci5vbkNvbnRhaW5lckNsaWNrKGV2ZW50KTtcbiAgfVxufVxuIl19