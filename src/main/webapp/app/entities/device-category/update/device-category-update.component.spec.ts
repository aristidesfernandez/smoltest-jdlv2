import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeviceCategoryFormService } from './device-category-form.service';
import { DeviceCategoryService } from '../service/device-category.service';
import { IDeviceCategory } from '../device-category.model';

import { DeviceCategoryUpdateComponent } from './device-category-update.component';

describe('DeviceCategory Management Update Component', () => {
  let comp: DeviceCategoryUpdateComponent;
  let fixture: ComponentFixture<DeviceCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceCategoryFormService: DeviceCategoryFormService;
  let deviceCategoryService: DeviceCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeviceCategoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DeviceCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceCategoryFormService = TestBed.inject(DeviceCategoryFormService);
    deviceCategoryService = TestBed.inject(DeviceCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deviceCategory: IDeviceCategory = { id: 456 };

      activatedRoute.data = of({ deviceCategory });
      comp.ngOnInit();

      expect(comp.deviceCategory).toEqual(deviceCategory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceCategory>>();
      const deviceCategory = { id: 123 };
      jest.spyOn(deviceCategoryFormService, 'getDeviceCategory').mockReturnValue(deviceCategory);
      jest.spyOn(deviceCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceCategory }));
      saveSubject.complete();

      // THEN
      expect(deviceCategoryFormService.getDeviceCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(deviceCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceCategory>>();
      const deviceCategory = { id: 123 };
      jest.spyOn(deviceCategoryFormService, 'getDeviceCategory').mockReturnValue({ id: null });
      jest.spyOn(deviceCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceCategory }));
      saveSubject.complete();

      // THEN
      expect(deviceCategoryFormService.getDeviceCategory).toHaveBeenCalled();
      expect(deviceCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceCategory>>();
      const deviceCategory = { id: 123 };
      jest.spyOn(deviceCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
