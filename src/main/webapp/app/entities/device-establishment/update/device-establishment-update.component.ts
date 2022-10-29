import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DeviceEstablishmentFormService, DeviceEstablishmentFormGroup } from './device-establishment-form.service';
import { IDeviceEstablishment } from '../device-establishment.model';
import { DeviceEstablishmentService } from '../service/device-establishment.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

@Component({
  selector: 'jhi-device-establishment-update',
  templateUrl: './device-establishment-update.component.html',
})
export class DeviceEstablishmentUpdateComponent implements OnInit {
  isSaving = false;
  deviceEstablishment: IDeviceEstablishment | null = null;

  devicesSharedCollection: IDevice[] = [];

  editForm: DeviceEstablishmentFormGroup = this.deviceEstablishmentFormService.createDeviceEstablishmentFormGroup();

  constructor(
    protected deviceEstablishmentService: DeviceEstablishmentService,
    protected deviceEstablishmentFormService: DeviceEstablishmentFormService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceEstablishment }) => {
      this.deviceEstablishment = deviceEstablishment;
      if (deviceEstablishment) {
        this.updateForm(deviceEstablishment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceEstablishment = this.deviceEstablishmentFormService.getDeviceEstablishment(this.editForm);
    if (deviceEstablishment.id !== null) {
      this.subscribeToSaveResponse(this.deviceEstablishmentService.update(deviceEstablishment));
    } else {
      this.subscribeToSaveResponse(this.deviceEstablishmentService.create(deviceEstablishment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceEstablishment>>): void {
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

  protected updateForm(deviceEstablishment: IDeviceEstablishment): void {
    this.deviceEstablishment = deviceEstablishment;
    this.deviceEstablishmentFormService.resetForm(this.editForm, deviceEstablishment);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      deviceEstablishment.device
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(
        map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.deviceEstablishment?.device))
      )
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
