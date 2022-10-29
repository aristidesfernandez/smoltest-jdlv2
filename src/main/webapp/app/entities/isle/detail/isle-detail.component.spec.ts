import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsleDetailComponent } from './isle-detail.component';

describe('Isle Management Detail Component', () => {
  let comp: IsleDetailComponent;
  let fixture: ComponentFixture<IsleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IsleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ isle: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IsleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IsleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load isle on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.isle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
