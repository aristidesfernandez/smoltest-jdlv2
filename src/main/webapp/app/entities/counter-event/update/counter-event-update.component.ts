import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CounterEventFormService, CounterEventFormGroup } from './counter-event-form.service';
import { ICounterEvent } from '../counter-event.model';
import { CounterEventService } from '../service/counter-event.service';
import { IEventDevice } from 'app/entities/event-device/event-device.model';
import { EventDeviceService } from 'app/entities/event-device/service/event-device.service';

@Component({
  selector: 'jhi-counter-event-update',
  templateUrl: './counter-event-update.component.html',
})
export class CounterEventUpdateComponent implements OnInit {
  isSaving = false;
  counterEvent: ICounterEvent | null = null;

  eventDevicesSharedCollection: IEventDevice[] = [];

  editForm: CounterEventFormGroup = this.counterEventFormService.createCounterEventFormGroup();

  constructor(
    protected counterEventService: CounterEventService,
    protected counterEventFormService: CounterEventFormService,
    protected eventDeviceService: EventDeviceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEventDevice = (o1: IEventDevice | null, o2: IEventDevice | null): boolean => this.eventDeviceService.compareEventDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counterEvent }) => {
      this.counterEvent = counterEvent;
      if (counterEvent) {
        this.updateForm(counterEvent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const counterEvent = this.counterEventFormService.getCounterEvent(this.editForm);
    if (counterEvent.id !== null) {
      this.subscribeToSaveResponse(this.counterEventService.update(counterEvent));
    } else {
      this.subscribeToSaveResponse(this.counterEventService.create(counterEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounterEvent>>): void {
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

  protected updateForm(counterEvent: ICounterEvent): void {
    this.counterEvent = counterEvent;
    this.counterEventFormService.resetForm(this.editForm, counterEvent);

    this.eventDevicesSharedCollection = this.eventDeviceService.addEventDeviceToCollectionIfMissing<IEventDevice>(
      this.eventDevicesSharedCollection,
      counterEvent.eventDevice
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eventDeviceService
      .query()
      .pipe(map((res: HttpResponse<IEventDevice[]>) => res.body ?? []))
      .pipe(
        map((eventDevices: IEventDevice[]) =>
          this.eventDeviceService.addEventDeviceToCollectionIfMissing<IEventDevice>(eventDevices, this.counterEvent?.eventDevice)
        )
      )
      .subscribe((eventDevices: IEventDevice[]) => (this.eventDevicesSharedCollection = eventDevices));
  }
}
