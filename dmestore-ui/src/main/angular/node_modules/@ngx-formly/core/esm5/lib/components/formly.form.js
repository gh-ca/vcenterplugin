/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component, Input, Optional, EventEmitter, Output, Attribute, ViewChild, ElementRef, NgZone } from '@angular/core';
import { FormGroup, FormGroupDirective, FormControl } from '@angular/forms';
import { FormlyFormBuilder } from '../services/formly.form.builder';
import { FormlyConfig } from '../services/formly.config';
import { assignFieldValue, isNullOrUndefined, wrapProperty, clone, defineHiddenProp, getKeyPath } from '../utils';
import { Subject } from 'rxjs';
import { debounceTime, switchMap, distinctUntilChanged, take } from 'rxjs/operators';
var FormlyForm = /** @class */ (function () {
    function FormlyForm(formlyBuilder, formlyConfig, ngZone, 
    // tslint:disable-next-line
    immutable, parentFormGroup) {
        var _this = this;
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
        function () { return _this.ngZone.onStable.asObservable().pipe(take(1)); }))).subscribe((/**
         * @return {?}
         */
        function () { return _this.ngZone.runGuarded((/**
         * @return {?}
         */
        function () {
            // runGuarded is used to keep the expression changes in-sync
            // https://github.com/ngx-formly/ngx-formly/issues/2095
            _this.checkExpressionChange();
            _this.modelChange.emit(_this._modelChangeValue = clone(_this.model));
        })); }));
        if (immutable !== null) {
            console.warn("NgxFormly: passing 'immutable' attribute to 'formly-form' component is deprecated since v5.5, enable immutable mode through NgModule declaration instead.");
        }
        this.immutable = (immutable !== null) || !!formlyConfig.extras.immutable;
    }
    Object.defineProperty(FormlyForm.prototype, "model", {
        get: /**
         * @return {?}
         */
        function () { return this._model || {}; },
        set: /**
         * @param {?} model
         * @return {?}
         */
        function (model) { this._model = this.immutable ? clone(model) : model; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyForm.prototype, "fields", {
        get: /**
         * @return {?}
         */
        function () { return this._fields || []; },
        set: /**
         * @param {?} fields
         * @return {?}
         */
        function (fields) { this._fields = this.immutable ? clone(fields) : fields; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyForm.prototype, "options", {
        get: /**
         * @return {?}
         */
        function () { return this._options; },
        set: /**
         * @param {?} options
         * @return {?}
         */
        function (options) { this._options = this.immutable ? clone(options) : options; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FormlyForm.prototype, "content", {
        set: /**
         * @param {?} content
         * @return {?}
         */
        function (content) {
            if (content && content.nativeElement.nextSibling) {
                console.warn("NgxFormly: content projection for 'formly-form' component is deprecated since v5.5, you should avoid passing content inside the 'formly-form' tag.");
            }
        },
        enumerable: true,
        configurable: true
    });
    /**
     * @return {?}
     */
    FormlyForm.prototype.ngDoCheck = /**
     * @return {?}
     */
    function () {
        if (this.formlyConfig.extras.checkExpressionOn === 'changeDetectionCheck') {
            this.checkExpressionChange();
        }
    };
    /**
     * @param {?} changes
     * @return {?}
     */
    FormlyForm.prototype.ngOnChanges = /**
     * @param {?} changes
     * @return {?}
     */
    function (changes) {
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
    };
    /**
     * @return {?}
     */
    FormlyForm.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.modelChangeSub.unsubscribe();
        this.clearModelSubscriptions();
    };
    /**
     * @param {?} __0
     * @return {?}
     */
    FormlyForm.prototype.changeModel = /**
     * @param {?} __0
     * @return {?}
     */
    function (_a) {
        var key = _a.key, value = _a.value, field = _a.field;
        assignFieldValue(field, value);
        this.modelChange$.next();
    };
    /**
     * @return {?}
     */
    FormlyForm.prototype.setOptions = /**
     * @return {?}
     */
    function () {
        var _this = this;
        if (!this.options) {
            this.options = {};
        }
        if (!this.options.resetModel) {
            this.options.resetModel = (/**
             * @param {?=} model
             * @return {?}
             */
            function (model) {
                model = clone(isNullOrUndefined(model) ? ((/** @type {?} */ (_this.options)))._initialModel : model);
                if (_this.model) {
                    Object.keys(_this.model).forEach((/**
                     * @param {?} k
                     * @return {?}
                     */
                    function (k) { return delete _this.model[k]; }));
                    Object.assign(_this.model, model || {});
                }
                ((/** @type {?} */ (_this.options)))._buildForm();
                // we should call `NgForm::resetForm` to ensure changing `submitted` state after resetting form
                // but only when the current component is a root one.
                if (_this.options.parentForm && _this.options.parentForm.control === _this.form) {
                    _this.options.parentForm.resetForm(model);
                }
                else {
                    _this.form.reset(model);
                }
            });
        }
        if (!this.options.parentForm && this.parentFormGroup) {
            defineHiddenProp(this.options, 'parentForm', this.parentFormGroup);
            wrapProperty(this.options.parentForm, 'submitted', (/**
             * @param {?} __0
             * @return {?}
             */
            function (_a) {
                var firstChange = _a.firstChange;
                if (!firstChange) {
                    _this.checkExpressionChange();
                    ((/** @type {?} */ (_this.options)))._markForCheck({
                        fieldGroup: _this.fields,
                        model: _this.model,
                        formControl: _this.form,
                        options: _this.options,
                    });
                }
            }));
        }
        if (!this.options.updateInitialValue) {
            this.options.updateInitialValue = (/**
             * @return {?}
             */
            function () { return ((/** @type {?} */ (_this.options)))._initialModel = clone(_this.model); });
        }
        if (!((/** @type {?} */ (this.options)))._buildForm) {
            ((/** @type {?} */ (this.options)))._buildForm = (/**
             * @param {?=} emitModelChange
             * @return {?}
             */
            function (emitModelChange) {
                if (emitModelChange === void 0) { emitModelChange = false; }
                _this.clearModelSubscriptions();
                _this.formlyBuilder.buildForm(_this.form, _this.fields, _this.model, _this.options);
                _this.trackModelChanges(_this.fields);
                if (emitModelChange) {
                    _this.modelChange.emit(_this._modelChangeValue = clone(_this.model));
                }
            });
        }
    };
    /**
     * @private
     * @return {?}
     */
    FormlyForm.prototype.checkExpressionChange = /**
     * @private
     * @return {?}
     */
    function () {
        if (this.options && ((/** @type {?} */ (this.options)))._checkField) {
            ((/** @type {?} */ (this.options)))._checkField({
                fieldGroup: this.fields,
                model: this.model,
                formControl: this.form,
                options: this.options,
            });
        }
    };
    /**
     * @private
     * @param {?} fields
     * @param {?=} rootKey
     * @return {?}
     */
    FormlyForm.prototype.trackModelChanges = /**
     * @private
     * @param {?} fields
     * @param {?=} rootKey
     * @return {?}
     */
    function (fields, rootKey) {
        var _this = this;
        if (rootKey === void 0) { rootKey = []; }
        fields.forEach((/**
         * @param {?} field
         * @return {?}
         */
        function (field) {
            if (field.key && !field.fieldGroup) {
                /** @type {?} */
                var control_1 = field.formControl;
                /** @type {?} */
                var valueChanges = control_1.valueChanges.pipe(distinctUntilChanged());
                var _a = field.modelOptions, updateOn = _a.updateOn, debounce = _a.debounce;
                if ((!updateOn || updateOn === 'change') && debounce && debounce.default > 0) {
                    valueChanges = control_1.valueChanges.pipe(debounceTime(debounce.default));
                }
                _this.modelChangeSubs.push(valueChanges.subscribe((/**
                 * @param {?} value
                 * @return {?}
                 */
                function (value) {
                    // workaround for https://github.com/angular/angular/issues/13792
                    if (control_1 instanceof FormControl && control_1['_fields'] && control_1['_fields'].length > 1) {
                        control_1.patchValue(value, { emitEvent: false, onlySelf: true });
                    }
                    if (field.parsers && field.parsers.length > 0) {
                        field.parsers.forEach((/**
                         * @param {?} parserFn
                         * @return {?}
                         */
                        function (parserFn) { return value = parserFn(value); }));
                    }
                    _this.changeModel({ key: tslib_1.__spread(rootKey, getKeyPath(field)).join('.'), value: value, field: field });
                })));
                // workaround for v5 (https://github.com/ngx-formly/ngx-formly/issues/2061)
                /** @type {?} */
                var observers = control_1.valueChanges['observers'];
                if (observers && observers.length > 1) {
                    observers.unshift(observers.pop());
                }
            }
            if (field.fieldGroup && field.fieldGroup.length > 0) {
                _this.trackModelChanges(field.fieldGroup, field.key ? tslib_1.__spread(rootKey, getKeyPath(field)) : rootKey);
            }
        }));
    };
    /**
     * @private
     * @return {?}
     */
    FormlyForm.prototype.clearModelSubscriptions = /**
     * @private
     * @return {?}
     */
    function () {
        this.modelChangeSubs.forEach((/**
         * @param {?} sub
         * @return {?}
         */
        function (sub) { return sub.unsubscribe(); }));
        this.modelChangeSubs = [];
    };
    Object.defineProperty(FormlyForm.prototype, "field", {
        get: /**
         * @private
         * @return {?}
         */
        function () {
            return this.fields && this.fields[0] && this.fields[0].parent;
        },
        enumerable: true,
        configurable: true
    });
    FormlyForm.decorators = [
        { type: Component, args: [{
                    selector: 'formly-form',
                    template: "\n    <formly-field *ngFor=\"let field of fields\"\n      hide-deprecation\n      [form]=\"field.form\"\n      [options]=\"field.options\"\n      [model]=\"field.model\"\n      [field]=\"field\">\n    </formly-field>\n    <ng-container #content>\n      <ng-content></ng-content>\n    </ng-container>\n  ",
                    providers: [FormlyFormBuilder]
                }] }
    ];
    /** @nocollapse */
    FormlyForm.ctorParameters = function () { return [
        { type: FormlyFormBuilder },
        { type: FormlyConfig },
        { type: NgZone },
        { type: undefined, decorators: [{ type: Attribute, args: ['immutable',] }] },
        { type: FormGroupDirective, decorators: [{ type: Optional }] }
    ]; };
    FormlyForm.propDecorators = {
        form: [{ type: Input }],
        model: [{ type: Input }],
        fields: [{ type: Input }],
        options: [{ type: Input }],
        modelChange: [{ type: Output }],
        content: [{ type: ViewChild, args: ['content',] }]
    };
    return FormlyForm;
}());
export { FormlyForm };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZvcm0uanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvbXBvbmVudHMvZm9ybWx5LmZvcm0udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFzQixLQUFLLEVBQWlCLFFBQVEsRUFBRSxZQUFZLEVBQUUsTUFBTSxFQUFhLFNBQVMsRUFBRSxTQUFTLEVBQUUsVUFBVSxFQUFFLE1BQU0sRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUN6SyxPQUFPLEVBQUUsU0FBUyxFQUFhLGtCQUFrQixFQUFFLFdBQVcsRUFBRSxNQUFNLGdCQUFnQixDQUFDO0FBRXZGLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxNQUFNLGlDQUFpQyxDQUFDO0FBQ3BFLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSwyQkFBMkIsQ0FBQztBQUN6RCxPQUFPLEVBQUUsZ0JBQWdCLEVBQUUsaUJBQWlCLEVBQUUsWUFBWSxFQUFFLEtBQUssRUFBRSxnQkFBZ0IsRUFBRSxVQUFVLEVBQUUsTUFBTSxVQUFVLENBQUM7QUFDbEgsT0FBTyxFQUFnQixPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFDN0MsT0FBTyxFQUFFLFlBQVksRUFBRSxTQUFTLEVBQUUsb0JBQW9CLEVBQUUsSUFBSSxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFFckY7SUFzREUsb0JBQ1UsYUFBZ0MsRUFDaEMsWUFBMEIsRUFDMUIsTUFBYztJQUN0QiwyQkFBMkI7SUFDSCxTQUFTLEVBQ2IsZUFBbUM7UUFOekQsaUJBYUM7UUFaUyxrQkFBYSxHQUFiLGFBQWEsQ0FBbUI7UUFDaEMsaUJBQVksR0FBWixZQUFZLENBQWM7UUFDMUIsV0FBTSxHQUFOLE1BQU0sQ0FBUTtRQUdGLG9CQUFlLEdBQWYsZUFBZSxDQUFvQjtRQTdCL0MsZ0JBQVcsR0FBRyxJQUFJLFlBQVksRUFBTyxDQUFDO1FBT3hDLGNBQVMsR0FBRyxLQUFLLENBQUM7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QyxRQUFROzs7dUJBM0NWLEtBQUs7d0JBRUwsS0FBSzt5QkFJTCxLQUFLOzBCQUlMLEtBQUs7OEJBSUwsTUFBTTswQkFDTixTQUFTLFNBQUMsU0FBUzs7SUFzTHRCLGlCQUFDO0NBQUEsQUF0TkQsSUFzTkM7U0F0TVksVUFBVTs7O0lBQ3JCLDBCQUFxQzs7SUFjckMsaUNBQWdEOzs7OztJQU9oRCwrQkFBMEI7Ozs7O0lBQzFCLDRCQUFvQjs7Ozs7SUFDcEIsdUNBQW9DOzs7OztJQUNwQyw2QkFBcUM7Ozs7O0lBQ3JDLDhCQUFvQzs7Ozs7SUFDcEMscUNBQTZDOzs7OztJQUM3QyxrQ0FBMkM7Ozs7O0lBQzNDLG9DQU9JOzs7OztJQUdGLG1DQUF3Qzs7Ozs7SUFDeEMsa0NBQWtDOzs7OztJQUNsQyw0QkFBc0I7Ozs7O0lBR3RCLHFDQUF1RCIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCwgRG9DaGVjaywgT25DaGFuZ2VzLCBJbnB1dCwgU2ltcGxlQ2hhbmdlcywgT3B0aW9uYWwsIEV2ZW50RW1pdHRlciwgT3V0cHV0LCBPbkRlc3Ryb3ksIEF0dHJpYnV0ZSwgVmlld0NoaWxkLCBFbGVtZW50UmVmLCBOZ1pvbmUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1Hcm91cCwgRm9ybUFycmF5LCBGb3JtR3JvdXBEaXJlY3RpdmUsIEZvcm1Db250cm9sIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcsIEZvcm1seUZvcm1PcHRpb25zLCBGb3JtbHlGb3JtT3B0aW9uc0NhY2hlIH0gZnJvbSAnLi9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZvcm1CdWlsZGVyIH0gZnJvbSAnLi4vc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlcic7XG5pbXBvcnQgeyBGb3JtbHlDb25maWcgfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IGFzc2lnbkZpZWxkVmFsdWUsIGlzTnVsbE9yVW5kZWZpbmVkLCB3cmFwUHJvcGVydHksIGNsb25lLCBkZWZpbmVIaWRkZW5Qcm9wLCBnZXRLZXlQYXRoIH0gZnJvbSAnLi4vdXRpbHMnO1xuaW1wb3J0IHsgU3Vic2NyaXB0aW9uLCBTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBkZWJvdW5jZVRpbWUsIHN3aXRjaE1hcCwgZGlzdGluY3RVbnRpbENoYW5nZWQsIHRha2UgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1mb3JtJyxcbiAgdGVtcGxhdGU6IGBcbiAgICA8Zm9ybWx5LWZpZWxkICpuZ0Zvcj1cImxldCBmaWVsZCBvZiBmaWVsZHNcIlxuICAgICAgaGlkZS1kZXByZWNhdGlvblxuICAgICAgW2Zvcm1dPVwiZmllbGQuZm9ybVwiXG4gICAgICBbb3B0aW9uc109XCJmaWVsZC5vcHRpb25zXCJcbiAgICAgIFttb2RlbF09XCJmaWVsZC5tb2RlbFwiXG4gICAgICBbZmllbGRdPVwiZmllbGRcIj5cbiAgICA8L2Zvcm1seS1maWVsZD5cbiAgICA8bmctY29udGFpbmVyICNjb250ZW50PlxuICAgICAgPG5nLWNvbnRlbnQ+PC9uZy1jb250ZW50PlxuICAgIDwvbmctY29udGFpbmVyPlxuICBgLFxuICBwcm92aWRlcnM6IFtGb3JtbHlGb3JtQnVpbGRlcl0sXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seUZvcm0gaW1wbGVtZW50cyBEb0NoZWNrLCBPbkNoYW5nZXMsIE9uRGVzdHJveSB7XG4gIEBJbnB1dCgpIGZvcm06IEZvcm1Hcm91cCB8IEZvcm1BcnJheTtcblxuICBASW5wdXQoKVxuICBzZXQgbW9kZWwobW9kZWw6IGFueSkgeyB0aGlzLl9tb2RlbCA9IHRoaXMuaW1tdXRhYmxlID8gY2xvbmUobW9kZWwpIDogbW9kZWw7IH1cbiAgZ2V0IG1vZGVsKCkgeyByZXR1cm4gdGhpcy5fbW9kZWwgfHwge307IH1cblxuICBASW5wdXQoKVxuICBzZXQgZmllbGRzKGZpZWxkczogRm9ybWx5RmllbGRDb25maWdbXSkgeyB0aGlzLl9maWVsZHMgPSB0aGlzLmltbXV0YWJsZSA/IGNsb25lKGZpZWxkcykgOiBmaWVsZHM7IH1cbiAgZ2V0IGZpZWxkcygpIHsgcmV0dXJuIHRoaXMuX2ZpZWxkcyB8fCBbXTsgfVxuXG4gIEBJbnB1dCgpXG4gIHNldCBvcHRpb25zKG9wdGlvbnM6IEZvcm1seUZvcm1PcHRpb25zKSB7IHRoaXMuX29wdGlvbnMgPSB0aGlzLmltbXV0YWJsZSA/IGNsb25lKG9wdGlvbnMpIDogb3B0aW9uczsgfVxuICBnZXQgb3B0aW9ucygpIHsgcmV0dXJuIHRoaXMuX29wdGlvbnM7IH1cblxuICBAT3V0cHV0KCkgbW9kZWxDaGFuZ2UgPSBuZXcgRXZlbnRFbWl0dGVyPGFueT4oKTtcbiAgQFZpZXdDaGlsZCgnY29udGVudCcpIHNldCBjb250ZW50KGNvbnRlbnQ6IEVsZW1lbnRSZWY8SFRNTEVsZW1lbnQ+KSB7XG4gICAgaWYgKGNvbnRlbnQgJiYgY29udGVudC5uYXRpdmVFbGVtZW50Lm5leHRTaWJsaW5nKSB7XG4gICAgICBjb25zb2xlLndhcm4oYE5neEZvcm1seTogY29udGVudCBwcm9qZWN0aW9uIGZvciAnZm9ybWx5LWZvcm0nIGNvbXBvbmVudCBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjUsIHlvdSBzaG91bGQgYXZvaWQgcGFzc2luZyBjb250ZW50IGluc2lkZSB0aGUgJ2Zvcm1seS1mb3JtJyB0YWcuYCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBpbW11dGFibGUgPSBmYWxzZTtcbiAgcHJpdmF0ZSBfbW9kZWw6IGFueTtcbiAgcHJpdmF0ZSBfbW9kZWxDaGFuZ2VWYWx1ZTogYW55ID0ge307XG4gIHByaXZhdGUgX2ZpZWxkczogRm9ybWx5RmllbGRDb25maWdbXTtcbiAgcHJpdmF0ZSBfb3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnM7XG4gIHByaXZhdGUgbW9kZWxDaGFuZ2VTdWJzOiBTdWJzY3JpcHRpb25bXSA9IFtdO1xuICBwcml2YXRlIG1vZGVsQ2hhbmdlJCA9IG5ldyBTdWJqZWN0PHZvaWQ+KCk7XG4gIHByaXZhdGUgbW9kZWxDaGFuZ2VTdWIgPSB0aGlzLm1vZGVsQ2hhbmdlJC5waXBlKFxuICAgIHN3aXRjaE1hcCgoKSA9PiB0aGlzLm5nWm9uZS5vblN0YWJsZS5hc09ic2VydmFibGUoKS5waXBlKHRha2UoMSkpKSxcbiAgKS5zdWJzY3JpYmUoKCkgPT4gdGhpcy5uZ1pvbmUucnVuR3VhcmRlZCgoKSA9PiB7XG4gICAgLy8gcnVuR3VhcmRlZCBpcyB1c2VkIHRvIGtlZXAgdGhlIGV4cHJlc3Npb24gY2hhbmdlcyBpbi1zeW5jXG4gICAgLy8gaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjA5NVxuICAgIHRoaXMuY2hlY2tFeHByZXNzaW9uQ2hhbmdlKCk7XG4gICAgdGhpcy5tb2RlbENoYW5nZS5lbWl0KHRoaXMuX21vZGVsQ2hhbmdlVmFsdWUgPSBjbG9uZSh0aGlzLm1vZGVsKSk7XG4gIH0pKTtcblxuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGZvcm1seUJ1aWxkZXI6IEZvcm1seUZvcm1CdWlsZGVyLFxuICAgIHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcsXG4gICAgcHJpdmF0ZSBuZ1pvbmU6IE5nWm9uZSxcbiAgICAvLyB0c2xpbnQ6ZGlzYWJsZS1uZXh0LWxpbmVcbiAgICBAQXR0cmlidXRlKCdpbW11dGFibGUnKSBpbW11dGFibGUsXG4gICAgQE9wdGlvbmFsKCkgcHJpdmF0ZSBwYXJlbnRGb3JtR3JvdXA6IEZvcm1Hcm91cERpcmVjdGl2ZSxcbiAgKSB7XG4gICAgaWYgKGltbXV0YWJsZSAhPT0gbnVsbCkge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ2ltbXV0YWJsZScgYXR0cmlidXRlIHRvICdmb3JtbHktZm9ybScgY29tcG9uZW50IGlzIGRlcHJlY2F0ZWQgc2luY2UgdjUuNSwgZW5hYmxlIGltbXV0YWJsZSBtb2RlIHRocm91Z2ggTmdNb2R1bGUgZGVjbGFyYXRpb24gaW5zdGVhZC5gKTtcbiAgICB9XG5cbiAgICB0aGlzLmltbXV0YWJsZSA9IChpbW11dGFibGUgIT09IG51bGwpIHx8ICEhZm9ybWx5Q29uZmlnLmV4dHJhcy5pbW11dGFibGU7XG4gIH1cblxuICBuZ0RvQ2hlY2soKSB7XG4gICAgaWYgKHRoaXMuZm9ybWx5Q29uZmlnLmV4dHJhcy5jaGVja0V4cHJlc3Npb25PbiA9PT0gJ2NoYW5nZURldGVjdGlvbkNoZWNrJykge1xuICAgICAgdGhpcy5jaGVja0V4cHJlc3Npb25DaGFuZ2UoKTtcbiAgICB9XG4gIH1cblxuICBuZ09uQ2hhbmdlcyhjaGFuZ2VzOiBTaW1wbGVDaGFuZ2VzKSB7XG4gICAgLy8gaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjI5NFxuICAgIGlmIChjaGFuZ2VzLm1vZGVsICYmIHRoaXMuZmllbGQpIHtcbiAgICAgIHRoaXMuZmllbGQubW9kZWwgPSB0aGlzLm1vZGVsO1xuICAgIH1cblxuICAgIGlmIChjaGFuZ2VzLmZpZWxkcyB8fCBjaGFuZ2VzLmZvcm0gfHwgKGNoYW5nZXMubW9kZWwgJiYgdGhpcy5fbW9kZWxDaGFuZ2VWYWx1ZSAhPT0gY2hhbmdlcy5tb2RlbC5jdXJyZW50VmFsdWUpKSB7XG4gICAgICB0aGlzLmZvcm0gPSB0aGlzLmZvcm0gfHwgKG5ldyBGb3JtR3JvdXAoe30pKTtcbiAgICAgIHRoaXMuc2V0T3B0aW9ucygpO1xuICAgICAgdGhpcy5vcHRpb25zLnVwZGF0ZUluaXRpYWxWYWx1ZSgpO1xuICAgICAgdGhpcy5jbGVhck1vZGVsU3Vic2NyaXB0aW9ucygpO1xuICAgICAgdGhpcy5mb3JtbHlCdWlsZGVyLmJ1aWxkRm9ybSh0aGlzLmZvcm0sIHRoaXMuZmllbGRzLCB0aGlzLm1vZGVsLCB0aGlzLm9wdGlvbnMpO1xuICAgICAgdGhpcy50cmFja01vZGVsQ2hhbmdlcyh0aGlzLmZpZWxkcyk7XG4gICAgfVxuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgdGhpcy5tb2RlbENoYW5nZVN1Yi51bnN1YnNjcmliZSgpO1xuICAgIHRoaXMuY2xlYXJNb2RlbFN1YnNjcmlwdGlvbnMoKTtcbiAgfVxuXG4gIGNoYW5nZU1vZGVsKHsga2V5LCB2YWx1ZSwgZmllbGQgfTogeyBrZXk6IHN0cmluZywgdmFsdWU6IGFueSwgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnIH0pIHtcbiAgICBhc3NpZ25GaWVsZFZhbHVlKGZpZWxkLCB2YWx1ZSk7XG4gICAgdGhpcy5tb2RlbENoYW5nZSQubmV4dCgpO1xuICB9XG5cbiAgc2V0T3B0aW9ucygpIHtcbiAgICBpZiAoIXRoaXMub3B0aW9ucykge1xuICAgICAgdGhpcy5vcHRpb25zID0ge307XG4gICAgfVxuXG4gICAgaWYgKCF0aGlzLm9wdGlvbnMucmVzZXRNb2RlbCkge1xuICAgICAgdGhpcy5vcHRpb25zLnJlc2V0TW9kZWwgPSAobW9kZWwgPzogYW55KSA9PiB7XG4gICAgICAgIG1vZGVsID0gY2xvbmUoaXNOdWxsT3JVbmRlZmluZWQobW9kZWwpID8gKDxGb3JtbHlGb3JtT3B0aW9uc0NhY2hlPiB0aGlzLm9wdGlvbnMpLl9pbml0aWFsTW9kZWwgOiBtb2RlbCk7XG4gICAgICAgIGlmICh0aGlzLm1vZGVsKSB7XG4gICAgICAgICAgT2JqZWN0LmtleXModGhpcy5tb2RlbCkuZm9yRWFjaChrID0+IGRlbGV0ZSB0aGlzLm1vZGVsW2tdKTtcbiAgICAgICAgICBPYmplY3QuYXNzaWduKHRoaXMubW9kZWwsIG1vZGVsIHx8IHt9KTtcbiAgICAgICAgfVxuXG4gICAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fYnVpbGRGb3JtKCk7XG5cbiAgICAgICAgLy8gd2Ugc2hvdWxkIGNhbGwgYE5nRm9ybTo6cmVzZXRGb3JtYCB0byBlbnN1cmUgY2hhbmdpbmcgYHN1Ym1pdHRlZGAgc3RhdGUgYWZ0ZXIgcmVzZXR0aW5nIGZvcm1cbiAgICAgICAgLy8gYnV0IG9ubHkgd2hlbiB0aGUgY3VycmVudCBjb21wb25lbnQgaXMgYSByb290IG9uZS5cbiAgICAgICAgaWYgKHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtICYmIHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtLmNvbnRyb2wgPT09IHRoaXMuZm9ybSkge1xuICAgICAgICAgIHRoaXMub3B0aW9ucy5wYXJlbnRGb3JtLnJlc2V0Rm9ybShtb2RlbCk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgdGhpcy5mb3JtLnJlc2V0KG1vZGVsKTtcbiAgICAgICAgfVxuICAgICAgfTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMub3B0aW9ucy5wYXJlbnRGb3JtICYmIHRoaXMucGFyZW50Rm9ybUdyb3VwKSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKHRoaXMub3B0aW9ucywgJ3BhcmVudEZvcm0nLCB0aGlzLnBhcmVudEZvcm1Hcm91cCk7XG4gICAgICB3cmFwUHJvcGVydHkodGhpcy5vcHRpb25zLnBhcmVudEZvcm0sICdzdWJtaXR0ZWQnLCAoeyBmaXJzdENoYW5nZSB9KSA9PiB7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UpIHtcbiAgICAgICAgICB0aGlzLmNoZWNrRXhwcmVzc2lvbkNoYW5nZSgpO1xuICAgICAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fbWFya0ZvckNoZWNrKHtcbiAgICAgICAgICAgIGZpZWxkR3JvdXA6IHRoaXMuZmllbGRzLFxuICAgICAgICAgICAgbW9kZWw6IHRoaXMubW9kZWwsXG4gICAgICAgICAgICBmb3JtQ29udHJvbDogdGhpcy5mb3JtLFxuICAgICAgICAgICAgb3B0aW9uczogdGhpcy5vcHRpb25zLFxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICB9KTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMub3B0aW9ucy51cGRhdGVJbml0aWFsVmFsdWUpIHtcbiAgICAgIHRoaXMub3B0aW9ucy51cGRhdGVJbml0aWFsVmFsdWUgPSAoKSA9PiAoPEZvcm1seUZvcm1PcHRpb25zQ2FjaGU+IHRoaXMub3B0aW9ucykuX2luaXRpYWxNb2RlbCA9IGNsb25lKHRoaXMubW9kZWwpO1xuICAgIH1cblxuICAgIGlmICghKDxGb3JtbHlGb3JtT3B0aW9uc0NhY2hlPiB0aGlzLm9wdGlvbnMpLl9idWlsZEZvcm0pIHtcbiAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fYnVpbGRGb3JtID0gKGVtaXRNb2RlbENoYW5nZSA9IGZhbHNlKSA9PiB7XG4gICAgICAgIHRoaXMuY2xlYXJNb2RlbFN1YnNjcmlwdGlvbnMoKTtcbiAgICAgICAgdGhpcy5mb3JtbHlCdWlsZGVyLmJ1aWxkRm9ybSh0aGlzLmZvcm0sIHRoaXMuZmllbGRzLCB0aGlzLm1vZGVsLCB0aGlzLm9wdGlvbnMpO1xuICAgICAgICB0aGlzLnRyYWNrTW9kZWxDaGFuZ2VzKHRoaXMuZmllbGRzKTtcblxuICAgICAgICBpZiAoZW1pdE1vZGVsQ2hhbmdlKSB7XG4gICAgICAgICAgdGhpcy5tb2RlbENoYW5nZS5lbWl0KHRoaXMuX21vZGVsQ2hhbmdlVmFsdWUgPSBjbG9uZSh0aGlzLm1vZGVsKSk7XG4gICAgICAgIH1cbiAgICAgIH07XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBjaGVja0V4cHJlc3Npb25DaGFuZ2UoKSB7XG4gICAgaWYgKHRoaXMub3B0aW9ucyAmJiAoPEZvcm1seUZvcm1PcHRpb25zQ2FjaGU+IHRoaXMub3B0aW9ucykuX2NoZWNrRmllbGQpIHtcbiAgICAgICg8Rm9ybWx5Rm9ybU9wdGlvbnNDYWNoZT4gdGhpcy5vcHRpb25zKS5fY2hlY2tGaWVsZCh7XG4gICAgICAgIGZpZWxkR3JvdXA6IHRoaXMuZmllbGRzLFxuICAgICAgICBtb2RlbDogdGhpcy5tb2RlbCxcbiAgICAgICAgZm9ybUNvbnRyb2w6IHRoaXMuZm9ybSxcbiAgICAgICAgb3B0aW9uczogdGhpcy5vcHRpb25zLFxuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSB0cmFja01vZGVsQ2hhbmdlcyhmaWVsZHM6IEZvcm1seUZpZWxkQ29uZmlnW10sIHJvb3RLZXk6IHN0cmluZ1tdID0gW10pIHtcbiAgICBmaWVsZHMuZm9yRWFjaChmaWVsZCA9PiB7XG4gICAgICBpZiAoZmllbGQua2V5ICYmICFmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICAgIGNvbnN0IGNvbnRyb2wgPSBmaWVsZC5mb3JtQ29udHJvbDtcbiAgICAgICAgbGV0IHZhbHVlQ2hhbmdlcyA9IGNvbnRyb2wudmFsdWVDaGFuZ2VzLnBpcGUoZGlzdGluY3RVbnRpbENoYW5nZWQoKSk7XG5cbiAgICAgICAgY29uc3QgeyB1cGRhdGVPbiwgZGVib3VuY2UgfSA9IGZpZWxkLm1vZGVsT3B0aW9ucztcbiAgICAgICAgaWYgKCghdXBkYXRlT24gfHwgdXBkYXRlT24gPT09ICdjaGFuZ2UnKSAmJiBkZWJvdW5jZSAmJiBkZWJvdW5jZS5kZWZhdWx0ID4gMCkge1xuICAgICAgICAgIHZhbHVlQ2hhbmdlcyA9IGNvbnRyb2wudmFsdWVDaGFuZ2VzLnBpcGUoZGVib3VuY2VUaW1lKGRlYm91bmNlLmRlZmF1bHQpKTtcbiAgICAgICAgfVxuXG4gICAgICAgIHRoaXMubW9kZWxDaGFuZ2VTdWJzLnB1c2godmFsdWVDaGFuZ2VzLnN1YnNjcmliZSgodmFsdWUpID0+IHtcbiAgICAgICAgICAvLyB3b3JrYXJvdW5kIGZvciBodHRwczovL2dpdGh1Yi5jb20vYW5ndWxhci9hbmd1bGFyL2lzc3Vlcy8xMzc5MlxuICAgICAgICAgIGlmIChjb250cm9sIGluc3RhbmNlb2YgRm9ybUNvbnRyb2wgJiYgY29udHJvbFsnX2ZpZWxkcyddICYmIGNvbnRyb2xbJ19maWVsZHMnXS5sZW5ndGggPiAxKSB7XG4gICAgICAgICAgICBjb250cm9sLnBhdGNoVmFsdWUodmFsdWUsIHsgZW1pdEV2ZW50OiBmYWxzZSwgb25seVNlbGY6IHRydWUgfSk7XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgaWYgKGZpZWxkLnBhcnNlcnMgJiYgZmllbGQucGFyc2Vycy5sZW5ndGggPiAwKSB7XG4gICAgICAgICAgICBmaWVsZC5wYXJzZXJzLmZvckVhY2gocGFyc2VyRm4gPT4gdmFsdWUgPSBwYXJzZXJGbih2YWx1ZSkpO1xuICAgICAgICAgIH1cblxuICAgICAgICAgIHRoaXMuY2hhbmdlTW9kZWwoeyBrZXk6IFsuLi5yb290S2V5LCAuLi5nZXRLZXlQYXRoKGZpZWxkKV0uam9pbignLicpLCB2YWx1ZSwgZmllbGQgfSk7XG4gICAgICAgIH0pKTtcblxuICAgICAgICAvLyB3b3JrYXJvdW5kIGZvciB2NSAoaHR0cHM6Ly9naXRodWIuY29tL25neC1mb3JtbHkvbmd4LWZvcm1seS9pc3N1ZXMvMjA2MSlcbiAgICAgICAgY29uc3Qgb2JzZXJ2ZXJzID0gY29udHJvbC52YWx1ZUNoYW5nZXNbJ29ic2VydmVycyddO1xuICAgICAgICBpZiAob2JzZXJ2ZXJzICYmIG9ic2VydmVycy5sZW5ndGggPiAxKSB7XG4gICAgICAgICAgb2JzZXJ2ZXJzLnVuc2hpZnQob2JzZXJ2ZXJzLnBvcCgpKTtcbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICBpZiAoZmllbGQuZmllbGRHcm91cCAmJiBmaWVsZC5maWVsZEdyb3VwLmxlbmd0aCA+IDApIHtcbiAgICAgICAgdGhpcy50cmFja01vZGVsQ2hhbmdlcyhmaWVsZC5maWVsZEdyb3VwLCBmaWVsZC5rZXkgPyBbLi4ucm9vdEtleSwgLi4uZ2V0S2V5UGF0aChmaWVsZCldIDogcm9vdEtleSk7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICBwcml2YXRlIGNsZWFyTW9kZWxTdWJzY3JpcHRpb25zKCkge1xuICAgIHRoaXMubW9kZWxDaGFuZ2VTdWJzLmZvckVhY2goc3ViID0+IHN1Yi51bnN1YnNjcmliZSgpKTtcbiAgICB0aGlzLm1vZGVsQ2hhbmdlU3VicyA9IFtdO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXQgZmllbGQoKTogYW55IHtcbiAgICByZXR1cm4gdGhpcy5maWVsZHMgJiYgdGhpcy5maWVsZHNbMF0gJiYgdGhpcy5maWVsZHNbMF0ucGFyZW50O1xuICB9XG59XG4iXX0=