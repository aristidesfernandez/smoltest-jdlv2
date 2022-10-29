import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeviceCategory, NewDeviceCategory } from '../device-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeviceCategory for edit and NewDeviceCategoryFormGroupInput for create.
 */
type DeviceCategoryFormGroupInput = IDeviceCategory | PartialWithRequiredKeyOf<NewDeviceCategory>;

type DeviceCategoryFormDefaults = Pick<NewDeviceCategory, 'id'>;

type DeviceCategoryFormGroupContent = {
  id: FormControl<IDeviceCategory['id'] | NewDeviceCategory['id']>;
  description: FormControl<IDeviceCategory['description']>;
  name: FormControl<IDeviceCategory['name']>;
};

export type DeviceCategoryFormGroup = FormGroup<DeviceCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceCategoryFormService {
  createDeviceCategoryFormGroup(deviceCategory: DeviceCategoryFormGroupInput = { id: null }): DeviceCategoryFormGroup {
    const deviceCategoryRawValue = {
      ...this.getFormDefaults(),
      ...deviceCategory,
    };
    return new FormGroup<DeviceCategoryFormGroupContent>({
      id: new FormControl(
        { value: deviceCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(deviceCategoryRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      name: new FormControl(deviceCategoryRawValue.name, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getDeviceCategory(form: DeviceCategoryFormGroup): IDeviceCategory | NewDeviceCategory {
    return form.getRawValue() as IDeviceCategory | NewDeviceCategory;
  }

  resetForm(form: DeviceCategoryFormGroup, deviceCategory: DeviceCategoryFormGroupInput): void {
    const deviceCategoryRawValue = { ...this.getFormDefaults(), ...deviceCategory };
    form.reset(
      {
        ...deviceCategoryRawValue,
        id: { value: deviceCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeviceCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
