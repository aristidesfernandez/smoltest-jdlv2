import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DeviceCategoryFormService, DeviceCategoryFormGroup } from './device-category-form.service';
import { IDeviceCategory } from '../device-category.model';
import { DeviceCategoryService } from '../service/device-category.service';

@Component({
  selector: 'jhi-device-category-update',
  templateUrl: './device-category-update.component.html',
})
export class DeviceCategoryUpdateComponent implements OnInit {
  isSaving = false;
  deviceCategory: IDeviceCategory | null = null;

  editForm: DeviceCategoryFormGroup = this.deviceCategoryFormService.createDeviceCategoryFormGroup();

  constructor(
    protected deviceCategoryService: DeviceCategoryService,
    protected deviceCategoryFormService: DeviceCategoryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceCategory }) => {
      this.deviceCategory = deviceCategory;
      if (deviceCategory) {
        this.updateForm(deviceCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceCategory = this.deviceCategoryFormService.getDeviceCategory(this.editForm);
    if (deviceCategory.id !== null) {
      this.subscribeToSaveResponse(this.deviceCategoryService.update(deviceCategory));
    } else {
      this.subscribeToSaveResponse(this.deviceCategoryService.create(deviceCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceCategory>>): void {
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

  protected updateForm(deviceCategory: IDeviceCategory): void {
    this.deviceCategory = deviceCategory;
    this.deviceCategoryFormService.resetForm(this.editForm, deviceCategory);
  }
}
