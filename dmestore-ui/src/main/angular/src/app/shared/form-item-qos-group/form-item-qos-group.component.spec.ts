import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormItemQosGroupComponent } from './form-item-qos-group.component';

describe('FormItemQosGroupComponent', () => {
  let component: FormItemQosGroupComponent;
  let fixture: ComponentFixture<FormItemQosGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormItemQosGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormItemQosGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
