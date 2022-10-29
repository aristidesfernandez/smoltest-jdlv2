import dayjs from 'dayjs/esm';

import { IDeviceEstablishment, NewDeviceEstablishment } from './device-establishment.model';

export const sampleWithRequiredData: IDeviceEstablishment = {
  id: '5f2b14c6-5ce0-4e5a-96e3-eb86c8fcb387',
  registrationAt: dayjs('2021-09-27T05:56'),
};

export const sampleWithPartialData: IDeviceEstablishment = {
  id: '0642c598-1dc3-4f54-bcd6-36aefafaaeb6',
  registrationAt: dayjs('2021-09-27T02:31'),
  departureAt: dayjs('2021-09-27T04:19'),
  deviceNumber: 19406,
  consecutiveDevice: 65257,
  negativeAward: 56157,
};

export const sampleWithFullData: IDeviceEstablishment = {
  id: '238b0243-e230-4e88-a656-b1dad5bfa2a6',
  registrationAt: dayjs('2021-09-27T01:49'),
  departureAt: dayjs('2021-09-27T03:43'),
  deviceNumber: 18418,
  consecutiveDevice: 66245,
  establishmentId: 47625,
  negativeAward: 51116,
};

export const sampleWithNewData: NewDeviceEstablishment = {
  registrationAt: dayjs('2021-09-27T07:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
