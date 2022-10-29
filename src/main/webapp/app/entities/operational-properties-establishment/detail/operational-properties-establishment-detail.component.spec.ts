import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperationalPropertiesEstablishmentDetailComponent } from './operational-properties-establishment-detail.component';

describe('OperationalPropertiesEstablishment Management Detail Component', () => {
  let comp: OperationalPropertiesEstablishmentDetailComponent;
  let fixture: ComponentFixture<OperationalPropertiesEstablishmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperationalPropertiesEstablishmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operationalPropertiesEstablishment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperationalPropertiesEstablishmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperationalPropertiesEstablishmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operationalPropertiesEstablishment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operationalPropertiesEstablishment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
