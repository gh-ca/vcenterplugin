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
var FormlyModule = /** @class */ (function () {
    function FormlyModule(configService, configs) {
        if (configs === void 0) { configs = []; }
        if (!configs) {
            return;
        }
        configs.forEach((/**
         * @param {?} config
         * @return {?}
         */
        function (config) { return configService.addConfig(config); }));
    }
    /**
     * @param {?=} config
     * @return {?}
     */
    FormlyModule.forRoot = /**
     * @param {?=} config
     * @return {?}
     */
    function (config) {
        if (config === void 0) { config = {}; }
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
    };
    /**
     * @param {?=} config
     * @return {?}
     */
    FormlyModule.forChild = /**
     * @param {?=} config
     * @return {?}
     */
    function (config) {
        if (config === void 0) { config = {}; }
        return {
            ngModule: FormlyModule,
            providers: [
                { provide: FORMLY_CONFIG, useValue: config, multi: true },
                { provide: ANALYZE_FOR_ENTRY_COMPONENTS, useValue: config, multi: true },
                FormlyFormBuilder,
            ],
        };
    };
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
    FormlyModule.ctorParameters = function () { return [
        { type: FormlyConfig },
        { type: Array, decorators: [{ type: Optional }, { type: Inject, args: [FORMLY_CONFIG,] }] }
    ]; };
    return FormlyModule;
}());
export { FormlyModule };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29yZS5tb2R1bGUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2NvcmUubW9kdWxlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7QUFBQSxPQUFPLEVBQUUsUUFBUSxFQUF1Qiw0QkFBNEIsRUFBRSxNQUFNLEVBQUUsUUFBUSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzlHLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxpQkFBaUIsQ0FBQztBQUMvQyxPQUFPLEVBQUUsVUFBVSxFQUFFLE1BQU0sMEJBQTBCLENBQUM7QUFDdEQsT0FBTyxFQUFFLFdBQVcsRUFBRSxNQUFNLDJCQUEyQixDQUFDO0FBQ3hELE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLGdDQUFnQyxDQUFDO0FBQ2xFLE9BQU8sRUFBRSxZQUFZLEVBQWdCLGFBQWEsRUFBRSxNQUFNLDBCQUEwQixDQUFDO0FBQ3JGLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxNQUFNLGdDQUFnQyxDQUFDO0FBQ25FLE9BQU8sRUFBRSxXQUFXLEVBQUUsTUFBTSwwQkFBMEIsQ0FBQztBQUN2RCxPQUFPLEVBQUUsdUJBQXVCLEVBQUUsTUFBTSx1Q0FBdUMsQ0FBQztBQUNoRixPQUFPLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUVyRSxPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxnREFBZ0QsQ0FBQztBQUMxRixPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxnREFBZ0QsQ0FBQztBQUMxRixPQUFPLEVBQUUsa0JBQWtCLEVBQUUsTUFBTSxvQ0FBb0MsQ0FBQztBQUN4RSxPQUFPLEVBQUUsYUFBYSxFQUFFLE1BQU0sd0JBQXdCLENBQUM7Ozs7O0FBRXZELE1BQU0sVUFBVSxtQkFBbUIsQ0FBQyxZQUEwQjtJQUM1RCxPQUFPO1FBQ0wsS0FBSyxFQUFFO1lBQ0wsRUFBRSxJQUFJLEVBQUUsY0FBYyxFQUFFLFNBQVMsRUFBRSxXQUFXLEVBQUU7WUFDaEQsRUFBRSxJQUFJLEVBQUUsaUJBQWlCLEVBQUUsU0FBUyxFQUFFLGtCQUFrQixFQUFFO1NBQzNEO1FBQ0QsVUFBVSxFQUFFO1lBQ1YsRUFBRSxJQUFJLEVBQUUsTUFBTSxFQUFFLFNBQVMsRUFBRSxJQUFJLGFBQWEsQ0FBQyxZQUFZLENBQUMsRUFBRTtZQUM1RCxFQUFFLElBQUksRUFBRSxrQkFBa0IsRUFBRSxTQUFTLEVBQUUsSUFBSSx3QkFBd0IsQ0FBQyxZQUFZLENBQUMsRUFBRTtZQUNuRixFQUFFLElBQUksRUFBRSxZQUFZLEVBQUUsU0FBUyxFQUFFLElBQUksa0JBQWtCLENBQUMsWUFBWSxDQUFDLEVBQUU7WUFDdkUsRUFBRSxJQUFJLEVBQUUsa0JBQWtCLEVBQUUsU0FBUyxFQUFFLElBQUksd0JBQXdCLEVBQUUsRUFBRTtTQUN4RTtLQUNGLENBQUM7QUFDSixDQUFDO0FBRUQ7SUFzQ0Usc0JBQ0UsYUFBMkIsRUFDUSxPQUE0QjtRQUEvRCx3QkFBQSxFQUFBLFlBQStEO1FBRS9ELElBQUksQ0FBQyxPQUFPLEVBQUU7WUFDWixPQUFPO1NBQ1I7UUFFRCxPQUFPLENBQUMsT0FBTzs7OztRQUFDLFVBQUEsTUFBTSxJQUFJLE9BQUEsYUFBYSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsRUFBL0IsQ0FBK0IsRUFBQyxDQUFDO0lBQzdELENBQUM7Ozs7O0lBakNNLG9CQUFPOzs7O0lBQWQsVUFBZSxNQUF5QjtRQUF6Qix1QkFBQSxFQUFBLFdBQXlCO1FBQ3RDLE9BQU87WUFDTCxRQUFRLEVBQUUsWUFBWTtZQUN0QixTQUFTLEVBQUU7Z0JBQ1QsRUFBRSxPQUFPLEVBQUUsYUFBYSxFQUFFLEtBQUssRUFBRSxJQUFJLEVBQUUsVUFBVSxFQUFFLG1CQUFtQixFQUFFLElBQUksRUFBRSxDQUFDLFlBQVksQ0FBQyxFQUFFO2dCQUM5RixFQUFFLE9BQU8sRUFBRSxhQUFhLEVBQUUsUUFBUSxFQUFFLE1BQU0sRUFBRSxLQUFLLEVBQUUsSUFBSSxFQUFFO2dCQUN6RCxFQUFFLE9BQU8sRUFBRSw0QkFBNEIsRUFBRSxRQUFRLEVBQUUsTUFBTSxFQUFFLEtBQUssRUFBRSxJQUFJLEVBQUU7Z0JBQ3hFLFlBQVk7Z0JBQ1osaUJBQWlCO2FBQ2xCO1NBQ0YsQ0FBQztJQUNKLENBQUM7Ozs7O0lBRU0scUJBQVE7Ozs7SUFBZixVQUFnQixNQUF5QjtRQUF6Qix1QkFBQSxFQUFBLFdBQXlCO1FBQ3ZDLE9BQU87WUFDTCxRQUFRLEVBQUUsWUFBWTtZQUN0QixTQUFTLEVBQUU7Z0JBQ1QsRUFBRSxPQUFPLEVBQUUsYUFBYSxFQUFFLFFBQVEsRUFBRSxNQUFNLEVBQUUsS0FBSyxFQUFFLElBQUksRUFBRTtnQkFDekQsRUFBRSxPQUFPLEVBQUUsNEJBQTRCLEVBQUUsUUFBUSxFQUFFLE1BQU0sRUFBRSxLQUFLLEVBQUUsSUFBSSxFQUFFO2dCQUN4RSxpQkFBaUI7YUFDbEI7U0FDRixDQUFDO0lBQ0osQ0FBQzs7Z0JBcENGLFFBQVEsU0FBQztvQkFDUixZQUFZLEVBQUU7d0JBQ1osVUFBVTt3QkFDVixXQUFXO3dCQUNYLGdCQUFnQjt3QkFDaEIsV0FBVzt3QkFDWCx1QkFBdUI7d0JBQ3ZCLGtCQUFrQjtxQkFDbkI7b0JBQ0QsZUFBZSxFQUFFLENBQUMsV0FBVyxFQUFFLGtCQUFrQixDQUFDO29CQUNsRCxPQUFPLEVBQUUsQ0FBQyxVQUFVLEVBQUUsV0FBVyxFQUFFLGdCQUFnQixFQUFFLFdBQVcsRUFBRSx1QkFBdUIsQ0FBQztvQkFDMUYsT0FBTyxFQUFFLENBQUMsWUFBWSxDQUFDO2lCQUN4Qjs7OztnQkF0Q1EsWUFBWTs0Q0FrRWhCLFFBQVEsWUFBSSxNQUFNLFNBQUMsYUFBYTs7SUFRckMsbUJBQUM7Q0FBQSxBQWhERCxJQWdEQztTQW5DWSxZQUFZIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgTmdNb2R1bGUsIE1vZHVsZVdpdGhQcm92aWRlcnMsIEFOQUxZWkVfRk9SX0VOVFJZX0NPTVBPTkVOVFMsIEluamVjdCwgT3B0aW9uYWwgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IENvbW1vbk1vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbic7XG5pbXBvcnQgeyBGb3JtbHlGb3JtIH0gZnJvbSAnLi9jb21wb25lbnRzL2Zvcm1seS5mb3JtJztcbmltcG9ydCB7IEZvcm1seUZpZWxkIH0gZnJvbSAnLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZCc7XG5pbXBvcnQgeyBGb3JtbHlBdHRyaWJ1dGVzIH0gZnJvbSAnLi9jb21wb25lbnRzL2Zvcm1seS5hdHRyaWJ1dGVzJztcbmltcG9ydCB7IEZvcm1seUNvbmZpZywgQ29uZmlnT3B0aW9uLCBGT1JNTFlfQ09ORklHIH0gZnJvbSAnLi9zZXJ2aWNlcy9mb3JtbHkuY29uZmlnJztcbmltcG9ydCB7IEZvcm1seUZvcm1CdWlsZGVyIH0gZnJvbSAnLi9zZXJ2aWNlcy9mb3JtbHkuZm9ybS5idWlsZGVyJztcbmltcG9ydCB7IEZvcm1seUdyb3VwIH0gZnJvbSAnLi90ZW1wbGF0ZXMvZm9ybWx5Lmdyb3VwJztcbmltcG9ydCB7IEZvcm1seVZhbGlkYXRpb25NZXNzYWdlIH0gZnJvbSAnLi90ZW1wbGF0ZXMvZm9ybWx5LnZhbGlkYXRpb24tbWVzc2FnZSc7XG5pbXBvcnQgeyBGb3JtbHlUZW1wbGF0ZVR5cGUgfSBmcm9tICcuL3RlbXBsYXRlcy9maWVsZC10ZW1wbGF0ZS50eXBlJztcblxuaW1wb3J0IHsgRmllbGRFeHByZXNzaW9uRXh0ZW5zaW9uIH0gZnJvbSAnLi9leHRlbnNpb25zL2ZpZWxkLWV4cHJlc3Npb24vZmllbGQtZXhwcmVzc2lvbic7XG5pbXBvcnQgeyBGaWVsZFZhbGlkYXRpb25FeHRlbnNpb24gfSBmcm9tICcuL2V4dGVuc2lvbnMvZmllbGQtdmFsaWRhdGlvbi9maWVsZC12YWxpZGF0aW9uJztcbmltcG9ydCB7IEZpZWxkRm9ybUV4dGVuc2lvbiB9IGZyb20gJy4vZXh0ZW5zaW9ucy9maWVsZC1mb3JtL2ZpZWxkLWZvcm0nO1xuaW1wb3J0IHsgQ29yZUV4dGVuc2lvbiB9IGZyb20gJy4vZXh0ZW5zaW9ucy9jb3JlL2NvcmUnO1xuXG5leHBvcnQgZnVuY3Rpb24gZGVmYXVsdEZvcm1seUNvbmZpZyhmb3JtbHlDb25maWc6IEZvcm1seUNvbmZpZyk6IENvbmZpZ09wdGlvbiB7XG4gIHJldHVybiB7XG4gICAgdHlwZXM6IFtcbiAgICAgIHsgbmFtZTogJ2Zvcm1seS1ncm91cCcsIGNvbXBvbmVudDogRm9ybWx5R3JvdXAgfSxcbiAgICAgIHsgbmFtZTogJ2Zvcm1seS10ZW1wbGF0ZScsIGNvbXBvbmVudDogRm9ybWx5VGVtcGxhdGVUeXBlIH0sXG4gICAgXSxcbiAgICBleHRlbnNpb25zOiBbXG4gICAgICB7IG5hbWU6ICdjb3JlJywgZXh0ZW5zaW9uOiBuZXcgQ29yZUV4dGVuc2lvbihmb3JtbHlDb25maWcpIH0sXG4gICAgICB7IG5hbWU6ICdmaWVsZC12YWxpZGF0aW9uJywgZXh0ZW5zaW9uOiBuZXcgRmllbGRWYWxpZGF0aW9uRXh0ZW5zaW9uKGZvcm1seUNvbmZpZykgfSxcbiAgICAgIHsgbmFtZTogJ2ZpZWxkLWZvcm0nLCBleHRlbnNpb246IG5ldyBGaWVsZEZvcm1FeHRlbnNpb24oZm9ybWx5Q29uZmlnKSB9LFxuICAgICAgeyBuYW1lOiAnZmllbGQtZXhwcmVzc2lvbicsIGV4dGVuc2lvbjogbmV3IEZpZWxkRXhwcmVzc2lvbkV4dGVuc2lvbigpIH0sXG4gICAgXSxcbiAgfTtcbn1cblxuQE5nTW9kdWxlKHtcbiAgZGVjbGFyYXRpb25zOiBbXG4gICAgRm9ybWx5Rm9ybSxcbiAgICBGb3JtbHlGaWVsZCxcbiAgICBGb3JtbHlBdHRyaWJ1dGVzLFxuICAgIEZvcm1seUdyb3VwLFxuICAgIEZvcm1seVZhbGlkYXRpb25NZXNzYWdlLFxuICAgIEZvcm1seVRlbXBsYXRlVHlwZSxcbiAgXSxcbiAgZW50cnlDb21wb25lbnRzOiBbRm9ybWx5R3JvdXAsIEZvcm1seVRlbXBsYXRlVHlwZV0sXG4gIGV4cG9ydHM6IFtGb3JtbHlGb3JtLCBGb3JtbHlGaWVsZCwgRm9ybWx5QXR0cmlidXRlcywgRm9ybWx5R3JvdXAsIEZvcm1seVZhbGlkYXRpb25NZXNzYWdlXSxcbiAgaW1wb3J0czogW0NvbW1vbk1vZHVsZV0sXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seU1vZHVsZSB7XG4gIHN0YXRpYyBmb3JSb290KGNvbmZpZzogQ29uZmlnT3B0aW9uID0ge30pOiBNb2R1bGVXaXRoUHJvdmlkZXJzPEZvcm1seU1vZHVsZT4ge1xuICAgIHJldHVybiB7XG4gICAgICBuZ01vZHVsZTogRm9ybWx5TW9kdWxlLFxuICAgICAgcHJvdmlkZXJzOiBbXG4gICAgICAgIHsgcHJvdmlkZTogRk9STUxZX0NPTkZJRywgbXVsdGk6IHRydWUsIHVzZUZhY3Rvcnk6IGRlZmF1bHRGb3JtbHlDb25maWcsIGRlcHM6IFtGb3JtbHlDb25maWddIH0sXG4gICAgICAgIHsgcHJvdmlkZTogRk9STUxZX0NPTkZJRywgdXNlVmFsdWU6IGNvbmZpZywgbXVsdGk6IHRydWUgfSxcbiAgICAgICAgeyBwcm92aWRlOiBBTkFMWVpFX0ZPUl9FTlRSWV9DT01QT05FTlRTLCB1c2VWYWx1ZTogY29uZmlnLCBtdWx0aTogdHJ1ZSB9LFxuICAgICAgICBGb3JtbHlDb25maWcsXG4gICAgICAgIEZvcm1seUZvcm1CdWlsZGVyLFxuICAgICAgXSxcbiAgICB9O1xuICB9XG5cbiAgc3RhdGljIGZvckNoaWxkKGNvbmZpZzogQ29uZmlnT3B0aW9uID0ge30pOiBNb2R1bGVXaXRoUHJvdmlkZXJzPEZvcm1seU1vZHVsZT4ge1xuICAgIHJldHVybiB7XG4gICAgICBuZ01vZHVsZTogRm9ybWx5TW9kdWxlLFxuICAgICAgcHJvdmlkZXJzOiBbXG4gICAgICAgIHsgcHJvdmlkZTogRk9STUxZX0NPTkZJRywgdXNlVmFsdWU6IGNvbmZpZywgbXVsdGk6IHRydWUgfSxcbiAgICAgICAgeyBwcm92aWRlOiBBTkFMWVpFX0ZPUl9FTlRSWV9DT01QT05FTlRTLCB1c2VWYWx1ZTogY29uZmlnLCBtdWx0aTogdHJ1ZSB9LFxuICAgICAgICBGb3JtbHlGb3JtQnVpbGRlcixcbiAgICAgIF0sXG4gICAgfTtcbiAgfVxuXG4gIGNvbnN0cnVjdG9yKFxuICAgIGNvbmZpZ1NlcnZpY2U6IEZvcm1seUNvbmZpZyxcbiAgICBAT3B0aW9uYWwoKSBASW5qZWN0KEZPUk1MWV9DT05GSUcpIGNvbmZpZ3M6IENvbmZpZ09wdGlvbltdID0gW10sXG4gICkge1xuICAgIGlmICghY29uZmlncykge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGNvbmZpZ3MuZm9yRWFjaChjb25maWcgPT4gY29uZmlnU2VydmljZS5hZGRDb25maWcoY29uZmlnKSk7XG4gIH1cbn1cbiJdfQ==