import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperationalPropertiesEstablishmentFormService } from './operational-properties-establishment-form.service';
import { OperationalPropertiesEstablishmentService } from '../service/operational-properties-establishment.service';
import { IOperationalPropertiesEstablishment } from '../operational-properties-establishment.model';
import { IKeyOperatingProperty } from 'app/entities/key-operating-property/key-operating-property.model';
import { KeyOperatingPropertyService } from 'app/entities/key-operating-property/service/key-operating-property.service';
import { IEstablishment } from 'app/entities/establishment/establishment.model';
import { EstablishmentService } from 'app/entities/establishment/service/establishment.service';

import { OperationalPropertiesEstablishmentUpdateComponent } from './operational-properties-establishment-update.component';

describe('OperationalPropertiesEstablishment Management Update Component', () => {
  let comp: OperationalPropertiesEstablishmentUpdateComponent;
  let fixture: ComponentFixture<OperationalPropertiesEstablishmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationalPropertiesEstablishmentFormService: OperationalPropertiesEstablishmentFormService;
  let operationalPropertiesEstablishmentService: OperationalPropertiesEstablishmentService;
  let keyOperatingPropertyService: KeyOperatingPropertyService;
  let establishmentService: EstablishmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperationalPropertiesEstablishmentUpdateComponent],
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
      .overrideTemplate(OperationalPropertiesEstablishmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationalPropertiesEstablishmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationalPropertiesEstablishmentFormService = TestBed.inject(OperationalPropertiesEstablishmentFormService);
    operationalPropertiesEstablishmentService = TestBed.inject(OperationalPropertiesEstablishmentService);
    keyOperatingPropertyService = TestBed.inject(KeyOperatingPropertyService);
    establishmentService = TestBed.inject(EstablishmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call KeyOperatingProperty query and add missing value', () => {
      const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = { id: 456 };
      const keyOperatingProperty: IKeyOperatingProperty = { id: 65138 };
      operationalPropertiesEstablishment.keyOperatingProperty = keyOperatingProperty;

      const keyOperatingPropertyCollection: IKeyOperatingProperty[] = [{ id: 5555 }];
      jest.spyOn(keyOperatingPropertyService, 'query').mockReturnValue(of(new HttpResponse({ body: keyOperatingPropertyCollection })));
      const additionalKeyOperatingProperties = [keyOperatingProperty];
      const expectedCollection: IKeyOperatingProperty[] = [...additionalKeyOperatingProperties, ...keyOperatingPropertyCollection];
      jest.spyOn(keyOperatingPropertyService, 'addKeyOperatingPropertyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operationalPropertiesEstablishment });
      comp.ngOnInit();

      expect(keyOperatingPropertyService.query).toHaveBeenCalled();
      expect(keyOperatingPropertyService.addKeyOperatingPropertyToCollectionIfMissing).toHaveBeenCalledWith(
        keyOperatingPropertyCollection,
        ...additionalKeyOperatingProperties.map(expect.objectContaining)
      );
      expect(comp.keyOperatingPropertiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Establishment query and add missing value', () => {
      const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = { id: 456 };
      const establishment: IEstablishment = { id: 30815 };
      operationalPropertiesEstablishment.establishment = establishment;

      const establishmentCollection: IEstablishment[] = [{ id: 22542 }];
      jest.spyOn(establishmentService, 'query').mockReturnValue(of(new HttpResponse({ body: establishmentCollection })));
      const additionalEstablishments = [establishment];
      const expectedCollection: IEstablishment[] = [...additionalEstablishments, ...establishmentCollection];
      jest.spyOn(establishmentService, 'addEstablishmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operationalPropertiesEstablishment });
      comp.ngOnInit();

      expect(establishmentService.query).toHaveBeenCalled();
      expect(establishmentService.addEstablishmentToCollectionIfMissing).toHaveBeenCalledWith(
        establishmentCollection,
        ...additionalEstablishments.map(expect.objectContaining)
      );
      expect(comp.establishmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operationalPropertiesEstablishment: IOperationalPropertiesEstablishment = { id: 456 };
      const keyOperatingProperty: IKeyOperatingProperty = { id: 14135 };
      operationalPropertiesEstablishment.keyOperatingProperty = keyOperatingProperty;
      const establishment: IEstablishment = { id: 95345 };
      operationalPropertiesEstablishment.establishment = establishment;

      activatedRoute.data = of({ operationalPropertiesEstablishment });
      comp.ngOnInit();

      expect(comp.keyOperatingPropertiesSharedCollection).toContain(keyOperatingProperty);
      expect(comp.establishmentsSharedCollection).toContain(establishment);
      expect(comp.operationalPropertiesEstablishment).toEqual(operationalPropertiesEstablishment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalPropertiesEstablishment>>();
      const operationalPropertiesEstablishment = { id: 123 };
      jest
        .spyOn(operationalPropertiesEstablishmentFormService, 'getOperationalPropertiesEstablishment')
        .mockReturnValue(operationalPropertiesEstablishment);
      jest.spyOn(operationalPropertiesEstablishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalPropertiesEstablishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operationalPropertiesEstablishment }));
      saveSubject.complete();

      // THEN
      expect(operationalPropertiesEstablishmentFormService.getOperationalPropertiesEstablishment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationalPropertiesEstablishmentService.update).toHaveBeenCalledWith(
        expect.objectContaining(operationalPropertiesEstablishment)
      );
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalPropertiesEstablishment>>();
      const operationalPropertiesEstablishment = { id: 123 };
      jest.spyOn(operationalPropertiesEstablishmentFormService, 'getOperationalPropertiesEstablishment').mockReturnValue({ id: null });
      jest.spyOn(operationalPropertiesEstablishmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalPropertiesEstablishment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operationalPropertiesEstablishment }));
      saveSubject.complete();

      // THEN
      expect(operationalPropertiesEstablishmentFormService.getOperationalPropertiesEstablishment).toHaveBeenCalled();
      expect(operationalPropertiesEstablishmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalPropertiesEstablishment>>();
      const operationalPropertiesEstablishment = { id: 123 };
      jest.spyOn(operationalPropertiesEstablishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalPropertiesEstablishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationalPropertiesEstablishmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareKeyOperatingProperty', () => {
      it('Should forward to keyOperatingPropertyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(keyOperatingPropertyService, 'compareKeyOperatingProperty');
        comp.compareKeyOperatingProperty(entity, entity2);
        expect(keyOperatingPropertyService.compareKeyOperatingProperty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEstablishment', () => {
      it('Should forward to establishmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(establishmentService, 'compareEstablishment');
        comp.compareEstablishment(entity, entity2);
        expect(establishmentService.compareEstablishment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
