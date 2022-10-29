import { IInterfaceBoard, NewInterfaceBoard } from './interface-board.model';

export const sampleWithRequiredData: IInterfaceBoard = {
  id: 42516,
};

export const sampleWithPartialData: IInterfaceBoard = {
  id: 66731,
  isAssigned: false,
  ipAddress: 'Platinum',
  hash: 'Hormigon',
  serial: 'collaborative Loan',
  version: 'Arquitecto',
};

export const sampleWithFullData: IInterfaceBoard = {
  id: 29990,
  isAssigned: true,
  ipAddress: 'empower generating',
  hash: 'Cuentas',
  serial: 'partnerships Perseverando Bulgarian',
  version: 'engage met',
  port: 'y Cantabri',
};

export const sampleWithNewData: NewInterfaceBoard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
