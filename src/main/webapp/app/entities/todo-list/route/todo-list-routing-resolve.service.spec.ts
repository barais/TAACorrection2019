jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITODOList, TODOList } from '../todo-list.model';
import { TODOListService } from '../service/todo-list.service';

import { TODOListRoutingResolveService } from './todo-list-routing-resolve.service';

describe('Service Tests', () => {
  describe('TODOList routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TODOListRoutingResolveService;
    let service: TODOListService;
    let resultTODOList: ITODOList | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TODOListRoutingResolveService);
      service = TestBed.inject(TODOListService);
      resultTODOList = undefined;
    });

    describe('resolve', () => {
      it('should return ITODOList returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTODOList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTODOList).toEqual({ id: 123 });
      });

      it('should return new ITODOList if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTODOList = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTODOList).toEqual(new TODOList());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TODOList })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTODOList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTODOList).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
