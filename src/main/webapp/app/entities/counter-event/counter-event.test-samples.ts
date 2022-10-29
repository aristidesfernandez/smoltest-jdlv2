import { ICounterEvent, NewCounterEvent } from './counter-event.model';

export const sampleWithRequiredData: ICounterEvent = {
  id: 'e5ec77b4-4583-4624-b6f7-a80189da1543',
  counterCode: 'Ad',
};

export const sampleWithPartialData: ICounterEvent = {
  id: '2d1bfa9b-ede2-4dad-ba07-75a9b03b775f',
  counterCode: 'in',
};

export const sampleWithFullData: ICounterEvent = {
  id: '4fa3a726-24f9-467f-ac48-d4249184e419',
  valueCounter: 29195,
  denominationSale: 72733,
  counterCode: 'Na',
};

export const sampleWithNewData: NewCounterEvent = {
  counterCode: 'Te',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
