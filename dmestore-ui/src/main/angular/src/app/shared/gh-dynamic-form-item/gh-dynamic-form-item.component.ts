import { ViewChild } from '@angular/core';
import { ComponentFactoryResolver } from '@angular/core';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { DynamicComponentDirectiveDirective } from '@shared/directives/dynamic-component-directive.directive';
import { helper } from 'app/app.helpers';
/* 
import { ClrSelectContainer } from '@clr/angular/esm2015/forms/select/select-container';
import { ClrInputContainer } from '@clr/angular/esm2015/forms/input/input-container';
 */
@Component({
  selector: 'app-gh-dynamic-form-item',
  templateUrl: './gh-dynamic-form-item.component.html',
  styleUrls: ['./gh-dynamic-form-item.component.scss'],
  providers: [TranslatePipe],
})
export class GhDynamicFormItemComponent implements OnInit {
  @Input() configs: GhFormItemBase<string>;
  @Input() form: FormGroup;
  @Output() onItemValueChange: EventEmitter<any>;

  @ViewChild(DynamicComponentDirectiveDirective, { static: true })
  dynamicContainer: DynamicComponentDirectiveDirective;

  GhFormItemType = GhFormItemType;
  helper = helper;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private translatePipe: TranslatePipe
  ) {
    this.onItemValueChange = new EventEmitter<any>();
  }

  get isValid() {
    return this.form?.controls[this.configs.prop].valid;
  }

  get errors() {
    return this.form?.controls[this.configs.prop].errors;
  }

  get tips() {
    if (this.configs.validTips_i18n) {
      return this.translatePipe.transform.apply(this.translatePipe, this.configs.validTips_i18n);
    }
    if (this.configs.validTips) {
      return this.configs.validTips;
    }
    return '';
  }

  /* 
  loadComponent() {
    const containerMap = {
      [GhFormItemType.select]: ClrSelectContainer,
      [GhFormItemType.input]: ClrInputContainer,
    };
    const component = containerMap[this.configs.controlType];
    if (!component) return false;
    debugger;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(component);

    const viewContainerRef = this.dynamicContainer.viewContainerRef;
    viewContainerRef.clear();
    const componentRef = viewContainerRef.createComponent<any>(componentFactory);
  }
  */
  
  ngOnInit(): void {
    /* this.loadComponent(); */
    this.form?.controls[this.configs.prop].valueChanges.subscribe(value => {
      this.onItemValueChange.emit({
        prop: this.configs.prop,
        value,
      });
    });
  }
}

export enum GhFormItemType {
  empty = '',
  input = 'input',
  select = 'select',
}

export class GhFormItemBase<T> {
  value: T;
  prop: string;
  label: string;
  required: boolean;
  order: number;
  controlType: string;
  type: string;
  options: { label: string; value: string }[];
  validTips: string;
  validTips_i18n: any[];

  constructor(
    options: {
      value?: T;
      prop?: string;
      label?: string;
      required?: boolean;
      order?: number;
      controlType?: GhFormItemType;
      type?: string;
      options?: { label: string; value: string }[];
      validTips?: string;
      validTips_i18n?: any[];
    } = {}
  ) {
    this.value = options.value;
    this.prop = options.prop || '';
    this.label = options.label || '';
    this.required = !!options.required;
    this.order = options.order === undefined ? 1 : options.order;
    this.controlType = options.controlType || GhFormItemType.empty;
    this.type = options.type || '';
    this.options = options.options || [];
    /*  */
    this.validTips = options.validTips || '';
    this.validTips_i18n = options.validTips_i18n || [];
  }
}

export class GhTextbox extends GhFormItemBase<string> {
  controlType = GhFormItemType.input;
}

export class GhDropdown extends GhFormItemBase<string> {
  controlType = GhFormItemType.select;
}
