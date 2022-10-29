import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDevice, NewDevice } from '../device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDevice for edit and NewDeviceFormGroupInput for create.
 */
type DeviceFormGroupInput = IDevice | PartialWithRequiredKeyOf<NewDevice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDevice | NewDevice> = Omit<
  T,
  'endLostCommunication' | 'startLostCommunication' | 'lastUpdateDate' | 'lastCorruptionDate'
> & {
  endLostCommunication?: string | null;
  startLostCommunication?: string | null;
  lastUpdateDate?: string | null;
  lastCorruptionDate?: string | null;
};

type DeviceFormRawValue = FormValueOf<IDevice>;

type NewDeviceFormRawValue = FormValueOf<NewDevice>;

type DeviceFormDefaults = Pick<
  NewDevice,
  | 'id'
  | 'isProtocolEsdcs'
  | 'isMultigame'
  | 'isMultiDenomination'
  | 'isRetanqueo'
  | 'hasHooper'
  | 'endLostCommunication'
  | 'startLostCommunication'
  | 'payManualPrize'
  | 'manualHandpay'
  | 'manualJackpot'
  | 'manualGameEvent'
  | 'homologationIndicator'
  | 'reportable'
  | 'lastUpdateDate'
  | 'enableRollover'
  | 'lastCorruptionDate'
  | 'prizesEnabled'
>;

type DeviceFormGroupContent = {
  id: FormControl<DeviceFormRawValue['id'] | NewDevice['id']>;
  serial: FormControl<DeviceFormRawValue['serial']>;
  isProtocolEsdcs: FormControl<DeviceFormRawValue['isProtocolEsdcs']>;
  numberPlayedReport: FormControl<DeviceFormRawValue['numberPlayedReport']>;
  sasDenomination: FormControl<DeviceFormRawValue['sasDenomination']>;
  isMultigame: FormControl<DeviceFormRawValue['isMultigame']>;
  isMultiDenomination: FormControl<DeviceFormRawValue['isMultiDenomination']>;
  isRetanqueo: FormControl<DeviceFormRawValue['isRetanqueo']>;
  state: FormControl<DeviceFormRawValue['state']>;
  theoreticalHold: FormControl<DeviceFormRawValue['theoreticalHold']>;
  sasIdentifier: FormControl<DeviceFormRawValue['sasIdentifier']>;
  creditLimit: FormControl<DeviceFormRawValue['creditLimit']>;
  hasHooper: FormControl<DeviceFormRawValue['hasHooper']>;
  coljuegosCode: FormControl<DeviceFormRawValue['coljuegosCode']>;
  fabricationDate: FormControl<DeviceFormRawValue['fabricationDate']>;
  currentToken: FormControl<DeviceFormRawValue['currentToken']>;
  denominationTito: FormControl<DeviceFormRawValue['denominationTito']>;
  endLostCommunication: FormControl<DeviceFormRawValue['endLostCommunication']>;
  startLostCommunication: FormControl<DeviceFormRawValue['startLostCommunication']>;
  reportMultiplier: FormControl<DeviceFormRawValue['reportMultiplier']>;
  nuid: FormControl<DeviceFormRawValue['nuid']>;
  payManualPrize: FormControl<DeviceFormRawValue['payManualPrize']>;
  manualHandpay: FormControl<DeviceFormRawValue['manualHandpay']>;
  manualJackpot: FormControl<DeviceFormRawValue['manualJackpot']>;
  manualGameEvent: FormControl<DeviceFormRawValue['manualGameEvent']>;
  betCode: FormControl<DeviceFormRawValue['betCode']>;
  homologationIndicator: FormControl<DeviceFormRawValue['homologationIndicator']>;
  coljuegosModel: FormControl<DeviceFormRawValue['coljuegosModel']>;
  reportable: FormControl<DeviceFormRawValue['reportable']>;
  aftDenomination: FormControl<DeviceFormRawValue['aftDenomination']>;
  lastUpdateDate: FormControl<DeviceFormRawValue['lastUpdateDate']>;
  enableRollover: FormControl<DeviceFormRawValue['enableRollover']>;
  lastCorruptionDate: FormControl<DeviceFormRawValue['lastCorruptionDate']>;
  daftDenomination: FormControl<DeviceFormRawValue['daftDenomination']>;
  prizesEnabled: FormControl<DeviceFormRawValue['prizesEnabled']>;
  currencyTypeId: FormControl<DeviceFormRawValue['currencyTypeId']>;
  isleId: FormControl<DeviceFormRawValue['isleId']>;
  model: FormControl<DeviceFormRawValue['model']>;
  deviceCategory: FormControl<DeviceFormRawValue['deviceCategory']>;
  deviceType: FormControl<DeviceFormRawValue['deviceType']>;
  formulaHandpay: FormControl<DeviceFormRawValue['formulaHandpay']>;
  formulaJackpot: FormControl<DeviceFormRawValue['formulaJackpot']>;
};

export type DeviceFormGroup = FormGroup<DeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceFormService {
  createDeviceFormGroup(device: DeviceFormGroupInput = { id: null }): DeviceFormGroup {
    const deviceRawValue = this.convertDeviceToDeviceRawValue({
      ...this.getFormDefaults(),
      ...device,
    });
    return new FormGroup<DeviceFormGroupContent>({
      id: new FormControl(
        { value: deviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      serial: new FormControl(deviceRawValue.serial, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      isProtocolEsdcs: new FormControl(deviceRawValue.isProtocolEsdcs),
      numberPlayedReport: new FormControl(deviceRawValue.numberPlayedReport),
      sasDenomination: new FormControl(deviceRawValue.sasDenomination),
      isMultigame: new FormControl(deviceRawValue.isMultigame),
      isMultiDenomination: new FormControl(deviceRawValue.isMultiDenomination),
      isRetanqueo: new FormControl(deviceRawValue.isRetanqueo),
      state: new FormControl(deviceRawValue.state, {
        validators: [Validators.maxLength(30)],
      }),
      theoreticalHold: new FormControl(deviceRawValue.theoreticalHold),
      sasIdentifier: new FormControl(deviceRawValue.sasIdentifier),
      creditLimit: new FormControl(deviceRawValue.creditLimit),
      hasHooper: new FormControl(deviceRawValue.hasHooper),
      coljuegosCode: new FormControl(deviceRawValue.coljuegosCode, {
        validators: [Validators.maxLength(50)],
      }),
      fabricationDate: new FormControl(deviceRawValue.fabricationDate),
      currentToken: new FormControl(deviceRawValue.currentToken),
      denominationTito: new FormControl(deviceRawValue.denominationTito),
      endLostCommunication: new FormControl(deviceRawValue.endLostCommunication),
      startLostCommunication: new FormControl(deviceRawValue.startLostCommunication),
      reportMultiplier: new FormControl(deviceRawValue.reportMultiplier),
      nuid: new FormControl(deviceRawValue.nuid),
      payManualPrize: new FormControl(deviceRawValue.payManualPrize),
      manualHandpay: new FormControl(deviceRawValue.manualHandpay),
      manualJackpot: new FormControl(deviceRawValue.manualJackpot),
      manualGameEvent: new FormControl(deviceRawValue.manualGameEvent),
      betCode: new FormControl(deviceRawValue.betCode),
      homologationIndicator: new FormControl(deviceRawValue.homologationIndicator),
      coljuegosModel: new FormControl(deviceRawValue.coljuegosModel, {
        validators: [Validators.maxLength(50)],
      }),
      reportable: new FormControl(deviceRawValue.reportable),
      aftDenomination: new FormControl(deviceRawValue.aftDenomination),
      lastUpdateDate: new FormControl(deviceRawValue.lastUpdateDate),
      enableRollover: new FormControl(deviceRawValue.enableRollover),
      lastCorruptionDate: new FormControl(deviceRawValue.lastCorruptionDate),
      daftDenomination: new FormControl(deviceRawValue.daftDenomination),
      prizesEnabled: new FormControl(deviceRawValue.prizesEnabled),
      currencyTypeId: new FormControl(deviceRawValue.currencyTypeId),
      isleId: new FormControl(deviceRawValue.isleId),
      model: new FormControl(deviceRawValue.model, {
        validators: [Validators.required],
      }),
      deviceCategory: new FormControl(deviceRawValue.deviceCategory, {
        validators: [Validators.required],
      }),
      deviceType: new FormControl(deviceRawValue.deviceType, {
        validators: [Validators.required],
      }),
      formulaHandpay: new FormControl(deviceRawValue.formulaHandpay),
      formulaJackpot: new FormControl(deviceRawValue.formulaJackpot),
    });
  }

  getDevice(form: DeviceFormGroup): IDevice | NewDevice {
    return this.convertDeviceRawValueToDevice(form.getRawValue() as DeviceFormRawValue | NewDeviceFormRawValue);
  }

  resetForm(form: DeviceFormGroup, device: DeviceFormGroupInput): void {
    const deviceRawValue = this.convertDeviceToDeviceRawValue({ ...this.getFormDefaults(), ...device });
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeviceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isProtocolEsdcs: false,
      isMultigame: false,
      isMultiDenomination: false,
      isRetanqueo: false,
      hasHooper: false,
      endLostCommunication: currentTime,
      startLostCommunication: currentTime,
      payManualPrize: false,
      manualHandpay: false,
      manualJackpot: false,
      manualGameEvent: false,
      homologationIndicator: false,
      reportable: false,
      lastUpdateDate: currentTime,
      enableRollover: false,
      lastCorruptionDate: currentTime,
      prizesEnabled: false,
    };
  }

  private convertDeviceRawValueToDevice(rawDevice: DeviceFormRawValue | NewDeviceFormRawValue): IDevice | NewDevice {
    return {
      ...rawDevice,
      endLostCommunication: dayjs(rawDevice.endLostCommunication, DATE_TIME_FORMAT),
      startLostCommunication: dayjs(rawDevice.startLostCommunication, DATE_TIME_FORMAT),
      lastUpdateDate: dayjs(rawDevice.lastUpdateDate, DATE_TIME_FORMAT),
      lastCorruptionDate: dayjs(rawDevice.lastCorruptionDate, DATE_TIME_FORMAT),
    };
  }

  private convertDeviceToDeviceRawValue(
    device: IDevice | (Partial<NewDevice> & DeviceFormDefaults)
  ): DeviceFormRawValue | PartialWithRequiredKeyOf<NewDeviceFormRawValue> {
    return {
      ...device,
      endLostCommunication: device.endLostCommunication ? device.endLostCommunication.format(DATE_TIME_FORMAT) : undefined,
      startLostCommunication: device.startLostCommunication ? device.startLostCommunication.format(DATE_TIME_FORMAT) : undefined,
      lastUpdateDate: device.lastUpdateDate ? device.lastUpdateDate.format(DATE_TIME_FORMAT) : undefined,
      lastCorruptionDate: device.lastCorruptionDate ? device.lastCorruptionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
