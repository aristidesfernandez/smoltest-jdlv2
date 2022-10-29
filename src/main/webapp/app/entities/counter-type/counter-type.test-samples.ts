import { ICounterType, NewCounterType } from './counter-type.model';

export const sampleWithRequiredData: ICounterType = {
  counterCode: '1f',
};

export const sampleWithPartialData: ICounterType = {
  counterCode: '9a',
  description: 'a Rústico',
  includedInFormula: true,
  prize: false,
};

export const sampleWithFullData: ICounterType = {
  counterCode: '55',
  name: 'Guapa Director',
  description: 'definición Organizado',
  includedInFormula: false,
  prize: true,
  category: 'Negro Fiji',
  udteWaitTime: 11807,
};

export const sampleWithNewData: NewCounterType = {
  counterCode: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
