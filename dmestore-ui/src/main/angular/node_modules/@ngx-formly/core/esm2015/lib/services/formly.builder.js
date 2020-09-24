import { __decorate, __metadata, __param } from "tslib";
import { Injectable, ComponentFactoryResolver, Injector, Optional } from '@angular/core';
import { FormGroup, FormArray, FormGroupDirective } from '@angular/forms';
import { FormlyConfig } from './formly.config';
import { defineHiddenProp, reduceFormUpdateValidityCalls, observe } from '../utils';
import * as i0 from "@angular/core";
import * as i1 from "./formly.config";
import * as i2 from "@angular/forms";
let FormlyFormBuilder = class FormlyFormBuilder {
    constructor(config, resolver, injector, parentForm) {
        this.config = config;
        this.resolver = resolver;
        this.injector = injector;
        this.parentForm = parentForm;
    }
    buildForm(form, fieldGroup = [], model, options) {
        this.build({ fieldGroup, model, form, options });
    }
    build(field) {
        if (!this.config.extensions.core) {
            throw new Error('NgxFormly: missing `forRoot()` call. use `forRoot()` when registering the `FormlyModule`.');
        }
        if (!field.parent) {
            this._setOptions(field);
            reduceFormUpdateValidityCalls(field.form, () => this._build(field));
            const options = field.options;
            options.checkExpressions && options.checkExpressions(field, true);
            options.detectChanges && options.detectChanges(field);
        }
        else {
            this._build(field);
        }
    }
    _build(field) {
        if (!field) {
            return;
        }
        this.getExtensions().forEach((extension) => extension.prePopulate && extension.prePopulate(field));
        this.getExtensions().forEach((extension) => extension.onPopulate && extension.onPopulate(field));
        if (field.fieldGroup) {
            field.fieldGroup.forEach((f) => this._build(f));
        }
        this.getExtensions().forEach((extension) => extension.postPopulate && extension.postPopulate(field));
    }
    getExtensions() {
        return Object.keys(this.config.extensions).map((name) => this.config.extensions[name]);
    }
    _setOptions(field) {
        field.form = field.form || new FormGroup({});
        field.model = field.model || {};
        field.options = field.options || {};
        const options = field.options;
        if (!options._resolver) {
            defineHiddenProp(options, '_resolver', this.resolver);
        }
        if (!options._injector) {
            defineHiddenProp(options, '_injector', this.injector);
        }
        if (!options.build) {
            options._buildForm = () => {
                console.warn(`Formly: 'options._buildForm' is deprecated since v6.0, use 'options.build' instead.`);
                this.build(field);
            };
            options.build = (f) => this.build(f);
        }
        if (!options.parentForm && this.parentForm) {
            defineHiddenProp(options, 'parentForm', this.parentForm);
            observe(options, ['parentForm', 'submitted'], ({ firstChange }) => {
                if (!firstChange) {
                    options.checkExpressions(field);
                    options.detectChanges(field);
                }
            });
        }
    }
};
FormlyFormBuilder.ctorParameters = () => [
    { type: FormlyConfig },
    { type: ComponentFactoryResolver },
    { type: Injector },
    { type: FormGroupDirective, decorators: [{ type: Optional }] }
];
FormlyFormBuilder.ɵprov = i0.ɵɵdefineInjectable({ factory: function FormlyFormBuilder_Factory() { return new FormlyFormBuilder(i0.ɵɵinject(i1.FormlyConfig), i0.ɵɵinject(i0.ComponentFactoryResolver), i0.ɵɵinject(i0.INJECTOR), i0.ɵɵinject(i2.FormGroupDirective, 8)); }, token: FormlyFormBuilder, providedIn: "root" });
FormlyFormBuilder = __decorate([
    Injectable({ providedIn: 'root' }),
    __param(3, Optional()),
    __metadata("design:paramtypes", [FormlyConfig,
        ComponentFactoryResolver,
        Injector,
        FormGroupDirective])
], FormlyFormBuilder);
export { FormlyFormBuilder };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5LmJ1aWxkZXIuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL3NlcnZpY2VzL2Zvcm1seS5idWlsZGVyLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7QUFBQSxPQUFPLEVBQUUsVUFBVSxFQUFFLHdCQUF3QixFQUFFLFFBQVEsRUFBRSxRQUFRLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDekYsT0FBTyxFQUFFLFNBQVMsRUFBRSxTQUFTLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUMxRSxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0saUJBQWlCLENBQUM7QUFFL0MsT0FBTyxFQUFFLGdCQUFnQixFQUFFLDZCQUE2QixFQUFFLE9BQU8sRUFBRSxNQUFNLFVBQVUsQ0FBQzs7OztBQUdwRixJQUFhLGlCQUFpQixHQUE5QixNQUFhLGlCQUFpQjtJQUM1QixZQUNVLE1BQW9CLEVBQ3BCLFFBQWtDLEVBQ2xDLFFBQWtCLEVBQ04sVUFBOEI7UUFIMUMsV0FBTSxHQUFOLE1BQU0sQ0FBYztRQUNwQixhQUFRLEdBQVIsUUFBUSxDQUEwQjtRQUNsQyxhQUFRLEdBQVIsUUFBUSxDQUFVO1FBQ04sZUFBVSxHQUFWLFVBQVUsQ0FBb0I7SUFDakQsQ0FBQztJQUVKLFNBQVMsQ0FBQyxJQUEyQixFQUFFLGFBQWtDLEVBQUUsRUFBRSxLQUFVLEVBQUUsT0FBMEI7UUFDakgsSUFBSSxDQUFDLEtBQUssQ0FBQyxFQUFFLFVBQVUsRUFBRSxLQUFLLEVBQUUsSUFBSSxFQUFFLE9BQU8sRUFBRSxDQUFDLENBQUM7SUFDbkQsQ0FBQztJQUVELEtBQUssQ0FBQyxLQUF3QjtRQUM1QixJQUFJLENBQUMsSUFBSSxDQUFDLE1BQU0sQ0FBQyxVQUFVLENBQUMsSUFBSSxFQUFFO1lBQ2hDLE1BQU0sSUFBSSxLQUFLLENBQUMsMkZBQTJGLENBQUMsQ0FBQztTQUM5RztRQUVELElBQUksQ0FBQyxLQUFLLENBQUMs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R0FBRyxDQUFDLENBQW9CLEVBQUUsRUFBRSxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUM7U0FDekQ7UUFFRCxJQUFJLENBQUMsT0FBTyxDQUFDLFVBQVUsSUFBSSxJQUFJLENBQUMsVUFBVSxFQUFFO1lBQzFDLGdCQUFnQixDQUFDLE9BQU8sRUFBRSxZQUFZLEVBQUUsSUFBSSxDQUFDLFVBQVUsQ0FBQyxDQUFDO1lBQ3pELE9BQU8sQ0FBQyxPQUFPLEVBQUUsQ0FBQyxZQUFZLEVBQUUsV0FBVyxDQUFDLEVBQUUsQ0FBQyxFQUFFLFdBQVcsRUFBRSxFQUFFLEVBQUU7Z0JBQ2hFLElBQUksQ0FBQyxXQUFXLEVBQUU7b0JBQ2hCLE9BQU8sQ0FBQyxnQkFBZ0IsQ0FBQyxLQUFLLENBQUMsQ0FBQztvQkFDaEMsT0FBTyxDQUFDLGFBQWEsQ0FBQyxLQUFLLENBQUMsQ0FBQztpQkFDOUI7WUFDSCxDQUFDLENBQUMsQ0FBQztTQUNKO0lBQ0gsQ0FBQztDQUNGLENBQUE7O1lBN0VtQixZQUFZO1lBQ1Ysd0JBQXdCO1lBQ3hCLFFBQVE7WUFDTSxrQkFBa0IsdUJBQWpELFFBQVE7OztBQUxBLGlCQUFpQjtJQUQ3QixVQUFVLENBQUMsRUFBRSxVQUFVLEVBQUUsTUFBTSxFQUFFLENBQUM7SUFNOUIsV0FBQSxRQUFRLEVBQUUsQ0FBQTtxQ0FISyxZQUFZO1FBQ1Ysd0JBQXdCO1FBQ3hCLFFBQVE7UUFDTSxrQkFBa0I7R0FMekMsaUJBQWlCLENBK0U3QjtTQS9FWSxpQkFBaUIiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbmplY3RhYmxlLCBDb21wb25lbnRGYWN0b3J5UmVzb2x2ZXIsIEluamVjdG9yLCBPcHRpb25hbCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRm9ybUdyb3VwLCBGb3JtQXJyYXksIEZvcm1Hcm91cERpcmVjdGl2ZSB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZyB9IGZyb20gJy4vZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZywgRm9ybWx5Rm9ybU9wdGlvbnMsIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUgfSBmcm9tICcuLi9tb2RlbHMnO1xuaW1wb3J0IHsgZGVmaW5lSGlkZGVuUHJvcCwgcmVkdWNlRm9ybVVwZGF0ZVZhbGlkaXR5Q2FsbHMsIG9ic2VydmUgfSBmcm9tICcuLi91dGlscyc7XG5cbkBJbmplY3RhYmxlKHsgcHJvdmlkZWRJbjogJ3Jvb3QnIH0pXG5leHBvcnQgY2xhc3MgRm9ybWx5Rm9ybUJ1aWxkZXIge1xuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGNvbmZpZzogRm9ybWx5Q29uZmlnLFxuICAgIHByaXZhdGUgcmVzb2x2ZXI6IENvbXBvbmVudEZhY3RvcnlSZXNvbHZlcixcbiAgICBwcml2YXRlIGluamVjdG9yOiBJbmplY3RvcixcbiAgICBAT3B0aW9uYWwoKSBwcml2YXRlIHBhcmVudEZvcm06IEZvcm1Hcm91cERpcmVjdGl2ZSxcbiAgKSB7fVxuXG4gIGJ1aWxkRm9ybShmb3JtOiBGb3JtR3JvdXAgfCBGb3JtQXJyYXksIGZpZWxkR3JvdXA6IEZvcm1seUZpZWxkQ29uZmlnW10gPSBbXSwgbW9kZWw6IGFueSwgb3B0aW9uczogRm9ybWx5Rm9ybU9wdGlvbnMpIHtcbiAgICB0aGlzLmJ1aWxkKHsgZmllbGRHcm91cCwgbW9kZWwsIGZvcm0sIG9wdGlvbnMgfSk7XG4gIH1cblxuICBidWlsZChmaWVsZDogRm9ybWx5RmllbGRDb25maWcpIHtcbiAgICBpZiAoIXRoaXMuY29uZmlnLmV4dGVuc2lvbnMuY29yZSkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKCdOZ3hGb3JtbHk6IG1pc3NpbmcgYGZvclJvb3QoKWAgY2FsbC4gdXNlIGBmb3JSb290KClgIHdoZW4gcmVnaXN0ZXJpbmcgdGhlIGBGb3JtbHlNb2R1bGVgLicpO1xuICAgIH1cblxuICAgIGlmICghZmllbGQucGFyZW50KSB7XG4gICAgICB0aGlzLl9zZXRPcHRpb25zKGZpZWxkKTtcbiAgICAgIHJlZHVjZUZvcm1VcGRhdGVWYWxpZGl0eUNhbGxzKGZpZWxkLmZvcm0sICgpID0+IHRoaXMuX2J1aWxkKGZpZWxkKSk7XG4gICAgICBjb25zdCBvcHRpb25zID0gKGZpZWxkIGFzIEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpLm9wdGlvbnM7XG4gICAgICBvcHRpb25zLmNoZWNrRXhwcmVzc2lvbnMgJiYgb3B0aW9ucy5jaGVja0V4cHJlc3Npb25zKGZpZWxkLCB0cnVlKTtcbiAgICAgIG9wdGlvbnMuZGV0ZWN0Q2hhbmdlcyAmJiBvcHRpb25zLmRldGVjdENoYW5nZXMoZmllbGQpO1xuICAgIH0gZWxzZSB7XG4gICAgICB0aGlzLl9idWlsZChmaWVsZCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBfYnVpbGQoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBpZiAoIWZpZWxkKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgdGhpcy5nZXRFeHRlbnNpb25zKCkuZm9yRWFjaCgoZXh0ZW5zaW9uKSA9PiBleHRlbnNpb24ucHJlUG9wdWxhdGUgJiYgZXh0ZW5zaW9uLnByZVBvcHVsYXRlKGZpZWxkKSk7XG4gICAgdGhpcy5nZXRFeHRlbnNpb25zKCkuZm9yRWFjaCgoZXh0ZW5zaW9uKSA9PiBleHRlbnNpb24ub25Qb3B1bGF0ZSAmJiBleHRlbnNpb24ub25Qb3B1bGF0ZShmaWVsZCkpO1xuXG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLmZpZWxkR3JvdXAuZm9yRWFjaCgoZikgPT4gdGhpcy5fYnVpbGQoZikpO1xuICAgIH1cblxuICAgIHRoaXMuZ2V0RXh0ZW5zaW9ucygpLmZvckVhY2goKGV4dGVuc2lvbikgPT4gZXh0ZW5zaW9uLnBvc3RQb3B1bGF0ZSAmJiBleHRlbnNpb24ucG9zdFBvcHVsYXRlKGZpZWxkKSk7XG4gIH1cblxuICBwcml2YXRlIGdldEV4dGVuc2lvbnMoKSB7XG4gICAgcmV0dXJuIE9iamVjdC5rZXlzKHRoaXMuY29uZmlnLmV4dGVuc2lvbnMpLm1hcCgobmFtZSkgPT4gdGhpcy5jb25maWcuZXh0ZW5zaW9uc1tuYW1lXSk7XG4gIH1cblxuICBwcml2YXRlIF9zZXRPcHRpb25zKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgZmllbGQuZm9ybSA9IGZpZWxkLmZvcm0gfHwgbmV3IEZvcm1Hcm91cCh7fSk7XG4gICAgZmllbGQubW9kZWwgPSBmaWVsZC5tb2RlbCB8fCB7fTtcbiAgICBmaWVsZC5vcHRpb25zID0gZmllbGQub3B0aW9ucyB8fCB7fTtcbiAgICBjb25zdCBvcHRpb25zID0gZmllbGQub3B0aW9ucztcblxuICAgIGlmICghb3B0aW9ucy5fcmVzb2x2ZXIpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19yZXNvbHZlcicsIHRoaXMucmVzb2x2ZXIpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5faW5qZWN0b3IpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3Aob3B0aW9ucywgJ19pbmplY3RvcicsIHRoaXMuaW5qZWN0b3IpO1xuICAgIH1cblxuICAgIGlmICghb3B0aW9ucy5idWlsZCkge1xuICAgICAgb3B0aW9ucy5fYnVpbGRGb3JtID0gKCkgPT4ge1xuICAgICAgICBjb25zb2xlLndhcm4oYEZvcm1seTogJ29wdGlvbnMuX2J1aWxkRm9ybScgaXMgZGVwcmVjYXRlZCBzaW5jZSB2Ni4wLCB1c2UgJ29wdGlvbnMuYnVpbGQnIGluc3RlYWQuYCk7XG4gICAgICAgIHRoaXMuYnVpbGQoZmllbGQpO1xuICAgICAgfTtcbiAgICAgIG9wdGlvbnMuYnVpbGQgPSAoZjogRm9ybWx5RmllbGRDb25maWcpID0+IHRoaXMuYnVpbGQoZik7XG4gICAgfVxuXG4gICAgaWYgKCFvcHRpb25zLnBhcmVudEZvcm0gJiYgdGhpcy5wYXJlbnRGb3JtKSB7XG4gICAgICBkZWZpbmVIaWRkZW5Qcm9wKG9wdGlvbnMsICdwYXJlbnRGb3JtJywgdGhpcy5wYXJlbnRGb3JtKTtcbiAgICAgIG9ic2VydmUob3B0aW9ucywgWydwYXJlbnRGb3JtJywgJ3N1Ym1pdHRlZCddLCAoeyBmaXJzdENoYW5nZSB9KSA9PiB7XG4gICAgICAgIGlmICghZmlyc3RDaGFuZ2UpIHtcbiAgICAgICAgICBvcHRpb25zLmNoZWNrRXhwcmVzc2lvbnMoZmllbGQpO1xuICAgICAgICAgIG9wdGlvbnMuZGV0ZWN0Q2hhbmdlcyhmaWVsZCk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH1cbiAgfVxufVxuIl19