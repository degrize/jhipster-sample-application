import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICulte, Culte } from '../culte.model';
import { CulteService } from '../service/culte.service';

@Injectable({ providedIn: 'root' })
export class CulteRoutingResolveService implements Resolve<ICulte> {
  constructor(protected service: CulteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICulte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((culte: HttpResponse<Culte>) => {
          if (culte.body) {
            return of(culte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Culte());
  }
}
