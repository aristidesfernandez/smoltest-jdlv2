import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';
import { IFormula } from 'app/entities/formula/formula.model';

export interface IModel {
  id: number;
  code?: string | null;
  name?: string | null;
  subtractBonus?: boolean | null;
  collectionCeil?: number | null;
  rolloverLimit?: number | null;
  manufacturer?: Pick<IManufacturer, 'id'> | null;
  formula?: Pick<IFormula, 'id'> | null;
}

export type NewModel = Omit<IModel, 'id'> & { id: null };
