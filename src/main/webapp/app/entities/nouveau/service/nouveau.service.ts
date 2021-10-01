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
import { INouveau, getNouveauIdentifier } from '../nouveau.model';

export type EntityResponseType = HttpResponse<INouveau>;
export type EntityArrayResponseType = HttpResponse<INouveau[]>;

@Injectable({ providedIn: 'root' })
export class NouveauService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nouveaus');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/nouveaus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nouveau: INouveau): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nouveau);
    return this.http
      .post<INouveau>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nouveau: INouveau): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nouveau);
    return this.http
      .put<INouveau>(`${this.resourceUrl}/${getNouveauIdentifier(nouveau) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(nouveau: INouveau): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nouveau);
    return this.http
      .patch<INouveau>(`${this.resourceUrl}/${getNouveauIdentifier(nouveau) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INouveau>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INouveau[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INouveau[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addNouveauToCollectionIfMissing(nouveauCollection: INouveau[], ...nouveausToCheck: (INouveau | null | undefined)[]): INouveau[] {
    const nouveaus: INouveau[] = nouveausToCheck.filter(isPresent);
    if (nouveaus.length > 0) {
      const nouveauCollectionIdentifiers = nouveauCollection.map(nouveauItem => getNouveauIdentifier(nouveauItem)!);
      const nouveausToAdd = nouveaus.filter(nouveauItem => {
        const nouveauIdentifier = getNouveauIdentifier(nouveauItem);
        if (nouveauIdentifier == null || nouveauCollectionIdentifiers.includes(nouveauIdentifier)) {
          return false;
        }
        nouveauCollectionIdentifiers.push(nouveauIdentifier);
        return true;
      });
      return [...nouveausToAdd, ...nouveauCollection];
    }
    return nouveauCollection;
  }

  protected convertDateFromClient(nouveau: INouveau): INouveau {
    return Object.assign({}, nouveau, {
      date: nouveau.date?.isValid() ? nouveau.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((nouveau: INouveau) => {
        nouveau.date = nouveau.date ? dayjs(nouveau.date) : undefined;
      });
    }
    return res;
  }
}
