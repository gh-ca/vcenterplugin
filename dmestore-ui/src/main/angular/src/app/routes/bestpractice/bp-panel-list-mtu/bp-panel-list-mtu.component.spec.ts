import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpPanelListMtuComponent } from './bp-panel-list-mtu.component';

describe('BpPanelListMtuComponent', () => {
  let component: BpPanelListMtuComponent;
  let fixture: ComponentFixture<BpPanelListMtuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpPanelListMtuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpPanelListMtuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
