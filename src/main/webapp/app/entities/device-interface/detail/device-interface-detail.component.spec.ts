import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeviceInterfaceDetailComponent } from './device-interface-detail.component';

describe('DeviceInterface Management Detail Component', () => {
  let comp: DeviceInterfaceDetailComponent;
  let fixture: ComponentFixture<DeviceInterfaceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceInterfaceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deviceInterface: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeviceInterfaceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeviceInterfaceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deviceInterface on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deviceInterface).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
