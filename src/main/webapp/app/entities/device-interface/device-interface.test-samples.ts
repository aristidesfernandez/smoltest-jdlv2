import dayjs from 'dayjs/esm';

import { DeviceInterfaceStatus } from 'app/entities/enumerations/device-interface-status.model';

import { IDeviceInterface, NewDeviceInterface } from './device-interface.model';

export const sampleWithRequiredData: IDeviceInterface = {
  id: 63711,
};

export const sampleWithPartialData: IDeviceInterface = {
  id: 20017,
  state: DeviceInterfaceStatus['MAINTENANCE'],
};

export const sampleWithFullData: IDeviceInterface = {
  id: 62776,
  startDate: dayjs('2021-09-27T14:51'),
  endDate: dayjs('2021-09-27T12:42'),
  establishmentId: 62402,
  state: DeviceInterfaceStatus['OPERATION'],
};

export const sampleWithNewData: NewDeviceInterface = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
