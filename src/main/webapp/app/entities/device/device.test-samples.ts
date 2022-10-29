import dayjs from 'dayjs/esm';

import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 'a39fe1a8-1d4c-48c3-8769-909544f92fcd',
  serial: 'quantifying Integración',
};

export const sampleWithPartialData: IDevice = {
  id: '83537444-d8a0-4e04-abed-4543615721b4',
  serial: 'Técnico',
  sasDenomination: 33193,
  isMultiDenomination: false,
  isRetanqueo: false,
  sasIdentifier: 14928,
  hasHooper: true,
  currentToken: 68073,
  denominationTito: 27310,
  startLostCommunication: dayjs('2021-09-27T10:18'),
  nuid: 'encriptar',
  manualHandpay: false,
  manualGameEvent: true,
  betCode: 'connecting',
  coljuegosModel: 'Morado enable nueva',
  reportable: false,
  aftDenomination: 33325,
  enableRollover: false,
  lastCorruptionDate: dayjs('2021-09-27T18:04'),
  daftDenomination: 55989,
};

export const sampleWithFullData: IDevice = {
  id: '8a0c1ec8-3337-4931-9f8f-b776e61730e5',
  serial: 'Violeta Coche',
  isProtocolEsdcs: true,
  numberPlayedReport: 85318,
  sasDenomination: 64197,
  isMultigame: false,
  isMultiDenomination: true,
  isRetanqueo: true,
  state: 'Berkshire',
  theoreticalHold: 41255,
  sasIdentifier: 97815,
  creditLimit: 40163,
  hasHooper: false,
  coljuegosCode: 'FTP',
  fabricationDate: dayjs('2021-09-27'),
  currentToken: 48171,
  denominationTito: 58873,
  endLostCommunication: dayjs('2021-09-27T17:04'),
  startLostCommunication: dayjs('2021-09-27T02:46'),
  reportMultiplier: 84444,
  nuid: 'mobile',
  payManualPrize: false,
  manualHandpay: true,
  manualJackpot: false,
  manualGameEvent: false,
  betCode: 'Omán Vía',
  homologationIndicator: false,
  coljuegosModel: 'Ergonómico Acero',
  reportable: true,
  aftDenomination: 36782,
  lastUpdateDate: dayjs('2021-09-27T01:36'),
  enableRollover: false,
  lastCorruptionDate: dayjs('2021-09-26T23:30'),
  daftDenomination: 53954,
  prizesEnabled: true,
  currencyTypeId: 97763,
  isleId: 8209,
};

export const sampleWithNewData: NewDevice = {
  serial: 'GB',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
