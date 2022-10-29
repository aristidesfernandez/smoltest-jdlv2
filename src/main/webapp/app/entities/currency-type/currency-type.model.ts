export interface ICurrencyType {
  id: number;
  description?: string | null;
  name?: string | null;
  isPriority?: boolean | null;
  location?: string | null;
  exchangeRate?: number | null;
}

export type NewCurrencyType = Omit<ICurrencyType, 'id'> & { id: null };
