import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MunicipalityDetailComponent } from './municipality-detail.component';

describe('Municipality Management Detail Component', () => {
  let comp: MunicipalityDetailComponent;
  let fixture: ComponentFixture<MunicipalityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MunicipalityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ municipality: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MunicipalityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MunicipalityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load municipality on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.municipality).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
