export interface IInterfaceBoard {
  id: number;
  isAssigned?: boolean | null;
  ipAddress?: string | null;
  hash?: string | null;
  serial?: string | null;
  version?: string | null;
  port?: string | null;
}

export type NewInterfaceBoard = Omit<IInterfaceBoard, 'id'> & { id: null };
