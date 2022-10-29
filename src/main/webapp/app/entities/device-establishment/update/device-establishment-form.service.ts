import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDeviceEstablishment, NewDeviceEstablishment } from '../device-establishment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeviceEstablishment for edit and NewDeviceEstablishmentFormGroupInput for create.
 */
type DeviceEstablishmentFormGroupInput = IDeviceEstablishment | PartialWithRequiredKeyOf<NewDeviceEstablishment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDeviceEstablishment | NewDeviceEstablishment> = Omit<T, 'registrationAt' | 'departureAt'> & {
  registrationAt?: string | null;
  departureAt?: string | null;
};

type DeviceEstablishmentFormRawValue = FormValueOf<IDeviceEstablishment>;

type NewDeviceEstablishmentFormRawValue = FormValueOf<NewDeviceEstablishment>;

type DeviceEstablishmentFormDefaults = Pick<NewDeviceEstablishment, 'id' | 'registrationAt' | 'departureAt'>;

type DeviceEstablishmentFormGroupContent = {
  id: FormControl<DeviceEstablishmentFormRawValue['id'] | NewDeviceEstablishment['id']>;
  registrationAt: FormControl<DeviceEstablishmentFormRawValue['registrationAt']>;
  departureAt: FormControl<DeviceEstablishmentFormRawValue['departureAt']>;
  deviceNumber: FormControl<DeviceEstablishmentFormRawValue['deviceNumber']>;
  consecutiveDevice: FormControl<DeviceEstablishmentFormRawValue['consecutiveDevice']>;
  establishmentId: FormControl<DeviceEstablishmentFormRawValue['establishmentId']>;
  negativeAward: FormControl<DeviceEstablishmentFormRawValue['negativeAward']>;
  device: FormControl<DeviceEstablishmentFormRawValue['device']>;
};

export type DeviceEstablishmentFormGroup = FormGroup<DeviceEstablishmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceEstablishmentFormService {
  createDeviceEstablishmentFormGroup(deviceEstablishment: DeviceEstablishmentFormGroupInput = { id: null }): DeviceEstablishmentFormGroup {
    const deviceEstablishmentRawValue = this.convertDeviceEstablishmentToDeviceEstablishmentRawValue({
      ...this.getFormDefaults(),
      ...deviceEstablishment,
    });
    return new FormGroup<DeviceEstablishmentFormGroupContent>({
      id: new FormControl(
        { value: deviceEstablishmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      registrationAt: new FormControl(deviceEstablishmentRawValue.registrationAt, {
        validators: [Validators.required],
      }),
      departureAt: new FormControl(deviceEstablishmentRawValue.departureAt),
      deviceNumber: new FormControl(deviceEstablishmentRawValue.deviceNumber),
      consecutiveDevice: new FormControl(deviceEstablishmentRawValue.consecutiveDevice),
      establishmentId: new FormControl(deviceEstablishmentRawValue.establishmentId),
      negativeAward: new FormControl(deviceEstablishmentRawValue.negativeAward),
      device: new FormControl(deviceEstablishmentRawValue.device),
    });
  }

  getDeviceEstablishment(form: DeviceEstablishmentFormGroup): IDeviceEstablishment | NewDeviceEstablishment {
    return this.convertDeviceEstablishmentRawValueToDeviceEstablishment(
      form.getRawValue() as DeviceEstablishmentFormRawValue | NewDeviceEstablishmentFormRawValue
    );
  }

  resetForm(form: DeviceEstablishmentFormGroup, deviceEstablishment: DeviceEstablishmentFormGroupInput): void {
    const deviceEstablishmentRawValue = this.convertDeviceEstablishmentToDeviceEstablishmentRawValue({
      ...this.getFormDefaults(),
      ...deviceEstablishment,
    });
    form.reset(
      {
        ...deviceEstablishmentRawValue,
        id: { value: deviceEstablishmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeviceEstablishmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      registrationAt: currentTime,
      departureAt: currentTime,
    };
  }

  private convertDeviceEstablishmentRawValueToDeviceEstablishment(
    rawDeviceEstablishment: DeviceEstablishmentFormRawValue | NewDeviceEstablishmentFormRawValue
  ): IDeviceEstablishment | NewDeviceEstablishment {
    return {
      ...rawDeviceEstablishment,
      registrationAt: dayjs(rawDeviceEstablishment.registrationAt, DATE_TIME_FORMAT),
      departureAt: dayjs(rawDeviceEstablishment.departureAt, DATE_TIME_FORMAT),
    };
  }

  private convertDeviceEstablishmentToDeviceEstablishmentRawValue(
    deviceEstablishment: IDeviceEstablishment | (Partial<NewDeviceEstablishment> & DeviceEstablishmentFormDefaults)
  ): DeviceEstablishmentFormRawValue | PartialWithRequiredKeyOf<NewDeviceEstablishmentFormRawValue> {
    return {
      ...deviceEstablishment,
      registrationAt: deviceEstablishment.registrationAt ? deviceEstablishment.registrationAt.format(DATE_TIME_FORMAT) : undefined,
      departureAt: deviceEstablishment.departureAt ? deviceEstablishment.departureAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
