import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GhDynamicFormComponent } from './gh-dynamic-form.component';

describe('GhDynamicFormComponent', () => {
  let component: GhDynamicFormComponent;
  let fixture: ComponentFixture<GhDynamicFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GhDynamicFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GhDynamicFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
