/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChild, TemplateRef } from '@angular/core';
import { ɵdefineHiddenProp as defineHiddenProp } from '@ngx-formly/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatDatepickerInput } from '@angular/material/datepicker';
export class FormlyDatepickerTypeComponent extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            templateOptions: {
                datepickerOptions: {
                    startView: 'month',
                    datepickerTogglePosition: 'suffix',
                    dateInput: (/**
                     * @return {?}
                     */
                    () => { }),
                    dateChange: (/**
                     * @return {?}
                     */
                    () => { }),
                    monthSelected: (/**
                     * @return {?}
                     */
                    () => { }),
                    yearSelected: (/**
                     * @return {?}
                     */
                    () => { }),
                },
            },
        };
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        super.ngAfterViewInit();
        // temporary fix for https://github.com/angular/material2/issues/6728
        ((/** @type {?} */ (this.datepickerInput)))._formField = this.formField;
        setTimeout((/**
         * @return {?}
         */
        () => {
            defineHiddenProp(this.field, '_mat' + this.to.datepickerOptions.datepickerTogglePosition, this.datepickerToggle);
            ((/** @type {?} */ (this.options)))._markForCheck(this.field);
        }));
    }
}
FormlyDatepickerTypeComponent.decorators = [
    { type: Component, args: [{
                selector: 'formly-field-mat-datepicker',
                template: `
    <input matInput
      [id]="id"
      [errorStateMatcher]="errorStateMatcher"
      [formControl]="formControl"
      [matDatepicker]="picker"
      [matDatepickerFilter]="to.datepickerOptions.filter"
      [max]="to.datepickerOptions.max"
      [min]="to.datepickerOptions.min"
      [formlyAttributes]="field"
      [placeholder]="to.placeholder"
      [tabindex]="to.tabindex"
      [readonly]="to.readonly"
      [required]="to.required"
      (dateInput)="to.datepickerOptions.dateInput(field, $event)"
      (dateChange)="to.datepickerOptions.dateChange(field, $event)">
    <ng-template #datepickerToggle>
      <mat-datepicker-toggle [for]="picker"></mat-datepicker-toggle>
    </ng-template>
    <mat-datepicker #picker
      [color]="to.color"
      [dateClass]="to.datepickerOptions.dateClass"
      [disabled]="to.datepickerOptions.disabled"
      [opened]="to.datepickerOptions.opened"
      [panelClass]="to.datepickerOptions.panelClass"
      [startAt]="to.datepickerOptions.startAt"
      [startView]="to.datepickerOptions.startView"
      [touchUi]="to.datepickerOptions.touchUi"
      (monthSelected)="to.datepickerOptions.monthSelected(field, $event, picker)"
      (yearSelected)="to.datepickerOptions.yearSelected(field, $event, picker)"
    >
    </mat-datepicker>
  `
            }] }
];
FormlyDatepickerTypeComponent.propDecorators = {
    formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }],
    datepickerInput: [{ type: ViewChild, args: [MatDatepickerInput,] }],
    datepickerToggle: [{ type: ViewChild, args: ['datepickerToggle',] }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGF0ZXBpY2tlci50eXBlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvZGF0ZXBpY2tlci8iLCJzb3VyY2VzIjpbImRhdGVwaWNrZXIudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQWlCLFdBQVcsRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUNqRixPQUFPLEVBQUUsaUJBQWlCLElBQUksZ0JBQWdCLEVBQUUsTUFBTSxrQkFBa0IsQ0FBQztBQUN6RSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDNUQsT0FBTyxFQUFFLFFBQVEsRUFBRSxNQUFNLHlCQUF5QixDQUFDO0FBQ25ELE9BQU8sRUFBRSxrQkFBa0IsRUFBRSxNQUFNLDhCQUE4QixDQUFDO0FBc0NsRSxNQUFNLE9BQU8sNkJBQThCLFNBQVEsU0FBUztJQXBDNUQ7O1FBeUNFLG1CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2YsaUJBQWlCLEVBQUU7b0JBQ2pCLFNBQVMsRUFBRSxPQUFPO29CQUNsQix3QkFBd0IsRUFBRSxRQUFRO29CQUNsQyxTQUFTOzs7b0JBQUUsR0FBRyxFQUFFLEdBQUUsQ0FBQyxDQUFBO29CQUNuQixVQUFVOzs7b0JBQUUsR0FBRyxFQUFFLEdBQUUsQ0FBQyxDQUFBO29CQUNwQixhQUFhOzs7b0JBQUUsR0FBRyxFQUFFLEdBQUUsQ0FBQyxDQUFBO29CQUN2QixZQUFZOzs7b0JBQUUsR0FBRyxFQUFFLEdBQUUsQ0FBQyxDQUFBO2lCQUN2QjthQUNGO1NBQ0YsQ0FBQztJQVlKLENBQUM7Ozs7SUFWQyxlQUFlO1FBQ2IsS0FBSyxDQUFDLGVBQWUsRUFBRSxDQUFDO1FBQ3hCLHFFQUFxRTtRQUNyRSxDQUFDLG1CQUFNLElBQUksQ0FBQyxlQUFlLEVBQUEsQ0FBQyxDQUFDLFVBQVUsR0FBRyxJQUFJLENBQUMsU0FBUyxDQUFDO1FBRXpELFVBQVU7OztRQUFDLEdBQUcsRUFBRTtZQUNkLGdCQUFnQixDQUFDLElBQUksQ0FBQyxLQUFLLEVBQUUsTUFBTSxHQUFHLElBQUksQ0FBQyxFQUFFLENBQUMsaUJBQWlCLENBQUMsd0JBQXdCLEVBQUUsSUFBSSxDQUFDLGdCQUFnQixDQUFDLENBQUM7WUFDakgsQ0FBQyxtQkFBTSxJQUFJLENBQUMsT0FBTyxFQUFBLENBQUMsQ0FBQyxhQUFhLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxDQUFDO1FBQ2pELENBQUMsRUFBQyxDQUFDO0lBQ0wsQ0FBQzs7O1lBL0RGLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUsNkJBQTZCO2dCQUN2QyxRQUFRLEVBQUU7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7O0dBZ0NUO2FBQ0Y7OzsrQkFFRSxTQUFTLFNBQUMsUUFBUSxFQUFFLG1CQUFNLEVBQUUsTUFBTSxFQUFFLElBQUksRUFBRSxFQUFBOzhCQUMxQyxTQUFTLFNBQUMsa0JBQWtCOytCQUM1QixTQUFTLFNBQUMsa0JBQWtCOzs7O0lBRjdCLHlEQUF5RTs7SUFDekUsd0RBQXlFOztJQUN6RSx5REFBbUU7O0lBRW5FLHVEQVdFIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQsIEFmdGVyVmlld0luaXQsIFRlbXBsYXRlUmVmIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyDJtWRlZmluZUhpZGRlblByb3AgYXMgZGVmaW5lSGlkZGVuUHJvcCB9IGZyb20gJ0BuZ3gtZm9ybWx5L2NvcmUnO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNYXRJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2lucHV0JztcbmltcG9ydCB7IE1hdERhdGVwaWNrZXJJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2RhdGVwaWNrZXInO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LWRhdGVwaWNrZXInLFxuICB0ZW1wbGF0ZTogYFxuICAgIDxpbnB1dCBtYXRJbnB1dFxuICAgICAgW2lkXT1cImlkXCJcbiAgICAgIFtlcnJvclN0YXRlTWF0Y2hlcl09XCJlcnJvclN0YXRlTWF0Y2hlclwiXG4gICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgW21hdERhdGVwaWNrZXJdPVwicGlja2VyXCJcbiAgICAgIFttYXREYXRlcGlja2VyRmlsdGVyXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLmZpbHRlclwiXG4gICAgICBbbWF4XT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1heFwiXG4gICAgICBbbWluXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1pblwiXG4gICAgICBbZm9ybWx5QXR0cmlidXRlc109XCJmaWVsZFwiXG4gICAgICBbcGxhY2Vob2xkZXJdPVwidG8ucGxhY2Vob2xkZXJcIlxuICAgICAgW3RhYmluZGV4XT1cInRvLnRhYmluZGV4XCJcbiAgICAgIFtyZWFkb25seV09XCJ0by5yZWFkb25seVwiXG4gICAgICBbcmVxdWlyZWRdPVwidG8ucmVxdWlyZWRcIlxuICAgICAgKGRhdGVJbnB1dCk9XCJ0by5kYXRlcGlja2VyT3B0aW9ucy5kYXRlSW5wdXQoZmllbGQsICRldmVudClcIlxuICAgICAgKGRhdGVDaGFuZ2UpPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMuZGF0ZUNoYW5nZShmaWVsZCwgJGV2ZW50KVwiPlxuICAgIDxuZy10ZW1wbGF0ZSAjZGF0ZXBpY2tlclRvZ2dsZT5cbiAgICAgIDxtYXQtZGF0ZXBpY2tlci10b2dnbGUgW2Zvcl09XCJwaWNrZXJcIj48L21hdC1kYXRlcGlja2VyLXRvZ2dsZT5cbiAgICA8L25nLXRlbXBsYXRlPlxuICAgIDxtYXQtZGF0ZXBpY2tlciAjcGlja2VyXG4gICAgICBbY29sb3JdPVwidG8uY29sb3JcIlxuICAgICAgW2RhdGVDbGFzc109XCJ0by5kYXRlcGlja2VyT3B0aW9ucy5kYXRlQ2xhc3NcIlxuICAgICAgW2Rpc2FibGVkXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLmRpc2FibGVkXCJcbiAgICAgIFtvcGVuZWRdPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMub3BlbmVkXCJcbiAgICAgIFtwYW5lbENsYXNzXT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnBhbmVsQ2xhc3NcIlxuICAgICAgW3N0YXJ0QXRdPVwidG8uZGF0ZXBpY2tlck9wdGlvbnMuc3RhcnRBdFwiXG4gICAgICBbc3RhcnRWaWV3XT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnN0YXJ0Vmlld1wiXG4gICAgICBbdG91Y2hVaV09XCJ0by5kYXRlcGlja2VyT3B0aW9ucy50b3VjaFVpXCJcbiAgICAgIChtb250aFNlbGVjdGVkKT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLm1vbnRoU2VsZWN0ZWQoZmllbGQsICRldmVudCwgcGlja2VyKVwiXG4gICAgICAoeWVhclNlbGVjdGVkKT1cInRvLmRhdGVwaWNrZXJPcHRpb25zLnllYXJTZWxlY3RlZChmaWVsZCwgJGV2ZW50LCBwaWNrZXIpXCJcbiAgICA+XG4gICAgPC9tYXQtZGF0ZXBpY2tlcj5cbiAgYCxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RGF0ZXBpY2tlclR5cGVDb21wb25lbnQgZXh0ZW5kcyBGaWVsZFR5cGUgaW1wbGVtZW50cyBBZnRlclZpZXdJbml0IHtcbiAgQFZpZXdDaGlsZChNYXRJbnB1dCwgPGFueT4geyBzdGF0aWM6IHRydWUgfSkgZm9ybUZpZWxkQ29udHJvbCE6IE1hdElucHV0O1xuICBAVmlld0NoaWxkKE1hdERhdGVwaWNrZXJJbnB1dCkgZGF0ZXBpY2tlcklucHV0ITogTWF0RGF0ZXBpY2tlcklucHV0PGFueT47XG4gIEBWaWV3Q2hpbGQoJ2RhdGVwaWNrZXJUb2dnbGUnKSBkYXRlcGlja2VyVG9nZ2xlITogVGVtcGxhdGVSZWY8YW55PjtcblxuICBkZWZhdWx0T3B0aW9ucyA9IHtcbiAgICB0ZW1wbGF0ZU9wdGlvbnM6IHtcbiAgICAgIGRhdGVwaWNrZXJPcHRpb25zOiB7XG4gICAgICAgIHN0YXJ0VmlldzogJ21vbnRoJyxcbiAgICAgICAgZGF0ZXBpY2tlclRvZ2dsZVBvc2l0aW9uOiAnc3VmZml4JyxcbiAgICAgICAgZGF0ZUlucHV0OiAoKSA9PiB7fSxcbiAgICAgICAgZGF0ZUNoYW5nZTogKCkgPT4ge30sXG4gICAgICAgIG1vbnRoU2VsZWN0ZWQ6ICgpID0+IHt9LFxuICAgICAgICB5ZWFyU2VsZWN0ZWQ6ICgpID0+IHt9LFxuICAgICAgfSxcbiAgICB9LFxuICB9O1xuXG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICBzdXBlci5uZ0FmdGVyVmlld0luaXQoKTtcbiAgICAvLyB0ZW1wb3JhcnkgZml4IGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzY3MjhcbiAgICAoPGFueT4gdGhpcy5kYXRlcGlja2VySW5wdXQpLl9mb3JtRmllbGQgPSB0aGlzLmZvcm1GaWVsZDtcblxuICAgIHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX21hdCcgKyB0aGlzLnRvLmRhdGVwaWNrZXJPcHRpb25zLmRhdGVwaWNrZXJUb2dnbGVQb3NpdGlvbiwgdGhpcy5kYXRlcGlja2VyVG9nZ2xlKTtcbiAgICAgICg8YW55PiB0aGlzLm9wdGlvbnMpLl9tYXJrRm9yQ2hlY2sodGhpcy5maWVsZCk7XG4gICAgfSk7XG4gIH1cbn1cbiJdfQ==