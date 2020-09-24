import { Renderer2, AfterViewChecked } from '@angular/core';
import { FieldType } from '@ngx-formly/material/form-field';
import { MatCheckbox } from '@angular/material/checkbox';
export declare class FormlyFieldCheckbox extends FieldType implements AfterViewChecked {
    private renderer;
    checkbox: MatCheckbox;
    defaultOptions: {
        templateOptions: {
            hideFieldUnderline: boolean;
            indeterminate: boolean;
            floatLabel: string;
            hideLabel: boolean;
            align: string;
            color: string;
        };
    };
    private _required;
    constructor(renderer: Renderer2);
    onContainerClick(event: MouseEvent): void;
    ngAfterViewChecked(): void;
}
