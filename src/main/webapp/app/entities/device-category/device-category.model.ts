export interface IDeviceCategory {
  id: number;
  description?: string | null;
  name?: string | null;
}

export type NewDeviceCategory = Omit<IDeviceCategory, 'id'> & { id: null };
