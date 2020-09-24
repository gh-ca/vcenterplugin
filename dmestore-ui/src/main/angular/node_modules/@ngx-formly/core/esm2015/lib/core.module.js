/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { NgModule, ANALYZE_FOR_ENTRY_COMPONENTS, Inject, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormlyForm } from './components/formly.form';
import { FormlyField } from './components/formly.field';
import { FormlyAttributes } from './components/formly.attributes';
import { FormlyConfig, FORMLY_CONFIG } from './services/formly.config';
import { FormlyFormBuilder } from './services/formly.form.builder';
import { FormlyGroup } from './templates/formly.group';
import { FormlyValidationMessage } from './templates/formly.validation-message';
import { FormlyTemplateType } from './templates/field-template.type';
import { FieldExpressionExtension } from './extensions/field-expression/field-expression';
import { FieldValidationExtension } from './extensions/field-validation/field-validation';
import { FieldFormExtension } from './extensions/field-form/field-form';
import { CoreExtension } from './extensions/core/core';
/**
 * @param {?} formlyConfig
 * @return {?}
 */
export function defaultFormlyConfig(formlyConfig) {
    return {
        types: [
            { name: 'formly-group', component: FormlyGroup },
            { name: 'formly-template', component: FormlyTemplateType },
        ],
        extensions: [
            { name: 'core', extension: new CoreExtension(formlyConfig) },
            { name: 'field-validation', extension: new FieldValidationExtension(formlyConfig) },
            { name: 'field-form', extension: new FieldFormExtension(formlyConfig) },
            { name: 'field-expression', extension: new FieldExpressionExtension() },
        ],
    };
}
export class FormlyModule {
    /**
     * @param {?} configService
     * @param {?=} configs
     */
    constructor(configService, configs = []) {
        if (!configs) {
            return;
        }
        configs.forEach((/**
         * @param {?} config
         * @return {?}
         */
        config => configService.addConfig(config)));
    }
    /**
     * @param {?=} config
     * @return {?}
     */
    static forRoot(config = {}) {
        return {
            ngModule: FormlyModule,
            providers: [
                { provide: FORMLY_CONFIG, multi: true, useFactory: defaultFormlyConfig, deps: [FormlyConfig] },
                { provide: FORMLY_CONFIG, useValue: config, multi: true },
                { provide: ANALYZE_FOR_ENTRY_COMPONENTS, useValue: config, multi: true },
                FormlyConfig,
                FormlyFormBuilder,
            ],
        };
    }
    /**
     * @param {?=} config
     * @return {?}
     */
    static forChild(config = {}) {
        return {
            ngModule: FormlyModule,
            providers: [
                { provide: FORMLY_CONFIG, useValue: config, multi: true },
                { provide: ANALYZE_FOR_ENTRY_COMPONENTS, useValue: config, multi: true },
                FormlyFormBuilder,
            ],
        };
    }
}
FormlyModule.decorators = [
    { type: NgModule, args: [{
                declarations: [
                    FormlyForm,
                    FormlyField,
                    FormlyAttributes,
                    FormlyGroup,
                    FormlyValidationMessage,
                    FormlyTemplateType,
                ],
                entryComponents: [FormlyGroup, FormlyTemplateType],
                exports: [FormlyForm, FormlyField, FormlyAttributes, FormlyGroup, FormlyValidationMessage],
                imports: [CommonModule],
            },] }
];
/** @nocollapse */
FormlyModule.ctorParameters = () => [
    { type: FormlyConfig },
    { type: Array, decorators: [{ type: Optional }, { type: Inject, args: [FORMLY_CONFIG,] }] }
];
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29yZS5tb2R1bGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvcmUubW9kdWxlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7QUFBQSxPQUFPLEVBQUUsUUFBUSxFQUF1Qiw0QkFBNEIsRUFBRSxNQUFNLEVBQUUsUUFBUSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzlHLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxpQkFBaUIsQ0FBQztBQUMvQyxPQUFPLEVBQUUsVUFBVSxFQUFFLE1BQU0sMEJBQTBCLENBQUM7QUFDdEQsT0FBTyxFQUFFLFdBQVcsRUFBRSxNQUFNLDJCQUEyQixDQUFDO0FBQ3hELE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLGdDQUFnQyxDQUFDO0FBQ2xFLE9BQU8sRUFBRSxZQUFZLEVBQWdCLGFBQWEsRUFBRSxNQUFNLDBCQUEwQixDQUFDO0FBQ3JGLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxNQUFNLGdDQUFnQyxDQUFDO0FBQ25FLE9BQU8sRUFBRSxXQUFXLEVBQUUsTUFBTSwwQkFBMEIsQ0FBQztBQUN2RCxPQUFPLEVBQUUsdUJBQXVCLEVBQUUsTUFBTSx1Q0FBdUMsQ0FBQztBQUNoRixPQUFPLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUVyRSxPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxnREFBZ0QsQ0FBQztBQUMxRixPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxnREFBZ0QsQ0FBQztBQUMxRixPQUFPLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxvQ0FBb0MsQ0FBQztBQUN4RSxPQUFPLEVBQUUsYUFBYSxFQUFFLE1BQU0sd0JBQXdCLENBQUM7Ozs7O0FBRXZELE1BQU0sVUFBVSxtQkFBbUIsQ0FBQyxZQUEwQjtJQUM1RCxPQUFPO1FBQ0wsS0FBSyxFQUFFO1lBQ0wsRUFBRSxJQUFJLEVBQUUsY0FBYyxFQUFFLFNBQVMsRUFBRSxXQUFXLEVBQUU7WUFDaEQsRUFBRSxJQUFJLEVBQUUsaUJBQWlCLEVBQUUsU0FBUyxFQUFFLGtCQUFrQixFQUFFO1NBQzNEO1FBQ0QsVUFBVSxFQUFFO1lBQ1YsRUFBRSxJQUFJLEVBQUUsTUFBTSxFQUFFLFNBQVMsRUFBRSxJQUFJLGFBQWEsQ0FBQyxZQUFZLENBQUMsRUFBRTtZQUM1RCxFQUFFLElBQUksRUFBRSxrQkFBa0IsRUFBRSxTQUFTLEVBQUUsSUFBSSx3QkFBd0IsQ0FBQyxZQUFZLENBQUMsRUFBRTtZQUNuRixFQUFFLElBQUksRUFBRSxZQUFZLEVBQUUsU0FBUyxFQUFFLElBQUksa0JBQWtCLENBQUMsWUFBWSxDQUFDLEVBQUU7WUFDdkUsRUFBRSxJQUFJLEVBQUUsa0JBQWtCLEVBQUUsU0FBUyxFQUFFLElBQUksd0JBQXdCLEVBQUUsRUFBRTtTQUN4RTtLQUNGLENBQUM7QUFDSixDQUFDO0FBZUQsTUFBTSxPQUFPLFlBQVk7Ozs7O0lBeUJ2QixZQUNFLGFBQTJCLEVBQ1EsVUFBMEIsRUFBRTtRQUUvRCxJQUFJLENBQUMsT0FBTyxFQUFFO1lBQ1osT0FBTztTQUNSO1FBRUQsT0FBTyxDQUFDLE9BQU87Ozs7UUFBQyxNQUFNLENBQUMsRUFBRSxDQUFDLGFBQWEsQ0FBQyxTQUFTLENBQUMsTUFBTSxDQUFDLEVBQUMsQ0FBQztJQUM3RCxDQUFDOzs7OztJQWpDRCxNQUFNLENBQUMsT0FBTyxDQUFDLFNBQXVCLEVBQUU7UUFDdEMsT0FBTztZQUNMLFFBQVEsRUFBRSxZQUFZO1lBQ3RCLFNBQVMsRUFBRTtnQkFDVCxFQUFFLE9BQU8sRUFBRSxhQUFhLEVBQUUsS0FBSyxFQUFFLElBQUksRUFBRSxVQUFVLEVBQUUsbUJBQW1CLEVBQUUsSUFBSSxFQUFFLENBQUMsWUFBWSxDQUFDLEVBQUU7Z0JBQzlGLEVBQUUsT0FBTyxFQUFFLGFBQWEsRUFBRSxRQUFRLEVBQUUsTUFBTSxFQUFFLEtBQUssRUFBRSxJQUFJLEVBQUU7Z0JBQ3pELEVBQUUsT0FBTyxFQUFFLDRCQUE0QixFQUFFLFFBQVEsRUFBRSxNQUFNLEVBQUUsS0FBSyxFQUFFLElBQUksRUFBRTtnQkFDeEUsWUFBWTtnQkFDWixpQkFBaUI7YUFDbEI7U0FDRixDQUFDO0lBQ0osQ0FBQzs7Ozs7SUFFRCxNQUFNLENBQUMsUUFBUSxDQUFDLFNBQXVCLEVBQUU7UUFDdkMsT0FBTztZQUNMLFFBQVEsRUFBRSxZQUFZO1lBQ3RCLFNBQVMsRUFBRTtnQkFDVCxFQUFFLE9BQU8sRUFBRSxhQUFhLEVBQUUsUUFBUSxFQUFFLE1BQU0sRUFBRSxLQUFLLEVBQUUsSUFBSSxFQUFFO2dCQUN6RCxFQUFFLE9BQU8sRUFBRSw0QkFBNEIsRUFBRSxRQUFRLEVBQUUsTUFBTSxFQUFFLEtBQUssRUFBRSxJQUFJLEVBQUU7Z0JBQ3hFLGlCQUFpQjthQUNsQjtTQUNGLENBQUM7SUFDSixDQUFDOzs7WUFwQ0YsUUFBUSxTQUFDO2dCQUNSLFlBQVksRUFBRTtvQkFDWixVQUFVO29CQUNWLFdBQVc7b0JBQ1gsZ0JBQWdCO29CQUNoQixXQUFXO29CQUNYLHVCQUF1QjtvQkFDdkIsa0JBQWtCO2lCQUNuQjtnQkFDRCxlQUFlLEVBQUUsQ0FBQyxXQUFXLEVBQUUsa0JBQWtCLENBQUM7Z0JBQ2xELE9BQU8sRUFBRSxDQUFDLFVBQVUsRUFBRSxXQUFXLEVBQUUsZ0JBQWdCLEVBQUUsV0FBVyxFQUFFLHVCQUF1QixDQUFDO2dCQUMxRixPQUFPLEVBQUUsQ0FBQyxZQUFZLENBQUM7YUFDeEI7Ozs7WUF0Q1EsWUFBWTt3Q0FrRWhCLFFBQVEsWUFBSSxNQUFNLFNBQUMsYUFBYSIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IE5nTW9kdWxlLCBNb2R1bGVXaXRoUHJvdmlkZXJzLCBBTkFMWVpFX0ZPUl9FTlRSWV9DT01QT05FTlRTLCBJbmplY3QsIE9wdGlvbmFsIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBDb21tb25Nb2R1bGUgfSBmcm9tICdAYW5ndWxhci9jb21tb24nO1xuaW1wb3J0IHsgRm9ybWx5Rm9ybSB9IGZyb20gJy4vY29tcG9uZW50cy9mb3JtbHkuZm9ybSc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZCB9IGZyb20gJy4vY29tcG9uZW50cy9mb3JtbHkuZmllbGQnO1xuaW1wb3J0IHsgRm9ybWx5QXR0cmlidXRlcyB9IGZyb20gJy4vY29tcG9uZW50cy9mb3JtbHkuYXR0cmlidXRlcyc7XG5pbXBvcnQgeyBGb3JtbHlDb25maWcsIENvbmZpZ09wdGlvbiwgRk9STUxZX0NPTkZJRyB9IGZyb20gJy4vc2VydmljZXMvZm9ybWx5LmNvbmZpZyc7XG5pbXBvcnQgeyBGb3JtbHlGb3JtQnVpbGRlciB9IGZyb20gJy4vc2VydmljZXMvZm9ybWx5LmZvcm0uYnVpbGRlcic7XG5pbXBvcnQgeyBGb3JtbHlHcm91cCB9IGZyb20gJy4vdGVtcGxhdGVzL2Zvcm1seS5ncm91cCc7XG5pbXBvcnQgeyBGb3JtbHlWYWxpZGF0aW9uTWVzc2FnZSB9IGZyb20gJy4vdGVtcGxhdGVzL2Zvcm1seS52YWxpZGF0aW9uLW1lc3NhZ2UnO1xuaW1wb3J0IHsgRm9ybWx5VGVtcGxhdGVUeXBlIH0gZnJvbSAnLi90ZW1wbGF0ZXMvZmllbGQtdGVtcGxhdGUudHlwZSc7XG5cbmltcG9ydCB7IEZpZWxkRXhwcmVzc2lvbkV4dGVuc2lvbiB9IGZyb20gJy4vZXh0ZW5zaW9ucy9maWVsZC1leHByZXNzaW9uL2ZpZWxkLWV4cHJlc3Npb24nO1xuaW1wb3J0IHsgRmllbGRWYWxpZGF0aW9uRXh0ZW5zaW9uIH0gZnJvbSAnLi9leHRlbnNpb25zL2ZpZWxkLXZhbGlkYXRpb24vZmllbGQtdmFsaWRhdGlvbic7XG5pbXBvcnQgeyBGaWVsZEZvcm1FeHRlbnNpb24gfSBmcm9tICcuL2V4dGVuc2lvbnMvZmllbGQtZm9ybS9maWVsZC1mb3JtJztcbmltcG9ydCB7IENvcmVFeHRlbnNpb24gfSBmcm9tICcuL2V4dGVuc2lvbnMvY29yZS9jb3JlJztcblxuZXhwb3J0IGZ1bmN0aW9uIGRlZmF1bHRGb3JtbHlDb25maWcoZm9ybWx5Q29uZmlnOiBGb3JtbHlDb25maWcpOiBDb25maWdPcHRpb24ge1xuICByZXR1cm4ge1xuICAgIHR5cGVzOiBbXG4gICAgICB7IG5hbWU6ICdmb3JtbHktZ3JvdXAnLCBjb21wb25lbnQ6IEZvcm1seUdyb3VwIH0sXG4gICAgICB7IG5hbWU6ICdmb3JtbHktdGVtcGxhdGUnLCBjb21wb25lbnQ6IEZvcm1seVRlbXBsYXRlVHlwZSB9LFxuICAgIF0sXG4gICAgZXh0ZW5zaW9uczogW1xuICAgICAgeyBuYW1lOiAnY29yZScsIGV4dGVuc2lvbjogbmV3IENvcmVFeHRlbnNpb24oZm9ybWx5Q29uZmlnKSB9LFxuICAgICAgeyBuYW1lOiAnZmllbGQtdmFsaWRhdGlvbicsIGV4dGVuc2lvbjogbmV3IEZpZWxkVmFsaWRhdGlvbkV4dGVuc2lvbihmb3JtbHlDb25maWcpIH0sXG4gICAgICB7IG5hbWU6ICdmaWVsZC1mb3JtJywgZXh0ZW5zaW9uOiBuZXcgRmllbGRGb3JtRXh0ZW5zaW9uKGZvcm1seUNvbmZpZykgfSxcbiAgICAgIHsgbmFtZTogJ2ZpZWxkLWV4cHJlc3Npb24nLCBleHRlbnNpb246IG5ldyBGaWVsZEV4cHJlc3Npb25FeHRlbnNpb24oKSB9LFxuICAgIF0sXG4gIH07XG59XG5cbkBOZ01vZHVsZSh7XG4gIGRlY2xhcmF0aW9uczogW1xuICAgIEZvcm1seUZvcm0sXG4gICAgRm9ybWx5RmllbGQsXG4gICAgRm9ybWx5QXR0cmlidXRlcyxcbiAgICBGb3JtbHlHcm91cCxcbiAgICBGb3JtbHlWYWxpZGF0aW9uTWVzc2FnZSxcbiAgICBGb3JtbHlUZW1wbGF0ZVR5cGUsXG4gIF0sXG4gIGVudHJ5Q29tcG9uZW50czogW0Zvcm1seUdyb3VwLCBGb3JtbHlUZW1wbGF0ZVR5cGVdLFxuICBleHBvcnRzOiBbRm9ybWx5Rm9ybSwgRm9ybWx5RmllbGQsIEZvcm1seUF0dHJpYnV0ZXMsIEZvcm1seUdyb3VwLCBGb3JtbHlWYWxpZGF0aW9uTWVzc2FnZV0sXG4gIGltcG9ydHM6IFtDb21tb25Nb2R1bGVdLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlNb2R1bGUge1xuICBzdGF0aWMgZm9yUm9vdChjb25maWc6IENvbmZpZ09wdGlvbiA9IHt9KTogTW9kdWxlV2l0aFByb3ZpZGVyczxGb3JtbHlNb2R1bGU+IHtcbiAgICByZXR1cm4ge1xuICAgICAgbmdNb2R1bGU6IEZvcm1seU1vZHVsZSxcbiAgICAgIHByb3ZpZGVyczogW1xuICAgICAgICB7IHByb3ZpZGU6IEZPUk1MWV9DT05GSUcsIG11bHRpOiB0cnVlLCB1c2VGYWN0b3J5OiBkZWZhdWx0Rm9ybWx5Q29uZmlnLCBkZXBzOiBbRm9ybWx5Q29uZmlnXSB9LFxuICAgICAgICB7IHByb3ZpZGU6IEZPUk1MWV9DT05GSUcsIHVzZVZhbHVlOiBjb25maWcsIG11bHRpOiB0cnVlIH0sXG4gICAgICAgIHsgcHJvdmlkZTogQU5BTFlaRV9GT1JfRU5UUllfQ09NUE9ORU5UUywgdXNlVmFsdWU6IGNvbmZpZywgbXVsdGk6IHRydWUgfSxcbiAgICAgICAgRm9ybWx5Q29uZmlnLFxuICAgICAgICBGb3JtbHlGb3JtQnVpbGRlcixcbiAgICAgIF0sXG4gICAgfTtcbiAgfVxuXG4gIHN0YXRpYyBmb3JDaGlsZChjb25maWc6IENvbmZpZ09wdGlvbiA9IHt9KTogTW9kdWxlV2l0aFByb3ZpZGVyczxGb3JtbHlNb2R1bGU+IHtcbiAgICByZXR1cm4ge1xuICAgICAgbmdNb2R1bGU6IEZvcm1seU1vZHVsZSxcbiAgICAgIHByb3ZpZGVyczogW1xuICAgICAgICB7IHByb3ZpZGU6IEZPUk1MWV9DT05GSUcsIHVzZVZhbHVlOiBjb25maWcsIG11bHRpOiB0cnVlIH0sXG4gICAgICAgIHsgcHJvdmlkZTogQU5BTFlaRV9GT1JfRU5UUllfQ09NUE9ORU5UUywgdXNlVmFsdWU6IGNvbmZpZywgbXVsdGk6IHRydWUgfSxcbiAgICAgICAgRm9ybWx5Rm9ybUJ1aWxkZXIsXG4gICAgICBdLFxuICAgIH07XG4gIH1cblxuICBjb25zdHJ1Y3RvcihcbiAgICBjb25maWdTZXJ2aWNlOiBGb3JtbHlDb25maWcsXG4gICAgQE9wdGlvbmFsKCkgQEluamVjdChGT1JNTFlfQ09ORklHKSBjb25maWdzOiBDb25maWdPcHRpb25bXSA9IFtdLFxuICApIHtcbiAgICBpZiAoIWNvbmZpZ3MpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBjb25maWdzLmZvckVhY2goY29uZmlnID0+IGNvbmZpZ1NlcnZpY2UuYWRkQ29uZmlnKGNvbmZpZykpO1xuICB9XG59XG4iXX0=