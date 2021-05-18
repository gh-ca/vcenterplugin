import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevDemoComponentComponent } from './dev-demo-component.component';

describe('DevDemoComponentComponent', () => {
  let component: DevDemoComponentComponent;
  let fixture: ComponentFixture<DevDemoComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevDemoComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevDemoComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
