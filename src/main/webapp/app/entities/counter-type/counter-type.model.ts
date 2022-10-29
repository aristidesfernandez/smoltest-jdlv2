export interface ICounterType {
  counterCode: string;
  name?: string | null;
  description?: string | null;
  includedInFormula?: boolean | null;
  prize?: boolean | null;
  category?: string | null;
  udteWaitTime?: number | null;
}

export type NewCounterType = Omit<ICounterType, 'counterCode'> & { counterCode: null };
