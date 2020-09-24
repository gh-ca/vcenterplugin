/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { FormlyToggleTypeComponent } from './toggle.type';
export class FormlyMatToggleModule {
}
FormlyMatToggleModule.decorators = [
    { type: NgModule, args: [{
                declarations: [FormlyToggleTypeComponent],
                imports: [
                    CommonModule,
                    ReactiveFormsModule,
                    MatSlideToggleModule,
                    FormlyMatFormFieldModule,
                    FormlyModule.forChild({
                        types: [{
                                name: 'toggle',
                                component: FormlyToggleTypeComponent,
                                wrappers: ['form-field'],
                            }],
                    }),
                ],
            },] }
];
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidG9nZ2xlLm1vZHVsZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL0BuZ3gtZm9ybWx5L21hdGVyaWFsL3RvZ2dsZS8iLCJzb3VyY2VzIjpbInRvZ2dsZS5tb2R1bGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDekMsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLGlCQUFpQixDQUFDO0FBQy9DLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxrQkFBa0IsQ0FBQztBQUNoRCxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUNyRCxPQUFPLEVBQUUsb0JBQW9CLEVBQUUsTUFBTSxnQ0FBZ0MsQ0FBQztBQUN0RSxPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUUzRSxPQUFPLEVBQUUseUJBQXlCLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFrQjFELE1BQU0sT0FBTyxxQkFBcUI7OztZQWhCakMsUUFBUSxTQUFDO2dCQUNSLFlBQVksRUFBRSxDQUFDLHlCQUF5QixDQUFDO2dCQUN6QyxPQUFPLEVBQUU7b0JBQ1AsWUFBWTtvQkFDWixtQkFBbUI7b0JBQ25CLG9CQUFvQjtvQkFDcEIsd0JBQXdCO29CQUN4QixZQUFZLENBQUMsUUFBUSxDQUFDO3dCQUNwQixLQUFLLEVBQUUsQ0FBQztnQ0FDTixJQUFJLEVBQUUsUUFBUTtnQ0FDZCxTQUFTLEVBQUUseUJBQXlCO2dDQUNwQyxRQUFRLEVBQUUsQ0FBQyxZQUFZLENBQUM7NkJBQ3pCLENBQUM7cUJBQ0gsQ0FBQztpQkFDSDthQUNGIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgTmdNb2R1bGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IENvbW1vbk1vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbic7XG5pbXBvcnQgeyBGb3JtbHlNb2R1bGUgfSBmcm9tICdAbmd4LWZvcm1seS9jb3JlJztcbmltcG9ydCB7IFJlYWN0aXZlRm9ybXNNb2R1bGUgfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBNYXRTbGlkZVRvZ2dsZU1vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL3NsaWRlLXRvZ2dsZSc7XG5pbXBvcnQgeyBGb3JtbHlNYXRGb3JtRmllbGRNb2R1bGUgfSBmcm9tICdAbmd4LWZvcm1seS9tYXRlcmlhbC9mb3JtLWZpZWxkJztcblxuaW1wb3J0IHsgRm9ybWx5VG9nZ2xlVHlwZUNvbXBvbmVudCB9IGZyb20gJy4vdG9nZ2xlLnR5cGUnO1xuXG5ATmdNb2R1bGUoe1xuICBkZWNsYXJhdGlvbnM6IFtGb3JtbHlUb2dnbGVUeXBlQ29tcG9uZW50XSxcbiAgaW1wb3J0czogW1xuICAgIENvbW1vbk1vZHVsZSxcbiAgICBSZWFjdGl2ZUZvcm1zTW9kdWxlLFxuICAgIE1hdFNsaWRlVG9nZ2xlTW9kdWxlLFxuICAgIEZvcm1seU1hdEZvcm1GaWVsZE1vZHVsZSxcbiAgICBGb3JtbHlNb2R1bGUuZm9yQ2hpbGQoe1xuICAgICAgdHlwZXM6IFt7XG4gICAgICAgIG5hbWU6ICd0b2dnbGUnLFxuICAgICAgICBjb21wb25lbnQ6IEZvcm1seVRvZ2dsZVR5cGVDb21wb25lbnQsXG4gICAgICAgIHdyYXBwZXJzOiBbJ2Zvcm0tZmllbGQnXSxcbiAgICAgIH1dLFxuICAgIH0pLFxuICBdLFxufSlcbmV4cG9ydCBjbGFzcyBGb3JtbHlNYXRUb2dnbGVNb2R1bGUgeyB9XG4iXX0=