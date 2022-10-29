import { IDeviceType, NewDeviceType } from './device-type.model';

export const sampleWithRequiredData: IDeviceType = {
  id: 87535,
};

export const sampleWithPartialData: IDeviceType = {
  id: 12932,
  description: 'Inversor SMTP',
};

export const sampleWithFullData: IDeviceType = {
  id: 17114,
  description: 'Berkshire Salud',
  name: 'Shilling Peso',
};

export const sampleWithNewData: NewDeviceType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
