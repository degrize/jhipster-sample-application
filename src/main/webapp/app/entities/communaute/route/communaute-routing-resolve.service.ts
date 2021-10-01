import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunaute, Communaute } from '../communaute.model';
import { CommunauteService } from '../service/communaute.service';

@Injectable({ providedIn: 'root' })
export class CommunauteRoutingResolveService implements Resolve<ICommunaute> {
  constructor(protected service: CommunauteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunaute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((communaute: HttpResponse<Communaute>) => {
          if (communaute.body) {
            return of(communaute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Communaute());
  }
}
