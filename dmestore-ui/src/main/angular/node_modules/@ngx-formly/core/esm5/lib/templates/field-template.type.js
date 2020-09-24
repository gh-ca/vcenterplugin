/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FieldType } from './field.type';
var FormlyTemplateType = /** @class */ (function (_super) {
    tslib_1.__extends(FormlyTemplateType, _super);
    function FormlyTemplateType(sanitizer) {
        var _this = _super.call(this) || this;
        _this.sanitizer = sanitizer;
        _this.innerHtml = { content: null, template: null };
        return _this;
    }
    Object.defineProperty(FormlyTemplateType.prototype, "template", {
        get: /**
         * @return {?}
         */
        function () {
            if (this.field && (this.field.template !== this.innerHtml.template)) {
                this.innerHtml = {
                    template: this.field.template,
                    content: this.to.safeHtml
                        ? this.sanitizer.bypassSecurityTrustHtml(this.field.template)
                        : this.field.template,
                };
            }
            return this.innerHtml.content;
        },
        enumerable: true,
        configurable: true
    });
    FormlyTemplateType.decorators = [
        { type: Component, args: [{
                    selector: 'formly-template',
                    template: "<div [innerHtml]=\"template\"></div>"
                }] }
    ];
    /** @nocollapse */
    FormlyTemplateType.ctorParameters = function () { return [
        { type: DomSanitizer }
    ]; };
    return FormlyTemplateType;
}(FieldType));
export { FormlyTemplateType };
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtdGVtcGxhdGUudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLXRlbXBsYXRlLnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzFDLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSwyQkFBMkIsQ0FBQztBQUN6RCxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sY0FBYyxDQUFDO0FBRXpDO0lBSXdDLDhDQUFTO0lBZS9DLDRCQUFvQixTQUF1QjtRQUEzQyxZQUNFLGlCQUFPLFNBQ1I7UUFGbUIsZUFBUyxHQUFULFNBQVMsQ0FBYztRQURuQyxlQUFTLEdBQUcsRUFBRSxPQUFPLEVBQUUsSUFBSSxFQUFFLFFBQVEsRUFBRSxJQUFJLEVBQUUsQ0FBQzs7SUFHdEQsQ0FBQztJQWhCRCxzQkFBSSx3Q0FBUTs7OztRQUFaO1lBQ0UsSUFBSSxJQUFJLENBQUMsS0FBSyxJQUFJLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxRQUFRLEtBQUssSUFBSSxDQUFDLFNBQVMsQ0FBQyxRQUFRLENBQUMsRUFBRTtnQkFDbkUsSUFBSSxDQUFDLFNBQVMsR0FBRztvQkFDZixRQUFRLEVBQUUsSUFBSSxDQUFDLEtBQUssQ0FBQyxRQUFRO29CQUM3QixPQUFPLEVBQUUsSUFBSSxDQUFDLEVBQUUsQ0FBQyxRQUFRO3dCQUN2QixDQUFDLENBQUMsSUFBSSxDQUFDLFNBQVMsQ0FBQyx1QkFBdUIsQ0FBQyxJQUFJLENBQUMsS0FBSyxDQUFDLFFBQVEsQ0FBQzt3QkFDN0QsQ0FBQyxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsUUFBUTtpQkFDeEIsQ0FBQzthQUNIO1lBRUQsT0FBTyxJQUFJLENBQUMsU0FBUyxDQUFDLE9BQU8sQ0FBQztRQUNoQyxDQUFDOzs7T0FBQTs7Z0JBaEJGLFNBQVMsU0FBQztvQkFDVCxRQUFRLEVBQUUsaUJBQWlCO29CQUMzQixRQUFRLEVBQUUsc0NBQW9DO2lCQUMvQzs7OztnQkFOUSxZQUFZOztJQXlCckIseUJBQUM7Q0FBQSxBQXRCRCxDQUl3QyxTQUFTLEdBa0JoRDtTQWxCWSxrQkFBa0I7Ozs7OztJQWM3Qix1Q0FBc0Q7Ozs7O0lBQzFDLHVDQUErQiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IENvbXBvbmVudCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgRG9tU2FuaXRpemVyIH0gZnJvbSAnQGFuZ3VsYXIvcGxhdGZvcm0tYnJvd3Nlcic7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICcuL2ZpZWxkLnR5cGUnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdmb3JtbHktdGVtcGxhdGUnLFxuICB0ZW1wbGF0ZTogYDxkaXYgW2lubmVySHRtbF09XCJ0ZW1wbGF0ZVwiPjwvZGl2PmAsXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seVRlbXBsYXRlVHlwZSBleHRlbmRzIEZpZWxkVHlwZSB7XG4gIGdldCB0ZW1wbGF0ZSgpIHtcbiAgICBpZiAodGhpcy5maWVsZCAmJiAodGhpcy5maWVsZC50ZW1wbGF0ZSAhPT0gdGhpcy5pbm5lckh0bWwudGVtcGxhdGUpKSB7XG4gICAgICB0aGlzLmlubmVySHRtbCA9IHtcbiAgICAgICAgdGVtcGxhdGU6IHRoaXMuZmllbGQudGVtcGxhdGUsXG4gICAgICAgIGNvbnRlbnQ6IHRoaXMudG8uc2FmZUh0bWxcbiAgICAgICAgICA/IHRoaXMuc2FuaXRpemVyLmJ5cGFzc1NlY3VyaXR5VHJ1c3RIdG1sKHRoaXMuZmllbGQudGVtcGxhdGUpXG4gICAgICAgICAgOiB0aGlzLmZpZWxkLnRlbXBsYXRlLFxuICAgICAgfTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy5pbm5lckh0bWwuY29udGVudDtcbiAgfVxuXG4gIHByaXZhdGUgaW5uZXJIdG1sID0geyBjb250ZW50OiBudWxsLCB0ZW1wbGF0ZTogbnVsbCB9O1xuICBjb25zdHJ1Y3Rvcihwcml2YXRlIHNhbml0aXplcjogRG9tU2FuaXRpemVyKSB7XG4gICAgc3VwZXIoKTtcbiAgfVxufVxuIl19