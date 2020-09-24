import { FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { FieldType } from '@ngx-formly/core';
export declare class FormlyErrorStateMatcher implements ErrorStateMatcher {
    private field;
    constructor(field: FieldType);
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean;
}
