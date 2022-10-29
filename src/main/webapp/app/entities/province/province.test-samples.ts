import { IProvince, NewProvince } from './province.model';

export const sampleWithRequiredData: IProvince = {
  id: 16991,
};

export const sampleWithPartialData: IProvince = {
  id: 37992,
  code: 'Account program JBOD',
  daneCode: 'value-added',
};

export const sampleWithFullData: IProvince = {
  id: 73242,
  code: 'Verde',
  name: 'Monte',
  daneCode: 'Fantástico',
  phoneId: 'eyeballs Canada',
};

export const sampleWithNewData: NewProvince = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
