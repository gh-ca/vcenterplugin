import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GhTreeCheckboxComponent } from './gh-tree-checkbox.component';

describe('GhTreeCheckboxComponent', () => {
  let component: GhTreeCheckboxComponent;
  let fixture: ComponentFixture<GhTreeCheckboxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GhTreeCheckboxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GhTreeCheckboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
