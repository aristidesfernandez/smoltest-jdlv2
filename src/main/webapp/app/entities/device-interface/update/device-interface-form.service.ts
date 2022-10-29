import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDeviceInterface, NewDeviceInterface } from '../device-interface.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeviceInterface for edit and NewDeviceInterfaceFormGroupInput for create.
 */
type DeviceInterfaceFormGroupInput = IDeviceInterface | PartialWithRequiredKeyOf<NewDeviceInterface>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDeviceInterface | NewDeviceInterface> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type DeviceInterfaceFormRawValue = FormValueOf<IDeviceInterface>;

type NewDeviceInterfaceFormRawValue = FormValueOf<NewDeviceInterface>;

type DeviceInterfaceFormDefaults = Pick<NewDeviceInterface, 'id' | 'startDate' | 'endDate'>;

type DeviceInterfaceFormGroupContent = {
  id: FormControl<DeviceInterfaceFormRawValue['id'] | NewDeviceInterface['id']>;
  startDate: FormControl<DeviceInterfaceFormRawValue['startDate']>;
  endDate: FormControl<DeviceInterfaceFormRawValue['endDate']>;
  establishmentId: FormControl<DeviceInterfaceFormRawValue['establishmentId']>;
  state: FormControl<DeviceInterfaceFormRawValue['state']>;
  device: FormControl<DeviceInterfaceFormRawValue['device']>;
  interfaceBoard: FormControl<DeviceInterfaceFormRawValue['interfaceBoard']>;
};

export type DeviceInterfaceFormGroup = FormGroup<DeviceInterfaceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceInterfaceFormService {
  createDeviceInterfaceFormGroup(deviceInterface: DeviceInterfaceFormGroupInput = { id: null }): DeviceInterfaceFormGroup {
    const deviceInterfaceRawValue = this.convertDeviceInterfaceToDeviceInterfaceRawValue({
      ...this.getFormDefaults(),
      ...deviceInterface,
    });
    return new FormGroup<DeviceInterfaceFormGroupContent>({
      id: new FormControl(
        { value: deviceInterfaceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(deviceInterfaceRawValue.startDate),
      endDate: new FormControl(deviceInterfaceRawValue.endDate),
      establishmentId: new FormControl(deviceInterfaceRawValue.establishmentId),
      state: new FormControl(deviceInterfaceRawValue.state),
      device: new FormControl(deviceInterfaceRawValue.device),
      interfaceBoard: new FormControl(deviceInterfaceRawValue.interfaceBoard),
    });
  }

  getDeviceInterface(form: DeviceInterfaceFormGroup): IDeviceInterface | NewDeviceInterface {
    return this.convertDeviceInterfaceRawValueToDeviceInterface(
      form.getRawValue() as DeviceInterfaceFormRawValue | NewDeviceInterfaceFormRawValue
    );
  }

  resetForm(form: DeviceInterfaceFormGroup, deviceInterface: DeviceInterfaceFormGroupInput): void {
    const deviceInterfaceRawValue = this.convertDeviceInterfaceToDeviceInterfaceRawValue({ ...this.getFormDefaults(), ...deviceInterface });
    form.reset(
      {
        ...deviceInterfaceRawValue,
        id: { value: deviceInterfaceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeviceInterfaceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertDeviceInterfaceRawValueToDeviceInterface(
    rawDeviceInterface: DeviceInterfaceFormRawValue | NewDeviceInterfaceFormRawValue
  ): IDeviceInterface | NewDeviceInterface {
    return {
      ...rawDeviceInterface,
      startDate: dayjs(rawDeviceInterface.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawDeviceInterface.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertDeviceInterfaceToDeviceInterfaceRawValue(
    deviceInterface: IDeviceInterface | (Partial<NewDeviceInterface> & DeviceInterfaceFormDefaults)
  ): DeviceInterfaceFormRawValue | PartialWithRequiredKeyOf<NewDeviceInterfaceFormRawValue> {
    return {
      ...deviceInterface,
      startDate: deviceInterface.startDate ? deviceInterface.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: deviceInterface.endDate ? deviceInterface.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
