import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IGem, getGemIdentifier } from '../gem.model';

export type EntityResponseType = HttpResponse<IGem>;
export type EntityArrayResponseType = HttpResponse<IGem[]>;

@Injectable({ providedIn: 'root' })
export class GemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gems');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/gems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gem: IGem): Observable<EntityResponseType> {
    return this.http.post<IGem>(this.resourceUrl, gem, { observe: 'response' });
  }

  update(gem: IGem): Observable<EntityResponseType> {
    return this.http.put<IGem>(`${this.resourceUrl}/${getGemIdentifier(gem) as number}`, gem, { observe: 'response' });
  }

  partialUpdate(gem: IGem): Observable<EntityResponseType> {
    return this.http.patch<IGem>(`${this.resourceUrl}/${getGemIdentifier(gem) as number}`, gem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addGemToCollectionIfMissing(gemCollection: IGem[], ...gemsToCheck: (IGem | null | undefined)[]): IGem[] {
    const gems: IGem[] = gemsToCheck.filter(isPresent);
    if (gems.length > 0) {
      const gemCollectionIdentifiers = gemCollection.map(gemItem => getGemIdentifier(gemItem)!);
      const gemsToAdd = gems.filter(gemItem => {
        const gemIdentifier = getGemIdentifier(gemItem);
        if (gemIdentifier == null || gemCollectionIdentifiers.includes(gemIdentifier)) {
          return false;
        }
        gemCollectionIdentifiers.push(gemIdentifier);
        return true;
      });
      return [...gemsToAdd, ...gemCollection];
    }
    return gemCollection;
  }
}
