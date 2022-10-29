import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IsleFormService, IsleFormGroup } from './isle-form.service';
import { IIsle } from '../isle.model';
import { IsleService } from '../service/isle.service';

@Component({
  selector: 'jhi-isle-update',
  templateUrl: './isle-update.component.html',
})
export class IsleUpdateComponent implements OnInit {
  isSaving = false;
  isle: IIsle | null = null;

  editForm: IsleFormGroup = this.isleFormService.createIsleFormGroup();

  constructor(protected isleService: IsleService, protected isleFormService: IsleFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isle }) => {
      this.isle = isle;
      if (isle) {
        this.updateForm(isle);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isle = this.isleFormService.getIsle(this.editForm);
    if (isle.id !== null) {
      this.subscribeToSaveResponse(this.isleService.update(isle));
    } else {
      this.subscribeToSaveResponse(this.isleService.create(isle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsle>>): void {
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

  protected updateForm(isle: IIsle): void {
    this.isle = isle;
    this.isleFormService.resetForm(this.editForm, isle);
  }
}
