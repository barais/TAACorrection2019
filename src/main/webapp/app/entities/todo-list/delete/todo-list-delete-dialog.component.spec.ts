jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TODOListService } from '../service/todo-list.service';

import { TODOListDeleteDialogComponent } from './todo-list-delete-dialog.component';

describe('Component Tests', () => {
  describe('TODOList Management Delete Component', () => {
    let comp: TODOListDeleteDialogComponent;
    let fixture: ComponentFixture<TODOListDeleteDialogComponent>;
    let service: TODOListService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TODOListDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(TODOListDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TODOListDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TODOListService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
