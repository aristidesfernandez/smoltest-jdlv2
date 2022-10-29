export interface IKeyOperatingProperty {
  id: number;
  description?: string | null;
  property?: string | null;
}

export type NewKeyOperatingProperty = Omit<IKeyOperatingProperty, 'id'> & { id: null };
