import dayjs from 'dayjs/esm';
import { IDevice } from 'app/entities/device/device.model';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { DeviceInterfaceStatus } from 'app/entities/enumerations/device-interface-status.model';

export interface IDeviceInterface {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  establishmentId?: number | null;
  state?: DeviceInterfaceStatus | null;
  device?: Pick<IDevice, 'id'> | null;
  interfaceBoard?: Pick<IInterfaceBoard, 'id'> | null;
}

export type NewDeviceInterface = Omit<IDeviceInterface, 'id'> & { id: null };
