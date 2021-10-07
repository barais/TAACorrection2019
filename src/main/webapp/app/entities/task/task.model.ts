import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ITag } from 'app/entities/tag/tag.model';
import { ITODOList } from 'app/entities/todo-list/todo-list.model';
import { RepetitionType } from 'app/entities/enumerations/repetition-type.model';

export interface ITask {
  id?: number;
  libelle?: string | null;
  dateButoire?: dayjs.Dayjs | null;
  repetition?: RepetitionType | null;
  dureeEstime?: string | null;
  lieu?: string | null;
  url?: string | null;
  description?: string | null;
  affecteduser?: IUser | null;
  tags?: ITag[] | null;
  todolist?: ITODOList | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public dateButoire?: dayjs.Dayjs | null,
    public repetition?: RepetitionType | null,
    public dureeEstime?: string | null,
    public lieu?: string | null,
    public url?: string | null,
    public description?: string | null,
    public affecteduser?: IUser | null,
    public tags?: ITag[] | null,
    public todolist?: ITODOList | null
  ) {}
}

export function getTaskIdentifier(task: ITask): number | undefined {
  return task.id;
}
