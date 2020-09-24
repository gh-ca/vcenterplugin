/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { getFieldId, isUndefined, getFieldValue, reverseDeepMerge, assignFieldValue } from '../../utils';
/**
 * \@experimental
 */
var /**
 * \@experimental
 */
CoreExtension = /** @class */ (function () {
    function CoreExtension(formlyConfig) {
        this.formlyConfig = formlyConfig;
        this.formId = 0;
    }
    /**
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.prePopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        this.getFieldComponentInstance(field).prePopulate();
        if (field.parent) {
            return;
        }
        /** @type {?} */
        var fieldTransforms = (field.options && field.options.fieldTransform) || this.formlyConfig.extras.fieldTransform;
        (Array.isArray(fieldTransforms) ? fieldTransforms : [fieldTransforms]).forEach((/**
         * @param {?} fieldTransform
         * @return {?}
         */
        function (fieldTransform) {
            if (fieldTransform) {
                console.warn("NgxFormly: fieldTransform is deprecated since v5.0, use custom extension instead.");
                /** @type {?} */
                var fieldGroup = fieldTransform(field.fieldGroup, field.model, (/** @type {?} */ (field.formControl)), field.options);
                if (!fieldGroup) {
                    throw new Error('fieldTransform must return an array of fields');
                }
            }
        }));
    };
    /**
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.onPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        var _this = this;
        this.initFieldOptions(field);
        this.getFieldComponentInstance(field).onPopulate();
        if (field.fieldGroup) {
            field.fieldGroup.forEach((/**
             * @param {?} f
             * @param {?} index
             * @return {?}
             */
            function (f, index) {
                Object.defineProperty(f, 'parent', { get: (/**
                     * @return {?}
                     */
                    function () { return field; }), configurable: true });
                Object.defineProperty(f, 'index', { get: (/**
                     * @return {?}
                     */
                    function () { return index; }), configurable: true });
                _this.formId++;
            }));
        }
    };
    /**
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.postPopulate = /**
     * @param {?} field
     * @return {?}
     */
    function (field) {
        this.getFieldComponentInstance(field).postPopulate();
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.initFieldOptions = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        /** @type {?} */
        var root = (/** @type {?} */ (field.parent));
        if (!root) {
            return;
        }
        Object.defineProperty(field, 'form', { get: (/**
             * @return {?}
             */
            function () { return root.formControl; }), configurable: true });
        Object.defineProperty(field, 'options', { get: (/**
             * @return {?}
             */
            function () { return root.options; }), configurable: true });
        Object.defineProperty(field, 'model', {
            get: (/**
             * @return {?}
             */
            function () { return field.key && field.fieldGroup ? getFieldValue(field) : root.model; }),
            configurable: true,
        });
        reverseDeepMerge(field, {
            id: getFieldId("formly_" + this.formId, field, field['index']),
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
            console.warn("NgxFormly: 'lifecycle' is deprecated since v5.0, use 'hooks' instead.");
        }
        if (field.type !== 'formly-template'
            && (field.template
                || (field.expressionProperties && field.expressionProperties.template))) {
            if (field.type) {
                console.warn("NgxFormly: passing 'type' property is not allowed when 'template' is set.");
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
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.initFieldWrappers = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        field.wrappers = field.wrappers || [];
        /** @type {?} */
        var fieldTemplateManipulators = tslib_1.__assign({ preWrapper: [], postWrapper: [] }, (field.templateOptions.templateManipulators || {}));
        field.wrappers = tslib_1.__spread(this.formlyConfig.templateManipulators.preWrapper.map((/**
         * @param {?} m
         * @return {?}
         */
        function (m) { return m(field); })), fieldTemplateManipulators.preWrapper.map((/**
         * @param {?} m
         * @return {?}
         */
        function (m) { return m(field); })), field.wrappers, this.formlyConfig.templateManipulators.postWrapper.map((/**
         * @param {?} m
         * @return {?}
         */
        function (m) { return m(field); })), fieldTemplateManipulators.postWrapper.map((/**
         * @param {?} m
         * @return {?}
         */
        function (m) { return m(field); }))).filter((/**
         * @param {?} el
         * @param {?} i
         * @param {?} a
         * @return {?}
         */
        function (el, i, a) { return el && i === a.indexOf(el); }));
    };
    /**
     * @private
     * @param {?} field
     * @return {?}
     */
    CoreExtension.prototype.getFieldComponentInstance = /**
     * @private
     * @param {?} field
     * @return {?}
     */
    function (field) {
        /** @type {?} */
        var componentRef = this.formlyConfig.resolveFieldTypeRef(field);
        /** @type {?} */
        var instance = componentRef ? (/** @type {?} */ (componentRef.instance)) : {};
        return {
            prePopulate: (/**
             * @return {?}
             */
            function () { return instance.prePopulate && instance.prePopulate(field); }),
            onPopulate: (/**
             * @return {?}
             */
            function () { return instance.onPopulate && instance.onPopulate(field); }),
            postPopulate: (/**
             * @return {?}
             */
            function () { return instance.postPopulate && instance.postPopulate(field); }),
        };
    };
    return CoreExtension;
}());
/**
 * \@experimental
 */
export { CoreExtension };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29yZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvZXh0ZW5zaW9ucy9jb3JlL2NvcmUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFHQSxPQUFPLEVBQUUsVUFBVSxFQUFFLFdBQVcsRUFBRSxhQUFhLEVBQUUsZ0JBQWdCLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSxhQUFhLENBQUM7Ozs7QUFHekc7Ozs7SUFFRSx1QkFBb0IsWUFBMEI7UUFBMUIsaUJBQVksR0FBWixZQUFZLENBQWM7UUFEdEMsV0FBTSxHQUFHLENBQUMsQ0FBQztJQUMrQixDQUFDOzs7OztJQUVuRCxtQ0FBVzs7OztJQUFYLFVBQVksS0FBNkI7UUFDdkMsSUFBSSxDQUFDLHlCQUF5QixDQUFDLEtBQUssQ0FBQyxDQUFDLFdBQVcsRUFBRSxDQUFDO1FBQ3BELElBQUksS0FBSyxDQUFDLE1BQU0sRUFBRTtZQUNoQixPQUFPO1NBQ1I7O1lBRUssZUFBZSxHQUFHLENBQUMsS0FBSyxDQUFDLE9BQU8sSUFBSSxLQUFLLENBQUMsT0FBTyxDQUFDLGNBQWMsQ0FBQyxJQUFJLElBQUksQ0FBQyxZQUFZLENBQUMsTUFBTSxDQUFDLGNBQWM7UUFDbEgsQ0FBQyxLQUFLLENBQUMsT0FBTyxDQUFDLGVBQWUsQ0FBQyxDQUFDLENBQUMsQ0FBQyxlQUFlLENBQUMsQ0FBQyxDQUFDLENBQUMsZUFBZSxDQUFDLENBQUMsQ0FBQyxPQUFPOzs7O1FBQUMsVUFBQSxjQUFjO1lBQzNGLElBQUksY0FBYyxFQUFFO2dCQUNsQixPQUFPLENBQUMsSUFBSSxDQUFDLG1GQUFtRixDQUFDLENBQUM7O29CQUM1RixVQUFVLEdBQUcsY0FBYyxDQUFDLEtBQUssQ0FBQyxVQUFVLEVBQUUsS0FBSyxDQUFDLEtBQUssRUFBRSxtQkFBVyxLQUFLLENBQUMsV0FBVyxFQUFBLEVBQUUsS0FBSyxDQUFDLE9BQU8sQ0FBQztnQkFDN0csSUFBSSxDQUFDLFVBQVUsRUFB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SyxDQUFDLEVBQW5ELENBQW1ELENBQUE7WUFDdEUsVUFBVTs7O1lBQUUsY0FBTSxPQUFBLFFBQVEsQ0FBQyxVQUFVLElBQUksUUFBUSxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsRUFBakQsQ0FBaUQsQ0FBQTtZQUNuRSxZQUFZOzs7WUFBRSxjQUFNLE9BQUEsUUFBUSxDQUFDLFlBQVksSUFBSSxRQUFRLENBQUMsWUFBWSxDQUFDLEtBQUssQ0FBQyxFQUFyRCxDQUFxRCxDQUFBO1NBQzFFLENBQUM7SUFDSixDQUFDO0lBQ0gsb0JBQUM7QUFBRCxDQUFDLEFBMUhELElBMEhDOzs7Ozs7Ozs7O0lBekhDLCtCQUFtQjs7Ozs7SUFDUCxxQ0FBa0MiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBGb3JtbHlFeHRlbnNpb24sIEZvcm1seUNvbmZpZywgVGVtcGxhdGVNYW5pcHVsYXRvcnMgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIEZvcm1seUZpZWxkQ29uZmlnIH0gZnJvbSAnLi4vLi4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IEZvcm1Hcm91cCB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcbmltcG9ydCB7IGdldEZpZWxkSWQsIGlzVW5kZWZpbmVkLCBnZXRGaWVsZFZhbHVlLCByZXZlcnNlRGVlcE1lcmdlLCBhc3NpZ25GaWVsZFZhbHVlIH0gZnJvbSAnLi4vLi4vdXRpbHMnO1xuXG4vKiogQGV4cGVyaW1lbnRhbCAqL1xuZXhwb3J0IGNsYXNzIENvcmVFeHRlbnNpb24gaW1wbGVtZW50cyBGb3JtbHlFeHRlbnNpb24ge1xuICBwcml2YXRlIGZvcm1JZCA9IDA7XG4gIGNvbnN0cnVjdG9yKHByaXZhdGUgZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcpIHsgfVxuXG4gIHByZVBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5nZXRGaWVsZENvbXBvbmVudEluc3RhbmNlKGZpZWxkKS5wcmVQb3B1bGF0ZSgpO1xuICAgIGlmIChmaWVsZC5wYXJlbnQpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBjb25zdCBmaWVsZFRyYW5zZm9ybXMgPSAoZmllbGQub3B0aW9ucyAmJiBmaWVsZC5vcHRpb25zLmZpZWxkVHJhbnNmb3JtKSB8fCB0aGlzLmZvcm1seUNvbmZpZy5leHRyYXMuZmllbGRUcmFuc2Zvcm07XG4gICAgKEFycmF5LmlzQXJyYXkoZmllbGRUcmFuc2Zvcm1zKSA/IGZpZWxkVHJhbnNmb3JtcyA6IFtmaWVsZFRyYW5zZm9ybXNdKS5mb3JFYWNoKGZpZWxkVHJhbnNmb3JtID0+IHtcbiAgICAgIGlmIChmaWVsZFRyYW5zZm9ybSkge1xuICAgICAgICBjb25zb2xlLndhcm4oYE5neEZvcm1seTogZmllbGRUcmFuc2Zvcm0gaXMgZGVwcmVjYXRlZCBzaW5jZSB2NS4wLCB1c2UgY3VzdG9tIGV4dGVuc2lvbiBpbnN0ZWFkLmApO1xuICAgICAgICBjb25zdCBmaWVsZEdyb3VwID0gZmllbGRUcmFuc2Zvcm0oZmllbGQuZmllbGRHcm91cCwgZmllbGQubW9kZWwsIDxGb3JtR3JvdXA+ZmllbGQuZm9ybUNvbnRyb2wsIGZpZWxkLm9wdGlvbnMpO1xuICAgICAgICBpZiAoIWZpZWxkR3JvdXApIHtcbiAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoJ2ZpZWxkVHJhbnNmb3JtIG11c3QgcmV0dXJuIGFuIGFycmF5IG9mIGZpZWxkcycpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICBvblBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgdGhpcy5pbml0RmllbGRPcHRpb25zKGZpZWxkKTtcbiAgICB0aGlzLmdldEZpZWxkQ29tcG9uZW50SW5zdGFuY2UoZmllbGQpLm9uUG9wdWxhdGUoKTtcbiAgICBpZiAoZmllbGQuZmllbGRHcm91cCkge1xuICAgICAgZmllbGQuZmllbGRHcm91cC5mb3JFYWNoKChmLCBpbmRleCkgPT4ge1xuICAgICAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZiwgJ3BhcmVudCcsIHsgZ2V0OiAoKSA9PiBmaWVsZCwgY29uZmlndXJhYmxlOiB0cnVlIH0pO1xuICAgICAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZiwgJ2luZGV4JywgeyBnZXQ6ICgpID0+IGluZGV4LCBjb25maWd1cmFibGU6IHRydWUgfSk7XG4gICAgICAgIHRoaXMuZm9ybUlkKys7XG4gICAgICB9KTtcbiAgICB9XG4gIH1cblxuICBwb3N0UG9wdWxhdGUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUpIHtcbiAgICB0aGlzLmdldEZpZWxkQ29tcG9uZW50SW5zdGFuY2UoZmllbGQpLnBvc3RQb3B1bGF0ZSgpO1xuICB9XG5cbiAgcHJpdmF0ZSBpbml0RmllbGRPcHRpb25zKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlKSB7XG4gICAgY29uc3Qgcm9vdCA9IDxGb3JtbHlGaWVsZENvbmZpZ0NhY2hlPiBmaWVsZC5wYXJlbnQ7XG4gICAgaWYgKCFyb290KSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgT2JqZWN0LmRlZmluZVByb3BlcnR5KGZpZWxkLCAnZm9ybScsIHsgZ2V0OiAoKSA9PiByb290LmZvcm1Db250cm9sLCBjb25maWd1cmFibGU6IHRydWUgfSk7XG4gICAgT2JqZWN0LmRlZmluZVByb3BlcnR5KGZpZWxkLCAnb3B0aW9ucycsIHsgZ2V0OiAoKSA9PiByb290Lm9wdGlvbnMsIGNvbmZpZ3VyYWJsZTogdHJ1ZSB9KTtcbiAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZmllbGQsICdtb2RlbCcsIHtcbiAgICAgIGdldDogKCkgPT4gZmllbGQua2V5ICYmIGZpZWxkLmZpZWxkR3JvdXAgPyBnZXRGaWVsZFZhbHVlKGZpZWxkKSA6IHJvb3QubW9kZWwsXG4gICAgICBjb25maWd1cmFibGU6IHRydWUsXG4gICAgfSk7XG5cbiAgICByZXZlcnNlRGVlcE1lcmdlKGZpZWxkLCB7XG4gICAgICBpZDogZ2V0RmllbGRJZChgZm9ybWx5XyR7dGhpcy5mb3JtSWR9YCwgZmllbGQsIGZpZWxkWydpbmRleCddKSxcbiAgICAgIGhvb2tzOiB7fSxcbiAgICAgIG1vZGVsT3B0aW9uczoge30sXG4gICAgICB0ZW1wbGF0ZU9wdGlvbnM6ICFmaWVsZC50eXBlIHx8ICFmaWVsZC5rZXkgPyB7fSA6IHtcbiAgICAgICAgbGFiZWw6ICcnLFxuICAgICAgICBwbGFjZWhvbGRlcjogJycsXG4gICAgICAgIGZvY3VzOiBmYWxzZSxcbiAgICAgICAgZGlzYWJsZWQ6IGZhbHNlLFxuICAgICAgfSxcbiAgICB9KTtcblxuICAgIGlmIChmaWVsZC5saWZlY3ljbGUpIHtcbiAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiAnbGlmZWN5Y2xlJyBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjAsIHVzZSAnaG9va3MnIGluc3RlYWQuYCk7XG4gICAgfVxuXG4gICAgaWYgKFxuICAgICAgZmllbGQudHlwZSAhPT0gJ2Zvcm1seS10ZW1wbGF0ZSdcbiAgICAgICYmIChcbiAgICAgICAgZmllbGQudGVtcGxhdGVcbiAgICAgICAgfHwgKGZpZWxkLmV4cHJlc3Npb25Qcm9wZXJ0aWVzICYmIGZpZWxkLmV4cHJlc3Npb25Qcm9wZXJ0aWVzLnRlbXBsYXRlKVxuICAgICAgKVxuICAgICkge1xuICAgICAgaWYgKGZpZWxkLnR5cGUpIHtcbiAgICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ3R5cGUnIHByb3BlcnR5IGlzIG5vdCBhbGxvd2VkIHdoZW4gJ3RlbXBsYXRlJyBpcyBzZXQuYCk7XG4gICAgICB9XG4gICAgICBmaWVsZC50eXBlID0gJ2Zvcm1seS10ZW1wbGF0ZSc7XG4gICAgfVxuXG4gICAgaWYgKCFmaWVsZC50eXBlICYmIGZpZWxkLmZpZWxkR3JvdXApIHtcbiAgICAgIGZpZWxkLnR5cGUgPSAnZm9ybWx5LWdyb3VwJztcbiAgICB9XG5cbiAgICBpZiAoZmllbGQudHlwZSkge1xuICAgICAgdGhpcy5mb3JtbHlDb25maWcuZ2V0TWVyZ2VkRmllbGQoZmllbGQpO1xuICAgIH1cblxuICAgIGlmIChmaWVsZC5wYXJlbnQgJiYgIWZpZWxkWydhdXRvQ2xlYXInXSAmJiAhaXNVbmRlZmluZWQoZmllbGQuZGVmYXVsdFZhbHVlKSAmJiBpc1VuZGVmaW5lZChnZXRGaWVsZFZhbHVlKGZpZWxkKSkpIHtcbiAgICAgIGFzc2lnbkZpZWxkVmFsdWUoZmllbGQsIGZpZWxkLmRlZmF1bHRWYWx1ZSk7XG4gICAgfVxuXG4gICAgdGhpcy5pbml0RmllbGRXcmFwcGVycyhmaWVsZCk7XG4gIH1cblxuICBwcml2YXRlIGluaXRGaWVsZFdyYXBwZXJzKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykge1xuICAgIGZpZWxkLndyYXBwZXJzID0gZmllbGQud3JhcHBlcnMgfHwgW107XG4gICAgY29uc3QgZmllbGRUZW1wbGF0ZU1hbmlwdWxhdG9yczogVGVtcGxhdGVNYW5pcHVsYXRvcnMgPSB7XG4gICAgICBwcmVXcmFwcGVyOiBbXSxcbiAgICAgIHBvc3RXcmFwcGVyOiBbXSxcbiAgICAgIC4uLihmaWVsZC50ZW1wbGF0ZU9wdGlvbnMudGVtcGxhdGVNYW5pcHVsYXRvcnMgfHwge30pLFxuICAgIH07XG5cbiAgICBmaWVsZC53cmFwcGVycyA9IFtcbiAgICAgIC4uLnRoaXMuZm9ybWx5Q29uZmlnLnRlbXBsYXRlTWFuaXB1bGF0b3JzLnByZVdyYXBwZXIubWFwKG0gPT4gbShmaWVsZCkpLFxuICAgICAgLi4uZmllbGRUZW1wbGF0ZU1hbmlwdWxhdG9ycy5wcmVXcmFwcGVyLm1hcChtID0+IG0oZmllbGQpKSxcbiAgICAgIC4uLmZpZWxkLndyYXBwZXJzLFxuICAgICAgLi4udGhpcy5mb3JtbHlDb25maWcudGVtcGxhdGVNYW5pcHVsYXRvcnMucG9zdFdyYXBwZXIubWFwKG0gPT4gbShmaWVsZCkpLFxuICAgICAgLi4uZmllbGRUZW1wbGF0ZU1hbmlwdWxhdG9ycy5wb3N0V3JhcHBlci5tYXAobSA9PiBtKGZpZWxkKSksXG4gICAgXS5maWx0ZXIoKGVsLCBpLCBhKSA9PiBlbCAmJiBpID09PSBhLmluZGV4T2YoZWwpKTtcbiAgfVxuXG4gIHByaXZhdGUgZ2V0RmllbGRDb21wb25lbnRJbnN0YW5jZShmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSkge1xuICAgIGNvbnN0IGNvbXBvbmVudFJlZiA9IHRoaXMuZm9ybWx5Q29uZmlnLnJlc29sdmVGaWVsZFR5cGVSZWYoZmllbGQpO1xuICAgIGNvbnN0IGluc3RhbmNlOiBGb3JtbHlFeHRlbnNpb24gPSBjb21wb25lbnRSZWYgPyBjb21wb25lbnRSZWYuaW5zdGFuY2UgYXMgYW55IDoge307XG5cbiAgICByZXR1cm4ge1xuICAgICAgcHJlUG9wdWxhdGU6ICgpID0+IGluc3RhbmNlLnByZVBvcHVsYXRlICYmIGluc3RhbmNlLnByZVBvcHVsYXRlKGZpZWxkKSxcbiAgICAgIG9uUG9wdWxhdGU6ICgpID0+IGluc3RhbmNlLm9uUG9wdWxhdGUgJiYgaW5zdGFuY2Uub25Qb3B1bGF0ZShmaWVsZCksXG4gICAgICBwb3N0UG9wdWxhdGU6ICgpID0+IGluc3RhbmNlLnBvc3RQb3B1bGF0ZSAmJiBpbnN0YW5jZS5wb3N0UG9wdWxhdGUoZmllbGQpLFxuICAgIH07XG4gIH1cbn1cbiJdfQ==