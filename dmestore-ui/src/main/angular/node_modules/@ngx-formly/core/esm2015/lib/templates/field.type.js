/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Input } from '@angular/core';
/**
 * @abstract
 * @template F
 */
export class FieldType {
    /**
     * @return {?}
     */
    get model() { return this.field.model; }
    /**
     * @param {?} m
     * @return {?}
     */
    set model(m) { console.warn(`NgxFormly: passing 'model' input to '${this.constructor.name}' component is not required anymore, you may remove it!`); }
    /**
     * @return {?}
     */
    get form() { return (/** @type {?} */ (this.field.parent.formControl)); }
    /**
     * @param {?} form
     * @return {?}
     */
    set form(form) { console.warn(`NgxFormly: passing 'form' input to '${this.constructor.name}' component is not required anymore, you may remove it!`); }
    /**
     * @return {?}
     */
    get options() { return this.field.options; }
    /**
     * @param {?} options
     * @return {?}
     */
    set options(options) { console.warn(`NgxFormly: passing 'options' input to '${this.constructor.name}' component is not required anymore, you may remove it!`); }
    /**
     * @return {?}
     */
    get key() { return this.field.key; }
    /**
     * @return {?}
     */
    get formControl() { return this.field.formControl; }
    /**
     * @return {?}
     */
    get to() { return this.field.templateOptions || {}; }
    /**
     * @return {?}
     */
    get showError() { return this.options.showError(this); }
    /**
     * @return {?}
     */
    get id() { return this.field.id; }
    /**
     * @return {?}
     */
    get formState() { return this.options.formState || {}; }
}
FieldType.propDecorators = {
    field: [{ type: Input }],
    model: [{ type: Input }],
    form: [{ type: Input }],
    options: [{ type: Input }]
};
if (false) {
    /** @type {?} */
    FieldType.prototype.field;
    /** @type {?} */
    FieldType.prototype.defaultOptions;
}
/**
 * @deprecated use `FieldType` instead
 * @abstract
 */
export class Field extends FieldType {
    constructor() {
        super();
        console.warn(`NgxFormly: 'Field' has been renamed to 'FieldType', extend 'FieldType' instead.`);
    }
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxLQUFLLEVBQUUsTUFBTSxlQUFlLENBQUM7Ozs7O0FBSXRDLE1BQU0sT0FBZ0IsU0FBUzs7OztJQUk3QixJQUNJLEtBQUssS0FBSyxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsS0FBSyxDQUFDLENBQUMsQ0FBQzs7Ozs7SUFDeEMsSUFBSSxLQUFLLENBQUMsQ0FBTSxJQUFJLE9BQU8sQ0FBQyxJQUFJLENBQUMsd0NBQXdDLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSx5REFBeUQsQ0FBQyxDQUFDLENBQUMsQ0FBQzs7OztJQUUzSixJQUNJLElBQUksS0FBSyxPQUFPLG1CQUFZLElBQUksQ0FBQyxLQUFLLENBQUMsTUFBTSxDQUFDLFdBQVcsRUFBQSxDQUFDLENBQUMsQ0FBQzs7Ozs7SUFDaEUsSUFBSSxJQUFJLENBQUMsSUFBSSxJQUFJLE9BQU8sQ0FBQyxJQUFJLENBQUMsdUNBQXVDLElBQUksQ0FBQyxXQUFXLENBQUMsSUFBSSx5REFBeUQsQ0FBQyxDQUFDLENBQUMsQ0FBQzs7OztJQUV2SixJQUNJLE9BQU8sS0FBSyxPQUFPLElBQUksQ0FBQyxLQUFLLENBQUMsT0FBTyxDQUFDLENBQUMsQ0FBQzs7Ozs7SUFDNUMsSUFBSSxPQUFPLENBQUMsT0FBcUIsSUFBSSxPQUFPLENBQUMsSUFBSSxDQUFDLDBDQUEwQyxJQUFJLENBQUMsV0FBVyxDQUFDLElBQUkseURBQXlELENBQUMsQ0FBQyxDQUFDLENBQUM7Ozs7SUFFOUssSUFBSSxHQUFHLEtBQUssT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLEdBQUcsQ0FBQyxDQUFDLENBQUM7Ozs7SUFFcEMsSUFBSSxXQUFXLEtBQUssT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVcsQ0FBQyxDQUFDLENBQUM7Ozs7SUFFcEQsSUFBSSxFQUFFLEtBQUssT0FBTyxJQUFJLENBQUMsS0FBSyxDQUFDLGVBQWUsSUFBSSxFQUFFLENBQUMsQ0FBQyxDQUFDOzs7O0lBRXJELElBQUksU0FBUyxLQUFjLE9BQU8sSUFBSSxDQUFDLE9BQU8sQ0FBQyxTQUFTLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxDQUFDOzs7O0lBRWpFLElBQUksRUFBRSxLQUFhLE9BQU8sSUFBSSxDQUFDLEtBQUssQ0FBQyxFQUFFLENBQUMsQ0FBQyxDQUFDOzs7O0lBRTFDLElBQUksU0FBUyxLQUFLLE9BQU8sSUFBSSxDQUFDLE9BQU8sQ0FBQyxTQUFTLElBQUksRUFBRSxDQUFDLENBQUMsQ0FBQzs7O29CQXpCdkQsS0FBSztvQkFHTCxLQUFLO21CQUlMLEtBQUs7c0JBSUwsS0FBSzs7OztJQVhOLDBCQUFrQjs7SUFDbEIsbUNBQW1COzs7Ozs7QUE4QnJCLE1BQU0sT0FBZ0IsS0FBTSxTQUFRLFNBQVM7SUFDM0M7UUFDRSxLQUFLLEVBQUUsQ0FBQztRQUNSLE9BQU8sQ0FBQyxJQUFJLENBQUMsaUZBQWlGLENBQUMsQ0FBQztJQUNsRyxDQUFDO0NBQ0YiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbnB1dCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRm9ybUdyb3VwIH0gZnJvbSAnQGFuZ3VsYXIvZm9ybXMnO1xuaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICcuLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGRUeXBlPEYgZXh0ZW5kcyBGb3JtbHlGaWVsZENvbmZpZyA9IEZvcm1seUZpZWxkQ29uZmlnPiB7XG4gIEBJbnB1dCgpIGZpZWxkOiBGO1xuICBkZWZhdWx0T3B0aW9ucz86IEY7XG5cbiAgQElucHV0KClcbiAgZ2V0IG1vZGVsKCkgeyByZXR1cm4gdGhpcy5maWVsZC5tb2RlbDsgfVxuICBzZXQgbW9kZWwobTogYW55KSB7IGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiBwYXNzaW5nICdtb2RlbCcgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIEBJbnB1dCgpXG4gIGdldCBmb3JtKCkgeyByZXR1cm4gPEZvcm1Hcm91cD4gdGhpcy5maWVsZC5wYXJlbnQuZm9ybUNvbnRyb2w7IH1cbiAgc2V0IGZvcm0oZm9ybSkgeyBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnZm9ybScgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIEBJbnB1dCgpXG4gIGdldCBvcHRpb25zKCkgeyByZXR1cm4gdGhpcy5maWVsZC5vcHRpb25zOyB9XG4gIHNldCBvcHRpb25zKG9wdGlvbnM6IEZbJ29wdGlvbnMnXSkgeyBjb25zb2xlLndhcm4oYE5neEZvcm1seTogcGFzc2luZyAnb3B0aW9ucycgaW5wdXQgdG8gJyR7dGhpcy5jb25zdHJ1Y3Rvci5uYW1lfScgY29tcG9uZW50IGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTsgfVxuXG4gIGdldCBrZXkoKSB7IHJldHVybiB0aGlzLmZpZWxkLmtleTsgfVxuXG4gIGdldCBmb3JtQ29udHJvbCgpIHsgcmV0dXJuIHRoaXMuZmllbGQuZm9ybUNvbnRyb2w7IH1cblxuICBnZXQgdG8oKSB7IHJldHVybiB0aGlzLmZpZWxkLnRlbXBsYXRlT3B0aW9ucyB8fCB7fTsgfVxuXG4gIGdldCBzaG93RXJyb3IoKTogYm9vbGVhbiB7IHJldHVybiB0aGlzLm9wdGlvbnMuc2hvd0Vycm9yKHRoaXMpOyB9XG5cbiAgZ2V0IGlkKCk6IHN0cmluZyB7IHJldHVybiB0aGlzLmZpZWxkLmlkOyB9XG5cbiAgZ2V0IGZvcm1TdGF0ZSgpIHsgcmV0dXJuIHRoaXMub3B0aW9ucy5mb3JtU3RhdGUgfHwge307IH1cbn1cblxuLyoqXG4gKiBAZGVwcmVjYXRlZCB1c2UgYEZpZWxkVHlwZWAgaW5zdGVhZFxuICovXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgRmllbGQgZXh0ZW5kcyBGaWVsZFR5cGUge1xuICBjb25zdHJ1Y3RvcigpIHtcbiAgICBzdXBlcigpO1xuICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiAnRmllbGQnIGhhcyBiZWVuIHJlbmFtZWQgdG8gJ0ZpZWxkVHlwZScsIGV4dGVuZCAnRmllbGRUeXBlJyBpbnN0ZWFkLmApO1xuICB9XG59XG4iXX0=