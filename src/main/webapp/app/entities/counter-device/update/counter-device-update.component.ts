import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CounterDeviceFormService, CounterDeviceFormGroup } from './counter-device-form.service';
import { ICounterDevice } from '../counter-device.model';
import { CounterDeviceService } from '../service/counter-device.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

@Component({
  selector: 'jhi-counter-device-update',
  templateUrl: './counter-device-update.component.html',
})
export class CounterDeviceUpdateComponent implements OnInit {
  isSaving = false;
  counterDevice: ICounterDevice | null = null;

  devicesSharedCollection: IDevice[] = [];

  editForm: CounterDeviceFormGroup = this.counterDeviceFormService.createCounterDeviceFormGroup();

  constructor(
    protected counterDeviceService: CounterDeviceService,
    protected counterDeviceFormService: CounterDeviceFormService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterDevice }) => {
      this.counterDevice = counterDevice;
      if (counterDevice) {
        this.updateForm(counterDevice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const counterDevice = this.counterDeviceFormService.getCounterDevice(this.editForm);
    if (counterDevice.id !== null) {
      this.subscribeToSaveResponse(this.counterDeviceService.update(counterDevice));
    } else {
      this.subscribeToSaveResponse(this.counterDeviceService.create(counterDevice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounterDevice>>): void {
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

  protected updateForm(counterDevice: ICounterDevice): void {
    this.counterDevice = counterDevice;
    this.counterDeviceFormService.resetForm(this.editForm, counterDevice);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      counterDevice.device
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.counterDevice?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
