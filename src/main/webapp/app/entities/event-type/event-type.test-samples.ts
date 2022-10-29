import { IEventType, NewEventType } from './event-type.model';

export const sampleWithRequiredData: IEventType = {
  id: 47,
  eventCode: 'Plástico Omán Buckin',
};

export const sampleWithPartialData: IEventType = {
  id: 14111,
  eventCode: 'Luxemburgo Malawi na',
  description: 'Checking generating',
  isPriority: false,
  isAlarm: true,
};

export const sampleWithFullData: IEventType = {
  id: 32374,
  eventCode: 'Gerente USB Asociado',
  sasCode: 'Bedfordshire',
  description: '24/365 Inverso',
  isStorable: true,
  isPriority: false,
  procesador: 'Navarra',
  isAlarm: false,
};

export const sampleWithNewData: NewEventType = {
  eventCode: 'deposit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
