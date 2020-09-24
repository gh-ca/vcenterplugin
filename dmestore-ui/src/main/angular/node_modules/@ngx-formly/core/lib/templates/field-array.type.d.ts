import { FormArray } from '@angular/forms';
import { FieldType } from './field.type';
import { FormlyFormBuilder } from '../services/formly.form.builder';
import { FormlyFieldConfig } from '../components/formly.field.config';
import { FormlyExtension } from '../services/formly.config';
export declare abstract class FieldArrayType<F extends FormlyFieldConfig = FormlyFieldConfig> extends FieldType<F> implements FormlyExtension {
    formControl: FormArray;
    defaultOptions: any;
    constructor(builder?: FormlyFormBuilder);
    onPopulate(field: FormlyFieldConfig): void;
    add(i?: number, initialModel?: any, { markAsDirty }?: {
        markAsDirty: boolean;
    }): void;
    remove(i: number, { markAsDirty }?: {
        markAsDirty: boolean;
    }): void;
}
