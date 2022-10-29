import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 4746,
};

export const sampleWithPartialData: ICountry = {
  id: 1891,
  code: 'SMS Sabroso Atún',
  name: 'niches panel',
  identifier: 'e-tailers',
};

export const sampleWithFullData: ICountry = {
  id: 23203,
  code: 'Raton Profundo',
  name: 'Uzbekistan La',
  identifier: 'Canarias open-source',
};

export const sampleWithNewData: NewCountry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
