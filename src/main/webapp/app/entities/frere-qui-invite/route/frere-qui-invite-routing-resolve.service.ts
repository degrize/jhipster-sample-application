import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrereQuiInvite, FrereQuiInvite } from '../frere-qui-invite.model';
import { FrereQuiInviteService } from '../service/frere-qui-invite.service';

@Injectable({ providedIn: 'root' })
export class FrereQuiInviteRoutingResolveService implements Resolve<IFrereQuiInvite> {
  constructor(protected service: FrereQuiInviteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrereQuiInvite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frereQuiInvite: HttpResponse<FrereQuiInvite>) => {
          if (frereQuiInvite.body) {
            return of(frereQuiInvite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FrereQuiInvite());
  }
}
