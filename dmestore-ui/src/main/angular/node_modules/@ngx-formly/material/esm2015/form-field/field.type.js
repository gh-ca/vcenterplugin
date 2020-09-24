/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { TemplateRef, ViewChild, Type } from '@angular/core';
import { FieldType as CoreFieldType, ɵdefineHiddenProp as defineHiddenProp } from '@ngx-formly/core';
import { Subject } from 'rxjs';
import { FormlyErrorStateMatcher } from './formly.error-state-matcher';
/**
 * @abstract
 * @template F
 */
export class FieldType extends CoreFieldType {
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
                defineHiddenProp(this.field, '_matprefix', this.matPrefix);
                defineHiddenProp(this.field, '_matsuffix', this.matSuffix);
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
FieldType.propDecorators = {
    matPrefix: [{ type: ViewChild, args: ['matPrefix',] }],
    matSuffix: [{ type: ViewChild, args: ['matSuffix',] }]
};
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL2Zvcm0tZmllbGQvIiwic291cmNlcyI6WyJmaWVsZC50eXBlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7QUFBQSxPQUFPLEVBQW9DLFdBQVcsRUFBRSxTQUFTLEVBQUUsSUFBSSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQy9GLE9BQU8sRUFBRSxTQUFTLElBQUksYUFBYSxFQUFFLGlCQUFpQixJQUFJLGdCQUFnQixFQUFxQixNQUFNLGtCQUFrQixDQUFDO0FBQ3hILE9BQU8sRUFBRSxPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFFL0IsT0FBTyxFQUFFLHVCQUF1QixFQUFFLE1BQU0sOEJBQThCLENBQUM7Ozs7O0FBRXZFLE1BQU0sT0FBZ0IsU0FBMkQsU0FBUSxhQUFnQjtJQUF6Rzs7UUFZRSxzQkFBaUIsR0FBRyxJQUFJLHVCQUF1QixDQUFDLElBQUksQ0FBQyxDQUFDO1FBQ3RELGlCQUFZLEdBQUcsSUFBSSxPQUFPLEVBQVEsQ0FBQztRQUNuQyxnQkFBVyxHQUFHLEtBQUssQ0FBQztJQWdFdEIsQ0FBQzs7OztJQTFFQyxJQUFJLGdCQUFnQixLQUFLLE9BQU8sSUFBSSxDQUFDLFFBQVEsSUFBSSxJQUFJLENBQUMsQ0FBQyxDQUFDOzs7OztJQUN4RCxJQUFJLGdCQUFnQixDQUFDLE9BQWlDO1FBQ3BELElBQUksQ0FBQyxRQUFRLEdBQUcsT0FBTyxDQUFDO1FBQ3hCLElBQUksSUFBSSxDQUFDLFNBQVMsSUFBSSxPQUFPLEtBQUssSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLEVBQUU7WUFDekQsSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLEdBQUcsT0FBTyxDQUFDO1NBQ25DO0lBQ0gsQ0FBQzs7OztJQU9ELFFBQVE7UUFDTixJQUFJLElBQUksQ0FBQyxTQUFTLEVBQUU7WUFDbEIsSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLEdBQUcsSUFBSSxDQUFDLGdCQUFnQixDQUFDO1NBQ2pEO0lBQ0gsQ0FBQzs7OztJQUVELGVBQWU7UUFDYixJQUFJLElBQUksQ0FBQyxTQUFTLElBQUksSUFBSSxDQUFDLFNBQVMsRUFBRTtZQUNwQyxVQUFVOzs7WUFBQyxHQUFHLEVBQUU7Z0JBQ2QsZ0JBQWdCLENBQUMsSUFBSSxDQUFDLEtBQUssRUFBRSxZQUFZLEVBQUUsSUFBSSxDQUFDLFNBQVMsQ0FBQyxDQUFDO2dCQUMzRCxnQkFBZ0IsQ0FBQyxJQUFJLENBQUMsS0FBSyxFQUFFLFlBQVksRUFBRSxJQUFJLENBQUMsU0FBUyxDQUFDLENBQUM7Z0JBQzNELENBQUMsbUJBQU0sSUFBSSxDQUFDLE9BQU8sRUFBQSxDQUFDLENBQUMsYUFBYSxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsQ0FBQztZQUNqRCxDQUFDLEVBQUMsQ0FBQztTQUNKO0lBQ0gsQ0FBQzs7OztJQUVELFdBQVc7UUFDVCxJQUFJLElBQUksQ0FBQyxTQUFTLEVBQUU7WUFDbEIsT0FBTyxJQUFJLENBQUMsU0FBUyxDQUFDLFFBQVEsQ0FBQztTQUNoQztRQUNELElBQUksQ0FBQyxZQUFZLENBQUMsUUFBUSxFQUFFLENBQUM7SUFDL0IsQ0FBQzs7Ozs7SUFFRCxpQkFBaUIsQ0FBQyxHQUFhLElBQVUsQ0FBQzs7Ozs7SUFDMUMsZ0JBQWdCLENBQUMsS0FBaUI7UUFDaEMsSUFBSSxDQUFDLEtBQUssQ0FBQyxLQUFLLEdBQUcsSUFBSSxDQUFDO1FBQ3hCLElBQUksQ0FBQyxZQUFZLENBQUMsSUFBSSxFQUFFLENBQUM7SUFDM0IsQ0FBQzs7OztJQUVELElBQUksVUFBVTs7Y0FDTixTQUFTLEdBQUcsbUJBQUEsbUJBQUEsSUFBSSxDQUFDLE9BQU8sRUFBQyxDQUFDLFNBQVMsRUFBQyxDQUFDLElBQUksQ0FBQztRQUNoRCxJQUFJLFNBQVMsS0FBSyxJQUFJLENBQUMsV0FBVyxFQUFFO1lBQ2xDLElBQUksQ0FBQyxXQUFXLEdBQUcsU0FBUyxDQUFDO1lBQzdCLElBQUksQ0FBQyxZQUFZLENBQUMsSUFBSSxFQUFFLENBQUM7U0FDMUI7UUFFRCxPQUFPLFNBQVMsQ0FBQztJQUNuQixDQUFDOzs7O0lBRUQsSUFBSSxXQUFXO1FBQ2IsSUFBSSxJQUFJLENBQUMsRUFBRSxDQUFDLElBQUksRUFBRTtZQUNoQixPQUFPLElBQUksQ0FBQyxFQUFFLENBQUMsSUFBSSxDQUFDO1NBQ3JCO1FBRUQsSUFBSSxDQUFDLG1CQUFNLElBQUksQ0FBQyxLQUFLLENBQUMsSUFBSSxFQUFBLENBQUMsWUFBWSxJQUFJLEVBQUU7WUFDM0MsT0FBTyxtQkFBQSxJQUFJLENBQUMsS0FBSyxDQUFDLElBQUksRUFBQyxDQUFDLFdBQVcsQ0FBQyxJQUFJLENBQUM7U0FDMUM7UUFFRCxPQUFPLG1CQUFBLElBQUksQ0FBQyxLQUFLLENBQUMsSUFBSSxFQUFDLENBQUM7SUFDMUIsQ0FBQzs7OztJQUNELElBQUksT0FBTyxLQUFLLE9BQU8sQ0FBQyxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsS0FBSyxJQUFJLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxDQUFDLENBQUM7Ozs7SUFDOUQsSUFBSSxRQUFRLEtBQUssT0FBTyxDQUFDLENBQUMsSUFBSSxDQUFDLEVBQUUsQ0FBQyxRQUFRLENBQUMsQ0FBQyxDQUFDOzs7O0lBQzdDLElBQUksUUFBUSxLQUFLLE9BQU8sQ0FBQyxDQUFDLElBQUksQ0FBQyxFQUFFLENBQUMsUUFBUSxDQUFDLENBQUMsQ0FBQzs7OztJQUM3QyxJQUFJLFdBQVcsS0FBSyxPQUFPLElBQUksQ0FBQyxFQUFFLENBQUMsV0FBVyxJQUFJLEVBQUUsQ0FBQyxDQUFDLENBQUM7Ozs7SUFDdkQsSUFBSSxzQkFBc0IsS0FBSyxPQUFPLElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxDQUFDLENBQUM7Ozs7SUFDOUQsSUFBSSxLQUFLLEtBQUssT0FBTyxJQUFJLENBQUMsV0FBVyxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUM7Ozs7O0lBQzlDLElBQUksS0FBSyxDQUFDLEtBQUssSUFBSSxJQUFJLENBQUMsV0FBVyxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUM7Ozs7SUFDeEQsSUFBSSxTQUFTLEtBQUssT0FBTyxtQkFBQSxJQUFJLENBQUMsV0FBVyxFQUFPLENBQUMsQ0FBQyxDQUFDOzs7O0lBQ25ELElBQUksS0FBSyxLQUFLLE9BQU8sSUFBSSxDQUFDLEtBQUssS0FBSyxTQUFTLElBQUksSUFBSSxDQUFDLEtBQUssS0FBSyxJQUFJLElBQUksSUFBSSxDQUFDLEtBQUssS0FBSyxFQUFFLENBQUMsQ0FBQyxDQUFDOzs7O0lBQzVGLElBQUksZ0JBQWdCLEtBQUssT0FBTyxJQUFJLENBQUMsT0FBTyxJQUFJLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUM7Ozs7SUFDOUQsSUFBSSxTQUFTLEtBQW1CLE9BQU8sSUFBSSxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUMsQ0FBQyxtQkFBSyxJQUFJLENBQUMsS0FBSyxFQUFBLENBQUMsQ0FBQyxlQUFlLENBQUMsQ0FBQyxDQUFDLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQzs7O3dCQTVFL0YsU0FBUyxTQUFDLFdBQVc7d0JBQ3JCLFNBQVMsU0FBQyxXQUFXOzs7O0lBRHRCLDhCQUFxRDs7SUFDckQsOEJBQXFEOztJQVVyRCxzQ0FBc0Q7O0lBQ3RELGlDQUFtQzs7SUFDbkMsZ0NBQW9COzs7OztJQUNwQiw2QkFBNEMiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBPbkluaXQsIE9uRGVzdHJveSwgQWZ0ZXJWaWV3SW5pdCwgVGVtcGxhdGVSZWYsIFZpZXdDaGlsZCwgVHlwZSB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRmllbGRUeXBlIGFzIENvcmVGaWVsZFR5cGUsIMm1ZGVmaW5lSGlkZGVuUHJvcCBhcyBkZWZpbmVIaWRkZW5Qcm9wLCBGb3JtbHlGaWVsZENvbmZpZyB9IGZyb20gJ0BuZ3gtZm9ybWx5L2NvcmUnO1xuaW1wb3J0IHsgU3ViamVjdCB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgTWF0Rm9ybUZpZWxkLCBNYXRGb3JtRmllbGRDb250cm9sIH0gZnJvbSAnQGFuZ3VsYXIvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBGb3JtbHlFcnJvclN0YXRlTWF0Y2hlciB9IGZyb20gJy4vZm9ybWx5LmVycm9yLXN0YXRlLW1hdGNoZXInO1xuXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGRUeXBlPEYgZXh0ZW5kcyBGb3JtbHlGaWVsZENvbmZpZyA9IEZvcm1seUZpZWxkQ29uZmlnPiBleHRlbmRzIENvcmVGaWVsZFR5cGU8Rj4gaW1wbGVtZW50cyBPbkluaXQsIEFmdGVyVmlld0luaXQsIE9uRGVzdHJveSwgTWF0Rm9ybUZpZWxkQ29udHJvbDxhbnk+IHtcbiAgQFZpZXdDaGlsZCgnbWF0UHJlZml4JykgbWF0UHJlZml4ITogVGVtcGxhdGVSZWY8YW55PjtcbiAgQFZpZXdDaGlsZCgnbWF0U3VmZml4JykgbWF0U3VmZml4ITogVGVtcGxhdGVSZWY8YW55PjtcblxuICBnZXQgZm9ybUZpZWxkQ29udHJvbCgpIHsgcmV0dXJuIHRoaXMuX2NvbnRyb2wgfHwgdGhpczsgfVxuICBzZXQgZm9ybUZpZWxkQ29udHJvbChjb250cm9sOiBNYXRGb3JtRmllbGRDb250cm9sPGFueT4pIHtcbiAgICB0aGlzLl9jb250cm9sID0gY29udHJvbDtcbiAgICBpZiAodGhpcy5mb3JtRmllbGQgJiYgY29udHJvbCAhPT0gdGhpcy5mb3JtRmllbGQuX2NvbnRyb2wpIHtcbiAgICAgIHRoaXMuZm9ybUZpZWxkLl9jb250cm9sID0gY29udHJvbDtcbiAgICB9XG4gIH1cblxuICBlcnJvclN0YXRlTWF0Y2hlciA9IG5ldyBGb3JtbHlFcnJvclN0YXRlTWF0Y2hlcih0aGlzKTtcbiAgc3RhdGVDaGFuZ2VzID0gbmV3IFN1YmplY3Q8dm9pZD4oKTtcbiAgX2Vycm9yU3RhdGUgPSBmYWxzZTtcbiAgcHJpdmF0ZSBfY29udHJvbCE6IE1hdEZvcm1GaWVsZENvbnRyb2w8YW55PjtcblxuICBuZ09uSW5pdCgpIHtcbiAgICBpZiAodGhpcy5mb3JtRmllbGQpIHtcbiAgICAgIHRoaXMuZm9ybUZpZWxkLl9jb250cm9sID0gdGhpcy5mb3JtRmllbGRDb250cm9sO1xuICAgIH1cbiAgfVxuXG4gIG5nQWZ0ZXJWaWV3SW5pdCgpIHtcbiAgICBpZiAodGhpcy5tYXRQcmVmaXggfHwgdGhpcy5tYXRTdWZmaXgpIHtcbiAgICAgIHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgICBkZWZpbmVIaWRkZW5Qcm9wKHRoaXMuZmllbGQsICdfbWF0cHJlZml4JywgdGhpcy5tYXRQcmVmaXgpO1xuICAgICAgICBkZWZpbmVIaWRkZW5Qcm9wKHRoaXMuZmllbGQsICdfbWF0c3VmZml4JywgdGhpcy5tYXRTdWZmaXgpO1xuICAgICAgICAoPGFueT4gdGhpcy5vcHRpb25zKS5fbWFya0ZvckNoZWNrKHRoaXMuZmllbGQpO1xuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgbmdPbkRlc3Ryb3koKSB7XG4gICAgaWYgKHRoaXMuZm9ybUZpZWxkKSB7XG4gICAgICBkZWxldGUgdGhpcy5mb3JtRmllbGQuX2NvbnRyb2w7XG4gICAgfVxuICAgIHRoaXMuc3RhdGVDaGFuZ2VzLmNvbXBsZXRlKCk7XG4gIH1cblxuICBzZXREZXNjcmliZWRCeUlkcyhpZHM6IHN0cmluZ1tdKTogdm9pZCB7IH1cbiAgb25Db250YWluZXJDbGljayhldmVudDogTW91c2VFdmVudCk6IHZvaWQge1xuICAgIHRoaXMuZmllbGQuZm9jdXMgPSB0cnVlO1xuICAgIHRoaXMuc3RhdGVDaGFuZ2VzLm5leHQoKTtcbiAgfVxuXG4gIGdldCBlcnJvclN0YXRlKCkge1xuICAgIGNvbnN0IHNob3dFcnJvciA9IHRoaXMub3B0aW9ucyEuc2hvd0Vycm9yISh0aGlzKTtcbiAgICBpZiAoc2hvd0Vycm9yICE9PSB0aGlzLl9lcnJvclN0YXRlKSB7XG4gICAgICB0aGlzLl9lcnJvclN0YXRlID0gc2hvd0Vycm9yO1xuICAgICAgdGhpcy5zdGF0ZUNoYW5nZXMubmV4dCgpO1xuICAgIH1cblxuICAgIHJldHVybiBzaG93RXJyb3I7XG4gIH1cblxuICBnZXQgY29udHJvbFR5cGUoKSB7XG4gICAgaWYgKHRoaXMudG8udHlwZSkge1xuICAgICAgcmV0dXJuIHRoaXMudG8udHlwZTtcbiAgICB9XG5cbiAgICBpZiAoKDxhbnk+IHRoaXMuZmllbGQudHlwZSkgaW5zdGFuY2VvZiBUeXBlKSB7XG4gICAgICByZXR1cm4gdGhpcy5maWVsZC50eXBlIS5jb25zdHJ1Y3Rvci5uYW1lO1xuICAgIH1cblxuICAgIHJldHVybiB0aGlzLmZpZWxkLnR5cGUhO1xuICB9XG4gIGdldCBmb2N1c2VkKCkgeyByZXR1cm4gISF0aGlzLmZpZWxkLmZvY3VzICYmICF0aGlzLmRpc2FibGVkOyB9XG4gIGdldCBkaXNhYmxlZCgpIHsgcmV0dXJuICEhdGhpcy50by5kaXNhYmxlZDsgfVxuICBnZXQgcmVxdWlyZWQoKSB7IHJldHVybiAhIXRoaXMudG8ucmVxdWlyZWQ7IH1cbiAgZ2V0IHBsYWNlaG9sZGVyKCkgeyByZXR1cm4gdGhpcy50by5wbGFjZWhvbGRlciB8fCAnJzsgfVxuICBnZXQgc2hvdWxkUGxhY2Vob2xkZXJGbG9hdCgpIHsgcmV0dXJuIHRoaXMuc2hvdWxkTGFiZWxGbG9hdDsgfVxuICBnZXQgdmFsdWUoKSB7IHJldHVybiB0aGlzLmZvcm1Db250cm9sLnZhbHVlOyB9XG4gIHNldCB2YWx1ZSh2YWx1ZSkgeyB0aGlzLmZvcm1Db250cm9sLnBhdGNoVmFsdWUodmFsdWUpOyB9XG4gIGdldCBuZ0NvbnRyb2woKSB7IHJldHVybiB0aGlzLmZvcm1Db250cm9sIGFzIGFueTsgfVxuICBnZXQgZW1wdHkoKSB7IHJldHVybiB0aGlzLnZhbHVlID09PSB1bmRlZmluZWQgfHwgdGhpcy52YWx1ZSA9PT0gbnVsbCB8fCB0aGlzLnZhbHVlID09PSAnJzsgfVxuICBnZXQgc2hvdWxkTGFiZWxGbG9hdCgpIHsgcmV0dXJuIHRoaXMuZm9jdXNlZCB8fCAhdGhpcy5lbXB0eTsgfVxuICBnZXQgZm9ybUZpZWxkKCk6IE1hdEZvcm1GaWVsZCB7IHJldHVybiB0aGlzLmZpZWxkID8gKDxhbnk+dGhpcy5maWVsZClbJ19fZm9ybUZpZWxkX18nXSA6IG51bGw7IH1cbn1cbiJdfQ==