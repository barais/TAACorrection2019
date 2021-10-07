import { ITask } from 'app/entities/task/task.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITODOList {
  id?: number;
  nom?: string | null;
  tasks?: ITask[] | null;
  user?: IUser | null;
}

export class TODOList implements ITODOList {
  constructor(public id?: number, public nom?: string | null, public tasks?: ITask[] | null, public user?: IUser | null) {}
}

export function getTODOListIdentifier(tODOList: ITODOList): number | undefined {
  return tODOList.id;
}
