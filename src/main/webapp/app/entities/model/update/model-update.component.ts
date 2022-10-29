import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ModelFormService, ModelFormGroup } from './model-form.service';
import { IModel } from '../model.model';
import { ModelService } from '../service/model.service';
import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';
import { ManufacturerService } from 'app/entities/manufacturer/service/manufacturer.service';
import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';

@Component({
  selector: 'jhi-model-update',
  templateUrl: './model-update.component.html',
})
export class ModelUpdateComponent implements OnInit {
  isSaving = false;
  model: IModel | null = null;

  manufacturersSharedCollection: IManufacturer[] = [];
  formulasSharedCollection: IFormula[] = [];

  editForm: ModelFormGroup = this.modelFormService.createModelFormGroup();

  constructor(
    protected modelService: ModelService,
    protected modelFormService: ModelFormService,
    protected manufacturerService: ManufacturerService,
    protected formulaService: FormulaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareManufacturer = (o1: IManufacturer | null, o2: IManufacturer | null): boolean =>
    this.manufacturerService.compareManufacturer(o1, o2);

  compareFormula = (o1: IFormula | null, o2: IFormula | null): boolean => this.formulaService.compareFormula(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ model }) => {
      this.model = model;
      if (model) {
        this.updateForm(model);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const model = this.modelFormService.getModel(this.editForm);
    if (model.id !== null) {
      this.subscribeToSaveResponse(this.modelService.update(model));
    } else {
      this.subscribeToSaveResponse(this.modelService.create(model));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(model: IModel): void {
    this.model = model;
    this.modelFormService.resetForm(this.editForm, model);

    this.manufacturersSharedCollection = this.manufacturerService.addManufacturerToCollectionIfMissing<IManufacturer>(
      this.manufacturersSharedCollection,
      model.manufacturer
    );
    this.formulasSharedCollection = this.formulaService.addFormulaToCollectionIfMissing<IFormula>(
      this.formulasSharedCollection,
      model.formula
    );
  }

  protected loadRelationshipsOptions(): void {
    this.manufacturerService
      .query()
      .pipe(map((res: HttpResponse<IManufacturer[]>) => res.body ?? []))
      .pipe(
        map((manufacturers: IManufacturer[]) =>
          this.manufacturerService.addManufacturerToCollectionIfMissing<IManufacturer>(manufacturers, this.model?.manufacturer)
        )
      )
      .subscribe((manufacturers: IManufacturer[]) => (this.manufacturersSharedCollection = manufacturers));

    this.formulaService
      .query()
      .pipe(map((res: HttpResponse<IFormula[]>) => res.body ?? []))
      .pipe(map((formulas: IFormula[]) => this.formulaService.addFormulaToCollectionIfMissing<IFormula>(formulas, this.model?.formula)))
      .subscribe((formulas: IFormula[]) => (this.formulasSharedCollection = formulas));
  }
}
