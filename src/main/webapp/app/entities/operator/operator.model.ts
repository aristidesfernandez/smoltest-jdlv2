import dayjs from 'dayjs/esm';
import { IMunicipality } from 'app/entities/municipality/municipality.model';

export interface IOperator {
  id: number;
  permitDescription?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  nit?: string | null;
  contractNumber?: string | null;
  companyName?: string | null;
  brand?: string | null;
  municipality?: Pick<IMunicipality, 'id'> | null;
}

export type NewOperator = Omit<IOperator, 'id'> & { id: null };
