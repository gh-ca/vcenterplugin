import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceLevelTableComponent } from './service-level-table.component';

describe('ServiceLevelTableComponent', () => {
  let component: ServiceLevelTableComponent;
  let fixture: ComponentFixture<ServiceLevelTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServiceLevelTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServiceLevelTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
