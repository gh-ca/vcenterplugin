/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, ViewChild, Renderer2, ElementRef, ViewContainerRef } from '@angular/core';
import { FieldWrapper, ɵdefineHiddenProp as defineHiddenProp, FormlyConfig } from '@ngx-formly/core';
import { MatFormField } from '@angular/material/form-field';
import { MatFormFieldControl } from '@angular/material/form-field';
import { Subject } from 'rxjs';
import { FocusMonitor } from '@angular/cdk/a11y';
import { FieldType } from './field.type';
/**
 * @record
 */
function MatFormlyFieldConfig() { }
if (false) {
    /** @type {?} */
    MatFormlyFieldConfig.prototype._matprefix;
    /** @type {?} */
    MatFormlyFieldConfig.prototype._matsuffix;
    /** @type {?} */
    MatFormlyFieldConfig.prototype.__formField__;
}
export class FormlyWrapperFormField extends FieldWrapper {
    /**
     * @param {?} config
     * @param {?} renderer
     * @param {?} elementRef
     * @param {?} focusMonitor
     */
    constructor(config, renderer, elementRef, focusMonitor) {
        super();
        this.config = config;
        this.renderer = renderer;
        this.elementRef = elementRef;
        this.focusMonitor = focusMonitor;
        this.stateChanges = new Subject();
        this._errorState = false;
        this.initialGapCalculated = false;
    }
    /**
     * @return {?}
     */
    ngOnInit() {
        this.formField._control = this;
        defineHiddenProp(this.field, '__formField__', this.formField);
        /** @type {?} */
        const ref = this.config.resolveFieldTypeRef(this.formlyField);
        if (ref && !(ref.instance instanceof FieldType)) {
            console.warn(`Component '${ref.componentType.name}' must extend 'FieldType' from '@ngx-formly/material/form-field'.`);
        }
        // fix for https://github.com/angular/material2/issues/11437
        if (this.formlyField.hide && (/** @type {?} */ (this.formlyField.templateOptions)).appearance === 'outline') {
            this.initialGapCalculated = true;
        }
        this.focusMonitor.monitor(this.elementRef, true).subscribe((/**
         * @param {?} origin
         * @return {?}
         */
        origin => {
            if (!origin && this.field.focus) {
                this.field.focus = false;
            }
            this.stateChanges.next();
        }));
    }
    /**
     * @return {?}
     */
    ngAfterContentChecked() {
        if (!this.initialGapCalculated || this.formlyField.hide) {
            return;
        }
        this.formField.updateOutlineGap();
        this.initialGapCalculated = true;
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        // temporary fix for https://github.com/angular/material2/issues/7891
        if (this.formField.appearance !== 'outline' && this.to.hideFieldUnderline === true) {
            /** @type {?} */
            const underlineElement = this.formField._elementRef.nativeElement.querySelector('.mat-form-field-underline');
            underlineElement && this.renderer.removeChild(underlineElement.parentNode, underlineElement);
        }
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        delete this.formlyField.__formField__;
        this.stateChanges.complete();
        this.focusMonitor.stopMonitoring(this.elementRef);
    }
    /**
     * @param {?} ids
     * @return {?}
     */
    setDescribedByIds(ids) { }
    /**
     * @param {?} event
     * @return {?}
     */
    onContainerClick(event) {
        this.formlyField.focus = true;
        this.stateChanges.next();
    }
    /**
     * @return {?}
     */
    get errorState() {
        /** @type {?} */
        const showError = (/** @type {?} */ ((/** @type {?} */ (this.options)).showError))(this);
        if (showError !== this._errorState) {
            this._errorState = showError;
            this.stateChanges.next();
        }
        return showError;
    }
    /**
     * @return {?}
     */
    get controlType() { return this.to.type; }
    /**
     * @return {?}
     */
    get focused() { return !!this.formlyField.focus && !this.disabled; }
    /**
     * @return {?}
     */
    get disabled() { return !!this.to.disabled; }
    /**
     * @return {?}
     */
    get required() { return !!this.to.required; }
    /**
     * @return {?}
     */
    get placeholder() { return this.to.placeholder || ''; }
    /**
     * @return {?}
     */
    get shouldPlaceholderFloat() { return this.shouldLabelFloat; }
    /**
     * @return {?}
     */
    get value() { return this.formControl.value; }
    /**
     * @return {?}
     */
    get ngControl() { return (/** @type {?} */ (this.formControl)); }
    /**
     * @return {?}
     */
    get empty() { return !this.formControl.value; }
    /**
     * @return {?}
     */
    get shouldLabelFloat() { return this.focused || !this.empty; }
    /**
     * @return {?}
     */
    get formlyField() { return (/** @type {?} */ (this.field)); }
}
FormlyWrapperFormField.decorators = [
    { type: Component, args: [{
                selector: 'formly-wrapper-mat-form-field',
                template: `
    <!-- fix https://github.com/angular/material2/pull/7083 by setting width to 100% -->
    <mat-form-field
      [hideRequiredMarker]="true"
      [floatLabel]="to.floatLabel"
      [appearance]="to.appearance"
      [color]="to.color"
      [style.width]="'100%'">
      <ng-container #fieldComponent></ng-container>
      <mat-label *ngIf="to.label && to.hideLabel !== true">
        {{ to.label }}
        <span *ngIf="to.required && to.hideRequiredMarker !== true" class="mat-form-field-required-marker">*</span>
      </mat-label>

      <ng-container matPrefix>
        <ng-container *ngTemplateOutlet="to.prefix ? to.prefix : formlyField._matprefix"></ng-container>
      </ng-container>

      <ng-container matSuffix>
        <ng-container *ngTemplateOutlet="to.suffix ? to.suffix : formlyField._matsuffix"></ng-container>
      </ng-container>

      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->
      <mat-error [id]="null">
        <formly-validation-message [field]="field"></formly-validation-message>
      </mat-error>
      <!-- fix https://github.com/angular/material2/issues/7737 by setting id to null  -->
      <mat-hint *ngIf="to.description" [id]="null">{{ to.description }}</mat-hint>
    </mat-form-field>
  `,
                providers: [{ provide: MatFormFieldControl, useExisting: FormlyWrapperFormField }]
            }] }
];
/** @nocollapse */
FormlyWrapperFormField.ctorParameters = () => [
    { type: FormlyConfig },
    { type: Renderer2 },
    { type: ElementRef },
    { type: FocusMonitor }
];
FormlyWrapperFormField.propDecorators = {
    fieldComponent: [{ type: ViewChild, args: ['fieldComponent', (/** @type {?} */ ({ read: ViewContainerRef, static: true })),] }],
    formField: [{ type: ViewChild, args: [MatFormField, (/** @type {?} */ ({ static: true })),] }]
};
if (false) {
    /** @type {?} */
    FormlyWrapperFormField.prototype.fieldComponent;
    /** @type {?} */
    FormlyWrapperFormField.prototype.formField;
    /** @type {?} */
    FormlyWrapperFormField.prototype.stateChanges;
    /** @type {?} */
    FormlyWrapperFormField.prototype._errorState;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.initialGapCalculated;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.config;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.renderer;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.elementRef;
    /**
     * @type {?}
     * @private
     */
    FormlyWrapperFormField.prototype.focusMonitor;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybS1maWVsZC53cmFwcGVyLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZC8iLCJzb3VyY2VzIjpbImZvcm0tZmllbGQud3JhcHBlci50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQXFCLFNBQVMsRUFBbUQsVUFBVSxFQUFFLGdCQUFnQixFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ2xLLE9BQU8sRUFBRSxZQUFZLEVBQUUsaUJBQWlCLElBQUksZ0JBQWdCLEVBQXFCLFlBQVksRUFBRSxNQUFNLGtCQUFrQixDQUFDO0FBQ3hILE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQztBQUM1RCxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQztBQUNuRSxPQUFPLEVBQUUsT0FBTyxFQUFFLE1BQU0sTUFBTSxDQUFDO0FBQy9CLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxtQkFBbUIsQ0FBQztBQUNqRCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sY0FBYyxDQUFDOzs7O0FBRXpDLG1DQUlDOzs7SUFIQywwQ0FBNkI7O0lBQzdCLDBDQUE2Qjs7SUFDN0IsNkNBQXNDOztBQXFDeEMsTUFBTSxPQUFPLHNCQUF1QixTQUFRLFlBQWtDOzs7Ozs7O0lBVzVFLFlBQ1UsTUFBb0IsRUFDcEIsUUFBbUIsRUFDbkIsVUFBc0IsRUFDdEIsWUFBMEI7UUFFbEMsS0FBSyxFQUFFLENBQUM7UUFMQSxXQUFNLEdBQU4sTUFBTSxDQUFjO1FBQ3BCLGFBQVEsR0FBUixRQUFRLENBQVc7UUFDbkIsZUFBVSxHQUFWLFVBQVUsQ0FBWTtRQUN0QixpQkFBWSxHQUFaLFlBQVksQ0FBYztRQVJwQyxpQkFBWSxHQUFHLElBQUksT0FBTyxFQUFRLENBQUM7UUFDbkMsZ0JBQVcsR0FBRyxLQUFLLENBQUM7UUFDWix5QkFBb0IsR0FBRyxLQUFLLENB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QUFDLENBQUM7OztZQTlIakUsU0FBUyxTQUFDO2dCQUNULFFBQVEsRUFBRSwrQkFBK0I7Z0JBQ3pDLFFBQVEsRUFBRTs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7R0E2QlQ7Z0JBQ0QsU0FBUyxFQUFFLENBQUMsRUFBRSxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsV0FBVyxFQUFFLHNCQUFzQixFQUFFLENBQUM7YUFDbkY7Ozs7WUE5Q2dGLFlBQVk7WUFEM0MsU0FBUztZQUFtRCxVQUFVO1lBSy9HLFlBQVk7Ozs2QkE2Q2xCLFNBQVMsU0FBQyxnQkFBZ0IsRUFBRSxtQkFBSyxFQUFFLElBQUksRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLEVBQUUsSUFBSSxFQUFFLEVBQUE7d0JBR3pFLFNBQVMsU0FBQyxZQUFZLEVBQUUsbUJBQU0sRUFBRSxNQUFNLEVBQUUsSUFBSSxFQUFFLEVBQUE7Ozs7SUFIL0MsZ0RBQThHOztJQUc5RywyQ0FBMEU7O0lBRTFFLDhDQUFtQzs7SUFDbkMsNkNBQW9COzs7OztJQUNwQixzREFBcUM7Ozs7O0lBR25DLHdDQUE0Qjs7Ozs7SUFDNUIsMENBQTJCOzs7OztJQUMzQiw0Q0FBOEI7Ozs7O0lBQzlCLDhDQUFrQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgVmlld0NoaWxkLCBPbkluaXQsIE9uRGVzdHJveSwgUmVuZGVyZXIyLCBBZnRlclZpZXdJbml0LCBBZnRlckNvbnRlbnRDaGVja2VkLCBUZW1wbGF0ZVJlZiwgRWxlbWVudFJlZiwgVmlld0NvbnRhaW5lclJlZiB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRmllbGRXcmFwcGVyLCDJtWRlZmluZUhpZGRlblByb3AgYXMgZGVmaW5lSGlkZGVuUHJvcCwgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seUNvbmZpZyB9IGZyb20gJ0BuZ3gtZm9ybWx5L2NvcmUnO1xuaW1wb3J0IHsgTWF0Rm9ybUZpZWxkIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNYXRGb3JtRmllbGRDb250cm9sIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBGb2N1c01vbml0b3IgfSBmcm9tICdAYW5ndWxhci9jZGsvYTExeSc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICcuL2ZpZWxkLnR5cGUnO1xuXG5pbnRlcmZhY2UgTWF0Rm9ybWx5RmllbGRDb25maWcgZXh0ZW5kcyBGb3JtbHlGaWVsZENvbmZpZyB7XG4gIF9tYXRwcmVmaXg6IFRlbXBsYXRlUmVmPGFueT47XG4gIF9tYXRzdWZmaXg6IFRlbXBsYXRlUmVmPGFueT47XG4gIF9fZm9ybUZpZWxkX186IEZvcm1seVdyYXBwZXJGb3JtRmllbGQ7XG59XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS13cmFwcGVyLW1hdC1mb3JtLWZpZWxkJyxcbiAgdGVtcGxhdGU6IGBcbiAgICA8IS0tIGZpeCBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvcHVsbC83MDgzIGJ5IHNldHRpbmcgd2lkdGggdG8gMTAwJSAtLT5cbiAgICA8bWF0LWZvcm0tZmllbGRcbiAgICAgIFtoaWRlUmVxdWlyZWRNYXJrZXJdPVwidHJ1ZVwiXG4gICAgICBbZmxvYXRMYWJlbF09XCJ0by5mbG9hdExhYmVsXCJcbiAgICAgIFthcHBlYXJhbmNlXT1cInRvLmFwcGVhcmFuY2VcIlxuICAgICAgW2NvbG9yXT1cInRvLmNvbG9yXCJcbiAgICAgIFtzdHlsZS53aWR0aF09XCInMTAwJSdcIj5cbiAgICAgIDxuZy1jb250YWluZXIgI2ZpZWxkQ29tcG9uZW50PjwvbmctY29udGFpbmVyPlxuICAgICAgPG1hdC1sYWJlbCAqbmdJZj1cInRvLmxhYmVsICYmIHRvLmhpZGVMYWJlbCAhPT0gdHJ1ZVwiPlxuICAgICAgICB7eyB0by5sYWJlbCB9fVxuICAgICAgICA8c3BhbiAqbmdJZj1cInRvLnJlcXVpcmVkICYmIHRvLmhpZGVSZXF1aXJlZE1hcmtlciAhPT0gdHJ1ZVwiIGNsYXNzPVwibWF0LWZvcm0tZmllbGQtcmVxdWlyZWQtbWFya2VyXCI+Kjwvc3Bhbj5cbiAgICAgIDwvbWF0LWxhYmVsPlxuXG4gICAgICA8bmctY29udGFpbmVyIG1hdFByZWZpeD5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdUZW1wbGF0ZU91dGxldD1cInRvLnByZWZpeCA/IHRvLnByZWZpeCA6IGZvcm1seUZpZWxkLl9tYXRwcmVmaXhcIj48L25nLWNvbnRhaW5lcj5cbiAgICAgIDwvbmctY29udGFpbmVyPlxuXG4gICAgICA8bmctY29udGFpbmVyIG1hdFN1ZmZpeD5cbiAgICAgICAgPG5nLWNvbnRhaW5lciAqbmdUZW1wbGF0ZU91dGxldD1cInRvLnN1ZmZpeCA/IHRvLnN1ZmZpeCA6IGZvcm1seUZpZWxkLl9tYXRzdWZmaXhcIj48L25nLWNvbnRhaW5lcj5cbiAgICAgIDwvbmctY29udGFpbmVyPlxuXG4gICAgICA8IS0tIGZpeCBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzc3MzcgYnkgc2V0dGluZyBpZCB0byBudWxsICAtLT5cbiAgICAgIDxtYXQtZXJyb3IgW2lkXT1cIm51bGxcIj5cbiAgICAgICAgPGZvcm1seS12YWxpZGF0aW9uLW1lc3NhZ2UgW2ZpZWxkXT1cImZpZWxkXCI+PC9mb3JtbHktdmFsaWRhdGlvbi1tZXNzYWdlPlxuICAgICAgPC9tYXQtZXJyb3I+XG4gICAgICA8IS0tIGZpeCBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzc3MzcgYnkgc2V0dGluZyBpZCB0byBudWxsICAtLT5cbiAgICAgIDxtYXQtaGludCAqbmdJZj1cInRvLmRlc2NyaXB0aW9uXCIgW2lkXT1cIm51bGxcIj57eyB0by5kZXNjcmlwdGlvbiB9fTwvbWF0LWhpbnQ+XG4gICAgPC9tYXQtZm9ybS1maWVsZD5cbiAgYCxcbiAgcHJvdmlkZXJzOiBbeyBwcm92aWRlOiBNYXRGb3JtRmllbGRDb250cm9sLCB1c2VFeGlzdGluZzogRm9ybWx5V3JhcHBlckZvcm1GaWVsZCB9XSxcbn0pXG5leHBvcnQgY2xhc3MgRm9ybWx5V3JhcHBlckZvcm1GaWVsZCBleHRlbmRzIEZpZWxkV3JhcHBlcjxNYXRGb3JtbHlGaWVsZENvbmZpZz4gaW1wbGVtZW50cyBPbkluaXQsIE9uRGVzdHJveSwgTWF0Rm9ybUZpZWxkQ29udHJvbDxhbnk+LCBBZnRlclZpZXdJbml0LCBBZnRlckNvbnRlbnRDaGVja2VkIHtcbiAgLy8gVE9ETzogcmVtb3ZlIGBhbnlgLCBvbmNlIGRyb3BwaW5nIGFuZ3VsYXIgYFY3YCBzdXBwb3J0LlxuICBAVmlld0NoaWxkKCdmaWVsZENvbXBvbmVudCcsIDxhbnk+eyByZWFkOiBWaWV3Q29udGFpbmVyUmVmLCBzdGF0aWM6IHRydWUgfSkgZmllbGRDb21wb25lbnQhOiBWaWV3Q29udGFpbmVyUmVmO1xuXG4gIC8vIFRPRE86IHJlbW92ZSBgYW55YCwgb25jZSBkcm9wcGluZyBhbmd1bGFyIGBWN2Agc3VwcG9ydC5cbiAgQFZpZXdDaGlsZChNYXRGb3JtRmllbGQsIDxhbnk+IHsgc3RhdGljOiB0cnVlIH0pIGZvcm1GaWVsZCE6IE1hdEZvcm1GaWVsZDtcblxuICBzdGF0ZUNoYW5nZXMgPSBuZXcgU3ViamVjdDx2b2lkPigpO1xuICBfZXJyb3JTdGF0ZSA9IGZhbHNlO1xuICBwcml2YXRlIGluaXRpYWxHYXBDYWxjdWxhdGVkID0gZmFsc2U7XG5cbiAgY29uc3RydWN0b3IoXG4gICAgcHJpdmF0ZSBjb25maWc6IEZvcm1seUNvbmZpZyxcbiAgICBwcml2YXRlIHJlbmRlcmVyOiBSZW5kZXJlcjIsXG4gICAgcHJpdmF0ZSBlbGVtZW50UmVmOiBFbGVtZW50UmVmLFxuICAgIHByaXZhdGUgZm9jdXNNb25pdG9yOiBGb2N1c01vbml0b3IsXG4gICkge1xuICAgIHN1cGVyKCk7XG4gIH1cblxuICBuZ09uSW5pdCgpIHtcbiAgICB0aGlzLmZvcm1GaWVsZC5fY29udHJvbCA9IHRoaXM7XG4gICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX19mb3JtRmllbGRfXycsIHRoaXMuZm9ybUZpZWxkKTtcblxuICAgIGNvbnN0IHJlZiA9IHRoaXMuY29uZmlnLnJlc29sdmVGaWVsZFR5cGVSZWYodGhpcy5mb3JtbHlGaWVsZCk7XG4gICAgaWYgKHJlZiAmJiAhKHJlZi5pbnN0YW5jZSBpbnN0YW5jZW9mIEZpZWxkVHlwZSkpIHtcbiAgICAgIGNvbnNvbGUud2FybihgQ29tcG9uZW50ICcke3JlZi5jb21wb25lbnRUeXBlLm5hbWV9JyBtdXN0IGV4dGVuZCAnRmllbGRUeXBlJyBmcm9tICdAbmd4LWZvcm1seS9tYXRlcmlhbC9mb3JtLWZpZWxkJy5gKTtcbiAgICB9XG5cbiAgICAvLyBmaXggZm9yIGh0dHBzOi8vZ2l0aHViLmNvbS9hbmd1bGFyL21hdGVyaWFsMi9pc3N1ZXMvMTE0MzdcbiAgICBpZiAodGhpcy5mb3JtbHlGaWVsZC5oaWRlICYmIHRoaXMuZm9ybWx5RmllbGQudGVtcGxhdGVPcHRpb25zIS5hcHBlYXJhbmNlID09PSAnb3V0bGluZScpIHtcbiAgICAgIHRoaXMuaW5pdGlhbEdhcENhbGN1bGF0ZWQgPSB0cnVlO1xuICAgIH1cblxuICAgIHRoaXMuZm9jdXNNb25pdG9yLm1vbml0b3IodGhpcy5lbGVtZW50UmVmLCB0cnVlKS5zdWJzY3JpYmUob3JpZ2luID0+IHtcbiAgICAgIGlmICghb3JpZ2luICYmIHRoaXMuZmllbGQuZm9jdXMpIHtcbiAgICAgICAgdGhpcy5maWVsZC5mb2N1cyA9IGZhbHNlO1xuICAgICAgfVxuXG4gICAgICB0aGlzLnN0YXRlQ2hhbmdlcy5uZXh0KCk7XG4gICAgfSk7XG4gIH1cblxuICBuZ0FmdGVyQ29udGVudENoZWNrZWQoKSB7XG4gICAgaWYgKCF0aGlzLmluaXRpYWxHYXBDYWxjdWxhdGVkIHx8IHRoaXMuZm9ybWx5RmllbGQuaGlkZSkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIHRoaXMuZm9ybUZpZWxkLnVwZGF0ZU91dGxpbmVHYXAoKTtcbiAgICB0aGlzLmluaXRpYWxHYXBDYWxjdWxhdGVkID0gdHJ1ZTtcbiAgfVxuXG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICAvLyB0ZW1wb3JhcnkgZml4IGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9tYXRlcmlhbDIvaXNzdWVzLzc4OTFcbiAgICBpZiAodGhpcy5mb3JtRmllbGQuYXBwZWFyYW5jZSAhPT0gJ291dGxpbmUnICYmIHRoaXMudG8uaGlkZUZpZWxkVW5kZXJsaW5lID09PSB0cnVlKSB7XG4gICAgICBjb25zdCB1bmRlcmxpbmVFbGVtZW50ID0gdGhpcy5mb3JtRmllbGQuX2VsZW1lbnRSZWYubmF0aXZlRWxlbWVudC5xdWVyeVNlbGVjdG9yKCcubWF0LWZvcm0tZmllbGQtdW5kZXJsaW5lJyk7XG4gICAgICB1bmRlcmxpbmVFbGVtZW50ICYmIHRoaXMucmVuZGVyZXIucmVtb3ZlQ2hpbGQodW5kZXJsaW5lRWxlbWVudC5wYXJlbnROb2RlLCB1bmRlcmxpbmVFbGVtZW50KTtcbiAgICB9XG4gIH1cblxuICBuZ09uRGVzdHJveSgpIHtcbiAgICBkZWxldGUgdGhpcy5mb3JtbHlGaWVsZC5fX2Zvcm1GaWVsZF9fO1xuICAgIHRoaXMuc3RhdGVDaGFuZ2VzLmNvbXBsZXRlKCk7XG4gICAgdGhpcy5mb2N1c01vbml0b3Iuc3RvcE1vbml0b3JpbmcodGhpcy5lbGVtZW50UmVmKTtcbiAgfVxuXG4gIHNldERlc2NyaWJlZEJ5SWRzKGlkczogc3RyaW5nW10pOiB2b2lkIHsgfVxuICBvbkNvbnRhaW5lckNsaWNrKGV2ZW50OiBNb3VzZUV2ZW50KTogdm9pZCB7XG4gICAgdGhpcy5mb3JtbHlGaWVsZC5mb2N1cyA9IHRydWU7XG4gICAgdGhpcy5zdGF0ZUNoYW5nZXMubmV4dCgpO1xuICB9XG5cbiAgZ2V0IGVycm9yU3RhdGUoKSB7XG4gICAgY29uc3Qgc2hvd0Vycm9yID0gdGhpcy5vcHRpb25zIS5zaG93RXJyb3IhKHRoaXMpO1xuICAgIGlmIChzaG93RXJyb3IgIT09IHRoaXMuX2Vycm9yU3RhdGUpIHtcbiAgICAgIHRoaXMuX2Vycm9yU3RhdGUgPSBzaG93RXJyb3I7XG4gICAgICB0aGlzLnN0YXRlQ2hhbmdlcy5uZXh0KCk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHNob3dFcnJvcjtcbiAgfVxuICBnZXQgY29udHJvbFR5cGUoKSB7IHJldHVybiB0aGlzLnRvLnR5cGU7IH1cbiAgZ2V0IGZvY3VzZWQoKSB7IHJldHVybiAhIXRoaXMuZm9ybWx5RmllbGQuZm9jdXMgJiYgIXRoaXMuZGlzYWJsZWQ7IH1cbiAgZ2V0IGRpc2FibGVkKCkgeyByZXR1cm4gISF0aGlzLnRvLmRpc2FibGVkOyB9XG4gIGdldCByZXF1aXJlZCgpIHsgcmV0dXJuICEhdGhpcy50by5yZXF1aXJlZDsgfVxuICBnZXQgcGxhY2Vob2xkZXIoKSB7IHJldHVybiB0aGlzLnRvLnBsYWNlaG9sZGVyIHx8ICcnOyB9XG4gIGdldCBzaG91bGRQbGFjZWhvbGRlckZsb2F0KCkgeyByZXR1cm4gdGhpcy5zaG91bGRMYWJlbEZsb2F0OyB9XG4gIGdldCB2YWx1ZSgpIHsgcmV0dXJuIHRoaXMuZm9ybUNvbnRyb2wudmFsdWU7IH1cbiAgZ2V0IG5nQ29udHJvbCgpIHsgcmV0dXJuIHRoaXMuZm9ybUNvbnRyb2wgYXMgYW55OyB9XG4gIGdldCBlbXB0eSgpIHsgcmV0dXJuICF0aGlzLmZvcm1Db250cm9sLnZhbHVlOyB9XG4gIGdldCBzaG91bGRMYWJlbEZsb2F0KCkgeyByZXR1cm4gdGhpcy5mb2N1c2VkIHx8ICF0aGlzLmVtcHR5OyB9XG5cbiAgZ2V0IGZvcm1seUZpZWxkKCkgeyByZXR1cm4gdGhpcy5maWVsZCBhcyBNYXRGb3JtbHlGaWVsZENvbmZpZzsgfVxufVxuIl19