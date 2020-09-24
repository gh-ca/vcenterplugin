/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { getFieldId, isUndefined, getFieldValue, reverseDeepMerge, assignFieldValue } from '../../utils';
/**
 * \@experimental
 */
export class CoreExtension {
    /**
     * @param {?} formlyConfig
     */
    constructor(formlyConfig) {
        this.formlyConfig = formlyConfig;
        this.formId = 0;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    prePopulate(field) {
        this.getFieldComponentInstance(field).prePopulate();
        if (field.parent) {
            return;
        }
        /** @type {?} */
        const fieldTransforms = (field.options && field.options.fieldTransform) || this.formlyConfig.extras.fieldTransform;
        (Array.isArray(fieldTransforms) ? fieldTransforms : [fieldTransforms]).forEach((/**
         * @param {?} fieldTransform
         * @return {?}
         */
        fieldTransform => {
            if (fieldTransform) {
                console.warn(`NgxFormly: fieldTransform is deprecated since v5.0, use custom extension instead.`);
                /** @type {?} */
                const fieldGroup = fieldTransform(field.fieldGroup, field.model, (/** @type {?} */ (field.formControl)), field.options);
                if (!fieldGroup) {
                    throw new Error('fieldTransform must return an array of fields');
                }
            }
        }));
    }
    /**
     * @param {?} field
     * @return {?}
     */
    onPopulate(field) {
        this.initFieldOptions(field);
        this.getFieldComponentInstance(field).onPopulate();
        if (field.fieldGroup) {
            field.fieldGroup.forEach((/**
             * @param {?} f
             * @param {?} index
             * @return {?}
             */
            (f, index) => {
                Object.defineProperty(f, 'parent', { get: (/**
                     * @return {?}
                     */
                    () => field), configurable: true });
                Object.defineProperty(f, 'index', { get: (/**
                     * @return {?}
                     */
                    () => index), configurable: true });
                this.formId++;
            }));
        }
    }
    /**
     * @param {?} field
     * @return {?}
     */
    postPopulate(field) {
        this.getFieldComponentInstance(field).postPopulate();
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    initFieldOptions(field) {
        /** @type {?} */
        const root = (/** @type {?} */ (field.parent));
        if (!root) {
            return;
        }
        Object.defineProperty(field, 'form', { get: (/**
             * @return {?}
             */
            () => root.formControl), configurable: true });
        Object.defineProperty(field, 'options', { get: (/**
             * @return {?}
             */
            () => root.options), configurable: true });
        Object.defineProperty(field, 'model', {
            get: (/**
             * @return {?}
             */
            () => field.key && field.fieldGroup ? getFieldValue(field) : root.model),
            configurable: true,
        });
        reverseDeepMerge(field, {
            id: getFieldId(`formly_${this.formId}`, field, field['index']),
            hooks: {},
            modelOptions: {},
            templateOptions: !field.type || !field.key ? {} : {
                label: '',
                placeholder: '',
                focus: false,
                disabled: false,
            },
        });
        if (field.lifecycle) {
            console.warn(`NgxFormly: 'lifecycle' is deprecated since v5.0, use 'hooks' instead.`);
        }
        if (field.type !== 'formly-template'
            && (field.template
                || (field.expressionProperties && field.expressionProperties.template))) {
            if (field.type) {
                console.warn(`NgxFormly: passing 'type' property is not allowed when 'template' is set.`);
            }
            field.type = 'formly-template';
        }
        if (!field.type && field.fieldGroup) {
            field.type = 'formly-group';
        }
        if (field.type) {
            this.formlyConfig.getMergedField(field);
        }
        if (field.parent && !field['autoClear'] && !isUndefined(field.defaultValue) && isUndefined(getFieldValue(field))) {
            assignFieldValue(field, field.defaultValue);
        }
        this.initFieldWrappers(field);
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    initFieldWrappers(field) {
        field.wrappers = field.wrappers || [];
        /** @type {?} */
        const fieldTemplateManipulators = Object.assign({ preWrapper: [], postWrapper: [] }, (field.templateOptions.templateManipulators || {}));
        field.wrappers = [
            ...this.formlyConfig.templateManipulators.preWrapper.map((/**
             * @param {?} m
             * @return {?}
             */
            m => m(field))),
            ...fieldTemplateManipulators.preWrapper.map((/**
             * @param {?} m
             * @return {?}
             */
            m => m(field))),
            ...field.wrappers,
            ...this.formlyConfig.templateManipulators.postWrapper.map((/**
             * @param {?} m
             * @return {?}
             */
            m => m(field))),
            ...fieldTemplateManipulators.postWrapper.map((/**
             * @param {?} m
             * @return {?}
             */
            m => m(field))),
        ].filter((/**
         * @param {?} el
         * @param {?} i
         * @param {?} a
         * @return {?}
         */
        (el, i, a) => el && i === a.indexOf(el)));
    }
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    getFieldComponentInstance(field) {
        /** @type {?} */
        const componentRef = this.formlyConfig.resolveFieldTypeRef(field);
        /** @type {?} */
        const instance = componentRef ? (/** @type {?} */ (componentRef.instance)) : {};
        return {
            prePopulate: (/**
             * @return {?}
             */
            () => instance.prePopulate && instance.prePopulate(field)),
            onPopulate: (/**
             * @return {?}
             */
            () => instance.onPopulate && instance.onPopulate(field)),
            postPopulate: (/**
             * @return {?}
             */
            () => instance.postPopulate && instance.postPopulate(field)),
        };
    }
}
if (false) {
    /**
     * @type {?}
     * @private
     */
    CoreExtension.prototype.formId;
    /**
     * @type {?}
     * @private
     */
    CoreExtension.prototype.formlyConfig;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29yZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9jb3JlL2NvcmUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUdBLE9BQU8sRUFBRSxVQUFVLEVBQUUsV0FBVyxFQUFFLGFBQWEsRUFBRSxnQkFBZ0IsRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLGFBQWEsQ0FBQzs7OztBQUd6RyxNQUFNLE9BQU8sYUFBYTs7OztJQUV4QixZQUFvQixZQUEwQjtRQUExQixpQkFBWSxHQUFaLFlBQVksQ0FBYztRQUR0QyxXQUFNLEdBQUcsQ0FBQyxDQUFDO0lBQytCLENBQUM7Ozs7O0lBRW5ELFdBQVcsQ0FBQyxLQUE2QjtRQUN2QyxJQUFJLENBQUMseUJBQXlCLENBQUMsS0FBSyxDQUFDLENBQUMsV0FBVyxFQUFFLENBQUM7UUFDcEQsSUFBSSxLQUFLLENBQUMsTUFBTSxFQUFFO1lBQ2hCLE9BQU87U0FDUjs7Y0FFSyxlQUFlLEdBQUcsQ0FBQyxLQUFLLENBQUMsT0FBTyxJQUFJLEtBQUssQ0FBQyxPQUFPLENBQUMsY0FBYyxDQUFDLElBQUksSUFBSSxDQUFDLFlBQVksQ0FBQyxNQUFNLENBQUMsY0FBYztRQUNsSCxDQUFDLEtBQUssQ0FBQyxPQUFPLENBQUMsZUFBZSxDQUFDLENBQUMsQ0FBQyxDQUFDLGVBQWUsQ0FBQyxDQUFDLENBQUMsQ0FBQyxlQUFlLENBQUMsQ0FBQyxDQUFDLE9BQU87Ozs7UUFBQyxjQUFjLENBQUMsRUFBRTtZQUM5RixJQUFJLGNBQWMsRUFBRTtnQkFDbEIsT0FBTyxDQUFDLElBQUksQ0FBQyxtRkFBbUYsQ0FBQyxDQUFDOztzQkFDNUYsVUFBVSxHQUFHLGNBQWMsQ0FBQyxLQUFLLENBQUMsVUFBVSxFQUFFLEtBQUssQ0FBQyxLQUFLLEVBQUUsbUJBQVcsS0FBSyxDQUFDLFdBQVcsRUFBQSxFQUFFLEtBQUssQ0FBQyxPQUFPLENBQUM7Z0JBQzdHLElBQUksQ0FB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VSxDQUFDLEtBQUssQ0FBQyxDQUFBO1lBQ25FLFlBQVk7OztZQUFFLEdBQUcsRUFBRSxDQUFDLFFBQVEsQ0FBQyxZQUFZLElBQUksUUFBUSxDQUFDLFlBQVksQ0FBQyxLQUFLLENBQUMsQ0FBQTtTQUMxRSxDQUFDO0lBQ0osQ0FBQztDQUNGOzs7Ozs7SUF6SEMsK0JBQW1COzs7OztJQUNQLHFDQUFrQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEZvcm1seUV4dGVuc2lvbiwgRm9ybWx5Q29uZmlnLCBUZW1wbGF0ZU1hbmlwdWxhdG9ycyB9IGZyb20gJy4uLy4uL3NlcnZpY2VzL2Zvcm1seS5jb25maWcnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWdDYWNoZSwgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICcuLi8uLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuaW1wb3J0IHsgRm9ybUdyb3VwIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgZ2V0RmllbGRJZCwgaXNVbmRlZmluZWQsIGdldEZpZWxkVmFsdWUsIHJldmVyc2VEZWVwTWVyZ2UsIGFzc2lnbkZpZWxkVmFsdWUgfSBmcm9tICcuLi8uLi91dGlscyc7XG5cbi8qKiBAZXhwZXJpbWVudGFsICovXG5leHBvcnQgY2xhc3MgQ29yZUV4dGVuc2lvbiBpbXBsZW1lbnRzIEZvcm1seUV4dGVuc2lvbiB7XG4gIHByaXZhdGUgZm9ybUlkID0gMDtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSBmb3JtbHlDb25maWc6IEZvcm1seUNvbmZpZykgeyB9XG5cbiAgcHJlUG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICB0aGlzLmdldEZpZWxkQ29tcG9uZW50SW5zdGFuY2UoZmllbGQpLnByZVBvcHVsYXRlKCk7XG4gICAgaWYgKGZpZWxkLnBhcmVudCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGNvbnN0IGZpZWxkVHJhbnNmb3JtcyA9IChmaWVsZC5vcHRpb25zICYmIGZpZWxkLm9wdGlvbnMuZmllbGRUcmFuc2Zvcm0pIHx8IHRoaXMuZm9ybWx5Q29uZmlnLmV4dHJhcy5maWVsZFRyYW5zZm9ybTtcbiAgICAoQXJyYXkuaXNBcnJheShmaWVsZFRyYW5zZm9ybXMpID8gZmllbGRUcmFuc2Zvcm1zIDogW2ZpZWxkVHJhbnNmb3Jtc10pLmZvckVhY2goZmllbGRUcmFuc2Zvcm0gPT4ge1xuICAgICAgaWYgKGZpZWxkVHJhbnNmb3JtKSB7XG4gICAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBmaWVsZFRyYW5zZm9ybSBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjAsIHVzZSBjdXN0b20gZXh0ZW5zaW9uIGluc3RlYWQuYCk7XG4gICAgICAgIGNvbnN0IGZpZWxkR3JvdXAgPSBmaWVsZFRyYW5zZm9ybShmaWVsZC5maWVsZEdyb3VwLCBmaWVsZC5tb2RlbCwgPEZvcm1Hcm91cD5maWVsZC5mb3JtQ29udHJvbCwgZmllbGQub3B0aW9ucyk7XG4gICAgICAgIGlmICghZmllbGRHcm91cCkge1xuICAgICAgICAgIHRocm93IG5ldyBFcnJvcignZmllbGRUcmFuc2Zvcm0gbXVzdCByZXR1cm4gYW4gYXJyYXkgb2YgZmllbGRzJyk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9KTtcbiAgfVxuXG4gIG9uUG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICB0aGlzLmluaXRGaWVsZE9wdGlvbnMoZmllbGQpO1xuICAgIHRoaXMuZ2V0RmllbGRDb21wb25lbnRJbnN0YW5jZShmaWVsZCkub25Qb3B1bGF0ZSgpO1xuICAgIGlmIChmaWVsZC5maWVsZEdyb3VwKSB7XG4gICAgICBmaWVsZC5maWVsZEdyb3VwLmZvckVhY2goKGYsIGluZGV4KSA9PiB7XG4gICAgICAgIE9iamVjdC5kZWZpbmVQcm9wZXJ0eShmLCAncGFyZW50JywgeyBnZXQ6ICgpID0+IGZpZWxkLCBjb25maWd1cmFibGU6IHRydWUgfSk7XG4gICAgICAgIE9iamVjdC5kZWZpbmVQcm9wZXJ0eShmLCAnaW5kZXgnLCB7IGdldDogKCkgPT4gaW5kZXgsIGNvbmZpZ3VyYWJsZTogdHJ1ZSB9KTtcbiAgICAgICAgdGhpcy5mb3JtSWQrKztcbiAgICAgIH0pO1xuICAgIH1cbiAgfVxuXG4gIHBvc3RQb3B1bGF0ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIHRoaXMuZ2V0RmllbGRDb21wb25lbnRJbnN0YW5jZShmaWVsZCkucG9zdFBvcHVsYXRlKCk7XG4gIH1cblxuICBwcml2YXRlIGluaXRGaWVsZE9wdGlvbnMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICBjb25zdCByb290ID0gPEZvcm1seUZpZWxkQ29uZmlnQ2FjaGU+IGZpZWxkLnBhcmVudDtcbiAgICBpZiAoIXJvb3QpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZmllbGQsICdmb3JtJywgeyBnZXQ6ICgpID0+IHJvb3QuZm9ybUNvbnRyb2wsIGNvbmZpZ3VyYWJsZTogdHJ1ZSB9KTtcbiAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZmllbGQsICdvcHRpb25zJywgeyBnZXQ6ICgpID0+IHJvb3Qub3B0aW9ucywgY29uZmlndXJhYmxlOiB0cnVlIH0pO1xuICAgIE9iamVjdC5kZWZpbmVQcm9wZXJ0eShmaWVsZCwgJ21vZGVsJywge1xuICAgICAgZ2V0OiAoKSA9PiBmaWVsZC5rZXkgJiYgZmllbGQuZmllbGRHcm91cCA/IGdldEZpZWxkVmFsdWUoZmllbGQpIDogcm9vdC5tb2RlbCxcbiAgICAgIGNvbmZpZ3VyYWJsZTogdHJ1ZSxcbiAgICB9KTtcblxuICAgIHJldmVyc2VEZWVwTWVyZ2UoZmllbGQsIHtcbiAgICAgIGlkOiBnZXRGaWVsZElkKGBmb3JtbHlfJHt0aGlzLmZvcm1JZH1gLCBmaWVsZCwgZmllbGRbJ2luZGV4J10pLFxuICAgICAgaG9va3M6IHt9LFxuICAgICAgbW9kZWxPcHRpb25zOiB7fSxcbiAgICAgIHRlbXBsYXRlT3B0aW9uczogIWZpZWxkLnR5cGUgfHwgIWZpZWxkLmtleSA/IHt9IDoge1xuICAgICAgICBsYWJlbDogJycsXG4gICAgICAgIHBsYWNlaG9sZGVyOiAnJyxcbiAgICAgICAgZm9jdXM6IGZhbHNlLFxuICAgICAgICBkaXNhYmxlZDogZmFsc2UsXG4gICAgICB9LFxuICAgIH0pO1xuXG4gICAgaWYgKGZpZWxkLmxpZmVjeWNsZSkge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6ICdsaWZlY3ljbGUnIGlzIGRlcHJlY2F0ZWQgc2luY2UgdjUuMCwgdXNlICdob29rcycgaW5zdGVhZC5gKTtcbiAgICB9XG5cbiAgICBpZiAoXG4gICAgICBmaWVsZC50eXBlICE9PSAnZm9ybWx5LXRlbXBsYXRlJ1xuICAgICAgJiYgKFxuICAgICAgICBmaWVsZC50ZW1wbGF0ZVxuICAgICAgICB8fCAoZmllbGQuZXhwcmVzc2lvblByb3BlcnRpZXMgJiYgZmllbGQuZXhwcmVzc2lvblByb3BlcnRpZXMudGVtcGxhdGUpXG4gICAgICApXG4gICAgKSB7XG4gICAgICBpZiAoZmllbGQudHlwZSkge1xuICAgICAgICBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAndHlwZScgcHJvcGVydHkgaXMgbm90IGFsbG93ZWQgd2hlbiAndGVtcGxhdGUnIGlzIHNldC5gKTtcbiAgICAgIH1cbiAgICAgIGZpZWxkLnR5cGUgPSAnZm9ybWx5LXRlbXBsYXRlJztcbiAgICB9XG5cbiAgICBpZiAoIWZpZWxkLnR5cGUgJiYgZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgZmllbGQudHlwZSA9ICdmb3JtbHktZ3JvdXAnO1xuICAgIH1cblxuICAgIGlmIChmaWVsZC50eXBlKSB7XG4gICAgICB0aGlzLmZvcm1seUNvbmZpZy5nZXRNZXJnZWRGaWVsZChmaWVsZCk7XG4gICAgfVxuXG4gICAgaWYgKGZpZWxkLnBhcmVudCAmJiAhZmllbGRbJ2F1dG9DbGVhciddICYmICFpc1VuZGVmaW5lZChmaWVsZC5kZWZhdWx0VmFsdWUpICYmIGlzVW5kZWZpbmVkKGdldEZpZWxkVmFsdWUoZmllbGQpKSkge1xuICAgICAgYXNzaWduRmllbGRWYWx1ZShmaWVsZCwgZmllbGQuZGVmYXVsdFZhbHVlKTtcbiAgICB9XG5cbiAgICB0aGlzLmluaXRGaWVsZFdyYXBwZXJzKGZpZWxkKTtcbiAgfVxuXG4gIHByaXZhdGUgaW5pdEZpZWxkV3JhcHBlcnMoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnKSB7XG4gICAgZmllbGQud3JhcHBlcnMgPSBmaWVsZC53cmFwcGVycyB8fCBbXTtcbiAgICBjb25zdCBmaWVsZFRlbXBsYXRlTWFuaXB1bGF0b3JzOiBUZW1wbGF0ZU1hbmlwdWxhdG9ycyA9IHtcbiAgICAgIHByZVdyYXBwZXI6IFtdLFxuICAgICAgcG9zdFdyYXBwZXI6IFtdLFxuICAgICAgLi4uKGZpZWxkLnRlbXBsYXRlT3B0aW9ucy50ZW1wbGF0ZU1hbmlwdWxhdG9ycyB8fCB7fSksXG4gICAgfTtcblxuICAgIGZpZWxkLndyYXBwZXJzID0gW1xuICAgICAgLi4udGhpcy5mb3JtbHlDb25maWcudGVtcGxhdGVNYW5pcHVsYXRvcnMucHJlV3JhcHBlci5tYXAobSA9PiBtKGZpZWxkKSksXG4gICAgICAuLi5maWVsZFRlbXBsYXRlTWFuaXB1bGF0b3JzLnByZVdyYXBwZXIubWFwKG0gPT4gbShmaWVsZCkpLFxuICAgICAgLi4uZmllbGQud3JhcHBlcnMsXG4gICAgICAuLi50aGlzLmZvcm1seUNvbmZpZy50ZW1wbGF0ZU1hbmlwdWxhdG9ycy5wb3N0V3JhcHBlci5tYXAobSA9PiBtKGZpZWxkKSksXG4gICAgICAuLi5maWVsZFRlbXBsYXRlTWFuaXB1bGF0b3JzLnBvc3RXcmFwcGVyLm1hcChtID0+IG0oZmllbGQpKSxcbiAgICBdLmZpbHRlcigoZWwsIGksIGEpID0+IGVsICYmIGkgPT09IGEuaW5kZXhPZihlbCkpO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRGaWVsZENvbXBvbmVudEluc3RhbmNlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgY29uc3QgY29tcG9uZW50UmVmID0gdGhpcy5mb3JtbHlDb25maWcucmVzb2x2ZUZpZWxkVHlwZVJlZihmaWVsZCk7XG4gICAgY29uc3QgaW5zdGFuY2U6IEZvcm1seUV4dGVuc2lvbiA9IGNvbXBvbmVudFJlZiA/IGNvbXBvbmVudFJlZi5pbnN0YW5jZSBhcyBhbnkgOiB7fTtcblxuICAgIHJldHVybiB7XG4gICAgICBwcmVQb3B1bGF0ZTogKCkgPT4gaW5zdGFuY2UucHJlUG9wdWxhdGUgJiYgaW5zdGFuY2UucHJlUG9wdWxhdGUoZmllbGQpLFxuICAgICAgb25Qb3B1bGF0ZTogKCkgPT4gaW5zdGFuY2Uub25Qb3B1bGF0ZSAmJiBpbnN0YW5jZS5vblBvcHVsYXRlKGZpZWxkKSxcbiAgICAgIHBvc3RQb3B1bGF0ZTogKCkgPT4gaW5zdGFuY2UucG9zdFBvcHVsYXRlICYmIGluc3RhbmNlLnBvc3RQb3B1bGF0ZShmaWVsZCksXG4gICAgfTtcbiAgfVxufVxuIl19