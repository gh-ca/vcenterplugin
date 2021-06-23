import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorTipsComponent } from './error-tips.component';

describe('ErrorTipsComponent', () => {
  let component: ErrorTipsComponent;
  let fixture: ComponentFixture<ErrorTipsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ErrorTipsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorTipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
