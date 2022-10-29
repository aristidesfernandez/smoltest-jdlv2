import dayjs from 'dayjs/esm';
import { IOperator } from 'app/entities/operator/operator.model';
import { IMunicipality } from 'app/entities/municipality/municipality.model';
import { EstablishmentType } from 'app/entities/enumerations/establishment-type.model';

export interface IEstablishment {
  id: number;
  liquidationTime?: dayjs.Dayjs | null;
  name?: string | null;
  type?: EstablishmentType | null;
  neighborhood?: string | null;
  address?: string | null;
  coljuegosCode?: string | null;
  startTime?: dayjs.Dayjs | null;
  closeTime?: dayjs.Dayjs | null;
  longitude?: number | null;
  latitude?: number | null;
  mercantileRegistration?: string | null;
  operator?: Pick<IOperator, 'id'> | null;
  municipality?: Pick<IMunicipality, 'id'> | null;
}

export type NewEstablishment = Omit<IEstablishment, 'id'> & { id: null };
