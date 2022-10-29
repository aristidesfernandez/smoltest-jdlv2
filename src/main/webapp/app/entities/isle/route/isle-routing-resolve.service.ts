import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsle } from '../isle.model';
import { IsleService } from '../service/isle.service';

@Injectable({ providedIn: 'root' })
export class IsleRoutingResolveService implements Resolve<IIsle | null> {
  constructor(protected service: IsleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsle | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isle: HttpResponse<IIsle>) => {
          if (isle.body) {
            return of(isle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
