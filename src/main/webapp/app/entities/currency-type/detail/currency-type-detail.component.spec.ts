import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CurrencyTypeDetailComponent } from './currency-type-detail.component';

describe('CurrencyType Management Detail Component', () => {
  let comp: CurrencyTypeDetailComponent;
  let fixture: ComponentFixture<CurrencyTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CurrencyTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ currencyType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CurrencyTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CurrencyTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load currencyType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.currencyType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
