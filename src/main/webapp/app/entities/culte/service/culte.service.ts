import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICulte, getCulteIdentifier } from '../culte.model';

export type EntityResponseType = HttpResponse<ICulte>;
export type EntityArrayResponseType = HttpResponse<ICulte[]>;

@Injectable({ providedIn: 'root' })
export class CulteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cultes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/cultes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(culte: ICulte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(culte);
    return this.http
      .post<ICulte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(culte: ICulte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(culte);
    return this.http
      .put<ICulte>(`${this.resourceUrl}/${getCulteIdentifier(culte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(culte: ICulte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(culte);
    return this.http
      .patch<ICulte>(`${this.resourceUrl}/${getCulteIdentifier(culte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICulte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICulte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICulte[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCulteToCollectionIfMissing(culteCollection: ICulte[], ...cultesToCheck: (ICulte | null | undefined)[]): ICulte[] {
    const cultes: ICulte[] = cultesToCheck.filter(isPresent);
    if (cultes.length > 0) {
      const culteCollectionIdentifiers = culteCollection.map(culteItem => getCulteIdentifier(culteItem)!);
      const cultesToAdd = cultes.filter(culteItem => {
        const culteIdentifier = getCulteIdentifier(culteItem);
        if (culteIdentifier == null || culteCollectionIdentifiers.includes(culteIdentifier)) {
          return false;
        }
        culteCollectionIdentifiers.push(culteIdentifier);
        return true;
      });
      return [...cultesToAdd, ...culteCollection];
    }
    return culteCollection;
  }

  protected convertDateFromClient(culte: ICulte): ICulte {
    return Object.assign({}, culte, {
      date: culte.date?.isValid() ? culte.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((culte: ICulte) => {
        culte.date = culte.date ? dayjs(culte.date) : undefined;
      });
    }
    return res;
  }
}
