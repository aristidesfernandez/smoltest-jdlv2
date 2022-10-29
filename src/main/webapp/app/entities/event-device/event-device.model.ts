import dayjs from 'dayjs/esm';
import { IDeviceEstablishment } from 'app/entities/device-establishment/device-establishment.model';
import { IEventType } from 'app/entities/event-type/event-type.model';

export interface IEventDevice {
  id: string;
  createdAt?: dayjs.Dayjs | null;
  theoreticalPercentage?: boolean | null;
  moneyDenomination?: number | null;
  deviceEstablishment?: Pick<IDeviceEstablishment, 'id'> | null;
  eventType?: Pick<IEventType, 'id'> | null;
}

export type NewEventDevice = Omit<IEventDevice, 'id'> & { id: null };
