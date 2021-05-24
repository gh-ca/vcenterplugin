import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageStatusCellComponent } from './storage-status-cell.component';

describe('StorageStatusCellComponent', () => {
  let component: StorageStatusCellComponent;
  let fixture: ComponentFixture<StorageStatusCellComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StorageStatusCellComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageStatusCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
