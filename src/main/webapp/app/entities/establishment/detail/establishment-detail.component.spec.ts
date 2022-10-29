import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EstablishmentDetailComponent } from './establishment-detail.component';

describe('Establishment Management Detail Component', () => {
  let comp: EstablishmentDetailComponent;
  let fixture: ComponentFixture<EstablishmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EstablishmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ establishment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EstablishmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EstablishmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load establishment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.establishment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
