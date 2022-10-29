import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CounterTypeDetailComponent } from './counter-type-detail.component';

describe('CounterType Management Detail Component', () => {
  let comp: CounterTypeDetailComponent;
  let fixture: ComponentFixture<CounterTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CounterTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ counterType: { counterCode: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CounterTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CounterTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load counterType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.counterType).toEqual(expect.objectContaining({ counterCode: 'ABC' }));
    });
  });
});
