jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TODOListService } from '../service/todo-list.service';
import { ITODOList, TODOList } from '../todo-list.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TODOListUpdateComponent } from './todo-list-update.component';

describe('Component Tests', () => {
  describe('TODOList Management Update Component', () => {
    let comp: TODOListUpdateComponent;
    let fixture: ComponentFixture<TODOListUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tODOListService: TODOListService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TODOListUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TODOListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TODOListUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tODOListService = TestBed.inject(TODOListService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const tODOList: ITODOList = { id: 456 };
        const user: IUser = { id: 77662 };
        tODOList.user = user;

        const userCollection: IUser[] = [{ id: 52395 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tODOList });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tODOList: ITODOList = { id: 456 };
        const user: IUser = { id: 64388 };
        tODOList.user = user;

        activatedRoute.data = of({ tODOList });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tODOList));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TODOList>>();
        const tODOList = { id: 123 };
        jest.spyOn(tODOListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tODOList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tODOList }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tODOListService.update).toHaveBeenCalledWith(tODOList);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TODOList>>();
        const tODOList = new TODOList();
        jest.spyOn(tODOListService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tODOList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tODOList }));
        saveSubject.complete();

        // THEN
        expect(tODOListService.create).toHaveBeenCalledWith(tODOList);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TODOList>>();
        const tODOList = { id: 123 };
        jest.spyOn(tODOListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tODOList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tODOListService.update).toHaveBeenCalledWith(tODOList);
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
    });
  });
});
