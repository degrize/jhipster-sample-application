import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageCulte, ImageCulte } from '../image-culte.model';
import { ImageCulteService } from '../service/image-culte.service';

@Injectable({ providedIn: 'root' })
export class ImageCulteRoutingResolveService implements Resolve<IImageCulte> {
  constructor(protected service: ImageCulteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImageCulte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imageCulte: HttpResponse<ImageCulte>) => {
          if (imageCulte.body) {
            return of(imageCulte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ImageCulte());
  }
}
