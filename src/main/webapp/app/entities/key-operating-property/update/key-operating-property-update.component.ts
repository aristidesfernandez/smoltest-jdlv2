import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { KeyOperatingPropertyFormService, KeyOperatingPropertyFormGroup } from './key-operating-property-form.service';
import { IKeyOperatingProperty } from '../key-operating-property.model';
import { KeyOperatingPropertyService } from '../service/key-operating-property.service';

@Component({
  selector: 'jhi-key-operating-property-update',
  templateUrl: './key-operating-property-update.component.html',
})
export class KeyOperatingPropertyUpdateComponent implements OnInit {
  isSaving = false;
  keyOperatingProperty: IKeyOperatingProperty | null = null;

  editForm: KeyOperatingPropertyFormGroup = this.keyOperatingPropertyFormService.createKeyOperatingPropertyFormGroup();

  constructor(
    protected keyOperatingPropertyService: KeyOperatingPropertyService,
    protected keyOperatingPropertyFormService: KeyOperatingPropertyFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyOperatingProperty }) => {
      this.keyOperatingProperty = keyOperatingProperty;
      if (keyOperatingProperty) {
        this.updateForm(keyOperatingProperty);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const keyOperatingProperty = this.keyOperatingPropertyFormService.getKeyOperatingProperty(this.editForm);
    if (keyOperatingProperty.id !== null) {
      this.subscribeToSaveResponse(this.keyOperatingPropertyService.update(keyOperatingProperty));
    } else {
      this.subscribeToSaveResponse(this.keyOperatingPropertyService.create(keyOperatingProperty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKeyOperatingProperty>>): void {
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

  protected updateForm(keyOperatingProperty: IKeyOperatingProperty): void {
    this.keyOperatingProperty = keyOperatingProperty;
    this.keyOperatingPropertyFormService.resetForm(this.editForm, keyOperatingProperty);
  }
}
