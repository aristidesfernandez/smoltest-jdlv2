export interface IIsle {
  id: number;
  description?: string | null;
  name?: string | null;
}

export type NewIsle = Omit<IIsle, 'id'> & { id: null };
