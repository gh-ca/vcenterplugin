import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatFormFieldControl, MatFormFieldModule } from '@angular/material/form-field';
import { FocusMonitor } from '@angular/cdk/a11y';
import { ViewChild, Type, Component, Renderer2, ElementRef, ViewContainerRef, NgModule } from '@angular/core';
import { FieldType, ɵdefineHiddenProp, FieldWrapper, FormlyConfig, FormlyModule } from '@ngx-formly/core';
import { Subject } from 'rxjs';

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyErrorStateMatcher {
    /**
     * @param {?} field
     */
    constructor(field) {
        this.field = field;
    }
    /**
     * @param {?} control
     * @param {?} form
     * @return {?}
     */
    isErrorState(control, form) {
        return this.field && this.field.showError;
    }
}

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @abstract
 * @template F
 */
class FieldType$1 extends FieldType {
    constructor() {
        super(...arguments);
        this.errorStateMatcher = new FormlyErrorStateMatcher(this);
        this.stateChanges = new Subject();
        this._errorState = false;
    }
    /**
     * @return {?}
     */
    get formFieldControl() { return this._control || this; }
    /**
     * @param {?} control
     * @return {?}
     */
    set formFieldControl(control) {
        this._control = control;
        if (this.formField && control !== this.formField._control) {
            this.formField._control = control;
        }
    }
    /**
     * @return {?}
     */
    ngOnInit() {
        if (this.formField) {
            this.formField._control = this.formFieldControl;
        }
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        if (this.matPrefix || this.matSuffix) {
            setTimeout((/**
             * @return {?}
             */
            () => {
                ɵdefineHiddenProp(this.field, '_matprefix', this.matPrefix);
                ɵdefineHiddenProp(this.field, '_matsuffix', this.matSuffix);
                ((/** @type {?} */ (this.options)))._markForCheck(this.field);
            }));
        }
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        if (this.formField) {
            delete this.formField._control;
        }
        this.stateChanges.complete();
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
        this.field.focus = true;
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
    get controlType() {
        if (this.to.type) {
            return this.to.type;
        }
        if (((/** @type {?} */ (this.field.type))) instanceof Type) {
            return (/** @type {?} */ (this.field.type)).constructor.name;
        }
        return (/** @type {?} */ (this.field.type));
    }
    /**
     * @return {?}
     */
    get focused() { return !!this.field.focus && !this.disabled; }
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
     * @param {?} value
     * @return {?}
     */
    set value(value) { this.formControl.patchValue(value); }
    /**
     * @return {?}
     */
    get ngControl() { return (/** @type {?} */ (this.formControl)); }
    /**
     * @return {?}
     */
    get empty() { return this.value === undefined || this.value === null || this.value === ''; }
    /**
     * @return {?}
     */
    get shouldLabelFloat() { return this.focused || !this.empty; }
    /**
     * @return {?}
     */
    get formField() { return this.field ? ((/** @type {?} */ (this.field)))['__formField__'] : null; }
}
FieldType$1.propDecorators = {
    matPrefix: [{ type: ViewChild, args: ['matPrefix',] }],
    matSuffix: [{ type: ViewChild, args: ['matSuffix',] }]
};

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyWrapperFormField extends FieldWrapper {
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
        ɵdefineHiddenProp(this.field, '__formField__', this.formField);
        /** @type {?} */
        const ref = this.config.resolveFieldTypeRef(this.formlyField);
        if (ref && !(ref.instance instanceof FieldType$1)) {
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

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class FormlyMatFormFieldModule {
}
FormlyMatFormFieldModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyWrapperFormField],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatFormFieldModule,
                    FormlyModule.forChild({
                        wrappers: [{
                                name: 'form-field',
                                component: FormlyWrapperFormField,
                            }],
                    }),
                ],
            },] }
];

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

export { FormlyMatFormFieldModule, FieldType$1 as FieldType, FormlyWrapperFormField as ɵa };

//# sourceMappingURL=ngx-formly-material-form-field.js.map