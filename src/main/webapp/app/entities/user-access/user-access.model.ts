import dayjs from 'dayjs/esm';

export interface IUserAccess {
  id: number;
  username?: string | null;
  ipAddress?: string | null;
  registrationAt?: dayjs.Dayjs | null;
}

export type NewUserAccess = Omit<IUserAccess, 'id'> & { id: null };
