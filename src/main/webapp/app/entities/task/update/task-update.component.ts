import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITask, Task } from '../task.model';
import { TaskService } from '../service/task.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { ITODOList } from 'app/entities/todo-list/todo-list.model';
import { TODOListService } from 'app/entities/todo-list/service/todo-list.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  tagsSharedCollection: ITag[] = [];
  tODOListsSharedCollection: ITODOList[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    dateButoire: [],
    repetition: [],
    dureeEstime: [],
    lieu: [],
    url: [],
    description: [],
    affecteduser: [],
    tags: [],
    todolist: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected taskService: TaskService,
    protected userService: UserService,
    protected tagService: TagService,
    protected tODOListService: TODOListService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('correctionTaaApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTagById(index: number, item: ITag): number {
    return item.id!;
  }

  trackTODOListById(index: number, item: ITODOList): number {
    return item.id!;
  }

  getSelectedTag(option: ITag, selectedVals?: ITag[]): ITag {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      libelle: task.libelle,
      dateButoire: task.dateButoire,
      repetition: task.repetition,
      dureeEstime: task.dureeEstime,
      lieu: task.lieu,
      url: task.url,
      description: task.description,
      affecteduser: task.affecteduser,
      tags: task.tags,
      todolist: task.todolist,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, task.affecteduser);
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(task.tags ?? []));
    this.tODOListsSharedCollection = this.tODOListService.addTODOListToCollectionIfMissing(this.tODOListsSharedCollection, task.todolist);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('affecteduser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

    this.tODOListService
      .query()
      .pipe(map((res: HttpResponse<ITODOList[]>) => res.body ?? []))
      .pipe(
        map((tODOLists: ITODOList[]) =>
          this.tODOListService.addTODOListToCollectionIfMissing(tODOLists, this.editForm.get('todolist')!.value)
        )
      )
      .subscribe((tODOLists: ITODOList[]) => (this.tODOListsSharedCollection = tODOLists));
  }

  protected createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      dateButoire: this.editForm.get(['dateButoire'])!.value,
      repetition: this.editForm.get(['repetition'])!.value,
      dureeEstime: this.editForm.get(['dureeEstime'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
      url: this.editForm.get(['url'])!.value,
      description: this.editForm.get(['description'])!.value,
      affecteduser: this.editForm.get(['affecteduser'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      todolist: this.editForm.get(['todolist'])!.value,
    };
  }
}
