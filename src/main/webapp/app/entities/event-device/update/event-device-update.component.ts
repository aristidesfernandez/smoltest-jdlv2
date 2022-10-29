import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventDeviceFormService, EventDeviceFormGroup } from './event-device-form.service';
import { IEventDevice } from '../event-device.model';
import { EventDeviceService } from '../service/event-device.service';
import { IDeviceEstablishment } from 'app/entities/device-establishment/device-establishment.model';
import { DeviceEstablishmentService } from 'app/entities/device-establishment/service/device-establishment.service';
import { IEventType } from 'app/entities/event-type/event-type.model';
import { EventTypeService } from 'app/entities/event-type/service/event-type.service';

@Component({
  selector: 'jhi-event-device-update',
  templateUrl: './event-device-update.component.html',
})
export class EventDeviceUpdateComponent implements OnInit {
  isSaving = false;
  eventDevice: IEventDevice | null = null;

  deviceEstablishmentsSharedCollection: IDeviceEstablishment[] = [];
  eventTypesSharedCollection: IEventType[] = [];

  editForm: EventDeviceFormGroup = this.eventDeviceFormService.createEventDeviceFormGroup();

  constructor(
    protected eventDeviceService: EventDeviceService,
    protected eventDeviceFormService: EventDeviceFormService,
    protected deviceEstablishmentService: DeviceEstablishmentService,
    protected eventTypeService: EventTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDeviceEstablishment = (o1: IDeviceEstablishment | null, o2: IDeviceEstablishment | null): boolean =>
    this.deviceEstablishmentService.compareDeviceEstablishment(o1, o2);

  compareEventType = (o1: IEventType | null, o2: IEventType | null): boolean => this.eventTypeService.compareEventType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventDevice }) => {
      this.eventDevice = eventDevice;
      if (eventDevice) {
        this.updateForm(eventDevice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventDevice = this.eventDeviceFormService.getEventDevice(this.editForm);
    if (eventDevice.id !== null) {
      this.subscribeToSaveResponse(this.eventDeviceService.update(eventDevice));
    } else {
      this.subscribeToSaveResponse(this.eventDeviceService.create(eventDevice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventDevice>>): void {
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

  protected updateForm(eventDevice: IEventDevice): void {
    this.eventDevice = eventDevice;
    this.eventDeviceFormService.resetForm(this.editForm, eventDevice);

    this.deviceEstablishmentsSharedCollection =
      this.deviceEstablishmentService.addDeviceEstablishmentToCollectionIfMissing<IDeviceEstablishment>(
        this.deviceEstablishmentsSharedCollection,
        eventDevice.deviceEstablishment
      );
    this.eventTypesSharedCollection = this.eventTypeService.addEventTypeToCollectionIfMissing<IEventType>(
      this.eventTypesSharedCollection,
      eventDevice.eventType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceEstablishmentService
      .query()
      .pipe(map((res: HttpResponse<IDeviceEstablishment[]>) => res.body ?? []))
      .pipe(
        map((deviceEstablishments: IDeviceEstablishment[]) =>
          this.deviceEstablishmentService.addDeviceEstablishmentToCollectionIfMissing<IDeviceEstablishment>(
            deviceEstablishments,
            this.eventDevice?.deviceEstablishment
          )
        )
      )
      .subscribe((deviceEstablishments: IDeviceEstablishment[]) => (this.deviceEstablishmentsSharedCollection = deviceEstablishments));

    this.eventTypeService
      .query()
      .pipe(map((res: HttpResponse<IEventType[]>) => res.body ?? []))
      .pipe(
        map((eventTypes: IEventType[]) =>
          this.eventTypeService.addEventTypeToCollectionIfMissing<IEventType>(eventTypes, this.eventDevice?.eventType)
        )
      )
      .subscribe((eventTypes: IEventType[]) => (this.eventTypesSharedCollection = eventTypes));
  }
}
