/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild, TemplateRef } from '@angular/core';
import { ɵdefineHiddenProp as defineHiddenProp } from '@ngx-formly/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatDatepickerInput } from '@angular/material/datepicker';
var FormlyDatepickerTypeComponent = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyDatepickerTypeComponent, _super);
    function FormlyDatepickerTypeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                datepickerOptions: {
                    startView: 'month',
                    datepickerTogglePosition: 'suffix',
                    dateInput: (/**
                     * @return {?}
                     */
                    function () { }),
                    dateChange: (/**
                     * @return {?}
                     */
                    function () { }),
                    monthSelected: (/**
                     * @return {?}
                     */
                    function () { }),
                    yearSelected: (/**
                     * @return {?}
                     */
                    function () { }),
                },
            },
        };
        return _this;
    }
    /**
     * @return {?}
     */
    FormlyDatepickerTypeComponent.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        _super.prototype.ngAfterViewInit.call(this);
        // temporary fix for https://github.com/angular/material2/issues/6728
        ((/** @type {?} */ (this.datepickerInput)))._formField = this.formField;
        setTimeout((/**
         * @return {?}
         */
        function () {
            defineHiddenProp(_this.field, '_mat' + _this.to.datepickerOptions.datepickerTogglePosition, _this.datepickerToggle);
            ((/** @type {?} */ (_this.options)))._markForCheck(_this.field);
        }));
    };
    FormlyDatepickerTypeComponent.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-datepicker',
                    template: "\n    <input matInput\n      [id]=\"id\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [formControl]=\"formControl\"\n      [matDatepicker]=\"picker\"\n      [matDatepickerFilter]=\"to.datepickerOptions.filter\"\n      [max]=\"to.datepickerOptions.max\"\n      [min]=\"to.datepickerOptions.min\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      (dateInput)=\"to.datepickerOptions.dateInput(field, $event)\"\n      (dateChange)=\"to.datepickerOptions.dateChange(field, $event)\">\n    <ng-template #datepickerToggle>\n      <mat-datepicker-toggle [for]=\"picker\"></mat-datepicker-toggle>\n    </ng-template>\n    <mat-datepicker #picker\n      [color]=\"to.color\"\n      [dateClass]=\"to.datepickerOptions.dateClass\"\n      [disabled]=\"to.datepickerOptions.disabled\"\n      [opened]=\"to.datepickerOptions.opened\"\n      [panelClass]=\"to.datepickerOptions.panelClass\"\n      [startAt]=\"to.datepickerOptions.startAt\"\n      [startView]=\"to.datepickerOptions.startView\"\n      [touchUi]=\"to.datepickerOptions.touchUi\"\n      (monthSelected)=\"to.datepickerOptions.monthSelected(field, $event, picker)\"\n      (yearSelected)=\"to.datepickerOptions.yearSelected(field, $event, picker)\"\n    >\n    </mat-datepicker>\n  "
                }] }
    ];
    FormlyDatepickerTypeComponent.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }],
        datepickerInput: [{ type: ViewChild, args: [MatDatepickerInput,] }],
        datepickerToggle: [{ type: ViewChild, args: ['datepickerToggle',] }]
    };
    return FormlyDatepickerTypeComponent;
}(FieldType));
export { FormlyDatepickerTypeComponent };
if (false) {
    /** @type {?} */
    FormlyDatepickerTypeComponent.prototype.formFieldControl;
    /** @type {?} */
    FormlyDatepickerTypeComponent.prototype.datepickerInput;
    /** @type {?} */
    FormlyDatepickerTypeComponent.prototype.datepickerToggle;
    /** @type {?} */
    FormlyDatepickerTypeComponent.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGF0ZXBpY2tlci50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvZGF0ZXBpY2tlci8iLCJzb3VyY2VzIjpbImRhdGVwaWNrZXIudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFpQixXQUFXLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDakYsT0FBTyxFQUFFLGlCQUFpQixJQUFJLGdCQUFnQixFQUFFLE1BQU0sa0JBQWtCLENBQUM7QUFDekUsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBQzVELE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSx5QkFBeUIsQ0FBQztBQUNuRCxPQUFPLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQztBQUVsRTtJQW9DbUQseURBQVM7SUFwQzVEO1FBQUEscUVBZ0VDO1FBdkJDLG9CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2YsaUJBQWlCLEVBQUU7b0JBQ2pCLFNBQVMsRUFBRSxPQUFPO29CQUNsQix3QkFBd0IsRUFBRSxRQUFRO29CQUNsQyxTQUFTOzs7b0JBQUUsY0FBTyxDQUFDLENBQUE7b0JBQ25CLFVBQVU7OztvQkFBRSxjQUFPLENBQUMsQ0FBQTtvQkFDcEIsYUFBYTs7O29CQUFFLGNBQU8sQ0FBQyxDQUFBO29CQUN2QixZQUFZOzs7b0JBQUUsY0FBTyxDQUFDLENBQUE7aUJBQ3ZCO2FBQ0Y7U0FDRixDQUFDOztJQVlKLENBQUM7Ozs7SUFWQyx1REFBZTs7O0lBQWY7UUFBQSxpQkFTQztRQVJDLGlCQUFNLGVBQWUsV0FBRSxDQUFDO1FBQ3hCLHFFQUFxRTtRQUNyRSxDQUFDLG1CQUFNLElBQUksQ0FBQyxlQUFlLEVBQUEsQ0FBQyxDQUFDLFVBQVUsR0FBRyxJQUFJLENBQUMsU0FBUyxDQUFDO1FBRXpELFVBQVU7OztRQUFDO1lBQ1QsZ0JBQWdCLENBQUMsS0FBSSxDQUFDLEtBQUssRUFBRSxNQUFNLEdBQUcsS0FBSSxDQUFDLEVBQUUsQ0FBQyxpQkFBaUIsQ0FBQyx3QkFBd0IsRUFBRSxLQUFJLENBQUMsZ0JBQWdCLENBQUMsQ0FBQztZQUNqSCxDQUFDLG1CQUFNLEtBQUksQ0FBQyxPQUFPLEVBQUEsQ0FBQyxDQUFDLGFBQWEsQ0FBQyxLQUFJLENBQUMsS0FBSyxDQUFDLENBQUM7UUFDakQsQ0FBQyxFQUFDLENBQUM7SUFDTCxDQUFDOztnQkEvREYsU0FBUyxTQUFDO29CQUNULFFBQVEsRUFBRSw2QkFBNkI7b0JBQ3ZDLFFBQVEsRUFBRSxrMkNBZ0NUO2lCQUNGOzs7bUNBRUUsU0FBUyxTQUFDLFFBQVEsRUFBRSxtQkFBTSxFQUFFLE1BQU0sRUFBRSxJQUFJLEVBQUUsRUFBQTtrQ0FDMUMsU0FBUyxTQUFDLGtCQUFrQjttQ0FDNUIsU0FBUyxTQUFDLGtCQUFrQjs7SUF5Qi9CLG9DQUFDO0NBQUEsQUFoRUQsQ0FvQ21ELFNBQVMsR0E0QjNEO1NBNUJZLDZCQUE2Qjs7O0lBQ3hDLHlEQUF5RTs7SUFDekUsd0RBQXlFOztJQUN6RSx5REFBbUU7O0lBRW5FLHVEQVdFIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQsIEFmdGVyVmlld0luaXQsIFRlbXBsYXRlUmVmIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyDJtWRlZmluZUhpZGRlblByb3AgYXMgZGVmaW5lSGlkZGVuUHJvcCB9IGZyb20gJ0BuZ3gtZm9ybWx5L2NvcmUnO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNYXRJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2lucHV0JztcbmltcG9ydCB7IE1hdERhdGVwaWNrZXJJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2RhdGVwaWNrZXInO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LWRhdGVwaWNrZXInLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxpbnB1dCBtYXRJbnB1dFxuICAgICAgW2lkXT1cImlkXCJcbiAgICAgIFtlcnJvclN0YXRlTWF0Y2hlcl09XCJlcnJvclN0YXRlTWF0Y2hlclwiXG4gICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgW21hdERhdGVwaWNrZXJdPVwicGlja2VyXCJcbiAgICAgIFttYXREYXRlcGlja2VyRmlsdGVyXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLmZpbHRlclwiXG4gICAgICBbbWF4XT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1heFwiXG4gICAgICBbbWluXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1pblwiXG4gICAgICBbZm9ybWx5QXR0cmlidXRlc109XCJmaWVsZFwiXG4gICAgICBbcGxhY2Vob2xkZXJdPVwidG8ucGxhY2Vob2xkZXJcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtyZWFkb25seV09XCJ0by5yZWFkb25seVwiXG4gICAgICBbcmVxdWlyZWRdPVwidG8ucmVxdWlyZWRcIlxuICAgICAgKGRhdGVJbnB1dCk9XCJ0by5kYXRlcGlja2VyT3B0aW9ucy5kYXRlSW5wdXQoZmllbGQsICRldmVudClcIlxuICAgICAgKGRhdGVDaGFuZ2UpPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMuZGF0ZUNoYW5nZShmaWVsZCwgJGV2ZW50KVwiPlxuICAgIDxuZy10ZW1wbGF0ZSAjZGF0ZXBpY2tlclRvZ2dsZT5cbiAgICAgIDxtYXQtZGF0ZXBpY2tlci10b2dnbGUgW2Zvcl09XCJwaWNrZXJcIj48L21hdC1kYXRlcGlja2VyLXRvZ2dsZT5cbiAgICA8L25nLXRlbXBsYXRlPlxuICAgIDxtYXQtZGF0ZXBpY2tlciAjcGlja2VyXG4gICAgICBbY29sb3JdPVwidG8uY29sb3JcIlxuICAgICAgW2RhdGVDbGFzc109XCJ0by5kYXRlcGlja2VyT3B0aW9ucy5kYXRlQ2xhc3NcIlxuICAgICAgW2Rpc2FibGVkXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLmRpc2FibGVkXCJcbiAgICAgIFtvcGVuZWRdPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMub3BlbmVkXCJcbiAgICAgIFtwYW5lbENsYXNzXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnBhbmVsQ2xhc3NcIlxuICAgICAgW3N0YXJ0QXRdPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMuc3RhcnRBdFwiXG4gICAgICBbc3RhcnRWaWV3XT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnN0YXJ0Vmlld1wiXG4gICAgICBbdG91Y2hVaV09XCJ0by5kYXRlcGlja2VyT3B0aW9ucy50b3VjaFVpXCJcbiAgICAgIChtb250aFNlbGVjdGVkKT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1vbnRoU2VsZWN0ZWQoZmllbGQsICRldmVudCwgcGlja2VyKVwiXG4gICAgICAoeWVhclNlbGVjdGVkKT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnllYXJTZWxlY3RlZChmaWVsZCwgJGV2ZW50LCBwaWNrZXIpXCJcbiAgICA+XG4gICAgPC9tYXQtZGF0ZXBpY2tlcj5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RGF0ZXBpY2tlclR5cGVDb21wb25lbnQgZXh0ZW5kcyBGaWVsZFR5cGUgaW1wbGVtZW50cyBBZnRlclZpZXdJbml0IHtcbiAgQFZpZXdDaGlsZChNYXRJbnB1dCwgPGFueT4geyBzdGF0aWM6IHRydWUgfSkgZm9ybUZpZWxkQ29udHJvbCE6IE1hdElucHV0O1xuICBAVmlld0NoaWxkKE1hdERhdGVwaWNrZXJJbnB1dCkgZGF0ZXBpY2tlcklucHV0ITogTWF0RGF0ZXBpY2tlcklucHV0PGFueT47XG4gIEBWaWV3Q2hpbGQoJ2RhdGVwaWNrZXJUb2dnbGUnKSBkYXRlcGlja2VyVG9nZ2xlITogVGVtcGxhdGVSZWY8YW55PjtcblxuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIGRhdGVwaWNrZXJPcHRpb25zOiB7XG4gICAgICAgIHN0YXJ0VmlldzogJ21vbnRoJyxcbiAgICAgICAgZGF0ZXBpY2tlclRvZ2dsZVBvc2l0aW9uOiAnc3VmZml4JyxcbiAgICAgICAgZGF0ZUlucHV0OiAoKSA9PiB7fSxcbiAgICAgICAgZGF0ZUNoYW5nZTogKCkgPT4ge30sXG4gICAgICAgIG1vbnRoU2VsZWN0ZWQ6ICgpID0+IHt9LFxuICAgICAgICB5ZWFyU2VsZWN0ZWQ6ICgpID0+IHt9LFxuICAgICAgfSxcbiAgICB9LFxuICB9O1xuXG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICBzdXBlci5uZ0FmdGVyVmlld0luaXQoKTtcbiAgICAvLyB0ZW1wb3JhcnkgZml4IGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzY3MjhcbiAgICAoPGFueT4gdGhpcy5kYXRlcGlja2VySW5wdXQpLl9mb3JtRmllbGQgPSB0aGlzLmZvcm1GaWVsZDtcblxuICAgIHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX21hdCcgKyB0aGlzLnRvLmRhdGVwaWNrZXJPcHRpb25zLmRhdGVwaWNrZXJUb2dnbGVQb3NpdGlvbiwgdGhpcy5kYXRlcGlja2VyVG9nZ2xlKTtcbiAgICAgICg8YW55PiB0aGlzLm9wdGlvbnMpLl9tYXJrRm9yQ2hlY2sodGhpcy5maWVsZCk7XG4gICAgfSk7XG4gIH1cbn1cbiJdfQ==