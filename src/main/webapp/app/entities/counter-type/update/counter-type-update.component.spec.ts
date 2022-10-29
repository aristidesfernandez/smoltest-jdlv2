import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CounterTypeFormService } from './counter-type-form.service';
import { CounterTypeService } from '../service/counter-type.service';
import { ICounterType } from '../counter-type.model';

import { CounterTypeUpdateComponent } from './counter-type-update.component';

describe('CounterType Management Update Component', () => {
  let comp: CounterTypeUpdateComponent;
  let fixture: ComponentFixture<CounterTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counterTypeFormService: CounterTypeFormService;
  let counterTypeService: CounterTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CounterTypeUpdateComponent],
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
      .overrideTemplate(CounterTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counterTypeFormService = TestBed.inject(CounterTypeFormService);
    counterTypeService = TestBed.inject(CounterTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const counterType: ICounterType = { counterCode: 'CBA' };

      activatedRoute.data = of({ counterType });
      comp.ngOnInit();

      expect(comp.counterType).toEqual(counterType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterType>>();
      const counterType = { counterCode: 'ABC' };
      jest.spyOn(counterTypeFormService, 'getCounterType').mockReturnValue(counterType);
      jest.spyOn(counterTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterType }));
      saveSubject.complete();

      // THEN
      expect(counterTypeFormService.getCounterType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(counterTypeService.update).toHaveBeenCalledWith(expect.objectContaining(counterType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterType>>();
      const counterType = { counterCode: 'ABC' };
      jest.spyOn(counterTypeFormService, 'getCounterType').mockReturnValue({ counterCode: null });
      jest.spyOn(counterTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterType }));
      saveSubject.complete();

      // THEN
      expect(counterTypeFormService.getCounterType).toHaveBeenCalled();
      expect(counterTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounterType>>();
      const counterType = { counterCode: 'ABC' };
      jest.spyOn(counterTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counterTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
