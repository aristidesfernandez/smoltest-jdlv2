import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OperatorFormService, OperatorFormGroup } from './operator-form.service';
import { IOperator } from '../operator.model';
import { OperatorService } from '../service/operator.service';
import { IMunicipality } from 'app/entities/municipality/municipality.model';
import { MunicipalityService } from 'app/entities/municipality/service/municipality.service';

@Component({
  selector: 'jhi-operator-update',
  templateUrl: './operator-update.component.html',
})
export class OperatorUpdateComponent implements OnInit {
  isSaving = false;
  operator: IOperator | null = null;

  municipalitiesSharedCollection: IMunicipality[] = [];

  editForm: OperatorFormGroup = this.operatorFormService.createOperatorFormGroup();

  constructor(
    protected operatorService: OperatorService,
    protected operatorFormService: OperatorFormService,
    protected municipalityService: MunicipalityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMunicipality = (o1: IMunicipality | null, o2: IMunicipality | null): boolean =>
    this.municipalityService.compareMunicipality(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operator }) => {
      this.operator = operator;
      if (operator) {
        this.updateForm(operator);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operator = this.operatorFormService.getOperator(this.editForm);
    if (operator.id !== null) {
      this.subscribeToSaveResponse(this.operatorService.update(operator));
    } else {
      this.subscribeToSaveResponse(this.operatorService.create(operator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperator>>): void {
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

  protected updateForm(operator: IOperator): void {
    this.operator = operator;
    this.operatorFormService.resetForm(this.editForm, operator);

    this.municipalitiesSharedCollection = this.municipalityService.addMunicipalityToCollectionIfMissing<IMunicipality>(
      this.municipalitiesSharedCollection,
      operator.municipality
    );
  }

  protected loadRelationshipsOptions(): void {
    this.municipalityService
      .query()
      .pipe(map((res: HttpResponse<IMunicipality[]>) => res.body ?? []))
      .pipe(
        map((municipalities: IMunicipality[]) =>
          this.municipalityService.addMunicipalityToCollectionIfMissing<IMunicipality>(municipalities, this.operator?.municipality)
        )
      )
      .subscribe((municipalities: IMunicipality[]) => (this.municipalitiesSharedCollection = municipalities));
  }
}
