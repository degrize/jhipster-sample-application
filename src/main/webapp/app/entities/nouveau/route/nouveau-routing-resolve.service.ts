import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INouveau, Nouveau } from '../nouveau.model';
import { NouveauService } from '../service/nouveau.service';

@Injectable({ providedIn: 'root' })
export class NouveauRoutingResolveService implements Resolve<INouveau> {
  constructor(protected service: NouveauService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INouveau> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nouveau: HttpResponse<Nouveau>) => {
          if (nouveau.body) {
            return of(nouveau.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Nouveau());
  }
}
