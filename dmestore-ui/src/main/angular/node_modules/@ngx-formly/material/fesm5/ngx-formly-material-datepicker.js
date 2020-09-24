import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { __extends } from 'tslib';
import { Component, ViewChild, NgModule } from '@angular/core';
import { ɵdefineHiddenProp, FormlyModule } from '@ngx-formly/core';
import { FieldType, FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatDatepickerInput, MatDatepickerModule } from '@angular/material/datepicker';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyDatepickerTypeComponent = /** @class */ (function (_super) {
    __extends(FormlyDatepickerTypeComponent, _super);
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
            ɵdefineHiddenProp(_this.field, '_mat' + _this.to.datepickerOptions.datepickerTogglePosition, _this.datepickerToggle);
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

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
var FormlyMatDatepickerModule = /** @class */ (function () {
    function FormlyMatDatepickerModule() {
    }
    FormlyMatDatepickerModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyDatepickerTypeComponent],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatInputModule,
                        MatDatepickerModule,
                        FormlyMatFormFieldModule,
                        FormlyModule.forChild({
                            types: [{
                                    name: 'datepicker',
                                    component: FormlyDatepickerTypeComponent,
                                    wrappers: ['form-field'],
                                }],
                        }),
                    ],
                },] }
    ];
    return FormlyMatDatepickerModule;
}());

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatDatepickerModule, FormlyDatepickerTypeComponent as ɵa };

//# sourceMappingURL=ngx-formly-material-datepicker.js.map