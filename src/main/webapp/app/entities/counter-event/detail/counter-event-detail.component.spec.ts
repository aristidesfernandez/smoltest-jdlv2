import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CounterEventDetailComponent } from './counter-event-detail.component';

describe('CounterEvent Management Detail Component', () => {
  let comp: CounterEventDetailComponent;
  let fixture: ComponentFixture<CounterEventDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CounterEventDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ counterEvent: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(CounterEventDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CounterEventDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load counterEvent on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.counterEvent).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
