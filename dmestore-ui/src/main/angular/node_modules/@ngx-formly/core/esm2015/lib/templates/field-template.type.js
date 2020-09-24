/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FieldType } from './field.type';
export class FormlyTemplateType extends FieldType {
    /**
     * @param {?} sanitizer
     */
    constructor(sanitizer) {
        super();
        this.sanitizer = sanitizer;
        this.innerHtml = { content: null, template: null };
    }
    /**
     * @return {?}
     */
    get template() {
        if (this.field && (this.field.template !== this.innerHtml.template)) {
            this.innerHtml = {
                template: this.field.template,
                content: this.to.safeHtml
                    ? this.sanitizer.bypassSecurityTrustHtml(this.field.template)
                    : this.field.template,
            };
        }
        return this.innerHtml.content;
    }
}
FormlyTemplateType.decorators = [
    { type: Component, args: [{
                selector: 'formly-template',
                template: `<div [innerHtml]="template"></div>`
            }] }
];
/** @nocollapse */
FormlyTemplateType.ctorParameters = () => [
    { type: DomSanitizer }
];
if (false) {
    /**
     * @type {?}
     * @private
     */
    FormlyTemplateType.prototype.innerHtml;
    /**
     * @type {?}
     * @private
     */
    FormlyTemplateType.prototype.sanitizer;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtdGVtcGxhdGUudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLXRlbXBsYXRlLnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDMUMsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLDJCQUEyQixDQUFDO0FBQ3pELE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxjQUFjLENBQUM7QUFNekMsTUFBTSxPQUFPLGtCQUFtQixTQUFRLFNBQVM7Ozs7SUFlL0MsWUFBb0IsU0FBdUI7UUFDekMsS0FBSyxFQUFFLENBQUM7UUFEVSxjQUFTLEdBQVQsU0FBUyxDQUFjO1FBRG5DLGNBQVMsR0FBRyxFQUFFLE9BQU8sRUFBRSxJQUFJLEVBQUUsUUFBUSxFQUFFLElBQUksRUFBRSxDQUFDO0lBR3RELENBQUM7Ozs7SUFoQkQsSUFBSSxRQUFRO1FBQ1YsSUFBSSxJQUFJLENBQUMsS0FBSyxJQUFJLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxRQUFRLEtBQUssSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLENBQUMsRUFBRTtZQUNuRSxJQUFJLENBQUMsU0FBUyxHQUFHO2dCQUNmLFFBQVEsRUFBRSxJQUFJLENBQUMsS0FBSyxDQUFDLFFBQVE7Z0JBQzdCLE9BQU8sRUFBRSxJQUFJLENBQUMsRUFBRSxDQUFDLFFBQVE7b0JBQ3ZCLENBQUMsQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDLHVCQUF1QixDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsUUFBUSxDQUFDO29CQUM3RCxDQUFDLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxRQUFRO2FBQ3hCLENBQUM7U0FDSDtRQUVELE9BQU8sSUFBSSxDQUFDLFNBQVMsQ0FBQyxPQUFPLENBQUM7SUFDaEMsQ0FBQzs7O1lBaEJGLFNBQVMsU0FBQztnQkFDVCxRQUFRLEVBQUUsaUJBQWlCO2dCQUMzQixRQUFRLEVBQUUsb0NBQW9DO2FBQy9DOzs7O1lBTlEsWUFBWTs7Ozs7OztJQXFCbkIsdUNBQXNEOzs7OztJQUMxQyx1Q0FBK0IiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBDb21wb25lbnQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IERvbVNhbml0aXplciB9IGZyb20gJ0Bhbmd1bGFyL3BsYXRmb3JtLWJyb3dzZXInO1xuaW1wb3J0IHsgRmllbGRUeXBlIH0gZnJvbSAnLi9maWVsZC50eXBlJztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnZm9ybWx5LXRlbXBsYXRlJyxcbiAgdGVtcGxhdGU6IGA8ZGl2IFtpbm5lckh0bWxdPVwidGVtcGxhdGVcIj48L2Rpdj5gLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlUZW1wbGF0ZVR5cGUgZXh0ZW5kcyBGaWVsZFR5cGUge1xuICBnZXQgdGVtcGxhdGUoKSB7XG4gICAgaWYgKHRoaXMuZmllbGQgJiYgKHRoaXMuZmllbGQudGVtcGxhdGUgIT09IHRoaXMuaW5uZXJIdG1sLnRlbXBsYXRlKSkge1xuICAgICAgdGhpcy5pbm5lckh0bWwgPSB7XG4gICAgICAgIHRlbXBsYXRlOiB0aGlzLmZpZWxkLnRlbXBsYXRlLFxuICAgICAgICBjb250ZW50OiB0aGlzLnRvLnNhZmVIdG1sXG4gICAgICAgICAgPyB0aGlzLnNhbml0aXplci5ieXBhc3NTZWN1cml0eVRydXN0SHRtbCh0aGlzLmZpZWxkLnRlbXBsYXRlKVxuICAgICAgICAgIDogdGhpcy5maWVsZC50ZW1wbGF0ZSxcbiAgICAgIH07XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMuaW5uZXJIdG1sLmNvbnRlbnQ7XG4gIH1cblxuICBwcml2YXRlIGlubmVySHRtbCA9IHsgY29udGVudDogbnVsbCwgdGVtcGxhdGU6IG51bGwgfTtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSBzYW5pdGl6ZXI6IERvbVNhbml0aXplcikge1xuICAgIHN1cGVyKCk7XG4gIH1cbn1cbiJdfQ==