import { __decorate, __metadata, __param } from "tslib";
import { Injectable, ComponentFactoryResolver, Injector, Optional } from '@angular/core';
import { FormGroup, FormArray, FormGroupDirective } from '@angular/forms';
import { FormlyConfig } from './formly.config';
import { defineHiddenProp, reduceFormUpdateValidityCalls, observe } from '../utils';
import * as i0 from "@angular/core";
import * as i1 from "./formly.config";
import * as i2 from "@angular/forms";
var FormlyFormBuilder = /** @class */ (function () {
    function FormlyFormBuilder(config, resolver, injector, parentForm) {
        this.config = config;
        this.resolver = resolver;
        this.injector = injector;
        this.parentForm = parentForm;
    }
    FormlyFormBuilder.prototype.buildForm = function (form, fieldGroup, model, options) {
        if (fieldGroup === void 0) { fieldGroup = []; }
        this.build({ fieldGroup: fieldGroup, model: model, form: form, options: options });
    };
    FormlyFormBuilder.prototype.build = function (field) {
        var _this = this;
        if (!this.config.extensions.core) {
            throw new Error('NgxFormly: missing `forRoot()` call. use `forRoot()` when registering the `FormlyModule`.');
        }
        if (!field.parent) {
            this._setOptions(field);
            reduceFormUpdateValidityCalls(field.form, function () { return _this._build(field); });
            var options = field.options;
            options.checkExpressions && options.checkExpressions(field, true);
            options.detectChanges && options.detectChanges(field);
        }
        else {
            this._build(field);
        }
    };
    FormlyFormBuilder.prototype._build = function (field) {
        var _this = this;
        if (!field) {
            return;
        }
        this.getExtensions().forEach(function (extension) { return extension.prePopulate && extension.prePopulate(field); });
        this.getExtensions().forEach(function (extension) { return extension.onPopulate && extension.onPopulate(field); });
        if (field.fieldGroup) {
            field.fieldGroup.forEach(function (f) { return _this._build(f); });
        }
        this.getExtensions().forEach(function (extension) { return extension.postPopulate && extension.postPopulate(field); });
    };
    FormlyFormBuilder.prototype.getExtensions = function () {
        var _this = this;
        return Object.keys(this.config.extensions).map(function (name) { return _this.config.extensions[name]; });
    };
    FormlyFormBuilder.prototype._setOptions = function (field) {
        var _this = this;
        field.form = field.form || new FormGroup({});
        field.model = field.model || {};
        field.options = field.options || {};
        var options = field.options;
        if (!options._resolver) {
            defineHiddenProp(options, '_resolver', this.resolver);
        }
        if (!options._injector) {
            defineHiddenProp(options, '_injector', this.injector);
        }
        if (!options.build) {
            options._buildForm = function () {
                console.warn("Formly: 'options._buildForm' is deprecated since v6.0, use 'options.build' instead.");
                _this.build(field);
            };
            options.build = function (f) { return _this.build(f); };
        }
        if (!options.parentForm && this.parentForm) {
            defineHiddenProp(options, 'parentForm', this.parentForm);
            observe(options, ['parentForm', 'submitted'], function (_a) {
                var firstChange = _a.firstChange;
                if (!firstChange) {
                    options.checkExpressions(field);
                    options.detectChanges(field);
                }
            });
        }
    };
    FormlyFormBuilder.ctorParameters = function () { return [
        { type: FormlyConfig },
        { type: ComponentFactoryResolver },
        { type: Injector },
        { type: FormGroupDirective, decorators: [{ type: Optional }] }
    ]; };
    FormlyFormBuilder.ɵprov = i0.ɵɵdefineInjectable({ factory: function FormlyFormBuilder_Factory() { return new FormlyFormBuilder(i0.ɵɵinject(i1.FormlyConfig), i0.ɵɵinject(i0.ComponentFactoryResolver), i0.ɵɵinject(i0.INJECTOR), i0.ɵɵinject(i2.FormGroupDirective, 8)); }, token: FormlyFormBuilder, providedIn: "root" });
    FormlyFormBuilder = __decorate([
        Injectable({ providedIn: 'root' }),
        __param(3, Optional()),
        __metadata("design:paramtypes", [FormlyConfig,
            ComponentFactoryResolver,
            Injector,
            FormGroupDirective])
    ], FormlyFormBuilder);
    return FormlyFormBuilder;
}());
export { FormlyFormBuilder };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmJ1aWxkZXIuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL3NlcnZpY2VzL2Zvcm1seS5idWlsZGVyLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7QUFBQSxPQUFPLEVBQUUsVUFBVSxFQUFFLHdCQUF3QixFQUFFLFFBQVEsRUFBRSxRQUFRLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDekYsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUMxRSxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0saUJBQWlCLENBQUM7QUFFL0MsT0FBTyxFQUFFLGdCQUFnQixFQUFFLDZCQUE2QixFQUFFLE9BQU8sRUFBRSxNQUFNLFVBQVUsQ0FBQzs7OztBQUdwRjtJQUNFLDJCQUNVLE1BQW9CLEVBQ3BCLFFBQWtDLEVBQ2xDLFFBQWtCLEVBQ04sVUFBOEI7UUFIMUMsV0FBTSxHQUFOLE1BQU0sQ0FBYztRQUNwQixhQUFRLEdBQVIsUUFBUSxDQUEwQjtRQUNsQyxhQUFRLEdBQVIsUUFBUSxDQUFVO1FBQ04sZUFBVSxHQUFWLFVBQVUsQ0FBb0I7SUFDakQsQ0FBQztJQUVKLHFDQUFTLEdBQVQsVUFBVSxJQUEyQixFQUFFLFVBQW9DLEVBQUUsS0FBVSxFQUFFLE9BQTBCO1FBQTVFLDJCQUFBLEVBQUEsZUFBb0M7UUFDekUsSUFBSSxDQUFDLEtBQUssQ0FBQyxFQUFFLFVBQVUsWUFBQSxFQUFFLEtBQUssT0FBQSxFQUFFLElBQUksTUFBQSxFQUFFLE9BQU8sU0FBQSxFQUFFLENBQUMsQ0FBQztJQUNuRCxDQUFDO0lBRUQsaUNBQUssR0FBTCxVQUFNLEtBQXdCO1FBQTlCLGlCQWNDO1FBYkMsSUFBSSxDQUFDLElBQUksQ0FBQyxNQUFNLENBQUMsVUFBVSxDQUFDLElBQUksRUFBRTtZQUNoQyxNQUFNLElBQUksS0FBSyxDQUFDLDJGQUEyRixDQUFDLENBQUM7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LE9BQU8sQ0FBQyxJQUFJLENBQUMscUZBQXFGLENBQUMsQ0FBQztnQkFDcEcsS0FBSSxDQUFDLEtBQUssQ0FBQyxLQUFLLENBQUMsQ0FBQztZQUNwQixDQUFDLENBQUM7WUFDRixPQUFPLENBQUMsS0FBSyxHQUFHLFVBQUMsQ0FBb0IsSUFBSyxPQUFBLEtBQUksQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLEVBQWIsQ0FBYSxDQUFDO1NBQ3pEO1FBRUQsSUFBSSxDQUFDLE9BQU8sQ0FBQyxVQUFVLElBQUksSUFBSSxDQUFDLFVBQVUsRUFBRTtZQUMxQyxnQkFBZ0IsQ0FBQyxPQUFPLEVBQUUsWUFBWSxFQUFFLElBQUksQ0FBQyxVQUFVLENBQUMsQ0FBQztZQUN6RCxPQUFPLENBQUMsT0FBTyxFQUFFLENBQUMsWUFBWSxFQUFFLFdBQVcsQ0FBQyxFQUFFLFVBQUMsRUFBZTtvQkFBYiw0QkFBVztnQkFDMUQsSUFBSSxDQUFDLFdBQVcsRUFBRTtvQkFDaEIsT0FBTyxDQUFDLGdCQUFnQixDQUFDLEtBQUssQ0FBQyxDQUFDO29CQUNoQyxPQUFPLENBQUMsYUFBYSxDQUFDLEtBQUssQ0FBQyxDQUFDO2lCQUM5QjtZQUNILENBQUMsQ0FBQyxDQUFDO1NBQ0o7SUFDSCxDQUFDOztnQkE1RWlCLFlBQVk7Z0JBQ1Ysd0JBQXdCO2dCQUN4QixRQUFRO2dCQUNNLGtCQUFrQix1QkFBakQsUUFBUTs7O0lBTEEsaUJBQWlCO1FBRDdCLFVBQVUsQ0FBQyxFQUFFLFVBQVUsRUFBRSxNQUFNLEVBQUUsQ0FBQztRQU05QixXQUFBLFFBQVEsRUFBRSxDQUFBO3lDQUhLLFlBQVk7WUFDVix3QkFBd0I7WUFDeEIsUUFBUTtZQUNNLGtCQUFrQjtPQUx6QyxpQkFBaUIsQ0ErRTdCOzRCQXRGRDtDQXNGQyxBQS9FRCxJQStFQztTQS9FWSxpQkFBaUIiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbmplY3RhYmxlLCBDb21wb25lbnRGYWN0b3J5UmVzb2x2ZXIsIEluamVjdG9yLCBPcHRpb25hbCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRm9ybUdyb3VwLCBGb3JtQXJyYXksIEZvcm1Hcm91cERpcmVjdGl2ZSB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZyB9IGZyb20gJy4vZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZywgRm9ybWx5Rm9ybU9wdGlvbnMsIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi9tb2RlbHMnO1xuaW1wb3J0IHsgZGVmaW5lSGlkZGVuUHJvcCwgcmVkdWNlRm9ybVVwZGF0ZVZhbGlkaXR5Q2FsbHMsIG9ic2VydmUgfSBmcm9tICcuLi91dGlscyc7XG5cbkBJbmplY3RhYmxlKHsgcHJvdmlkZWRJbjogJ3Jvb3QnIH0pXG5leHBvcnQgY2xhc3MgRm9ybWx5Rm9ybUJ1aWxkZXIge1xuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGNvbmZpZzogRm9ybWx5Q29uZmlnLFxuICAgIHByaXZhdGUgcmVzb2x2ZXI6IENvbXBvbmVudEZhY3RvcnlSZXNvbHZlcixcbiAgICBwcml2YXRlIGluamVjdG9yOiBJbmplY3RvcixcbiAgICBAT3B0aW9uYWwoKSBwcml2YXRlIHBhcmVudEZvcm06IEZvcm1Hcm91cERpcmVjdGl2ZSxcbiAgKSB7fVxuXG4gIGJ1aWxkRm9ybShmb3JtOiBGb3JtR3JvdXAgfCBGb3JtQXJyYXksIGZpZWxkR3JvdXA6IEZvcm1seUZpZWxkQ29uZmlnW10gPSBbXSwgbW9kZWw6IGFueSwgb3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnMpIHtcbiAgICB0aGlzLmJ1aWxkKHsgZmllbGRHcm91cCwgbW9kZWwsIGZvcm0sIG9wdGlvbnMgfSk7XG4gIH1cblxuICBidWlsZChmaWVsZDogRm9ybWx5RmllbGRDb25maWcpIHtcbiAgICBpZiAoIXRoaXMuY29uZmlnLmV4dGVuc2lvbnMuY29yZSkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKCdOZ3hGb3JtbHk6IG1pc3NpbmcgYGZvclJvb3QoKWAgY2FsbC4gdXNlIGBmb3JSb290KClgIHdoZW4gcmVnaXN0ZXJpbmcgdGhlIGBGb3JtbHlNb2R1bGVgLicpO1xuICAgIH1cblxuICAgIGlmICghZmllbGQucGFyZW50KSB7XG4gICAgICB0aGlzLl9zZXRPcHRpb25zKGZpZWxkKTtcbiAgICAgIHJlZHVjZUZvcm1VcGRhdGVWYWxpZGl0eUNhbGxzKGZpZWxkLmZvcm0sICgpID0+IHRoaXMuX2J1aWxkKGZpZWxkKSk7XG4gICAgICBjb25zdCBvcHRpb25zID0gKGZpZWxkIGFzIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpLm9wdGlvbnM7XG4gICAgICBvcHRpb25zLmNoZWNrRXhwcmVzc2lvbnMgJiYgb3B0aW9ucy5jaGVja0V4cHJlc3Npb25zKGZpZWxkLCB0cnVlKTtcbiAgICAgIG9wdGlvbnMuZGV0ZWN0Q2hhbmdlcyAmJiBvcHRpb25zLmRldGVjdENoYW5nZXMoZmllbGQpO1xuICAgIH0gZWxzZSB7XG4gICAgICB0aGlzLl9idWlsZChmaWVsZCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBfYnVpbGQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoIWZpZWxkKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgdGhpcy5nZXRFeHRlbnNpb25zKCkuZm9yRWFjaCgoZXh0ZW5zaW9uKSA9PiBleHRlbnNpb24ucHJlUG9wdWxhdGUgJiYgZXh0ZW5zaW9uLnByZVBvcHVsYXRlKGZpZWxkKSk7XG4gICAgdGhpcy5nZXRFeHRlbnNpb25zKCkuZm9yRWFjaCgoZXh0ZW5zaW9uKSA9PiBleHRlbnNpb24ub25Qb3B1bGF0ZSAmJiBleHRlbnNpb24ub25Qb3B1bGF0ZShmaWVsZCkpO1xuXG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLmZpZWxkR3JvdXAuZm9yRWFjaCgoZikgPT4gdGhpcy5fYnVpbGQoZikpO1xuICAgIH1cblxuICAgIHRoaXMuZ2V0RXh0ZW5zaW9ucygpLmZvckVhY2goKGV4dGVuc2lvbikgPT4gZXh0ZW5zaW9uLnBvc3RQb3B1bGF0ZSAmJiBleHRlbnNpb24ucG9zdFBvcHVsYXRlKGZpZWxkKSk7XG4gIH1cblxuICBwcml2YXRlIGdldEV4dGVuc2lvbnMoKSB7XG4gICAgcmV0dXJuIE9iamVjdC5rZXlzKHRoaXMuY29uZmlnLmV4dGVuc2lvbnMpLm1hcCgobmFtZSkgPT4gdGhpcy5jb25maWcuZXh0ZW5zaW9uc1tuYW1lXSk7XG4gIH1cblxuICBwcml2YXRlIF9zZXRPcHRpb25zKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgZmllbGQuZm9ybSA9IGZpZWxkLmZvcm0gfHwgbmV3IEZvcm1Hcm91cCh7fSk7XG4gICAgZmllbGQubW9kZWwgPSBmaWVsZC5tb2RlbCB8fCB7fTtcbiAgICBmaWVsZC5vcHRpb25zID0gZmllbGQub3B0aW9ucyB8fCB7fTtcbiAgICBjb25zdCBvcHRpb25zID0gZmllbGQub3B0aW9ucztcblxuICAgIGlmICghb3B0aW9ucy5fcmVzb2x2ZXIpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19yZXNvbHZlcicsIHRoaXMucmVzb2x2ZXIpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5faW5qZWN0b3IpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19pbmplY3RvcicsIHRoaXMuaW5qZWN0b3IpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5idWlsZCkge1xuICAgICAgb3B0aW9ucy5fYnVpbGRGb3JtID0gKCkgPT4ge1xuICAgICAgICBjb25zb2xlLndhcm4oYEZvcm1seTogJ29wdGlvbnMuX2J1aWxkRm9ybScgaXMgZGVwcmVjYXRlZCBzaW5jZSB2Ni4wLCB1c2UgJ29wdGlvbnMuYnVpbGQnIGluc3RlYWQuYCk7XG4gICAgICAgIHRoaXMuYnVpbGQoZmllbGQpO1xuICAgICAgfTtcbiAgICAgIG9wdGlvbnMuYnVpbGQgPSAoZjogRm9ybWx5RmllbGRDb25maWcpID0+IHRoaXMuYnVpbGQoZik7XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLnBhcmVudEZvcm0gJiYgdGhpcy5wYXJlbnRGb3JtKSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKG9wdGlvbnMsICdwYXJlbnRGb3JtJywgdGhpcy5wYXJlbnRGb3JtKTtcbiAgICAgIG9ic2VydmUob3B0aW9ucywgWydwYXJlbnRGb3JtJywgJ3N1Ym1pdHRlZCddLCAoeyBmaXJzdENoYW5nZSB9KSA9PiB7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UpIHtcbiAgICAgICAgICBvcHRpb25zLmNoZWNrRXhwcmVzc2lvbnMoZmllbGQpO1xuICAgICAgICAgIG9wdGlvbnMuZGV0ZWN0Q2hhbmdlcyhmaWVsZCk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH1cbiAgfVxufVxuIl19