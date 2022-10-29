import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEventDevice, NewEventDevice } from '../event-device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventDevice for edit and NewEventDeviceFormGroupInput for create.
 */
type EventDeviceFormGroupInput = IEventDevice | PartialWithRequiredKeyOf<NewEventDevice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEventDevice | NewEventDevice> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type EventDeviceFormRawValue = FormValueOf<IEventDevice>;

type NewEventDeviceFormRawValue = FormValueOf<NewEventDevice>;

type EventDeviceFormDefaults = Pick<NewEventDevice, 'id' | 'createdAt' | 'theoreticalPercentage'>;

type EventDeviceFormGroupContent = {
  id: FormControl<EventDeviceFormRawValue['id'] | NewEventDevice['id']>;
  createdAt: FormControl<EventDeviceFormRawValue['createdAt']>;
  theoreticalPercentage: FormControl<EventDeviceFormRawValue['theoreticalPercentage']>;
  moneyDenomination: FormControl<EventDeviceFormRawValue['moneyDenomination']>;
  deviceEstablishment: FormControl<EventDeviceFormRawValue['deviceEstablishment']>;
  eventType: FormControl<EventDeviceFormRawValue['eventType']>;
};

export type EventDeviceFormGroup = FormGroup<EventDeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventDeviceFormService {
  createEventDeviceFormGroup(eventDevice: EventDeviceFormGroupInput = { id: null }): EventDeviceFormGroup {
    const eventDeviceRawValue = this.convertEventDeviceToEventDeviceRawValue({
      ...this.getFormDefaults(),
      ...eventDevice,
    });
    return new FormGroup<EventDeviceFormGroupContent>({
      id: new FormControl(
        { value: eventDeviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdAt: new FormControl(eventDeviceRawValue.createdAt),
      theoreticalPercentage: new FormControl(eventDeviceRawValue.theoreticalPercentage),
      moneyDenomination: new FormControl(eventDeviceRawValue.moneyDenomination),
      deviceEstablishment: new FormControl(eventDeviceRawValue.deviceEstablishment, {
        validators: [Validators.required],
      }),
      eventType: new FormControl(eventDeviceRawValue.eventType, {
        validators: [Validators.required],
      }),
    });
  }

  getEventDevice(form: EventDeviceFormGroup): IEventDevice | NewEventDevice {
    return this.convertEventDeviceRawValueToEventDevice(form.getRawValue() as EventDeviceFormRawValue | NewEventDeviceFormRawValue);
  }

  resetForm(form: EventDeviceFormGroup, eventDevice: EventDeviceFormGroupInput): void {
    const eventDeviceRawValue = this.convertEventDeviceToEventDeviceRawValue({ ...this.getFormDefaults(), ...eventDevice });
    form.reset(
      {
        ...eventDeviceRawValue,
        id: { value: eventDeviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventDeviceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      theoreticalPercentage: false,
    };
  }

  private convertEventDeviceRawValueToEventDevice(
    rawEventDevice: EventDeviceFormRawValue | NewEventDeviceFormRawValue
  ): IEventDevice | NewEventDevice {
    return {
      ...rawEventDevice,
      createdAt: dayjs(rawEventDevice.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertEventDeviceToEventDeviceRawValue(
    eventDevice: IEventDevice | (Partial<NewEventDevice> & EventDeviceFormDefaults)
  ): EventDeviceFormRawValue | PartialWithRequiredKeyOf<NewEventDeviceFormRawValue> {
    return {
      ...eventDevice,
      createdAt: eventDevice.createdAt ? eventDevice.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
