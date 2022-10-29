export interface IDeviceType {
  id: number;
  description?: string | null;
  name?: string | null;
}

export type NewDeviceType = Omit<IDeviceType, 'id'> & { id: null };
