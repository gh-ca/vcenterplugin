/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, ViewChild } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatSlideToggle } from '@angular/material/slide-toggle';
var FormlyToggleTypeComponent = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyToggleTypeComponent, _super);
    function FormlyToggleTypeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            templateOptions: {
                hideFieldUnderline: true,
                floatLabel: 'always',
                hideLabel: true,
            },
        };
        return _this;
    }
    /**
     * @param {?} event
     * @return {?}
     */
    FormlyToggleTypeComponent.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.slideToggle.focus();
        _super.prototype.onContainerClick.call(this, event);
    };
    FormlyToggleTypeComponent.decorators = [
        { type: Component, args: [{
                    selector: 'formly-field-mat-toggle',
                    template: "\n    <mat-slide-toggle\n      [id]=\"id\"\n      [formControl]=\"formControl\"\n      [formlyAttributes]=\"field\"\n      [color]=\"to.color\"\n      [tabindex]=\"to.tabindex\"\n      [required]=\"to.required\">\n      {{ to.label }}\n    </mat-slide-toggle>\n  "
                }] }
    ];
    FormlyToggleTypeComponent.propDecorators = {
        slideToggle: [{ type: ViewChild, args: [MatSlideToggle,] }]
    };
    return FormlyToggleTypeComponent;
}(FieldType));
export { FormlyToggleTypeComponent };
if (false) {
    /** @type {?} */
    FormlyToggleTypeComponent.prototype.slideToggle;
    /** @type {?} */
    FormlyToggleTypeComponent.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidG9nZ2xlLnR5cGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9tYXRlcmlhbC90b2dnbGUvIiwic291cmNlcyI6WyJ0b2dnbGUudHlwZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ3JELE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUM1RCxPQUFPLEVBQUUsY0FBYyxFQUFFLE1BQU0sZ0NBQWdDLENBQUM7QUFFaEU7SUFjK0MscURBQVM7SUFkeEQ7UUFBQSxxRUE0QkM7UUFaQyxvQkFBYyxHQUFHO1lBQ2YsZUFBZSxFQUFFO2dCQUNmLGtCQUFrQixFQUFFLElBQUk7Z0JBQ3hCLFVBQVUsRUFBRSxRQUFRO2dCQUNwQixTQUFTLEVBQUUsSUFBSTthQUNoQjtTQUNGLENBQUM7O0lBTUosQ0FBQzs7Ozs7SUFKQyxvREFBZ0I7Ozs7SUFBaEIsVUFBaUIsS0FBaUI7UUFDaEMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxLQUFLLEVBQUUsQ0FBQztRQUN6QixpQkFBTSxnQkFBZ0IsWUFBQyxLQUFLLENBQUMsQ0FBQztJQUNoQyxDQUFDOztnQkEzQkYsU0FBUyxTQUFDO29CQUNULFFBQVEsRUFBRSx5QkFBeUI7b0JBQ25DLFFBQVEsRUFBRSx5UUFVVDtpQkFDRjs7OzhCQUVFLFNBQVMsU0FBQyxjQUFjOztJQWEzQixnQ0FBQztDQUFBLEFBNUJELENBYytDLFNBQVMsR0FjdkQ7U0FkWSx5QkFBeUI7OztJQUNwQyxnREFBd0Q7O0lBQ3hELG1EQU1FIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgQ29tcG9uZW50LCBWaWV3Q2hpbGQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgTWF0U2xpZGVUb2dnbGUgfSBmcm9tICdAYW5ndWxhci9tYXRlcmlhbC9zbGlkZS10b2dnbGUnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktZmllbGQtbWF0LXRvZ2dsZScsXG4gIHRlbXBsYXRlOiBgXG4gICAgPG1hdC1zbGlkZS10b2dnbGVcbiAgICAgIFtpZF09XCJpZFwiXG4gICAgICBbZm9ybUNvbnRyb2xdPVwiZm9ybUNvbnRyb2xcIlxuICAgICAgW2Zvcm1seUF0dHJpYnV0ZXNdPVwiZmllbGRcIlxuICAgICAgW2NvbG9yXT1cInRvLmNvbG9yXCJcbiAgICAgIFt0YWJpbmRleF09XCJ0by50YWJpbmRleFwiXG4gICAgICBbcmVxdWlyZWRdPVwidG8ucmVxdWlyZWRcIj5cbiAgICAgIHt7IHRvLmxhYmVsIH19XG4gICAgPC9tYXQtc2xpZGUtdG9nZ2xlPlxuICBgLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlUb2dnbGVUeXBlQ29tcG9uZW50IGV4dGVuZHMgRmllbGRUeXBlIHtcbiAgQFZpZXdDaGlsZChNYXRTbGlkZVRvZ2dsZSkgc2xpZGVUb2dnbGUhOiBNYXRTbGlkZVRvZ2dsZTtcbiAgZGVmYXVsdE9wdGlvbnMgPSB7XG4gICAgdGVtcGxhdGVPcHRpb25zOiB7XG4gICAgICBoaWRlRmllbGRVbmRlcmxpbmU6IHRydWUsXG4gICAgICBmbG9hdExhYmVsOiAnYWx3YXlzJyxcbiAgICAgIGhpZGVMYWJlbDogdHJ1ZSxcbiAgICB9LFxuICB9O1xuXG4gIG9uQ29udGFpbmVyQ2xpY2soZXZlbnQ6IE1vdXNlRXZlbnQpOiB2b2lkIHtcbiAgICB0aGlzLnNsaWRlVG9nZ2xlLmZvY3VzKCk7XG4gICAgc3VwZXIub25Db250YWluZXJDbGljayhldmVudCk7XG4gIH1cbn1cbiJdfQ==