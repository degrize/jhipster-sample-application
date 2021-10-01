import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageCulte, getImageCulteIdentifier } from '../image-culte.model';

export type EntityResponseType = HttpResponse<IImageCulte>;
export type EntityArrayResponseType = HttpResponse<IImageCulte[]>;

@Injectable({ providedIn: 'root' })
export class ImageCulteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-cultes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/image-cultes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imageCulte: IImageCulte): Observable<EntityResponseType> {
    return this.http.post<IImageCulte>(this.resourceUrl, imageCulte, { observe: 'response' });
  }

  update(imageCulte: IImageCulte): Observable<EntityResponseType> {
    return this.http.put<IImageCulte>(`${this.resourceUrl}/${getImageCulteIdentifier(imageCulte) as number}`, imageCulte, {
      observe: 'response',
    });
  }

  partialUpdate(imageCulte: IImageCulte): Observable<EntityResponseType> {
    return this.http.patch<IImageCulte>(`${this.resourceUrl}/${getImageCulteIdentifier(imageCulte) as number}`, imageCulte, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImageCulte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImageCulte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImageCulte[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addImageCulteToCollectionIfMissing(
    imageCulteCollection: IImageCulte[],
    ...imageCultesToCheck: (IImageCulte | null | undefined)[]
  ): IImageCulte[] {
    const imageCultes: IImageCulte[] = imageCultesToCheck.filter(isPresent);
    if (imageCultes.length > 0) {
      const imageCulteCollectionIdentifiers = imageCulteCollection.map(imageCulteItem => getImageCulteIdentifier(imageCulteItem)!);
      const imageCultesToAdd = imageCultes.filter(imageCulteItem => {
        const imageCulteIdentifier = getImageCulteIdentifier(imageCulteItem);
        if (imageCulteIdentifier == null || imageCulteCollectionIdentifiers.includes(imageCulteIdentifier)) {
          return false;
        }
        imageCulteCollectionIdentifiers.push(imageCulteIdentifier);
        return true;
      });
      return [...imageCultesToAdd, ...imageCulteCollection];
    }
    return imageCulteCollection;
  }
}
