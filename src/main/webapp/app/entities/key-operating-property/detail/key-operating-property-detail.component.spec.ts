import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KeyOperatingPropertyDetailComponent } from './key-operating-property-detail.component';

describe('KeyOperatingProperty Management Detail Component', () => {
  let comp: KeyOperatingPropertyDetailComponent;
  let fixture: ComponentFixture<KeyOperatingPropertyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KeyOperatingPropertyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ keyOperatingProperty: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KeyOperatingPropertyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KeyOperatingPropertyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load keyOperatingProperty on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.keyOperatingProperty).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
