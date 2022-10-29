import { IIsle, NewIsle } from './isle.model';

export const sampleWithRequiredData: IIsle = {
  id: 89248,
};

export const sampleWithPartialData: IIsle = {
  id: 38906,
};

export const sampleWithFullData: IIsle = {
  id: 72556,
  description: 'payment rich Madera',
  name: 'Verde',
};

export const sampleWithNewData: NewIsle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
