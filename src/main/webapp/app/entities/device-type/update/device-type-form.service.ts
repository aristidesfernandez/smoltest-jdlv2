import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeviceType, NewDeviceType } from '../device-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeviceType for edit and NewDeviceTypeFormGroupInput for create.
 */
type DeviceTypeFormGroupInput = IDeviceType | PartialWithRequiredKeyOf<NewDeviceType>;

type DeviceTypeFormDefaults = Pick<NewDeviceType, 'id'>;

type DeviceTypeFormGroupContent = {
  id: FormControl<IDeviceType['id'] | NewDeviceType['id']>;
  description: FormControl<IDeviceType['description']>;
  name: FormControl<IDeviceType['name']>;
};

export type DeviceTypeFormGroup = FormGroup<DeviceTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceTypeFormService {
  createDeviceTypeFormGroup(deviceType: DeviceTypeFormGroupInput = { id: null }): DeviceTypeFormGroup {
    const deviceTypeRawValue = {
      ...this.getFormDefaults(),
      ...deviceType,
    };
    return new FormGroup<DeviceTypeFormGroupContent>({
      id: new FormControl(
        { value: deviceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(deviceTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      name: new FormControl(deviceTypeRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getDeviceType(form: DeviceTypeFormGroup): IDeviceType | NewDeviceType {
    return form.getRawValue() as IDeviceType | NewDeviceType;
  }

  resetForm(form: DeviceTypeFormGroup, deviceType: DeviceTypeFormGroupInput): void {
    const deviceTypeRawValue = { ...this.getFormDefaults(), ...deviceType };
    form.reset(
      {
        ...deviceTypeRawValue,
        id: { value: deviceTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeviceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
