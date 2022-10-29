import { ICurrencyType, NewCurrencyType } from './currency-type.model';

export const sampleWithRequiredData: ICurrencyType = {
  id: 51685,
};

export const sampleWithPartialData: ICurrencyType = {
  id: 80143,
  name: 'Account haptic',
  location: 'synthesize monitorizada Navarra',
};

export const sampleWithFullData: ICurrencyType = {
  id: 73043,
  description: 'modular global withdrawal',
  name: 'Pescado Ladrillo',
  isPriority: true,
  location: 'Berkshire experiences payment',
  exchangeRate: 42039,
};

export const sampleWithNewData: NewCurrencyType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
