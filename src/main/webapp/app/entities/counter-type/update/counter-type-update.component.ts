import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CounterTypeFormService, CounterTypeFormGroup } from './counter-type-form.service';
import { ICounterType } from '../counter-type.model';
import { CounterTypeService } from '../service/counter-type.service';

@Component({
  selector: 'jhi-counter-type-update',
  templateUrl: './counter-type-update.component.html',
})
export class CounterTypeUpdateComponent implements OnInit {
  isSaving = false;
  counterType: ICounterType | null = null;

  editForm: CounterTypeFormGroup = this.counterTypeFormService.createCounterTypeFormGroup();

  constructor(
    protected counterTypeService: CounterTypeService,
    protected counterTypeFormService: CounterTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterType }) => {
      this.counterType = counterType;
      if (counterType) {
        this.updateForm(counterType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const counterType = this.counterTypeFormService.getCounterType(this.editForm);
    if (counterType.counterCode !== null) {
      this.subscribeToSaveResponse(this.counterTypeService.update(counterType));
    } else {
      this.subscribeToSaveResponse(this.counterTypeService.create(counterType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounterType>>): void {
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

  protected updateForm(counterType: ICounterType): void {
    this.counterType = counterType;
    this.counterTypeFormService.resetForm(this.editForm, counterType);
  }
}
