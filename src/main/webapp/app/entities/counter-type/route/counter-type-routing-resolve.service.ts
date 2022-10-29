import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounterType } from '../counter-type.model';
import { CounterTypeService } from '../service/counter-type.service';

@Injectable({ providedIn: 'root' })
export class CounterTypeRoutingResolveService implements Resolve<ICounterType | null> {
  constructor(protected service: CounterTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounterType | null | never> {
    const id = route.params['counterCode'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((counterType: HttpResponse<ICounterType>) => {
          if (counterType.body) {
            return of(counterType.body);
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
