import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEventType, NewEventType } from '../event-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventType for edit and NewEventTypeFormGroupInput for create.
 */
type EventTypeFormGroupInput = IEventType | PartialWithRequiredKeyOf<NewEventType>;

type EventTypeFormDefaults = Pick<NewEventType, 'id' | 'isStorable' | 'isPriority' | 'isAlarm'>;

type EventTypeFormGroupContent = {
  id: FormControl<IEventType['id'] | NewEventType['id']>;
  eventCode: FormControl<IEventType['eventCode']>;
  sasCode: FormControl<IEventType['sasCode']>;
  description: FormControl<IEventType['description']>;
  isStorable: FormControl<IEventType['isStorable']>;
  isPriority: FormControl<IEventType['isPriority']>;
  procesador: FormControl<IEventType['procesador']>;
  isAlarm: FormControl<IEventType['isAlarm']>;
};

export type EventTypeFormGroup = FormGroup<EventTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventTypeFormService {
  createEventTypeFormGroup(eventType: EventTypeFormGroupInput = { id: null }): EventTypeFormGroup {
    const eventTypeRawValue = {
      ...this.getFormDefaults(),
      ...eventType,
    };
    return new FormGroup<EventTypeFormGroupContent>({
      id: new FormControl(
        { value: eventTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      eventCode: new FormControl(eventTypeRawValue.eventCode, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      sasCode: new FormControl(eventTypeRawValue.sasCode, {
        validators: [Validators.maxLength(50)],
      }),
      description: new FormControl(eventTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      isStorable: new FormControl(eventTypeRawValue.isStorable),
      isPriority: new FormControl(eventTypeRawValue.isPriority),
      procesador: new FormControl(eventTypeRawValue.procesador, {
        validators: [Validators.maxLength(100)],
      }),
      isAlarm: new FormControl(eventTypeRawValue.isAlarm),
    });
  }

  getEventType(form: EventTypeFormGroup): IEventType | NewEventType {
    return form.getRawValue() as IEventType | NewEventType;
  }

  resetForm(form: EventTypeFormGroup, eventType: EventTypeFormGroupInput): void {
    const eventTypeRawValue = { ...this.getFormDefaults(), ...eventType };
    form.reset(
      {
        ...eventTypeRawValue,
        id: { value: eventTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventTypeFormDefaults {
    return {
      id: null,
      isStorable: false,
      isPriority: false,
      isAlarm: false,
    };
  }
}
