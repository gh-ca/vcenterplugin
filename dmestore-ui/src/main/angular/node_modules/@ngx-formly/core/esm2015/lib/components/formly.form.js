/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component, Input, Optional, EventEmitter, Output, Attribute, ViewChild, ElementRef, NgZone } from '@angular/core';
import { FormGroup, FormGroupDirective, FormControl } from '@angular/forms';
import { FormlyFormBuilder } from '../services/formly.form.builder';
import { FormlyConfig } from '../services/formly.config';
import { assignFieldValue, isNullOrUndefined, wrapProperty, clone, defineHiddenProp, getKeyPath } from '../utils';
import { Subject } from 'rxjs';
import { debounceTime, switchMap, distinctUntilChanged, take } from 'rxjs/operators';
export class FormlyForm {
    /**
     * @param {?} formlyBuilder
     * @param {?} formlyConfig
     * @param {?} ngZone
     * @param {?} immutable
     * @param {?} parentFormGroup
     */
    constructor(formlyBuilder, formlyConfig, ngZone, 
    // tslint:disable-next-line
    immutable, parentFormGroup) {
        this.formlyBuilder = formlyBuilder;
        this.formlyConfig = formlyConfig;
        this.ngZone = ngZone;
        this.parentFormGroup = parentFormGroup;
        this.modelChange = new EventEmitter();
        this.immutable = false;
        this._modelChangeValue = {};
        this.modelChangeSubs = [];
        this.modelChange$ = new Subject();
        this.modelChangeSub = this.modelChange$.pipe(switchMap((/**
         * @return {?}
         */
        () => this.ngZone.onStable.asObservable().pipe(take(1))))).subscribe((/**
         * @return {?}
         */
        () => this.ngZone.runGuarded((/**
         * @return {?}
         */
        () => {
            // runGuarded is used to keep the expression changes in-sync
            // https://github.com/ngx-formly/ngx-formly/issues/2095
            this.checkExpressionChange();
            this.modelChange.emit(this._modelChangeValue = clone(this.model));
        }))));
        if (immutable !== null) {
            console.warn(`NgxFormly: passing 'immutable' attribute to 'formly-form' component is deprecated since v5.5, enable immutable mode through NgModule declaration instead.`);
        }
        this.immutable = (immutable !== null) || !!formlyConfig.extras.immutable;
    }
    /**
     * @param {?} model
     * @return {?}
     */
    set model(model) { this._model = this.immutable ? clone(model) : model; }
    /**
     * @return {?}
     */
    get model() { return this._model || {}; }
    /**
     * @param {?} fields
     * @return {?}
     */
    set fields(fields) { this._fields = this.immutable ? clone(fields) : fields; }
    /**
     * @return {?}
     */
    get fields() { return this._fields || []; }
    /**
     * @param {?} options
     * @return {?}
     */
    set options(options) { this._options = this.immutable ? clone(options) : options; }
    /**
     * @return {?}
     */
    get options() { return this._options; }
    /**
     * @param {?} content
     * @return {?}
     */
    set content(content) {
        if (content && content.nativeElement.nextSibling) {
            console.warn(`NgxFormly: content projection for 'formly-form' component is deprecated since v5.5, you should avoid passing content inside the 'formly-form' tag.`);
        }
    }
    /**
     * @return {?}
     */
    ngDoCheck() {
        if (this.formlyConfig.extras.checkExpressionOn === 'changeDetectionCheck') {
            this.checkExpressionChange();
        }
    }
    /**
     * @param {?} changes
     * @return {?}
     */
    ngOnChanges(changes) {
        // https://github.com/ngx-formly/ngx-formly/issues/2294
        if (changes.model && this.field) {
            this.field.model = this.model;
        }
        if (changes.fields || changes.form || (changes.model && this._modelChangeValue !== changes.model.currentValue)) {
            this.form = this.form || (new FormGroup({}));
            this.setOptions();
            this.options.updateInitialValue();
            this.clearModelSubscriptions();
            this.formlyBuilder.buildForm(this.form, this.fields, this.model, this.options);
            this.trackModelChanges(this.fields);
        }
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.modelChangeSub.unsubscribe();
        this.clearModelSubscriptions();
    }
    /**
     * @param {?} __0
     * @return {?}
     */
    changeModel({ key, value, field }) {
        assignFieldValue(field, value);
        this.modelChange$.next();
    }
    /**
     * @return {?}
     */
    setOptions() {
        if (!this.options) {
            this.options = {};
        }
        if (!this.options.resetModel) {
            this.options.resetModel = (/**
             * @param {?=} model
             * @return {?}
             */
            (model) => {
                model = clone(isNullOrUndefined(model) ? ((/** @type {?} */ (this.options)))._initialModel : model);
                if (this.model) {
                    Object.keys(this.model).forEach((/**
                     * @param {?} k
                     * @return {?}
                     */
                    k => delete this.model[k]));
                    Object.assign(this.model, model || {});
                }
                ((/** @type {?} */ (this.options)))._buildForm();
                // we should call `NgForm::resetForm` to ensure changing `submitted` state after resetting form
                // but only when the current component is a root one.
                if (this.options.parentForm && this.options.parentForm.control === this.form) {
                    this.options.parentForm.resetForm(model);
                }
                else {
                    this.form.reset(model);
                }
            });
        }
        if (!this.options.parentForm && this.parentFormGroup) {
            defineHiddenProp(this.options, 'parentForm', this.parentFormGroup);
            wrapProperty(this.options.parentForm, 'submitted', (/**
             * @param {?} __0
             * @return {?}
             */
            ({ firstChange }) => {
                if (!firstChange) {
                    this.checkExpressionChange();
                    ((/** @type {?} */ (this.options)))._markForCheck({
                        fieldGroup: this.fields,
                        model: this.model,
                        formControl: this.form,
                        options: this.options,
                    });
                }
            }));
        }
        if (!this.options.updateInitialValue) {
            this.options.updateInitialValue = (/**
             * @return {?}
             */
            () => ((/** @type {?} */ (this.options)))._initialModel = clone(this.model));
        }
        if (!((/** @type {?} */ (this.options)))._buildForm) {
            ((/** @type {?} */ (this.options)))._buildForm = (/**
             * @param {?=} emitModelChange
             * @return {?}
             */
            (emitModelChange = false) => {
                this.clearModelSubscriptions();
                this.formlyBuilder.buildForm(this.form, this.fields, this.model, this.options);
                this.trackModelChanges(this.fields);
                if (emitModelChange) {
                    this.modelChange.emit(this._modelChangeValue = clone(this.model));
                }
            });
        }
    }
    /**
     * @private
     * @return {?}
     */
    checkExpressionChange() {
        if (this.options && ((/** @type {?} */ (this.options)))._checkField) {
            ((/** @type {?} */ (this.options)))._checkField({
                fieldGroup: this.fields,
                model: this.model,
                formControl: this.form,
                options: this.options,
            });
        }
    }
    /**
     * @private
     * @param {?} fields
     * @param {?=} rootKey
     * @return {?}
     */
    trackModelChanges(fields, rootKey = []) {
        fields.forEach((/**
         * @param {?} field
         * @return {?}
         */
        field => {
            if (field.key && !field.fieldGroup) {
                /** @type {?} */
                const control = field.formControl;
                /** @type {?} */
                let valueChanges = control.valueChanges.pipe(distinctUntilChanged());
                const { updateOn, debounce } = field.modelOptions;
                if ((!updateOn || updateOn === 'change') && debounce && debounce.default > 0) {
                    valueChanges = control.valueChanges.pipe(debounceTime(debounce.default));
                }
                this.modelChangeSubs.push(valueChanges.subscribe((/**
                 * @param {?} value
                 * @return {?}
                 */
                (value) => {
                    // workaround for https://github.com/angular/angular/issues/13792
                    if (control instanceof FormControl && control['_fields'] && control['_fields'].length > 1) {
                        control.patchValue(value, { emitEvent: false, onlySelf: true });
                    }
                    if (field.parsers && field.parsers.length > 0) {
                        field.parsers.forEach((/**
                         * @param {?} parserFn
                         * @return {?}
                         */
                        parserFn => value = parserFn(value)));
                    }
                    this.changeModel({ key: [...rootKey, ...getKeyPath(field)].join('.'), value, field });
                })));
                // workaround for v5 (https://github.com/ngx-formly/ngx-formly/issues/2061)
                /** @type {?} */
                const observers = control.valueChanges['observers'];
                if (observers && observers.length > 1) {
                    observers.unshift(observers.pop());
                }
            }
            if (field.fieldGroup && field.fieldGroup.length > 0) {
                this.trackModelChanges(field.fieldGroup, field.key ? [...rootKey, ...getKeyPath(field)] : rootKey);
            }
        }));
    }
    /**
     * @private
     * @return {?}
     */
    clearModelSubscriptions() {
        this.modelChangeSubs.forEach((/**
         * @param {?} sub
         * @return {?}
         */
        sub => sub.unsubscribe()));
        this.modelChangeSubs = [];
    }
    /**
     * @private
     * @return {?}
     */
    get field() {
        return this.fields && this.fields[0] && this.fields[0].parent;
    }
}
FormlyForm.decorators = [
    { type: Component, args: [{
                selector: 'formly-form',
                template: `
    <formly-field *ngFor="let field of fields"
      hide-deprecation
      [form]="field.form"
      [options]="field.options"
      [model]="field.model"
      [field]="field">
    </formly-field>
    <ng-container #content>
      <ng-content></ng-content>
    </ng-container>
  `,
                providers: [FormlyFormBuilder]
            }] }
];
/** @nocollapse */
FormlyForm.ctorParameters = () => [
    { type: FormlyFormBuilder },
    { type: FormlyConfig },
    { type: NgZone },
    { type: undefined, decorators: [{ type: Attribute, args: ['immutable',] }] },
    { type: FormGroupDirective, decorators: [{ type: Optional }] }
];
FormlyForm.propDecorators = {
    form: [{ type: Input }],
    model: [{ type: Input }],
    fields: [{ type: Input }],
    options: [{ type: Input }],
    modelChange: [{ type: Output }],
    content: [{ type: ViewChild, args: ['content',] }]
};
if (false) {
    /** @type {?} */
    FormlyForm.prototype.form;
    /** @type {?} */
    FormlyForm.prototype.modelChange;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.immutable;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype._model;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype._modelChangeValue;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype._fields;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype._options;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.modelChangeSubs;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.modelChange$;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.modelChangeSub;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.formlyBuilder;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.formlyConfig;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.ngZone;
    /**
     * @type {?}
     * @private
     */
    FormlyForm.prototype.parentFormGroup;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZvcm0uanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvbXBvbmVudHMvZm9ybWx5LmZvcm0udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQXNCLEtBQUssRUFBaUIsUUFBUSxFQUFFLFlBQVksRUFBRSxNQUFNLEVBQWEsU0FBUyxFQUFFLFNBQVMsRUFBRSxVQUFVLEVBQUUsTUFBTSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ3pLLE9BQU8sRUFBRSxTQUFTLEVBQWEsa0JBQWtCLEVBQUUsV0FBVyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFFdkYsT0FBTyxFQUFFLGlCQUFpQixFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFDcEUsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLDJCQUEyQixDQUFDO0FBQ3pELE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxpQkFBaUIsRUFBRSxZQUFZLEVBQUUsS0FBSyxFQUFFLGdCQUFnQixFQUFFLFVBQVUsRUFBRSxNQUFNLFVBQVUsQ0FBQztBQUNsSCxPQUFPLEVBQWdCLE9BQU8sRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUM3QyxPQUFPLEVBQUUsWUFBWSxFQUFFLFNBQVMsRUFBRSxvQkFBb0IsRUFBRSxJQUFJLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQWtCckYsTUFBTSxPQUFPLFVBQVU7Ozs7Ozs7O0lBc0NyQixZQUNVLGFBQWdDLEVBQ2hDLFlBQTBCLEVBQzFCLE1BQWM7SUFDdEIsMkJBQTJCO0lBQ0gsU0FBUyxFQUNiLGVBQW1DO1FBTC9DLGtCQUFhLEdBQWIsYUFBYSxDQUFtQjtRQUNoQyxpQkFBWSxHQUFaLFlBQVksQ0FBYztRQUMxQixXQUFNLEdBQU4sTUFBTSxDQUFRO1FBR0Ysb0JBQWUsR0FBZixlQUFlLENBQW9CO1FBN0IvQyxnQkFBVyxHQUFHLElBQUksWUFBWSxFQUFPLENBQUM7UUFPeEMsY0FBUyxHQUFH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O0lBQzFCLDRCQUFvQjs7Ozs7SUFDcEIsdUNBQW9DOzs7OztJQUNwQyw2QkFBcUM7Ozs7O0lBQ3JDLDhCQUFvQzs7Ozs7SUFDcEMscUNBQTZDOzs7OztJQUM3QyxrQ0FBMkM7Ozs7O0lBQzNDLG9DQU9JOzs7OztJQUdGLG1DQUF3Qzs7Ozs7SUFDeEMsa0NBQWtDOzs7OztJQUNsQyw0QkFBc0I7Ozs7O0lBR3RCLHFDQUF1RCIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgRG9DaGVjaywgT25DaGFuZ2VzLCBJbnB1dCwgU2ltcGxlQ2hhbmdlcywgT3B0aW9uYWwsIEV2ZW50RW1pdHRlciwgT3V0cHV0LCBPbkRlc3Ryb3ksIEF0dHJpYnV0ZSwgVmlld0NoaWxkLCBFbGVtZW50UmVmLCBOZ1pvbmUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1Hcm91cCwgRm9ybUFycmF5LCBGb3JtR3JvdXBEaXJlY3RpdmUsIEZvcm1Db250cm9sIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seUZvcm1PcHRpb25zLCBGb3JtbHlGb3JtT3B0aW9uc0NhY2hlIH0gZnJvbSAnLi9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZvcm1CdWlsZGVyIH0gZnJvbSAnLi4vc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlcic7XG5pbXBvcnQgeyBGb3JtbHlDb25maWcgfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IGFzc2lnbkZpZWxkVmFsdWUsIGlzTnVsbE9yVW5kZWZpbmVkLCB3cmFwUHJvcGVydHksIGNsb25lLCBkZWZpbmVIaWRkZW5Qcm9wLCBnZXRLZXlQYXRoIH0gZnJvbSAnLi4vdXRpbHMnO1xuaW1wb3J0IHsgU3Vic2NyaXB0aW9uLCBTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBkZWJvdW5jZVRpbWUsIHN3aXRjaE1hcCwgZGlzdGluY3RVbnRpbENoYW5nZWQsIHRha2UgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1mb3JtJyxcbiAgdGVtcGxhdGU6IGBcbiAgICA8Zm9ybWx5LWZpZWxkICpuZ0Zvcj1cImxldCBmaWVsZCBvZiBmaWVsZHNcIlxuICAgICAgaGlkZS1kZXByZWNhdGlvblxuICAgICAgW2Zvcm1dPVwiZmllbGQuZm9ybVwiXG4gICAgICBbb3B0aW9uc109XCJmaWVsZC5vcHRpb25zXCJcbiAgICAgIFttb2RlbF09XCJmaWVsZC5tb2RlbFwiXG4gICAgICBbZmllbGRdPVwiZmllbGRcIj5cbiAgICA8L2Zvcm1seS1maWVsZD5cbiAgICA8bmctY29udGFpbmVyICNjb250ZW50PlxuICAgICAgPG5nLWNvbnRlbnQ+PC9uZy1jb250ZW50PlxuICAgIDwvbmctY29udGFpbmVyPlxuICBgLFxuICBwcm92aWRlcnM6IFtGb3JtbHlGb3JtQnVpbGRlcl0sXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seUZvcm0gaW1wbGVtZW50cyBEb0NoZWNrLCBPbkNoYW5nZXMsIE9uRGVzdHJveSB7XG4gIEBJbnB1dCgpIGZvcm06IEZvcm1Hcm91cCB8IEZvcm1BcnJheTtcblxuICBASW5wdXQoKVxuICBzZXQgbW9kZWwobW9kZWw6IGFueSkgeyB0aGlzLl9tb2RlbCA9IHRoaXMuaW1tdXRhYmxlID8gY2xvbmUobW9kZWwpIDogbW9kZWw7IH1cbiAgZ2V0IG1vZGVsKCkgeyByZXR1cm4gdGhpcy5fbW9kZWwgfHwge307IH1cblxuICBASW5wdXQoKVxuICBzZXQgZmllbGRzKGZpZWxkczogRm9ybWx5RmllbGRDb25maWdbXSkgeyB0aGlzLl9maWVsZHMgPSB0aGlzLmltbXV0YWJsZSA/IGNsb25lKGZpZWxkcykgOiBmaWVsZHM7IH1cbiAgZ2V0IGZpZWxkcygpIHsgcmV0dXJuIHRoaXMuX2ZpZWxkcyB8fCBbXTsgfVxuXG4gIEBJbnB1dCgpXG4gIHNldCBvcHRpb25zKG9wdGlvbnM6IEZvcm1seUZvcm1PcHRpb25zKSB7IHRoaXMuX29wdGlvbnMgPSB0aGlzLmltbXV0YWJsZSA/IGNsb25lKG9wdGlvbnMpIDogb3B0aW9uczsgfVxuICBnZXQgb3B0aW9ucygpIHsgcmV0dXJuIHRoaXMuX29wdGlvbnM7IH1cblxuICBAT3V0cHV0KCkgbW9kZWxDaGFuZ2UgPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgQFZpZXdDaGlsZCgnY29udGVudCcpIHNldCBjb250ZW50KGNvbnRlbnQ6IEVsZW1lbnRSZWY8SFRNTEVsZW1lbnQ+KSB7XG4gICAgaWYgKGNvbnRlbnQgJiYgY29udGVudC5uYXRpdmVFbGVtZW50Lm5leHRTaWJsaW5nKSB7XG4gICAgICBjb25zb2xlLndhcm4oYE5neEZvcm1seTogY29udGVudCBwcm9qZWN0aW9uIGZvciAnZm9ybWx5LWZvcm0nIGNvbXBvbmVudCBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjUsIHlvdSBzaG91bGQgYXZvaWQgcGFzc2luZyBjb250ZW50IGluc2lkZSB0aGUgJ2Zvcm1seS1mb3JtJyB0YWcuYCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBpbW11dGFibGUgPSBmYWxzZTtcbiAgcHJpdmF0ZSBfbW9kZWw6IGFueTtcbiAgcHJpdmF0ZSBfbW9kZWxDaGFuZ2VWYWx1ZTogYW55ID0ge307XG4gIHByaXZhdGUgX2ZpZWxkczogRm9ybWx5RmllbGRDb25maWdbXTtcbiAgcHJpdmF0ZSBfb3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnM7XG4gIHByaXZhdGUgbW9kZWxDaGFuZ2VTdWJzOiBTdWJzY3JpcHRpb25bXSA9IFtdO1xuICBwcml2YXRlIG1vZGVsQ2hhbmdlJCA9IG5ldyBTdWJqZWN0PHZvaWQ+KCk7XG4gIHByaXZhdGUgbW9kZWxDaGFuZ2VTdWIgPSB0aGlzLm1vZGVsQ2hhbmdlJC5waXBlKFxuICAgIHN3aXRjaE1hcCgoKSA9PiB0aGlzLm5nWm9uZS5vblN0YWJsZS5hc09ic2VydmFibGUoKS5waXBlKHRha2UoMSkpKSxcbiAgKS5zdWJzY3JpYmUoKCkgPT4gdGhpcy5uZ1pvbmUucnVuR3VhcmRlZCgoKSA9PiB7XG4gICAgLy8gcnVuR3VhcmRlZCBpcyB1c2VkIHRvIGtlZXAgdGhlIGV4cHJlc3Npb24gY2hhbmdlcyBpbi1zeW5jXG4gICAgLy8gaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjA5NVxuICAgIHRoaXMuY2hlY2tFeHByZXNzaW9uQ2hhbmdlKCk7XG4gICAgdGhpcy5tb2RlbENoYW5nZS5lbWl0KHRoaXMuX21vZGVsQ2hhbmdlVmFsdWUgPSBjbG9uZSh0aGlzLm1vZGVsKSk7XG4gIH0pKTtcblxuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGZvcm1seUJ1aWxkZXI6IEZvcm1seUZvcm1CdWlsZGVyLFxuICAgIHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcsXG4gICAgcHJpdmF0ZSBuZ1pvbmU6IE5nWm9uZSxcbiAgICAvLyB0c2xpbnQ6ZGlzYWJsZS1uZXh0LWxpbmVcbiAgICBAQXR0cmlidXRlKCdpbW11dGFibGUnKSBpbW11dGFibGUsXG4gICAgQE9wdGlvbmFsKCkgcHJpdmF0ZSBwYXJlbnRGb3JtR3JvdXA6IEZvcm1Hcm91cERpcmVjdGl2ZSxcbiAgKSB7XG4gICAgaWYgKGltbXV0YWJsZSAhPT0gbnVsbCkge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ2ltbXV0YWJsZScgYXR0cmlidXRlIHRvICdmb3JtbHktZm9ybScgY29tcG9uZW50IGlzIGRlcHJlY2F0ZWQgc2luY2UgdjUuNSwgZW5hYmxlIGltbXV0YWJsZSBtb2RlIHRocm91Z2ggTmdNb2R1bGUgZGVjbGFyYXRpb24gaW5zdGVhZC5gKTtcbiAgICB9XG5cbiAgICB0aGlzLmltbXV0YWJsZSA9IChpbW11dGFibGUgIT09IG51bGwpIHx8ICEhZm9ybWx5Q29uZmlnLmV4dHJhcy5pbW11dGFibGU7XG4gIH1cblxuICBuZ0RvQ2hlY2soKSB7XG4gICAgaWYgKHRoaXMuZm9ybWx5Q29uZmlnLmV4dHJhcy5jaGVja0V4cHJlc3Npb25PbiA9PT0gJ2NoYW5nZURldGVjdGlvbkNoZWNrJykge1xuICAgICAgdGhpcy5jaGVja0V4cHJlc3Npb25DaGFuZ2UoKTtcbiAgICB9XG4gIH1cblxuICBuZ09uQ2hhbmdlcyhjaGFuZ2VzOiBTaW1wbGVDaGFuZ2VzKSB7XG4gICAgLy8gaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjI5NFxuICAgIGlmIChjaGFuZ2VzLm1vZGVsICYmIHRoaXMuZmllbGQpIHtcbiAgICAgIHRoaXMuZmllbGQubW9kZWwgPSB0aGlzLm1vZGVsO1xuICAgIH1cblxuICAgIGlmIChjaGFuZ2VzLmZpZWxkcyB8fCBjaGFuZ2VzLmZvcm0gfHwgKGNoYW5nZXMubW9kZWwgJiYgdGhpcy5fbW9kZWxDaGFuZ2VWYWx1ZSAhPT0gY2hhbmdlcy5tb2RlbC5jdXJyZW50VmFsdWUpKSB7XG4gICAgICB0aGlzLmZvcm0gPSB0aGlzLmZvcm0gfHwgKG5ldyBGb3JtR3JvdXAoe30pKTtcbiAgICAgIHRoaXMuc2V0T3B0aW9ucygpO1xuICAgICAgdGhpcy5vcHRpb25zLnVwZGF0ZUluaXRpYWxWYWx1ZSgpO1xuICAgICAgdGhpcy5jbGVhck1vZGVsU3Vic2NyaXB0aW9ucygpO1xuICAgICAgdGhpcy5mb3JtbHlCdWlsZGVyLmJ1aWxkRm9ybSh0aGlzLmZvcm0sIHRoaXMuZmllbGRzLCB0aGlzLm1vZGVsLCB0aGlzLm9wdGlvbnMpO1xuICAgICAgdGhpcy50cmFja01vZGVsQ2hhbmdlcyh0aGlzLmZpZWxkcyk7XG4gICAgfVxuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgdGhpcy5tb2RlbENoYW5nZVN1Yi51bnN1YnNjcmliZSgpO1xuICAgIHRoaXMuY2xlYXJNb2RlbFN1YnNjcmlwdGlvbnMoKTtcbiAgfVxuXG4gIGNoYW5nZU1vZGVsKHsga2V5LCB2YWx1ZSwgZmllbGQgfTogeyBrZXk6IHN0cmluZywgdmFsdWU6IGFueSwgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnIH0pIHtcbiAgICBhc3NpZ25GaWVsZFZhbHVlKGZpZWxkLCB2YWx1ZSk7XG4gICAgdGhpcy5tb2RlbENoYW5nZSQubmV4dCgpO1xuICB9XG5cbiAgc2V0T3B0aW9ucygpIHtcbiAgICBpZiAoIXRoaXMub3B0aW9ucykge1xuICAgICAgdGhpcy5vcHRpb25zID0ge307XG4gICAgfVxuXG4gICAgaWYgKCF0aGlzLm9wdGlvbnMucmVzZXRNb2RlbCkge1xuICAgICAgdGhpcy5vcHRpb25zLnJlc2V0TW9kZWwgPSAobW9kZWwgPzogYW55KSA9PiB7XG4gICAgICAgIG1vZGVsID0gY2xvbmUoaXNOdWxsT3JVbmRlZmluZWQobW9kZWwpID8gKDxGb3JtbHlGb3JtT3B0aW9uc0NhY2hlPiB0aGlzLm9wdGlvbnMpLl9pbml0aWFsTW9kZWwgOiBtb2RlbCk7XG4gICAgICAgIGlmICh0aGlzLm1vZGVsKSB7XG4gICAgICAgICAgT2JqZWN0LmtleXModGhpcy5tb2RlbCkuZm9yRWFjaChrID0+IGRlbGV0ZSB0aGlzLm1vZGVsW2tdKTtcbiAgICAgICAgICBPYmplY3QuYXNzaWduKHRoaXMubW9kZWwsIG1vZGVsIHx8IHt9KTtcbiAgICAgICAgfVxuXG4gICAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fYnVpbGRGb3JtKCk7XG5cbiAgICAgICAgLy8gd2Ugc2hvdWxkIGNhbGwgYE5nRm9ybTo6cmVzZXRGb3JtYCB0byBlbnN1cmUgY2hhbmdpbmcgYHN1Ym1pdHRlZGAgc3RhdGUgYWZ0ZXIgcmVzZXR0aW5nIGZvcm1cbiAgICAgICAgLy8gYnV0IG9ubHkgd2hlbiB0aGUgY3VycmVudCBjb21wb25lbnQgaXMgYSByb290IG9uZS5cbiAgICAgICAgaWYgKHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtICYmIHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtLmNvbnRyb2wgPT09IHRoaXMuZm9ybSkge1xuICAgICAgICAgIHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtLnJlc2V0Rm9ybShtb2RlbCk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgdGhpcy5mb3JtLnJlc2V0KG1vZGVsKTtcbiAgICAgICAgfVxuICAgICAgfTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMub3B0aW9ucy5wYXJlbnRGb3JtICYmIHRoaXMucGFyZW50Rm9ybUdyb3VwKSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKHRoaXMub3B0aW9ucywgJ3BhcmVudEZvcm0nLCB0aGlzLnBhcmVudEZvcm1Hcm91cCk7XG4gICAgICB3cmFwUHJvcGVydHkodGhpcy5vcHRpb25zLnBhcmVudEZvcm0sICdzdWJtaXR0ZWQnLCAoeyBmaXJzdENoYW5nZSB9KSA9PiB7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UpIHtcbiAgICAgICAgICB0aGlzLmNoZWNrRXhwcmVzc2lvbkNoYW5nZSgpO1xuICAgICAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fbWFya0ZvckNoZWNrKHtcbiAgICAgICAgICAgIGZpZWxkR3JvdXA6IHRoaXMuZmllbGRzLFxuICAgICAgICAgICAgbW9kZWw6IHRoaXMubW9kZWwsXG4gICAgICAgICAgICBmb3JtQ29udHJvbDogdGhpcy5mb3JtLFxuICAgICAgICAgICAgb3B0aW9uczogdGhpcy5vcHRpb25zLFxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICB9KTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMub3B0aW9ucy51cGRhdGVJbml0aWFsVmFsdWUpIHtcbiAgICAgIHRoaXMub3B0aW9ucy51cGRhdGVJbml0aWFsVmFsdWUgPSAoKSA9PiAoPEZvcm1seUZvcm1PcHRpb25zQ2FjaGU+IHRoaXMub3B0aW9ucykuX2luaXRpYWxNb2RlbCA9IGNsb25lKHRoaXMubW9kZWwpO1xuICAgIH1cblxuICAgIGlmICghKDxGb3JtbHlGb3JtT3B0aW9uc0NhY2hlPiB0aGlzLm9wdGlvbnMpLl9idWlsZEZvcm0pIHtcbiAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fYnVpbGRGb3JtID0gKGVtaXRNb2RlbENoYW5nZSA9IGZhbHNlKSA9PiB7XG4gICAgICAgIHRoaXMuY2xlYXJNb2RlbFN1YnNjcmlwdGlvbnMoKTtcbiAgICAgICAgdGhpcy5mb3JtbHlCdWlsZGVyLmJ1aWxkRm9ybSh0aGlzLmZvcm0sIHRoaXMuZmllbGRzLCB0aGlzLm1vZGVsLCB0aGlzLm9wdGlvbnMpO1xuICAgICAgICB0aGlzLnRyYWNrTW9kZWxDaGFuZ2VzKHRoaXMuZmllbGRzKTtcblxuICAgICAgICBpZiAoZW1pdE1vZGVsQ2hhbmdlKSB7XG4gICAgICAgICAgdGhpcy5tb2RlbENoYW5nZS5lbWl0KHRoaXMuX21vZGVsQ2hhbmdlVmFsdWUgPSBjbG9uZSh0aGlzLm1vZGVsKSk7XG4gICAgICAgIH1cbiAgICAgIH07XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBjaGVja0V4cHJlc3Npb25DaGFuZ2UoKSB7XG4gICAgaWYgKHRoaXMub3B0aW9ucyAmJiAoPEZvcm1seUZvcm1PcHRpb25zQ2FjaGU+IHRoaXMub3B0aW9ucykuX2NoZWNrRmllbGQpIHtcbiAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fY2hlY2tGaWVsZCh7XG4gICAgICAgIGZpZWxkR3JvdXA6IHRoaXMuZmllbGRzLFxuICAgICAgICBtb2RlbDogdGhpcy5tb2RlbCxcbiAgICAgICAgZm9ybUNvbnRyb2w6IHRoaXMuZm9ybSxcbiAgICAgICAgb3B0aW9uczogdGhpcy5vcHRpb25zLFxuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSB0cmFja01vZGVsQ2hhbmdlcyhmaWVsZHM6IEZvcm1seUZpZWxkQ29uZmlnW10sIHJvb3RLZXk6IHN0cmluZ1tdID0gW10pIHtcbiAgICBmaWVsZHMuZm9yRWFjaChmaWVsZCA9PiB7XG4gICAgICBpZiAoZmllbGQua2V5ICYmICFmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICAgIGNvbnN0IGNvbnRyb2wgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgICAgICAgbGV0IHZhbHVlQ2hhbmdlcyA9IGNvbnRyb2wudmFsdWVDaGFuZ2VzLnBpcGUoZGlzdGluY3RVbnRpbENoYW5nZWQoKSk7XG5cbiAgICAgICAgY29uc3QgeyB1cGRhdGVPbiwgZGVib3VuY2UgfSA9IGZpZWxkLm1vZGVsT3B0aW9ucztcbiAgICAgICAgaWYgKCghdXBkYXRlT24gfHwgdXBkYXRlT24gPT09ICdjaGFuZ2UnKSAmJiBkZWJvdW5jZSAmJiBkZWJvdW5jZS5kZWZhdWx0ID4gMCkge1xuICAgICAgICAgIHZhbHVlQ2hhbmdlcyA9IGNvbnRyb2wudmFsdWVDaGFuZ2VzLnBpcGUoZGVib3VuY2VUaW1lKGRlYm91bmNlLmRlZmF1bHQpKTtcbiAgICAgICAgfVxuXG4gICAgICAgIHRoaXMubW9kZWxDaGFuZ2VTdWJzLnB1c2godmFsdWVDaGFuZ2VzLnN1YnNjcmliZSgodmFsdWUpID0+IHtcbiAgICAgICAgICAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9hbmd1bGFyL2lzc3Vlcy8xMzc5MlxuICAgICAgICAgIGlmIChjb250cm9sIGluc3RhbmNlb2YgRm9ybUNvbnRyb2wgJiYgY29udHJvbFsnX2ZpZWxkcyddICYmIGNvbnRyb2xbJ19maWVsZHMnXS5sZW5ndGggPiAxKSB7XG4gICAgICAgICAgICBjb250cm9sLnBhdGNoVmFsdWUodmFsdWUsIHsgZW1pdEV2ZW50OiBmYWxzZSwgb25seVNlbGY6IHRydWUgfSk7XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgaWYgKGZpZWxkLnBhcnNlcnMgJiYgZmllbGQucGFyc2Vycy5sZW5ndGggPiAwKSB7XG4gICAgICAgICAgICBmaWVsZC5wYXJzZXJzLmZvckVhY2gocGFyc2VyRm4gPT4gdmFsdWUgPSBwYXJzZXJGbih2YWx1ZSkpO1xuICAgICAgICAgIH1cblxuICAgICAgICAgIHRoaXMuY2hhbmdlTW9kZWwoeyBrZXk6IFsuLi5yb290S2V5LCAuLi5nZXRLZXlQYXRoKGZpZWxkKV0uam9pbignLicpLCB2YWx1ZSwgZmllbGQgfSk7XG4gICAgICAgIH0pKTtcblxuICAgICAgICAvLyB3b3JrYXJvdW5kIGZvciB2NSAoaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjA2MSlcbiAgICAgICAgY29uc3Qgb2JzZXJ2ZXJzID0gY29udHJvbC52YWx1ZUNoYW5nZXNbJ29ic2VydmVycyddO1xuICAgICAgICBpZiAob2JzZXJ2ZXJzICYmIG9ic2VydmVycy5sZW5ndGggPiAxKSB7XG4gICAgICAgICAgb2JzZXJ2ZXJzLnVuc2hpZnQob2JzZXJ2ZXJzLnBvcCgpKTtcbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICBpZiAoZmllbGQuZmllbGRHcm91cCAmJiBmaWVsZC5maWVsZEdyb3VwLmxlbmd0aCA+IDApIHtcbiAgICAgICAgdGhpcy50cmFja01vZGVsQ2hhbmdlcyhmaWVsZC5maWVsZEdyb3VwLCBmaWVsZC5rZXkgPyBbLi4ucm9vdEtleSwgLi4uZ2V0S2V5UGF0aChmaWVsZCldIDogcm9vdEtleSk7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICBwcml2YXRlIGNsZWFyTW9kZWxTdWJzY3JpcHRpb25zKCkge1xuICAgIHRoaXMubW9kZWxDaGFuZ2VTdWJzLmZvckVhY2goc3ViID0+IHN1Yi51bnN1YnNjcmliZSgpKTtcbiAgICB0aGlzLm1vZGVsQ2hhbmdlU3VicyA9IFtdO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXQgZmllbGQoKTogYW55IHtcbiAgICByZXR1cm4gdGhpcy5maWVsZHMgJiYgdGhpcy5maWVsZHNbMF0gJiYgdGhpcy5maWVsZHNbMF0ucGFyZW50O1xuICB9XG59XG4iXX0=