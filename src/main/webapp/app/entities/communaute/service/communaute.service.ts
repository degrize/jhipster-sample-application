import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICommunaute, getCommunauteIdentifier } from '../communaute.model';

export type EntityResponseType = HttpResponse<ICommunaute>;
export type EntityArrayResponseType = HttpResponse<ICommunaute[]>;

@Injectable({ providedIn: 'root' })
export class CommunauteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/communautes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/communautes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(communaute: ICommunaute): Observable<EntityResponseType> {
    return this.http.post<ICommunaute>(this.resourceUrl, communaute, { observe: 'response' });
  }

  update(communaute: ICommunaute): Observable<EntityResponseType> {
    return this.http.put<ICommunaute>(`${this.resourceUrl}/${getCommunauteIdentifier(communaute) as number}`, communaute, {
      observe: 'response',
    });
  }

  partialUpdate(communaute: ICommunaute): Observable<EntityResponseType> {
    return this.http.patch<ICommunaute>(`${this.resourceUrl}/${getCommunauteIdentifier(communaute) as number}`, communaute, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommunaute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommunaute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommunaute[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCommunauteToCollectionIfMissing(
    communauteCollection: ICommunaute[],
    ...communautesToCheck: (ICommunaute | null | undefined)[]
  ): ICommunaute[] {
    const communautes: ICommunaute[] = communautesToCheck.filter(isPresent);
    if (communautes.length > 0) {
      const communauteCollectionIdentifiers = communauteCollection.map(communauteItem => getCommunauteIdentifier(communauteItem)!);
      const communautesToAdd = communautes.filter(communauteItem => {
        const communauteIdentifier = getCommunauteIdentifier(communauteItem);
        if (communauteIdentifier == null || communauteCollectionIdentifiers.includes(communauteIdentifier)) {
          return false;
        }
        communauteCollectionIdentifiers.push(communauteIdentifier);
        return true;
      });
      return [...communautesToAdd, ...communauteCollection];
    }
    return communauteCollection;
  }
}
