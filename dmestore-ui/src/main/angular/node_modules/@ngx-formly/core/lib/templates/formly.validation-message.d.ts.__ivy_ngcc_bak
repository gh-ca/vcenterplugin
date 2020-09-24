import { OnChanges } from '@angular/core';
import { FormlyConfig } from '../services/formly.config';
import { FormlyFieldConfig } from '../components/formly.field.config';
import { Observable } from 'rxjs';
export declare class FormlyValidationMessage implements OnChanges {
    private formlyConfig;
    field: FormlyFieldConfig;
    errorMessage$: Observable<string>;
    constructor(formlyConfig: FormlyConfig);
    ngOnChanges(): void;
    readonly errorMessage: string | Observable<string>;
}
