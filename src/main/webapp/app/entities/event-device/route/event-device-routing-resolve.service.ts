import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventDevice } from '../event-device.model';
import { EventDeviceService } from '../service/event-device.service';

@Injectable({ providedIn: 'root' })
export class EventDeviceRoutingResolveService implements Resolve<IEventDevice | null> {
  constructor(protected service: EventDeviceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventDevice | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventDevice: HttpResponse<IEventDevice>) => {
          if (eventDevice.body) {
            return of(eventDevice.body);
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
