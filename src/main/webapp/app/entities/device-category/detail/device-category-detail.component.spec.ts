import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeviceCategoryDetailComponent } from './device-category-detail.component';

describe('DeviceCategory Management Detail Component', () => {
  let comp: DeviceCategoryDetailComponent;
  let fixture: ComponentFixture<DeviceCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deviceCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeviceCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeviceCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deviceCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deviceCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
