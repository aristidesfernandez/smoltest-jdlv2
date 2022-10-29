import { IOperationalPropertiesEstablishment, NewOperationalPropertiesEstablishment } from './operational-properties-establishment.model';

export const sampleWithRequiredData: IOperationalPropertiesEstablishment = {
  id: 48879,
};

export const sampleWithPartialData: IOperationalPropertiesEstablishment = {
  id: 89132,
};

export const sampleWithFullData: IOperationalPropertiesEstablishment = {
  id: 44972,
  value: 'Soluciones wireless Enfocado',
};

export const sampleWithNewData: NewOperationalPropertiesEstablishment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
