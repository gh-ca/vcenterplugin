import { FormlyFieldConfig } from './core';
import { FormlyFieldConfigCache } from './components/formly.field.config';
export declare function getFieldId(formId: string, field: FormlyFieldConfig, index: string | number): string;
export declare function getKeyPath(field: FormlyFieldConfigCache): string[];
export declare const FORMLY_VALIDATORS: string[];
export declare function assignFieldValue(field: FormlyFieldConfigCache, value: any): void;
export declare function assignModelValue(model: any, paths: string[], value: any): void;
export declare function getFieldInitialValue(field: FormlyFieldConfig): any;
export declare function getFieldValue(field: FormlyFieldConfig): any;
export declare function reverseDeepMerge(dest: any, ...args: any[]): any;
export declare function isNullOrUndefined(value: any): boolean;
export declare function isUndefined(value: any): boolean;
export declare function isBlankString(value: any): boolean;
export declare function isFunction(value: any): boolean;
export declare function objAndSameType(obj1: any, obj2: any): boolean;
export declare function isObject(x: any): boolean;
export declare function isPromise(obj: any): obj is Promise<any>;
export declare function clone(value: any): any;
export declare function defineHiddenProp(field: any, prop: string, defaultValue: any): void;
export declare function wrapProperty<T = any>(o: any, prop: string, setFn: (change: {
    currentValue: T;
    previousValue?: T;
    firstChange: boolean;
}) => void): () => ((change: {
    currentValue: T;
    previousValue?: T;
    firstChange: boolean;
}) => void)[];
export declare function reduceFormUpdateValidityCalls(form: any, action: Function): void;
