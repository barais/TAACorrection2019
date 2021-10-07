jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaskService } from '../service/task.service';
import { ITask, Task } from '../task.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { ITODOList } from 'app/entities/todo-list/todo-list.model';
import { TODOListService } from 'app/entities/todo-list/service/todo-list.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Component Tests', () => {
  describe('Task Management Update Component', () => {
    let comp: TaskUpdateComponent;
    let fixture: ComponentFixture<TaskUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taskService: TaskService;
    let userService: UserService;
    let tagService: TagService;
    let tODOListService: TODOListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaskUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taskService = TestBed.inject(TaskService);
      userService = TestBed.inject(UserService);
      tagService = TestBed.inject(TagService);
      tODOListService = TestBed.inject(TODOListService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const task: ITask = { id: 456 };
        const affecteduser: IUser = { id: 50386 };
        task.affecteduser = affecteduser;

        const userCollection: IUser[] = [{ id: 35938 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [affecteduser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tag query and add missing value', () => {
        const task: ITask = { id: 456 };
        const tags: ITag[] = [{ id: 18252 }];
        task.tags = tags;

        const tagCollection: ITag[] = [{ id: 55 }];
        jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
        const additionalTags = [...tags];
        const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
        jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(tagService.query).toHaveBeenCalled();
        expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags);
        expect(comp.tagsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TODOList query and add missing value', () => {
        const task: ITask = { id: 456 };
        const todolist: ITODOList = { id: 49506 };
        task.todolist = todolist;

        const tODOListCollection: ITODOList[] = [{ id: 86372 }];
        jest.spyOn(tODOListService, 'query').mockReturnValue(of(new HttpResponse({ body: tODOListCollection })));
        const additionalTODOLists = [todolist];
        const expectedCollection: ITODOList[] = [...additionalTODOLists, ...tODOListCollection];
        jest.spyOn(tODOListService, 'addTODOListToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(tODOListService.query).toHaveBeenCalled();
        expect(tODOListService.addTODOListToCollectionIfMissing).toHaveBeenCalledWith(tODOListCollection, ...additionalTODOLists);
        expect(comp.tODOListsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const task: ITask = { id: 456 };
        const affecteduser: IUser = { id: 81236 };
        task.affecteduser = affecteduser;
        const tags: ITag = { id: 7916 };
        task.tags = [tags];
        const todolist: ITODOList = { id: 61266 };
        task.todolist = todolist;

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(task));
        expect(comp.usersSharedCollection).toContain(affecteduser);
        expect(comp.tagsSharedCollection).toContain(tags);
        expect(comp.tODOListsSharedCollection).toContain(todolist);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = { id: 123 };
        jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taskService.update).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = new Task();
        jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(taskService.create).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Task>>();
        const task = { id: 123 };
        jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taskService.update).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTagById', () => {
        it('Should return tracked Tag primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTagById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTODOListById', () => {
        it('Should return tracked TODOList primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTODOListById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedTag', () => {
        it('Should return option if no Tag is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedTag(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Tag for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedTag(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Tag is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedTag(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
