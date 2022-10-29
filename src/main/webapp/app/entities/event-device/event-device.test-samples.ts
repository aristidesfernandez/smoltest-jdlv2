import dayjs from 'dayjs/esm';

import { IEventDevice, NewEventDevice } from './event-device.model';

export const sampleWithRequiredData: IEventDevice = {
  id: 'ca3b2a8e-a0f6-4789-abd5-1557a2d72303',
};

export const sampleWithPartialData: IEventDevice = {
  id: 'eed6a874-1aba-4ac6-9148-2b4b7a45c4e6',
  theoreticalPercentage: true,
};

export const sampleWithFullData: IEventDevice = {
  id: 'd8f801ab-d6de-4278-9a11-90d234ce8dea',
  createdAt: dayjs('2021-09-27T04:31'),
  theoreticalPercentage: true,
  moneyDenomination: 84129,
};

export const sampleWithNewData: NewEventDevice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
