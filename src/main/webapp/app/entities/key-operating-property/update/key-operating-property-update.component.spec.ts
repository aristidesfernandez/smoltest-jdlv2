import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KeyOperatingPropertyFormService } from './key-operating-property-form.service';
import { KeyOperatingPropertyService } from '../service/key-operating-property.service';
import { IKeyOperatingProperty } from '../key-operating-property.model';

import { KeyOperatingPropertyUpdateComponent } from './key-operating-property-update.component';

describe('KeyOperatingProperty Management Update Component', () => {
  let comp: KeyOperatingPropertyUpdateComponent;
  let fixture: ComponentFixture<KeyOperatingPropertyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let keyOperatingPropertyFormService: KeyOperatingPropertyFormService;
  let keyOperatingPropertyService: KeyOperatingPropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KeyOperatingPropertyUpdateComponent],
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
      .overrideTemplate(KeyOperatingPropertyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KeyOperatingPropertyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    keyOperatingPropertyFormService = TestBed.inject(KeyOperatingPropertyFormService);
    keyOperatingPropertyService = TestBed.inject(KeyOperatingPropertyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const keyOperatingProperty: IKeyOperatingProperty = { id: 456 };

      activatedRoute.data = of({ keyOperatingProperty });
      comp.ngOnInit();

      expect(comp.keyOperatingProperty).toEqual(keyOperatingProperty);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKeyOperatingProperty>>();
      const keyOperatingProperty = { id: 123 };
      jest.spyOn(keyOperatingPropertyFormService, 'getKeyOperatingProperty').mockReturnValue(keyOperatingProperty);
      jest.spyOn(keyOperatingPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keyOperatingProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: keyOperatingProperty }));
      saveSubject.complete();

      // THEN
      expect(keyOperatingPropertyFormService.getKeyOperatingProperty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(keyOperatingPropertyService.update).toHaveBeenCalledWith(expect.objectContaining(keyOperatingProperty));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKeyOperatingProperty>>();
      const keyOperatingProperty = { id: 123 };
      jest.spyOn(keyOperatingPropertyFormService, 'getKeyOperatingProperty').mockReturnValue({ id: null });
      jest.spyOn(keyOperatingPropertyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keyOperatingProperty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: keyOperatingProperty }));
      saveSubject.complete();

      // THEN
      expect(keyOperatingPropertyFormService.getKeyOperatingProperty).toHaveBeenCalled();
      expect(keyOperatingPropertyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKeyOperatingProperty>>();
      const keyOperatingProperty = { id: 123 };
      jest.spyOn(keyOperatingPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ keyOperatingProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(keyOperatingPropertyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
