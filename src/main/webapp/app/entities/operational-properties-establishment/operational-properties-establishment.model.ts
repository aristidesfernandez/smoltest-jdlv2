import { IKeyOperatingProperty } from 'app/entities/key-operating-property/key-operating-property.model';
import { IEstablishment } from 'app/entities/establishment/establishment.model';

export interface IOperationalPropertiesEstablishment {
  id: number;
  value?: string | null;
  keyOperatingProperty?: Pick<IKeyOperatingProperty, 'id'> | null;
  establishment?: Pick<IEstablishment, 'id'> | null;
}

export type NewOperationalPropertiesEstablishment = Omit<IOperationalPropertiesEstablishment, 'id'> & { id: null };
