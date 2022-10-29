export interface IManufacturer {
  id: number;
  code?: string | null;
  name?: string | null;
}

export type NewManufacturer = Omit<IManufacturer, 'id'> & { id: null };
