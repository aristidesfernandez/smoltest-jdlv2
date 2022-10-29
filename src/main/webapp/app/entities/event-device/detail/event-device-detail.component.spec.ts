import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventDeviceDetailComponent } from './event-device-detail.component';

describe('EventDevice Management Detail Component', () => {
  let comp: EventDeviceDetailComponent;
  let fixture: ComponentFixture<EventDeviceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventDeviceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventDevice: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(EventDeviceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventDeviceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventDevice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventDevice).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
