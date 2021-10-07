import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITODOList, getTODOListIdentifier } from '../todo-list.model';

export type EntityResponseType = HttpResponse<ITODOList>;
export type EntityArrayResponseType = HttpResponse<ITODOList[]>;

@Injectable({ providedIn: 'root' })
export class TODOListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/todo-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tODOList: ITODOList): Observable<EntityResponseType> {
    return this.http.post<ITODOList>(this.resourceUrl, tODOList, { observe: 'response' });
  }

  update(tODOList: ITODOList): Observable<EntityResponseType> {
    return this.http.put<ITODOList>(`${this.resourceUrl}/${getTODOListIdentifier(tODOList) as number}`, tODOList, { observe: 'response' });
  }

  partialUpdate(tODOList: ITODOList): Observable<EntityResponseType> {
    return this.http.patch<ITODOList>(`${this.resourceUrl}/${getTODOListIdentifier(tODOList) as number}`, tODOList, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITODOList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITODOList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTODOListToCollectionIfMissing(tODOListCollection: ITODOList[], ...tODOListsToCheck: (ITODOList | null | undefined)[]): ITODOList[] {
    const tODOLists: ITODOList[] = tODOListsToCheck.filter(isPresent);
    if (tODOLists.length > 0) {
      const tODOListCollectionIdentifiers = tODOListCollection.map(tODOListItem => getTODOListIdentifier(tODOListItem)!);
      const tODOListsToAdd = tODOLists.filter(tODOListItem => {
        const tODOListIdentifier = getTODOListIdentifier(tODOListItem);
        if (tODOListIdentifier == null || tODOListCollectionIdentifiers.includes(tODOListIdentifier)) {
          return false;
        }
        tODOListCollectionIdentifiers.push(tODOListIdentifier);
        return true;
      });
      return [...tODOListsToAdd, ...tODOListCollection];
    }
    return tODOListCollection;
  }
}
