import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGem, Gem } from '../gem.model';
import { GemService } from '../service/gem.service';

@Injectable({ providedIn: 'root' })
export class GemRoutingResolveService implements Resolve<IGem> {
  constructor(protected service: GemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gem: HttpResponse<Gem>) => {
          if (gem.body) {
            return of(gem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gem());
  }
}
