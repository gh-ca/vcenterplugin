import { FormlyFieldConfigCache } from '../../components/formly.field.config';
import { FormlyExtension } from '../../services/formly.config';
/** @experimental */
export declare class FieldExpressionExtension implements FormlyExtension {
    prePopulate(field: FormlyFieldConfigCache): void;
    onPopulate(field: FormlyFieldConfigCache): void;
    private _evalExpression;
    private checkField;
    private _checkField;
    private checkFieldExpressionChange;
    private checkFieldVisibilityChange;
    private setDisabledState;
    private toggleFormControl;
    private setExprValue;
    private emitExpressionChanges;
}
