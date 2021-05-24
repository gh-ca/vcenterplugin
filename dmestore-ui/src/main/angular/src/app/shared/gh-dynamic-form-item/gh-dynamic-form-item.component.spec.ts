import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GhDynamicFormItemComponent } from './gh-dynamic-form-item.component';

describe('GhDynamicFormItemComponent', () => {
  let component: GhDynamicFormItemComponent;
  let fixture: ComponentFixture<GhDynamicFormItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GhDynamicFormItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GhDynamicFormItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
