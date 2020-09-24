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
var FormlyFormBuilder = /** @class */ (function () {
    function FormlyFormBuilder(formlyConfig, componentFactoryResolver, injector) {
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
    FormlyFormBuilder.prototype.buildForm = /**
     * @param {?} formControl
     * @param {?=} fieldGroup
     * @param {?=} model
     * @param {?=} options
     * @return {?}
     */
    function (formControl, fieldGroup, model, options) {
        var _this = this;
        if (fieldGroup === void 0) { fieldGroup = []; }
        if (!this.formlyConfig.extensions.core) {
            throw new Error('NgxFormly: missing `forRoot()` call. use `forRoot()` when registering the `FormlyModule`.');
        }
        /** @type {?} */
        var field = { fieldGroup: fieldGroup, model: model, formControl: formControl, options: this._setOptions(options) };
        reduceFormUpdateValidityCalls(formControl, (/**
         * @return {?}
         */
        function () { return _this._buildForm(field); }));
        field.options._checkField(field, true);
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    FormlyFormBuilder.prototype._buildForm = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        function (extension) { return extension.prePopulate && extension.prePopulate(field); }));
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        function (extension) { return extension.onPopulate && extension.onPopulate(field); }));
        if (field.fieldGroup) {
            field.fieldGroup.forEach((/**
             * @param {?} f
             * @return {?}
             */
            function (f) { return _this._buildForm(f); }));
        }
        this.getExtensions().forEach((/**
         * @param {?} extension
         * @return {?}
         */
        function (extension) { return extension.postPopulate && extension.postPopulate(field); }));
    };
    /**
     * @private
     * @return {?}
     */
    FormlyFormBuilder.prototype.getExtensions = /**
     * @private
     * @return {?}
     */
    function () {
        var _this = this;
        return Object.keys(this.formlyConfig.extensions).map((/**
         * @param {?} name
         * @return {?}
         */
        function (name) { return _this.formlyConfig.extensions[name]; }));
    };
    /**
     * @private
     * @param {?} options
     * @return {?}
     */
    FormlyFormBuilder.prototype._setOptions = /**
     * @private
     * @param {?} options
     * @return {?}
     */
    function (options) {
        var _this = this;
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
            function (field) {
                if (field._componentRefs) {
                    field._componentRefs.forEach((/**
                     * @param {?} ref
                     * @return {?}
                     */
                    function (ref) {
                        // NOTE: we cannot use ref.changeDetectorRef, see https://github.com/ngx-formly/ngx-formly/issues/2191
                        /** @type {?} */
                        var changeDetectorRef = ref.injector.get(ChangeDetectorRef);
                        changeDetectorRef.markForCheck();
                    }));
                }
                if (field.fieldGroup) {
                    field.fieldGroup.forEach((/**
                     * @param {?} f
                     * @return {?}
                     */
                    function (f) { return options._markForCheck(f); }));
                }
            });
        }
        if (!options._buildField) {
            options._buildField = (/**
             * @param {?} field
             * @return {?}
             */
            function (field) {
                _this.buildForm(field.form, field.fieldGroup, field.model, field.options);
                return field;
            });
        }
        return options;
    };
    FormlyFormBuilder.decorators = [
        { type: Injectable, args: [{ providedIn: 'root' },] }
    ];
    /** @nocollapse */
    FormlyFormBuilder.ctorParameters = function () { return [
        { type: FormlyConfig },
        { type: ComponentFactoryResolver },
        { type: Injector }
    ]; };
    /** @nocollapse */ FormlyFormBuilder.ngInjectableDef = i0.defineInjectable({ factory: function FormlyFormBuilder_Factory() { return new FormlyFormBuilder(i0.inject(i1.FormlyConfig), i0.inject(i0.ComponentFactoryResolver), i0.inject(i0.INJECTOR)); }, token: FormlyFormBuilder, providedIn: "root" });
    return FormlyFormBuilder;
}());
export { FormlyFormBuilder };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmZvcm0uYnVpbGRlci5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlci50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7O0FBQUEsT0FBTyxFQUFFLFVBQVUsRUFBRSx3QkFBd0IsRUFBRSxRQUFRLEVBQUUsaUJBQWlCLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFFbEcsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLGlCQUFpQixDQUFDO0FBRS9DLE9BQU8sRUFBRSxPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFDL0IsT0FBTyxFQUFFLGdCQUFnQixFQUFFLDZCQUE2QixFQUFFLE1BQU0sVUFBVSxDQUFDOzs7QUFFM0U7SUFFRSwyQkFDVSxZQUEwQixFQUMxQix3QkFBa0QsRUFDbEQsUUFBa0I7UUFGbEIsaUJBQVksR0FBWixZQUFZLENBQWM7UUFDMUIsNkJBQXdCLEdBQXhCLHdCQUF3QixDQUEwQjtRQUNsRCxhQUFRLEdBQVIsUUFBUSxDQUFVO0lBQ3pCLENBQUM7Ozs7Ozs7O0lBRUoscUNBQVM7Ozs7Ozs7SUFBVCxVQUFVLFdBQWtDLEVBQUUsVUFBb0MsRUFBRSxLQUFVLEVBQUUsT0FBMEI7UUFBMUgsaUJBUUM7UUFSNkMsMkJBQUEsRUFBQSxlQUFvQztRQUNoRixJQUFJLENBQUMsSUFBSSxDQUFDLFlBQVksQ0FBQyxVQUFVLENBQUMsSUFBSSxFQUFFO1lBQ3RDLE1BQU0sSUFBSSxLQUFLLENBQUMsMkZBQTJGLENBQUMsQ0FBQztTQUM5Rzs7WUFFSyxLQUFLLEdBQUcsRUFBRSxVQUFVLFlBQUEsRUFBRSxLQUFLLE9BQUEsRUFBRSxXQUFXLGFBQUEsRUFBRSxPQUFPLEVBQUUsSUFBSSxDQUFDLFdBQVcsQ0FBQyxPQUFPLENBQUMsRUFBRTtRQUNwRiw2QkFBNkIsQ0FBQyxXQUFXOzs7UUFBRSxjQUFNLE9BQUEsS0FBSSxDQUFDLFVBQVUsQ0FB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LGlCQUFpQixDQUFDLFlBQVksRUFBRSxDQUFDO29CQUNuQyxDQUFDLEVBQUMsQ0FBQztpQkFDSjtnQkFFRCxJQUFJLEtBQUssQ0FBQyxVQUFVLEVBQUU7b0JBQ3BCLEtBQUssQ0FBQyxVQUFVLENBQUMsT0FBTzs7OztvQkFBQyxVQUFBLENBQUMsSUFBSSxPQUFBLE9BQU8sQ0FBQyxhQUFhLENBQUMsQ0FBQyxDQUFDLEVBQXhCLENBQXdCLEVBQUMsQ0FBQztpQkFDekQ7WUFDSCxDQUFDLENBQUEsQ0FBQztTQUNIO1FBRUQsSUFBSSxDQUFDLE9BQU8sQ0FBQyxXQUFXLEVBQUU7WUFDeEIsT0FBTyxDQUFDLFdBQVc7Ozs7WUFBRyxVQUFDLEtBQXdCO2dCQUM3QyxLQUFJLENBQUMsU0FBUyxDQUFDLEtBQUssQ0FBQyxJQUFJLEVBQUUsS0FBSyxDQUFDLFVBQVUsRUFBRSxLQUFLLENBQUMsS0FBSyxFQUFFLEtBQUssQ0FBQyxPQUFPLENBQUMsQ0FBQztnQkFDekUsT0FBTyxLQUFLLENBQUM7WUFDZixDQUFDLENBQUEsQ0FBQztTQUNIO1FBRUQsT0FBTyxPQUFPLENBQUM7SUFDakIsQ0FBQzs7Z0JBakZGLFVBQVUsU0FBQyxFQUFFLFVBQVUsRUFBRSxNQUFNLEVBQUU7Ozs7Z0JBTHpCLFlBQVk7Z0JBRkEsd0JBQXdCO2dCQUFFLFFBQVE7Ozs0QkFBdkQ7Q0F5RkMsQUFsRkQsSUFrRkM7U0FqRlksaUJBQWlCOzs7Ozs7SUFFMUIseUNBQWtDOzs7OztJQUNsQyxxREFBMEQ7Ozs7O0lBQzFELHFDQUEwQiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEluamVjdGFibGUsIENvbXBvbmVudEZhY3RvcnlSZXNvbHZlciwgSW5qZWN0b3IsIENoYW5nZURldGVjdG9yUmVmIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGb3JtR3JvdXAsIEZvcm1BcnJheSB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZyB9IGZyb20gJy4vZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZywgRm9ybWx5Rm9ybU9wdGlvbnMsIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIEZvcm1seVZhbHVlQ2hhbmdlRXZlbnQsIEZvcm1seUZvcm1PcHRpb25zQ2FjaGUgfSBmcm9tICcuLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgU3ViamVjdCB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgZGVmaW5lSGlkZGVuUHJvcCwgcmVkdWNlRm9ybVVwZGF0ZVZhbGlkaXR5Q2FsbHMgfSBmcm9tICcuLi91dGlscyc7XG5cbkBJbmplY3RhYmxlKHsgcHJvdmlkZWRJbjogJ3Jvb3QnIH0pXG5leHBvcnQgY2xhc3MgRm9ybWx5Rm9ybUJ1aWxkZXIge1xuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGZvcm1seUNvbmZpZzogRm9ybWx5Q29uZmlnLFxuICAgIHByaXZhdGUgY29tcG9uZW50RmFjdG9yeVJlc29sdmVyOiBDb21wb25lbnRGYWN0b3J5UmVzb2x2ZXIsXG4gICAgcHJpdmF0ZSBpbmplY3RvcjogSW5qZWN0b3IsXG4gICkge31cblxuICBidWlsZEZvcm0oZm9ybUNvbnRyb2w6IEZvcm1Hcm91cCB8IEZvcm1BcnJheSwgZmllbGRHcm91cDogRm9ybWx5RmllbGRDb25maWdbXSA9IFtdLCBtb2RlbDogYW55LCBvcHRpb25zOiBGb3JtbHlGb3JtT3B0aW9ucykge1xuICAgIGlmICghdGhpcy5mb3JtbHlDb25maWcuZXh0ZW5zaW9ucy5jb3JlKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoJ05neEZvcm1seTogbWlzc2luZyBgZm9yUm9vdCgpYCBjYWxsLiB1c2UgYGZvclJvb3QoKWAgd2hlbiByZWdpc3RlcmluZyB0aGUgYEZvcm1seU1vZHVsZWAuJyk7XG4gICAgfVxuXG4gICAgY29uc3QgZmllbGQgPSB7IGZpZWxkR3JvdXAsIG1vZGVsLCBmb3JtQ29udHJvbCwgb3B0aW9uczogdGhpcy5fc2V0T3B0aW9ucyhvcHRpb25zKSB9O1xuICAgIHJlZHVjZUZvcm1VcGRhdGVWYWxpZGl0eUNhbGxzKGZvcm1Db250cm9sLCAoKSA9PiB0aGlzLl9idWlsZEZvcm0oZmllbGQpKTtcbiAgICBmaWVsZC5vcHRpb25zLl9jaGVja0ZpZWxkKGZpZWxkLCB0cnVlKTtcbiAgfVxuXG4gIHByaXZhdGUgX2J1aWxkRm9ybShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIHRoaXMuZ2V0RXh0ZW5zaW9ucygpLmZvckVhY2goZXh0ZW5zaW9uID0+IGV4dGVuc2lvbi5wcmVQb3B1bGF0ZSAmJiBleHRlbnNpb24ucHJlUG9wdWxhdGUoZmllbGQpKTtcbiAgICB0aGlzLmdldEV4dGVuc2lvbnMoKS5mb3JFYWNoKGV4dGVuc2lvbiA9PiBleHRlbnNpb24ub25Qb3B1bGF0ZSAmJiBleHRlbnNpb24ub25Qb3B1bGF0ZShmaWVsZCkpO1xuXG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLmZpZWxkR3JvdXAuZm9yRWFjaCgoZikgPT4gdGhpcy5fYnVpbGRGb3JtKGYpKTtcbiAgICB9XG5cbiAgICB0aGlzLmdldEV4dGVuc2lvbnMoKS5mb3JFYWNoKGV4dGVuc2lvbiA9PiBleHRlbnNpb24ucG9zdFBvcHVsYXRlICYmIGV4dGVuc2lvbi5wb3N0UG9wdWxhdGUoZmllbGQpKTtcbiAgfVxuXG4gIHByaXZhdGUgZ2V0RXh0ZW5zaW9ucygpIHtcbiAgICByZXR1cm4gT2JqZWN0LmtleXModGhpcy5mb3JtbHlDb25maWcuZXh0ZW5zaW9ucykubWFwKG5hbWUgPT4gdGhpcy5mb3JtbHlDb25maWcuZXh0ZW5zaW9uc1tuYW1lXSk7XG4gIH1cblxuICBwcml2YXRlIF9zZXRPcHRpb25zKG9wdGlvbnM6IEZvcm1seUZvcm1PcHRpb25zQ2FjaGUpIHtcbiAgICBvcHRpb25zID0gb3B0aW9ucyB8fCB7fTtcbiAgICBvcHRpb25zLmZvcm1TdGF0ZSA9IG9wdGlvbnMuZm9ybVN0YXRlIHx8IHt9O1xuXG4gICAgaWYgKCFvcHRpb25zLnNob3dFcnJvcikge1xuICAgICAgb3B0aW9ucy5zaG93RXJyb3IgPSB0aGlzLmZvcm1seUNvbmZpZy5leHRyYXMuc2hvd0Vycm9yO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5maWVsZENoYW5nZXMpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ2ZpZWxkQ2hhbmdlcycsIG5ldyBTdWJqZWN0PEZvcm1seVZhbHVlQ2hhbmdlRXZlbnQ+KCkpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5fcmVzb2x2ZXIpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19yZXNvbHZlcicsIHRoaXMuY29tcG9uZW50RmFjdG9yeVJlc29sdmVyKTtcbiAgICB9XG5cbiAgICBpZiAoIW9wdGlvbnMuX2luamVjdG9yKSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKG9wdGlvbnMsICdfaW5qZWN0b3InLCB0aGlzLmluamVjdG9yKTtcbiAgICB9XG5cbiAgICBpZiAoIW9wdGlvbnMuX2hpZGRlbkZpZWxkc0ZvckNoZWNrKSB7XG4gICAgICBvcHRpb25zLl9oaWRkZW5GaWVsZHNGb3JDaGVjayA9IFtdO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5fbWFya0ZvckNoZWNrKSB7XG4gICAgICBvcHRpb25zLl9tYXJrRm9yQ2hlY2sgPSAoZmllbGQpID0+IHtcbiAgICAgICAgaWYgKGZpZWxkLl9jb21wb25lbnRSZWZzKSB7XG4gICAgICAgICAgZmllbGQuX2NvbXBvbmVudFJlZnMuZm9yRWFjaChyZWYgPT4ge1xuICAgICAgICAgICAgLy8gTk9URTogd2UgY2Fubm90IHVzZSByZWYuY2hhbmdlRGV0ZWN0b3JSZWYsIHNlZSBodHRwczovL2dpdGh1Yi5jb20vbmd4LWZvcm1seS9uZ3gtZm9ybWx5L2lzc3Vlcy8yMTkxXG4gICAgICAgICAgICBjb25zdCBjaGFuZ2VEZXRlY3RvclJlZiA9IHJlZi5pbmplY3Rvci5nZXQoQ2hhbmdlRGV0ZWN0b3JSZWYpO1xuICAgICAgICAgICAgY2hhbmdlRGV0ZWN0b3JSZWYubWFya0ZvckNoZWNrKCk7XG4gICAgICAgICAgfSk7XG4gICAgICAgIH1cblxuICAgICAgICBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgICAgIGZpZWxkLmZpZWxkR3JvdXAuZm9yRWFjaChmID0+IG9wdGlvbnMuX21hcmtGb3JDaGVjayhmKSk7XG4gICAgICAgIH1cbiAgICAgIH07XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLl9idWlsZEZpZWxkKSB7XG4gICAgICBvcHRpb25zLl9idWlsZEZpZWxkID0gKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykgPT4ge1xuICAgICAgICB0aGlzLmJ1aWxkRm9ybShmaWVsZC5mb3JtLCBmaWVsZC5maWVsZEdyb3VwLCBmaWVsZC5tb2RlbCwgZmllbGQub3B0aW9ucyk7XG4gICAgICAgIHJldHVybiBmaWVsZDtcbiAgICAgIH07XG4gICAgfVxuXG4gICAgcmV0dXJuIG9wdGlvbnM7XG4gIH1cbn1cbiJdfQ==