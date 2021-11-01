import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpRepairHistoryTableComponent } from './bp-repair-history-table.component';

describe('BpRepairHistoryTableComponent', () => {
  let component: BpRepairHistoryTableComponent;
  let fixture: ComponentFixture<BpRepairHistoryTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpRepairHistoryTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpRepairHistoryTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
