import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpCellMulRowComponent } from './bp-cell-mul-row.component';

describe('BpCellMulRowComponent', () => {
  let component: BpCellMulRowComponent;
  let fixture: ComponentFixture<BpCellMulRowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpCellMulRowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpCellMulRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
