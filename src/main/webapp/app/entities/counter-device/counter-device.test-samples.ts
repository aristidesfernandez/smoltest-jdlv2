import { ICounterDevice, NewCounterDevice } from './counter-device.model';

export const sampleWithRequiredData: ICounterDevice = {
  id: '6e035136-d1d8-4ded-bce8-17a93527eb8b',
};

export const sampleWithPartialData: ICounterDevice = {
  id: '3713eb56-ddb1-49cc-997d-a3c2be46b041',
  value: 20449,
  rolloverValue: 71295,
  manualCounter: true,
};

export const sampleWithFullData: ICounterDevice = {
  id: '71b60fb5-9c4f-4975-bf3d-767b6ae96090',
  value: 41117,
  rolloverValue: 63668,
  creditSale: 15052,
  manualCounter: false,
  manualMultiplier: 45470,
  decimalsManualCounter: true,
};

export const sampleWithNewData: NewCounterDevice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
