import { IModel, NewModel } from './model.model';

export const sampleWithRequiredData: IModel = {
  id: 76257,
  code: 'Implementación navigate',
};

export const sampleWithPartialData: IModel = {
  id: 2306,
  code: 'withdrawal',
  name: 'Acero',
  collectionCeil: 16909,
  rolloverLimit: 33957,
};

export const sampleWithFullData: IModel = {
  id: 59836,
  code: 'Camboya Configuración',
  name: 'Ladrillo Berkshire Videojuegos',
  subtractBonus: true,
  collectionCeil: 37785,
  rolloverLimit: 1959,
};

export const sampleWithNewData: NewModel = {
  code: 'Tunisian Hormigon Loan',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
