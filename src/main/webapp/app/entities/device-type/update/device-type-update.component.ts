import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DeviceTypeFormService, DeviceTypeFormGroup } from './device-type-form.service';
import { IDeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';

@Component({
  selector: 'jhi-device-type-update',
  templateUrl: './device-type-update.component.html',
})
export class DeviceTypeUpdateComponent implements OnInit {
  isSaving = false;
  deviceType: IDeviceType | null = null;

  editForm: DeviceTypeFormGroup = this.deviceTypeFormService.createDeviceTypeFormGroup();

  constructor(
    protected deviceTypeService: DeviceTypeService,
    protected deviceTypeFormService: DeviceTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceType }) => {
      this.deviceType = deviceType;
      if (deviceType) {
        this.updateForm(deviceType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceType = this.deviceTypeFormService.getDeviceType(this.editForm);
    if (deviceType.id !== null) {
      this.subscribeToSaveResponse(this.deviceTypeService.update(deviceType));
    } else {
      this.subscribeToSaveResponse(this.deviceTypeService.create(deviceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceType>>): void {
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

  protected updateForm(deviceType: IDeviceType): void {
    this.deviceType = deviceType;
    this.deviceTypeFormService.resetForm(this.editForm, deviceType);
  }
}
