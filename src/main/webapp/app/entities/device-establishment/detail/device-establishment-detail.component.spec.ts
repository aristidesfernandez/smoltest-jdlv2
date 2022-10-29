import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeviceEstablishmentDetailComponent } from './device-establishment-detail.component';

describe('DeviceEstablishment Management Detail Component', () => {
  let comp: DeviceEstablishmentDetailComponent;
  let fixture: ComponentFixture<DeviceEstablishmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceEstablishmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deviceEstablishment: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(DeviceEstablishmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeviceEstablishmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deviceEstablishment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deviceEstablishment).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
