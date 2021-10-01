import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGuard, Guard } from '../guard.model';
import { GuardService } from '../service/guard.service';

@Injectable({ providedIn: 'root' })
export class GuardRoutingResolveService implements Resolve<IGuard> {
  constructor(protected service: GuardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((guard: HttpResponse<Guard>) => {
          if (guard.body) {
            return of(guard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Guard());
  }
}
