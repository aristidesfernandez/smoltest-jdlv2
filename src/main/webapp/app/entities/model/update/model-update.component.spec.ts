import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ModelFormService } from './model-form.service';
import { ModelService } from '../service/model.service';
import { IModel } from '../model.model';
import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';
import { ManufacturerService } from 'app/entities/manufacturer/service/manufacturer.service';
import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';

import { ModelUpdateComponent } from './model-update.component';

describe('Model Management Update Component', () => {
  let comp: ModelUpdateComponent;
  let fixture: ComponentFixture<ModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let modelFormService: ModelFormService;
  let modelService: ModelService;
  let manufacturerService: ManufacturerService;
  let formulaService: FormulaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ModelUpdateComponent],
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
      .overrideTemplate(ModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    modelFormService = TestBed.inject(ModelFormService);
    modelService = TestBed.inject(ModelService);
    manufacturerService = TestBed.inject(ManufacturerService);
    formulaService = TestBed.inject(FormulaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Manufacturer query and add missing value', () => {
      const model: IModel = { id: 456 };
      const manufacturer: IManufacturer = { id: 58997 };
      model.manufacturer = manufacturer;

      const manufacturerCollection: IManufacturer[] = [{ id: 65256 }];
      jest.spyOn(manufacturerService, 'query').mockReturnValue(of(new HttpResponse({ body: manufacturerCollection })));
      const additionalManufacturers = [manufacturer];
      const expectedCollection: IManufacturer[] = [...additionalManufacturers, ...manufacturerCollection];
      jest.spyOn(manufacturerService, 'addManufacturerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ model });
      comp.ngOnInit();

      expect(manufacturerService.query).toHaveBeenCalled();
      expect(manufacturerService.addManufacturerToCollectionIfMissing).toHaveBeenCalledWith(
        manufacturerCollection,
        ...additionalManufacturers.map(expect.objectContaining)
      );
      expect(comp.manufacturersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Formula query and add missing value', () => {
      const model: IModel = { id: 456 };
      const formula: IFormula = { id: 12952 };
      model.formula = formula;

      const formulaCollection: IFormula[] = [{ id: 11036 }];
      jest.spyOn(formulaService, 'query').mockReturnValue(of(new HttpResponse({ body: formulaCollection })));
      const additionalFormulas = [formula];
      const expectedCollection: IFormula[] = [...additionalFormulas, ...formulaCollection];
      jest.spyOn(formulaService, 'addFormulaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ model });
      comp.ngOnInit();

      expect(formulaService.query).toHaveBeenCalled();
      expect(formulaService.addFormulaToCollectionIfMissing).toHaveBeenCalledWith(
        formulaCollection,
        ...additionalFormulas.map(expect.objectContaining)
      );
      expect(comp.formulasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const model: IModel = { id: 456 };
      const manufacturer: IManufacturer = { id: 42892 };
      model.manufacturer = manufacturer;
      const formula: IFormula = { id: 30057 };
      model.formula = formula;

      activatedRoute.data = of({ model });
      comp.ngOnInit();

      expect(comp.manufacturersSharedCollection).toContain(manufacturer);
      expect(comp.formulasSharedCollection).toContain(formula);
      expect(comp.model).toEqual(model);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModel>>();
      const model = { id: 123 };
      jest.spyOn(modelFormService, 'getModel').mockReturnValue(model);
      jest.spyOn(modelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: model }));
      saveSubject.complete();

      // THEN
      expect(modelFormService.getModel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(modelService.update).toHaveBeenCalledWith(expect.objectContaining(model));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModel>>();
      const model = { id: 123 };
      jest.spyOn(modelFormService, 'getModel').mockReturnValue({ id: null });
      jest.spyOn(modelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: model }));
      saveSubject.complete();

      // THEN
      expect(modelFormService.getModel).toHaveBeenCalled();
      expect(modelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModel>>();
      const model = { id: 123 };
      jest.spyOn(modelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(modelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareManufacturer', () => {
      it('Should forward to manufacturerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(manufacturerService, 'compareManufacturer');
        comp.compareManufacturer(entity, entity2);
        expect(manufacturerService.compareManufacturer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFormula', () => {
      it('Should forward to formulaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formulaService, 'compareFormula');
        comp.compareFormula(entity, entity2);
        expect(formulaService.compareFormula).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
