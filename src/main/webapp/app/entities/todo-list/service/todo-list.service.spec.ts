import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITODOList, TODOList } from '../todo-list.model';

import { TODOListService } from './todo-list.service';

describe('Service Tests', () => {
  describe('TODOList Service', () => {
    let service: TODOListService;
    let httpMock: HttpTestingController;
    let elemDefault: ITODOList;
    let expectedResult: ITODOList | ITODOList[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TODOListService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TODOList', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TODOList()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TODOList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TODOList', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
          },
          new TODOList()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TODOList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TODOList', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTODOListToCollectionIfMissing', () => {
        it('should add a TODOList to an empty array', () => {
          const tODOList: ITODOList = { id: 123 };
          expectedResult = service.addTODOListToCollectionIfMissing([], tODOList);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tODOList);
        });

        it('should not add a TODOList to an array that contains it', () => {
          const tODOList: ITODOList = { id: 123 };
          const tODOListCollection: ITODOList[] = [
            {
              ...tODOList,
            },
            { id: 456 },
          ];
          expectedResult = service.addTODOListToCollectionIfMissing(tODOListCollection, tODOList);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TODOList to an array that doesn't contain it", () => {
          const tODOList: ITODOList = { id: 123 };
          const tODOListCollection: ITODOList[] = [{ id: 456 }];
          expectedResult = service.addTODOListToCollectionIfMissing(tODOListCollection, tODOList);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tODOList);
        });

        it('should add only unique TODOList to an array', () => {
          const tODOListArray: ITODOList[] = [{ id: 123 }, { id: 456 }, { id: 61614 }];
          const tODOListCollection: ITODOList[] = [{ id: 123 }];
          expectedResult = service.addTODOListToCollectionIfMissing(tODOListCollection, ...tODOListArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tODOList: ITODOList = { id: 123 };
          const tODOList2: ITODOList = { id: 456 };
          expectedResult = service.addTODOListToCollectionIfMissing([], tODOList, tODOList2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tODOList);
          expect(expectedResult).toContain(tODOList2);
        });

        it('should accept null and undefined values', () => {
          const tODOList: ITODOList = { id: 123 };
          expectedResult = service.addTODOListToCollectionIfMissing([], null, tODOList, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tODOList);
        });

        it('should return initial array if no TODOList is added', () => {
          const tODOListCollection: ITODOList[] = [{ id: 123 }];
          expectedResult = service.addTODOListToCollectionIfMissing(tODOListCollection, undefined, null);
          expect(expectedResult).toEqual(tODOListCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
