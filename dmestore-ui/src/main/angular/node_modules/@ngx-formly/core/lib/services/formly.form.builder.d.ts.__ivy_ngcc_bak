import { ComponentFactoryResolver, Injector } from '@angular/core';
import { FormGroup, FormArray } from '@angular/forms';
import { FormlyConfig } from './formly.config';
import { FormlyFieldConfig, FormlyFormOptions } from '../components/formly.field.config';
export declare class FormlyFormBuilder {
    private formlyConfig;
    private componentFactoryResolver;
    private injector;
    constructor(formlyConfig: FormlyConfig, componentFactoryResolver: ComponentFactoryResolver, injector: Injector);
    buildForm(formControl: FormGroup | FormArray, fieldGroup: FormlyFieldConfig[], model: any, options: FormlyFormOptions): void;
    private _buildForm;
    private getExtensions;
    private _setOptions;
}
