import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IGuard, getGuardIdentifier } from '../guard.model';

export type EntityResponseType = HttpResponse<IGuard>;
export type EntityArrayResponseType = HttpResponse<IGuard[]>;

@Injectable({ providedIn: 'root' })
export class GuardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/guards');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/guards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(guard: IGuard): Observable<EntityResponseType> {
    return this.http.post<IGuard>(this.resourceUrl, guard, { observe: 'response' });
  }

  update(guard: IGuard): Observable<EntityResponseType> {
    return this.http.put<IGuard>(`${this.resourceUrl}/${getGuardIdentifier(guard) as number}`, guard, { observe: 'response' });
  }

  partialUpdate(guard: IGuard): Observable<EntityResponseType> {
    return this.http.patch<IGuard>(`${this.resourceUrl}/${getGuardIdentifier(guard) as number}`, guard, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuard[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addGuardToCollectionIfMissing(guardCollection: IGuard[], ...guardsToCheck: (IGuard | null | undefined)[]): IGuard[] {
    const guards: IGuard[] = guardsToCheck.filter(isPresent);
    if (guards.length > 0) {
      const guardCollectionIdentifiers = guardCollection.map(guardItem => getGuardIdentifier(guardItem)!);
      const guardsToAdd = guards.filter(guardItem => {
        const guardIdentifier = getGuardIdentifier(guardItem);
        if (guardIdentifier == null || guardCollectionIdentifiers.includes(guardIdentifier)) {
          return false;
        }
        guardCollectionIdentifiers.push(guardIdentifier);
        return true;
      });
      return [...guardsToAdd, ...guardCollection];
    }
    return guardCollection;
  }
}
