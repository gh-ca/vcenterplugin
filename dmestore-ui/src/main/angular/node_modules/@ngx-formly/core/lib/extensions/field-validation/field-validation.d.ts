import { FormlyExtension, FormlyConfig } from '../../services/formly.config';
import { FormlyFieldConfigCache } from '../../components/formly.field.config';
/** @experimental */
export declare class FieldValidationExtension implements FormlyExtension {
    private formlyConfig;
    constructor(formlyConfig: FormlyConfig);
    onPopulate(field: FormlyFieldConfigCache): void;
    private initFieldValidation;
    private getPredefinedFieldValidation;
    private wrapNgValidatorFn;
    private handleAsyncResult;
    private handleResult;
}
