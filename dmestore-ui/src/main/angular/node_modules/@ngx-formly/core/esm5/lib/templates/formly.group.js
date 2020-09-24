/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component } from '@angular/core';
import { FieldType } from './field.type';
var FormlyGroup = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyGroup, _super);
    function FormlyGroup() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.defaultOptions = {
            defaultValue: {},
        };
        return _this;
    }
    FormlyGroup.decorators = [
        { type: Component, args: [{
                    selector: 'formly-group',
                    template: "\n    <formly-field *ngFor=\"let f of field.fieldGroup\" [field]=\"f\"></formly-field>\n    <ng-content></ng-content>\n  ",
                    host: {
                        '[class]': 'field.fieldGroupClassName || ""',
                    }
                }] }
    ];
    return FormlyGroup;
}(FieldType));
export { FormlyGroup };
if (false) {
    /** @type {?} */
    FormlyGroup.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5Lmdyb3VwLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvY29yZS8iLCJzb3VyY2VzIjpbImxpYi90ZW1wbGF0ZXMvZm9ybWx5Lmdyb3VwLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7O0FBQUEsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUMxQyxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sY0FBYyxDQUFDO0FBRXpDO0lBVWlDLHVDQUFTO0lBVjFDO1FBQUEscUVBY0M7UUFIQyxvQkFBYyxHQUFHO1lBQ2YsWUFBWSxFQUFFLEVBQUU7U0FDakIsQ0FBQzs7SUFDSixDQUFDOztnQkFkQSxTQUFTLFNBQUM7b0JBQ1QsUUFBUSxFQUFFLGNBQWM7b0JBQ3hCLFFBQVEsRUFBRSwySEFHVDtvQkFDRCxJQUFJLEVBQUU7d0JBQ0osU0FBUyxFQUFFLGlDQUFpQztxQkFDN0M7aUJBQ0Y7O0lBS0Qsa0JBQUM7Q0FBQSxBQWRELENBVWlDLFNBQVMsR0FJekM7U0FKWSxXQUFXOzs7SUFDdEIscUNBRUUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJy4vZmllbGQudHlwZSc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1ncm91cCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPGZvcm1seS1maWVsZCAqbmdGb3I9XCJsZXQgZiBvZiBmaWVsZC5maWVsZEdyb3VwXCIgW2ZpZWxkXT1cImZcIj48L2Zvcm1seS1maWVsZD5cbiAgICA8bmctY29udGVudD48L25nLWNvbnRlbnQ+XG4gIGAsXG4gIGhvc3Q6IHtcbiAgICAnW2NsYXNzXSc6ICdmaWVsZC5maWVsZEdyb3VwQ2xhc3NOYW1lIHx8IFwiXCInLFxuICB9LFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlHcm91cCBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIGRlZmF1bHRPcHRpb25zID0ge1xuICAgIGRlZmF1bHRWYWx1ZToge30sXG4gIH07XG59XG4iXX0=