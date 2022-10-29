import dayjs from 'dayjs/esm';

import { EstablishmentType } from 'app/entities/enumerations/establishment-type.model';

import { IEstablishment, NewEstablishment } from './establishment.model';

export const sampleWithRequiredData: IEstablishment = {
  id: 60820,
};

export const sampleWithPartialData: IEstablishment = {
  id: 78725,
  name: 'Acero parsing',
  address: 'Metal',
  coljuegosCode: 'homogénea hack Relacciones',
  startTime: dayjs('2021-09-27T11:34'),
  mercantileRegistration: 'Global deposit',
};

export const sampleWithFullData: IEstablishment = {
  id: 49162,
  liquidationTime: dayjs('2021-09-27T01:27'),
  name: 'éxito',
  type: EstablishmentType['CASINO'],
  neighborhood: 'Asistente Rústico',
  address: 'Puerta bus',
  coljuegosCode: 'Acero Videojuegos capacitor',
  startTime: dayjs('2021-09-27T03:20'),
  closeTime: dayjs('2021-09-27T05:36'),
  longitude: 82204,
  latitude: 13414,
  mercantileRegistration: 'Central',
};

export const sampleWithNewData: NewEstablishment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
