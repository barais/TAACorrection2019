import { ITask } from 'app/entities/task/task.model';

export interface ITag {
  id?: number;
  nom?: string | null;
  tasks?: ITask[] | null;
}

export class Tag implements ITag {
  constructor(public id?: number, public nom?: string | null, public tasks?: ITask[] | null) {}
}

export function getTagIdentifier(tag: ITag): number | undefined {
  return tag.id;
}
