import { AfterViewInit, TemplateRef } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatDatepickerInput } from '@angular/material/datepicker';
export declare class FormlyDatepickerTypeComponent extends FieldType implements AfterViewInit {
    formFieldControl: MatInput;
    datepickerInput: MatDatepickerInput<any>;
    datepickerToggle: TemplateRef<any>;
    defaultOptions: {
        templateOptions: {
            datepickerOptions: {
                startView: string;
                datepickerTogglePosition: string;
                dateInput: () => void;
                dateChange: () => void;
                monthSelected: () => void;
                yearSelected: () => void;
            };
        };
    };
    ngAfterViewInit(): void;
}
