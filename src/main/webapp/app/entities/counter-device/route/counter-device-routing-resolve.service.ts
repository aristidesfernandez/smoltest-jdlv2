import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounterDevice } from '../counter-device.model';
import { CounterDeviceService } from '../service/counter-device.service';

@Injectable({ providedIn: 'root' })
export class CounterDeviceRoutingResolveService implements Resolve<ICounterDevice | null> {
  constructor(protected service: CounterDeviceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounterDevice | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((counterDevice: HttpResponse<ICounterDevice>) => {
          if (counterDevice.body) {
            return of(counterDevice.body);
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
