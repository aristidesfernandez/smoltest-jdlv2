import dayjs from 'dayjs/esm';

import { IOperator, NewOperator } from './operator.model';

export const sampleWithRequiredData: IOperator = {
  id: 23970,
};

export const sampleWithPartialData: IOperator = {
  id: 94989,
  permitDescription: 'Zapatos Paradigma País',
  startDate: dayjs('2021-09-27T05:04'),
  nit: 'deposit Oficial',
  companyName: 'arquitectura Adelante invoice',
};

export const sampleWithFullData: IOperator = {
  id: 17599,
  permitDescription: 'Granito Account',
  startDate: dayjs('2021-09-27T10:36'),
  endDate: dayjs('2021-09-27T09:29'),
  nit: 'withdrawal Metal',
  contractNumber: 'Borders Tunisian SCSI',
  companyName: 'Sección Plástico Estratega',
  brand: 'Rojo',
};

export const sampleWithNewData: NewOperator = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
