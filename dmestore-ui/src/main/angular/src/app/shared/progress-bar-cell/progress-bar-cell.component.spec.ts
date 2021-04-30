import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressBarCellComponent } from './progress-bar-cell.component';

describe('ProgressBarCellComponent', () => {
  let component: ProgressBarCellComponent;
  let fixture: ComponentFixture<ProgressBarCellComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgressBarCellComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgressBarCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
