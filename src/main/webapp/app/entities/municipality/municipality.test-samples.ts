import { IMunicipality, NewMunicipality } from './municipality.model';

export const sampleWithRequiredData: IMunicipality = {
  id: 14326,
  daneCode: 'Buckinghamshire firewall ',
};

export const sampleWithPartialData: IMunicipality = {
  id: 11819,
  name: 'Buckinghamshire',
  daneCode: 'Auto mobile Cataluña',
};

export const sampleWithFullData: IMunicipality = {
  id: 68715,
  code: 'JSON',
  name: 'transparent Cambridgeshire Algodón',
  daneCode: 'Account Amarillo Música',
};

export const sampleWithNewData: NewMunicipality = {
  daneCode: 'driver TCP platforms',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
