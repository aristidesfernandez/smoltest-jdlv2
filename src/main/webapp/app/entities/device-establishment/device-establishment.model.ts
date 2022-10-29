import dayjs from 'dayjs/esm';
import { IDevice } from 'app/entities/device/device.model';

export interface IDeviceEstablishment {
  id: string;
  registrationAt?: dayjs.Dayjs | null;
  departureAt?: dayjs.Dayjs | null;
  deviceNumber?: number | null;
  consecutiveDevice?: number | null;
  establishmentId?: number | null;
  negativeAward?: number | null;
  device?: Pick<IDevice, 'id'> | null;
}

export type NewDeviceEstablishment = Omit<IDeviceEstablishment, 'id'> & { id: null };
