import { IProvince } from 'app/entities/province/province.model';

export interface IMunicipality {
  id: number;
  code?: string | null;
  name?: string | null;
  daneCode?: string | null;
  province?: Pick<IProvince, 'id'> | null;
}

export type NewMunicipality = Omit<IMunicipality, 'id'> & { id: null };
