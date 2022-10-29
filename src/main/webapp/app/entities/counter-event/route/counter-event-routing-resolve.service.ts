import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounterEvent } from '../counter-event.model';
import { CounterEventService } from '../service/counter-event.service';

@Injectable({ providedIn: 'root' })
export class CounterEventRoutingResolveService implements Resolve<ICounterEvent | null> {
  constructor(protected service: CounterEventService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounterEvent | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((counterEvent: HttpResponse<ICounterEvent>) => {
          if (counterEvent.body) {
            return of(counterEvent.body);
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
