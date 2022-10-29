import dayjs from 'dayjs/esm';

import { IUserAccess, NewUserAccess } from './user-access.model';

export const sampleWithRequiredData: IUserAccess = {
  id: 32172,
  username: 'multitarea',
  ipAddress: 'synthesize acceso Ge',
  registrationAt: dayjs('2021-09-27T02:01'),
};

export const sampleWithPartialData: IUserAccess = {
  id: 32850,
  username: 'index Bricolaje',
  ipAddress: 'Plástico',
  registrationAt: dayjs('2021-09-27T11:42'),
};

export const sampleWithFullData: IUserAccess = {
  id: 16888,
  username: 'strategic',
  ipAddress: 'Realineado',
  registrationAt: dayjs('2021-09-26T23:59'),
};

export const sampleWithNewData: NewUserAccess = {
  username: 'Usabilidad withdrawal',
  ipAddress: 'Algodón mobile',
  registrationAt: dayjs('2021-09-27T18:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
