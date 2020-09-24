/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component } from '@angular/core';
import { FieldType } from './field.type';
export class FormlyGroup extends FieldType {
    constructor() {
        super(...arguments);
        this.defaultOptions = {
            defaultValue: {},
        };
    }
}
FormlyGroup.decorators = [
    { type: Component, args: [{
                selector: 'formly-group',
                template: `
    <formly-field *ngFor="let f of field.fieldGroup" [field]="f"></formly-field>
    <ng-content></ng-content>
  `,
                host: {
                    '[class]': 'field.fieldGroupClassName || ""',
                }
            }] }
];
if (false) {
    /** @type {?} */
    FormlyGroup.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZm9ybWx5Lmdyb3VwLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvY29yZS8iLCJzb3VyY2VzIjpbImxpYi90ZW1wbGF0ZXMvZm9ybWx5Lmdyb3VwLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7QUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzFDLE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxjQUFjLENBQUM7QUFZekMsTUFBTSxPQUFPLFdBQVksU0FBUSxTQUFTO0lBVjFDOztRQVdFLG1CQUFjLEdBQUc7WUFDZixZQUFZLEVBQUUsRUFBRTtTQUNqQixDQUFDO0lBQ0osQ0FBQzs7O1lBZEEsU0FBUyxTQUFDO2dCQUNULFFBQVEsRUFBRSxjQUFjO2dCQUN4QixRQUFRLEVBQUU7OztHQUdUO2dCQUNELElBQUksRUFBRTtvQkFDSixTQUFTLEVBQUUsaUNBQWlDO2lCQUM3QzthQUNGOzs7O0lBRUMscUNBRUUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IEZpZWxkVHlwZSB9IGZyb20gJy4vZmllbGQudHlwZSc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2Zvcm1seS1ncm91cCcsXG4gIHRlbXBsYXRlOiBgXG4gICAgPGZvcm1seS1maWVsZCAqbmdGb3I9XCJsZXQgZiBvZiBmaWVsZC5maWVsZEdyb3VwXCIgW2ZpZWxkXT1cImZcIj48L2Zvcm1seS1maWVsZD5cbiAgICA8bmctY29udGVudD48L25nLWNvbnRlbnQ+XG4gIGAsXG4gIGhvc3Q6IHtcbiAgICAnW2NsYXNzXSc6ICdmaWVsZC5maWVsZEdyb3VwQ2xhc3NOYW1lIHx8IFwiXCInLFxuICB9LFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlHcm91cCBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIGRlZmF1bHRPcHRpb25zID0ge1xuICAgIGRlZmF1bHRWYWx1ZToge30sXG4gIH07XG59XG4iXX0=