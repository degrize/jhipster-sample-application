import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDecision, getDecisionIdentifier } from '../decision.model';

export type EntityResponseType = HttpResponse<IDecision>;
export type EntityArrayResponseType = HttpResponse<IDecision[]>;

@Injectable({ providedIn: 'root' })
export class DecisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decisions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/decisions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(decision: IDecision): Observable<EntityResponseType> {
    return this.http.post<IDecision>(this.resourceUrl, decision, { observe: 'response' });
  }

  update(decision: IDecision): Observable<EntityResponseType> {
    return this.http.put<IDecision>(`${this.resourceUrl}/${getDecisionIdentifier(decision) as number}`, decision, { observe: 'response' });
  }

  partialUpdate(decision: IDecision): Observable<EntityResponseType> {
    return this.http.patch<IDecision>(`${this.resourceUrl}/${getDecisionIdentifier(decision) as number}`, decision, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDecision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDecision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDecision[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDecisionToCollectionIfMissing(decisionCollection: IDecision[], ...decisionsToCheck: (IDecision | null | undefined)[]): IDecision[] {
    const decisions: IDecision[] = decisionsToCheck.filter(isPresent);
    if (decisions.length > 0) {
      const decisionCollectionIdentifiers = decisionCollection.map(decisionItem => getDecisionIdentifier(decisionItem)!);
      const decisionsToAdd = decisions.filter(decisionItem => {
        const decisionIdentifier = getDecisionIdentifier(decisionItem);
        if (decisionIdentifier == null || decisionCollectionIdentifiers.includes(decisionIdentifier)) {
          return false;
        }
        decisionCollectionIdentifiers.push(decisionIdentifier);
        return true;
      });
      return [...decisionsToAdd, ...decisionCollection];
    }
    return decisionCollection;
  }
}
