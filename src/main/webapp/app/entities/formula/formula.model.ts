export interface IFormula {
  id: number;
  description?: string | null;
  expression?: string | null;
}

export type NewFormula = Omit<IFormula, 'id'> & { id: null };
