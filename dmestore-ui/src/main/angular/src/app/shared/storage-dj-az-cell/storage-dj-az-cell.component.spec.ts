import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageDjAzCellComponent } from './storage-dj-az-cell.component';

describe('StorageDjAzCellComponent', () => {
  let component: StorageDjAzCellComponent;
  let fixture: ComponentFixture<StorageDjAzCellComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StorageDjAzCellComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageDjAzCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
