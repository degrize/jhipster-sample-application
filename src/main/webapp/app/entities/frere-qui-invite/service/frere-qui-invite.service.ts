import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFrereQuiInvite, getFrereQuiInviteIdentifier } from '../frere-qui-invite.model';

export type EntityResponseType = HttpResponse<IFrereQuiInvite>;
export type EntityArrayResponseType = HttpResponse<IFrereQuiInvite[]>;

@Injectable({ providedIn: 'root' })
export class FrereQuiInviteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frere-qui-invites');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/frere-qui-invites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(frereQuiInvite: IFrereQuiInvite): Observable<EntityResponseType> {
    return this.http.post<IFrereQuiInvite>(this.resourceUrl, frereQuiInvite, { observe: 'response' });
  }

  update(frereQuiInvite: IFrereQuiInvite): Observable<EntityResponseType> {
    return this.http.put<IFrereQuiInvite>(`${this.resourceUrl}/${getFrereQuiInviteIdentifier(frereQuiInvite) as number}`, frereQuiInvite, {
      observe: 'response',
    });
  }

  partialUpdate(frereQuiInvite: IFrereQuiInvite): Observable<EntityResponseType> {
    return this.http.patch<IFrereQuiInvite>(
      `${this.resourceUrl}/${getFrereQuiInviteIdentifier(frereQuiInvite) as number}`,
      frereQuiInvite,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFrereQuiInvite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFrereQuiInvite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFrereQuiInvite[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFrereQuiInviteToCollectionIfMissing(
    frereQuiInviteCollection: IFrereQuiInvite[],
    ...frereQuiInvitesToCheck: (IFrereQuiInvite | null | undefined)[]
  ): IFrereQuiInvite[] {
    const frereQuiInvites: IFrereQuiInvite[] = frereQuiInvitesToCheck.filter(isPresent);
    if (frereQuiInvites.length > 0) {
      const frereQuiInviteCollectionIdentifiers = frereQuiInviteCollection.map(
        frereQuiInviteItem => getFrereQuiInviteIdentifier(frereQuiInviteItem)!
      );
      const frereQuiInvitesToAdd = frereQuiInvites.filter(frereQuiInviteItem => {
        const frereQuiInviteIdentifier = getFrereQuiInviteIdentifier(frereQuiInviteItem);
        if (frereQuiInviteIdentifier == null || frereQuiInviteCollectionIdentifiers.includes(frereQuiInviteIdentifier)) {
          return false;
        }
        frereQuiInviteCollectionIdentifiers.push(frereQuiInviteIdentifier);
        return true;
      });
      return [...frereQuiInvitesToAdd, ...frereQuiInviteCollection];
    }
    return frereQuiInviteCollection;
  }
}
