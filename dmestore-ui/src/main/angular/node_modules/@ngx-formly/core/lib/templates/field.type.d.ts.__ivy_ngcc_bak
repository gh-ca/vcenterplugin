import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '../components/formly.field.config';
export declare abstract class FieldType<F extends FormlyFieldConfig = FormlyFieldConfig> {
    field: F;
    defaultOptions?: F;
    model: any;
    form: FormGroup;
    options: F['options'];
    readonly key: string | number | string[];
    readonly formControl: import("@angular/forms").AbstractControl;
    readonly to: import("../components/formly.field.config").FormlyTemplateOptions;
    readonly showError: boolean;
    readonly id: string;
    readonly formState: any;
}
/**
 * @deprecated use `FieldType` instead
 */
export declare abstract class Field extends FieldType {
    constructor();
}
