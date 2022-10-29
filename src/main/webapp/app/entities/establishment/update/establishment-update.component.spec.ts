import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EstablishmentFormService } from './establishment-form.service';
import { EstablishmentService } from '../service/establishment.service';
import { IEstablishment } from '../establishment.model';
import { IOperator } from 'app/entities/operator/operator.model';
import { OperatorService } from 'app/entities/operator/service/operator.service';
import { IMunicipality } from 'app/entities/municipality/municipality.model';
import { MunicipalityService } from 'app/entities/municipality/service/municipality.service';

import { EstablishmentUpdateComponent } from './establishment-update.component';

describe('Establishment Management Update Component', () => {
  let comp: EstablishmentUpdateComponent;
  let fixture: ComponentFixture<EstablishmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let establishmentFormService: EstablishmentFormService;
  let establishmentService: EstablishmentService;
  let operatorService: OperatorService;
  let municipalityService: MunicipalityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EstablishmentUpdateComponent],
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
      .overrideTemplate(EstablishmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstablishmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    establishmentFormService = TestBed.inject(EstablishmentFormService);
    establishmentService = TestBed.inject(EstablishmentService);
    operatorService = TestBed.inject(OperatorService);
    municipalityService = TestBed.inject(MunicipalityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Operator query and add missing value', () => {
      const establishment: IEstablishment = { id: 456 };
      const operator: IOperator = { id: 82896 };
      establishment.operator = operator;

      const operatorCollection: IOperator[] = [{ id: 32944 }];
      jest.spyOn(operatorService, 'query').mockReturnValue(of(new HttpResponse({ body: operatorCollection })));
      const additionalOperators = [operator];
      const expectedCollection: IOperator[] = [...additionalOperators, ...operatorCollection];
      jest.spyOn(operatorService, 'addOperatorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ establishment });
      comp.ngOnInit();

      expect(operatorService.query).toHaveBeenCalled();
      expect(operatorService.addOperatorToCollectionIfMissing).toHaveBeenCalledWith(
        operatorCollection,
        ...additionalOperators.map(expect.objectContaining)
      );
      expect(comp.operatorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Municipality query and add missing value', () => {
      const establishment: IEstablishment = { id: 456 };
      const municipality: IMunicipality = { id: 89440 };
      establishment.municipality = municipality;

      const municipalityCollection: IMunicipality[] = [{ id: 15726 }];
      jest.spyOn(municipalityService, 'query').mockReturnValue(of(new HttpResponse({ body: municipalityCollection })));
      const additionalMunicipalities = [municipality];
      const expectedCollection: IMunicipality[] = [...additionalMunicipalities, ...municipalityCollection];
      jest.spyOn(municipalityService, 'addMunicipalityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ establishment });
      comp.ngOnInit();

      expect(municipalityService.query).toHaveBeenCalled();
      expect(municipalityService.addMunicipalityToCollectionIfMissing).toHaveBeenCalledWith(
        municipalityCollection,
        ...additionalMunicipalities.map(expect.objectContaining)
      );
      expect(comp.municipalitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const establishment: IEstablishment = { id: 456 };
      const operator: IOperator = { id: 90276 };
      establishment.operator = operator;
      const municipality: IMunicipality = { id: 52889 };
      establishment.municipality = municipality;

      activatedRoute.data = of({ establishment });
      comp.ngOnInit();

      expect(comp.operatorsSharedCollection).toContain(operator);
      expect(comp.municipalitiesSharedCollection).toContain(municipality);
      expect(comp.establishment).toEqual(establishment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstablishment>>();
      const establishment = { id: 123 };
      jest.spyOn(establishmentFormService, 'getEstablishment').mockReturnValue(establishment);
      jest.spyOn(establishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: establishment }));
      saveSubject.complete();

      // THEN
      expect(establishmentFormService.getEstablishment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(establishmentService.update).toHaveBeenCalledWith(expect.objectContaining(establishment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstablishment>>();
      const establishment = { id: 123 };
      jest.spyOn(establishmentFormService, 'getEstablishment').mockReturnValue({ id: null });
      jest.spyOn(establishmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establishment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: establishment }));
      saveSubject.complete();

      // THEN
      expect(establishmentFormService.getEstablishment).toHaveBeenCalled();
      expect(establishmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstablishment>>();
      const establishment = { id: 123 };
      jest.spyOn(establishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(establishmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOperator', () => {
      it('Should forward to operatorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(operatorService, 'compareOperator');
        comp.compareOperator(entity, entity2);
        expect(operatorService.compareOperator).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMunicipality', () => {
      it('Should forward to municipalityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(municipalityService, 'compareMunicipality');
        comp.compareMunicipality(entity, entity2);
        expect(municipalityService.compareMunicipality).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
