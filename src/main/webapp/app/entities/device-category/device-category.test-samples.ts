import { IDeviceCategory, NewDeviceCategory } from './device-category.model';

export const sampleWithRequiredData: IDeviceCategory = {
  id: 37081,
};

export const sampleWithPartialData: IDeviceCategory = {
  id: 78389,
  description: '1080p Pasaje',
};

export const sampleWithFullData: IDeviceCategory = {
  id: 73631,
  description: 'Configuración',
  name: 'open-source Account Hryvnia',
};

export const sampleWithNewData: NewDeviceCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
