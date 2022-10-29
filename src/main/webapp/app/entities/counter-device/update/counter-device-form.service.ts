import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICounterDevice, NewCounterDevice } from '../counter-device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICounterDevice for edit and NewCounterDeviceFormGroupInput for create.
 */
type CounterDeviceFormGroupInput = ICounterDevice | PartialWithRequiredKeyOf<NewCounterDevice>;

type CounterDeviceFormDefaults = Pick<NewCounterDevice, 'id' | 'manualCounter' | 'decimalsManualCounter'>;

type CounterDeviceFormGroupContent = {
  id: FormControl<ICounterDevice['id'] | NewCounterDevice['id']>;
  value: FormControl<ICounterDevice['value']>;
  rolloverValue: FormControl<ICounterDevice['rolloverValue']>;
  creditSale: FormControl<ICounterDevice['creditSale']>;
  manualCounter: FormControl<ICounterDevice['manualCounter']>;
  manualMultiplier: FormControl<ICounterDevice['manualMultiplier']>;
  decimalsManualCounter: FormControl<ICounterDevice['decimalsManualCounter']>;
  device: FormControl<ICounterDevice['device']>;
};

export type CounterDeviceFormGroup = FormGroup<CounterDeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CounterDeviceFormService {
  createCounterDeviceFormGroup(counterDevice: CounterDeviceFormGroupInput = { id: null }): CounterDeviceFormGroup {
    const counterDeviceRawValue = {
      ...this.getFormDefaults(),
      ...counterDevice,
    };
    return new FormGroup<CounterDeviceFormGroupContent>({
      id: new FormControl(
        { value: counterDeviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      value: new FormControl(counterDeviceRawValue.value),
      rolloverValue: new FormControl(counterDeviceRawValue.rolloverValue),
      creditSale: new FormControl(counterDeviceRawValue.creditSale),
      manualCounter: new FormControl(counterDeviceRawValue.manualCounter),
      manualMultiplier: new FormControl(counterDeviceRawValue.manualMultiplier),
      decimalsManualCounter: new FormControl(counterDeviceRawValue.decimalsManualCounter),
      device: new FormControl(counterDeviceRawValue.device, {
        validators: [Validators.required],
      }),
    });
  }

  getCounterDevice(form: CounterDeviceFormGroup): ICounterDevice | NewCounterDevice {
    return form.getRawValue() as ICounterDevice | NewCounterDevice;
  }

  resetForm(form: CounterDeviceFormGroup, counterDevice: CounterDeviceFormGroupInput): void {
    const counterDeviceRawValue = { ...this.getFormDefaults(), ...counterDevice };
    form.reset(
      {
        ...counterDeviceRawValue,
        id: { value: counterDeviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CounterDeviceFormDefaults {
    return {
      id: null,
      manualCounter: false,
      decimalsManualCounter: false,
    };
  }
}
