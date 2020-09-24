import { ComponentFactoryResolver, Injector } from '@angular/core';
import { FormGroup, FormArray, FormGroupDirective } from '@angular/forms';
import { FormlyConfig } from './formly.config';
import { FormlyFieldConfig, FormlyFormOptions } from '../models';
export declare class FormlyFormBuilder {
    private config;
    private resolver;
    private injector;
    private parentForm;
    constructor(config: FormlyConfig, resolver: ComponentFactoryResolver, injector: Injector, parentForm: FormGroupDirective);
    buildForm(form: FormGroup | FormArray, fieldGroup: FormlyFieldConfig[], model: any, options: FormlyFormOptions): void;
    build(field: FormlyFieldConfig): void;
    private _build;
    private getExtensions;
    private _setOptions;
}
