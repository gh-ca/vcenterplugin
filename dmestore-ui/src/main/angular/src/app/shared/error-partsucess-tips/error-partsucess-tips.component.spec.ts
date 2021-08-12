import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorPartsucessTipsComponent } from './error-partsucess-tips.component';

describe('ErrorPartsucessTipsComponent', () => {
  let component: ErrorPartsucessTipsComponent;
  let fixture: ComponentFixture<ErrorPartsucessTipsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ErrorPartsucessTipsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorPartsucessTipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
