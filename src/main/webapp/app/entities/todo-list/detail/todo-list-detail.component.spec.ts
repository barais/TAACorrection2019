import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TODOListDetailComponent } from './todo-list-detail.component';

describe('Component Tests', () => {
  describe('TODOList Management Detail Component', () => {
    let comp: TODOListDetailComponent;
    let fixture: ComponentFixture<TODOListDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TODOListDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tODOList: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TODOListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TODOListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tODOList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tODOList).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
