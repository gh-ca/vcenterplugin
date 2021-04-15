import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VmfsCreateFaildTipsComponent } from './vmfs-create-faild-tips.component';

describe('VmfsCreateFaildTipsComponent', () => {
  let component: VmfsCreateFaildTipsComponent;
  let fixture: ComponentFixture<VmfsCreateFaildTipsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VmfsCreateFaildTipsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VmfsCreateFaildTipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
