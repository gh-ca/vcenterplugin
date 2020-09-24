/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Injectable, ComponentFactoryResolver, Injector, ChangeDetectorRef } from '@angular/core';
import { FormlyConfig } from './formly.config';
import { Subject } from 'rxjs';
import { defineHiddenProp, reduceFormUpdateValidityCalls } from '../utils';
import * as i0 from "@angular/core";
import * as i1 from "./formly.config";
export class FormlyFormBuilder {
    /**
     * @param {?} formlyConfig
     * @param {?} componentFactoryResolver
     * @param {?} injector
     */
    constructor(formlyConfig, componentFactoryResolver, injector) {
        this.formlyConfig = formlyConfig;
        this.componentFactoryResolver = componentFactoryResolver;
        this.injector = injector;
    }
    /**
     * @param {?} formControl
     * @param {?=} fieldGroup
     * @param {?=} model
     * @param {?=} options
     * @return {?}
     */
    buildForm(formControl, fieldGroup = [], model, options) {
        if (!this.formlyConfig.extensions.core) {
            throw new Error('NgxFormly: missing `forRoot()` call. use `forRoot()` when registering the `FormlyModule`.');
        }
        /** @type {?} */
        const field = { fieldGroup, model, formControl, options: this._setOptions(options) };
        reduceFormUpdateValidityCalls(formControl, (/**
         * @return {?}
         */
        () => this._buildForm(field)));
        field.options._checkField(field, true);
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    _buildForm(field) {
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        extension => extension.prePopulate && extension.prePopulate(field)));
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        extension => extension.onPopulate && extension.onPopulate(field)));
        if (field.fieldGroup) {
            field.fieldGroup.forEach((/**
             * @param {?} f
             * @return {?}
             */
            (f) => this._buildForm(f)));
        }
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        extension => extension.postPopulate && extension.postPopulate(field)));
    }
    /**
     * @private
     * @return {?}
     */
    getExtensions() {
        return Object.keys(this.formlyConfig.extensions).map((/**
         * @param {?} name
         * @return {?}
         */
        name => this.formlyConfig.extensions[name]));
    }
    /**
     * @private
     * @param {?} options
     * @return {?}
     */
    _setOptions(options) {
        options = options || {};
        options.formState = options.formState || {};
        if (!options.showError) {
            options.showError = this.formlyConfig.extras.showError;
        }
        if (!options.fieldChanges) {
            defineHiddenProp(options, 'fieldChanges', new Subject());
        }
        if (!options._resolver) {
            defineHiddenProp(options, '_resolver', this.componentFactoryResolver);
        }
        if (!options._injector) {
            defineHiddenProp(options, '_injector', this.injector);
        }
        if (!options._hiddenFieldsForCheck) {
            options._hiddenFieldsForCheck = [];
        }
        if (!options._markForCheck) {
            options._markForCheck = (/**
             * @param {?} field
             * @return {?}
             */
            (field) => {
                if (field._componentRefs) {
                    field._componentRefs.forEach((/**
                     * @param {?} ref
                     * @return {?}
                     */
                    ref => {
                        // NOTE: we cannot use ref.changeDetectorRef, see https://github.com/ngx-formly/ngx-formly/issues/2191
                        /** @type {?} */
                        const changeDetectorRef = ref.injector.get(ChangeDetectorRef);
                        changeDetectorRef.markForCheck();
                    }));
                }
                if (field.fieldGroup) {
                    field.fieldGroup.forEach((/**
                     * @param {?} f
                     * @return {?}
                     */
                    f => options._markForCheck(f)));
                }
            });
        }
        if (!options._buildField) {
            options._buildField = (/**
             * @param {?} field
             * @return {?}
             */
            (field) => {
                this.buildForm(field.form, field.fieldGroup, field.model, field.options);
                return field;
            });
        }
        return options;
    }
}
FormlyFormBuilder.decorators = [
    { type: Injectable, args: [{ providedIn: 'root' },] }
];
/** @nocollapse */
FormlyFormBuilder.ctorParameters = () => [
    { type: FormlyConfig },
    { type: ComponentFactoryResolver },
    { type: Injector }
];
/** @nocollapse */ FormlyFormBuilder.ngInjectableDef = i0.defineInjectable({ factory: function FormlyFormBuilder_Factory() { return new FormlyFormBuilder(i0.inject(i1.FormlyConfig), i0.inject(i0.ComponentFactoryResolver), i0.inject(i0.INJECTOR)); }, token: FormlyFormBuilder, providedIn: "root" });
if (false) {
    /**
     * @type {?}
     * @private
     */
    FormlyFormBuilder.prototype.formlyConfig;
    /**
     * @type {?}
     * @private
     */
    FormlyFormBuilder.prototype.componentFactoryResolver;
    /**
     * @type {?}
     * @private
     */
    FormlyFormBuilder.prototype.injector;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZvcm0uYnVpbGRlci5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlci50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFVBQVUsRUFBRSx3QkFBd0IsRUFBRSxRQUFRLEVBQUUsaUJBQWlCLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFFbEcsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLGlCQUFpQixDQUFDO0FBRS9DLE9BQU8sRUFBRSxPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFDL0IsT0FBTyxFQUFFLGdCQUFnQixFQUFFLDZCQUE2QixFQUFFLE1BQU0sVUFBVSxDQUFDOzs7QUFHM0UsTUFBTSxPQUFPLGlCQUFpQjs7Ozs7O0lBQzVCLFlBQ1UsWUFBMEIsRUFDMUIsd0JBQWtELEVBQ2xELFFBQWtCO1FBRmxCLGlCQUFZLEdBQVosWUFBWSxDQUFjO1FBQzFCLDZCQUF3QixHQUF4Qix3QkFBd0IsQ0FBMEI7UUFDbEQsYUFBUSxHQUFSLFFBQVEsQ0FBVTtJQUN6QixDQUFDOzs7Ozs7OztJQUVKLFNBQVMsQ0FBQyxXQUFrQyxFQUFFLGFBQWtDLEVBQUUsRUFBRSxLQUFVLEVBQUUsT0FBMEI7UUFDeEgsSUFBSSxDQUFDLElBQUksQ0FBQyxZQUFZLENBQUMsVUFBVSxDQUFDLElBQUksRUFBRTtZQUN0QyxNQUFNLElBQUksS0FBSyxDQUFDLDJGQUEyRixDQUFDLENBQUM7U0FDOUc7O2NBRUssS0FBSyxHQUFHLEVBQUUsVUFBVSxFQUFFLEtBQUssRUFBRSxXQUFXLEVBQUUsT0FBTyxFQUFFLElBQUksQ0FBQyxXQUFXLENBQUMsT0FBTyxDQUFDLEVBQUU7UUFDcEYsNkJBQTZCLENBQUMsV0FBVzs7O1FBQUUsR0FBRyxFQUFFLENBQUMsSUFBSSxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsRUFBQyxDQUFDO1FBQ3pFLEtB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QU8sQ0FBQyxhQUFhLENBQUMsQ0FBQyxDQUFDLEVBQUMsQ0FBQztpQkFDekQ7WUFDSCxDQUFDLENBQUEsQ0FBQztTQUNIO1FBRUQsSUFBSSxDQUFDLE9BQU8sQ0FBQyxXQUFXLEVBQUU7WUFDeEIsT0FBTyxDQUFDLFdBQVc7Ozs7WUFBRyxDQUFDLEtBQXdCLEVBQUUsRUFBRTtnQkFDakQsSUFBSSxDQUFDLFNBQVMsQ0FBQyxLQUFLLENBQUMsSUFBSSxFQUFFLEtBQUssQ0FBQyxVQUFVLEVBQUUsS0FBSyxDQUFDLEtBQUssRUFBRSxLQUFLLENBQUMsT0FBTyxDQUFDLENBQUM7Z0JBQ3pFLE9BQU8sS0FBSyxDQUFDO1lBQ2YsQ0FBQyxDQUFBLENBQUM7U0FDSDtRQUVELE9BQU8sT0FBTyxDQUFDO0lBQ2pCLENBQUM7OztZQWpGRixVQUFVLFNBQUMsRUFBRSxVQUFVLEVBQUUsTUFBTSxFQUFFOzs7O1lBTHpCLFlBQVk7WUFGQSx3QkFBd0I7WUFBRSxRQUFROzs7Ozs7OztJQVVuRCx5Q0FBa0M7Ozs7O0lBQ2xDLHFEQUEwRDs7Ozs7SUFDMUQscUNBQTBCIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgSW5qZWN0YWJsZSwgQ29tcG9uZW50RmFjdG9yeVJlc29sdmVyLCBJbmplY3RvciwgQ2hhbmdlRGV0ZWN0b3JSZWYgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZvcm1Hcm91cCwgRm9ybUFycmF5IH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5Q29uZmlnIH0gZnJvbSAnLi9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnLCBGb3JtbHlGb3JtT3B0aW9ucywgRm9ybWx5RmllbGRDb25maWdDYWNoZSwgRm9ybWx5VmFsdWVDaGFuZ2VFdmVudCwgRm9ybWx5Rm9ybU9wdGlvbnNDYWNoZSB9IGZyb20gJy4uL2NvbXBvbmVudHMvZm9ybWx5LmZpZWxkLmNvbmZpZyc7XG5pbXBvcnQgeyBTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBkZWZpbmVIaWRkZW5Qcm9wLCByZWR1Y2VGb3JtVXBkYXRlVmFsaWRpdHlDYWxscyB9IGZyb20gJy4uL3V0aWxzJztcblxuQEluamVjdGFibGUoeyBwcm92aWRlZEluOiAncm9vdCcgfSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlGb3JtQnVpbGRlciB7XG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcsXG4gICAgcHJpdmF0ZSBjb21wb25lbnRGYWN0b3J5UmVzb2x2ZXI6IENvbXBvbmVudEZhY3RvcnlSZXNvbHZlcixcbiAgICBwcml2YXRlIGluamVjdG9yOiBJbmplY3RvcixcbiAgKSB7fVxuXG4gIGJ1aWxkRm9ybShmb3JtQ29udHJvbDogRm9ybUdyb3VwIHwgRm9ybUFycmF5LCBmaWVsZEdyb3VwOiBGb3JtbHlGaWVsZENvbmZpZ1tdID0gW10sIG1vZGVsOiBhbnksIG9wdGlvbnM6IEZvcm1seUZvcm1PcHRpb25zKSB7XG4gICAgaWYgKCF0aGlzLmZvcm1seUNvbmZpZy5leHRlbnNpb25zLmNvcmUpIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcignTmd4Rm9ybWx5OiBtaXNzaW5nIGBmb3JSb290KClgIGNhbGwuIHVzZSBgZm9yUm9vdCgpYCB3aGVuIHJlZ2lzdGVyaW5nIHRoZSBgRm9ybWx5TW9kdWxlYC4nKTtcbiAgICB9XG5cbiAgICBjb25zdCBmaWVsZCA9IHsgZmllbGRHcm91cCwgbW9kZWwsIGZvcm1Db250cm9sLCBvcHRpb25zOiB0aGlzLl9zZXRPcHRpb25zKG9wdGlvbnMpIH07XG4gICAgcmVkdWNlRm9ybVVwZGF0ZVZhbGlkaXR5Q2FsbHMoZm9ybUNvbnRyb2wsICgpID0+IHRoaXMuX2J1aWxkRm9ybShmaWVsZCkpO1xuICAgIGZpZWxkLm9wdGlvbnMuX2NoZWNrRmllbGQoZmllbGQsIHRydWUpO1xuICB9XG5cbiAgcHJpdmF0ZSBfYnVpbGRGb3JtKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5nZXRFeHRlbnNpb25zKCkuZm9yRWFjaChleHRlbnNpb24gPT4gZXh0ZW5zaW9uLnByZVBvcHVsYXRlICYmIGV4dGVuc2lvbi5wcmVQb3B1bGF0ZShmaWVsZCkpO1xuICAgIHRoaXMuZ2V0RXh0ZW5zaW9ucygpLmZvckVhY2goZXh0ZW5zaW9uID0+IGV4dGVuc2lvbi5vblBvcHVsYXRlICYmIGV4dGVuc2lvbi5vblBvcHVsYXRlKGZpZWxkKSk7XG5cbiAgICBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgZmllbGQuZmllbGRHcm91cC5mb3JFYWNoKChmKSA9PiB0aGlzLl9idWlsZEZvcm0oZikpO1xuICAgIH1cblxuICAgIHRoaXMuZ2V0RXh0ZW5zaW9ucygpLmZvckVhY2goZXh0ZW5zaW9uID0+IGV4dGVuc2lvbi5wb3N0UG9wdWxhdGUgJiYgZXh0ZW5zaW9uLnBvc3RQb3B1bGF0ZShmaWVsZCkpO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRFeHRlbnNpb25zKCkge1xuICAgIHJldHVybiBPYmplY3Qua2V5cyh0aGlzLmZvcm1seUNvbmZpZy5leHRlbnNpb25zKS5tYXAobmFtZSA9PiB0aGlzLmZvcm1seUNvbmZpZy5leHRlbnNpb25zW25hbWVdKTtcbiAgfVxuXG4gIHByaXZhdGUgX3NldE9wdGlvbnMob3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnNDYWNoZSkge1xuICAgIG9wdGlvbnMgPSBvcHRpb25zIHx8IHt9O1xuICAgIG9wdGlvbnMuZm9ybVN0YXRlID0gb3B0aW9ucy5mb3JtU3RhdGUgfHwge307XG5cbiAgICBpZiAoIW9wdGlvbnMuc2hvd0Vycm9yKSB7XG4gICAgICBvcHRpb25zLnNob3dFcnJvciA9IHRoaXMuZm9ybWx5Q29uZmlnLmV4dHJhcy5zaG93RXJyb3I7XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLmZpZWxkQ2hhbmdlcykge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcChvcHRpb25zLCAnZmllbGRDaGFuZ2VzJywgbmV3IFN1YmplY3Q8Rm9ybWx5VmFsdWVDaGFuZ2VFdmVudD4oKSk7XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLl9yZXNvbHZlcikge1xuICAgICAgZGVmaW5lSGlkZGVuUHJvcChvcHRpb25zLCAnX3Jlc29sdmVyJywgdGhpcy5jb21wb25lbnRGYWN0b3J5UmVzb2x2ZXIpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5faW5qZWN0b3IpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19pbmplY3RvcicsIHRoaXMuaW5qZWN0b3IpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5faGlkZGVuRmllbGRzRm9yQ2hlY2spIHtcbiAgICAgIG9wdGlvbnMuX2hpZGRlbkZpZWxkc0ZvckNoZWNrID0gW107XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLl9tYXJrRm9yQ2hlY2spIHtcbiAgICAgIG9wdGlvbnMuX21hcmtGb3JDaGVjayA9IChmaWVsZCkgPT4ge1xuICAgICAgICBpZiAoZmllbGQuX2NvbXBvbmVudFJlZnMpIHtcbiAgICAgICAgICBmaWVsZC5fY29tcG9uZW50UmVmcy5mb3JFYWNoKHJlZiA9PiB7XG4gICAgICAgICAgICAvLyBOT1RFOiB3ZSBjYW5ub3QgdXNlIHJlZi5jaGFuZ2VEZXRlY3RvclJlZiwgc2VlIGh0dHBzOi8vZ2l0aHViLmNvbS9uZ3gtZm9ybWx5L25neC1mb3JtbHkvaXNzdWVzLzIxOTFcbiAgICAgICAgICAgIGNvbnN0IGNoYW5nZURldGVjdG9yUmVmID0gcmVmLmluamVjdG9yLmdldChDaGFuZ2VEZXRlY3RvclJlZik7XG4gICAgICAgICAgICBjaGFuZ2VEZXRlY3RvclJlZi5tYXJrRm9yQ2hlY2soKTtcbiAgICAgICAgICB9KTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICAgICAgZmllbGQuZmllbGRHcm91cC5mb3JFYWNoKGYgPT4gb3B0aW9ucy5fbWFya0ZvckNoZWNrKGYpKTtcbiAgICAgICAgfVxuICAgICAgfTtcbiAgICB9XG5cbiAgICBpZiAoIW9wdGlvbnMuX2J1aWxkRmllbGQpIHtcbiAgICAgIG9wdGlvbnMuX2J1aWxkRmllbGQgPSAoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKSA9PiB7XG4gICAgICAgIHRoaXMuYnVpbGRGb3JtKGZpZWxkLmZvcm0sIGZpZWxkLmZpZWxkR3JvdXAsIGZpZWxkLm1vZGVsLCBmaWVsZC5vcHRpb25zKTtcbiAgICAgICAgcmV0dXJuIGZpZWxkO1xuICAgICAgfTtcbiAgICB9XG5cbiAgICByZXR1cm4gb3B0aW9ucztcbiAgfVxufVxuIl19