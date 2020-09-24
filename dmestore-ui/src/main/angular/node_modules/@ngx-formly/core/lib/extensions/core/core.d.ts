import { FormlyExtension, FormlyConfig } from '../../services/formly.config';
import { FormlyFieldConfigCache } from '../../components/formly.field.config';
/** @experimental */
export declare class CoreExtension implements FormlyExtension {
    private formlyConfig;
    private formId;
    constructor(formlyConfig: FormlyConfig);
    prePopulate(field: FormlyFieldConfigCache): void;
    onPopulate(field: FormlyFieldConfigCache): void;
    postPopulate(field: FormlyFieldConfigCache): void;
    private initFieldOptions;
    private initFieldWrappers;
    private getFieldComponentInstance;
}
