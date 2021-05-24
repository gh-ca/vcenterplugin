import { ViewContainerRef } from '@angular/core';
import { Directive } from '@angular/core';

@Directive({
  selector: '[appDynamicComponentDirective]',
})
export class DynamicComponentDirectiveDirective {
  constructor(public viewContainerRef: ViewContainerRef) {}
}
