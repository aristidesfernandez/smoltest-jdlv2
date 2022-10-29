import { IEventDevice } from 'app/entities/event-device/event-device.model';

export interface ICounterEvent {
  id: string;
  valueCounter?: number | null;
  denominationSale?: number | null;
  counterCode?: string | null;
  eventDevice?: Pick<IEventDevice, 'id'> | null;
}

export type NewCounterEvent = Omit<ICounterEvent, 'id'> & { id: null };
