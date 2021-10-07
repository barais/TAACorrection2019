import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITODOList, TODOList } from '../todo-list.model';
import { TODOListService } from '../service/todo-list.service';

@Injectable({ providedIn: 'root' })
export class TODOListRoutingResolveService implements Resolve<ITODOList> {
  constructor(protected service: TODOListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITODOList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tODOList: HttpResponse<TODOList>) => {
          if (tODOList.body) {
            return of(tODOList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TODOList());
  }
}
