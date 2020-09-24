import { OnInit, OnDestroy, Renderer2, AfterViewInit, AfterContentChecked, TemplateRef, ElementRef, ViewContainerRef } from '@angular/core';
import { FieldWrapper, FormlyFieldConfig, FormlyConfig } from '@ngx-formly/core';
import { MatFormField } from '@angular/material/form-field';
import { MatFormFieldControl } from '@angular/material/form-field';
import { Subject } from 'rxjs';
import { FocusMonitor } from '@angular/cdk/a11y';
interface MatFormlyFieldConfig extends FormlyFieldConfig {
    _matprefix: TemplateRef<any>;
    _matsuffix: TemplateRef<any>;
    __formField__: FormlyWrapperFormField;
}
export declare class FormlyWrapperFormField extends FieldWrapper<MatFormlyFieldConfig> implements OnInit, OnDestroy, MatFormFieldControl<any>, AfterViewInit, AfterContentChecked {
    private config;
    private renderer;
    private elementRef;
    private focusMonitor;
    fieldComponent: ViewContainerRef;
    formField: MatFormField;
    stateChanges: Subject<void>;
    _errorState: boolean;
    private initialGapCalculated;
    constructor(config: FormlyConfig, renderer: Renderer2, elementRef: ElementRef, focusMonitor: FocusMonitor);
    ngOnInit(): void;
    ngAfterContentChecked(): void;
    ngAfterViewInit(): void;
    ngOnDestroy(): void;
    setDescribedByIds(ids: string[]): void;
    onContainerClick(event: MouseEvent): void;
    readonly errorState: boolean;
    readonly controlType: string | undefined;
    readonly focused: boolean;
    readonly disabled: boolean;
    readonly required: boolean;
    readonly placeholder: string;
    readonly shouldPlaceholderFloat: boolean;
    readonly value: any;
    readonly ngControl: any;
    readonly empty: boolean;
    readonly shouldLabelFloat: boolean;
    readonly formlyField: MatFormlyFieldConfig;
}
export {};
