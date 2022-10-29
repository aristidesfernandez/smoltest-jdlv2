import { IManufacturer, NewManufacturer } from './manufacturer.model';

export const sampleWithRequiredData: IManufacturer = {
  id: 6644,
};

export const sampleWithPartialData: IManufacturer = {
  id: 15999,
  code: 'Paradigma',
  name: 'circuit',
};

export const sampleWithFullData: IManufacturer = {
  id: 91963,
  code: 'Soluciones cero Sabroso',
  name: 'Hormigon 24/7 a',
};

export const sampleWithNewData: NewManufacturer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
