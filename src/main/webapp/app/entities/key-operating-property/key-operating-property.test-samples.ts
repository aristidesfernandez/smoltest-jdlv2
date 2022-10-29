import { IKeyOperatingProperty, NewKeyOperatingProperty } from './key-operating-property.model';

export const sampleWithRequiredData: IKeyOperatingProperty = {
  id: 13083,
};

export const sampleWithPartialData: IKeyOperatingProperty = {
  id: 87171,
  description: 'payment Checking',
};

export const sampleWithFullData: IKeyOperatingProperty = {
  id: 18278,
  description: 'Bolivia one-to-one Gris',
  property: 'Baleares Real',
};

export const sampleWithNewData: NewKeyOperatingProperty = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
