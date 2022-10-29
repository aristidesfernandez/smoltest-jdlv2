import { IFormula, NewFormula } from './formula.model';

export const sampleWithRequiredData: IFormula = {
  id: 82237,
};

export const sampleWithPartialData: IFormula = {
  id: 50121,
  description: 'Hormigon invoice bypassing',
  expression: 'Colegio Carretera connecting',
};

export const sampleWithFullData: IFormula = {
  id: 75217,
  description: 'digital navigating Plástico',
  expression: 'Queso Integración Nuevo',
};

export const sampleWithNewData: NewFormula = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
