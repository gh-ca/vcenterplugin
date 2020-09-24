/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormlyModule } from '@ngx-formly/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyMatFormFieldModule } from '@ngx-formly/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormlyFieldCheckbox } from './checkbox.type';
var FormlyMatCheckboxModule = /** @class */ (function () {
    function FormlyMatCheckboxModule() {
    }
    FormlyMatCheckboxModule.decorators = [
        { type: NgModule, args: [{
                    declarations: [FormlyFieldCheckbox],
                    imports: [
                        CommonModule,
                        ReactiveFormsModule,
                        MatCheckboxModule,
                        FormlyMatFormFieldModule,
                        FormlyModule.forChild({
                            types: [
                                {
                                    name: 'checkbox',
                                    component: FormlyFieldCheckbox,
                                    wrappers: ['form-field'],
                                },
                            ],
                        }),
                    ],
                },] }
    ];
    return FormlyMatCheckboxModule;
}());
export { FormlyMatCheckboxModule };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hlY2tib3gubW9kdWxlLmpzIiwic291cmNlUm9vdCI6Im5nOi8vQG5neC1mb3JtbHkvbWF0ZXJpYWwvY2hlY2tib3gvIiwic291cmNlcyI6WyJjaGVja2JveC5tb2R1bGUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDekMsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLGlCQUFpQixDQUFDO0FBQy9DLE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxrQkFBa0IsQ0FBQztBQUNoRCxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUVyRCxPQUFPLEVBQUUsd0JBQXdCLEVBQUUsTUFBTSxpQ0FBaUMsQ0FBQztBQUMzRSxPQUFPLEVBQUUsaUJBQWlCLEVBQUUsTUFBTSw0QkFBNEIsQ0FBQztBQUUvRCxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxpQkFBaUIsQ0FBQztBQUV0RDtJQUFBO0lBb0J1QyxDQUFDOztnQkFwQnZDLFFBQVEsU0FBQztvQkFDUixZQUFZLEVBQUUsQ0FBQyxtQkFBbUIsQ0FBQztvQkFDbkMsT0FBTyxFQUFFO3dCQUNQLFlBQVk7d0JBQ1osbUJBQW1CO3dCQUVuQixpQkFBaUI7d0JBRWpCLHdCQUF3Qjt3QkFDeEIsWUFBWSxDQUFDLFFBQVEsQ0FBQzs0QkFDcEIsS0FBSyxFQUFFO2dDQUNMO29DQUNFLElBQUksRUFBRSxVQUFVO29DQUNoQixTQUFTLEVBQUUsbUJBQW1CO29DQUM5QixRQUFRLEVBQUUsQ0FBQyxZQUFZLENBQUM7aUNBQ3pCOzZCQUNGO3lCQUNGLENBQUM7cUJBQ0g7aUJBQ0Y7O0lBQ3NDLDhCQUFDO0NBQUEsQUFwQnhDLElBb0J3QztTQUEzQix1QkFBdUIiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBOZ01vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgQ29tbW9uTW9kdWxlIH0gZnJvbSAnQGFuZ3VsYXIvY29tbW9uJztcbmltcG9ydCB7IEZvcm1seU1vZHVsZSB9IGZyb20gJ0BuZ3gtZm9ybWx5L2NvcmUnO1xuaW1wb3J0IHsgUmVhY3RpdmVGb3Jtc01vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2Zvcm1zJztcblxuaW1wb3J0IHsgRm9ybWx5TWF0Rm9ybUZpZWxkTW9kdWxlIH0gZnJvbSAnQG5neC1mb3JtbHkvbWF0ZXJpYWwvZm9ybS1maWVsZCc7XG5pbXBvcnQgeyBNYXRDaGVja2JveE1vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL21hdGVyaWFsL2NoZWNrYm94JztcblxuaW1wb3J0IHsgRm9ybWx5RmllbGRDaGVja2JveCB9IGZyb20gJy4vY2hlY2tib3gudHlwZSc7XG5cbkBOZ01vZHVsZSh7XG4gIGRlY2xhcmF0aW9uczogW0Zvcm1seUZpZWxkQ2hlY2tib3hdLFxuICBpbXBvcnRzOiBbXG4gICAgQ29tbW9uTW9kdWxlLFxuICAgIFJlYWN0aXZlRm9ybXNNb2R1bGUsXG5cbiAgICBNYXRDaGVja2JveE1vZHVsZSxcblxuICAgIEZvcm1seU1hdEZvcm1GaWVsZE1vZHVsZSxcbiAgICBGb3JtbHlNb2R1bGUuZm9yQ2hpbGQoe1xuICAgICAgdHlwZXM6IFtcbiAgICAgICAge1xuICAgICAgICAgIG5hbWU6ICdjaGVja2JveCcsXG4gICAgICAgICAgY29tcG9uZW50OiBGb3JtbHlGaWVsZENoZWNrYm94LFxuICAgICAgICAgIHdyYXBwZXJzOiBbJ2Zvcm0tZmllbGQnXSxcbiAgICAgICAgfSxcbiAgICAgIF0sXG4gICAgfSksXG4gIF0sXG59KVxuZXhwb3J0IGNsYXNzIEZvcm1seU1hdENoZWNrYm94TW9kdWxlIHsgfVxuIl19