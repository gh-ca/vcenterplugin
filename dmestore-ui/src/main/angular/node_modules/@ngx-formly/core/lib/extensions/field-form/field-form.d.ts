import { FormlyExtension, FormlyConfig } from '../../services/formly.config';
import { FormlyFieldConfigCache } from '../../components/formly.field.config';
/** @experimental */
export declare class FieldFormExtension implements FormlyExtension {
    private config;
    constructor(config: FormlyConfig);
    onPopulate(field: FormlyFieldConfigCache): void;
    postPopulate(field: FormlyFieldConfigCache): void;
    private addFormControl;
    private setValidators;
    private mergeValidators;
}
