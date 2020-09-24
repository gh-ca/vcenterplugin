/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Inject, Optional } from '@angular/core';
import { FormArray } from '@angular/forms';
import { FieldType } from './field.type';
import { clone, isNullOrUndefined, assignFieldValue } from '../utils';
import { FormlyFormBuilder } from '../services/formly.form.builder';
import { FORMLY_CONFIG } from '../services/formly.config';
import { registerControl, unregisterControl } from '../extensions/field-form/utils';
/**
 * @abstract
 * @template F
 */
export class FieldArrayType extends FieldType {
    /**
     * @param {?=} builder
     */
    constructor(builder) {
        super();
        this.defaultOptions = {
            defaultValue: [],
        };
        if (builder instanceof FormlyFormBuilder) {
            console.warn(`NgxFormly: passing 'FormlyFormBuilder' to '${this.constructor.name}' type is not required anymore, you may remove it!`);
        }
    }
    /**
     * @param {?} field
     * @return {?}
     */
    onPopulate(field) {
        if (!field.formControl && field.key) {
            registerControl(field, new FormArray([], { updateOn: field.modelOptions.updateOn }));
        }
        field.fieldGroup = field.fieldGroup || [];
        /** @type {?} */
        const length = field.model ? field.model.length : 0;
        if (field.fieldGroup.length > length) {
            for (let i = field.fieldGroup.length - 1; i >= length; --i) {
                unregisterControl(field.fieldGroup[i]);
                field.fieldGroup.splice(i, 1);
            }
        }
        for (let i = field.fieldGroup.length; i < length; i++) {
            /** @type {?} */
            const f = Object.assign({}, clone(field.fieldArray), { key: `${i}` });
            field.fieldGroup.push(f);
        }
    }
    /**
     * @param {?=} i
     * @param {?=} initialModel
     * @param {?=} __2
     * @return {?}
     */
    add(i, initialModel, { markAsDirty } = { markAsDirty: true }) {
        i = isNullOrUndefined(i) ? this.field.fieldGroup.length : i;
        if (!this.model) {
            assignFieldValue(this.field, []);
        }
        this.model.splice(i, 0, initialModel ? clone(initialModel) : undefined);
        ((/** @type {?} */ (this.options)))._buildForm(true);
        markAsDirty && this.formControl.markAsDirty();
    }
    /**
     * @param {?} i
     * @param {?=} __1
     * @return {?}
     */
    remove(i, { markAsDirty } = { markAsDirty: true }) {
        this.model.splice(i, 1);
        unregisterControl(this.field.fieldGroup[i], true);
        this.field.fieldGroup.splice(i, 1);
        this.field.fieldGroup.forEach((/**
         * @param {?} f
         * @param {?} key
         * @return {?}
         */
        (f, key) => f.key = `${key}`));
        ((/** @type {?} */ (this.options)))._buildForm(true);
        markAsDirty && this.formControl.markAsDirty();
    }
}
/** @nocollapse */
FieldArrayType.ctorParameters = () => [
    { type: FormlyFormBuilder, decorators: [{ type: Inject, args: [FORMLY_CONFIG,] }, { type: Optional }] }
];
if (false) {
    /** @type {?} */
    FieldArrayType.prototype.formControl;
    /** @type {?} */
    FieldArrayType.prototype.defaultOptions;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZmllbGQtYXJyYXkudHlwZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L2NvcmUvIiwic291cmNlcyI6WyJsaWIvdGVtcGxhdGVzL2ZpZWxkLWFycmF5LnR5cGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxNQUFNLEVBQUUsUUFBUSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQ2pELE9BQU8sRUFBRSxTQUFTLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUMzQyxPQUFPLEVBQUUsU0FBUyxFQUFFLE1BQU0sY0FBYyxDQUFDO0FBQ3pDLE9BQU8sRUFBRSxLQUFLLEVBQUUsaUJBQWlCLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSxVQUFVLENBQUM7QUFDdEUsT0FBTyxFQUFFLGlCQUFpQixFQUFFLE1BQU0saUNBQWlDLENBQUM7QUFFcEUsT0FBTyxFQUFFLGFBQWEsRUFBbUIsTUFBTSwyQkFBMkIsQ0FBQztBQUMzRSxPQUFPLEVBQUUsZUFBZSxFQUFFLGlCQUFpQixFQUFFLE1BQU0sZ0NBQWdDLENBQUM7Ozs7O0FBRXBGLE1BQU0sT0FBZ0IsY0FBZ0UsU0FBUSxTQUFZOzs7O0lBTXhHLFlBQStDLE9BQTJCO1FBQ3hFLEtBQUssRUFBRSxDQUFDO1FBTFYsbUJBQWMsR0FBUTtZQUNwQixZQUFZLEVBQUUsRUFBRTtTQUNqQixDQUFDO1FBS0EsSUFBSSxPQUFPLFlBQVksaUJBQWlCLEVBQUU7WUFDeEMsT0FBTyxDQUFDLElBQUksQ0FBQyw4Q0FBOEMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxJQUFJLG9EQUFvRCxDQUFDLENBQUM7U0FDdkk7SUFDSCxDQUFDOzs7OztJQUVELFVBQVUsQ0FBQyxLQUF3QjtRQUNqQyxJQUFJLENBQUMsS0FBSyxDQUFDLFdBQVcsSUFBSSxLQUFLLENBQUMsR0FBRyxFQUFFO1lBQ25DLGVBQWUsQ0FBQyxLQUFLLEVBQUUsSUFBSSxTQUFTLENBQUMsRUFBRSxFQUFFLEVBQUUsUUFBUSxFQUFFLEtBQUss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QUFFLENBQUM7SUFDaEQsQ0FBQzs7OztZQTVETSxpQkFBaUIsdUJBV1gsTUFBTSxTQUFDLGFBQWEsY0FBRyxRQUFROzs7O0lBTDVDLHFDQUF1Qjs7SUFDdkIsd0NBRUUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbmplY3QsIE9wdGlvbmFsIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBGb3JtQXJyYXkgfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBGaWVsZFR5cGUgfSBmcm9tICcuL2ZpZWxkLnR5cGUnO1xuaW1wb3J0IHsgY2xvbmUsIGlzTnVsbE9yVW5kZWZpbmVkLCBhc3NpZ25GaWVsZFZhbHVlIH0gZnJvbSAnLi4vdXRpbHMnO1xuaW1wb3J0IHsgRm9ybWx5Rm9ybUJ1aWxkZXIgfSBmcm9tICcuLi9zZXJ2aWNlcy9mb3JtbHkuZm9ybS5idWlsZGVyJztcbmltcG9ydCB7IEZvcm1seUZpZWxkQ29uZmlnIH0gZnJvbSAnLi4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQuY29uZmlnJztcbmltcG9ydCB7IEZPUk1MWV9DT05GSUcsIEZvcm1seUV4dGVuc2lvbiB9IGZyb20gJy4uL3NlcnZpY2VzL2Zvcm1seS5jb25maWcnO1xuaW1wb3J0IHsgcmVnaXN0ZXJDb250cm9sLCB1bnJlZ2lzdGVyQ29udHJvbCB9IGZyb20gJy4uL2V4dGVuc2lvbnMvZmllbGQtZm9ybS91dGlscyc7XG5cbmV4cG9ydCBhYnN0cmFjdCBjbGFzcyBGaWVsZEFycmF5VHlwZTxGIGV4dGVuZHMgRm9ybWx5RmllbGRDb25maWcgPSBGb3JtbHlGaWVsZENvbmZpZz4gZXh0ZW5kcyBGaWVsZFR5cGU8Rj4gaW1wbGVtZW50cyBGb3JtbHlFeHRlbnNpb24ge1xuICBmb3JtQ29udHJvbDogRm9ybUFycmF5O1xuICBkZWZhdWx0T3B0aW9uczogYW55ID0ge1xuICAgIGRlZmF1bHRWYWx1ZTogW10sXG4gIH07XG5cbiAgY29uc3RydWN0b3IoQEluamVjdChGT1JNTFlfQ09ORklHKSBAT3B0aW9uYWwoKSBidWlsZGVyPzogRm9ybWx5Rm9ybUJ1aWxkZXIpIHtcbiAgICBzdXBlcigpO1xuXG4gICAgaWYgKGJ1aWxkZXIgaW5zdGFuY2VvZiBGb3JtbHlGb3JtQnVpbGRlcikge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHBhc3NpbmcgJ0Zvcm1seUZvcm1CdWlsZGVyJyB0byAnJHt0aGlzLmNvbnN0cnVjdG9yLm5hbWV9JyB0eXBlIGlzIG5vdCByZXF1aXJlZCBhbnltb3JlLCB5b3UgbWF5IHJlbW92ZSBpdCFgKTtcbiAgICB9XG4gIH1cblxuICBvblBvcHVsYXRlKGZpZWxkOiBGb3JtbHlGaWVsZENvbmZpZykge1xuICAgIGlmICghZmllbGQuZm9ybUNvbnRyb2wgJiYgZmllbGQua2V5KSB7XG4gICAgICByZWdpc3RlckNvbnRyb2woZmllbGQsIG5ldyBGb3JtQXJyYXkoW10sIHsgdXBkYXRlT246IGZpZWxkLm1vZGVsT3B0aW9ucy51cGRhdGVPbiB9KSk7XG4gICAgfVxuXG4gICAgZmllbGQuZmllbGRHcm91cCA9IGZpZWxkLmZpZWxkR3JvdXAgfHwgW107XG5cbiAgICBjb25zdCBsZW5ndGggPSBmaWVsZC5tb2RlbCA/IGZpZWxkLm1vZGVsLmxlbmd0aCA6IDA7XG4gICAgaWYgKGZpZWxkLmZpZWxkR3JvdXAubGVuZ3RoID4gbGVuZ3RoKSB7XG4gICAgICBmb3IgKGxldCBpID0gZmllbGQuZmllbGRHcm91cC5sZW5ndGggLSAxOyBpID49IGxlbmd0aDsgLS1pKSB7XG4gICAgICAgIHVucmVnaXN0ZXJDb250cm9sKGZpZWxkLmZpZWxkR3JvdXBbaV0pO1xuICAgICAgICBmaWVsZC5maWVsZEdyb3VwLnNwbGljZShpLCAxKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICBmb3IgKGxldCBpID0gZmllbGQuZmllbGRHcm91cC5sZW5ndGg7IGkgPCBsZW5ndGg7IGkrKykge1xuICAgICAgY29uc3QgZiA9IHsgLi4uY2xvbmUoZmllbGQuZmllbGRBcnJheSksIGtleTogYCR7aX1gIH07XG4gICAgICBmaWVsZC5maWVsZEdyb3VwLnB1c2goZik7XG4gICAgfVxuICB9XG5cbiAgYWRkKGk/OiBudW1iZXIsIGluaXRpYWxNb2RlbD86IGFueSwgeyBtYXJrQXNEaXJ0eSB9ID0geyBtYXJrQXNEaXJ0eTogdHJ1ZSB9KSB7XG4gICAgaSA9IGlzTnVsbE9yVW5kZWZpbmVkKGkpID8gdGhpcy5maWVsZC5maWVsZEdyb3VwLmxlbmd0aCA6IGk7XG4gICAgaWYgKCF0aGlzLm1vZGVsKSB7XG4gICAgICBhc3NpZ25GaWVsZFZhbHVlKHRoaXMuZmllbGQsIFtdKTtcbiAgICB9XG5cbiAgICB0aGlzLm1vZGVsLnNwbGljZShpLCAwLCBpbml0aWFsTW9kZWwgPyBjbG9uZShpbml0aWFsTW9kZWwpIDogdW5kZWZpbmVkKTtcblxuICAgICg8YW55PiB0aGlzLm9wdGlvbnMpLl9idWlsZEZvcm0odHJ1ZSk7XG4gICAgbWFya0FzRGlydHkgJiYgdGhpcy5mb3JtQ29udHJvbC5tYXJrQXNEaXJ0eSgpO1xuICB9XG5cbiAgcmVtb3ZlKGk6IG51bWJlciwgeyBtYXJrQXNEaXJ0eSB9ID0geyBtYXJrQXNEaXJ0eTogdHJ1ZSB9KSB7XG4gICAgdGhpcy5tb2RlbC5zcGxpY2UoaSwgMSk7XG4gICAgdW5yZWdpc3RlckNvbnRyb2wodGhpcy5maWVsZC5maWVsZEdyb3VwW2ldLCB0cnVlKTtcbiAgICB0aGlzLmZpZWxkLmZpZWxkR3JvdXAuc3BsaWNlKGksIDEpO1xuICAgIHRoaXMuZmllbGQuZmllbGRHcm91cC5mb3JFYWNoKChmLCBrZXkpID0+IGYua2V5ID0gYCR7a2V5fWApO1xuXG4gICAgKDxhbnk+IHRoaXMub3B0aW9ucykuX2J1aWxkRm9ybSh0cnVlKTtcbiAgICBtYXJrQXNEaXJ0eSAmJiB0aGlzLmZvcm1Db250cm9sLm1hcmtBc0RpcnR5KCk7XG4gIH1cbn1cbiJdfQ==