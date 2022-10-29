import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICounterEvent, NewCounterEvent } from '../counter-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICounterEvent for edit and NewCounterEventFormGroupInput for create.
 */
type CounterEventFormGroupInput = ICounterEvent | PartialWithRequiredKeyOf<NewCounterEvent>;

type CounterEventFormDefaults = Pick<NewCounterEvent, 'id'>;

type CounterEventFormGroupContent = {
  id: FormControl<ICounterEvent['id'] | NewCounterEvent['id']>;
  valueCounter: FormControl<ICounterEvent['valueCounter']>;
  denominationSale: FormControl<ICounterEvent['denominationSale']>;
  counterCode: FormControl<ICounterEvent['counterCode']>;
  eventDevice: FormControl<ICounterEvent['eventDevice']>;
};

export type CounterEventFormGroup = FormGroup<CounterEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CounterEventFormService {
  createCounterEventFormGroup(counterEvent: CounterEventFormGroupInput = { id: null }): CounterEventFormGroup {
    const counterEventRawValue = {
      ...this.getFormDefaults(),
      ...counterEvent,
    };
    return new FormGroup<CounterEventFormGroupContent>({
      id: new FormControl(
        { value: counterEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      valueCounter: new FormControl(counterEventRawValue.valueCounter),
      denominationSale: new FormControl(counterEventRawValue.denominationSale),
      counterCode: new FormControl(counterEventRawValue.counterCode, {
        validators: [Validators.required, Validators.maxLength(2)],
      }),
      eventDevice: new FormControl(counterEventRawValue.eventDevice, {
        validators: [Validators.required],
      }),
    });
  }

  getCounterEvent(form: CounterEventFormGroup): ICounterEvent | NewCounterEvent {
    return form.getRawValue() as ICounterEvent | NewCounterEvent;
  }

  resetForm(form: CounterEventFormGroup, counterEvent: CounterEventFormGroupInput): void {
    const counterEventRawValue = { ...this.getFormDefaults(), ...counterEvent };
    form.reset(
      {
        ...counterEventRawValue,
        id: { value: counterEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CounterEventFormDefaults {
    return {
      id: null,
    };
  }
}
