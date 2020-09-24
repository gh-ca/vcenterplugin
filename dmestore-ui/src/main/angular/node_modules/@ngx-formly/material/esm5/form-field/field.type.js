/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { TemplateRef, ViewChild, Type } from '@angular/core';
import { FieldType as CoreFieldType, ɵdefineHiddenProp as defineHiddenProp } from '@ngx-formly/core';
import { Subject } from 'rxjs';
import { FormlyErrorStateMatcher } from './formly.error-state-matcher';
/**
 * @abstract
 * @template F
 */
var FieldType = /** @class */ (function (_super) {
    tslib_1.__extends(FieldType, _super);
    function FieldType() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.errorStateMatcher = new FormlyErrorStateMatcher(_this);
        _this.stateChanges = new Subject();
        _this._errorState = false;
        return _this;
    }
    Object.defineProperty(FieldType.prototype, "formFieldControl", {
        get: /**
         * @return {?}
         */
        function () { return this._control || this; },
        set: /**
         * @param {?} control
         * @return {?}
         */
        function (control) {
            this._control = control;
            if (this.formField && control !== this.formField._control) {
                this.formField._control = control;
            }
        },
        enumerable: true,
        configurable: true
    });
    /**
     * @return {?}
     */
    FieldType.prototype.ngOnInit = /**
     * @return {?}
     */
    function () {
        if (this.formField) {
            this.formField._control = this.formFieldControl;
        }
    };
    /**
     * @return {?}
     */
    FieldType.prototype.ngAfterViewInit = /**
     * @return {?}
     */
    function () {
        var _this = this;
        if (this.matPrefix || this.matSuffix) {
            setTimeout((/**
             * @return {?}
             */
            function () {
                defineHiddenProp(_this.field, '_matprefix', _this.matPrefix);
                defineHiddenProp(_this.field, '_matsuffix', _this.matSuffix);
                ((/** @type {?} */ (_this.options)))._markForCheck(_this.field);
            }));
        }
    };
    /**
     * @return {?}
     */
    FieldType.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        if (this.formField) {
            delete this.formField._control;
        }
        this.stateChanges.complete();
    };
    /**
     * @param {?} ids
     * @return {?}
     */
    FieldType.prototype.setDescribedByIds = /**
     * @param {?} ids
     * @return {?}
     */
    function (ids) { };
    /**
     * @param {?} event
     * @return {?}
     */
    FieldType.prototype.onContainerClick = /**
     * @param {?} event
     * @return {?}
     */
    function (event) {
        this.field.focus = true;
        this.stateChanges.next();
    };
    Object.defineProperty(FieldType.prototype, "errorState", {
        get: /**
         * @return {?}
         */
        function () {
            /** @type {?} */
            var showError = (/** @type {?} */ ((/** @type {?} */ (this.options)).showError))(this);
            if (showError !== this._errorState) {
                this._errorState = showError;
                this.stateChanges.next();
            }
            return showError;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "controlType", {
        get: /**
         * @return {?}
         */
        function () {
            if (this.to.type) {
                return this.to.type;
            }
            if (((/** @type {?} */ (this.field.type))) instanceof Type) {
                return (/** @type {?} */ (this.field.type)).constructor.name;
            }
            return (/** @type {?} */ (this.field.type));
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "focused", {
        get: /**
         * @return {?}
         */
        function () { return !!this.field.focus && !this.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "disabled", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.disabled; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "required", {
        get: /**
         * @return {?}
         */
        function () { return !!this.to.required; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "placeholder", {
        get: /**
         * @return {?}
         */
        function () { return this.to.placeholder || ''; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "shouldPlaceholderFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.shouldLabelFloat; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "value", {
        get: /**
         * @return {?}
         */
        function () { return this.formControl.value; },
        set: /**
         * @param {?} value
         * @return {?}
         */
        function (value) { this.formControl.patchValue(value); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "ngControl", {
        get: /**
         * @return {?}
         */
        function () { return (/** @type {?} */ (this.formControl)); },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "empty", {
        get: /**
         * @return {?}
         */
        function () { return this.value === undefined || this.value === null || this.value === ''; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "shouldLabelFloat", {
        get: /**
         * @return {?}
         */
        function () { return this.focused || !this.empty; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(FieldType.prototype, "formField", {
        get: /**
         * @return {?}
         */
        function () { return this.field ? ((/** @type {?} */ (this.field)))['__formField__'] : null; },
        enumerable: true,
        configurable: true
    });
    FieldType.propDecorators = {
        matPrefix: [{ type: ViewChild, args: ['matPrefix',] }],
        matSuffix: [{ type: ViewChild, args: ['matSuffix',] }]
    };
    return FieldType;
}(CoreFieldType));
export { FieldType };
if (false) {
    /** @type {?} */
    FieldType.prototype.matPrefix;
    /** @type {?} */
    FieldType.prototype.matSuffix;
    /** @type {?} */
    FieldType.prototype.errorStateMatcher;
    /** @type {?} */
    FieldType.prototype.stateChanges;
    /** @type {?} */
    FieldType.prototype._errorState;
    /**
     * @type {?}
     * @private
     */
    FieldType.prototype._control;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQvIiwic291cmNlcyI6WyJmaWVsZC50eXBlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7O0FBQUEsT0FBTyxFQUFvQyxXQUFXLEVBQUUsU0FBUyxFQUFFLElBQUksRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUMvRixPQUFPLEVBQUUsU0FBUyxJQUFJLGFBQWEsRUFBRSxpQkFBaUIsSUFBSSxnQkFBZ0IsRUFBcUIsTUFBTSxrQkFBa0IsQ0FBQztBQUN4SCxPQUFPLEVBQUUsT0FBTyxFQUFFLE1BQU0sTUFBTSxDQUFDO0FBRS9CLE9BQU8sRUFBRSx1QkFBdUIsRUFBRSxNQUFNLDhCQUE4QixDQUFDOzs7OztBQUV2RTtJQUF5RixxQ0FBZ0I7SUFBekc7UUFBQSxxRUE4RUM7UUFsRUMsdUJBQWlCLEdBQUcsSUFBSSx1QkFBdUIsQ0FBQyxLQUFJLENBQUMsQ0FBQztRQUN0RCxrQkFBWSxHQUFHLElBQUksT0FBTyxFQUFRLENBQUM7UUFDbkMsaUJBQVcsR0FBRyxLQUFLLENBQUM7O0lBZ0V0QixDQUFDO0lBMUVDLHNCQUFJLHVDQUFnQjs7OztRQUFwQixjQUF5QixPQUFPLElBQUksQ0FBQyxRQUFRLElBQUksSUFBSSxDQUFDLENBQUMsQ0FBQzs7Ozs7UUFDeEQsVUFBcUIsT0FBaUM7WUFDcEQsSUFBSSxDQUFDLFFBQVEsR0FBRyxPQUFPLENBQUM7WUFDeEIsSUFBSSxJQUFJLENBQUMsU0FBUyxJQUFJLE9BQU8sS0FBSyxJQUFJLENBQUMsU0FBUyxDQUFDLFFBQVEsRUFBRTtnQkFDekQsSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLEdBQUcsT0FBTyxDQUFDO2FBQ25DO1FBQ0gsQ0FBQzs7O09BTnVEOzs7O0lBYXhELDRCQUFROzs7SUFBUjtRQUNFLElBQUksSUFBSSxDQUFDLFNBQVMsRUFBRTtZQUNsQixJQUFJLENBQUMsU0FBUyxDQUFDLFFBQVEs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c3Ryb3ksIEFmdGVyVmlld0luaXQsIFRlbXBsYXRlUmVmLCBWaWV3Q2hpbGQsIFR5cGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSBhcyBDb3JlRmllbGRUeXBlLCDJtWRlZmluZUhpZGRlblByb3AgYXMgZGVmaW5lSGlkZGVuUHJvcCwgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICdAbmd4LWZvcm1seS9jb3JlJztcbmltcG9ydCB7IFN1YmplY3QgfSBmcm9tICdyeGpzJztcbmltcG9ydCB7IE1hdEZvcm1GaWVsZCwgTWF0Rm9ybUZpZWxkQ29udHJvbCB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2Zvcm0tZmllbGQnO1xuaW1wb3J0IHsgRm9ybWx5RXJyb3JTdGF0ZU1hdGNoZXIgfSBmcm9tICcuL2Zvcm1seS5lcnJvci1zdGF0ZS1tYXRjaGVyJztcblxuZXhwb3J0IGFic3RyYWN0IGNsYXNzIEZpZWxkVHlwZTxGIGV4dGVuZHMgRm9ybWx5RmllbGRDb25maWcgPSBGb3JtbHlGaWVsZENvbmZpZz4gZXh0ZW5kcyBDb3JlRmllbGRUeXBlPEY+IGltcGxlbWVudHMgT25Jbml0LCBBZnRlclZpZXdJbml0LCBPbkRlc3Ryb3ksIE1hdEZvcm1GaWVsZENvbnRyb2w8YW55PiB7XG4gIEBWaWV3Q2hpbGQoJ21hdFByZWZpeCcpIG1hdFByZWZpeCE6IFRlbXBsYXRlUmVmPGFueT47XG4gIEBWaWV3Q2hpbGQoJ21hdFN1ZmZpeCcpIG1hdFN1ZmZpeCE6IFRlbXBsYXRlUmVmPGFueT47XG5cbiAgZ2V0IGZvcm1GaWVsZENvbnRyb2woKSB7IHJldHVybiB0aGlzLl9jb250cm9sIHx8IHRoaXM7IH1cbiAgc2V0IGZvcm1GaWVsZENvbnRyb2woY29udHJvbDogTWF0Rm9ybUZpZWxkQ29udHJvbDxhbnk+KSB7XG4gICAgdGhpcy5fY29udHJvbCA9IGNvbnRyb2w7XG4gICAgaWYgKHRoaXMuZm9ybUZpZWxkICYmIGNvbnRyb2wgIT09IHRoaXMuZm9ybUZpZWxkLl9jb250cm9sKSB7XG4gICAgICB0aGlzLmZvcm1GaWVsZC5fY29udHJvbCA9IGNvbnRyb2w7XG4gICAgfVxuICB9XG5cbiAgZXJyb3JTdGF0ZU1hdGNoZXIgPSBuZXcgRm9ybWx5RXJyb3JTdGF0ZU1hdGNoZXIodGhpcyk7XG4gIHN0YXRlQ2hhbmdlcyA9IG5ldyBTdWJqZWN0PHZvaWQ+KCk7XG4gIF9lcnJvclN0YXRlID0gZmFsc2U7XG4gIHByaXZhdGUgX2NvbnRyb2whOiBNYXRGb3JtRmllbGRDb250cm9sPGFueT47XG5cbiAgbmdPbkluaXQoKSB7XG4gICAgaWYgKHRoaXMuZm9ybUZpZWxkKSB7XG4gICAgICB0aGlzLmZvcm1GaWVsZC5fY29udHJvbCA9IHRoaXMuZm9ybUZpZWxkQ29udHJvbDtcbiAgICB9XG4gIH1cblxuICBuZ0FmdGVyVmlld0luaXQoKSB7XG4gICAgaWYgKHRoaXMubWF0UHJlZml4IHx8IHRoaXMubWF0U3VmZml4KSB7XG4gICAgICBzZXRUaW1lb3V0KCgpID0+IHtcbiAgICAgICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX21hdHByZWZpeCcsIHRoaXMubWF0UHJlZml4KTtcbiAgICAgICAgZGVmaW5lSGlkZGVuUHJvcCh0aGlzLmZpZWxkLCAnX21hdHN1ZmZpeCcsIHRoaXMubWF0U3VmZml4KTtcbiAgICAgICAgKDxhbnk+IHRoaXMub3B0aW9ucykuX21hcmtGb3JDaGVjayh0aGlzLmZpZWxkKTtcbiAgICAgIH0pO1xuICAgIH1cbiAgfVxuXG4gIG5nT25EZXN0cm95KCkge1xuICAgIGlmICh0aGlzLmZvcm1GaWVsZCkge1xuICAgICAgZGVsZXRlIHRoaXMuZm9ybUZpZWxkLl9jb250cm9sO1xuICAgIH1cbiAgICB0aGlzLnN0YXRlQ2hhbmdlcy5jb21wbGV0ZSgpO1xuICB9XG5cbiAgc2V0RGVzY3JpYmVkQnlJZHMoaWRzOiBzdHJpbmdbXSk6IHZvaWQgeyB9XG4gIG9uQ29udGFpbmVyQ2xpY2soZXZlbnQ6IE1vdXNlRXZlbnQpOiB2b2lkIHtcbiAgICB0aGlzLmZpZWxkLmZvY3VzID0gdHJ1ZTtcbiAgICB0aGlzLnN0YXRlQ2hhbmdlcy5uZXh0KCk7XG4gIH1cblxuICBnZXQgZXJyb3JTdGF0ZSgpIHtcbiAgICBjb25zdCBzaG93RXJyb3IgPSB0aGlzLm9wdGlvbnMhLnNob3dFcnJvciEodGhpcyk7XG4gICAgaWYgKHNob3dFcnJvciAhPT0gdGhpcy5fZXJyb3JTdGF0ZSkge1xuICAgICAgdGhpcy5fZXJyb3JTdGF0ZSA9IHNob3dFcnJvcjtcbiAgICAgIHRoaXMuc3RhdGVDaGFuZ2VzLm5leHQoKTtcbiAgICB9XG5cbiAgICByZXR1cm4gc2hvd0Vycm9yO1xuICB9XG5cbiAgZ2V0IGNvbnRyb2xUeXBlKCkge1xuICAgIGlmICh0aGlzLnRvLnR5cGUpIHtcbiAgICAgIHJldHVybiB0aGlzLnRvLnR5cGU7XG4gICAgfVxuXG4gICAgaWYgKCg8YW55PiB0aGlzLmZpZWxkLnR5cGUpIGluc3RhbmNlb2YgVHlwZSkge1xuICAgICAgcmV0dXJuIHRoaXMuZmllbGQudHlwZSEuY29uc3RydWN0b3IubmFtZTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy5maWVsZC50eXBlITtcbiAgfVxuICBnZXQgZm9jdXNlZCgpIHsgcmV0dXJuICEhdGhpcy5maWVsZC5mb2N1cyAmJiAhdGhpcy5kaXNhYmxlZDsgfVxuICBnZXQgZGlzYWJsZWQoKSB7IHJldHVybiAhIXRoaXMudG8uZGlzYWJsZWQ7IH1cbiAgZ2V0IHJlcXVpcmVkKCkgeyByZXR1cm4gISF0aGlzLnRvLnJlcXVpcmVkOyB9XG4gIGdldCBwbGFjZWhvbGRlcigpIHsgcmV0dXJuIHRoaXMudG8ucGxhY2Vob2xkZXIgfHwgJyc7IH1cbiAgZ2V0IHNob3VsZFBsYWNlaG9sZGVyRmxvYXQoKSB7IHJldHVybiB0aGlzLnNob3VsZExhYmVsRmxvYXQ7IH1cbiAgZ2V0IHZhbHVlKCkgeyByZXR1cm4gdGhpcy5mb3JtQ29udHJvbC52YWx1ZTsgfVxuICBzZXQgdmFsdWUodmFsdWUpIHsgdGhpcy5mb3JtQ29udHJvbC5wYXRjaFZhbHVlKHZhbHVlKTsgfVxuICBnZXQgbmdDb250cm9sKCkgeyByZXR1cm4gdGhpcy5mb3JtQ29udHJvbCBhcyBhbnk7IH1cbiAgZ2V0IGVtcHR5KCkgeyByZXR1cm4gdGhpcy52YWx1ZSA9PT0gdW5kZWZpbmVkIHx8IHRoaXMudmFsdWUgPT09IG51bGwgfHwgdGhpcy52YWx1ZSA9PT0gJyc7IH1cbiAgZ2V0IHNob3VsZExhYmVsRmxvYXQoKSB7IHJldHVybiB0aGlzLmZvY3VzZWQgfHwgIXRoaXMuZW1wdHk7IH1cbiAgZ2V0IGZvcm1GaWVsZCgpOiBNYXRGb3JtRmllbGQgeyByZXR1cm4gdGhpcy5maWVsZCA/ICg8YW55PnRoaXMuZmllbGQpWydfX2Zvcm1GaWVsZF9fJ10gOiBudWxsOyB9XG59XG4iXX0=