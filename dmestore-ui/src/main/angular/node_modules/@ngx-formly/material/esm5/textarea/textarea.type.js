/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { MatInput } from '@angular/material/input';
import { FieldType } from '@ngx-formly/material/form-field';
import { MAT_INPUT_VALUE_ACCESSOR } from '@angular/material/input';
var FormlyFieldTextArea = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyFieldTextArea, _super);
    function FormlyFieldTextArea() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                cols: 1,
                rows: 1,
            },
        };
        return _this;
    }
    FormlyFieldTextArea.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-textarea',
                    template: "\n    <textarea matInput\n      [id]=\"id\"\n      [readonly]=\"to.readonly\"\n      [required]=\"to.required\"\n      [formControl]=\"formControl\"\n      [errorStateMatcher]=\"errorStateMatcher\"\n      [cols]=\"to.cols\"\n      [rows]=\"to.rows\"\n      [formlyAttributes]=\"field\"\n      [placeholder]=\"to.placeholder\"\n      [tabindex]=\"to.tabindex\"\n      [cdkTextareaAutosize]=\"to.autosize\"\n      [cdkAutosizeMinRows]=\"to.autosizeMinRows\"\n      [cdkAutosizeMaxRows]=\"to.autosizeMaxRows\"\n      [class.cdk-textarea-autosize]=\"to.autosize\"\n      >\n    </textarea>\n  ",
                    providers: [
                        // fix for https://github.com/ngx-formly/ngx-formly/issues/1688
                        // rely on formControl value instead of elementRef which return empty value in Firefox.
                        { provide: MAT_INPUT_VALUE_ACCESSOR, useExisting: FormlyFieldTextArea },
                    ]
                }] }
    ];
    FormlyFieldTextArea.propDecorators = {
        formFieldControl: [{ type: ViewChild, args: [MatInput, (/** @type {?} */ ({ static: true })),] }]
    };
    return FormlyFieldTextArea;
}(FieldType));
export { FormlyFieldTextArea };
if (false) {
    /** @type {?} */
    FormlyFieldTextArea.prototype.formFieldControl;
    /** @type {?} */
    FormlyFieldTextArea.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidGV4dGFyZWEudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL3RleHRhcmVhLyIsInNvdXJjZXMiOlsidGV4dGFyZWEudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQVUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzdELE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSx5QkFBeUIsQ0FBQztBQUNuRCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDNUQsT0FBTyxFQUFFLHdCQUF3QixFQUFFLE1BQU0seUJBQXlCLENBQUM7QUFFbkU7SUEyQnlDLCtDQUFTO0lBM0JsRDtRQUFBLHFFQW1DQztRQU5DLG9CQUFjLEdBQUc7WUFDZixlQUFlLEVBQUU7Z0JBQ2YsSUFBSSxFQUFFLENBQUM7Z0JBQ1AsSUFBSSxFQUFFLENBQUM7YUFDUjtTQUNGLENBQUM7O0lBQ0osQ0FBQzs7Z0JBbkNBLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsMkJBQTJCO29CQUNyQyxRQUFRLEVBQUUsK2tCQWtCVDtvQkFDRCxTQUFTLEVBQUU7d0JBQ1QsK0RBQStEO3dCQUMvRCx1RkFBdUY7d0JBQ3ZGLEVBQUUsT0FBTyxFQUFFLHdCQUF3QixFQUFFLFdBQVcsRUFBRSxtQkFBbUIsRUFBRTtxQkFDeEU7aUJBQ0Y7OzttQ0FFRSxTQUFTLFNBQUMsUUFBUSxFQUFFLG1CQUFNLEVBQUUsTUFBTSxFQUFFLElBQUksRUFBRSxFQUFBOztJQU83QywwQkFBQztDQUFBLEFBbkNELENBMkJ5QyxTQUFTLEdBUWpEO1NBUlksbUJBQW1COzs7SUFDOUIsK0NBQXlFOztJQUN6RSw2Q0FLRSIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgT25Jbml0LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IE1hdElucHV0IH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvaW5wdXQnO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNQVRfSU5QVVRfVkFMVUVfQUNDRVNTT1IgfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9pbnB1dCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1maWVsZC1tYXQtdGV4dGFyZWEnLFxuICB0ZW1wbGF0ZTogYFxuICAgIDx0ZXh0YXJlYSBtYXRJbnB1dFxuICAgICAgW2lkXT1cImlkXCJcbiAgICAgIFtyZWFkb25seV09XCJ0by5yZWFkb25seVwiXG4gICAgICBbcmVxdWlyZWRdPVwidG8ucmVxdWlyZWRcIlxuICAgICAgW2Zvcm1Db250cm9sXT1cImZvcm1Db250cm9sXCJcbiAgICAgIFtlcnJvclN0YXRlTWF0Y2hlcl09XCJlcnJvclN0YXRlTWF0Y2hlclwiXG4gICAgICBbY29sc109XCJ0by5jb2xzXCJcbiAgICAgIFtyb3dzXT1cInRvLnJvd3NcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW3BsYWNlaG9sZGVyXT1cInRvLnBsYWNlaG9sZGVyXCJcbiAgICAgIFt0YWJpbmRleF09XCJ0by50YWJpbmRleFwiXG4gICAgICBbY2RrVGV4dGFyZWFBdXRvc2l6ZV09XCJ0by5hdXRvc2l6ZVwiXG4gICAgICBbY2RrQXV0b3NpemVNaW5Sb3dzXT1cInRvLmF1dG9zaXplTWluUm93c1wiXG4gICAgICBbY2RrQXV0b3NpemVNYXhSb3dzXT1cInRvLmF1dG9zaXplTWF4Um93c1wiXG4gICAgICBbY2xhc3MuY2RrLXRleHRhcmVhLWF1dG9zaXplXT1cInRvLmF1dG9zaXplXCJcbiAgICAgID5cbiAgICA8L3RleHRhcmVhPlxuICBgLFxuICBwcm92aWRlcnM6IFtcbiAgICAvLyBmaXggZm9yIGh0dHBzOi8vZ2l0aHViLmNvbS9uZ3gtZm9ybWx5L25neC1mb3JtbHkvaXNzdWVzLzE2ODhcbiAgICAvLyByZWx5IG9uIGZvcm1Db250cm9sIHZhbHVlIGluc3RlYWQgb2YgZWxlbWVudFJlZiB3aGljaCByZXR1cm4gZW1wdHkgdmFsdWUgaW4gRmlyZWZveC5cbiAgICB7IHByb3ZpZGU6IE1BVF9JTlBVVF9WQUxVRV9BQ0NFU1NPUiwgdXNlRXhpc3Rpbmc6IEZvcm1seUZpZWxkVGV4dEFyZWEgfSxcbiAgXSxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5RmllbGRUZXh0QXJlYSBleHRlbmRzIEZpZWxkVHlwZSBpbXBsZW1lbnRzIE9uSW5pdCB7XG4gIEBWaWV3Q2hpbGQoTWF0SW5wdXQsIDxhbnk+IHsgc3RhdGljOiB0cnVlIH0pIGZvcm1GaWVsZENvbnRyb2whOiBNYXRJbnB1dDtcbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBjb2xzOiAxLFxuICAgICAgcm93czogMSxcbiAgICB9LFxuICB9O1xufVxuIl19